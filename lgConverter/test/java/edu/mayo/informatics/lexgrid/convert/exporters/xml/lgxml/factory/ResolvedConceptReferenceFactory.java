
package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.factory;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.concepts.Entity;

public class ResolvedConceptReferenceFactory {

   public static ResolvedConceptReference createRcrAutoMaker() {
       Entity entity = EntityFactory.createEntityAutoMaker();
       return ResolvedConceptReferenceFactory.createRcrFromEntity(entity);       
   }
   
   public static ResolvedConceptReference createRcrFord() {
       Entity entity = EntityFactory.createEntityFord();
       return ResolvedConceptReferenceFactory.createRcrFromEntity(entity);       
   }   
  
   public static ResolvedConceptReference createRcrTires() {
       Entity entity = EntityFactory.createEntityTires();
       return ResolvedConceptReferenceFactory.createRcrFromEntity(entity);       
   }   
   
   public static ResolvedConceptReference createRcrAutomobile_original() {
       Entity entity = EntityFactory.createEntityAutomobile();
       return ResolvedConceptReferenceFactory.createRcrFromEntity(entity);       
   }
   
   public static ResolvedConceptReference createRcrAutomobile() {
       ResolvedConceptReference rcr = new ResolvedConceptReference();
       Entity entity = EntityFactory.createEntityAutomobile();
       rcr.setEntity(entity);
       
/*       
       rcr.s
       
       // populate association data
       AssociationList sourceOf = new AssociationList();
       Association assoc = new Association();
      
       sourceOf.setAssociation(arg0)
       rcr.setSourceOf(sourceOf)
       rcr.setTargetOf(targetOf)
*/
       return ResolvedConceptReferenceFactory.createRcrFromEntity(entity);       
   }
   

   public static ResolvedConceptReference createRcrRoot() {
       Entity entity = EntityFactory.createEntityRoot();
       return ResolvedConceptReferenceFactory.createRcrFromEntity(entity);       
   }

   public static ResolvedConceptReference createRcrCar() {
       Entity entity = EntityFactory.createEntityCar();
       return ResolvedConceptReferenceFactory.createRcrFromEntity(entity);       
   }

   public static ResolvedConceptReference createRcrTruck() {
       Entity entity = EntityFactory.createEntityTruck();
       return ResolvedConceptReferenceFactory.createRcrFromEntity(entity);
   }
   
   private static ResolvedConceptReference createRcrFromEntity(Entity entity) {
       if (entity == null)
           return null;
       
       ResolvedConceptReference rcr = new ResolvedConceptReference();
       rcr.setCode(entity.getEntityCode());
       rcr.setCodeNamespace(entity.getEntityCodeNamespace());
       rcr.setConceptCode(entity.getEntityCode());
       rcr.setCodingSchemeName(entity.getEntityCodeNamespace());
       rcr.setEntity(entity);      
       return rcr;
   }

}