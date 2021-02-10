
package org.LexGrid.LexBIG.Impl.helpers;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Query.Filter;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.LexGrid.commonTypes.Source;

/**
 * Filter to use for JUnit test cases. This one only allows items that have an
 * 'a' as their second letter in the entity descrition.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class TestFilter implements Filter {
    public static String provider_ = "Mayo Clinic";
    final static String role_ = "author"; // 2006 model change
    final static Source providerSource = new Source(); // 2006 model change
    public static String description_ = "Filter for JUnit tests";
    public static String name_ = "JUnit Test Filter";
    public static String version_ = "1.0";

    public boolean match(ResolvedConceptReference ref) {
        return ref.getEntityDescription().getContent().charAt(1) == 'a';
    }

    public String getDescription() {
        return description_;
    }

    public String getName() {
        return name_;
    }

    public String getProvider() {
        return provider_;
    }

    public String getVersion() {
        return version_;
    }

    public static void register() throws LBParameterException, LBException {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setExtensionBaseClass(TestFilter.class.getInterfaces()[0].getName());
        ed.setExtensionClass(TestFilter.class.getName());
        ed.setDescription(description_);
        ed.setName(name_);
        ed.setVersion(version_);
        // Update for 2006 Model
        providerSource.setContent(provider_); // 2006 model change
        providerSource.setRole(role_); // 2006 model change
        ed.setExtensionProvider(providerSource); // 2006 model change
        // ed.setExtensionProvider(provider_);

        // I'm registering them this way to avoid the lexBig service manager
        // API.
        // If you are writing an add-on extension, you should register them
        // through the
        // proper interface.
        ExtensionRegistryImpl.instance().registerFilterExtension(ed);
    }

}