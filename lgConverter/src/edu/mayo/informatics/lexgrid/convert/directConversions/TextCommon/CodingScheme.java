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
package edu.mayo.informatics.lexgrid.convert.directConversions.TextCommon;

/**
 * CodingScheme holder for Text loaders.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 5882 $ checked in on $Date: 2007-09-10
 *          15:50:31 +0000 (Mon, 10 Sep 2007) $
 */
public class CodingScheme {
    public String codingSchemeName = "";
    public String codingSchemeId = "";
    public String defaultLanguage = "";
    public String representsVersion = "";
    public String formalName = "";
    public String source = "";
    public String entityDescription = "";
    public String copyright = "Copyright: (c) 2004-2007 Mayo Foundation for Medical Education and Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the triple-shield Mayo logo are trademarks and service marks of MFMER.Except as contained in the copyright notice above, or as used to identify MFMER as the author of this software, the trade names, trademarks, service marks, or product names of the copyright holder shall not be used in advertising, promotion or otherwise in connection with this software without prior written authorization of the copyright holder. Licensed under the Eclipse Public License, Version 1.0 (the \"License\"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.eclipse.org/legal/epl-v10.html.";
    public Concept[] concepts;
    public Association[] associations;

    public boolean isTypeB = false;
}