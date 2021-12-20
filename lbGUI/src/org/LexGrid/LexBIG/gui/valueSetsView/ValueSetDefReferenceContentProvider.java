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
package org.LexGrid.LexBIG.gui.valueSetsView;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.gui.LB_VSD_GUI;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Content provider for the Value Set Definition Reference Contents SWT Table.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class ValueSetDefReferenceContentProvider implements IStructuredContentProvider {
    private LB_VSD_GUI lbVDGui_;
    private static Logger log = LogManager.getLogger("LB_VSGUI_LOGGER");
    private org.LexGrid.valueSets.DefinitionEntry[] currentDEntryRenderings_ = null;
    private ValueSetDefinition vsd_;

    public ValueSetDefReferenceContentProvider(LB_VSD_GUI lbVDGui, ValueSetDefinition vsd) {
        lbVDGui_ = lbVDGui;
        vsd_ = vsd;
    }

    public Object[] getElements(Object arg0) {
        try {
            return getDefinitionEntries();
        } catch (LBInvocationException e) {
            log.error("Unexpected Error", e);
            return new String[] {};
        } catch (URISyntaxException e) {
            log.error("Unexpected Error", e);
            return new String[] {};
        }
    }

    public void dispose() {
        // do nothing
    }

    public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
        currentDEntryRenderings_ = null;
    }

    private org.LexGrid.valueSets.DefinitionEntry[] getDefinitionEntries()
            throws LBInvocationException, URISyntaxException {
        if (currentDEntryRenderings_ == null) {
            List<DefinitionEntry> defListToReturn = new ArrayList<DefinitionEntry>();
            
            if (lbVDGui_.getValueSetDefinitionService() != null) {
                List<DefinitionEntry> dEntries = new ArrayList<DefinitionEntry>();
                if (vsd_ != null)
                    dEntries = vsd_.getDefinitionEntryAsReference();
                
                for (DefinitionEntry de : dEntries)
                {
                    if (de.getValueSetDefinitionReference() != null)
                        defListToReturn.add(de);
                }
                
                if (dEntries.size() > 0)
                {
                    currentDEntryRenderings_ = new DefinitionEntry[defListToReturn.size()];
                    for (int i = 0; i < defListToReturn.size(); i++)
                    {
                        currentDEntryRenderings_[i] = defListToReturn.get(i);
                    }
                } else {
                    currentDEntryRenderings_ = new DefinitionEntry[] {};
                }
            Arrays.sort(currentDEntryRenderings_,
                        new DefinitionEntryRenderingComparator());
            } else {
                currentDEntryRenderings_ = new DefinitionEntry[] {};
            }

        }
        return currentDEntryRenderings_;
    }

    private class DefinitionEntryRenderingComparator implements
            Comparator<DefinitionEntry> {

        public int compare(DefinitionEntry o1, DefinitionEntry o2) {
            return (String.valueOf(o1.getRuleOrder()).compareToIgnoreCase(String.valueOf(o2.getRuleOrder())));
        }

    }

}