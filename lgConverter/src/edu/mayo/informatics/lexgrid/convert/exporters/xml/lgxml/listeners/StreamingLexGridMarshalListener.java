
package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.listeners;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

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
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.EntryState;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.MarshalListener;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;

import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.constants.LexGridConstants;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.interfaces.AssociationEntityCache;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.interfaces.AssociationSourceCache;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.util.AssociationEntityCacheFactory;
import edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.util.AssociationSourceCacheFactory;

public class StreamingLexGridMarshalListener implements MarshalListener {
    private Marshaller marshaller;
    private CodedNodeSet cns;
    private CodedNodeGraph cng;
    // private CodedNodeGraph associationCng;
    private String curAssociationName, curRelations;
    private AssociationSourceCache sourceCache;
    private AssociationEntityCache associationEntityCache = AssociationEntityCacheFactory.createCache();
    private LgMessageDirectorIF messager;
    private long entityCount;
    private long associationCount;
    private long associationEntityCount;
    

    private final int MAX_BLOCK_SIZE = 10;
    private int blockSize = 10;

    public StreamingLexGridMarshalListener(Marshaller marshaller, CodedNodeGraph cng, CodedNodeSet cns, int pageSize,
            LgMessageDirectorIF messager) {
        this.setBlockSize(pageSize);
        this.marshaller = marshaller;
        this.cng = cng;
        this.cns = cns;
        this.messager = messager;
        this.entityCount = 0;
        this.associationCount = 0;
        this.associationEntityCount = 0;
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
        sourceCache = AssociationSourceCacheFactory.createCache();
        return true;
    }

    private boolean preMarshalRelations(Object obj) {
        Relations relations = (Relations) obj;
        curRelations = relations.getContainerName();
        return true;
    }

    private boolean preMarshalAssociationSource(Object obj) {
        AssociationSource as = (AssociationSource) obj;
        if (as.getSourceEntityCode().equals(LexGridConstants.MR_FLAG)) {
            this.marshaller.setRootElement("source"); // under the
            // associationpredicate,
            // there are a "source"
            // list of
            // AssociationSource.
            if (cng != null) {
                messager.info("starting process association source ..." + this.curAssociationName + "...");
                try {
                    ResolvedConceptReferenceList rcrl = cng.resolveAsList(null, true, false, 0, -1, null, null, null,
                            null, -1);
                    Iterator<ResolvedConceptReference> blockIterator;
                    ResolvedConceptReference curConRef;
                    if (rcrl != null) {
                        blockIterator = (Iterator<ResolvedConceptReference>) rcrl.iterateResolvedConceptReference();
                        while (blockIterator.hasNext()) {
                            curConRef = (ResolvedConceptReference) blockIterator.next();
                         //   long targetStart = System.nanoTime();
                            processTargets(curConRef);
                         //   long targetEnd = System.nanoTime();
                        //    System.out.println("process targets time: " +  (targetEnd - targetStart));
                         //   long assocStart = System.nanoTime();
                            processAssociationList(curConRef.getSourceOf());
                          //  long assocEnd = System.nanoTime();
                          //  System.out.println("process assoc time: " +  (assocEnd - assocStart));
                        }
                    }
                    messager.info("association processing complete. marshalled " + this.associationCount
                            + " association objects");
                } catch (LBInvocationException e) {
                    messager.error(e.toString());
                } catch (LBParameterException e) {
                    messager.error(e.toString());
                } catch (MarshalException e) {
                    messager.error(e.toString());
                } catch (ValidationException e) {
                    messager.error(e.toString());
                }
                messager.info("..... done");
                return false;
            }
            return false; // prevent the MR_FLAG from being marshaled when no CNG is present 
        }
        return true;
    }

    private boolean preMarshalEntity(Object obj) {
        messager.info("start processing entities...");

        String mappingFileName = "lexevs_export_mapping.xml";

        if (((Entity) obj).getEntityCode().equals(LexGridConstants.MR_FLAG)) {
            if (cns != null) {
                try {
                    ResolvedConceptReferencesIterator rcri_iterator = cns.resolve(null, null, null, null, true);
                    while (rcri_iterator.hasNext()) {
                        ResolvedConceptReference curConRef = rcri_iterator.next();
                        Entity curEntity = (Entity) curConRef.getEntity();

                        if (curEntity == null)
                            continue;

                        if ((curEntity.getIsAnonymous() != null) && (curEntity.getIsAnonymous().booleanValue()))
                            continue;

                        if (curEntity.getEntityCode().startsWith("@"))
                            continue;

                        if (curEntity instanceof AssociationEntity) {
                            this.associationEntityCache.put((AssociationEntity) curEntity);
                            ++this.associationEntityCount;
                            messager.info("adding association entity to cache.  total of " + this.associationEntityCount + " objects added to cache so far");
                            
                        } else {
                            this.marshaller.marshal(curEntity);
                            ++this.entityCount;
                            if (this.entityCount % 1000 == 0) {
                                messager.info("processed " + this.entityCount + " entity objects");
                            }
                        }
                    }
                    
                    rcri_iterator.release();
                    messager.info("Entity processing complete. " + this.entityCount + " entity objects marshalled");

                    // now marshal the AssociationEntity
                    messager.info("start processing association entities...");
                    List<String> keys = this.associationEntityCache.getKeys();
                    if (keys.size() > 0) {
                        // load the mapping for association entity
                        try {
                            Mapping mapping = new Mapping();
                            messager.info("attempting to load " + mappingFileName + "...");
                            InputStream inputStream = StreamingLexGridMarshalListener.class
                                    .getResourceAsStream("/"
                                            + mappingFileName);
                            InputSource inputSource = new InputSource(inputStream);
                            mapping.loadMapping(inputSource);
                            this.marshaller.setMapping(mapping);
                            messager.info(mappingFileName + " loaded successfully");
                        } catch (MappingException e) {
                            messager.error(e.toString());
                        }
                        String key;
                        AssociationEntity aE;
                        for (int i = 0; i < keys.size(); ++i) {
                            key = keys.get(i);
                            aE = this.associationEntityCache.get(key);
                            this.marshaller.marshal(aE);
                        }
                        messager.info("AssociationEntity object processing complete.");
                    }
                } catch (LBInvocationException e) {
                    messager.error(e.toString());
                } catch (LBParameterException e) {
                    messager.error(e.toString());
                } catch (LBResourceUnavailableException e) {
                    messager.error(e.toString());
                } catch (MarshalException e) {
                    messager.error(e.toString());
                } catch (ValidationException e) {
                    messager.error(e.toString());
                }
            }

            return false;
        }

        return true;
    }

    @Override
    public void postMarshal(Object arg0) {
        if (org.LexGrid.codingSchemes.CodingScheme.class.equals(arg0.getClass())) {
            messager.info("Done marshalling CodingScheme. Clean up caches.");
            if (this.sourceCache != null)
                this.sourceCache.destroy();
            if (this.associationEntityCache != null)
                this.associationEntityCache.destroy();
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
        } else if (EntryState.class.equals(arg0.getClass()) == true) { // remove
            // entry
            // state
            // for
            // now.
            // it
            // needs
            // to be
            // considered
            // in
            // revisions
            return false;
        } else if (Relations.class.equals(arg0.getClass()) == true) {
            this.preMarshalRelations(arg0);
        } else {
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

        Iterator<?> associationIterator = _asl.iterateAssociation();

        while (associationIterator.hasNext()) {
            Association association = (Association) associationIterator.next();
            if (association.getAssociationName().equals(this.curAssociationName) == false
                    || association.getRelationsContainerName().equals(this.curRelations) == false)
                continue;

            // get the source
            if (association.getAssociatedConcepts().getAssociatedConceptCount() > 0) {

                Iterator associatedConceptsIterator = association.getAssociatedConcepts().iterateAssociatedConcept();
                while (associatedConceptsIterator.hasNext()) {
                    AssociatedConcept source = (AssociatedConcept) associatedConceptsIterator.next();

                    ConceptReference focus = new ConceptReference();
                    focus.setCode(source.getConceptCode());
                    focus.setCodeNamespace(source.getCodeNamespace());
                    ResolvedConceptReferenceList localRcrl = null;
                    try {
                        localRcrl = this.cng.resolveAsList(focus, true, false, 0, -1, null, null, null, null, -1);
                    } catch (LBInvocationException e) {
                        messager.error(e.toString());
                    } catch (LBParameterException e) {
                        messager.error(e.toString());
                    }
                    ResolvedConceptReference sourceRef = null;
                    if (localRcrl != null && localRcrl.getResolvedConceptReferenceCount() > 0) {
                        Iterator<ResolvedConceptReference> innterIterator = (Iterator<ResolvedConceptReference>) localRcrl
                                .iterateResolvedConceptReference();
                        sourceRef = (ResolvedConceptReference) innterIterator.next();
                    }

                    if (sourceRef == null || this.sourceExist(sourceRef)) {
                        continue;
                    }

                    AssociationList targets = sourceRef.getSourceOf();
                    processTargets(sourceRef);

                    if ((targets != null) && (targets.getAssociationCount() > 0)) {
                        try {
                            processAssociationList(targets);
                        } catch (Exception e) {
                            messager.info(e.toString());
                        }
                    }
                }
            }
        }
    }

    private boolean sourceExist(ResolvedConceptReference rcr) {
        boolean rv = this.sourceCache.exists(rcr);
        return rv;
    }

    private void processTargets(ResolvedConceptReference sRef) throws MarshalException,
            ValidationException {
        AssociationList targets = sRef.getSourceOf();
        if ((targets != null) && (targets.getAssociationCount() > 0)) {

            Iterator<?> targetsIterator = targets.iterateAssociation();
            while (targetsIterator.hasNext()) {
                Association targetAssociation = (Association) targetsIterator.next();
                if (targetAssociation.getAssociationName().equals(curAssociationName)
                        && targetAssociation.getRelationsContainerName().equals(curRelations)) {
                    Iterator<?> associatedTargetsIterator = targetAssociation.getAssociatedConcepts()
                            .iterateAssociatedConcept();
                    while (associatedTargetsIterator.hasNext()) {
                        AssociatedConcept target = (AssociatedConcept) associatedTargetsIterator.next();

                        if (target == null)
                            continue;

                        AssociationTarget associationTarget = new AssociationTarget();
                        associationTarget.setTargetEntityCodeNamespace(target.getCodeNamespace());
                        associationTarget.setTargetEntityCode(target.getConceptCode());

                        AssociationSource aS = new AssociationSource();
                        aS.setSourceEntityCodeNamespace(sRef.getCodeNamespace());
                        aS.setSourceEntityCode(sRef.getConceptCode());

                        aS.addTarget(associationTarget);
                        NameAndValueList assocQuals = target.getAssociationQualifiers();
                        if ((assocQuals != null) && (assocQuals.getNameAndValueCount() > 0)) {
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

                        this.sourceCache.add(aS);
                        this.marshaller.marshal(aS);
                        ++this.associationCount;
                        if (this.associationCount % 1000 == 0) {
                            messager.info("processed " + this.associationCount + " association objects");
                        }
                    }
                }
            }
        }
    }
}