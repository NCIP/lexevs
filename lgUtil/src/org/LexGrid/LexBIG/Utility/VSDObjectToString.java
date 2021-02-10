
package org.LexGrid.LexBIG.Utility;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociatedDataList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.CodedNodeReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeTagList;
import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeVersionList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ExtensionDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.LexGrid.LexBIG.DataModel.Collections.ModuleDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.NCIChangeEventList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortDescriptionList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Collections.SupportedElementList;
import org.LexGrid.LexBIG.DataModel.Collections.SystemReleaseList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedData;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodedNodeReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.LogEntry;
import org.LexGrid.LexBIG.DataModel.Core.MetadataProperty;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ReferenceLink;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedCodedNodeReference;
import org.LexGrid.LexBIG.DataModel.Core.SupportedElement;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExportStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ModuleDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ProcessStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.RenderingDetail;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortDescription;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SortOption;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.SystemReleaseDetail;
import org.LexGrid.LexBIG.DataModel.NCIHistory.NCIChangeEvent;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.codingSchemes.CodingSchemes;
import org.LexGrid.commonTypes.Describable;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.commonTypes.VersionableAndDescribable;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.concepts.PropertyLink;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedHierarchy;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.naming.URIMap;
import org.LexGrid.relations.AssociatableElement;
import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.valueSets.CodingSchemeReference;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.PickListDefinitions;
import org.LexGrid.valueSets.PickListEntry;
import org.LexGrid.valueSets.PickListEntryExclusion;
import org.LexGrid.valueSets.PickListEntryNode;
import org.LexGrid.valueSets.PickListEntryNodeChoice;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitionReference;
import org.LexGrid.valueSets.ValueSetDefinitions;
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.EditHistory;
import org.LexGrid.versions.EntityVersion;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.SystemRelease;
import org.apache.commons.lang.StringUtils;

/**
 * Class to turn LexBIG model objects into representative strings.
 * <p>
 * Note: This class was introduced as a helper.  Since objects
 * are generated from the xml master schema, the corresponding
 * toString() methods cannot be easily customized.  This class
 * allows for customized and formatted printout of each object
 * type.
 * <p>
 * To fulfill toString() for a new object, the only thing required
 * is to add a corresponding append() method for that class.
 */
public class VSDObjectToString {
    private static final String lineBreak = System.getProperty("line.separator");
    private static final String wordBreak = " \t\n\r\f";
    private static ThreadLocal<String> breakAndIndent = new ThreadLocal<String>();
    private static ThreadLocal<Integer> lineWrapAt = new ThreadLocal<Integer>();
    private static final int defaultLineWrapAt = 80;
    private static final String sp4 = "    ";
    private static final String sp8 = "        ";
    private static final DateFormat mmddyyyy = new SimpleDateFormat("MM/dd/yyyy");
    private static final DateFormat mmddyyyyhhmmss = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

    /**
     * Returns a string representation of the object.
     * 
     * @param object
     *            The object to evaluate.
     */
    public static String toString(Object o) {
        return toString(o, "");
    }

    /**
     * Returns a string representation of the object, indented according to the
     * specified parameter values and wrapped at a predetermined default length
     * suitable for most displays.
     * 
     * @param object
     *            The object to evaluate.
     * @param indent
     *            A string, typically whitespace, prepended to each new
     *            line of the returned value.
     */
    @SuppressWarnings("unchecked")
    public static String toString(Object o, String indent) {
        StringBuffer buff = new StringBuffer(128);
        String breakAndIndentPrev = getBreakAndIndent();
        try {
            setBreakAndIndent(lineBreak + indent + sp4);
            
            // Null check ...
            if (o == null) {
                buff.append(indent).append("<unspecified>");
            }
            // Break down arrays to sub-components ...
            else if (o instanceof Object[]) {
                Object[] temp = (Object[]) o;
                buff.append(indent)
                    .append("Array of ").append(temp.length).append(" items: ");
                
                for (int i = 0; i < temp.length; i++)
                    buff.append(lineBreak)
                        .append(toString(temp[i], indent + sp4));
            }
            // Break down iterators and collections to sub-components ...
            else if (o instanceof Iterable) {
                buff.append(indent)
                    .append(o instanceof Collection
                            ? "Collection of " + ((Collection) o).size() + " items:"
                            : "Iterator over the following items:");
                
                Iterator<Object> iter = ((Iterable) o).iterator();
                while (iter.hasNext())
                    buff.append(lineBreak)
                        .append(toString(iter.next(), indent + sp4));
            }
    
            // Invoke individual methods based off of the object class.
            // Reflection is used to find an append method that matches
            // the object type.  If a method is not found for the
            // immediate class, the superclass is checked, then it's
            // superclass, etc, until one is found.
            else {
                Class oClazz = o.getClass();
                Method appendMethod = null;
                boolean added = false;
                try {
                    do {
                        try {
                            appendMethod =
                                VSDObjectToString.class.getDeclaredMethod("append",
                                    new Class[] {StringBuffer.class, String.class, oClazz});
                        } catch (NoSuchMethodException e) {
                        }
                        
                        // Not found?  Check the superclass ...
                        if (appendMethod == null)
                            oClazz = oClazz.getSuperclass();
                        
                    } while (appendMethod == null && oClazz != null);
            
                    // Found?  Invoke now ...
                    if (appendMethod != null) {
                        appendMethod.invoke(VSDObjectToString.class, new Object[] {buff, indent, o});
                        added = true;
                    }
                    
                } catch (Exception e) {
                } finally {
                    // Just in case ...
                    if (!added)
                        buff.append(indent).append(o.toString());
                }
            }
        } finally {
            setBreakAndIndent(breakAndIndentPrev);
        }
        return buff.toString();
    }
    
    /**
     * Returns a string representation of the object, with text and
     * descriptions indented and wrapped according to the specified
     * parameter values.
     * 
     * @param object
     *            The object to evaluate.
     * @param indent
     *            A string, typically whitespace, prepended to each new
     *            line of the returned value.
     * @param wrapAt
     *            Maximum number of characters in each line; a value < 0
     *            indicates no wrap. A value of 0 indicates to use a
     *            default wrap value, suitable for most displays.
     */
    public static String toString(Object o, String indent, int wrapAt) {
        int wrapAtPrev = getWrapAt();
        try {
            // Wrap at specified boundary ...
            setWrapAt(wrapAt);
            return toString(o, indent);
        } finally {
            // Always restore prior value ...
            setWrapAt(wrapAtPrev);
        }
    }

    ///////////////////
    // Helper Methods
    ///////////////////
    /**
     * Returns the class name for the given object
     * to insert into printed representations.
     */
    protected static String getType(Object o) {
        return o.getClass().getSimpleName();
    }
    
    /**
     * Returns the text used to introduce line break
     * and indentation as set for the current thread.
     */
    protected static String getBreakAndIndent() {
        return breakAndIndent.get();
    }

    /**
     * Assigns the text used to introduce line break
     * and indentation as set for the current thread.
     */
    protected static void setBreakAndIndent(String s) {
        breakAndIndent.set(s);
    }

    /**
     * Returns the requested line wrap position
     * set for the current thread.
     * <p>
     * Note: If < 0, no wrap is performed.  0 indicates
     * to use the default wrap value.  If > 0, wrapping
     * is performed on the specified boundary.
     */
    protected static int getWrapAt() {
        Integer i = lineWrapAt.get();
        return i != null ? i : 0;
    }
    
    /**
     * Assigns the requested line wrap position
     * set for the current thread.
     * <p>
     * Note: If < 0, no wrap is performed.  0 indicates
     * to use the default wrap value.  If > 0, wrapping
     * is performed on the specified boundary.
     */
    protected static void setWrapAt(int i) {
        lineWrapAt.set(i);
    }
    
    /**
     * Appends the given text to the buffer, breaking at word
     * and line boundaries as dictated by whitespace and a maximum
     * length. Additional lines are indented an extra tab position.
     * <p>
     * Note: The wrap position is set on a per-thread basis using
     * the lineWrapAt ThreadLocal variable.
     * @param buff  Buffer to append to; not null.
     * @param prefix  Prefix to insert on first line; not null.
     * @param indent  Indentation string; not null.
     * @param text  The text to append.
     */
    protected static void appendAndWrap(StringBuffer buff, String prefix, String indent, String text) {
        buff.append(indent).append(prefix);
        int linePos = indent.length() + prefix.length();

        // Null check, just in case.
        if (text == null) {
            buff.append("<unspecified>");
            return;
        }
        
        // Are we wrapping?
        int wrapPos = getWrapAt();
        if (wrapPos < 0) {
            buff.append(text);
            return;
        } else if (wrapPos == 0) {
            wrapPos = defaultLineWrapAt;
        }
        
        // Wrap was requested.  Break on word and line
        // boundaries, indenting subsequent lines.
        StringTokenizer st = new StringTokenizer(text, wordBreak, true);
        boolean sourceLineIsEmpty = true;
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            
            // At a wrap boundary?  Start a new line
            // and append the token.
            if (linePos + token.length() >= wrapPos) {
                buff.append(lineBreak).append(indent).append(sp4).append(token);
                linePos = indent.length() + sp4.length() + token.length();
            }
            // Still inside of a line of source text?
            // Ensure indent and add the token.
            else if (!lineBreak.contains(token)) {
                if (linePos <= 0) {
                    buff.append(indent).append(sp4);
                    linePos = indent.length() + sp4.length();
                }
                buff.append(token);
                linePos += token.length();
                sourceLineIsEmpty = sourceLineIsEmpty && token.trim().length() == 0;
            }
            // Source defined a text break?  Keep track
            // and add a break if the source line was intentionally
            // left blank as whitespace.  If not, add a
            // space to separate consecutive words that were
            // separated by only a line break.
            else {
                if (sourceLineIsEmpty) {
                    buff.append(lineBreak);
                    linePos = 0;
                } else
                    buff.append(' ');
                sourceLineIsEmpty = true;
            }
        }
    }
    
    ///////////////////////////////
    // LexBIG Collections package
    ///////////////////////////////
    protected static void append(StringBuffer buff, String indent, AbsoluteCodingSchemeVersionReferenceList o) {
        buff.append(indent).append(getType(o)).append(lineBreak)
            .append(toString(o.getAbsoluteCodingSchemeVersionReference(), indent + sp4));
    }

    protected static void append(StringBuffer buff, String indent, AssociatedConceptList o) {
        buff.append(indent).append(getType(o)).append(lineBreak)
            .append(toString(o.getAssociatedConcept(), indent + sp4));
    }

    protected static void append(StringBuffer buff, String indent, AssociatedDataList o) {
        buff.append(indent).append(getType(o)).append(lineBreak)
            .append(toString(o.getAssociatedData(), indent + sp4));
    }

    protected static void append(StringBuffer buff, String indent, AssociationList o) {
        buff.append(indent).append(getType(o)).append(lineBreak)
            .append(toString(o.getAssociation(), indent + sp4));
    }

    protected static void append(StringBuffer buff, String indent, CodingSchemeRenderingList o) {
        buff.append(indent).append(getType(o)).append(lineBreak)
            .append(toString(o.getCodingSchemeRendering(), indent + sp4));
    }

    protected static void append(StringBuffer buff, String indent, CodingSchemeTagList o) {
        buff.append(indent).append(getType(o)).append(lineBreak)
            .append(toString(o.getTag(), indent + sp4));
    }

    protected static void append(StringBuffer buff, String indent, ConceptReferenceList o) {
        buff.append(indent).append(getType(o)).append(lineBreak)
            .append(toString(o.getConceptReference(), indent + sp4));
    }

    protected static void append(StringBuffer buff, String indent, CodingSchemeVersionList o) {
        buff.append(indent).append(getType(o)).append(lineBreak)
            .append(toString(o.getEntry(), indent + sp4));
    }

    protected static void append(StringBuffer buff, String indent, CodedNodeReferenceList o) {
        buff.append(indent).append(getType(o)).append(lineBreak)
            .append(toString(o.getCodedNodeReference(), indent + sp4));
    }

    protected static void append(StringBuffer buff, String indent, ExtensionDescriptionList o) {
        buff.append(indent).append(getType(o)).append(lineBreak)
            .append(toString(o.getExtensionDescription(), indent + sp4));
    }

    protected static void append(StringBuffer buff, String indent, LocalNameList o) {
        buff.append(indent).append(getType(o)).append(lineBreak)
            .append(toString(o.getEntry(), indent + sp4));
    }

    protected static void append(StringBuffer buff, String indent, MetadataPropertyList o) {
        buff.append(indent).append(getType(o)).append(lineBreak)
            .append(toString(o.getMetadataProperty(), indent + sp4));
    }

    protected static void append(StringBuffer buff, String indent, ModuleDescriptionList o) {
        buff.append(indent).append(getType(o)).append(lineBreak)
            .append(toString(o.getModuleDescription(), indent + sp4));
    }

    protected static void append(StringBuffer buff, String indent, NameAndValueList o) {
        buff.append(indent).append(getType(o)).append(lineBreak)
            .append(toString(o.getNameAndValue(), indent + sp4));
    }

    protected static void append(StringBuffer buff, String indent, NCIChangeEventList o) {
        buff.append(indent).append(getType(o)).append(lineBreak)
            .append(toString(o.getEntry(), indent + sp4));
    }

    protected static void append(StringBuffer buff, String indent, ResolvedConceptReferenceList o) {
        buff.append(indent).append(getType(o)).append(getBreakAndIndent())
            .append("isIncomplete: ").append(o.getIncomplete()).append(lineBreak)
            .append(toString(o.getResolvedConceptReference(), indent + sp4));
    }

    protected static void append(StringBuffer buff, String indent, SortDescriptionList o) {
        buff.append(indent).append(getType(o)).append(lineBreak)
            .append(toString(o.getSortDescription(), indent + sp4));
    }

    protected static void append(StringBuffer buff, String indent, SortOptionList o) {
        buff.append(indent).append(getType(o)).append(lineBreak)
            .append(toString(o.getEntry(), indent + sp4));
    }

    protected static void append(StringBuffer buff, String indent, SupportedElementList o) {
        buff.append(indent).append(getType(o)).append(lineBreak)
            .append(toString(o.getSupportedElement(), indent + sp4));
    }

    protected static void append(StringBuffer buff, String indent, SystemReleaseList o) {
        buff.append(indent).append(getType(o)).append(lineBreak)
            .append(toString(o.getSystemRelease(), indent + sp4));
    }
    
    ////////////////////////
    // LexBIG Core package
    ////////////////////////
    protected static void append(StringBuffer buff, String indent, AbsoluteCodingSchemeVersionReference o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("URN: ").append(o.getCodingSchemeURN())
            .append(getBreakAndIndent())
            .append("Version: " ).append(o.getCodingSchemeVersion());
    }
    
    protected static void append(StringBuffer buff, String indent, AssociatedConcept o) {
        append(buff, indent, (ResolvedCodedNodeReference) o);
        buff.append(getBreakAndIndent())
            .append("IsNavigable: ").append(o.getIsNavigable())
            .append(getBreakAndIndent())
            .append("Association Qualifiers").append(lineBreak)
            .append(toString(o.getAssociationQualifiers(), indent + sp8));
    }
    
    protected static void append(StringBuffer buff, String indent, AssociatedData o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("ID: ").append(o.getId())
            .append(getBreakAndIndent())
            .append("Data Type: ").append(o.getDataType())
            .append(getBreakAndIndent())
            .append("Content: ").append(o.getContent());
        if (o.getAnyObjectCount() > 0) {
            buff.append(getBreakAndIndent())
                .append("Any Object Entries: ").append(lineBreak)
                .append(toString(o.getAnyObject(), indent + sp8));
        }
    }
    
    protected static void append(StringBuffer buff, String indent, Association o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Name: ").append(o.getAssociationName())
            .append(getBreakAndIndent())
            .append("Directional Name: ").append(o.getDirectionalName());
        if (o.getAssociationReference() != null)
            buff.append(getBreakAndIndent())
                .append("Association Reference: ").append(lineBreak)
                .append(toString(o.getAssociationReference(), indent + sp8));
        if (o.getAssociatedConcepts() != null && o.getAssociatedConcepts().getAssociatedConceptCount() > 0) {
            buff.append(getBreakAndIndent())
                .append("Associated Concepts: ").append(lineBreak)
                .append(toString(o.getAssociatedConcepts(), indent + sp8));
        }
    }
    
    protected static void append(StringBuffer buff, String indent, CodedNodeReference o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Coding Scheme Name: ").append(o.getCodingSchemeName())
            .append(getBreakAndIndent())
            .append("Code: ").append(o.getCode());
    }
    
    protected static void append(StringBuffer buff, String indent, CodingSchemeSummary o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Coding Scheme Description: ").append(lineBreak)
            .append(toString(o.getCodingSchemeDescription(), indent + sp8))
            .append(getBreakAndIndent())
            .append("Coding Scheme URI: ").append(lineBreak)
            .append(toString(o.getCodingSchemeURI(), indent + sp8))
            .append(getBreakAndIndent())
            .append("Formal Name: ").append(lineBreak)
            .append(toString(o.getFormalName(), indent + sp8))
            .append(getBreakAndIndent())
            .append("Local Name: ").append(lineBreak)
            .append(toString(o.getLocalName(), indent + sp8))
            .append(getBreakAndIndent())
            .append("Represents Version: ").append(lineBreak)
            .append(toString(o.getRepresentsVersion(), indent + sp8));
    }
    
    protected static void append(StringBuffer buff, String indent, CodingSchemeVersionOrTag o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Tag: ").append(o.getTag())
            .append(getBreakAndIndent())
            .append("Version: ").append(o.getVersion());
    }
    
    protected static void append(StringBuffer buff, String indent, ConceptReference o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Coding Scheme Name: ").append(o.getCodingSchemeName())
            .append(getBreakAndIndent())
            .append("Code: ").append(o.getCode());
    }
    
    protected static void append(StringBuffer buff, String indent, LogEntry o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Message #: ").append(o.getMessageNumber())
            .append(getBreakAndIndent())
            .append("Entry Level: ").append(o.getEntryLevel());
        if (o.getEntryTime() != null)
            buff.append(getBreakAndIndent())
                .append("Entry Time: ").append(mmddyyyyhhmmss.format(o.getEntryTime()));
        buff
            .append(getBreakAndIndent())
            .append("Program Name: ").append(o.getProgramName())
            .append(getBreakAndIndent())
            .append("Message Text: ").append(o.getMessage());
    }
    
    protected static void append(StringBuffer buff, String indent, MetadataProperty o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Name: ").append(o.getName())
            .append(getBreakAndIndent())
            .append("Value: ").append(o.getValue())
            .append(getBreakAndIndent())
            .append("Coding Scheme URI: ").append(o.getCodingSchemeURI())
            .append(getBreakAndIndent())
            .append("Coding Scheme Version: ").append(o.getCodingSchemeVersion());
        if (o.getContextCount() > 0) {
            buff.append(getBreakAndIndent())
                .append("Contexts: ").append(lineBreak)
                .append(toString(o.getContext(), indent + sp8));
        }
    }
    
    protected static void append(StringBuffer buff, String indent, NameAndValue o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Name: ").append(o.getName())
            .append(getBreakAndIndent())
            .append("Content: ").append(o.getContent());
    }
    
    protected static void append(StringBuffer buff, String indent, ReferenceLink o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("HRef: ").append(o.getHref())
            .append(getBreakAndIndent())
            .append("Content: ").append(o.getContent());
    }
    
    protected static void append(StringBuffer buff, String indent, ResolvedCodedNodeReference o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Coding Scheme Name: ").append(
                    o.getCodingSchemeName() == null ? "<null>" : o.getCodingSchemeName())
            .append(getBreakAndIndent())
            .append("Coding Scheme URI: ").append(
                    o.getCodingSchemeURI() == null ? "<null>" : o.getCodingSchemeURI())
            .append(getBreakAndIndent())
            .append("Code: ").append(o.getConceptCode());
        if (o.getEntityDescription() != null)
            buff.append(lineBreak)
                .append(toString(o.getEntityDescription(), indent + sp4));
        if (o.getEntity() != null)
            buff.append(getBreakAndIndent())
                .append("Referenced Entry: ").append(lineBreak)
                .append(toString(o.getEntity(), indent + sp8));
        if (o.getSourceOf() != null)
            buff.append(getBreakAndIndent())
                .append("Source Of: ").append(lineBreak)
                .append(toString(o.getSourceOf(), indent + sp8));
        if (o.getTargetOf() != null)
            buff.append(getBreakAndIndent())
                .append("Target Of: ").append(lineBreak)
                .append(toString(o.getTargetOf(), indent + sp8));
    }
    
    protected static void append(StringBuffer buff, String indent, SupportedElement o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Is Complete: ").append(o.getIsComplete());
    }
    
    //////////////////////////////////////
    // LexBIG Interface Elements package
    //////////////////////////////////////
    protected static void append(StringBuffer buff, String indent, CodingSchemeRendering o) {
        buff.append(indent).append(getType(o))
            .append(lineBreak)
            .append(toString(o.getCodingSchemeSummary(), indent + sp4))
            .append(lineBreak)
            .append(toString(o.getRenderingDetail(), indent + sp4));
        if (o.getServiceHandle() != null &&
                (o.getServiceHandle().getHref() != null || o.getServiceHandle().getContent() != null))
            buff.append(lineBreak).append(
                toString(o.getServiceHandle(), indent + sp4));
        if (o.getReferenceLink() != null &&
                (o.getReferenceLink().getHref() != null || o.getReferenceLink().getContent() != null))
            buff.append(lineBreak).append(
                toString(o.getReferenceLink(), indent + sp4));
    }
    
    protected static void append(StringBuffer buff, String indent, ExportStatus o) {
        append(buff, indent, (ProcessStatus) o);
        buff.append(getBreakAndIndent())
            .append("Destination: ").append(o.getDestination());
    }
    
    protected static void append(StringBuffer buff, String indent, ExtensionDescription o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Extension Name: ").append(o.getName())
            .append(getBreakAndIndent())
            .append("Description: ").append(o.getDescription())
            .append(getBreakAndIndent())
            .append("Base Class: ").append(o.getExtensionBaseClass())
            .append(getBreakAndIndent())
            .append("Extension Class: ").append(o.getExtensionClass())
            .append(getBreakAndIndent())
            .append("Version: ").append(o.getVersion());
    }
    
    protected static void append(StringBuffer buff, String indent, LoadStatus o) {
        append(buff, indent, (ProcessStatus) o);
        buff.append(getBreakAndIndent())
            .append("Source: ").append(o.getLoadSource())
            .append(getBreakAndIndent())
            .append("Number of Entities Loaded: ").append(o.getNumConceptsLoaded())
            .append(getBreakAndIndent())
            .append("Number of Relations Loaded: ").append(o.getNumRelationsLoaded());
        }
    
    protected static void append(StringBuffer buff, String indent, ModuleDescription o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Name: ").append(o.getName())
            .append(getBreakAndIndent())
            .append("Version: ").append(o.getVersion())
            .append(getBreakAndIndent())
            .append("Description: ").append(o.getDescription());
    }
    
    protected static void append(StringBuffer buff, String indent, ProcessStatus o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("State: ").append(o.getState())
            .append(getBreakAndIndent())
            .append("Message: ").append(o.getMessage())
            .append(getBreakAndIndent())
            .append("Warnings Logged: ").append(o.getWarningsLogged())
            .append(getBreakAndIndent())
            .append("Errors Logged: ").append(o.getErrorsLogged());
        if (o.getStartTime() != null)
            buff.append(getBreakAndIndent())
                .append("Start Time: ")
                .append(mmddyyyyhhmmss.format(o.getStartTime()));
        if (o.getEndTime() != null)
            buff.append(getBreakAndIndent())
                .append("End Time: ")
                .append(mmddyyyyhhmmss.format(o.getEndTime()));
    }
   
    protected static void append(StringBuffer buff, String indent, RenderingDetail o) {
        buff.append(indent).append(getType(o));
        if (o.getDeactivateDate() != null)
            buff.append(getBreakAndIndent())
                .append("Deactivate date: ")
                .append(mmddyyyy.format(o.getDeactivateDate()));
        if (o.getLastUpdateTime() != null)
            buff.append(getBreakAndIndent())
                .append("Last update time: ")
                .append(mmddyyyy.format(o.getLastUpdateTime()));
        if (o.getVersionStatus() != null)
            buff.append(getBreakAndIndent())
                .append("Version Status: ").append(lineBreak)
                .append(toString(o.getVersionStatus(), indent + sp8));
        if (o.getVersionTags() != null && o.getVersionTags().getTagCount() > 0)
            buff.append(getBreakAndIndent())
                .append("Version Tags: ").append(lineBreak)
                .append(toString(o.getVersionTags(), indent + sp8));
    }
    
    protected static void append(StringBuffer buff, String indent, SortDescription o) {
        append(buff, indent, (ExtensionDescription) o);
        if (o.getRestrictToContextCount() > 0) {
            buff.append(getBreakAndIndent())
                .append("Restrict to Context: ").append(lineBreak)
                .append(toString(o.getRestrictToContext(), indent + sp8));
        }
    }
    
    protected static void append(StringBuffer buff, String indent, SortOption o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Extension Name: ").append(o.getExtensionName())
            .append(getBreakAndIndent())
            .append("Ascending: ").append(o.getAscending());
    }
    
    protected static void append(StringBuffer buff, String indent, SystemReleaseDetail o) {
        buff.append(indent).append(getType(o))
            .append(lineBreak)
            .append(toString(o.getEntityVersions(), indent + sp4));
    }
    
    ///////////////////////////
    // LexBIG History package
    ///////////////////////////
    protected static void append(StringBuffer buff, String indent, NCIChangeEvent o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Code: ").append(o.getConceptcode())
            .append(getBreakAndIndent())
            .append("Edit Action: ").append(o.getEditaction());
        if (o.getEditDate() != null)
            buff.append(getBreakAndIndent())
                .append("Edit Date: ").append(mmddyyyy.format(o.getEditDate()));
        if (StringUtils.isNotBlank(o.getConceptName()))
            buff.append(getBreakAndIndent())
                .append("Concept Name: ").append(o.getConceptName());
        if (StringUtils.isNotBlank(o.getReferencecode()))
            buff.append(getBreakAndIndent())
                .append("Reference Code: ").append(o.getReferencecode());
        if (StringUtils.isNotBlank(o.getReferencename()))
            buff.append(getBreakAndIndent())
                .append("Reference Name: ").append(o.getReferencename());
   }

    ///////////////////////////////////
    // LexGrid Coding Schemes package
    ///////////////////////////////////
    protected static void append(StringBuffer buff, String indent, CodingScheme o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Name: ").append(o.getCodingSchemeName())
            .append(getBreakAndIndent())
            .append("Formal Name: ").append(o.getFormalName())
            .append(getBreakAndIndent())
            .append("URI: ").append(o.getCodingSchemeURI())
            .append(getBreakAndIndent())
            .append("Approximate Number of Concepts: ").append(o.getApproxNumConcepts())
            .append(getBreakAndIndent())
            .append("Default Language: ").append(o.getDefaultLanguage())
            .append(getBreakAndIndent())
            .append("Represents Version: ").append(o.getRepresentsVersion());
        if (o.getEntityDescription() != null)
            buff.append(lineBreak)
                .append(toString(o.getEntityDescription(), indent + sp4));
        if (o.getLocalNameCount() > 0)
            buff.append(getBreakAndIndent())
                .append("Local Names: ").append(lineBreak)
                .append(toString(o.getLocalName(), indent + sp8));
        if (o.getCopyright() != null)
            buff.append(getBreakAndIndent())
                .append("Copyright: ").append(lineBreak)
                .append(toString(o.getCopyright(), indent + sp8));
        if (o.getMappings() != null)
            buff.append(getBreakAndIndent())
                .append("Mappings: ").append(lineBreak)
                .append(toString(o.getMappings(), indent + sp8));
        if (o.getSourceCount() > 0)
            buff.append(getBreakAndIndent())
                .append("Sources: ").append(lineBreak)
                .append(toString(o.getSource(), indent + sp8));
        if (o.getRelationsCount() > 0)
            buff.append(getBreakAndIndent())
                .append("Relations: ").append(lineBreak)
                .append(toString(o.getRelations(), indent + sp8));
    }
    
    protected static void append(StringBuffer buff, String indent, CodingSchemes o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Coding Schemes: ").append(lineBreak)
            .append(toString(o.getCodingScheme(), indent + sp8));
    }
    
    /////////////////////////////////
    // LexGrid Common Types package
    /////////////////////////////////
    protected static void append(StringBuffer buff, String indent, Describable o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Entity Description: ").append(lineBreak)
            .append(toString(o.getEntityDescription(), indent + sp8));
    }
    
    protected static void append(StringBuffer buff, String indent, EntityDescription o) {
        appendAndWrap(buff, "Description: ", indent, o.getContent());
    }
    
    protected static void append(StringBuffer buff, String indent, Properties o) {
        buff.append(indent)
            .append(getBreakAndIndent())
            .append(getBoldedString("Properties: ")).append(lineBreak)
            .append(toString(o.getProperty(), indent + sp8));
    }
    
    protected static void append(StringBuffer buff, String indent, Property o) {
        buff.append(indent)
            .append(getBreakAndIndent())
            .append(getBoldedString("Name: ")).append(o.getPropertyName())
            .append(getBreakAndIndent())
            .append(getBoldedString("Identifier: ") + o.getPropertyId())
            .append(getBreakAndIndent())
            .append(getBoldedString("Value: ")).append(lineBreak)
            .append(toString(o.getValue(), indent + sp8))
            .append(getBreakAndIndent())
            .append(getBoldedString("Language: ")).append(o.getLanguage());
        if (o.getSource().length > 0)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Sources: ")).append(lineBreak)
                .append(toString(o.getSource(), indent + sp8));
        if (o.getUsageContext().length > 0)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Usage Contexts")).append(lineBreak)
                .append(toString(o.getUsageContext(), indent + sp8));
        if (o.getPropertyQualifier().length > 0)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Property Qualifiers")).append(lineBreak)
                .append(toString(o.getPropertyQualifier(), indent + sp8));
    }
    
    protected static void append(StringBuffer buff, String indent, PropertyQualifier o) {
        buff.append(indent)
            .append(getBreakAndIndent())
            .append(getBoldedString("Property Qualifier Name: ")).append(o.getPropertyQualifierName())
            .append(getBreakAndIndent())
            .append(getBoldedString("Value: ")).append(lineBreak)
            .append(toString(o.getValue(), indent + sp8));
    }
    
    protected static void append(StringBuffer buff, String indent, Source o) {
        buff.append(indent)
            .append(getBreakAndIndent())
            .append(getBoldedString("Content: ")).append(o.getContent());
        if (o.getRole() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Role: ")).append(o.getRole());
        if (o.getSubRef() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Sub Ref: ")).append(o.getSubRef());
    }
    
    protected static void append(StringBuffer buff, String indent, Text o) {
        appendAndWrap(buff, getType(o) + ": ", indent, o.getContent());
    }
    
    protected static void append(StringBuffer buff, String indent, Versionable o) {
        buff.append(indent)
            .append(getBreakAndIndent())
            .append(getBoldedString("Is Active: ")).append(o.getIsActive())
            .append(getBreakAndIndent())
            .append(getBoldedString("Status: ")).append(o.getStatus());
        if (o.getEffectiveDate() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Effective Date: "))
                .append(mmddyyyy.format(o.getEffectiveDate()));
        if (o.getExpirationDate() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Expiration Date: "))
                .append(mmddyyyy.format(o.getExpirationDate()));
        if (o.getEntryState() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Entry State: ")).append(lineBreak)
                .append(toString(o.getEntryState(), indent + sp8));
        if (o.getOwner() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Owner: ")).append(lineBreak)
                .append(toString(o.getOwner(), indent + sp8));
    }
    
    protected static void append(StringBuffer buff, String indent, VersionableAndDescribable o) {
        append(buff, indent, (Versionable) o);
        if (o.getEntityDescription() != null)
            buff.append(lineBreak)
            .append(getBoldedString("Entity Description: "))
                .append(toString(o.getEntityDescription(), indent + sp4));
    }
    
    /////////////////////////////
    // LexGrid Concepts package
    /////////////////////////////
    protected static void append(StringBuffer buff, String indent, Definition o) {
        append(buff, indent, (Property) o);
        buff.append(getBreakAndIndent())
            .append("Is Preferred: ").append(o.getIsPreferred());
    }

    protected static void append(StringBuffer buff, String indent, Entity o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Code: ").append(o.getEntityCode());
        if (StringUtils.isNotBlank(o.getEntityCodeNamespace()))
            buff.append(getBreakAndIndent())
                .append("Namespace: ").append(o.getEntityCodeNamespace());
        if (o.getIsActive() != null)
            buff.append(getBreakAndIndent())
                .append("Is Active: ").append(o.getIsActive());
        if (o.getIsAnonymous() != null)
            buff.append(getBreakAndIndent())
                .append("Is Anonymous: ").append(o.getIsAnonymous());
        if (o.getIsDefined() != null)
            buff.append(getBreakAndIndent())
                .append("Is Defined: ").append(o.getIsDefined());
        if (StringUtils.isNotBlank(o.getStatus()))
            buff.append(getBreakAndIndent())
                .append("Status: ").append(o.getStatus());
        if (o.getEntityDescription() != null)
            buff.append(lineBreak)
                .append(toString(o.getEntityDescription(), indent + sp4));
        if (o.getEffectiveDate() != null)
            buff.append(getBreakAndIndent())
                .append("Effective Date: ")
                .append(mmddyyyy.format(o.getEffectiveDate()));
        if (o.getExpirationDate() != null)
            buff.append(getBreakAndIndent())
                .append("Expiration Date: ")
                .append(mmddyyyy.format(o.getExpirationDate()));
        if (o.getPresentation().length > 0)
            buff.append(getBreakAndIndent())
                .append("Presentations: ").append(lineBreak)
                .append(toString(o.getPresentation(), indent + sp8));
        if (o.getDefinition().length > 0)
            buff.append(getBreakAndIndent())
                .append("Definitions: ").append(lineBreak)
                .append(toString(o.getDefinition(), indent + sp8));
        if (o.getComment().length > 0)
            buff.append(getBreakAndIndent())
                .append("Comments: ").append(lineBreak)
                .append(toString(o.getComment(), indent + sp8));
        if (o.getProperty().length > 0)
            buff.append(getBreakAndIndent())
                .append("Other Properties: ").append(lineBreak)
                .append(toString(o.getProperty(), indent + sp8));
        if (o.getPropertyLink().length > 0)
            buff.append(getBreakAndIndent())
                .append("Property Links: ").append(lineBreak)
                .append(toString(o.getPropertyLink(), indent + sp8));
        if (o.getEntryState() != null)
            buff.append(getBreakAndIndent())
                .append("Entry State: ").append(lineBreak)
                .append(toString(o.getEntryState(), indent + sp8));
        if (o.getOwner() != null)
            buff.append(getBreakAndIndent())
                .append("Owner: ").append(lineBreak)
                .append(toString(o.getOwner(), indent + sp8));
    }
    
    protected static void append(StringBuffer buff, String indent, Entities o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Entities: ").append(lineBreak)
            .append(toString(o.getEntity(), indent + sp8));
   }

    protected static void append(StringBuffer buff, String indent, Presentation o) {
        append(buff, indent, (Property) o);
        buff.append(getBreakAndIndent())
            .append("Is Preferred: ").append(o.getIsPreferred())
            .append(getBreakAndIndent())
            .append("Representational Form: ").append(o.getRepresentationalForm())
            .append(getBreakAndIndent())
            .append("Match if No Context: ").append(o.getMatchIfNoContext())
            .append(getBreakAndIndent())
            .append("Degree of Fidelity: ").append(o.getDegreeOfFidelity());
    }
    
    protected static void append(StringBuffer buff, String indent, PropertyLink o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Link: ").append(o.getPropertyLink())
            .append(getBreakAndIndent())
            .append("Source Property: ").append(o.getSourceProperty())
            .append(getBreakAndIndent())
            .append("Target Property: ").append(o.getTargetProperty());
    }
    
    ///////////////////////////
    // LexGrid Naming package
    ///////////////////////////
    protected static void append(StringBuffer buff, String indent, Mappings o) {
        if (o.getSupportedAssociationCount() > 0)
            buff.append(indent).append(getType(o))
                .append("SupportedAssociations: ").append(lineBreak)
                .append(toString(o.getSupportedAssociation(), indent + sp8));
        if (o.getSupportedAssociationQualifierCount() > 0)
            buff.append(getBreakAndIndent())
                .append("SupportedAssociationQualifiers: ").append(lineBreak)
                .append(toString(o.getSupportedAssociationQualifier(), indent + sp8));
        if (o.getSupportedCodingSchemeCount() > 0)
            buff.append(getBreakAndIndent())
                .append("SupportedCodingSchemes: ").append(lineBreak)
                .append(toString(o.getSupportedCodingScheme(), indent + sp8));
        if (o.getSupportedContainerNameCount() > 0)
            buff.append(getBreakAndIndent())
                .append("SupportedContainer: ").append(lineBreak)
                .append(toString(o.getSupportedContainerName(), indent + sp8));
        if (o.getSupportedContextCount() > 0)
            buff.append(getBreakAndIndent())
                .append("SupportedContext: ").append(lineBreak)
                .append(toString(o.getSupportedContext(), indent + sp8));
        if (o.getSupportedDataTypeCount() > 0)
            buff.append(getBreakAndIndent()) 
                .append("SupportedDataType: ").append(lineBreak)
                .append(toString(o.getSupportedDataType(), indent + sp8));
        if (o.getSupportedDegreeOfFidelityCount() > 0)
            buff.append(getBreakAndIndent())
                .append("SupportedDegreeOfFidelity: ").append(lineBreak)
                .append(toString(o.getSupportedDegreeOfFidelity(), indent + sp8));
        if (o.getSupportedEntityTypeCount() > 0)
            buff.append(getBreakAndIndent())
                .append("SupportedEntityType: ").append(lineBreak)
                .append(toString(o.getSupportedEntityType(), indent + sp8));
        if (o.getSupportedHierarchyCount() > 0)
            buff.append(getBreakAndIndent())
                .append("SupportedHierarchies: ").append(lineBreak)
                .append(toString(o.getSupportedHierarchy(), indent + sp8));
        if (o.getSupportedLanguageCount() > 0)
            buff.append(getBreakAndIndent()) 
                .append("SupportedLanguages: ").append(lineBreak)
                .append(toString(o.getSupportedLanguage(), indent + sp8));
        if (o.getSupportedNamespaceCount() > 0)
            buff.append(getBreakAndIndent()) 
                .append("SupportedNameSpace: ").append(lineBreak)
                .append(toString(o.getSupportedNamespace(), indent + sp8));
        if (o.getSupportedPropertyCount() > 0)
            buff.append(getBreakAndIndent()) 
                .append("SupportedProperties: ").append(lineBreak)
                .append(toString(o.getSupportedProperty(), indent + sp8));
        if (o.getSupportedPropertyLinkCount() > 0)
            buff.append(getBreakAndIndent()) 
                .append("SupportedPropertyLinks: ").append(lineBreak)
                .append(toString(o.getSupportedPropertyLink(), indent + sp8));
        if (o.getSupportedPropertyQualifierCount() > 0)
            buff.append(getBreakAndIndent())
                .append("SupportedPropertyQualifier: ").append(lineBreak)
                .append(toString(o.getSupportedPropertyQualifier(), indent + sp8));
        if (o.getSupportedPropertyQualifierTypeCount() > 0)
            buff.append(getBreakAndIndent())
                .append("SupportedPropertyQualifierType: ").append(lineBreak)
                .append(toString(o.getSupportedPropertyQualifierType(), indent + sp8));
        if (o.getSupportedPropertyTypeCount() > 0)
            buff.append(getBreakAndIndent())
                .append("SupportedPropertyType: ").append(lineBreak)
                .append(toString(o.getSupportedPropertyType(), indent + sp8));
        if (o.getSupportedRepresentationalFormCount() > 0)
            buff.append(getBreakAndIndent())
                .append("SupportedRepresentationalForms: ").append(lineBreak)
                .append(toString(o.getSupportedRepresentationalForm(), indent + sp8));
        if (o.getSupportedSortOrderCount() > 0)
            buff.append(getBreakAndIndent())
                .append("SupportedSortOrder: ").append(lineBreak)
                .append(toString(o.getSupportedSortOrder(), indent + sp8));
        if (o.getSupportedSourceCount() > 0)
            buff.append(getBreakAndIndent())
                .append("SupportedSources: ").append(lineBreak)
                .append(toString(o.getSupportedSource(), indent + sp8));
        if (o.getSupportedSourceRoleCount() > 0)
            buff.append(getBreakAndIndent())
                .append("SupportedSourceRole: ").append(lineBreak)
                .append(toString(o.getSupportedSourceRole(), indent + sp8));
        if (o.getSupportedStatusCount() > 0)
            buff.append(getBreakAndIndent())
                .append("SupportedStatus: ").append(lineBreak)
                .append(toString(o.getSupportedStatus(), indent + sp8));
    }
    
    protected static void append(StringBuffer buff, String indent, SupportedCodingScheme o) {
        append(buff, indent, (URIMap) o);
        buff.append(getBreakAndIndent())
            .append("Is Imported: ").append(o.getIsImported());       
    }
    
    protected static void append(StringBuffer buff, String indent, SupportedHierarchy o) {
        append(buff, indent, (URIMap) o);
        buff.append(getBreakAndIndent())
            .append("Root Code: ").append(o.getRootCode())
            .append(getBreakAndIndent())
            .append("Forward Navigable: ").append(o.getIsForwardNavigable())
            .append(getBreakAndIndent())
            .append("Associations: ").append(lineBreak)
            .append(toString(o.getAssociationNames(), indent + sp8));       
    }
    
    protected static void append(StringBuffer buff, String indent, SupportedNamespace o) {
        append(buff, indent, (URIMap) o);
        buff.append(getBreakAndIndent())
            .append("Equivalent Coding Scheme: ").append(o.getEquivalentCodingScheme());       
    }
    
    protected static void append(StringBuffer buff, String indent, SupportedSource o) {
        append(buff, indent, (URIMap) o);
        buff.append(getBreakAndIndent())
            .append("Assembly Rule: ").append(o.getAssemblyRule());       
    }
    
    protected static void append(StringBuffer buff, String indent, URIMap o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Local ID: ").append(o.getLocalId());
        if (StringUtils.isNotBlank(o.getUri()))
            buff.append(getBreakAndIndent())
                .append("URI: ").append(o.getUri());
        if (StringUtils.isNotBlank(o.getContent()))
            buff.append(getBreakAndIndent())
                .append("Content: ").append(o.getContent());
    }
    
    //////////////////////////////
    // LexGrid Relations package
    //////////////////////////////
    protected static void append(StringBuffer buff, String indent, org.LexGrid.relations.AssociationEntity o) {
        append(buff, indent, (Entity) o);
        if (StringUtils.isNotBlank(o.getEntityCode()))
            buff.append(getBreakAndIndent())
                .append("Association Entity Code: ").append(o.getEntityCode());
        if (StringUtils.isNotBlank(o.getForwardName()))
            buff.append(getBreakAndIndent())
                .append("Forward Name: ").append(o.getForwardName());
        if (StringUtils.isNotBlank(o.getReverseName()))
            buff.append(getBreakAndIndent())
                .append("Reverse Name: ").append(o.getReverseName());
        buff.append(getBreakAndIndent())
            .append("isNavigable: ").append(toString(o.getIsNavigable()))
            .append(getBreakAndIndent())
            .append("isTransitive: ").append(toString(o.getIsTransitive()));
    }
    
    protected static void append(StringBuffer buff, String indent, AssociatableElement o) {
        append(buff, indent, (Versionable) o);
        buff.append(getBreakAndIndent())
            .append("Association Instance ID: ").append(o.getAssociationInstanceId())
            .append(getBreakAndIndent())
            .append("Is Defining: ").append(o.getIsDefining())
            .append(getBreakAndIndent())
            .append("Is Inferred: ").append(o.getIsInferred());
        if (o.getUsageContextCount() > 0)
            buff.append(getBreakAndIndent())
                .append("Usage Context: ").append(lineBreak)
                .append(toString(o.getUsageContext(), indent + sp8));
        if (o.getAssociationQualificationCount() > 0)
            buff.append(getBreakAndIndent())
                .append("Qualification: ").append(lineBreak)
                .append(toString(o.getAssociationQualification(), indent + sp8));
    }
    
    protected static void append(StringBuffer buff, String indent, AssociationData o) {
        append(buff, indent, (AssociatableElement) o);
        buff.append(getBreakAndIndent())
            .append("Data: ").append(lineBreak)
            .append(toString(o.getAssociationDataText(), indent + sp8));
    }
    
    protected static void append(StringBuffer buff, String indent, AssociationQualification o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Qualifier: ").append(o.getAssociationQualifier())
            .append(getBreakAndIndent())
            .append("Qualifier Text: ").append(o.getQualifierText());
    }
    
    protected static void append(StringBuffer buff, String indent, AssociationSource o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Source Code: ").append(o.getSourceEntityCode())
            .append(getBreakAndIndent())
            .append("Source Code Namespace: ").append(o.getSourceEntityCodeNamespace());
    }
    
    protected static void append(StringBuffer buff, String indent, AssociationTarget o) {
        append(buff, indent, (AssociatableElement) o);
        buff.append(getBreakAndIndent())
            .append("Target Code: ").append(o.getTargetEntityCode())
            .append(getBreakAndIndent())
            .append("Target Code Namespace: ").append(o.getTargetEntityCodeNamespace());
    }
    
    protected static void append(StringBuffer buff, String indent, Relations o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Container Name: ").append(o.getContainerName())
            .append(getBreakAndIndent())
            .append("Is Mapping: ").append(o.getIsMapping());
        if (o.getEntityDescription() != null)
            buff.append(lineBreak)
                .append(toString(o.getEntityDescription(), indent + sp4));
        if (o.getSourceCodingScheme() != null)
            buff.append(lineBreak)
                .append("Source Coding Scheme: ").append(toString(o.getSourceCodingScheme(), indent + sp4));
        if (o.getSourceCodingSchemeVersion() != null)
            buff.append(lineBreak)
                .append("Source Coding Scheme Version: ").append(toString(o.getSourceCodingSchemeVersion(), indent + sp4));
        if (o.getTargetCodingScheme() != null)
            buff.append(lineBreak)
                .append("Target Coding Scheme: ").append(toString(o.getTargetCodingScheme(), indent + sp4));
        if (o.getTargetCodingSchemeVersion() != null)
            buff.append(lineBreak)
                .append("Target Coding Scheme Version: ").append(toString(o.getTargetCodingSchemeVersion(), indent + sp4));
    }
    
    /////////////////////////////
    // LexGrid Versions package
    /////////////////////////////
    protected static void append(StringBuffer buff, String indent, ChangedEntry o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Choice Value: ").append(o.getChoiceValue())
            .append(getBreakAndIndent())
            .append("Changed PickList Entries: ").append(lineBreak)
            .append(toString(o.getChangedPickListDefinitionEntry(), indent + sp8))
            .append(getBreakAndIndent())
            .append("Changed Coding Scheme: ").append(lineBreak)
            .append(toString(o.getChangedCodingSchemeEntry(), indent + sp8));
    }
    
    protected static void append(StringBuffer buff, String indent, EditHistory o) {
        buff.append(indent).append(getType(o));
        if (o.getRevisionCount() > 0)
            buff.append(getBreakAndIndent())
                .append("Revisions: ").append(toString(o.getRevision(), indent + sp8));
    }
    
    protected static void append(StringBuffer buff, String indent, EntityVersion o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Release: ").append(o.getReleaseURN())
            .append(getBreakAndIndent())
            .append("Version: ").append(o.getVersion())
            .append(getBreakAndIndent())
            .append("Is Complete: ").append(o.getIsComplete());
        if (o.getVersionOrder() != null)
            buff.append(getBreakAndIndent())
                .append("Version Order: ").append(o.getVersionOrder());
        if (o.getVersionDate() != null)
            buff.append(getBreakAndIndent())
                .append("Version Date: ").append(mmddyyyy.format(o.getVersionDate()));
        if (o.getEffectiveDate() != null)
            buff.append(getBreakAndIndent())
                .append("Effective Date: ").append(mmddyyyy.format(o.getEffectiveDate()));
        if (o.getChangeDocumentation() != null)
            buff.append(getBreakAndIndent())
                .append("Change Documentation").append(lineBreak)
                .append(toString(o.getChangeDocumentation(), indent + sp8));
        if (o.getChangeInstructions() != null)
            buff.append(getBreakAndIndent())
                .append("Change Instructions").append(lineBreak)
                .append(toString(o.getChangeInstructions(), indent + sp8));
    }
    
    protected static void append(StringBuffer buff, String indent, EntryState o) {
        buff.append(indent)
            .append(getBreakAndIndent())
            .append(getBoldedString("Change Type: ")).append(o.getChangeType())
            .append(getBreakAndIndent())
            .append(getBoldedString("Relative Order: ")).append(o.getRelativeOrder())
            .append(getBreakAndIndent())
            .append(getBoldedString("Containing Revision: ")).append(o.getContainingRevision())
            .append(getBreakAndIndent())
            .append(getBoldedString("Previous Revision: ")).append(o.getPrevRevision());
    }
    
    protected static void append(StringBuffer buff, String indent, Revision o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append(getBoldedString("Revision ID: ")).append(o.getRevisionId())
            .append(getBreakAndIndent())
            .append(getBoldedString("Edit Order: ")).append(o.getEditOrder());
        if (o.getRevisionDate() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Revision Date: ")).append(o.getRevisionDate());
        if (o.getEntityDescription() != null)
            buff.append(lineBreak)
                .append(toString(o.getEntityDescription(), indent + sp4));            
        if (o.getChangeInstructions() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Change Instructions: ")).append(lineBreak)
                .append(toString(o.getChangeInstructions(), indent + sp8));
        if (o.getChangeAgent() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Change Agent: ")).append(lineBreak)
                .append(toString(o.getChangeAgent(), indent + sp8));
        if (o.getChangedEntryCount() > 0)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Change Entries: ")).append(lineBreak)
                .append(toString(o.getChangedEntry(), indent + sp8));
        
    }
    
    protected static void append(StringBuffer buff, String indent, SystemRelease o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append(getBoldedString("Release ID: ")).append(o.getReleaseId());
        if (o.getReleaseDate() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Release Date: ") + mmddyyyy.format(o.getReleaseDate()));
        buff
            .append(getBreakAndIndent())
            .append(getBoldedString("Release URI: ")).append(o.getReleaseURI())
            .append(getBreakAndIndent())
            .append(getBoldedString("Based on Release: ")).append(o.getBasedOnRelease())
            .append(getBreakAndIndent())
            .append(getBoldedString("Release Agency: ")).append(o.getReleaseAgency());
        if (o.getEntityDescription() != null)
            buff.append(lineBreak)
                .append(toString(o.getEntityDescription(), indent + sp4));            
    }
    
    
    /////////////////////////////////
    // LexGrid ValueSets package
    /////////////////////////////////
    protected static void append(StringBuffer buff, String indent, CodingSchemeReference o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append("Coding Scheme: ").append(o.getCodingScheme());
    }

    protected static void append(StringBuffer buff, String indent, DefinitionEntry o) {
        buff.append(indent)
            .append(getBreakAndIndent())
            .append(getBoldedString("Operator: ")).append(o.getOperator())
            .append(getBreakAndIndent())
            .append(getBoldedString("Rule Order: ")).append(o.getRuleOrder());
        if (o.getEntityReference() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Entity Reference: ")).append(lineBreak)
                .append(toString(o.getEntityReference(), indent + sp8));
        if (o.getValueSetDefinitionReference() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Value Set Definition Reference: ")).append(lineBreak)
                .append(o.getValueSetDefinitionReference());
        if (o.getCodingSchemeReference() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Coding Scheme Reference: ")).append(lineBreak)
                .append(o.getCodingSchemeReference());
    }

    protected static void append(StringBuffer buff, String indent, EntityReference o) {
        buff.append(indent)
            .append(getBreakAndIndent())
            .append(getBoldedString("Entity Code: ")).append(o.getEntityCode());
        if (StringUtils.isNotBlank(o.getEntityCodeNamespace()))
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Entity Code Namespace: ")).append(o.getEntityCodeNamespace());
        if (StringUtils.isNotBlank(o.getReferenceAssociation()))
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Reference Association: ")).append(o.getReferenceAssociation());
        buff
            .append(getBreakAndIndent())
            .append(getBoldedString("Leaf Only: ")).append(o.isLeafOnly())
            .append(getBreakAndIndent())
            .append(getBoldedString("Target to Source: ")).append(o.isTargetToSource())
            .append(getBreakAndIndent())
            .append(getBoldedString("Transitive Closure: ")).append(o.isTransitiveClosure());
    }

    protected static void append(StringBuffer buff, String indent, PickListDefinition o) {
        buff.append(getBreakAndIndent())
            .append(getBoldedString("Pick List ID: ")).append(o.getPickListId())
            .append(getBreakAndIndent())
            .append(getBoldedString("Represents Value Set Definition: ")).append(o.getRepresentsValueSetDefinition())
            .append(getBreakAndIndent())
            .append(getBoldedString("Represents Complete Value Set: ")).append(o.isCompleteSet());
        if (StringUtils.isNotBlank(o.getDefaultEntityCodeNamespace()))
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Default Code Namespace: ")).append(o.getDefaultEntityCodeNamespace());
        if (StringUtils.isNotBlank(o.getDefaultLanguage()))
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Default Language: ")).append(o.getDefaultLanguage());
        if (o.getDefaultPickContext() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Default Pick Contexts: ")).append(lineBreak)
                .append(toString(o.getDefaultEntityCodeNamespace(), indent + sp8));
        if (StringUtils.isNotBlank(o.getDefaultSortOrder()))
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Default Sort Order: ")).append(o.getDefaultSortOrder());
        if (o.getProperties() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Properties: ")).append(lineBreak)
                .append(toString(o.getProperties(), indent + sp8));
        if (o.getSource() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Sources: ")).append(lineBreak)
                .append(toString(o.getSource(), indent + sp8));
        if (o.getPickListEntryNode() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Pick List Entry Nodes : ")).append(lineBreak)
                .append(toString(o.getPickListEntryNode(), indent + sp8));
        append(buff, indent, (VersionableAndDescribable) o);        
    }
    
    private static String getBoldedString(String str){
        return "<b>" + str + "</b>";
    }
    
    protected static void append(StringBuffer buff, String indent, PickListEntry o) {
        buff.append(indent)
            .append(getBreakAndIndent())
            .append(getBoldedString("Pick Text: ")).append(o.getPickText())
            .append(getBreakAndIndent())
            .append(getBoldedString("Entry Order: ")).append(o.getEntryOrder())
            .append(getBreakAndIndent())
            .append(getBoldedString("Is Default: ")).append(o.isIsDefault())
            .append(getBreakAndIndent())
            .append(getBoldedString("Match No Context: ")).append(o.isMatchIfNoContext());
        if (StringUtils.isNotBlank(o.getEntityCode()))
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Code: ")).append(o.getEntityCode());
        if (StringUtils.isNotBlank(o.getEntityCodeNamespace()))
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Code Namespace: ")).append(o.getEntityCodeNamespace());
        if (StringUtils.isNotBlank(o.getPropertyId()))
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Property ID: ")).append(o.getPropertyId());
        if (StringUtils.isNotBlank(o.getLanguage()))
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Language: ")).append(o.getLanguage());
        if (o.getPickContext().length > 0)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Pick Contexts: ")).append(lineBreak)
                .append(toString(o.getPickContext(), indent + sp8));
    }
    
    protected static void append(StringBuffer buff, String indent, PickListEntryExclusion o) {
        buff.append(indent)
            .append(getBreakAndIndent())
            .append(getBoldedString("Entity Code: ")).append(o.getEntityCode());
        if (StringUtils.isNotBlank(o.getEntityCodeNamespace()))
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Entity Code Namespace: ")).append(o.getEntityCodeNamespace());
    }
    
    protected static void append(StringBuffer buff, String indent, PickListEntryNode o) {
        append(buff, indent, (Versionable) o);
        buff.append(getBreakAndIndent())
            .append(getBoldedString("Pick List Entry ID: ")).append(o.getPickListEntryId());
        if (o.getProperties() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Properties: ")).append(lineBreak)
                .append(toString(o.getProperties(), indent + sp8));
    }
    
    protected static void append(StringBuffer buff, String indent, PickListEntryNodeChoice o) {
        buff.append(indent);
        if (o.getInclusionEntry() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Inclusion: ")).append(lineBreak)
                .append(toString(o.getInclusionEntry(), indent + sp8));
        if (o.getExclusionEntry() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Exclusion: ")).append(lineBreak)
                .append(toString(o.getExclusionEntry(), indent + sp8));
    }
    
    protected static void append(StringBuffer buff, String indent, PickListDefinitions o) {
        if (o.getPickListDefinition().length > 0)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Definitions: ")).append(lineBreak)
                .append(toString(o.getPickListDefinition(), indent + sp8));
        if (o.getMappings() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Mappings: ")).append(lineBreak)
                .append(toString(o.getMappings(), indent + sp8));
    }
    
    //ValueSetDefinition
    protected static void append(StringBuffer buff, String indent, ValueSetDefinition o) {
        buff.append(getBreakAndIndent())
            .append(getBoldedString("Value Set Definition Name: ")).append(o.getValueSetDefinitionName());
        if (StringUtils.isNotBlank(o.getValueSetDefinitionURI()))
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Value Set Definition URI: ")).append(o.getValueSetDefinitionURI());
        if (StringUtils.isNotBlank(o.getDefaultCodingScheme()))
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Default Coding Scheme: ")).append(o.getDefaultCodingScheme());
        if (o.getDefinitionEntry().length > 0)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Definition Entries: ")).append(lineBreak)
                .append(toString(o.getDefinitionEntry(), indent + sp8));
        if (o.getRepresentsRealmOrContext().length > 0)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Represents Realm or Context: ")).append(lineBreak)
                .append(toString(o.getRepresentsRealmOrContext(), indent + sp8));
        if (o.getSource().length > 0)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Sources: ")).append(lineBreak)
                .append(toString(o.getSource(), indent + sp8));
        if (o.getProperties() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Properties: ")).append(lineBreak)
                .append(toString(o.getProperties(), indent + sp8));
        append(buff, indent, (VersionableAndDescribable) o);        
    }
    
    protected static void append(StringBuffer buff, String indent, ValueSetDefinitionReference o) {
        buff.append(indent).append(getType(o))
            .append(getBreakAndIndent())
            .append(getBoldedString("URI: ")).append(o.getValueSetDefinitionURI());
    }
    
    protected static void append(StringBuffer buff, String indent, ValueSetDefinitions o) {
        buff.append(indent).append(getType(o));
        if (o.getValueSetDefinition().length > 0)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Definitions: ")).append(lineBreak)
                .append(toString(o.getValueSetDefinition(), indent + sp8));
        if (o.getMappings() != null)
            buff.append(getBreakAndIndent())
                .append(getBoldedString("Mappings: ")).append(lineBreak)
                .append(toString(o.getMappings(), indent + sp8));
    }
    
    protected static void append(StringBuffer buff, String indent, String o) {
        buff.append(indent).append(o);        
    }  
    
    protected static void append(StringBuffer buff, String indent, Boolean o) {
        buff.append(indent).append(o);        
    }      
    
    protected static void append(StringBuffer buff, String indent, boolean o) {
        buff.append(indent).append(o);        
    }  
    
    public static void main(String args[]) {
       
       System.out.println(VSDObjectToString.toString("Test", "  "));
       System.out.println(VSDObjectToString.toString(Boolean.FALSE, "  "));
       System.out.println(VSDObjectToString.toString(Boolean.FALSE.booleanValue(), "  "));
       
   
    }
}