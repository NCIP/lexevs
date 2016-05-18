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
package org.LexGrid.LexBIG.Impl.function;

import java.util.Enumeration;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.RenderingDetail;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;

public class TestUtil {

/**
     * Indicates whether a scheme is registered with the specified state.
     * 
     * @param testid
     * @param localName
     *            Local name to match; null to match any.
     * @param urn
     *            URN to match; null to match any.
     * @param version
     *            Version represented by scheme; null to match any.
     * @param status
     *            One of values enumerated on CodingSchemeVersionStatus; <0 to
     *            match any.
     * @return true if scheme exists with given status; false otherwise.
     * @throws LBInvocationException
     */
public static synchronized boolean verifyScheme(String localName, String urn, String version, CodingSchemeVersionStatus status)
            throws LBInvocationException {
        // Verify vocabulary is loaded and active ...
        boolean verified = false;

        LexBIGService lbSvc = ServiceHolder.instance().getLexBIGService();
        CodingSchemeRenderingList csrl = lbSvc.getSupportedCodingSchemes();
        Enumeration<? extends CodingSchemeRendering> csrlEnum = csrl.enumerateCodingSchemeRendering();
        while (csrlEnum.hasMoreElements() && !verified) {
            CodingSchemeRendering csr = csrlEnum.nextElement();
            CodingSchemeSummary css = csr.getCodingSchemeSummary();
            RenderingDetail rd = csr.getRenderingDetail();
            if ((localName == null || localName.equalsIgnoreCase(css.getLocalName()))
                    && (urn == null || urn.equalsIgnoreCase(css.getCodingSchemeURI()))
                    && (version == null || version.equalsIgnoreCase(css.getRepresentsVersion()))
                    && (status == null || status.equals(rd.getVersionStatus()))) {
                verified = true;
                break;
            }
        }

        return verified;
    }

    /**
     * Activate a coding scheme.
     * 
     * @param testid
     * @param urn
     * @param version
     * @return true if successful; false otherwise
     * @throws LBException
     */
    public static synchronized boolean activateScheme(String urn, String version) throws LBException {
        boolean result = false;

        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);
        lbsm.activateCodingSchemeVersion(ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(urn, version));

        // Verify vocabulary is active ...
        result = verifyScheme(null, urn, version, CodingSchemeVersionStatus.ACTIVE);

        return result;
    }

    /**
     * Deactivate a coding scheme.
     * 
     * @param urn
     * @param version
     * @return true if successful; false otherwise
     * @throws LBException
     */
    public static synchronized boolean deactivateScheme(String urn, String version) throws LBException {
        boolean result = false;

        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);
        lbsm.deactivateCodingSchemeVersion(ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(urn, version),
                null);

        // Verify vocabulary is active ...
        result = verifyScheme(null, urn, version, CodingSchemeVersionStatus.INACTIVE);

        return result;

    }

    /**
     * Remove a coding scheme.
     * 
     * @param urn
     * @param version
     * @return true if successful; false otherwise
     * @throws LBException
     */
    public static synchronized boolean removeScheme(String urn, String version) throws LBException {
        boolean result = false;

        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);
        lbsm.removeCodingSchemeVersion(ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(urn, version));

        // Verify vocabulary is active ...
        result = !verifyScheme(null, urn, version, null);

        return result;
    }

    public static void removeAll() throws Exception {
        LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
        LexBIGServiceManager lbsm = lbs.getServiceManager(null);

        for(CodingSchemeRendering csr : lbs.getSupportedCodingSchemes().getCodingSchemeRendering()) {
            AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference(csr.getCodingSchemeSummary());

            lbsm.deactivateCodingSchemeVersion(ref, null);
            lbsm.removeCodingSchemeVersion(ref);
        }

    }
}