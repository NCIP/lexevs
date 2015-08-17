/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.lexevs.dao.database.prefix;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.lexevs.dao.database.access.registry.RegistryDao;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * The Class CyclingCharDbPrefixGenerator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CyclingCharDbPrefixGenerator implements InitializingBean, NextDatabasePrefixGenerator {
	
	/** The prefix length limit. */
	private int prefixLengthLimit = 4;
	
	private Registry registry;
	
	/** The prefix resolver. */
	private PrefixResolver prefixResolver;
	
	/** The ALPHABET. */
	public static char[] ALPHABET = new char[]{
		'A','B','C','D','E','F',
		'G','H','I','J','K','L',
		'M','N','O','P','Q','R','S',
		'T','U','V','W','X','Y','Z'};
	
	/** The FIRS t_ character. */
	public static char FIRST_CHARACTER = ALPHABET[0];
	
	/** The LAS t_ character. */
	public static char LAST_CHARACTER = ALPHABET[ALPHABET.length - 1];
	
	
	public static void main(String[] args) {
		CyclingCharDbPrefixGenerator g = new CyclingCharDbPrefixGenerator();
		
		String prefix = "aaaa";
		while(true) {
			prefix = g.generateNextDatabasePrefix(prefix);
			System.out.println(prefix);
		}
		
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.isTrue(prefixLengthLimit >= 2, "The minimum prefix size is 2");
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.prefix.NextDatabasePrefixGenerator#generateNextDatabasePrefix(java.lang.String)
	 */
	public String generateNextDatabasePrefix(String currentIdentifier) {
		currentIdentifier = currentIdentifier.toUpperCase();
		
		char[] chars = currentIdentifier.toCharArray();
		chars = adjustLength(chars);
		
		if(isInCycle(chars)){
			chars = new char[chars.length];
			for(int i=0;i<chars.length;i++) {
				chars[i] = FIRST_CHARACTER;
			}
		}
		String prefix = new String(incrementByOne(chars)).toLowerCase();
		
		while(this.doesPrefixAlreadyExistInDatabase(prefix)) {
			prefix = this.generateNextDatabasePrefix(prefix);
		}
		
		return prefix;
	}
	
	protected boolean doesPrefixAlreadyExistInDatabase(String prefix) {
		List<String> usedPrefixes = new ArrayList<String>();
		
		for(RegistryEntry entry : this.registry.getAllRegistryEntriesOfType(ResourceType.CODING_SCHEME)) {
			usedPrefixes.add(entry.getPrefix());
			usedPrefixes.add(entry.getStagingPrefix());
		}
		
		return usedPrefixes.contains(prefix);	
	}
	
	/**
	 * Increment by one.
	 * 
	 * @param chars the chars
	 * 
	 * @return the char[]
	 */
	public char[] incrementByOne(char[] chars){
		for(int i=chars.length-1;i>=0;i--){
			if(chars[i] != LAST_CHARACTER){
				chars[i] = findNextChar(chars[i]);
				return chars;
			} else {
				chars[i] = FIRST_CHARACTER;
			}
		}
		throw new RuntimeException("All Prefixes have been used.");
	}
	
	/**
	 * Checks if is in cycle.
	 * 
	 * @param chars the chars
	 * 
	 * @return true, if is in cycle
	 */
	protected boolean isInCycle(char[] chars){
		boolean isLastChar = true;
		
		for(char c : chars) {
			isLastChar = isLastChar && c == LAST_CHARACTER;
		}
		
		return isLastChar;
	}

	/**
	 * Generate starting cycling prefix.
	 * 
	 * @return the char[]
	 */
	protected char[] generateStartingCyclingPrefix(){
		char[] prefix = new char[this.prefixLengthLimit];
		for(int i=0;i< prefix.length -1;i++){
			prefix[i] = FIRST_CHARACTER;
		}
		prefix[this.prefixLengthLimit -1 ] = LAST_CHARACTER;
		
		return prefix;
	}
	
	/**
	 * Adjust length.
	 * 
	 * @param chars the chars
	 * 
	 * @return the char[]
	 */
	protected char[] adjustLength(char[] chars){
		if(chars.length < prefixLengthLimit){
			chars = adjustLength(ArrayUtils.add(chars, FIRST_CHARACTER));
		} else if(chars.length > prefixLengthLimit){
			chars = ArrayUtils.subarray(chars, 0, prefixLengthLimit);
		}
		return chars;
	}
	
	/**
	 * Needs extra char.
	 * 
	 * @param chars the chars
	 * 
	 * @return true, if successful
	 */
	protected boolean needsExtraChar(char[] chars){
		return (chars[chars.length-1] == LAST_CHARACTER);
	}
	
	/**
	 * Find next char.
	 * 
	 * @param charToFind the char to find
	 * 
	 * @return the char
	 */
	protected char findNextChar(char charToFind){
		int alphabetIndex = ArrayUtils.indexOf(ALPHABET, charToFind);
		return ALPHABET[alphabetIndex + 1];
	}

	/**
	 * Sets the prefix length limit.
	 * 
	 * @param prefixLengthLimit the new prefix length limit
	 */
	public void setPrefixLengthLimit(int prefixLengthLimit) {
		this.prefixLengthLimit = prefixLengthLimit;
	}

	/**
	 * Gets the prefix length limit.
	 * 
	 * @return the prefix length limit
	 */
	public int getPrefixLengthLimit() {
		return prefixLengthLimit;
	}

	/**
	 * Gets the prefix resolver.
	 * 
	 * @return the prefix resolver
	 */
	public PrefixResolver getPrefixResolver() {
		return prefixResolver;
	}

	/**
	 * Sets the prefix resolver.
	 * 
	 * @param prefixResolver the new prefix resolver
	 */
	public void setPrefixResolver(PrefixResolver prefixResolver) {
		this.prefixResolver = prefixResolver;
	}

	public Registry getRegistry() {
		return registry;
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}
}