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