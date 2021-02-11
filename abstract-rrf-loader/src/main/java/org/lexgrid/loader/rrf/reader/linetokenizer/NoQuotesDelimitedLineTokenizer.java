
package org.lexgrid.loader.rrf.reader.linetokenizer;

import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

/**
 * The Class NoQuotesDelimitedLineTokenizer.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class NoQuotesDelimitedLineTokenizer extends DelimitedLineTokenizer {

	/**
	 * Don't treat ANY character as a quote character.
	 * 
	 * @param c the c
	 * 
	 * @return true, if checks if is quote character
	 */
	protected boolean isQuoteCharacter(char c) {
		return false;
	}

}