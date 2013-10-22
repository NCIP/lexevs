package edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary

import java.awt.event.ItemEvent;

import groovy.util.XmlParser


class ValueSetParser {
    String filename
   
    
    
 //   int parseCSCount(){ def CSCount = parser.codeSystem.size()}
    
    List getCodeSystems(){
        def parser = new XmlParser().parse(filename)
        parser.codeSystem
    }
    
    List getValueSets(){
        def parser = new XmlParser().parse(filename)
        parser.valueSet
    }
    
    String getCodeSystemNameForOID(String oid){
        def slurper = new XmlSlurper().parse(filename)
       slurper.codeSystem.findAll{ it.@codeSystemId == oid }.@name
    }
    
    Object getCodeSystemForOID(oid){
        def slurper = new XmlSlurper().parse(filename)
        slurper.codeSystem.findAll{ it.@codeSystemId == oid }
    }
    
    Object getEntityForOidandCode(String oid, String code){
        def vsCodeSystem = getCodeSystemForOID(oid)
        def releasedVersion = getReleasedVersionFromCodeSystemNode(vsCodeSystem)
        def mifModel
        releasedVersion.children().each {concept ->
            if (concept.name() == "concept" && concept.code.@code[0] == code){
            mifModel = makeNewConceptModel(concept, vsCodeSystem)
            }
        }
        mifModel
    }
    
    Object makeNewConceptModel(concept, vsCodeSystem){
        new MifValueSetEntityModel(
            concept.conceptProperty.@value.text(), 
            drillDownToTextFromAnnotations(concept), 
            concept.printName.@text.text(), 
            vsCodeSystem[0].@codeSystemId.text(),
            vsCodeSystem[0].@title.text(), 
            concept.code.@code.text())
    }
    
    Object getReleasedVersionFromCodeSystemNode(node){
        node[0].children().each{ releasedVersion -> 
            if (releasedVersion.name() == "releasedVersion"){
              return  releasedVersion
            }         
        }
    }
    
    String drillDownToTextFromAnnotations(node){
        def string
        node.annotations.documentation.definition.text.each {it.each {it.each{string = it}}}
        string
    }
    
    
    Object getEntitiesForCodeSystemOid(String oid){
        def slurper = new XmlParser().parse(filename)
        def list = [] as Set
//        def vsCodeSystem = slurper.codeSystem.findAll{ it.@codeSystemId == oid }
        def vsCodeSystem = getCodeSystemForOID(oid)
//        vsCodeSystem[0].children().each{
//             releaseVersion -> if (releaseVersion.name().localPart == "releasedVersion"){
        def releaseVersion = getReleasedVersionFromCodeSystemNode(vsCodeSystem)
            releaseVersion.children().each {concept -> 
                if (concept.name() == "concept"){
//                list.add(new MifValueSetEntityModel(
//                    concept.conceptProperty.@value[0], 
//                    drillDownToTextFromAnnotations(concept), 
//                    concept.printName.@text[0],
//                    vsCodeSystem[0].@title,
//                    vsCodeSystem[0].@codeSystemId[0] ,
//                    concept.code.@code[0] ))
                  list.add(makeNewConceptModel(concept, vsCodeSystem))
                }
                }
//            }       
//
//        }
        list
    }


}

