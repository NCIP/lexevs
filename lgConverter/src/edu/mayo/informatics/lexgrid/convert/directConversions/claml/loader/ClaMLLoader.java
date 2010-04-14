package edu.mayo.informatics.lexgrid.convert.directConversions.claml.loader;

import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Load.Loader;

public interface ClaMLLoader extends Loader{

	public void load(URI source) throws LBException;
}
