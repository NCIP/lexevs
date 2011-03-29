package org.cts2.profile.author;

import org.cts2.core.ChangeSetBase;
import org.cts2.profile.update.UpdateService;

public interface BaseAuthoringService extends UpdateService {

	public ChangeSetBase readChangeSet(String changeSetUri);
}
