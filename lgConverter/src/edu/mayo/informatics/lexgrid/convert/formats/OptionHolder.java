
package edu.mayo.informatics.lexgrid.convert.formats;

import java.util.Hashtable;

/**
 * Class to simplify the collection of Options.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public class OptionHolder {
    private Hashtable options;

    public OptionHolder() {
        options = new Hashtable();
    }

    public boolean contains(String optionName) {
        return options.containsKey(optionName);
    }

    public Option get(String optionName) {
        return (Option) options.get(optionName);
    }

    public void add(Option option) {
        options.put(option.getOptionName(), option);
    }

    public Option remove(String optionName) {
        return (Option) options.remove(optionName);
    }
}