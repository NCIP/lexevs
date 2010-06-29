package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.listeners;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.MarshalListener;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.lexevs.dao.database.ibatis.entity.model.IdableAssociationEntity;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.constants.LexGridConstants;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.interfaces.AssociationSourceCache;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.util.AssociationSourceCacheFactory;

public class StreamingLexGridMarshalListener implements MarshalListener {
    Marshaller marshaller;
    CodedNodeSet cns;
    CodedNodeGraph cng;
    String curAssociationName;
    ResolvedConceptReferencesIterator rcri_iterator;
    ResolvedConceptReferenceList refList;
    Iterator<ResolvedConceptReference> blockIterator;
    ResolvedConceptReference curConRef;
    Entity curEntity;
    // List<AssociationSource> sourceList = new ArrayList<AssociationSource>();
    AssociationSourceCache sourceCache = AssociationSourceCacheFactory.createCache(); 

    AssociationPredicate userAP = null;
    String stopToken = "$$TEST$$";
    public static String codingSchemeName = null;

    public static Vector<String> processed = new Vector<String>();

    private final int MAX_BLOCK_SIZE = 10;
    private int blockSize = 10;
    private int entityIndex = 0;
    private int entitiesToReturn = -1; // limit our output to the file just to
                                       // keep it small

    public int getMaxEntitiesToReturn() {
        return entitiesToReturn;
    }

    public void setMaxEntitiesToReturn(int limit) {
        this.entitiesToReturn = limit;
    }

    public StreamingLexGridMarshalListener(Marshaller marshaller, CodedNodeSet cns, int pageSize) {
        this.setBlockSize(pageSize);
        this.marshaller = marshaller;
        this.cns = cns;
    }

    public StreamingLexGridMarshalListener(Marshaller marshaller, CodedNodeGraph cng, int pageSize) {
        this.setBlockSize(pageSize);
        this.marshaller = marshaller;
        this.cng = cng;
    }

    public StreamingLexGridMarshalListener(Marshaller marshaller, CodedNodeGraph cng, CodedNodeSet cns, int pageSize) {
        this.setBlockSize(pageSize);
        this.marshaller = marshaller;
        this.cng = cng;
        this.cns = cns;
    }

    public StreamingLexGridMarshalListener(Marshaller marshaller, CodedNodeGraph cng, CodedNodeSet cns, int pageSize,
            int limit) {
        this.setBlockSize(pageSize);
        this.entitiesToReturn = limit;
        this.marshaller = marshaller;
        this.cng = cng;
        this.cns = cns;
    }

    private void setBlockSize(int size) {
        if (size > MAX_BLOCK_SIZE || size < 1) {
            blockSize = MAX_BLOCK_SIZE;
        } else {
            blockSize = size;
        }
    }

    private boolean preMarshalAssociationPredicate(Object obj) {
        AssociationPredicate ap = (AssociationPredicate) obj;
        curAssociationName = ap.getAssociationName();
        return true;
    }

    private boolean preMarshalAssociationSource(Object obj) {
        AssociationSource as = (AssociationSource) obj;
        if (as.getSourceEntityCode().equals(LexGridConstants.MR_FLAG)) {
            System.out.println("Found a MR_FLAG in a AssociationSource");
            if (cng != null) {
                try {
                    ResolvedConceptReferenceList rcrl = cng.resolveAsList(null, true, false, 0, -1, null, null, null,
                            null, -1);
                    if (rcrl != null && rcrl.getResolvedConceptReferenceCount() > 0) {
                        blockIterator = (Iterator<ResolvedConceptReference>) rcrl.iterateResolvedConceptReference();
                        while (blockIterator.hasNext()) {
                            curConRef = (ResolvedConceptReference) blockIterator.next();
                            AssociationList asl = curConRef.getSourceOf();
                            processTargets(curConRef, curAssociationName);
                            processAssociationList(asl);
                        }
                    }
                    else{ // there is no association found, try to find the association source 
                        CodedNodeGraph restrictCng = cng.restrictToSourceCodes(cns);
                        rcrl = restrictCng.resolveAsList(null, true, false, 0, -1, null, null, null,
                                null, -1);
                        if (rcrl != null)
                            blockIterator = (Iterator<ResolvedConceptReference>) rcrl.iterateResolvedConceptReference();
                        while (blockIterator.hasNext()) {
                            curConRef = (ResolvedConceptReference) blockIterator.next();
                            if (this.sourceExist(curConRef) == false) {
                                AssociationList asl = curConRef.getSourceOf();
                                processTargets(curConRef, curAssociationName);
                                processAssociationList(asl);
                            }
                        }
                    }
                } catch (LBInvocationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (LBParameterException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (MarshalException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ValidationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return false;
            }
        }
        return true;
    }

    private boolean preMarshalEntity(Object obj) {
        if ((entitiesToReturn > 0) && (entityIndex >= entitiesToReturn)) {
            return false;
        }

        if (((Entity) obj).getEntityCode().equals(LexGridConstants.MR_FLAG)) {
            // get groups of Entity objects using CNS.
            if (cns != null) {
                try {
                    rcri_iterator = cns.resolve(null, null, null, null, true);
                    List<AssociationEntity> associationEntityList = new ArrayList<AssociationEntity>();
                    // will a LBabcException break us out?
                    while (rcri_iterator.hasNext()) {
                        refList = rcri_iterator.next(blockSize);
                        blockIterator = (Iterator<ResolvedConceptReference>) refList.iterateResolvedConceptReference();
                        while (blockIterator.hasNext()) {
                            curConRef = (ResolvedConceptReference) blockIterator.next();
                            curEntity = (Entity) curConRef.getEntity();

                            if (curEntity == null) {
//                                System.out.println("*************  curEntity is null");
                                continue;
                            } else {
//                                System.out.println("************* curEntity is \"" + curEntity.getEntityCode() + "\"");
                            }

                            if ((curEntity.getIsAnonymous() != null) && (curEntity.getIsAnonymous().booleanValue()))
                                continue;

                            if (curEntity.getEntityCode().startsWith("@"))
                                continue;

//                            System.out.println("******************  Marshalling Entity=" + curEntity.getEntityCode());

                            if (curEntity instanceof AssociationEntity) {
                                associationEntityList.add((AssociationEntity) curEntity);
                            } else {
                                this.marshaller.marshal(curEntity);
                                ++entityIndex;
                            }
                        }
                    }

                    //load the mapping for association entity
                    try {
                        Mapping mapping = new Mapping();
                        mapping.loadMapping("file:///"+StreamingLexGridMarshalListener.class.getResource("./").getPath()+"mapping.xml");
                        this.marshaller.setMapping(mapping);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (MappingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }    
                    // now marshal the AssociationEntity
                    for (AssociationEntity associationEntity : associationEntityList) {
                        this.marshaller.marshal(associationEntity);
                        ++entityIndex;
                    }

                } catch (LBInvocationException e) {
                    e.printStackTrace();
                } catch (LBParameterException e) {
                    e.printStackTrace();
                } catch (LBResourceUnavailableException e) {
                    e.printStackTrace();
                } catch (MarshalException e) {
                    e.printStackTrace();
                } catch (ValidationException e) {
                    e.printStackTrace();
                }
            }

            return false;
        }

        return true;
    }

    @Override
    public void postMarshal(Object arg0) {
        if (Entities.class.equals(arg0.getClass()) == true) {
            // System.out.println("POST: found an Entities object");
        }

    }

    @Override
    public boolean preMarshal(Object arg0) {
        if (AssociationPredicate.class.equals(arg0.getClass())) {
            return this.preMarshalAssociationPredicate(arg0);
        } else if (AssociationSource.class.equals(arg0.getClass()) == true) {
            return this.preMarshalAssociationSource(arg0);
        } else if ((Entity.class.equals(arg0.getClass()) == true)) {
            return this.preMarshalEntity(arg0);
        }

        else {
            if (Relations.class.equals(arg0.getClass()) && cng == null) {
                return false;
            }
        }

        return true;
    }

    private void processAssociationList(AssociationList _asl) throws MarshalException, ValidationException {
        if (_asl == null) {
            return;
        }

        String localCurAssociationName = this.curAssociationName;

        if (localCurAssociationName == null) {
            return;
        }

        AssociationSource associationSource = null;
        Iterator<?> associationIterator = _asl.iterateAssociation();

        while (associationIterator.hasNext()) {
            Association association = (Association) associationIterator.next();
            // System.out.println("\tProcessing Association=" +
            // association.getAssociationName());

            // get the source
            int associatedConcepts = association.getAssociatedConcepts().getAssociatedConceptCount();
            // System.out.println("\tAssociated Concepts=" +
            // associatedConcepts);

            if (associatedConcepts > 0) {
                Vector<AssociationSource> associationSourceV = new Vector<AssociationSource>();

                Iterator associatedConceptsIterator = association.getAssociatedConcepts().iterateAssociatedConcept();
                while (associatedConceptsIterator.hasNext()) {
                    AssociatedConcept source = (AssociatedConcept) associatedConceptsIterator.next();
                    // System.out.println("\tProcessing AssociatedConcept (source):"
                    // + source.getConceptCode());

                    if (codingSchemeName == null)
                        codingSchemeName = source.getCodingSchemeName();

                    // ConceptReference focus =
                    // Constructors.createConceptReference(source.getConceptCode(),
                    // codingSchemeName);
                    ConceptReference focus = new ConceptReference();
                    focus.setCode(source.getConceptCode());
                    focus.setCodingSchemeName(codingSchemeName);
                    ResolvedConceptReferenceList localRcrl = null;
                    try {
                        // System.out.println("Focus=" + focus);
                        localRcrl = cng.resolveAsList(focus, true, false, 0, -1, null, null, null, null, -1);
                    } catch (LBInvocationException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (LBParameterException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    ResolvedConceptReference sourceRef = null;
                    if (localRcrl != null) {
                        Iterator<ResolvedConceptReference> innterIterator = (Iterator<ResolvedConceptReference>) localRcrl
                                .iterateResolvedConceptReference();
                        sourceRef = (ResolvedConceptReference) innterIterator.next();
                    }

                    if (sourceRef == null || this.sourceExist(sourceRef)) {
                        // System.out.println("Failed to get Source Ref for " +
                        // source.getConceptCode());
                        continue;
                    }

                    AssociationList targets = sourceRef.getSourceOf();
                    processTargets(sourceRef, localCurAssociationName);

                    if ((targets != null) && (targets.getAssociationCount() > 0)) {
                        // System.out.println("\n--------> CALLING AGAIN targets ="
                        // + targets.getAssociation().length + " --------->\n");
                        processAssociationList(targets);
                    }
                }
            }
        }
    }

    private boolean sourceExist(ResolvedConceptReference rcr) {
        boolean rv = this.sourceCache.exists(rcr);
        return rv;

/*
        for (AssociationSource source : sourceList) {
            if (source.getSourceEntityCode().equalsIgnoreCase(rcr.getCode())
                    && source.getSourceEntityCodeNamespace().equalsIgnoreCase(rcr.getCodeNamespace())) {
                return true;
            }
        }        
        return false;
*/        
    }

    private void processTargets(ResolvedConceptReference sRef, String asName) throws MarshalException,
            ValidationException {
        // Vector<AssociationSource> aV = new Vector<AssociationSource>();
        // AssociationSource aS = null;
        AssociationList targets = sRef.getSourceOf();
        if ((targets != null) && (targets.getAssociationCount() > 0)) {
            // aS = new AssociationSource();
            // aS.setSourceEntityCodeNamespace(sRef.getCodeNamespace());
            // aS.setSourceEntityCode(sRef.getConceptCode());

            Iterator<?> targetsIterator = targets.iterateAssociation();
            boolean targetsFound = false;
            while (targetsIterator.hasNext()) {
                Association targetAssociation = (Association) targetsIterator.next();
                Iterator<?> associatedTargetsIterator = targetAssociation.getAssociatedConcepts()
                        .iterateAssociatedConcept();
                while (associatedTargetsIterator.hasNext()) {
                    AssociatedConcept target = (AssociatedConcept) associatedTargetsIterator.next();

                    if (target == null)
                        continue;

                    AssociationTarget associationTarget = new AssociationTarget();
                    associationTarget.setTargetEntityCodeNamespace(target.getCodeNamespace());
                    associationTarget.setTargetEntityCode(target.getConceptCode());
                    // System.out.println("\t\t" + target.getConceptCode() +
                    // " with " + targetAssociation.getAssociationName());
                    if (targetAssociation.getAssociationName().equals(asName)) {
                        AssociationSource aS = new AssociationSource();
                        aS.setSourceEntityCodeNamespace(sRef.getCodeNamespace());
                        aS.setSourceEntityCode(sRef.getConceptCode());

                        targetsFound = true;
                        aS.addTarget(associationTarget);
                        // System.out.println("\t\t\tAdding Target:" +
                        // associationTarget.getTargetEntityCode());
                        NameAndValueList assocQuals = target.getAssociationQualifiers();
                        if ((assocQuals != null) && (assocQuals.getNameAndValueCount() > 0)) {
                            // System.out.println("Processing Association Qualifiers now...");
                            Iterator<?> associatedQualItr = assocQuals.iterateNameAndValue();
                            while (associatedQualItr.hasNext()) {
                                NameAndValue nv = (NameAndValue) associatedQualItr.next();

                                AssociationQualification qlf = new AssociationQualification();
                                qlf.setAssociationQualifier(nv.getName());
                                Text v = new Text();
                                v.setContent(nv.getContent());
                                qlf.setQualifierText(v);
                                associationTarget.addAssociationQualification(qlf);
                            }
                        }
//                        this.marshaller.setRootElement("source");
                        
                        try {
                            Mapping mapping = new Mapping();
                            mapping.loadMapping("file:///"+StreamingLexGridMarshalListener.class.getResource("./").getPath()+"mapping.xml");
                            this.marshaller.setMapping(mapping);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (MappingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }    
                        
                        // sourceList.add(aS);
                        this.sourceCache.add(aS);
                        this.marshaller.marshal(aS);
                    }
                }
            }

            // if (targetsFound)
            // aV.add(aS);
        }

        // if(aV.size() > 0)
        // {
        // for (int vi=0; vi < aV.size();vi++)
        // {
        // System.out.println("\t Source[" + vi + "] ADDED TO PREDICATE");
        // ap.addSource(aV.elementAt(vi));
        // this.marshaller.marshal(aV.elementAt(vi));
        // }
        // aV = new Vector<AssociationSource>();
        // }
    }
}
