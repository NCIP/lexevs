package org.cts2.internal.profile.author;

import java.util.Date;

import org.cts2.codesystemversion.CodeSystemVersion;
import org.cts2.core.ChangeSetBase;
import org.cts2.core.ChangeableResource;
import org.cts2.core.OpaqueData;
import org.cts2.core.SourceAndNotation;
import org.cts2.core.SourceReference;
import org.cts2.core.types.EntryState;
import org.cts2.internal.profile.AbstractBaseService;
import org.cts2.profile.author.CodeSystemVersionAuthoringService;
import org.cts2.service.codesystemversion.UpdateCodeSystemVersionRequest;
import org.cts2.service.core.ChangeSetEntryList;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.SuccessIndicator;
import org.cts2.service.core.ValidationResponse;
import org.cts2.updates.ChangeSet;

public class LexEvsCodeSystemVersionAuthoringService extends AbstractBaseService
	implements CodeSystemVersionAuthoringService {

	@Override
	public ChangeSetBase readChangeSet(String changeSetUri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void commitChangeSet(String changeSetUri) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ChangeSet createChangeSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChangeSet deleteChangeable(String changeSetUri,
			ChangeableResource target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rollbackChangeSet(String changeSetUri) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateChangeSetMetadata(String changeSetUri,
			ChangeableResource target, NameOrURI owner, NameOrURI status,
			EntryState entryState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateChangeSetMetadata(String changeSetUri,
			SourceReference creator, OpaqueData changeInstructions) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SuccessIndicator putChangeSet(ChangeSet changeSet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValidationResponse validateChangeSet(ChangeSet changeSet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ChangeSetEntryList listChanges(Date fromDate, Date toDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createCodeSystemVersion(String changeSetUri,
			String documentUri, String name,
			SourceAndNotation sourceAndNotation, NameOrURI versionOf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CodeSystemVersion updateCodeSystemVersion(
			NameOrURI codeSystemVersion, UpdateCodeSystemVersionRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
