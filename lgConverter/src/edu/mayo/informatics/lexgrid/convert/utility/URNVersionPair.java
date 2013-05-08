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
package edu.mayo.informatics.lexgrid.convert.utility;

public class URNVersionPair {
    String urn, version;

    public URNVersionPair(String urn, String version) {
        this.urn = urn;
        this.version = version;
    }

    public String getUrn() {
        return urn;
    }

    public String getVersion() {
        return version;
    }

    public static URNVersionPair[] stringArrayToNullVersionPairArray(String[] stringArray) {
        URNVersionPair[] pairArray = new URNVersionPair[stringArray.length];
        for (int i = 0; i < stringArray.length; i++) {
            pairArray[i] = new URNVersionPair(stringArray[i], null);
        }
        return pairArray;
    }

    public static String[] getCodingSchemeNames(URNVersionPair[] pairArray) {
        String[] stringArray = new String[pairArray.length];
        for (int i = 0; i < pairArray.length; i++) {
            stringArray[i] = pairArray[i].getUrn();
        }
        return stringArray;
    }
}