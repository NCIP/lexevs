package org.LexGrid.LexBIG.Extensions.Load;
import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBException;

public interface UriBasedHistoryLoader extends Loader {

	public void load(URI source, URI versions, boolean append,
			boolean stopOnErrors, boolean async) throws LBException;

	public void validate(URI source, URI versions, int validationLevel)
			throws LBException;
}
