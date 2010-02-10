/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.lexevs.logging;

/**
 * @author <A HREF="mailto:rokickik@mail.nih.gov">Konrad Rokicki</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class SimpleLogger implements LgLoggerIF {

    public void busy() {
    }

    public String debug(String message) {
        return null;
    }

    public String error(String message, Throwable sourceException) {
        System.out.println("Error: " + message);
        sourceException.printStackTrace();
        return null;
    }

    public String error(String message) {
        System.out.println("Error: " + message);
        return null;
    }

    public String fatal(String message, Throwable sourceException) {
        System.out.println("Error: " + message);
        sourceException.printStackTrace();
        return null;
    }

    public String fatal(String message) {
        System.out.println("Error: " + message);
        return null;
    }

    public void fatalAndThrowException(String message, Throwable sourceException) throws Exception {
        System.out.println("Error: " + message);
        sourceException.printStackTrace();
        throw new Exception(message);
    }

    public void fatalAndThrowException(String message) throws Exception {
        System.out.println("Error: " + message);
        throw new Exception(message);
    }

    public String info(String message) {
        System.out.println("Info: " + message);
        return null;
    }

    public String warn(String message, Throwable sourceException) {
        System.out.println("Warn: " + message);
        sourceException.printStackTrace();
        return null;
    }

    public String warn(String message) {
        System.out.println("Warn: " + message);
        return null;
    }

    public void logMethod() {
    }

    public void logMethod(Object[] params) {
    }

    public void loadLogDebug(String message) {
        System.out.println("Debug: " + message);
    }

    public void loadLogError(String message, Throwable e) {
        System.out.println("Error: " + message);
        e.printStackTrace();
    }

    public void loadLogError(String message) {
        System.out.println("Error: " + message);
    }

    public void loadLogWarn(String message, Throwable e) {
        System.out.println("Warn: " + message);
        e.printStackTrace();
    }
}