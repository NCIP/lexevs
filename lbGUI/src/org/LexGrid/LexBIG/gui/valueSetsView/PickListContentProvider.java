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

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.gui.LB_VSD_GUI;
import org.LexGrid.valueSets.PickListDefinition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Content provider for the Pick List SWT Table.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class PickListContentProvider implements IStructuredContentProvider {
    private LB_VSD_GUI lbVDGui_;
    private static Logger log = LogManager.getLogger("LB_VSGUI_LOGGER");
    private PickListDefinition[] currentPLRenderings_ = null;

    public PickListContentProvider(LB_VSD_GUI lbVDGui) {
        lbVDGui_ = lbVDGui;
    }

    public Object[] getElements(Object arg0) {
        try {
            return getPickList();
        } catch (LBInvocationException e) {
            log.error("Unexpected Error", e);
            return new String[] {};
        }
    }

    public void dispose() {
        // do nothing
    }

    public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
        currentPLRenderings_ = null;
    }

    private PickListDefinition[] getPickList()
            throws LBInvocationException {
        if (currentPLRenderings_ == null) {
            if (lbVDGui_.getPickListDefinitionService() != null) {
                try {
                    List<String> pickListIds = lbVDGui_.getPickListDefinitionService().listPickListIds();
                    
                    if (pickListIds != null && pickListIds.size() > 0)
                    {
                        currentPLRenderings_ = new PickListDefinition[pickListIds.size()];
                        for (int i = 0; i < pickListIds.size(); i++)
                        {
                                currentPLRenderings_[i] = lbVDGui_.getPickListDefinitionService().getPickListDefinitionById(pickListIds.get(i));
                        }
                    }
                } catch (LBException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (currentPLRenderings_ != null)
                    Arrays.sort(currentPLRenderings_, new PickListRenderingComparator());
                else
                    currentPLRenderings_ = new PickListDefinition[] {};
            } else {
                currentPLRenderings_ = new PickListDefinition[] {};
            }

        }
        return currentPLRenderings_;
    }

    private class PickListRenderingComparator implements
            Comparator<PickListDefinition> {

        public int compare(PickListDefinition o1, PickListDefinition o2) {
            return o1.getPickListId()
                    .compareToIgnoreCase(
                            o2.getPickListId());
        }

    }

}