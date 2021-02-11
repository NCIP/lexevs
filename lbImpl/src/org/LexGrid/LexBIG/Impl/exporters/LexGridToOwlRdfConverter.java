
package org.LexGrid.LexBIG.Impl.exporters;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExportStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Load.OntologyFormat;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.exporters.ExporterMessageDirector;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.types.EntityTypes;
import org.LexGrid.concepts.Comment;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.concepts.PropertyLink;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedAssociationQualifier;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.SupportedProperty;
import org.LexGrid.relations.AssociationEntity;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.exporter.owlrdf.LexRdf;
import org.lexgrid.exporter.owlrdf.LexRdfConstants;
import org.lexgrid.exporter.owlrdf.LexRdfMap;
import org.lexgrid.exporter.owlrdf.Skos;
import org.lexgrid.exporter.owlrdf.StringHelper;

import com.hp.hpl.jena.ontology.AnnotationProperty;
import com.hp.hpl.jena.ontology.ComplementClass;
import com.hp.hpl.jena.ontology.IntersectionClass;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.ontology.Ontology;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.ontology.UnionClass;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.ReifiedStatement;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.sdb.StoreDesc;
import com.hp.hpl.jena.sdb.sql.SDBConnection;
import com.hp.hpl.jena.sdb.store.DatabaseType;
import com.hp.hpl.jena.sdb.store.LayoutType;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

public class LexGridToOwlRdfConverter {
    private Store store_ = null;
    private String currentNamespace_;
    private OntModel model_;
    private CodingScheme cs_;
    private CodedNodeGraph cng_;
    private CodedNodeSet cns_;
    private OntologyFormat ontFormat_;
    private LgMessageDirectorIF messenger_;
    private Writer writer_;

    private int assnCounter = 0; // for dev only

    class DriverShim implements Driver {
        private Driver driver;

        DriverShim(Driver d) {
            this.driver = d;
        }

        public Logger getParentLogger() throws SQLFeatureNotSupportedException {
            throw new SQLFeatureNotSupportedException();
        }

        public boolean acceptsURL(String u) throws SQLException {
            return this.driver.acceptsURL(u);
        }

        public Connection connect(String u, Properties p) throws SQLException {
            return this.driver.connect(u, p);
        }

        public int getMajorVersion() {
            return this.driver.getMajorVersion();
        }

        public int getMinorVersion() {
            return this.driver.getMinorVersion();
        }

        public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException {
            return this.driver.getPropertyInfo(u, p);
        }

        public boolean jdbcCompliant() {
            return this.driver.jdbcCompliant();
        }
    }

    public LexGridToOwlRdfConverter() {

    }

    public Store getStore() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        if (store_ != null)
            return store_;

        String url = LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables()
                .getAutoLoadDBURL();
        String userName = LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables()
                .getAutoLoadDBUsername();
        String password = LexEvsServiceLocator.getInstance().getSystemResourceService().getSystemVariables()
                .getAutoLoadDBPassword();
        org.lexevs.dao.database.type.DatabaseType type = LexEvsServiceLocator.getInstance()
                .getLexEvsDatabaseOperations().getDatabaseType();

        StoreDesc storeDesc = null;

        URLClassLoader loader = LexEvsServiceLocator.getInstance().getSystemResourceService().getClassLoader();

        if (type.equals(org.lexevs.dao.database.type.DatabaseType.MYSQL)) {
            Driver d = (Driver) Class.forName("com.mysql.jdbc.Driver", true, loader).newInstance();
            DriverManager.registerDriver(new DriverShim(d));
            storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesIndex, DatabaseType.MySQL);
        } else if (type.equals(org.lexevs.dao.database.type.DatabaseType.ORACLE)) {
            Driver d = (Driver) Class.forName("oracle.jdbc.driver.OracleDriver", true, loader).newInstance();
            DriverManager.registerDriver(new DriverShim(d));
            storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesIndex, DatabaseType.Oracle);
        } else if (type.equals(org.lexevs.dao.database.type.DatabaseType.POSTGRES)) {
            Driver d = (Driver) Class.forName("org.postgresql.Driver", true, loader).newInstance();
            DriverManager.registerDriver(new DriverShim(d));
            storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesIndex, DatabaseType.PostgreSQL);
        } else if (type.equals(org.lexevs.dao.database.type.DatabaseType.HSQL)) {
            Driver d = (Driver) Class.forName("org.hsqldb.jdbcDriver", true, loader).newInstance();
            DriverManager.registerDriver(new DriverShim(d));
            storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesIndex, DatabaseType.HSQLDB);
        } else if (type.equals(org.lexevs.dao.database.type.DatabaseType.DB2)) {
            Driver d = (Driver) Class.forName("com.ibm.db2.jcc.DB2Driver", true, loader).newInstance();
            DriverManager.registerDriver(new DriverShim(d));
            storeDesc = new StoreDesc(LayoutType.LayoutTripleNodesIndex, DatabaseType.DB2);
        }

        SDBConnection conn = new SDBConnection(url, userName, password);
        store_ = SDBFactory.connectStore(conn, storeDesc);

        // If SDB tables do not exist, it will throw an exception.
        try {
            store_.getSize();
        } catch (Exception e) {
            store_.getTableFormatter().create();
            messenger_.info("create sdb tables.");
            return store_;
        }
        return store_;
    }

    public Model getBaseModel(String iri) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, SQLException {
        return SDBFactory.connectNamedModel(this.getStore(), iri);
    }

    private void codingSchemeMapping() throws SecurityException, IllegalArgumentException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        messenger_.info("processing coding scheme meta data.");
        Ontology ontology = model_.createOntology(cs_.getCodingSchemeURI());
        String ncname_codingSchemeName= convertToNMTokenString(cs_.getCodingSchemeName());
        ontology.addLabel(ncname_codingSchemeName, LexRdfConstants.ENGLISH);
        ontology.addVersionInfo(cs_.getRepresentsVersion());

        if (cs_.getCopyright() != null)
            ontology.addProperty(DC.rights, cs_.getCopyright().getContent());
        if (cs_.getFormalName() != null)
            ontology.addProperty(DC.title, cs_.getFormalName());
        if (cs_.getDefaultLanguage() != null)
            ontology.addProperty(DC.language, cs_.getDefaultLanguage());
        for (Source source : cs_.getSource()) {
            if (source.getContent() != null)
                ontology.addProperty(DC.source, source.getContent());
        }
        // import namespaces
        this.initNamespace();
    }

    private void entityMapping() throws LBResourceUnavailableException, SecurityException, IllegalArgumentException,
            LBException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (cns_ == null)
            return;

        messenger_.info("Processing entities ...");

        List<Entity> instanceList = new ArrayList<Entity>();

        ResolvedConceptReferencesIterator iterator = cns_.resolve(null, null, null, null, true);

        int counter = 0;
        while (iterator.hasNext()) {
            ResolvedConceptReference conRef = iterator.next();
            Entity entity = conRef.getEntity();
            counter++;
            if (counter % 1000 == 0) {
                messenger_.info("Processed " + counter + " entities");
            }

            if (entity == null)
                continue;

            // handle the anonymous entity in association mapping, not here.
            if (entity.isIsAnonymous() != null && entity.isIsAnonymous() == true)
                continue;

            if (entity.getEntityType() == null || entity.getEntityType().length == 0
                    || entity.getEntityType().length > 1) {
                // map to skos:concept
                this.addSkosConcept(entity);
            } else if (entity.getEntityType()[0].equalsIgnoreCase(EntityTypes.CONCEPT.value())) {
                // map to owl:class
                this.addOwlClass(entity);
            } else if (entity.getEntityType()[0].equalsIgnoreCase(EntityTypes.ASSOCIATION.value())) {
                // map to owl:objectProperty/dataTypeProperty
                this.addOntProperty((AssociationEntity) entity);
            } else if (entity.getEntityType()[0].equalsIgnoreCase(EntityTypes.INSTANCE.value())) {
                // save instance into a list, process the instances later
                instanceList.add(entity);
            }
        }

        // at last, process the instance, since the instance maybe an instance
        // of owl:class
        processInstance(instanceList);
        messenger_.info("Processed entity total: " + Integer.toString(counter));
    }

    private void associationMapping() throws NullPointerException, LBException, SecurityException,
            IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (cns_ == null || cng_ == null) {
            return;
        }

        messenger_.info("Processing associations ...");

        ResolvedConceptReferencesIterator conIterator = cns_.resolve(null, null, null, null, false);

        while (conIterator.hasNext()) {
            ResolvedConceptReference focus = conIterator.next();
            ResolvedConceptReferenceList localRcrl = cng_.resolveAsList(focus, true, false, 1, -1, null, null, null,
                    null, -1);

            if (localRcrl == null)
                continue;
            Iterator<? extends ResolvedConceptReference> localIterator = localRcrl.iterateResolvedConceptReference();
            while (localIterator.hasNext()) {
                ResolvedConceptReference sourceConRef = localIterator.next();

                if (LexRdfMap.isMapped(sourceConRef, ontFormat_) == true)
                    continue;
                this.processLgTargets(sourceConRef);
            }
        }

        messenger_.info("proccessed the total associations: " + Integer.toString(assnCounter));
    }

    private SupportedAssociation getLgSupportedAssociation(String associationName) throws LBException {
        for (SupportedAssociation supportedAssn : cs_.getMappings().getSupportedAssociation()) {
            if (supportedAssn.getContent().equalsIgnoreCase(associationName)
                    || supportedAssn.getLocalId().equalsIgnoreCase(associationName)) {
                return supportedAssn;
            }
        }

        return null;
    }

    private AnnotationProperty getAnnotationProeprty(String uri) {
        AnnotationProperty p = model_.getAnnotationProperty(uri);
        if (p == null)
            return model_.createAnnotationProperty(uri);
        else
            return p;
    }

    private void processLgQualifier(Resource s, Resource p, Resource o, String qualifierName, String qualifierValue)
            throws LBException {
        Statement statement = model_.createStatement(s, (Property) p, o);
        ReifiedStatement rs = model_.createReifiedStatement(statement);
        AnnotationProperty prop = this.getAnnotationProeprty(this.resolveNamespace(currentNamespace_) + qualifierName);
        prop.addSuperProperty(LexRdf.associationQualification);
        rs.addProperty(prop, qualifierValue);
    }

    private void processLgTargets(ResolvedConceptReference sourceConRef) throws NullPointerException, LBException,
            SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {
        String sourceUri = this.resolveNamespace(sourceConRef.getCodeNamespace()) + sourceConRef.getCode();
        Resource source = null;

        assnCounter++;
        if (assnCounter % 1000 == 0) {
            messenger_.info("processed " + Integer.toString(assnCounter) + " associations");
        }

        // only consider the anonymous target
        if (sourceConRef.getCode().startsWith("@")) {
            return;
        }

        if (sourceConRef.getEntity().getEntityTypeCount() > 0
                && sourceConRef.getEntity().getEntityType()[0].equalsIgnoreCase(EntityTypes.INSTANCE.value())) {
            // do nothing for instance
            return;
        } else if (sourceConRef.getEntity().getEntityTypeCount() > 0
                && sourceConRef.getEntity().getEntityType()[0].equalsIgnoreCase(EntityTypes.ASSOCIATION.value())) {
            // for association
            String uri = getLgPropertyUri(sourceConRef.getCodeNamespace(), sourceConRef.getCode());
            if (uri != null)
                source = model_.getOntProperty(this.getLgPropertyUri(sourceConRef.getCodeNamespace(),
                        sourceConRef.getCode()));
            else
                source = model_.getOntProperty(this.resolveNamespace(sourceConRef.getCodeNamespace())
                        + sourceConRef.getCode());

        } else if (sourceConRef.getEntity().getEntityTypeCount() > 0
                && sourceConRef.getEntity().getEntityType()[0].equalsIgnoreCase(EntityTypes.CONCEPT.value())) {
            // for concept
            source = model_.getOntClass(sourceUri);
        }
        
        if (source == null) {
            //We do not know the entity type of the entity....weird
            return;
        }

        AssociationList targetList = sourceConRef.getSourceOf();
        if ((targetList != null) && (targetList.getAssociationCount() > 0)) {
            Iterator<? extends Association> targetsIterator = targetList.iterateAssociation();
            while (targetsIterator.hasNext()) {
                Association targetAssociation = targetsIterator.next();
                // get association entity according to the association name
                SupportedAssociation supportedAssn = this.getLgSupportedAssociation(targetAssociation
                        .getAssociationName());
                Property prop = null;
                if (supportedAssn == null) {
                    throw new LBException("cannot find the supported association for "
                            + targetAssociation.getAssociationName());
                }
                
                else {
                    String propUri = null;
                    if (supportedAssn.getEntityCode() == null) {
                        propUri = this.getLgPropertyUri(supportedAssn.getEntityCodeNamespace(),
                                supportedAssn.getLocalId());
                    } else {
                        propUri = this.getLgPropertyUri(supportedAssn.getEntityCodeNamespace(),
                                reformatCode(supportedAssn.getEntityCode()));
                    }

                    if (propUri != null)
                        prop = model_.getProperty(propUri);

                    if (prop == null) {
                        // if it maps to owl/rdf properties, we don't need to
                        // add it
                        if (LexRdfMap.isMapped(null, supportedAssn.getLocalId(), ontFormat_) == true)
                            return;
                        propUri = this.resolveNamespace(currentNamespace_) + supportedAssn.getLocalId();
                        prop = model_.createOntProperty(propUri);
                    }

                }

                Iterator<? extends AssociatedConcept> associatedTargetsIterator = targetAssociation
                        .getAssociatedConcepts().iterateAssociatedConcept();
                while (associatedTargetsIterator.hasNext()) {
                    AssociatedConcept target = associatedTargetsIterator.next();
                    Resource targetRsc = model_.getResource(this.resolveNamespace(target.getCodeNamespace())
                            + target.getCode());

                    if (target == null || target.getCode().startsWith("@@"))
                        continue;

                    if (target.getCode().startsWith("@")) {
                        Resource r = processLgAnonymousTarget(target);
                        if (r == null)
                            continue;
                        if (prop.getURI().equals(RDFS.subClassOf.getURI())) {
                            this.createTriple(r, prop, source);
                        } else
                            this.createTriple(source, prop, r);
                        continue;
                    }

                    // qualifier
                    SupportedAssociationQualifier q = this.getLgQualifier(target);
                    if (q != null && prop != null) {                       
                        if (this.ontFormat_.equals(OntologyFormat.UMLS)) {
                            this.processLgQualifier(source, prop, targetRsc, q.getLocalId(), q.getContent());
                        }
                    }

                    if (this.ontFormat_.equals(OntologyFormat.UMLS) == false
                            && prop.getURI().equalsIgnoreCase(RDFS.subClassOf.getURI())) {
                        targetRsc = model_.getOntClass(targetRsc.getURI());
                        this.createTriple(targetRsc, prop, source);
                    } else if (this.ontFormat_.equals(OntologyFormat.UMLS)
                            && prop.getURI().equalsIgnoreCase(RDFS.subClassOf.getURI())) {
                        if (supportedAssn.getLocalId().equals("CHD")) {
                            targetRsc = model_.getOntClass(targetRsc.getURI());
                            this.createTriple(targetRsc, prop, source);
                        } else if (supportedAssn.getLocalId().equals("PAR")) {
                            this.createTriple(source, prop, targetRsc);
                        }
                    } else
                        this.createTriple(source, prop, targetRsc);
                }
            }
        }
    }

    /*
     * Get Uri according to the name space and code. Apply the hash map if it
     * isn't owl/rdf ontology
     */
    private String getLgPropertyUri(String ns, String code) throws LBException {
        if (this.ontFormat_.equals(OntologyFormat.OWLRDF))
            return this.resolveNamespace(ns) + code;
        else {
            if (LexRdfMap.get(code, ontFormat_) != null)
                return LexRdfMap.get(code, ontFormat_).getURI();
            else
                return null;
        }
    }

    /**
     * Create enumerated class -- oneOf. According to the owl loader if there is
     * anonymous class does not have association target, it could be an
     * enumerated class. This method checks its property which specify the
     * enumerated type, and its entity description contains the list of
     * enumerated things, for instance {instanceA, instanceB, classC}.
     * 
     * @param anonymousSource
     * @return
     * @throws LBException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws SecurityException
     */
    private Resource createEnumeratedClassFromEmptyAnonymousSource(AssociatedConcept anonymousSource)
            throws LBException, SecurityException, IllegalArgumentException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        Entity en = anonymousSource.getEntity();
        boolean isOneOf = false;

        for (org.LexGrid.commonTypes.Property lgProp : en.getProperty()) {           
            if (!lgProp.getPropertyName().equalsIgnoreCase("type"))
                continue;
            StringHelper helper = new StringHelper(lgProp.getValue().getContent(), model_.getNsPrefixMap());
            if (helper.getStrFormat().equals(StringHelper.StrFormat.PREFIX_TYPE)) {
                if (helper.getType() != null && helper.getType().getURI().equals(OWL.oneOf.getURI())) {
                    isOneOf = true;
                    break;
                }
            }
        }
        if (isOneOf == false)
            return null;

        if (en.getEntityDescription() != null && en.getEntityDescription().getContent().startsWith("{")
                && en.getEntityDescription().getContent().endsWith("}")) {

            String content = en.getEntityDescription().getContent();
            content = content.substring(1, content.length() - 1);
            String[] elements = content.split(" ");
            if (elements.length == 0)
                return null;
            List<RDFNode> rdfList = new ArrayList<RDFNode>();
            for (String e : elements) {
                String code = e.trim();
                RDFNode rsc = model_.getResource(this.resolveNamespace(currentNamespace_) + code);
                rdfList.add(rsc);
            }
            return model_.createEnumeratedClass(null, model_.createList(rdfList.iterator()));
        }
        return null;
    }

    private Resource processLgAnonymousTarget(AssociatedConcept anonymousSource) throws SecurityException,
            IllegalArgumentException, LBException, NoSuchMethodException, IllegalAccessException,
            InvocationTargetException {

        // AssociationList assnList = anonymousSource.getSourceOf();
        // anonymous class's getsourceof does not work correct, use cng instead
        // for now
        ResolvedConceptReferenceList localRcrl = cng_.resolveAsList(anonymousSource, true, false, 1, -1, null, null,
                null, null, -1);
        if (localRcrl == null || localRcrl.getResolvedConceptReferenceCount() == 0)
            return null;
        AssociationList assnList = localRcrl.getResolvedConceptReference(0).getSourceOf();

        if (assnList == null) {
            return createEnumeratedClassFromEmptyAnonymousSource(anonymousSource);
        }

        Iterator<? extends Association> iteratorAssociations = assnList.iterateAssociation();

        // used for intersectionof or unionof
        List<RDFNode> rdfList = null;
        Class<?> classHolder = null;
        for (org.LexGrid.commonTypes.Property lgProp : anonymousSource.getEntity().getProperty()) {
            // if supported property change, it is not neccessary
            if (lgProp.getValue().getContent().equalsIgnoreCase(OWL.intersectionOf.getLocalName())) {
                rdfList = new ArrayList<RDFNode>();
                classHolder = IntersectionClass.class;
            } else if (lgProp.getValue().getContent().equalsIgnoreCase(OWL.unionOf.getLocalName())) {
                rdfList = new ArrayList<RDFNode>();
                classHolder = UnionClass.class;
            } else if (lgProp.getValue().getContent().equalsIgnoreCase(OWL.complementOf.getLocalName())) {
                classHolder = ComplementClass.class;
            }
        }

        while (iteratorAssociations.hasNext()) {
            Association assn = iteratorAssociations.next();
            SupportedAssociation suppAssn = this.getLgSupportedAssociation(assn.getAssociationName());
            Property localProp = null;
            if (suppAssn == null) {
                throw new LBException("no supported assocation found for " + assn.getAssociationName());
            } else {
                localProp = model_.getProperty(this.getLgPropertyUri(suppAssn.getEntityCodeNamespace(),
                        reformatCode(suppAssn.getEntityCode())));
            }

            if (assn.getAssociatedConcepts() == null)
                continue;

            Iterator<? extends AssociatedConcept> iteratorConcepts = assn.getAssociatedConcepts()
                    .iterateAssociatedConcept();

            while (iteratorConcepts.hasNext()) {
                AssociatedConcept target = iteratorConcepts.next();
                Resource targetRsc = model_.getResource(this.resolveNamespace(target.getCodeNamespace())
                        + target.getCode());
                if (target.getCode().startsWith("@@"))
                    continue;
                if (target.getCode().startsWith("@") && anonymousSource.getCode().startsWith("@")) {
                    // target is anonymous class, recursively call itself
                    targetRsc = processLgAnonymousTarget(target);
                    if (targetRsc == null)
                        continue;
                    if (localProp.getURI().equals(OWL.complementOf.getURI()))
                        targetRsc = model_.createComplementClass(null, targetRsc);
                    else {
                        if (localProp.getURI().equals(RDFS.subClassOf.getURI()) == false
                                && localProp.getNameSpace().equals(currentNamespace_))
                            System.err.println("process anonymous targe error");
                    }
                }

                SupportedAssociationQualifier qualifier = getLgQualifier(target);
                Restriction restriction = null;
                if (localProp != null && qualifier != null) {
                    restriction = createRestriction(qualifier, localProp, targetRsc);
                }

                if (rdfList != null) {
                    if (restriction != null)
                        rdfList.add(restriction);
                    else if (localProp != null && localProp.getURI().equals(OWL.complementOf.getURI())) {
                        targetRsc = model_.createComplementClass(null, targetRsc);
                    }
                    rdfList.add(targetRsc);
                } else {
                    if (restriction != null)
                        if (classHolder != null && classHolder.equals(ComplementClass.class) == true) {
                            return model_.createComplementClass(null, restriction);
                        } else {
                            return restriction;
                        }
                    else if (classHolder != null && classHolder.equals(ComplementClass.class) == true) {
                        return model_.createComplementClass(null, targetRsc);

                    } else {
                        return targetRsc;
                    }
                }
            }
        }

        if (rdfList != null && rdfList.size() > 1) {
            return this.createIntersectionOrUnionClass(classHolder, rdfList);
        } else
            return null;
    }

    private void createTriple(Resource s, Property p, Resource o) {
        if (s == null || p == null || o == null)
            return;
        try {
            if (p.getURI().equals(OWL.equivalentClass.getURI())) {
                ((OntClass) s).addEquivalentClass(o);
            } else if (p.getURI().equals(OWL.disjointWith.getURI())) {
                ((OntClass) s).addDisjointWith(o);
            } else if (p.getURI().equals(OWL.differentFrom.getURI())) {
                ((OntClass) s).addDifferentFrom(o);
            } else if (p.getURI().equals(OWL.sameAs.getURI())) {
                ((OntClass) s).addSameAs(o);
            } else if (p.getURI().equals(RDFS.isDefinedBy.getURI())) {
                ((OntClass) s).addIsDefinedBy(o);
            } else if (p.getURI().equals(RDFS.seeAlso.getURI())) {
                ((OntClass) s).addSeeAlso(o);
            } else if (p.getURI().equals(RDFS.subClassOf.getURI())) {
                ((OntClass) s).addSubClass(o);
            } else if (p.getURI().equals(RDFS.domain)) {
                ((OntProperty) s).addDomain(o);
            } else if (p.getURI().equals(RDFS.range)) {
                ((OntProperty) s).addRange(o);
            } else if (p.getURI().equals(OWL.complementOf.getURI())) {
                if (s.getURI() == null)
                    s = model_.createComplementClass(null, o);
                else
                    model_.createComplementClass(s.getURI(), o);
            } else {
                s.addProperty(p, o);
            }
        } catch (Exception e) {
            messenger_.error(e.toString());
        }

    }

    private Restriction createRestriction(SupportedAssociationQualifier q, Property p, Resource r) {
        if (q.getUri().equalsIgnoreCase(OWL.someValuesFrom.getURI()))
            return model_.createSomeValuesFromRestriction(null, p, r);
        else if (q.getUri().equalsIgnoreCase(OWL.allValuesFrom.getURI()))
            return model_.createAllValuesFromRestriction(null, p, r);
        else if (q.getUri().equalsIgnoreCase(OWL.hasValue.getURI()))
            return model_.createHasValueRestriction(null, p, r);
        /**
         * We cannot create max cardinality and min cardinality restrictions,
         * since the associatedData does not support the qualifier for now. We
         * will add this feature in the next release
         */
        // else if (q.getUri().equalsIgnoreCase(OWL.maxCardinality.getURI()))
        // return model_.createMaxCardinalityRestriction(null, p, n);
        // else if (q.getUri().equalsIgnoreCase(OWL.minCardinality.getURI()))
        // return model_.createMinCardinalityRestriction(null, p, n);
        else
            return null;
    }

    // create intersection class or union class
    private Resource createIntersectionOrUnionClass(Class<?> cls, List<RDFNode> rdfList) throws LBException,
            SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        final String prefix = "create";
        Method m = model_.getClass().getMethod(prefix + cls.getSimpleName(), String.class, RDFList.class);
        m.setAccessible(true);
        return (Resource) m.invoke(model_, null, model_.createList(rdfList.iterator()));
    }

    // Only consider one qualifier
    private SupportedAssociationQualifier getLgQualifier(AssociatedConcept concept) throws SecurityException,
            IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        NameAndValueList nameValueList = concept.getAssociationQualifiers();
        if (nameValueList != null && nameValueList.getNameAndValueCount() > 0) {
            for (NameAndValue nameAndValue : nameValueList.getNameAndValue()) {
                // qualifier is saved in name field
                if (StringUtils.isEmpty(nameAndValue.getName()) == false) {
                    return DaoUtility.getURIMap(cs_, SupportedAssociationQualifier.class, nameAndValue.getName());
                }
            }
        }
        return null;
    }

    // private boolean isAssociationSourceProcessed(ResolvedConceptReference
    // rcr) {
    // return this.sourceCache.exists(rcr);
    // }

    private void addOntProperty(Entity entity) throws LBException, SecurityException, IllegalArgumentException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // if it maps to owl/rdf properties, we don't need to add it
        if (LexRdfMap.isMapped(entity, ontFormat_) == true)
            return;

        String namespace = resolveNamespace(entity.getEntityCodeNamespace());
        OntProperty ontProperty;

        /*
         * If it is owl, create a general property and use lgproperty to define
         * what type of property it is if is not an owl ontology, create an
         * object property.
         */
        if (ontFormat_.equals(OntologyFormat.OWLRDF))
            ontProperty = model_.createOntProperty(namespace + reformatCode(entity.getEntityCode()));
        else
            ontProperty = model_.createObjectProperty(namespace + reformatCode(entity.getEntityCode()));

        if (entity instanceof AssociationEntity && ((AssociationEntity) entity).isIsTransitive() != null
                && ((AssociationEntity) entity).isIsTransitive() == true) {
            ontProperty.addRDFType(OWL.TransitiveProperty);
        }

        for (org.LexGrid.commonTypes.Property property : entity.getProperty()) {
            processLgProperty(ontProperty, property);
        }
    }

    private void processLgProperty(OntProperty ontProperty, org.LexGrid.commonTypes.Property property)
            throws LBException, SecurityException, IllegalArgumentException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        // get supported property
        SupportedProperty supProp = DaoUtility.getURIMap(cs_, SupportedProperty.class, property.getPropertyName());
        if (supProp == null) {
            throw new LBException("no supported property found for " + property.getPropertyName());
        }

        if (supProp.getUri().equalsIgnoreCase(RDFS.domain.getURI())) {
            String id = resolveNamespace(currentNamespace_) + trimLocalId(property.getValue().getContent());
            this.createTriple(ontProperty, model_.getProperty(supProp.getUri()), model_.getResource(id));
        } else if (supProp.getUri().equalsIgnoreCase(OWL.inverseOf.getURI())) {
            StringHelper sh = new StringHelper(property.getValue().getContent(), model_.getNsPrefixMap());
            if (sh.getStrFormat().equals(StringHelper.StrFormat.TYPE_VALUE)) {
                OntProperty localProperty = model_.createOntProperty(trimLocalId(sh.getValue()));
                localProperty.addRDFType(sh.getType());
                ontProperty.addInverseOf(localProperty);
            } else
                throw new LBException("invalid property value");
        } else if (supProp.getUri().equalsIgnoreCase(RDFS.range.getURI())) {
            String id = resolveNamespace(currentNamespace_) + this.trimLocalId(property.getValue().getContent());
            this.createTriple(ontProperty, model_.getProperty(supProp.getUri()), model_.getResource(id));
        } else if (supProp.getUri().equalsIgnoreCase(RDF.type.getURI())) {
            StringHelper sh = new StringHelper(property.getValue().getContent(), model_.getNsPrefixMap());
            if (sh.getStrFormat() != null)
                ontProperty.addRDFType(sh.getType());
            else {
                Resource ontType = LexRdfMap.get(property.getValue().getContent(), ontFormat_);
                if (ontType != null)
                    ontProperty.addRDFType(ontType);
            }
        } else if (supProp.getUri().equalsIgnoreCase(RDFS.subPropertyOf.getURI())) {
            StringHelper sh = new StringHelper(property.getValue().getContent(), model_.getNsPrefixMap());
            OntProperty localProperty = model_.createOntProperty(sh.getValue());
            localProperty.addRDFType(sh.getType());
            // ontProperty.addSubProperty(localProperty);
            localProperty.addSubProperty(ontProperty);
        } else if (supProp.getUri().equalsIgnoreCase(OWL.equivalentProperty.getURI())) {
            StringHelper sh = new StringHelper(property.getValue().getContent(), model_.getNsPrefixMap());
            if (sh.getStrFormat() == StringHelper.StrFormat.TYPE_VALUE)
                ontProperty.addEquivalentProperty(model_.getProperty(sh.getValue()));
        } else if (supProp.getUri().equalsIgnoreCase(RDFS.label.getURI())) {
            // do nothing. label will be map to presentation
        } else {
            System.err.println("attention, not inlucded property, create annotation property for: "
                    + property.getPropertyName() + ": " + property.getValue().getContent());
            AnnotationProperty annotationProp = model_.createAnnotationProperty(this
                    .resolveNamespace(currentNamespace_) + property.getPropertyName());
            ontProperty.addProperty(annotationProp, property.getValue().getContent());
        }
    }

    private void processInstance(List<Entity> instanceList) throws LBException {

        /*
         * find the if this instance has defined type in the association table,
         * if it does, create a that type of class, if the instance has no
         * defined type, map it to owl:thing
         */
        for (Entity entity : instanceList) {
            CodedNodeGraph localCng = this.restrictToCngAssociation(RDF.type.getLocalName());
            ConceptReference sourceConRef = new ConceptReference();
            sourceConRef.setCode(entity.getEntityCode());
            sourceConRef.setCodeNamespace(entity.getEntityCodeNamespace());
            sourceConRef.setCodingSchemeName(cs_.getCodingSchemeName());
            sourceConRef.setConceptCode(entity.getEntityCode());
            ResolvedConceptReferenceList resolvedConRefList = localCng.resolveAsList(sourceConRef, true, false, 0, 1,
                    null, null, null, -1);

            // resolvedConRefList size is always one
            if (resolvedConRefList.getResolvedConceptReferenceCount() == 0
                    || resolvedConRefList.getResolvedConceptReferenceCount() > 1) {
                throw new LBException("CNG resolve as list failed");
            }

            AssociationList associationList = resolvedConRefList.getResolvedConceptReference(0).getSourceOf();

            if (associationList == null || associationList.getAssociation(0) == null
                    || associationList.getAssociation(0).getAssociatedConcepts().getAssociatedConceptCount() == 0)
                // no specific type, treat it as owl:thing
                this.addOwlThing(model_, cs_, entity);
            else {
                // specific type
                for (AssociatedConcept target : associationList.getAssociation(0).getAssociatedConcepts()
                        .getAssociatedConcept()) {
                    String namespace = this.resolveNamespace(entity.getEntityCodeNamespace());
                    OntResource resource = model_.createOntResource(namespace + reformatCode(entity.getEntityCode()));
                    OntResource typeResource = model_.getOntResource(this.resolveNamespace(target.getCodeNamespace())
                            + target.getCode());
                    if (typeResource == null) {
                        typeResource = model_.createOntResource(this.resolveNamespace(target.getCodeNamespace())
                                + target.getCode());
                    }
                    resource.addRDFType(typeResource);
                }
            }
        }
    }

    // if the instance has not defined type, map it to owl:thing
    private void addOwlThing(OntModel model, CodingScheme cs, Entity entity) throws LBException {
        String namespace = this.resolveNamespace(entity.getEntityCodeNamespace());
        OntResource resource = model.createOntResource(namespace + reformatCode(entity.getEntityCode()));
        resource.addRDFType(OWL.Thing);
    }

    private void addSkosConcept(Entity entity) throws LBException {
        System.out.println("SKOS!!!!!!"); // for dev
        String namespace = this.resolveNamespace(entity.getEntityCodeNamespace());
        OntResource resource = model_.createOntResource(namespace + reformatCode(entity.getEntityCode()));
        resource.addRDFType(Skos.Concept);

        Map<String, ReifiedStatement> statementHash = new HashMap<String, ReifiedStatement>();
        // handle entity's comments
        for (Comment comment : entity.getComment()) {
            this.addLgCommentOrDefinitionOrPresentation(resource, Skos.note, comment, statementHash);
        }

        // handle entity's definition
        for (Definition def : entity.getDefinition()) {
            this.addLgCommentOrDefinitionOrPresentation(resource, Skos.definition, def, statementHash);
        }

        // handle entity's presentation
        for (Presentation presentation : entity.getPresentation()) {
            if (presentation.getIsPreferred() == true) {
                this.addLgCommentOrDefinitionOrPresentation(resource, Skos.prefLabel, presentation, statementHash);
            } else {
                this.addLgCommentOrDefinitionOrPresentation(resource, Skos.altLabel, presentation, statementHash);
            }
        }

        // handle property links
        for (PropertyLink propLink : entity.getPropertyLink()) {
            AnnotationProperty ap = model_.createAnnotationProperty(namespace + propLink.getPropertyLink());

            String sourceProp = propLink.getSourceProperty();
            String targetProp = propLink.getTargetProperty();
            statementHash.get(sourceProp).addProperty(ap, statementHash.get(targetProp).getURI());

        }

    }

   

    private void addOwlClass(Entity entity) throws LBException, SecurityException, IllegalArgumentException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String namespace = this.resolveNamespace(entity.getEntityCodeNamespace());

        // add owl class
        OntClass owlClass = model_.createClass(namespace + reformatCode(entity.getEntityCode()));

        // set rdf:isDefined
        if (entity.isIsDefined() != null)
            owlClass.addProperty(LexRdf.isDefined, entity.isIsDefined().toString());

        Map<String, ReifiedStatement> statementHash = new HashMap<String, ReifiedStatement>();
        // handle entity's comments
        for (Comment comment : entity.getComment()) {
            this.addLgCommentOrDefinitionOrPresentation(owlClass, Skos.note, comment, statementHash);
        }

        // handle entity's definition
        for (Definition def : entity.getDefinition()) {
            this.addLgCommentOrDefinitionOrPresentation(owlClass, Skos.definition, def, statementHash);
        }

        // handle entity's presentation
        for (Presentation presentation : entity.getPresentation()) {
            if (presentation.getIsPreferred() != null && presentation.getIsPreferred() == true) {
                this.addLgCommentOrDefinitionOrPresentation(owlClass, Skos.prefLabel, presentation, statementHash);
            } else {
                this.addLgCommentOrDefinitionOrPresentation(owlClass, Skos.altLabel, presentation, statementHash);
            }
        }

        // handle entity's property
        for (org.LexGrid.commonTypes.Property lgProp : entity.getProperty()) {
            SupportedProperty supProp = DaoUtility.getURIMap(cs_, SupportedProperty.class, lgProp.getPropertyName());
            String uri = supProp.getUri();

            if (StringUtils.isBlank(uri) && StringUtils.isNotBlank(lgProp.getPropertyName())) {
                uri= resolveNamespace("");
                uri += lgProp.getPropertyName();
            }
            if (StringUtils.isNotBlank(uri)) {
                owlClass.addLiteral(model_.getProperty(uri), lgProp.getValue().getContent());
            }
        }

        // handle property links
        for (PropertyLink propLink : entity.getPropertyLink()) {
            AnnotationProperty ap = model_.createAnnotationProperty(namespace + propLink.getPropertyLink());
            String sourceProp = propLink.getSourceProperty();
            String targetProp = propLink.getTargetProperty();
            statementHash.get(sourceProp).addProperty(ap, statementHash.get(targetProp).getURI());

        }
    }

    // only a regular mapping from lg:property's value to
    // comment/definition/presentation. not necessary to consider the property
    // name
    private void addLgCommentOrDefinitionOrPresentation(OntResource resource, Property prop,
            org.LexGrid.commonTypes.Property lgProp, Map<String, ReifiedStatement> statementHash) {
        Statement statement = model_.createStatement(resource, prop, lgProp.getValue().getContent());

        ReifiedStatement rs = model_.createReifiedStatement(statement);

        // general stuff for comment/definition/presentation
        if (StringUtils.isNotBlank(lgProp.getLanguage()))
            rs.addProperty(DC.language, lgProp.getLanguage());

        for (Source source : lgProp.getSource()) {
            if (StringUtils.isEmpty(source.getContent()) == false)
                rs.addProperty(DC.source, source.getContent());
        }

        if (lgProp instanceof Definition) {
            if (((Definition) lgProp).isIsPreferred() != null && ((Definition) lgProp).isIsPreferred() == true)
                rs.addProperty(LexRdf.isPreferred, "true");
        } else if (lgProp instanceof Presentation) {
            if (StringUtils.isEmpty(((Presentation) lgProp).getDegreeOfFidelity()) == false)
                rs.addProperty(LexRdf.degreeOfFidelity, ((Presentation) lgProp).getDegreeOfFidelity());
            if (((Presentation) lgProp).getMatchIfNoContext() != null
                    && ((Presentation) lgProp).getMatchIfNoContext() == true)
                rs.addProperty(LexRdf.matchIfNoContext, ((Presentation) lgProp).getMatchIfNoContext().toString());
            if (StringUtils.isEmpty(((Presentation) lgProp).getRepresentationalForm()) == false)
                rs.addProperty(LexRdf.representationalForm, ((Presentation) lgProp).getRepresentationalForm());
        }

        statementHash.put(lgProp.getPropertyId(), rs);

    }

    private String resolveNamespace(String localId) throws LBException {
        Map<String, String> supportedNamespace = model_.getNsPrefixMap();
        String uri;
        if (StringUtils.isBlank(localId)) {
            uri= supportedNamespace.get(currentNamespace_);
        } else {
            String nmTokenLocalId= convertToNMTokenString(localId);
            uri = supportedNamespace.get(nmTokenLocalId);
        }
        if (uri != null) {
            if (!uri.endsWith("#") && !uri.endsWith("/") && !uri.endsWith(":"))
                uri = uri + "#";
        }

        // for dev only
        if (uri == null)
            throw new LBException("ns " + localId + " has not been imported to ontology yet!");
        return uri;
    }
    
    /**
     * Remove "/" and ":" from the strings of the code field
     * @param code
     * @return
     */
    private String reformatCode(String code) {
        String formattedCode= code.replace("/", "_").replace(":", "_");
        return formattedCode;
        
    }

    private String trimLocalId(String localId) {
        if (localId.startsWith(this.currentNamespace_ + ":"))
            return localId.replace(this.currentNamespace_ + ":", "");
        return localId;
    }

    private void initNamespace() {
        Map<String, String> nsMap = new HashMap<String, String>();

        // skos
        nsMap.put(LexRdfConstants.SKOS_NS_PREFIX, LexRdfConstants.SKOS_NS_URI);

        // lexrdf
        nsMap.put(LexRdfConstants.LEXRDF_NS_PREFIX, LexRdfConstants.LEXRDF_NS_URI);

        // add codingSchemeURI
        currentNamespace_ = model_.getOntology(cs_.getCodingSchemeURI()).getLabel(null);
        nsMap.put("", cs_.getCodingSchemeURI());
        nsMap.put(currentNamespace_, cs_.getCodingSchemeURI() + "#");

        // check coding scheme mappings
        if (cs_.getMappings() != null) {
            // supported namespace
            SupportedNamespace[] supportedNss = cs_.getMappings().getSupportedNamespace();
            for (SupportedNamespace ns : supportedNss) {
                String nmTokenLocalId= convertToNMTokenString(ns.getLocalId());
                if (StringUtils.isNotBlank(nmTokenLocalId) && StringUtils.isNotBlank(ns.getUri()) ) {
                  nsMap.put(nmTokenLocalId, ns.getUri());
                }
            }
            // supported associations
            if (ontFormat_.equals(OntologyFormat.OWLRDF)) {
                SupportedAssociation[] supportedAssns = cs_.getMappings().getSupportedAssociation();
                for (SupportedAssociation assn : supportedAssns) {
                    if (assn.getUri() != null) {
                        model_.createOntProperty(assn.getUri());
                    }
                }
            }
        }

        model_.setNsPrefixes(nsMap);
    }

    /*
     * when we need to put restrictions on cng, we need to create a copy of it
     * and run on it, the queries and restrictions can change the cng itself.
     */
    private CodedNodeGraph restrictToCngAssociation(String restriction) throws LBException {
        LexBIGService lbsvc = LexBIGServiceImpl.defaultInstance();
        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(cs_.getRepresentsVersion());
        CodedNodeGraph localCng;

        localCng = lbsvc.getNodeGraph(cs_.getCodingSchemeURI(), versionOrTag, null);
        localCng.restrictToAssociations(Constructors.createNameAndValueList(restriction), null);

        return cng_.intersect(localCng);
    }

    

    private OntologyFormat findOntFormat() {
        for (org.LexGrid.commonTypes.Property p : cs_.getProperties().getProperty()) {
            if (p.getPropertyName().equals(OntologyFormat.getMetaName())) {
                return OntologyFormat.valueOf(p.getValue().getContent());
            }
        }
        return null;
    }

    public void toTripleStore(CodingScheme cs, CodedNodeGraph baseCng, CodedNodeSet cns, Writer writer,
            LgMessageDirectorIF messenger, OntologyFormat ontFormat) {
        this.cs_ = cs;
        this.cns_ = cns;
        this.cng_ = baseCng;
        this.messenger_ = messenger;
        this.writer_ = writer;
        if (ontFormat != null)
            this.ontFormat_ = ontFormat;
        else
            ontFormat_ = this.findOntFormat();

        Model baseModel;
        try {
            baseModel = this.getBaseModel(cs.getCodingSchemeURI());
            // create or open the default model
            model_ = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, baseModel);

            // clean up the triple store to load
            store_.getTableFormatter().truncate();

            // coding schema mapping
            this.codingSchemeMapping();

            // entity mapping

            this.entityMapping();

            // association mapping

            this.associationMapping();

            // model_.write(System.out); // for dev & test/

            // now write the model in XML form to a file

            this.toOwlOntology(cs_.getCodingSchemeURI());
        } catch (Exception ex) {
            this.messenger_.error(ex.toString());
            ex.printStackTrace();

        }

        // Close the database connection
        model_.close();
        store_.getConnection().close();

    }

    public void toOwlOntology(String iri) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, SQLException {
        messenger_.info("Converting triple store to ontology ...");

        Model baseModel = this.getBaseModel(iri);

        OntModel localModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, baseModel);

        RDFWriter rdfw = localModel.getWriter(LexRdfConstants.RDFXML_ABBREV);
        rdfw.setProperty(LexRdfConstants.SHOW_XML_DECLARATION, true);
        rdfw.setProperty(LexRdfConstants.XML_BASE, cs_.getCodingSchemeURI());
        rdfw.setProperty(LexRdfConstants.RELATIVE_URIS, "");
        // FileOutputStream f = new FileOutputStream(outputFile_);
        rdfw.write(localModel, writer_, cs_.getCodingSchemeURI());
        baseModel.close();
    }

    public static void main(String[] args) {
        ExportStatus status = new ExportStatus();
        status.setState(ProcessState.PROCESSING);
        status.setStartTime(new Date(System.currentTimeMillis()));

        ExporterMessageDirector md = new ExporterMessageDirector("LexRdfExporter", status);

        // String codingSchemeUri =
        // "http://www.xfront.com/owl/ontologies/camera/",
        // codingSchemeVersion = "UNASSIGNED",
        // output = "c:/temp/camera.owl";

        // String codingSchemeUri = "http://purl.org/net/OCRe/OCRe-Start-Here",
        // codingSchemeVersion = "UNASSIGNED",
        // output = "c:/temp/ocre.owl";
        // String codingSchemeUri =
        // "http://www.co-ode.org/ontologies/pizza/2005/05/16/pizza.owl",
        // codingSchemeVersion = "version 1.2",
        // output = "C:/temp/pizza.owl";
        String codingSchemeUri = "http://www.w3.org/TR/2003/PR-owl-guide-20031209/wine", codingSchemeVersion = "UNASSIGNED", output = "C:/temp/wine.owl";
        // String codingSchemeUri = "urn:lsid:bioontology.org:cell",
        // codingSchemeVersion = "UNASSIGNED",
        // output = "c:/temp/cell_obo.owl";

        // String codingSchemeUri = "urn:lsid:bioontology.org:uberon",
        // codingSchemeVersion = "UNASSIGNED";
        // String codingSchemeUri = "urn:oid:2.16.840.1.113883.6.110",
        // codingSchemeVersion = "1993.bvt",
        // output = "c:/temp/umls.owl";

        LexGridToOwlRdfConverter converter = new LexGridToOwlRdfConverter();

        CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
        versionOrTag.setVersion(codingSchemeVersion);

        LexBIGService lbsvc = LexBIGServiceImpl.defaultInstance();
        try {
            CodingScheme codingScheme = lbsvc.resolveCodingScheme(codingSchemeUri,
                    Constructors.createCodingSchemeVersionOrTagFromVersion(codingSchemeVersion));
            CodedNodeSet cns = lbsvc.getNodeSet(codingSchemeUri, versionOrTag, null);

            CodedNodeGraph cng = lbsvc.getNodeGraph(codingSchemeUri, versionOrTag, null);

            FileOutputStream f = new FileOutputStream(output);
            Writer w = new OutputStreamWriter(f);
            converter.toTripleStore(codingScheme, cng, cns, w, md, null);

        } catch (LBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    
    public static String convertToNMTokenString(String str) {
        if (StringUtils.isBlank(str))
            return str;

        String conv = str.trim().replaceAll("\\s+", "_");
        conv = conv.replace('/', '_');
        conv = conv.replaceAll("[^a-zA-Z0-9._-]", "");

        if (conv.length() > 1 && conv.substring(0, 1).matches("[0-9]"))
            conv = "_" + conv;

        return conv;

       
    }

}