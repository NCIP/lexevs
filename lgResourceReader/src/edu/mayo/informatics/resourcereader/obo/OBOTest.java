
package edu.mayo.informatics.resourcereader.obo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OBOTest {

    // private static final String REGEX = "(\\w*):(\\w*)(\\s*\"?\\w*\"?)(,?)";
    private static final String REGEX = "((\\w*):(\\w*))";
    private static final String INPUT = "dog:doggie dog doggie dogg, GO:ma,GO:blah \"abba abbcd\" ";

    public static void main(String[] args) {
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(INPUT); // get a matcher object
        int count = 0;
        while (m.find()) {
            count++;
            System.out.println("Match number " + count);
            System.out.println("start(): " + m.start());
            System.out.println("end(): " + m.end());
            System.out.println("group(1): " + m.group(1));
            System.out.println("group(2): " + m.group(2));
            System.out.println("group(3): " + m.group(3));
        }
    }
}