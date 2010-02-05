/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG.Impl.dataAccess;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;

/**
 * This class assists in exporting LexGrid config information.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class ExportUtility {
    public static void exportToLocation(File targetLocation) throws LBInvocationException, IOException,
            LBParameterException {
        if (!targetLocation.isDirectory()) {
            throw new LBParameterException("Target Location must be a directory", "targetLocation");
        }

        SystemVariables sv = ResourceManager.instance().getSystemVariables();
        File registry = new File(sv.getAutoLoadRegistryPath());
        WriteLockManager wm = WriteLockManager.instance();
        try {
            wm.lockLockFile();
            copyFile(registry, new File(targetLocation, registry.getName()));
        } finally {
            wm.releaseLockFile();
        }

        File config = new File(sv.getConfigFileLocation());
        copyFile(config, new File(targetLocation, config.getName()));

        if (wm.getLockCount() != 0) {
            throw new LBParameterException("Can't do an export while a load is happening");
        }

        File index = new File(sv.getAutoLoadIndexLocation());
        recursiveCopy(index, targetLocation);

    }

    private static void recursiveCopy(File sourceFile, File targetLocation) throws IOException {
        if (sourceFile.isFile()) {
            copyFile(sourceFile, new File(targetLocation, sourceFile.getName()));
            return;
        }
        // source file must be a directory

        File targetFolder = new File(targetLocation, sourceFile.getName());
        targetFolder.mkdir();

        File[] subFiles = sourceFile.listFiles();
        for (int i = 0; i < subFiles.length; i++) {
            recursiveCopy(subFiles[i], targetFolder);
        }
    }

    private static void copyFile(File sourceFile, File targetFile) throws IOException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        bis = new BufferedInputStream(new FileInputStream(sourceFile));
        bos = new BufferedOutputStream(new FileOutputStream(targetFile));

        byte[] buffer = new byte[2048];
        while (true) {
            int bytes_read = 0;

            bytes_read = bis.read(buffer);

            if (bytes_read == -1) {
                break;
            }
            bos.write(buffer, 0, bytes_read);
        }

        bis.close();
        bos.close();

        targetFile.setLastModified(sourceFile.lastModified());
    }

    public static void copyAndEditRegistry(File copyTo, String oldValue, String newValue) throws LBInvocationException,
            IOException {
        File registry = ResourceManager.instance().getRegistry().getRegistryFile();
        BufferedReader reader = new BufferedReader(new FileReader(registry));
        BufferedWriter writer = new BufferedWriter(new FileWriter(copyTo));

        WriteLockManager wm = WriteLockManager.instance();
        try {
            wm.lockLockFile();

            String currentLine = reader.readLine();
            while (currentLine != null) {
                int pos = currentLine.indexOf(oldValue);
                if (pos != -1) {
                    currentLine = currentLine.substring(0, pos) + newValue
                            + currentLine.substring(pos + oldValue.length());
                }

                writer.write(currentLine);
                writer.newLine();

                currentLine = reader.readLine();
            }
            reader.close();
            writer.close();
        } finally {
            wm.releaseLockFile();
        }
    }

    public static void main(String[] args) throws LBInvocationException, LBParameterException, IOException {
        exportToLocation(new File("C:\\temp\\test"));
    }
}