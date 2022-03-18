
package edu.mayo.informatics.lexgrid.convert.directConversions.hl7.mif.vocabulary;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.logging.messaging.impl.CachingMessageDirectorImpl;

public class MifVocabulary2LGMain {

    private LgMessageDirectorIF messages;
//    private LoaderPreferences loaderPrefs = null; // CRS

    public CodingScheme map(LgMessageDirectorIF lg_messages, MifVocabularyModel mifVocabularyModel) throws Exception {
        
        messages = new CachingMessageDirectorImpl(lg_messages);
        
        CodingScheme csclass = null;

        try {
            csclass = new CodingScheme();
            MifVocabularyMapToLexGrid mifVocabMap = new MifVocabularyMapToLexGrid(messages, mifVocabularyModel);
            mifVocabMap.initRun(csclass);
            messages.info("Processing DONE!!");
        } catch (Exception e) {
            messages.fatalAndThrowException("Load failed due to errors mapping parsed HL7 MIF Vocabulary content into LexGrid objects", e);
        }

        return csclass;

    }

//    public void setLoaderPrefs(LoaderPreferences loaderPrefs) {
//        this.loaderPrefs = loaderPrefs;
//    }

    
}