/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG.verify;

import java.io.File;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lexevs.logging.LoggerFactory;
import org.springframework.util.Assert;

/**
 * The Class LexEvsInstallationVerifyTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LexEvsInstallationVerifyTest {

	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		final LexEvsInstallationVerifyTest test = new LexEvsInstallationVerifyTest();
		
		//don't log all the extra info -- start this early.
		LoggerFactory.getLogger();
		LogManager.getRootLogger().atLevel(Level.ERROR);
		
		test.printTaskName("Starting LexEVS");
		
		test.doExecute(new ExecutorCallback() {

			@Override
			public void execute() throws Exception {
				LexBIGServiceImpl.defaultInstance();
			}
		});
		
		test.printTaskName("Test Load of Content");
		
		test.doExecute(new ExecutorCallback() {

			@Override
			public void execute() throws Exception {
				test.testLoadAutombiles();
			}
		});
		
		test.printTaskName("Test CodedNodeSet Query");
		
		test.doExecute(new ExecutorCallback() {

			@Override
			public void execute() throws Exception {
				test.testCodedNodeSetQuery();
			}
		});
		
		test.printTaskName("Test CodedNodeGraph Query");
		
		test.doExecute(new ExecutorCallback() {

			@Override
			public void execute() throws Exception {
				test.testCodedNodeGraphQuery();
			}
		});
		
		test.printTaskName("Test Remove Content");
		
		test.doExecute(new ExecutorCallback() {

			@Override
			public void execute() throws Exception {
				test.testRemoveAutombiles();
			}
		});
	}
	
	/**
	 * Prints the task name.
	 * 
	 * @param taskName the task name
	 */
	private void printTaskName(String taskName) {
		System.out.println();
		System.out.println("==============================");
		System.out.println(taskName);
		System.out.println("==============================");
	}
	
	/**
	 * Test load autombiles.
	 * 
	 * @throws LBParameterException the LB parameter exception
	 * @throws LBInvocationException the LB invocation exception
	 * @throws InterruptedException the interrupted exception
	 * @throws LBException the LB exception
	 */
	public void testLoadAutombiles() throws LBParameterException, LBInvocationException, InterruptedException,
		LBException {
		LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);
		LexGridMultiLoaderImpl loader = (LexGridMultiLoaderImpl) lbsm.getLoader("LexGrid_Loader");

		loader.load(new File("resources/testData/Automobiles.xml").toURI(), true, false);

		lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

		lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
	}
	
	/**
	 * Test coded node set query.
	 * 
	 * @throws LBParameterException the LB parameter exception
	 * @throws LBInvocationException the LB invocation exception
	 * @throws InterruptedException the interrupted exception
	 * @throws LBException the LB exception
	 */
	public void testCodedNodeSetQuery() throws LBParameterException, LBInvocationException, InterruptedException,
		LBException {
		CodedNodeSet cns = LexBIGServiceImpl.defaultInstance().getNodeSet(
				LexBIGServiceTestCase.AUTO_URN, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.AUTO_VERSION), null);
		
		ResolvedConceptReferencesIterator itr = cns.resolve(null, null, null);
		
		Assert.state(itr.hasNext(), "CodedNodeSet did not resolve any results.");
		
		//cycle through.
		while(itr.hasNext()) {
			itr.next();
		}
	}
	
	/**
	 * Test coded node graph query.
	 * 
	 * @throws LBParameterException the LB parameter exception
	 * @throws LBInvocationException the LB invocation exception
	 * @throws InterruptedException the interrupted exception
	 * @throws LBException the LB exception
	 */
	public void testCodedNodeGraphQuery() throws LBParameterException, LBInvocationException, InterruptedException,
		LBException {
		CodedNodeGraph cng = LexBIGServiceImpl.defaultInstance().getNodeGraph(
				LexBIGServiceTestCase.AUTO_URN, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(LexBIGServiceTestCase.AUTO_VERSION), null);
		
		ResolvedConceptReferenceList list = cng.resolveAsList(null, true, false, -1, -1, null, null, null, -1);
		
		Assert.state(list.getResolvedConceptReferenceCount() > 0, "CodedNodeGraph did not resolve any results.");
	}
	
    /**
     * Test remove autombiles.
     * 
     * @throws LBException the LB exception
     */
    public void testRemoveAutombiles() throws LBException {
        LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:oid:11.11.0.1", "1.0");

        lbsm.deactivateCodingSchemeVersion(a, null);

        lbsm.removeCodingSchemeVersion(a);
    }
	
	/**
	 * The Interface ExecutorCallback.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private interface ExecutorCallback {
		
		/**
		 * Execute.
		 * 
		 * @throws Exception the exception
		 */
		public void execute() throws Exception;
	}
	
	/**
	 * Do execute.
	 * 
	 * @param callback the callback
	 */
	private void doExecute(final ExecutorCallback callback) {
		
		ExecutorThread executorThread = new ExecutorThread(callback);
		
		executorThread.start();
		
		while(executorThread.isAlive()) {
			System.out.print(".");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		
		if(executorThread.ex == null) {
			System.out.println("[OK]");
		} else {
			System.out.println("[FAIL]");
		}
	}

	
	/**
	 * The Class ExecutorThread.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private class ExecutorThread extends Thread {
		
		/** The ex. */
		Exception ex;
		
		/** The callback. */
		ExecutorCallback callback;
		
		/**
		 * Instantiates a new executor thread.
		 * 
		 * @param callback the callback
		 */
		ExecutorThread(ExecutorCallback callback){
			this.callback = callback;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			try {
				callback.execute();
			} catch (Exception e) {
				ex = e;
				return;
			}
		}
	}
}