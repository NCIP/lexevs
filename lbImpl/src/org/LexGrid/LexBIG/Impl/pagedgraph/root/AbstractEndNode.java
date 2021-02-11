
package org.LexGrid.LexBIG.Impl.pagedgraph.root;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.locator.LexEvsServiceLocator;

public abstract class AbstractEndNode extends ConceptReference {

    private static final long serialVersionUID = 4346299905579305877L;

    public static String ROOT = "@";
    public static String TAIL = "@@";
    
    protected AbstractEndNode(String uri, String version, String code) {
        try {
            this.setCode(code);
            this.setCodeNamespace(
            LexEvsServiceLocator.getInstance().
                getSystemResourceService().
                getInternalCodingSchemeNameForUserCodingSchemeName(uri, version)
            );
        } catch (LBParameterException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static class Root extends AbstractEndNode {
        private static final long serialVersionUID = 9146396838103778981L;

        protected Root(String uri, String version) {
            super(uri, version, ROOT);
        }  
    }
    
    public static class Tail extends AbstractEndNode {
        private static final long serialVersionUID = -7014931215754506298L;

        protected Tail(String uri, String version) {
            super(uri, version, TAIL);
        }  
    }
}