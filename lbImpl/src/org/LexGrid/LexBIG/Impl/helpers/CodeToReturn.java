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
package org.LexGrid.LexBIG.Impl.helpers;

import java.io.Serializable;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.annotations.LgClientSideSafe;
import org.apache.commons.lang.StringUtils;
import org.lexevs.system.ResourceManager;

/**
 * Class to hold the details for a code match.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CodeToReturn implements Serializable {

    private static final long serialVersionUID = -6930464365388016514L;
    private String code_ = null;
    private String entityDescription_ = null;
    private String[] entityTypes_ = null;
    private String namespace_ = null;
    private float score_ = 0;
    private String uri_ = null;
    private String version_ = null;
    private String entityUid;

    public CodeToReturn() {
        super();
    }

    public CodeToReturn(String code, String entityDescription, String uri, String version) {
        this(code, entityDescription, uri, version, 0);
    }

    public CodeToReturn(String code, String entityDescription, String uri, String version, float score) {
        this(code, entityDescription, uri, version, score, null, null);
    }

    public CodeToReturn(String code, String entityDescription, String uri, String version, float score, String namespace, String[] entityTypes) {
        code_ = code;
        entityDescription_ = entityDescription;
        entityTypes_ = entityTypes;
        namespace_ = namespace;
        score_ = score;
        uri_ = uri;
        
        if (version == null) {
            try {
                version_ = ResourceManager.instance().getInternalVersionStringForTag(uri, null);
            } catch (LBParameterException e) {
                // this can happen when resolving graphs - links to code systems
                // that don't exist.
            }
        } else {
            version_ = version;
        }
    }

    @Override
    @LgClientSideSafe
    public boolean equals(Object obj) {
        if (obj instanceof CodeToReturn) {
            CodeToReturn a = (CodeToReturn) obj;
            if (StringUtils.equals(this.getCode(), a.getCode())) {
                if(StringUtils.isNotBlank(this.getNamespace()) && StringUtils.isNotBlank(a.getNamespace())){
                    if (StringUtils.equals(this.getNamespace(), a.getNamespace())) {
                        return true;
                    } else {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @LgClientSideSafe
    public String getCode() {
        return this.code_;
    }

    @LgClientSideSafe
    public String getEntityDescription() {
        return this.entityDescription_;
    }

    @LgClientSideSafe
    public String[] getEntityTypes() {
        return entityTypes_;
    }

    @LgClientSideSafe
    public String getNamespace() {
        return namespace_;
    }

    @LgClientSideSafe
    public float getScore() {
        return this.score_;
    }

    @LgClientSideSafe
    public String getUri() {
        return this.uri_;
    }

    @LgClientSideSafe
    public String getVersion() {
        return this.version_;
    }

    @LgClientSideSafe
    public void setCode(String code) {
        this.code_ = code;
    }

    @LgClientSideSafe
    public void setEntityDescription(String entityDescription) {
        this.entityDescription_ = entityDescription;
    }

    @LgClientSideSafe
    public void setEntityTypes(String[] entityTypes) {
        this.entityTypes_ = entityTypes;
    }

    @LgClientSideSafe
    public void setNamespace(String namespace) {
        this.namespace_ = namespace;
    }

    @LgClientSideSafe
    public void setScore(float score) {
        this.score_ = score;
    }

    @LgClientSideSafe
    public void setUri(String uri) {
        this.uri_ = uri;
    }

    @LgClientSideSafe
    public void setVersion(String version) {
        this.version_ = version;
    }
    
    public String toString(){
        return this.getUri() + " : " + this.getCode();
    }

    @LgClientSideSafe
    public void setEntityUid(String entityUid) {
        this.entityUid = entityUid;
    }

    @LgClientSideSafe
    public String getEntityUid() {
        return entityUid;
    }

    public void compact() {
        //do nothing -- subclasses or lazy loaded CodeToReturs may implement this;
    }
}