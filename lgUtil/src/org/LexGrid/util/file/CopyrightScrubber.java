
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

public class CopyrightScrubber {
    protected static String COPYRIGHT = "";
    
//    protected static String COPYRIGHT = "/*" + "\n"
//            + " * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and " + "\n"
//            + " * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the" + "\n"
//            + " * triple-shield Mayo logo are trademarks and service marks of MFMER." + "\n" + " *" + "\n"
//            + " * Except as contained in the copyright notice above, or as used to identify " + "\n"
//            + " * MFMER as the author of this software, the trade names, trademarks, service" + "\n"
//            + " * marks, or product names of the copyright holder shall not be used in" + "\n"
//            + " * advertising, promotion or otherwise in connection with this software without" + "\n"
//            + " * prior written authorization of the copyright holder." + "\n" + " * " + "\n"
//            + " * Licensed under the Eclipse Public License, Version 1.0 (the \"License\");" + "\n"
//            + " * you may not use this file except in compliance with the License." + "\n"
//            + " * You may obtain a copy of the License at " + "\n" + " * " + "\n"
//            + " * \t\thttp://www.eclipse.org/legal/epl-v10.html" + "\n" + " * " + "\n" + " */";
    protected Pattern P_head = Pattern.compile("^(/\\*.*?\\*/.*?)?(package.*)");
    protected Pattern P_body = Pattern.compile("^([^/]*)(/\\*\\*.*?\\*/)(.*)");
    protected Pattern P_oldComment = Pattern.compile("(.*)(Copyright:)(.*?)(<p>)(.*)(\\*/)");
    protected Pattern P_oldAuthor = Pattern.compile("(.*?)(@author.*?)(\\*\\*.*)");
    protected Pattern P_nonGen = Pattern.compile("(.*@non-generated.*)");
    protected char RSub = '\036';
    protected char NSub = '\037';

    /**
     * Displays basic information regarding the program and its usage to
     * standard output.
     */
    public static void displayHelp() {
        System.out.println("Command syntax:");
        System.out.println("	java org.LexGrid.util.file.CopyrightScrubber <rootPath>");
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
                CopyrightScrubber pgm = new CopyrightScrubber();
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

    public CopyrightScrubber() {
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
        for (int x=0; x< args.length;  x++) {
            File root = new File(args[x]);
            if (!root.exists() || !root.isDirectory())
                throw new IOException("root not found or not a directory");
            process(root);
        }
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
                StringBuffer sb_out = new StringBuffer(COPYRIGHT);
                Matcher m_head = P_head.matcher(unprocessed);
                if (m_head.matches() && m_head.group(2).length() > 0)
                    unprocessed = trimWhiteSpace(m_head.group(2));
                Matcher m_body = P_body.matcher(unprocessed);
                if (m_body.matches()) {
                    String decl = trimWhiteSpace(m_body.group(1));
                    String comment = trimWhiteSpace(m_body.group(2));
                    ;
                    String code = trimWhiteSpace(m_body.group(3));
                    ;
                    sb_out.append("\r\n").append(toMultiLine(decl));
                    if (comment.length() > 0) {
                        Matcher m_oldComment = P_oldComment.matcher(comment);
                        if (m_oldComment.matches()) {
                            comment = trimWhiteSpace(m_oldComment.group(5));
                            Matcher m_oldAuthor = P_oldAuthor.matcher(m_oldComment.group(3));
                            if (m_oldAuthor.matches())
                                comment = comment + "\r\n *\r\n * " + trimWhiteSpace(m_oldAuthor.group(2));
                            Matcher m_nonGen = P_nonGen.matcher(m_oldComment.group(3));
                            if (m_nonGen.matches())
                                comment = comment + "\r\n * @non-generated";
                            sb_out.append("\r\n\r\n/**\r\n ").append(toMultiLine(comment)).append("\r\n */\r\n");
                        } else
                            sb_out.append("\r\n\r\n").append(toMultiLine(comment)).append("\r\n");
                    }
                    sb_out.append(toMultiLine(code));
                } else
                    sb_out.append("\r\n").append(toMultiLine(unprocessed));

                OutputStream out = new BufferedOutputStream(new FileOutputStream(f));
                out.write(sb_out.toString().getBytes());
                out.close();
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