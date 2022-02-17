
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
import org.lexevs.registry.WriteLockManager;
import org.lexevs.system.ResourceManager;
import org.lexevs.system.constants.SystemVariables;

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
        //TODO: Adapt the transfer scheme utilities to 6.0
       throw new UnsupportedOperationException("Registry is now Database-based.");
    }

    public static void main(String[] args) throws LBInvocationException, LBParameterException, IOException {
        exportToLocation(new File("C:\\temp\\test"));
    }
}