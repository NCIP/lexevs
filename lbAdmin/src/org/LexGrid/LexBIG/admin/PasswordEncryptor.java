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
package org.LexGrid.LexBIG.admin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.lexevs.system.utility.CryptoUtility;

public class PasswordEncryptor {

/**
     * @param args
     * @throws IOException 
     */
public static void main(String[] args) throws IOException {

        // Parse the command line ...
        CommandLine cl = null;
        Options options = getCommandOptions();

        try {
            cl = new BasicParser().parse(options, args);
        } catch (ParseException e) {
            Util.displayAndLogError("Issue with command line args: " + e.getMessage() , e);
            Util.displayCommandOptions("PasswordEncryptor", options, "PasswordEncryptor -p \"-- password --\"", e);
            return;
        }

        // Interpret provided values ...
        String password = cl.getOptionValue("p");

        String cipherText = CryptoUtility.encrypt(password);
        
        File file = new File("password.txt");
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(cipherText);
        } finally {
            if( writer != null ) {
                writer.close();
            }
        }
    }

    /**
     * Return supported command options.
     * 
     * @return org.apache.commons.cli.Options
     */
    private static Options getCommandOptions() {
        Options options = new Options();
        Option o;

        o = new Option("p", "password", true, "Password for encryption.");
        o.setArgName("password");
        o.setRequired(true);
        options.addOption(o);

        return options;
    }

}