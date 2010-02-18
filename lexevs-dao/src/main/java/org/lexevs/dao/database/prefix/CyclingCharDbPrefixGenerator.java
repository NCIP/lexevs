package org.lexevs.dao.database.prefix;

import org.apache.commons.lang.ArrayUtils;
import org.lexevs.dao.database.utility.DatabaseUtility;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class CyclingCharDbPrefixGenerator implements InitializingBean, NextDatabasePrefixGenerator {
	
	private int prefixLengthLimit = 4;
	
	private DatabaseUtility databaseUtility;
	
	private String testDatabaseName = "lexGridTableMetaData";
	
	private PrefixResolver prefixResolver;
	
	public static char[] ALPHABET = new char[]{
		'A','B','C','D','E','F',
		'G','H','I','J','K','L',
		'M','N','O','P','Q','R','S',
		'T','U','V','W','X','Y','Z'};
	
	public static char FIRST_CHARACTER = ALPHABET[0];
	public static char LAST_CHARACTER = ALPHABET[ALPHABET.length - 1];
	

	public void afterPropertiesSet() throws Exception {
		Assert.isTrue(prefixLengthLimit >= 2, "The minimum prefix size is 2");
	}

	public String generateNextDatabasePrefix(String currentIdentifier) {
		currentIdentifier = currentIdentifier.toUpperCase();
		
		char[] chars = currentIdentifier.toCharArray();
		chars = adjustLength(chars);
		
		if(isInCycle(chars)){
			return new String(
					findNextInCycle( generateStartingCyclingPrefix() )).toLowerCase();
		} else {
			return new String(incrementByOne(chars)).toLowerCase();
		}
	}
	
	protected char[] incrementByOne(char[] chars){
		for(int i=0;i<chars.length;i++){
			if(chars[i] != LAST_CHARACTER){
				chars[i] = findNextChar(chars[i]);
				return chars;
			}
		}
		throw new RuntimeException("All Prefixes have been used.");
	}
	
	protected boolean isInCycle(char[] chars){
		return (chars.length == prefixLengthLimit
				&& chars[chars.length - 1] == LAST_CHARACTER);
	}
	
	protected char[] findNextInCycle(char[] chars){
		if(this.databaseUtility.doesTableExist(prefixResolver.resolveDefaultPrefix() + new String(chars).toLowerCase() + this.testDatabaseName)){
			return findNextInCycle(incrementByOne(chars));
		} else {
			return chars;
		}
	}
	
	protected char[] generateStartingCyclingPrefix(){
		char[] prefix = new char[this.prefixLengthLimit];
		for(int i=0;i< prefix.length -1;i++){
			prefix[i] = FIRST_CHARACTER;
		}
		prefix[this.prefixLengthLimit -1 ] = LAST_CHARACTER;
		
		return prefix;
	}
	
	protected char[] adjustLength(char[] chars){
		if(chars.length < prefixLengthLimit){
			chars = adjustLength(ArrayUtils.add(chars, FIRST_CHARACTER));
		} else if(chars.length > prefixLengthLimit){
			chars = ArrayUtils.subarray(chars, 0, prefixLengthLimit);
		}
		return chars;
	}
	
	protected boolean needsExtraChar(char[] chars){
		return (chars[chars.length-1] == LAST_CHARACTER);
	}
	
	protected char findNextChar(char charToFind){
		int alphabetIndex = ArrayUtils.indexOf(ALPHABET, charToFind);
		return ALPHABET[alphabetIndex + 1];
	}

	public void setPrefixLengthLimit(int prefixLengthLimit) {
		this.prefixLengthLimit = prefixLengthLimit;
	}

	public int getPrefixLengthLimit() {
		return prefixLengthLimit;
	}

	public void setDatabaseUtility(DatabaseUtility databaseUtility) {
		this.databaseUtility = databaseUtility;
	}

	public DatabaseUtility getDatabaseUtility() {
		return databaseUtility;
	}

	public String getTestDatabaseName() {
		return testDatabaseName;
	}

	public void setTestDatabaseName(String testDatabaseName) {
		this.testDatabaseName = testDatabaseName;
	}

	public PrefixResolver getPrefixResolver() {
		return prefixResolver;
	}

	public void setPrefixResolver(PrefixResolver prefixResolver) {
		this.prefixResolver = prefixResolver;
	}
}
