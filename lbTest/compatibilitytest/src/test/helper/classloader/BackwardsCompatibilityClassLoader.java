package test.helper.classloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import org.LexGrid.LexBIG.Impl.dataAccess.ResourceManager;
import org.LexGrid.LexBIG.Impl.helpers.MyClassLoader;

public class BackwardsCompatibilityClassLoader extends URLClassLoader {

    public BackwardsCompatibilityClassLoader() {
        super(new URL[]{}, null);   
        System.setProperty("LG_CONFIG_FILE", "resources/config/lbconfig.props");
        
        File lexbigjar = new File("compatibilitytest/jar/lbRuntime.jar");      
       
       
   
            ArrayList<File> temp = getJarsFromFolders(ResourceManager.instance().getSystemVariables()
                    .getJarFileLocations());
            try {  
                
                super.addURL(lexbigjar.toURL());
                
            for(File file : temp){
                System.out.println("loading " + file);
                super.addURL(file.toURL());
            }
        
     
          
      

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
        
        private static ArrayList<File> getJarsFromFolders(String[] fileNames) {
            ArrayList<File> files = new ArrayList<File>();
            for (int j = 0; j < fileNames.length; j++) {
                File fileName = new File(fileNames[j]);
                files.addAll(getJarsFromFolder(fileName));
            }
            return files;
        }

        private static ArrayList<File> getJarsFromFolder(File fileName) {
            ArrayList<File> files = new ArrayList<File>();

            if (fileName.exists()) {
                if (fileName.isDirectory()) {
                    File[] children = fileName.listFiles();
                    for (int i = 0; i < children.length; i++) {
                        files.addAll(getJarsFromFolder(children[i]));
                    }
                } else if (fileName.getName().toLowerCase().endsWith(".jar")) {
                    files.add(fileName);
                }
            }
            return files;
        }
}
