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
        formatter.format("  %-2s:  %-2s%n",  "Name",  "Value");
        builder.append("\n");
        if(userMetaData == null | userMetaData.size() == 0){
            formatter.close();
            return "No user meta data defined for this coding scheme";
        }
        for (Map.Entry<String, String> entry : userMetaData.entrySet()) {
            formatter.format("  %-2s:  %-2s%n", entry.getKey(),  entry.getValue());
        }
        formatter.close();
       return builder.toString();
    }
}
