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
package org.lexgrid.valuesets.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.LexGrid.emf.codingSchemes.CodingScheme;
import org.LexGrid.emf.commonTypes.CommontypesFactory;
import org.LexGrid.emf.commonTypes.Property;
import org.LexGrid.emf.commonTypes.PropertyQualifier;
import org.LexGrid.emf.commonTypes.Source;
import org.LexGrid.emf.commonTypes.Text;
import org.LexGrid.emf.commonTypes.impl.PropertyImpl;
import org.LexGrid.emf.concepts.Comment;
import org.LexGrid.emf.concepts.ConceptsFactory;
import org.LexGrid.emf.concepts.Definition;
import org.LexGrid.emf.concepts.Presentation;
import org.LexGrid.emf.concepts.impl.DefinitionImpl;
import org.LexGrid.emf.concepts.impl.PresentationImpl;
import org.LexGrid.emf.valueDomains.PickListDefinition;
import org.LexGrid.emf.valueDomains.PickListEntryNode;
import org.LexGrid.emf.valueDomains.impl.PickListDefinitionImpl;
import org.LexGrid.managedobj.FindException;
import org.LexGrid.managedobj.InsertException;
import org.LexGrid.managedobj.ManagedObjIF;
import org.LexGrid.managedobj.ObjectAlreadyExistsException;
import org.LexGrid.managedobj.ObjectNotFoundException;
import org.LexGrid.managedobj.ServiceInitException;
import org.LexGrid.managedobj.UpdateException;
import org.LexGrid.managedobj.jdbc.JDBCBaseService;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.ecore.EObject;

/**
 * <pre>
 * Title:        PropertyService.java
 * Description:  Class that handles properties to and from the database.
 * </pre>
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class VSDPropertyServices extends VSDBaseService
{
	private VSDEntryTypeServices entryTypeServices_;
	private VSDEntryStateService entryStateServices_;
	
    public VSDPropertyServices(JDBCBaseService anchorService) throws ServiceInitException
    {
        super(anchorService);
    }
    
    public void findByPrimaryKey(ManagedObjIF obj, int entryId) throws FindException
    {
        PreparedStatement selectFromProperty = null;
        
        try
        {
            int propertyCount = 0;
            selectFromProperty = checkOutPreparedStatement(modifySql(
            		" SELECT P1.* " +
            		" FROM " + VSDConstants.TBL_PROPERTY + " {AS} P1 " +
            		" , " + VSDConstants.TBL_ENTRY_TYPE + " {AS} P2 " +
            		" WHERE P1." + SQLTableConstants.TBLCOL_FOREIGNENTRYID + " = ? " +
            		" AND P1." + SQLTableConstants.TBLCOL_ENTRYID + " = P2." + SQLTableConstants.TBLCOL_REFERENCEENTRYID +
            		" AND P2." + SQLTableConstants.TBLCOL_ENTRYTYPE + " = ?"));
            
            selectFromProperty.setInt(1, entryId);
            selectFromProperty.setString(2, SQLTableConstants.ENTRY_STATE_TYPE_PROPERTY);
            
            ResultSet results = selectFromProperty.executeQuery();

            while (results.next())
            {
                Property currProperty = null;
                int propertyEntryId = results.getInt(SQLTableConstants.TBLCOL_ENTRYID);
                String propertyName = results.getString(SQLTableConstants.TBLCOL_PROPERTYNAME);
                Boolean isActive = getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISACTIVE);
                String propertyType = results.getString(SQLTableConstants.TBLCOL_PROPERTYTYPE);
                String language = results.getString(SQLTableConstants.TBLCOL_LANGUAGE);
                Boolean isPreferred = getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_ISPREFERRED);
                Boolean matchIfNoContext = getBooleanFromResultSet(results, SQLTableConstants.TBLCOL_MATCHIFNOCONTEXT);
                String propertyId = results.getString(SQLTableConstants.TBLCOL_PROPERTYID);
                String format = results.getString(SQLTableConstants.TBLCOL_FORMAT);
                String degreeOfFidelity = results.getString(SQLTableConstants.TBLCOL_DEGREEOFFIDELITY);
                String representationalForm = results.getString(SQLTableConstants.TBLCOL_REPRESENTATIONALFORM);
                int entryStateId = results.getInt(SQLTableConstants.TBLCOL_ENTRYSTATEID);
                String propertyValue = results.getString(SQLTableConstants.TBLCOL_PROPERTYVALUE);
                String val1 = results.getString(SQLTableConstants.TBLCOL_VAL1);
                String val2 = results.getString(SQLTableConstants.TBLCOL_VAL2);
                
                if (StringUtils.isNotEmpty(propertyType) && propertyType.equals(SQLTableConstants.TBLCOLVAL_DEFINITION))
                {
                    currProperty = ConceptsFactory.eINSTANCE.createDefinition();
                    if (isPreferred != null)
                    	((DefinitionImpl) currProperty).setIsPreferred(isPreferred);
                    currProperty.setPropertyType(propertyType);
                }
                else if (StringUtils.isNotEmpty(propertyType) && propertyType.equals(SQLTableConstants.TBLCOLVAL_PRESENTATION))
                {
                    currProperty = ConceptsFactory.eINSTANCE.createPresentation();
                    if (isPreferred != null)
                    	((PresentationImpl) currProperty).setIsPreferred(isPreferred);
                    ((PresentationImpl) currProperty).setDegreeOfFidelity(degreeOfFidelity);
                    
                    if (matchIfNoContext != null)
                    	((PresentationImpl) currProperty).setMatchIfNoContext(matchIfNoContext);
                    ((PresentationImpl) currProperty).setRepresentationalForm(representationalForm);
                    
                    currProperty.setPropertyType(propertyType);
                }
                else if (StringUtils.isNotEmpty(propertyType) && propertyType.equals(SQLTableConstants.TBLCOLVAL_COMMENT))
                {
                    currProperty = ConceptsFactory.eINSTANCE.createComment();
                    currProperty.setPropertyType(propertyType);
                }
                else
                {
                    currProperty = CommontypesFactory.eINSTANCE.createProperty();
                    currProperty.setPropertyType(propertyType);
                }

                if (isActive != null)
                	currProperty.setIsActive(isActive);
                currProperty.setLanguage(language);
                currProperty.setPropertyName(propertyName);
                
                // we don't need to pass propertyId if it is internally generated 
                if (!propertyId.startsWith("_@("))
                	currProperty.setPropertyId(propertyId);
                
                Text txt = CommontypesFactory.eINSTANCE.createText();
                txt.setValue((String)propertyValue);
                txt.setDataType(format);
                
                currProperty.setValue(txt);
                
                resolveEntryState(currProperty, entryStateId);
                
                getPropQualsAndAttribs(currProperty, propertyEntryId);
                
                addPropertyToObject(obj, currProperty);
                propertyCount++;
            }
            results.close();
        }
        catch (SQLException e)
        {
            throw new FindException(e);
        }
        finally
        {
            checkInPreparedStatement(selectFromProperty);
        }

    }
    
    private void getPropQualsAndAttribs(Property currProperty, int entryId) throws FindException{
    	PreparedStatement selectFromProperty = null;
        
        try
        {
            selectFromProperty = checkOutPreparedStatement(modifySql(
            		" SELECT P1.* " +
            		" FROM " + VSDConstants.TBL_PROPERTY + " {AS} P1 " +
            		" , " + VSDConstants.TBL_ENTRY_TYPE + " {AS} P2 " +
            		" WHERE P1." + SQLTableConstants.TBLCOL_FOREIGNENTRYID + " = ? " +
            		" AND P1." + SQLTableConstants.TBLCOL_ENTRYID + " = P2." + SQLTableConstants.TBLCOL_REFERENCEENTRYID +
            		" AND P2." + SQLTableConstants.TBLCOL_ENTRYTYPE + " = ?"));
            
            selectFromProperty.setInt(1, entryId);
            selectFromProperty.setString(2, VSDConstants.ENTRY_STATE_TYPE_ATTRIBUTE);
            
            ResultSet results = selectFromProperty.executeQuery();

            while (results.next())
            {
                String propertyName = results.getString(SQLTableConstants.TBLCOL_PROPERTYNAME);
                String propertyType = results.getString(SQLTableConstants.TBLCOL_PROPERTYTYPE);
                String format = results.getString(SQLTableConstants.TBLCOL_FORMAT);
                String propertyValue = results.getString(SQLTableConstants.TBLCOL_PROPERTYVALUE);
                String val1 = results.getString(SQLTableConstants.TBLCOL_VAL1);
                String val2 = results.getString(SQLTableConstants.TBLCOL_VAL2);
                
               if (propertyName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SOURCE)) {
                	Source src = CommontypesFactory.eINSTANCE.createSource();
					src.setValue(propertyValue);
					src.setRole(val2);
					src.setSubRef(val1);
					currProperty.getSource().add(src);
				} else if (propertyName.equalsIgnoreCase(SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT)) {
					currProperty.getUsageContext().add(propertyValue);
				} else {
					 PropertyQualifier pQual = CommontypesFactory.eINSTANCE.createPropertyQualifier();
					 pQual.setPropertyQualifierName(propertyName);
					 pQual.setPropertyQualifierType(propertyType);
					 Text txt = CommontypesFactory.eINSTANCE.createText();
					 txt.setValue(propertyValue);
					 txt.setDataType(format);
					 pQual.setValue(txt);
					 currProperty.getPropertyQualifier().add(pQual);
				}
            }
            results.close();
        }
        catch (SQLException e)
        {
            throw new FindException(e);
        }
        finally
        {
            checkInPreparedStatement(selectFromProperty);
        }
    }

    
    @Override
	public void insert(ManagedObjIF obj) throws InsertException,
			ObjectAlreadyExistsException {
    	
    	Property currProperty = (Property) (obj);
    	Integer foreingEntryId = null;
    	
    	EObject object = currProperty.getContainer(PickListDefinition.class, 0);

		if (object instanceof PickListDefinition ) {
			PickListDefinition plDef = (PickListDefinition)object;
			String pickListId = plDef.getPickListId();

			String query = "SELECT " + SQLTableConstants.TBLCOL_ENTRYID
					+ " FROM " + VSDConstants.TBL_PICK_LIST + " WHERE "
					+ SQLTableConstants.TBLCOL_PICKLISTID + " = ?";

			PreparedStatement prepStmt = null;
			try {
				prepStmt = checkOutPreparedStatement(query);
				prepStmt.setString(1, pickListId);

				ResultSet result = prepStmt.executeQuery();

				if( result.next() ) {
					foreingEntryId = result
						.getInt(SQLTableConstants.TBLCOL_ENTRYID);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				checkInPreparedStatement(prepStmt);
			}
			
			insert(obj, foreingEntryId);
			return;
		} 

		object = currProperty .getContainer(PickListEntryNode.class, 0);

		if (object instanceof PickListEntryNode ) {
			PickListEntryNode plEntryNode = (PickListEntryNode)object;
			String plEntryId = plEntryNode.getPickListEntryId();

			String query = "SELECT " + SQLTableConstants.TBLCOL_ENTRYID
					+ " FROM " + VSDConstants.TBL_PL_ENTRY + " WHERE "
					+ SQLTableConstants.TBLCOL_PLENTRYID + " = ?";

			PreparedStatement prepStmt = null;
			try {
				prepStmt = checkOutPreparedStatement(query);
				prepStmt.setString(1, plEntryId);

				ResultSet result = prepStmt.executeQuery();

				if( result.next() ) {
					foreingEntryId = result
							.getInt(SQLTableConstants.TBLCOL_ENTRYID);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				checkInPreparedStatement(prepStmt);
				prepStmt = null;
			}
			
			insert(obj, foreingEntryId);
			return;
		}
	}

	public void insert(ManagedObjIF obj, int foreignEntryId) throws InsertException, ObjectAlreadyExistsException
    {
        PreparedStatement insertIntoProperty = null;
        try
        {
            Property currProperty = (Property) (obj);
            
            int entryId = getNextEntryId();
            insertEntryType(currProperty, entryId);
            
            String entryStateIdStr = null;
            
            if (currProperty.getEntryState() != null)
            {
            	entryStateIdStr = String.valueOf(getNextEntryId());
            	insertEntryState(currProperty, Integer.valueOf(entryStateIdStr));
            }
           
            
            // internally generate propertyId if it is not supplied
            if(currProperty.getPropertyId() == null) {
            	currProperty.setPropertyId("_@(" + java.util.UUID.randomUUID() + ")");
            }
            
            if(currProperty.getValue() == null || currProperty.getValue().getValue() == null)
            {
            	Text txt = CommontypesFactory.eINSTANCE.createText();
            	txt.setValue(" ");
            	currProperty.setValue(txt);
            }
            
            insertIntoProperty = getKeyedInsertStatement(VSDConstants.TBL_PROPERTY);
            
            if (currProperty instanceof Presentation)
            { 
                Presentation temp = (Presentation) (currProperty);
                int k = 1;
                insertIntoProperty.setInt(k++, entryId);
                insertIntoProperty.setInt(k++, foreignEntryId);
                insertIntoProperty.setString(k++, temp.getPropertyName());
                setBooleanOnPreparedStatment(insertIntoProperty, k++, temp.getIsActive());
                String propertyType = StringUtils.isEmpty(temp.getPropertyType()) ? SQLTableConstants.TBLCOLVAL_PRESENTATION : temp.getPropertyType();
                insertIntoProperty.setString(k++, propertyType);
                insertIntoProperty.setString(k++, temp.getLanguage());
                setBooleanOnPreparedStatment(insertIntoProperty, k++, temp.getIsPreferred());
                setBooleanOnPreparedStatment(insertIntoProperty, k++, temp.getMatchIfNoContext());
                insertIntoProperty.setString(k++, temp.getPropertyId());
                insertIntoProperty.setString(k++, temp.getValue().getDataType());
                
                if (StringUtils.isNotEmpty(entryStateIdStr))
                {
                	insertIntoProperty.setInt(k++, Integer.valueOf(entryStateIdStr));
                }
                else 
                {
                	if(this.getDatabaseName().equals("ACCESS")){
                		insertIntoProperty.setString(k++, null);
                	} else {
                		insertIntoProperty.setObject(k++, null, java.sql.Types.BIGINT);
                	}
                }
                
                insertIntoProperty.setString(k++, temp.getDegreeOfFidelity());
                insertIntoProperty.setString(k++, temp.getRepresentationalForm());
                String valueString = null;
                if(temp.getValue() == null || (temp.getValue().getValue().equalsIgnoreCase(""))) {
                	valueString = " ";
                } else {
                	valueString = temp.getValue().getValue();
                }
                insertIntoProperty.setString(k++, valueString);
                insertIntoProperty.setString(k++, " ");
                insertIntoProperty.setString(k++, null);
            }
            else if (currProperty instanceof Definition)
            {
                Definition temp = (Definition) (currProperty);
                int k = 1;
                insertIntoProperty.setInt(k++, entryId);
                insertIntoProperty.setInt(k++, foreignEntryId);
                insertIntoProperty.setString(k++, temp.getPropertyName());
                setBooleanOnPreparedStatment(insertIntoProperty, k++, temp.getIsActive());
                String propertyType = StringUtils.isEmpty(temp.getPropertyType()) ? SQLTableConstants.TBLCOLVAL_DEFINITION : temp.getPropertyType();
                insertIntoProperty.setString(k++, propertyType);
                insertIntoProperty.setString(k++, temp.getLanguage());
                setBooleanOnPreparedStatment(insertIntoProperty, k++, temp.getIsPreferred());
                setBooleanOnPreparedStatment(insertIntoProperty, k++, null);
                insertIntoProperty.setString(k++, temp.getPropertyId());
                insertIntoProperty.setString(k++, temp.getValue().getDataType());
                
                if (StringUtils.isNotEmpty(entryStateIdStr))
                {
                	insertIntoProperty.setInt(k++, Integer.valueOf(entryStateIdStr));
                }
                else 
                {
                	if(this.getDatabaseName().equals("ACCESS")){
                		insertIntoProperty.setString(k++, null);
                	} else {
                		insertIntoProperty.setObject(k++, null, java.sql.Types.BIGINT);
                	}
                }
                
                insertIntoProperty.setString(k++, null);
                insertIntoProperty.setString(k++, null);
                String valueString = null;
                if(temp.getValue() == null || (temp.getValue().getValue().equalsIgnoreCase(""))) {
                	valueString = " ";
                } else {
                	valueString = temp.getValue().getValue();
                }
                insertIntoProperty.setString(k++, valueString);
                insertIntoProperty.setString(k++, " ");
                insertIntoProperty.setString(k++, null);
            }
            else if (currProperty instanceof Comment)
            {
                Comment temp = (Comment) (currProperty);
                int k = 1;
                insertIntoProperty.setInt(k++, entryId);
                insertIntoProperty.setInt(k++, foreignEntryId);
                insertIntoProperty.setString(k++, temp.getPropertyName());
                setBooleanOnPreparedStatment(insertIntoProperty, k++, temp.getIsActive());
                String propertyType = StringUtils.isEmpty(temp.getPropertyType()) ? SQLTableConstants.TBLCOLVAL_COMMENT : temp.getPropertyType();
                insertIntoProperty.setString(k++, propertyType);
                insertIntoProperty.setString(k++, temp.getLanguage());
                setBooleanOnPreparedStatment(insertIntoProperty, k++, null);
                setBooleanOnPreparedStatment(insertIntoProperty, k++, null);
                insertIntoProperty.setString(k++, temp.getPropertyId());
                insertIntoProperty.setString(k++, temp.getValue().getDataType());
                
                if (StringUtils.isNotEmpty(entryStateIdStr))
                {
                	insertIntoProperty.setInt(k++, Integer.valueOf(entryStateIdStr));
                }
                else 
                {
                	if(this.getDatabaseName().equals("ACCESS")){
                		insertIntoProperty.setString(k++, null);
                	} else {
                		insertIntoProperty.setObject(k++, null, java.sql.Types.BIGINT);
                	}
                }
                
                insertIntoProperty.setString(k++, null);
                insertIntoProperty.setString(k++, null);
                String valueString = null;
                if(temp.getValue() == null || (temp.getValue().getValue().equalsIgnoreCase(""))) {
                	valueString = " ";
                } else {
                	valueString = temp.getValue().getValue();
                }
                insertIntoProperty.setString(k++, valueString);
                insertIntoProperty.setString(k++, " ");
                insertIntoProperty.setString(k++, null);
            }
            else
            {
                int k = 1;
                insertIntoProperty.setInt(k++, entryId);
                insertIntoProperty.setInt(k++, foreignEntryId);
                insertIntoProperty.setString(k++, currProperty.getPropertyName());
                setBooleanOnPreparedStatment(insertIntoProperty, k++, currProperty.getIsActive());
                String propertyType = StringUtils.isEmpty(currProperty.getPropertyType()) ? SQLTableConstants.TBLCOLVAL_PROPERTY : currProperty.getPropertyType();
                insertIntoProperty.setString(k++, propertyType);
                insertIntoProperty.setString(k++, currProperty.getLanguage());
                setBooleanOnPreparedStatment(insertIntoProperty, k++, null);
                setBooleanOnPreparedStatment(insertIntoProperty, k++, null);
                insertIntoProperty.setString(k++, currProperty.getPropertyId());
                insertIntoProperty.setString(k++, currProperty.getValue().getDataType());
                
                if (StringUtils.isNotEmpty(entryStateIdStr))
                {
                	insertIntoProperty.setInt(k++, Integer.valueOf(entryStateIdStr));
                }
                else 
                {
                	if(this.getDatabaseName().equals("ACCESS")){
                		insertIntoProperty.setString(k++, null);
                	} else {
                		insertIntoProperty.setObject(k++, null, java.sql.Types.BIGINT);
                	}
                }
                
                insertIntoProperty.setString(k++, null);
                insertIntoProperty.setString(k++, null);
                String valueString = null;
                if(currProperty.getValue() == null || (currProperty.getValue().getValue().equalsIgnoreCase(""))) {
                	valueString = " ";
                } else {
                	valueString = currProperty.getValue().getValue();
                }
                insertIntoProperty.setString(k++, valueString);
                insertIntoProperty.setString(k++, " ");
                insertIntoProperty.setString(k++, null);
            }
            insertIntoProperty.executeUpdate();
            insertIntoProperty.close();
            
            insertPropertyQualifier(currProperty, entryId);
        }
        catch (SQLException e)
        {
            throw new ObjectAlreadyExistsException("Error inserting property " + ((Property) (obj)).getPropertyId()
                    + " on foreignEntryId ; " + foreignEntryId, e);
        }
        catch (Exception e)
        {
            throw new InsertException("Error inserting property " + ((Property) (obj)).getPropertyId() + " on foreignEntryId : "
                    + foreignEntryId, e);
        }
        finally
        {
            try
            {
                insertIntoProperty.close();
            }
            catch (Exception e)
            {
                // do nothing
                
            }
        }
    }
    
    private void insertPropertyQualifier(Property currProperty, int propEntryId) throws InsertException, ObjectAlreadyExistsException
    {
    	PreparedStatement insertIntoProperty = null;
		try {
			insertIntoProperty = getKeyedInsertStatement(VSDConstants.TBL_PROPERTY);

			
			Iterator<Source> sourceIter = currProperty.getSource().iterator();
			while (sourceIter.hasNext()) {
				Source source = sourceIter.next();
				
				int k = 1;
				
				int entryId = getNextEntryId();
				insertEntryType(VSDConstants.ENTRY_STATE_TYPE_ATTRIBUTE, entryId);

				insertIntoProperty.setInt(k++, entryId);
				insertIntoProperty.setInt(k++, propEntryId);
				insertIntoProperty.setString(k++, SQLTableConstants.TBLCOLVAL_SOURCE);
				setBooleanOnPreparedStatment(insertIntoProperty, k++, true);
				insertIntoProperty.setString(k++, null);
				insertIntoProperty.setString(k++, null);
				setBooleanOnPreparedStatment(insertIntoProperty, k++, null);
				setBooleanOnPreparedStatment(insertIntoProperty, k++, null);
				insertIntoProperty.setString(k++, currProperty.getPropertyId());
				insertIntoProperty.setString(k++, null);
				if(this.getDatabaseName().equals("ACCESS")){
            		insertIntoProperty.setString(k++, null);
            	} else {
            		insertIntoProperty.setObject(k++, null, java.sql.Types.BIGINT);
            	}
				insertIntoProperty.setString(k++, null);
				insertIntoProperty.setString(k++, null);
				String valueString = source.getValue();
				insertIntoProperty.setString(k++, valueString);
				String subRef = setBlankStringsNull(source.getSubRef());
				insertIntoProperty.setString(k++, subRef == null ? " " : subRef);
				insertIntoProperty.setString(k++, setBlankStringsNull(source.getRole()));
				
				insertIntoProperty.executeUpdate();
			}
			
			Iterator<String> contextIter = currProperty.getUsageContext().iterator();
			while (contextIter.hasNext()) {
				String context = contextIter.next();
				
				int k = 1;
				
				int entryId = getNextEntryId();
				insertEntryType(VSDConstants.ENTRY_STATE_TYPE_ATTRIBUTE, entryId);

				insertIntoProperty.setInt(k++, entryId);
				insertIntoProperty.setInt(k++, propEntryId);
				insertIntoProperty.setString(k++, SQLTableConstants.TBLCOLVAL_SUPPTAG_CONTEXT);
				setBooleanOnPreparedStatment(insertIntoProperty, k++, true);
				insertIntoProperty.setString(k++, null);
				insertIntoProperty.setString(k++, null);
				setBooleanOnPreparedStatment(insertIntoProperty, k++, null);
				setBooleanOnPreparedStatment(insertIntoProperty, k++, null);
				insertIntoProperty.setString(k++, currProperty.getPropertyId());
				insertIntoProperty.setString(k++, null);
				if(this.getDatabaseName().equals("ACCESS")){
            		insertIntoProperty.setString(k++, null);
            	} else {
            		insertIntoProperty.setObject(k++, null, java.sql.Types.BIGINT);
            	}
				insertIntoProperty.setString(k++, null);
				insertIntoProperty.setString(k++, null);
				insertIntoProperty.setString(k++, context);
				insertIntoProperty.setString(k++, " ");
				insertIntoProperty.setString(k++, null);
				
				insertIntoProperty.executeUpdate();
			}
			
			Iterator<PropertyQualifier> qualIter = currProperty.getPropertyQualifier().iterator();
			while (qualIter.hasNext()) {
				PropertyQualifier pQual = qualIter.next();
				
				int k = 1;
				
				int entryId = getNextEntryId();
				insertEntryType(VSDConstants.ENTRY_STATE_TYPE_ATTRIBUTE, entryId);

				insertIntoProperty.setInt(k++, entryId);
				insertIntoProperty.setInt(k++, propEntryId);
				insertIntoProperty.setString(k++, pQual.getPropertyQualifierName());
				setBooleanOnPreparedStatment(insertIntoProperty, k++, true);
				insertIntoProperty.setString(k++, pQual.getPropertyQualifierType());
				insertIntoProperty.setString(k++, null);
				setBooleanOnPreparedStatment(insertIntoProperty, k++, null);
				setBooleanOnPreparedStatment(insertIntoProperty, k++, null);
				insertIntoProperty.setString(k++, currProperty.getPropertyId());
				insertIntoProperty.setString(k++, pQual.getValue() != null ? pQual.getValue().getDataType() : null);
				if(this.getDatabaseName().equals("ACCESS")){
            		insertIntoProperty.setString(k++, null);
            	} else {
            		insertIntoProperty.setObject(k++, null, java.sql.Types.BIGINT);
            	}
				insertIntoProperty.setString(k++, null);
				insertIntoProperty.setString(k++, null);
				insertIntoProperty.setString(k++, pQual.getValue() != null ? pQual.getValue().getValue() : null);
				insertIntoProperty.setString(k++, " ");
				insertIntoProperty.setString(k++, null);
				
				insertIntoProperty.executeUpdate();
			}
            insertIntoProperty.close();
		} catch (SQLException e) {
			throw new ObjectAlreadyExistsException(e);
		} catch (Exception e) {
			throw new InsertException(e);
		} finally {
			try {
				if (insertIntoProperty != null)
					insertIntoProperty.close();
			} catch (Exception e) {
				// do nothing
			}
		}
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.LexGrid.managedobj.service.BaseService#getInstanceClass()
     */
    protected Class getInstanceClass()
    {
        return PropertyImpl.class;
    }
    
    protected void initNestedServices() throws ServiceInitException
    {
        entryTypeServices_ = new VSDEntryTypeServices(this);
        entryStateServices_ = new VSDEntryStateService(this);
    }
    
    public void update(ManagedObjIF obj) throws UpdateException, ObjectNotFoundException
	{
		// TODO: Implement update
		throw new java.lang.UnsupportedOperationException("Method update not yet implemented.");
	}
	
	public void remove(int foreignEntryId, String foreignEntryType) throws SQLException, FindException
	{
		PreparedStatement selectFromProperty = null;
        ArrayList<Integer> propEntryIds = new ArrayList<Integer>();
        ArrayList<Integer> entryStateIds = new ArrayList<Integer>();
        
        try
        {
            selectFromProperty = checkOutPreparedStatement(modifySql(
            		" SELECT P1. " + SQLTableConstants.TBLCOL_ENTRYID +
            		" , " + SQLTableConstants.TBLCOL_ENTRYSTATEID +
            		" FROM " + VSDConstants.TBL_PROPERTY + " {AS} P1 " +
            		" , " + VSDConstants.TBL_ENTRY_TYPE + " {AS} P2 " +
            		" WHERE P1." + SQLTableConstants.TBLCOL_FOREIGNENTRYID + " = ? " +
            		" AND P1." + SQLTableConstants.TBLCOL_FOREIGNENTRYID + " = P2." + SQLTableConstants.TBLCOL_REFERENCEENTRYID +
            		" AND P2." + SQLTableConstants.TBLCOL_ENTRYTYPE + " = ?"));
            
            selectFromProperty.setInt(1, foreignEntryId);
            selectFromProperty.setString(2, foreignEntryType);
            
            ResultSet results = selectFromProperty.executeQuery();
            String entryStateIdStr = null;
            while (results.next())
            {
                propEntryIds.add(results.getInt(SQLTableConstants.TBLCOL_ENTRYID));
                entryStateIdStr = results.getString(SQLTableConstants.TBLCOL_ENTRYSTATEID);
                if (StringUtils.isNotEmpty(entryStateIdStr))
                	entryStateIds.add(Integer.valueOf(entryStateIdStr));
            }
            results.close();
        }
        finally
        {
            checkInPreparedStatement(selectFromProperty);
        }
            
        for (int propEntryId : propEntryIds)
        {
        	// remove property qualifiers and attributes of this property
            removePropQualsAndAttribs(propEntryId);
            
        	// remove property
            removeProperty(propEntryId);
        }
        
        if (entryStateIds.size() > 0)
        {
        	// remove this property entryState
            entryStateServices_.remove(entryStateIds, SQLTableConstants.ENTRY_STATE_TYPE_PROPERTY);
        }
        
        if (propEntryIds.size() > 0)
        {
        	// remove from entryType table
          	entryTypeServices_.remove(propEntryIds, SQLTableConstants.ENTRY_STATE_TYPE_PROPERTY);
        }
        
	}
	
	public void removePropQualsAndAttribs(int entryId) throws FindException, SQLException{
		PreparedStatement selectPropQualsAndAttribsFromProperty = null;
    	PreparedStatement deletePropQualsAndAttribsFromProperty = null;
        
    	ArrayList<Integer> propEntryIds = new ArrayList<Integer>();
    	
        try
        {
        	selectPropQualsAndAttribsFromProperty = checkOutPreparedStatement(modifySql(
            		" SELECT P1." + SQLTableConstants.TBLCOL_ENTRYID +
            		" FROM " + VSDConstants.TBL_PROPERTY + " {AS} P1 " +
            		" , " + VSDConstants.TBL_ENTRY_TYPE + " {AS} P2 " +
            		" WHERE P1." + SQLTableConstants.TBLCOL_FOREIGNENTRYID + " = ? " +
            		" AND P1." + SQLTableConstants.TBLCOL_ENTRYID + " = P2." + SQLTableConstants.TBLCOL_REFERENCEENTRYID +
            		" AND P2." + SQLTableConstants.TBLCOL_ENTRYTYPE + " = ?"));
            
        	selectPropQualsAndAttribsFromProperty.setInt(1, entryId);
        	selectPropQualsAndAttribsFromProperty.setString(2, VSDConstants.ENTRY_STATE_TYPE_ATTRIBUTE);
            
            ResultSet results = selectPropQualsAndAttribsFromProperty.executeQuery();

            while (results.next())
            {
            	propEntryIds.add(results.getInt(SQLTableConstants.TBLCOL_ENTRYID));
            }
            
            results.close();
        }
        finally
        {
        	checkInPreparedStatement(selectPropQualsAndAttribsFromProperty);
        } 
        
        try
        {
        	
        	deletePropQualsAndAttribsFromProperty = checkOutPreparedStatement(modifySql(
            		" DELETE FROM " + VSDConstants.TBL_PROPERTY + 
            		" WHERE " + SQLTableConstants.TBLCOL_ENTRYID + " = ? "));
            
            
            for (int propEntryId : propEntryIds)
            {
	            deletePropQualsAndAttribsFromProperty.setInt(1, propEntryId);
	            
	            deletePropQualsAndAttribsFromProperty.executeUpdate();
	            deletePropQualsAndAttribsFromProperty.clearParameters();
            }
            
            if (propEntryIds.size() > 0)
            {
            	entryTypeServices_.remove(propEntryIds, VSDConstants.ENTRY_STATE_TYPE_ATTRIBUTE);            
            	propEntryIds.clear();
            }
        }
        finally
        {
        	checkInPreparedStatement(deletePropQualsAndAttribsFromProperty);
        } 
        
        propEntryIds.clear();
    }
	
	private void removeProperty(int entryId) throws FindException, SQLException{
    	PreparedStatement deleteProperty = null;
        
        try
        {
            deleteProperty = checkOutPreparedStatement(modifySql(
            		" DELETE FROM " + VSDConstants.TBL_PROPERTY + 
            		" WHERE " + SQLTableConstants.TBLCOL_ENTRYID + " = ? "));
            
            deleteProperty.setInt(1, entryId);
            
            deleteProperty.executeUpdate();            
        }
       finally
        {
            checkInPreparedStatement(deleteProperty);
        }
    }
	
    protected void insertEntryType(Property currProperty, int entryId) throws InsertException, ObjectAlreadyExistsException
	{
    	entryTypeServices_.insert(currProperty, entryId);
	}
    
    protected void insertEntryType(String entryType, int entryId) throws InsertException, ObjectAlreadyExistsException {
    	entryTypeServices_.insert(entryType, entryId);
    }
    
    protected void insertEntryState(Property currProperty, int entryId) throws InsertException, ObjectAlreadyExistsException
	{
    	entryStateServices_.insert(currProperty, entryId, null);
	}
    
    protected String resolveEntryType(int entryId) throws FindException
	{
    	return entryTypeServices_.resolveEntryTypeFor(entryId);
	}
    
    protected void resolveEntryState(Property currProperty, int entryId) throws FindException
	{
    	entryStateServices_.findbyPrimaryKey(currProperty, entryId);
	}
}