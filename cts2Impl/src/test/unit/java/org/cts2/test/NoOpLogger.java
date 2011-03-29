package org.cts2.test;

import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;

public class NoOpLogger implements LgLoggerIF {

	@Override
	public void busy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String info(String message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String debug(String message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String warn(String message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String warn(String message, Throwable sourceException) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String error(String message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String error(String message, Throwable sourceException) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String fatal(String message) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String fatal(String message, Throwable sourceException) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void fatalAndThrowException(String message) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fatalAndThrowException(String message, Throwable sourceException)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logMethod() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void logMethod(Object[] params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadLogDebug(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadLogError(String message, Throwable e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadLogError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadLogWarn(String message, Throwable e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exportLogDebug(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exportLogError(String message, Throwable e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exportLogError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exportLogWarn(String message, Throwable e) {
		// TODO Auto-generated method stub
		
	}

}
