
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