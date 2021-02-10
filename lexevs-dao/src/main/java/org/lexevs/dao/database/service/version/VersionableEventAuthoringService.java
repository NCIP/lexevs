
package org.lexevs.dao.database.service.version;

import java.util.Arrays;
import java.util.Comparator;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.codingSchemes.CodingSchemes;
import org.LexGrid.commonTypes.Versionable;
import org.LexGrid.naming.Mappings;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.PickListDefinitions;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitions;
import org.LexGrid.versions.ChangedEntry;
import org.LexGrid.versions.EditHistory;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.Revision;
import org.LexGrid.versions.SystemRelease;
import org.LexGrid.versions.types.ChangeType;
import org.lexevs.dao.database.access.revision.RevisionDao;
import org.lexevs.dao.database.access.systemRelease.SystemReleaseDao;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.valuesets.PickListDefinitionService;
import org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class VersionableEventVersionService.
 * 
 * @author <a href="mailto:rao.ramachandra@mayo.edu">Ramachandra Rao (Satya)</a>
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class VersionableEventAuthoringService extends AbstractDatabaseService
		implements AuthoringService {

	/** The coding scheme service. */
	private CodingSchemeService codingSchemeService = null;

	/** The value set definition service. */
	private ValueSetDefinitionService valueSetDefinitionService = null;

	/** The pick list definition service. */
	private PickListDefinitionService pickListDefinitionService = null;

	/** The Constant LEXGRID_GENERATED_REVISION. */
	public static final String LEXGRID_GENERATED_REVISION = "autoGen-";
	
	/** The revision comparator. */
	private Comparator<Revision> revisionComparator = new Comparator<Revision>() {

		@Override
		public int compare(Revision rev1, Revision rev2) {
			
			Long editOrder1 = rev1.getEditOrder();
			Long editOrder2 = rev2.getEditOrder();
			
			if( editOrder1 == null && editOrder2 == null ) {
				return 0;
			}
			
			if( editOrder1 == null )
				return 1;
			
			if( editOrder2 == null )
				return -1;
			
			if( editOrder1 == editOrder2 ) {
				return 0;
			}
			
			return editOrder1 < editOrder2 ? -1 : 1;
		}
	};
	
	/** The changed entry comparator. */
	private Comparator<ChangedEntry> changedEntryComparator = new Comparator<ChangedEntry>() {

		@Override
		public int compare(ChangedEntry cEntry1, ChangedEntry cEntry2) {
			
			Versionable changedEntry1 = (Versionable) cEntry1.getChoiceValue();
			Versionable changedEntry2 = (Versionable) cEntry2.getChoiceValue();
			
			Long relativeOrder1 = null;
			Long relativeOrder2 = null;
			
			if( changedEntry1 != null ) {
				EntryState entryState1 = changedEntry1.getEntryState();
				
				if( entryState1 != null ) {
					relativeOrder1 = entryState1.getRelativeOrder();
				}
			}
			
			if( changedEntry2 != null ) {
				EntryState entryState2 = changedEntry2.getEntryState();
				
				if( entryState2 != null ) {
					relativeOrder2 = entryState2.getRelativeOrder();
				}
			}
			
			if( relativeOrder1 == null && relativeOrder2 == null ) {
				return 0;
			}
			
			if( relativeOrder1 == null )
				return 1;
			
			if( relativeOrder2 == null )
				return -1;
			
			if( relativeOrder1 == relativeOrder2 ) {
				return 0;
			}
			
			return relativeOrder1 < relativeOrder2 ? -1 : 1;
		}
	};
	
	/**
	 * Load system release. A systemRelease can contain a codingScheme,
	 * valueSet, pickList and/or revision objects. All codingScheme, valueSet
	 * and pickLists loaded outside revision are wrapped under a system
	 * generated revision object.
	 * 
	 * @param systemRelease the system release
	 * @param indexNewCodingScheme the index new coding scheme
	 * 
	 * @throws LBRevisionException the LB revision exception
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void loadSystemRelease(SystemRelease systemRelease, Boolean indexNewCodingScheme) throws LBRevisionException {

		if (systemRelease == null) {
			return;
		}
		
		RevisionDao revisionDao = this.getDaoManager().getRevisionDao();
		String revisionId = LEXGRID_GENERATED_REVISION + revisionDao.getNewRevisionId();
		String releaseURI = systemRelease.getReleaseURI();
		
		/* 1. Insert system release entry.*/
		SystemReleaseDao sysReleaseDao = this.getDaoManager()
				.getSystemReleaseDao();
		sysReleaseDao.insertSystemReleaseEntry(systemRelease);
		
		/* 2. Insert revision entry.*/
		CodingSchemes codingSchemes = systemRelease.getCodingSchemes();
		ValueSetDefinitions valueSetDefinitions = systemRelease
				.getValueSetDefinitions();
		PickListDefinitions pickLists = systemRelease.getPickListDefinitions();
		
		if (codingSchemes != null || valueSetDefinitions != null
				|| pickLists != null) {

			Revision revision = new Revision();
			revision.setRevisionId(revisionId);
			revisionDao.insertRevisionEntry(revision, releaseURI);
		}
		
		/* 3. Add coding schemes.*/
		if (codingSchemes != null) {
			CodingScheme[] codingSchemeList = codingSchemes.getCodingScheme();

			for (int i = 0; i < codingSchemeList.length; i++) {
				
				codingSchemeList[i].setEntryState(getEntryState(revisionId));

				try {
					codingSchemeService.revise(codingSchemeList[i], releaseURI, indexNewCodingScheme);
				} catch (LBException e) {
					super.getLogger().error(
							"Error occured while revising the codingScheme: "
									+ e.getMessage());
				}
			}
		}

		/* 4. Add valueset definitions.*/
		if (valueSetDefinitions != null) {

			ValueSetDefinition[] valueSetDefList = valueSetDefinitions
					.getValueSetDefinition();
			Mappings vsMapping = valueSetDefinitions.getMappings();

			for (int i = 0; i < valueSetDefList.length; i++) {

				valueSetDefList[i].setEntryState(getEntryState(revisionId));

				try {
					valueSetDefinitionService.revise(valueSetDefList[i],
							vsMapping, releaseURI);
				} catch (LBException e) {
					super.getLogger().error(
							"Error occured while revising the valueSetDefinition : "
									+ e.getMessage());
				}
			}
		}

		/* 5. Add picklist definitions.*/
		if (pickLists != null) {
			PickListDefinition[] pickListDefList = pickLists
					.getPickListDefinition();
			Mappings plMappings = pickLists.getMappings();

			for (int i = 0; i < pickListDefList.length; i++) {

				pickListDefList[i].setEntryState(getEntryState(revisionId));

				try {
					pickListDefinitionService.revise(pickListDefList[i],
							plMappings, releaseURI);
				} catch (LBException e) {
					super.getLogger().error(
							"Error occured while revising the pickListDefinition : "
									+ e.getMessage());
				}
			}
		}
		
		/* 6. Process revisions.*/
		EditHistory editHistory = systemRelease.getEditHistory();
		if (editHistory != null) {
			Revision[] revisionList = editHistory.getRevision();
			
			if (revisionList != null && revisionList.length != 0) {
				
				Arrays.sort(revisionList, revisionComparator);

				for (int i = 0; i < revisionList.length; i++) {

					loadRevision(revisionList[i], systemRelease.getReleaseURI(), indexNewCodingScheme);
				}
			}
		}
	}

	/**
	 * Method Loads the revision of an entry point object in lexEVS system.
	 * Revision will be validated for proper syntax and sequence before loading.
	 * If invalid, LBRevisionException is thrown. Entry point objects in lexEVS
	 * system are CodingScheme, ValueSet and PickList. A revision can contain
	 * single or multiple instances of one or all of the entry point objects.
	 * ChangedEntries are loaded by ascending order of relativeOrder.
	 * 
	 * @param revision - revision object to be applied.
	 * @param indexNewCodingScheme - Boolean value to indicate if the any newly loaded codingScheme
	 * in this revision needs to Lucene indexed or not.
	 * @param releaseURI the release uri
	 * 
	 * @throws LBRevisionException the LB revision exception
	 */
	@Override
	@Transactional(rollbackFor=Exception.class)
	public void loadRevision(Revision revision, String releaseURI, Boolean indexNewCodingScheme) throws LBRevisionException {

		if (revision == null)
			return;

		ChangedEntry[] changedEntry = revision.getChangedEntry();

		if (changedEntry != null && changedEntry.length != 0) {
			
			RevisionDao revisionDao = this.getDaoManager().getRevisionDao();
			revisionDao.insertRevisionEntry(revision, releaseURI);
			
			Arrays.sort(changedEntry, changedEntryComparator);
			
			for (int j = 0; j < changedEntry.length; j++) {

				ChangedEntry cEntry = changedEntry[j];

				if (cEntry != null) {

					// Process CodingScheme revisions
					try {
						CodingScheme codingScheme = cEntry
								.getChangedCodingSchemeEntry();
						if (codingScheme != null) {
							codingSchemeService
									.revise(codingScheme, releaseURI, indexNewCodingScheme);
							continue;
						}
					} catch (LBException e) {
						super.getLogger().error(
								"Error occured while revising the codingScheme: " 
										+ e.getMessage());
						throw new LBRevisionException("Error occured while revising the codingScheme: " 
										+ e.getMessage());
					}

					// Process ValueSet Definition revisions
					try {
						ValueSetDefinition valueSetDefinition = cEntry
								.getChangedValueSetDefinitionEntry();
						if (valueSetDefinition != null) {
							valueSetDefinitionService.revise(
									valueSetDefinition, valueSetDefinition.getMappings(), releaseURI);
							continue;
						}
					} catch (LBException e) {
						super.getLogger().error(
								"Error occured while revising the valueSetDefinition : "
										+ e.getMessage());
						throw new LBRevisionException("Error occured while revising the valueSetDefinition : "
										+ e.getMessage());
					}

					// Process PickList Definition revisions
					try {
						PickListDefinition pickListDef = cEntry
								.getChangedPickListDefinitionEntry();
						if (pickListDef != null) {

							pickListDefinitionService.revise(pickListDef, null,
									releaseURI);
							continue;
						}
					} catch (LBException e) {
						super.getLogger().error(
								"Error occured while revising the pickListDefinition : "
										+ e.getMessage());
						throw new LBRevisionException("Error occured while revising the pickListDefinition : "
								+ e.getMessage());
					}
				}
			}
		}
	}

	/**
	 * Method Loads an entry point versionable object by wrapping it into a
	 * revision. Revision will be validated for proper syntax and sequence
	 * before loading. If invalid, LBRevisionException is thrown. Entry point
	 * objects in lexEVS system are CodingScheme, ValueSet and PickList.
	 * 
	 * @param versionable the versionable
	 * @param releaseURI - URI of the systemRelease (if any)
	 * @param indexNewCodingScheme - Boolean value to indicate if the any newly loaded
	 * codingScheme in this revision needs to Lucene indexed or not.
	 * 
	 * @throws LBRevisionException the LB revision exception
	 */
	@Transactional(rollbackFor=Exception.class)
	public void loadRevision(Versionable versionable, String releaseURI, Boolean indexNewCodingScheme)
			throws LBRevisionException {

		RevisionDao revisionDao = this.getDaoManager().getRevisionDao();
		Revision revision = new Revision();
		revision.setRevisionId(LEXGRID_GENERATED_REVISION + revisionDao.getNewRevisionId());

		ChangedEntry changeEntry = new ChangedEntry();

		if( versionable.getEntryState() == null ) {
			EntryState entryState = new EntryState();
			
			entryState.setChangeType(ChangeType.NEW);
			entryState.setContainingRevision(revision.getRevisionId());
			entryState.setRelativeOrder(0L);
			
			versionable.setEntryState(entryState);
		}
		
		if (versionable instanceof CodingScheme) {

			changeEntry.setChangedCodingSchemeEntry((CodingScheme) versionable);

		} else if (versionable instanceof ValueSetDefinition) {
			changeEntry
					.setChangedValueSetDefinitionEntry((ValueSetDefinition) versionable);
		} else if (versionable instanceof PickListDefinition) {
			changeEntry
					.setChangedPickListDefinitionEntry((PickListDefinition) versionable);
		}

		revision.addChangedEntry(changeEntry);

		loadRevision(revision, releaseURI, indexNewCodingScheme);
	}
	
	/**
	 * Gets the coding scheme service.
	 * 
	 * @return the codingSchemeService
	 */
	public CodingSchemeService getCodingSchemeService() {
		return codingSchemeService;
	}

	/**
	 * Sets the coding scheme service.
	 * 
	 * @param codingSchemeService the coding scheme service
	 */
	public void setCodingSchemeService(CodingSchemeService codingSchemeService) {
		this.codingSchemeService = codingSchemeService;
	}

	/**
	 * Gets the value set definition service.
	 * 
	 * @return the valueSetDefinitionService
	 */
	public ValueSetDefinitionService getValueSetDefinitionService() {
		return valueSetDefinitionService;
	}

	/**
	 * Sets the value set definition service.
	 * 
	 * @param valueSetDefinitionService the valueSetDefinitionService to set
	 */
	public void setValueSetDefinitionService(
			ValueSetDefinitionService valueSetDefinitionService) {
		this.valueSetDefinitionService = valueSetDefinitionService;
	}

	/**
	 * Gets the pick list definition service.
	 * 
	 * @return the pickListDefinitionService
	 */
	public PickListDefinitionService getPickListDefinitionService() {
		return pickListDefinitionService;
	}

	/**
	 * Sets the pick list definition service.
	 * 
	 * @param pickListDefinitionService the pickListDefinitionService to set
	 */
	public void setPickListDefinitionService(
			PickListDefinitionService pickListDefinitionService) {
		this.pickListDefinitionService = pickListDefinitionService;
	}

	/**
	 * Gets the entry state.
	 * 
	 * @param revisionId the revision id
	 * 
	 * @return the entry state
	 */
	private EntryState getEntryState(String revisionId) {
		EntryState entryState = new EntryState();
		
		entryState.setChangeType(ChangeType.NEW);
		entryState.setContainingRevision(revisionId);
		entryState.setRelativeOrder(0L);
		return entryState;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.version.AuthoringService#getSystemReleaseMetadataById(java.lang.String)
	 */
	@Override
	public SystemRelease getSystemReleaseMetadataById(String systemReleaseId) {
		SystemReleaseDao sysReleaseDao = this.getDaoManager().getSystemReleaseDao();
		return sysReleaseDao.getSystemReleaseMetadataById(systemReleaseId);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.version.AuthoringService#getSystemReleaseMetadataByUri(java.lang.String)
	 */
	@Override
	public SystemRelease getSystemReleaseMetadataByUri(String systemReleaseUri) {
		SystemReleaseDao sysReleaseDao = this.getDaoManager().getSystemReleaseDao();
		return sysReleaseDao.getSystemReleaseMetadataByUri(systemReleaseUri);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.version.AuthoringService#insertSystemReleaseMetadata(org.LexGrid.versions.SystemRelease)
	 */
	@Override
	public String insertSystemReleaseMetadata(SystemRelease systemRelease) {
		SystemReleaseDao sysReleaseDao = this.getDaoManager().getSystemReleaseDao();
		return sysReleaseDao.insertSystemReleaseEntry(systemRelease);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.dao.database.service.version.AuthoringService#removeRevisionRecordbyId(java.lang.String)
	 */
	@Override
	public boolean removeRevisionRecordbyId(String revisionId) throws LBException {
		RevisionDao revisionDao = this.getDaoManager().getRevisionDao();
		return revisionDao.removeRevisionById(revisionId);
	}
}