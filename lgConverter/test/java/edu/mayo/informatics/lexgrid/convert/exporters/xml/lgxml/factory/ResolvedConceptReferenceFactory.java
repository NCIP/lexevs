package edu.mayo.informatics.lexgrid.convert.exporters.xml.lgxml.factory;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.concepts.Entity;

public class ResolvedConceptReferenceFactory {

   public static ResolvedConceptReference createRcrAutoMaker() {
       Entity entity = EntityFactory.createEntityAutoMaker();
       return ResolvedConceptReferenceFactory.createRcrFromEntity(entity);       
   }
   
   public static ResolvedConceptReference createRcrAutomobile() {
       Entity entity = EntityFactory.createEntityAutomobile();
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
       ResolvedConceptReference rcr = new ResolvedConceptReference();
       rcr.setEntity(entity);      
       return rcr;
   }

}
