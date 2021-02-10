
package org.LexGrid.LexBIG.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;

/**
 * Example showing how to find codes matching descriptive text. The program
 * accepts up to two parameters...
 * 
 * The first param (required) indicates the text used to search matching
 * descriptions. Matches are determined through a customized match algorithm,
 * which uses a simple heuristic to try and rank returned values by relevance.
 * 
 * The second param (optional) indicates the type of entity to search. Possible
 * values include the LexGrid built-in types "concept" and "instance".
 * Additional supported types can be defined uniquely to a coding scheme. If
 * provided, this should be a comma-delimited list of types. If not provided,
 * all entity types are searched.
 * 
 * Example: FindCodesForDescription "blood" Example: FindCodesForDescription
 * "breast cancer" "concept"
 * 
 */
public class FindCodesForDescription {
    // Identify common stop words (words to be ignored in most match
    // circumstances).
    // This list extends from the LVG stop words ...
    static final List<String> STOP_WORDS = Arrays.asList(new String[] { "a", "an", "and", "by", "for", "of", "on",
            "in", "nos", "the", "to", "with" });

    public FindCodesForDescription() {
        super();
    }

    /**
     * Entry point for processing.
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Example: FindCodesForDescription \"breast cancer\" \"concept\"");
            return;
        }

        try {
            String phrase = args[0];
            String[] nodeTypes = null;
            if (args.length > 1)
                nodeTypes = args[1].split(",");
            new FindCodesForDescription().run(phrase, nodeTypes);
        } catch (Exception e) {
            Util.displayAndLogError("REQUEST FAILED !!!", e);
        }
    }

    public void run(String phrase, String[] nodeTypes) throws LBException {
        CodingSchemeSummary css = Util.promptForCodeSystem();
        if (css != null) {
            LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
            CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
            csvt.setVersion(css.getRepresentsVersion());
            LocalNameList typeList = Constructors.createLocalNameList(nodeTypes);

            ResolvedConceptReferencesIterator nodeRefs = search(lbs, css.getLocalName(), csvt, phrase, typeList);
            if (!nodeRefs.hasNext())
                Util.displayMessage("No matches found.");
            while (nodeRefs.hasNext()) {
                ResolvedConceptReference rcr = nodeRefs.next();
                Util.displayMessage("Code: " + rcr.getConceptCode());
                Util.displayMessage("\tCoding Scheme Name...: " + rcr.getCodingSchemeName());
                Util.displayMessage("\tCoding Scheme URI....: " + rcr.getCodingSchemeURI());
                Util.displayMessage("\tCoding Scheme Version: " + rcr.getCodingSchemeVersion());
                Util.displayMessage("\tCode Namespace...... : "
                        + (rcr.getCodeNamespace() != null ? rcr.getCodeNamespace() : "<default>"));
                Util.displayMessage("\tCode Description.... : "
                        + (rcr.getEntityDescription() != null ? rcr.getEntityDescription().getContent() : ""));
                String typeString = "";
                for (Iterator<? extends String> types = rcr.iterateEntityType(); types.hasNext();)
                    typeString += (types.next() + (types.hasNext() ? "," : ""));
                Util.displayMessage("\tCode Entity Types... : " + typeString);
            }
        }
    }

    protected ResolvedConceptReferencesIterator search(LexBIGService lbs, String codingSchemeName,
            CodingSchemeVersionOrTag csvt, String phrase, LocalNameList nodeTypeList) {
        try {
            CodedNodeSet cns = lbs.getNodeSet(codingSchemeName, csvt, nodeTypeList);
            cns.restrictToMatchingDesignations(phrase, SearchDesignationOption.PREFERRED_ONLY,
                    "DoubleMetaphoneLuceneQuery", null);
            ResolvedConceptReferencesIterator resultIterator = cns.resolve(null, null, null, null, true);
            return sortByScore(phrase, resultIterator, 100);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Sorts the given concept references based on a scoring algorithm designed
     * to provide a more natural ordering. Scores are determined by comparing
     * each reference against a provided search term.
     * 
     * @param searchTerm
     *            The term used for comparison; single or multi-word.
     * @param toSort
     *            The iterator containing references to sort.
     * @param maxToReturn
     *            Sets upper limit for number of top-scored items returned.
     * @return Iterator over sorted references.
     * @throws LBException
     */
    protected ResolvedConceptReferencesIterator sortByScore(String searchTerm,
            ResolvedConceptReferencesIterator toSort, int maxToReturn) throws LBException {
        // Determine the set of individual words to compare against.
        List<String> compareWords = toScoreWords(searchTerm);

        // Create a bucket to store results.
        Map<String, ScoredTerm> scoredResult = new TreeMap<String, ScoredTerm>();

        // Score all items ...
        while (toSort.hasNext()) {
            // Working in chunks of 100.
            ResolvedConceptReferenceList refs = toSort.next(100);
            for (int i = 0; i < refs.getResolvedConceptReferenceCount(); i++) {
                ResolvedConceptReference ref = refs.getResolvedConceptReference(i);
                String code = ref.getConceptCode();
                Entity node = ref.getEntity();

                // Note: Preferred descriptions carry more weight,
                // but we process all terms to allow the score to improve
                // based on any contained presentation.
                Presentation[] allTermsForEntity = node.getPresentation();
                for (Presentation p : allTermsForEntity) {
                    float score = score(p.getValue().getContent(), compareWords, p.isIsPreferred(), i);

                    // Check for a previous match on this code for a different
                    // presentation.
                    // If already present, keep the highest score.
                    if (scoredResult.containsKey(code)) {
                        ScoredTerm scoredTerm = (ScoredTerm) scoredResult.get(code);
                        if (scoredTerm.score > score)
                            continue;
                    }
                    scoredResult.put(code, new ScoredTerm(ref, score));
                }
            }
        }
        // Return an iterator that will sort the scored result.
        return new ScoredIterator(scoredResult.values(), maxToReturn);
    }

    /**
     * Returns a score providing a relative comparison of the given text against
     * a set of keywords.
     * <p>
     * Currently the score is evaluated as a simple percentage based on number
     * of words in the first set that are also in the second, with minor
     * adjustment for order (matching later words given slightly higher weight,
     * anticipating often the subject of search will follow descriptive text).
     * Weight is also increased for shorter phrases (measured in # words) If the
     * text is indicated to be preferred, the score is doubled to promote
     * 'bubbling to the top'.
     * <p>
     * Ranking from the original search is available but not currently used in
     * the heuristic (tends to throw-off desired alphabetic groupings later).
     * 
     * @param text
     * @param keywords
     * @param isPreferred
     * @param searchRank
     * @return The score; a higher value indicates a stronger match.
     */
    protected float score(String text, List<String> keywords, boolean isPreferred, float searchRank) {
        List<String> wordsToCompare = toScoreWords(text);
        float totalWords = wordsToCompare.size();
        float matchScore = 0;
        float position = 0;
        for (Iterator<String> words = wordsToCompare.listIterator(); words.hasNext(); position++) {
            String word = words.next();
            if (keywords.contains(word))
                matchScore += ((position / 10) + 1);
        }
        return Math.max(0, 100 + (matchScore / totalWords * 100) - (totalWords * 2)) * (isPreferred ? 2 : 1);
    }

    /**
     * Return words from the given string to be used in scoring algorithms, in
     * order of occurrence and ignoring duplicates, stop words, whitespace and
     * common separators.
     * 
     * @param s
     * @return List
     */
    protected List<String> toScoreWords(String s) {
        return toWords(s, "[\\s,:+-;]", true, true);
    }

    /**
     * Return words from the given string in order of occurrence, normalized to
     * lower case, separated by the given delimiters (regular expression), and
     * optionally removing stop words and duplicates.
     * 
     * @param s
     * @param delimitRegex
     * @param removeStopWords
     * @param removeDuplicates
     * @return List<String>
     */
    protected List<String> toWords(String s, String delimitRegex, boolean removeStopWords, boolean removeDuplicates) {
        String[] words = s.split(delimitRegex);
        List<String> adjusted = new ArrayList<String>();
        for (int i = 0; i < words.length; i++) {
            String temp = words[i].toLowerCase();
            if (removeDuplicates && adjusted.contains(temp))
                continue;
            if (!removeStopWords || !STOP_WORDS.contains(temp))
                adjusted.add(temp);
        }
        return adjusted;
    }
}