package org.LexGrid.LexBIG.gui;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CodeSystemUserMetaData {

    private Map<String, String> userMetaData;
    
    public CodeSystemUserMetaData(Map <String, String> metadata){
        userMetaData = metadata;
    }
    

    public Map<String, String> getUserMetaData() {
        return userMetaData;
    }

    public void setUserMetaData(Map<String, String> userMetaData) {
        this.userMetaData = userMetaData;
    }
    
    public String toString(){
        StringBuilder builder = new StringBuilder();
        Formatter formatter = new Formatter(builder, Locale.US);
        formatter.format("%-50.50s%-50.50s%n",  "Name",  "Value");
        builder.append("\n");
        for (Map.Entry<String, String> entry : userMetaData.entrySet()) {
            formatter.format("%-50.50s%-50.50s%n", entry.getKey(),  entry.getValue());
        }
        formatter.close();
       return builder.toString();
    }
    
    public static void main(String[] args){
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("this", "That");
        map.put("another","thing");
        map.put("verylongnamehere", "sowhat");
        System.out.println(new CodeSystemUserMetaData(map).toString());
    }

}
