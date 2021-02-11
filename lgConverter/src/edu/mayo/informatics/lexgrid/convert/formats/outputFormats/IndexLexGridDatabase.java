
package edu.mayo.informatics.lexgrid.convert.formats.outputFormats;

import java.io.File;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.formats.Option;
import edu.mayo.informatics.lexgrid.convert.formats.OutputFormatInterface;

/**
 * Output format for building a lucene index..
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class IndexLexGridDatabase implements OutputFormatInterface {
    public static final String description = "Index LexGrid Database";

    protected String indexLocation_;
    protected String indexName_;
    protected boolean normEnabled_ = false;
    protected String normConfigFile_;

    public IndexLexGridDatabase() {

    }

    public IndexLexGridDatabase(String indexLocation, String indexName, boolean normEnabled, String normConfigFile) {
        this.indexLocation_ = indexLocation;
        this.indexName_ = indexName;
        this.normEnabled_ = normEnabled;
        this.normConfigFile_ = normConfigFile;
    }

    public String getConnectionSummary() {
        return description + "\n" + "Builds a Lucene Index for a LexGrid database.\n"
                + "The index will be written to the folder '" + indexLocation_ + "'.\n" + "The index will be named '"
                + indexName_ + "'.\n" + "Normalization is " + (normEnabled_ ? "enabled" : "disabled") + ".\n"
                + (normEnabled_ ? "The configuration file for Norm is '" + normConfigFile_ + "'." : "");
    }

    public String getDescription() {
        return description;
    }

    public String testConnection() throws ConnectionFailure {
        File file = new File(indexLocation_);
        if (file.exists() && file.isDirectory()) {
            if (indexName_ != null && indexName_.length() > 0) {
                if (normEnabled_) {
                    file = new File(normConfigFile_);
                    if (file.exists() && file.isFile()) {
                        return "";
                    } else {
                        throw new ConnectionFailure("The file '" + normConfigFile_ + "' does not exist");
                    }
                }
            } else {
                throw new ConnectionFailure("The index name is required.");
            }
        } else {
            throw new ConnectionFailure("The directory '" + indexLocation_ + "' does not exist");
        }
        return "";
    }

    public Option[] getOptions() {
        return new Option[] { new Option(Option.BUILD_DB_METAPHONE, new Boolean(true)),
                new Option(Option.USE_COMPOUND_FMT, new Boolean(false)),
                new Option(Option.BUILD_STEM, new Boolean(true)) };
    }

    public String getIndexLocation() {
        return this.indexLocation_;
    }

    public String getIndexName() {
        return this.indexName_;
    }

    public String getNormConfigFile() {
        return this.normConfigFile_;
    }

    public boolean isNormEnabled() {
        return this.normEnabled_;
    }

}