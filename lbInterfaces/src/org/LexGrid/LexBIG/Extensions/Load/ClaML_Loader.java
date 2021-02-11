
package org.LexGrid.LexBIG.Extensions.Load;

import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBException;

public interface ClaML_Loader {
	public void load(String path, boolean stopOnErrors, boolean async) throws LBException;
	public void validate(URI source, int validationLevel) throws LBException;
}