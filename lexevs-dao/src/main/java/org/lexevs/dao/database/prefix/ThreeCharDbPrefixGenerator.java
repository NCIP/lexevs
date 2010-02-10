package org.lexevs.dao.database.prefix;

import org.apache.commons.lang.ArrayUtils;

public class ThreeCharDbPrefixGenerator implements NextDatabasePrefixGenerator {
	
	public static char[] ALPHABET = new char[]{
		'A','B','C','D','E','F',
		'G','H','I','J','K','L',
		'M','N','O','P','Q','R','S',
		'T','U','V','W','X','Y','Z'};

	public String generateNextDatabasePrefix(String currentIdentifier) {
		currentIdentifier = currentIdentifier.toUpperCase();
		
		char[] chars = currentIdentifier.toCharArray();
		if(needsExtraChar(chars)){
			return new String(addNewChar(chars)).toLowerCase();
		} else {
			return new String(incrementByOne(chars)).toLowerCase();
		}
	}
	
	protected char[] incrementByOne(char[] chars){
		chars[chars.length-1] = findNextChar(chars[chars.length-1]);
		return chars;
	}
	
	protected char[] addNewChar(char[] chars){
		return ArrayUtils.add(chars, chars.length, ALPHABET[0]);
	}
	
	protected boolean needsExtraChar(char[] chars){
		return (chars[chars.length-1] == ALPHABET[ALPHABET.length - 1]);
	}
	
	protected char findNextChar(char charToFind){
		int alphabetIndex = ArrayUtils.indexOf(ALPHABET, charToFind);
		return ALPHABET[alphabetIndex + 1];
	}
}
