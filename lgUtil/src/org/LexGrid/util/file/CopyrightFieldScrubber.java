
package org.LexGrid.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CopyrightFieldScrubber {
    protected Pattern cOldPattern = Pattern.compile("(.*)(String copyright = \")(.*?)(\";.*)");
    protected String cNew = "Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the triple-shield Mayo logo are trademarks and service marks of MFMER.Except as contained in the copyright notice above, or as used to identify MFMER as the author of this software, the trade names, trademarks, service marks, or product names of the copyright holder shall not be used in advertising, promotion or otherwise in connection with this software without prior written authorization of the copyright holder. Licensed under the Eclipse Public License, Version 1.0 (the \\\"License\\\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.eclipse.org/legal/epl-v10.html.";
    protected char RSub = '\036';
    protected char NSub = '\037';

    /**
     * Displays basic information regarding the program and its usage to
     * standard output.
     */
    public static void displayHelp() {
        System.out.println("Command syntax:");
        System.out.println("	java org.LexGrid.util.file.CopyrightFieldScrubber <rootPath>");
    }

    /**
     * Program entry point.
     * 
     * @param args
     *            String[]
     */
    public static void main(String[] args) {
        try {
            if (args.length < 1)
                displayHelp();
            else {
                CopyrightFieldScrubber pgm = new CopyrightFieldScrubber();
                pgm.run(args);
            }
        } catch (Throwable t) {
            System.out.println("An error occurred: ");
            t.printStackTrace(System.out);
            System.out.println("Please correct the error and retry the request.");
            System.out.println();
            displayHelp();
        }
    }

    public CopyrightFieldScrubber() {
        super();
    }

    /**
     * Runs the import utility based on the provided arguments.
     * 
     * @param args
     *            String[]
     * @throws Exception
     */
    protected void run(String[] args) throws Exception {
        File root = new File(args[0]);
        if (!root.exists() || !root.isDirectory())
            throw new IOException("root not found or not a directory");
        process(root);
    }

    protected void process(File root) throws IOException {
        File[] content = root.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isDirectory() || pathname.getPath().endsWith(".java");
            }
        });

        for (int i = 0; i < content.length; i++) {
            File f = content[i];
            if (f.isDirectory())
                process(f);
            else {
                System.out.println("Scrubbing file: " + f.getPath());
                InputStream in = new BufferedInputStream(new FileInputStream(f));
                StringBuffer sb_in = new StringBuffer(2048);
                int ch;
                while ((ch = in.read()) >= 0)
                    sb_in.append((char) ch);
                in.close();

                String unprocessed = toSingleLine(sb_in.toString());
                StringBuffer sb_out = new StringBuffer();
                Matcher m = cOldPattern.matcher(unprocessed);
                if (m.matches()) {
                    sb_out.append(toMultiLine(m.group(1) + m.group(2) + cNew + m.group(4)));

                    OutputStream out = new BufferedOutputStream(new FileOutputStream(f));
                    out.write(sb_out.toString().getBytes());
                    out.close();
                }
            }
        }
    }

    protected String toSingleLine(String s) {
        StringBuffer sb = new StringBuffer(s.length());
        boolean isLeadingWhitespace = true;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '\r' || ch == '\n' && !isLeadingWhitespace)
                sb.append(ch == '\r' ? RSub : NSub);
            else {
                isLeadingWhitespace = false;
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    protected String toMultiLine(String s) {
        StringBuffer sb = new StringBuffer(s.length());
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            sb.append(ch == RSub ? '\r' : (ch == NSub ? '\n' : ch));
        }
        return sb.toString();
    }

    protected String trimWhiteSpace(String s) {
        return trimTrailingWhiteSpace(trimLeadingWhiteSpace(s));
    }

    protected String trimLeadingWhiteSpace(String s) {
        int firstNonWhite = -1;
        for (int i = 0; i < s.length() && firstNonWhite < 0; i++) {
            char ch = s.charAt(i);
            if (!Character.isWhitespace(ch) && ch != RSub && ch != NSub)
                firstNonWhite = i;
        }
        return firstNonWhite > 0 ? s.substring(firstNonWhite) : s;
    }

    protected String trimTrailingWhiteSpace(String s) {
        int lastNonWhite = -1;
        for (int i = s.length() - 1; i > 0 && lastNonWhite < 0; i--) {
            char ch = s.charAt(i);
            if (!Character.isWhitespace(ch) && ch != RSub && ch != NSub)
                lastNonWhite = i;
        }
        return lastNonWhite > 0 ? s.substring(0, lastNonWhite + 1) : s;
    }
}