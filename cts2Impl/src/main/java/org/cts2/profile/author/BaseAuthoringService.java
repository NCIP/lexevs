/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package org.cts2.profile.author;

import org.cts2.core.ChangeSetBase;
import org.cts2.core.ChangeableResource;
import org.cts2.core.OpaqueData;
import org.cts2.core.SourceReference;
import org.cts2.core.types.EntryState;
import org.cts2.profile.update.UpdateService;
import org.cts2.service.core.NameOrURI;
import org.cts2.updates.ChangeSet;

/**
 * The Interface BaseAuthoringService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface BaseAuthoringService extends UpdateService {

	/**
	 * Read change set.
	 *
	 * @param changeSetUri the change set uri
	 * @return the change set base
	 */
	public ChangeSetBase readChangeSet(String changeSetUri);
	
	/**
	 * Commit change set.
	 *
	 * @param changeSetUri the change set uri
	 */
	public void commitChangeSet(String changeSetUri);
	
	/**
	 * Creates the change set.
	 *
	 * @return the change set
	 */
	public ChangeSet createChangeSet();
	
	/**
	 * Delete changeable.
	 *
	 * @param changeSetUri the change set uri
	 * @param target the target
	 * @return the change set
	 */
	public ChangeSet deleteChangeable(String changeSetUri, ChangeableResource target);
	
	/**
	 * Rollback change set.
	 *
	 * @param changeSetUri the change set uri
	 */
	public void rollbackChangeSet(String changeSetUri);
	
	/**
	 * Update change set metadata.
	 *
	 * @param changeSetUri the change set uri
	 * @param target the target
	 * @param owner the owner
	 * @param status the status
	 * @param entryState the entry state
	 */
	public void updateChangeSetMetadata(String changeSetUri, ChangeableResource target, NameOrURI owner, NameOrURI status, EntryState entryState);

	/**
	 * Update change set metadata.
	 *
	 * @param changeSetUri the change set uri
	 * @param creator the creator
	 * @param changeInstructions the change instructions
	 */
	public void updateChangeSetMetadata(String changeSetUri, SourceReference creator, OpaqueData changeInstructions);
}
