
package org.LexGrid.LexBIG.gui.valueSetsView;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.gui.LB_VSD_GUI;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.URIMap;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Content provider for the Value Set Definition Supported Attributes SWT Table.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class SupportedAttributesContentProvider implements IStructuredContentProvider {
    private LB_VSD_GUI lbVDGui_;
    private static Logger log = LogManager.getLogger("LB_VSGUI_LOGGER");
    private URIMap[] currentSuppAttribRenderings_ = null;
    private ValueSetDefinition vsd_;

    public SupportedAttributesContentProvider(LB_VSD_GUI lbVDGui, ValueSetDefinition vsd) {
        lbVDGui_ = lbVDGui;
        vsd_ = vsd;
    }

    public Object[] getElements(Object arg0) {
        try {
            return getSupportedAttributes();
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
        currentSuppAttribRenderings_ = null;
    }

    private static String SUPPORTED_ATTRIB_GETTER_PREFIX = "_supported";
    
    @SuppressWarnings("unchecked")
    private URIMap[] getSupportedAttributes()
            throws LBInvocationException, URISyntaxException {
        if (currentSuppAttribRenderings_ == null) {
            if (lbVDGui_.getValueSetDefinitionService() != null &&  vsd_ != null && vsd_.getMappings() != null) {
                Mappings mappings = vsd_.getMappings();
                List<URIMap> uriMapsList = new ArrayList<URIMap>();
                for(Field field : mappings.getClass().getDeclaredFields()){
                    if(field.getName().startsWith(SUPPORTED_ATTRIB_GETTER_PREFIX)){
                        field.setAccessible(true);
                        try {
                            uriMapsList.addAll((List<URIMap>) field.get(mappings));
                        } catch (IllegalArgumentException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                
                if (uriMapsList.size() > 0)
                {
                    currentSuppAttribRenderings_ = new URIMap[uriMapsList.size()];
                    for (int i = 0; i < uriMapsList.size(); i++)
                    {
                        currentSuppAttribRenderings_[i] = uriMapsList.get(i);
                    }
                } else {
                    currentSuppAttribRenderings_ = new URIMap[] {};
                }
            Arrays.sort(currentSuppAttribRenderings_,
                        new URIMapComparator());
            } else {
                currentSuppAttribRenderings_ = new URIMap[] {};
            }

        }
        return currentSuppAttribRenderings_;
    }

    private class URIMapComparator implements Comparator<URIMap> {
        public int compare(URIMap o1, URIMap o2) {
            return (String.valueOf(o1.getLocalId()).compareToIgnoreCase(String.valueOf(o2.getLocalId())));
        }
    }

}