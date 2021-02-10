
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