package edu.mayo.informatics.lexgrid.convert.directConversions.medDRA.Data;import java.util.List;import org.LexGrid.commonTypes.Property;import org.LexGrid.concepts.Comment;import org.LexGrid.concepts.Definition;import org.LexGrid.concepts.Presentation;public interface DatabaseEntityRecord extends DatabaseRecord {    public String getCode();    public String getName();    public List<Presentation> getPresentations();    public List<Definition> getDefinitions();    public List<Comment> getComments();    public List<Property> getProperties();}