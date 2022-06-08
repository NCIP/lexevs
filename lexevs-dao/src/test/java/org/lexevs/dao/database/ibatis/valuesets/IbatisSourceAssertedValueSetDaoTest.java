package org.lexevs.dao.database.ibatis.valuesets;

import org.junit.Test;

import static org.junit.Assert.*;

public class IbatisSourceAssertedValueSetDaoTest {
    IbatisSourceAssertedValueSetDao valueSetDao = new IbatisSourceAssertedValueSetDao();

    @Test
    public void doGetSupportedLgSchemaVersions() {
        valueSetDao.doGetSupportedLgSchemaVersions();
    }

    @Test
    public void getSourceAssertedValueSetEntitiesForEntityCode() {
        valueSetDao.getSourceAssertedValueSetEntitiesForEntityCode("code", "relation", "predicate", "codingScheme");
    }

    @Test
    public void getSourceAssertedValueSetTopNodeForEntityCode() {
        valueSetDao.getSourceAssertedValueSetTopNodeForEntityCode("code", "codingScheme");
    }

    @Test
    public void getSourceAssertedValueSetTopNodeDescription() {
        valueSetDao.getSourceAssertedValueSetTopNodeDescription("description", "codingScheme");
    }

    @Test
    public void getValueSetEntityUids() {
        valueSetDao.getValueSetEntityUids("codingScheme", "predicate", 0, -1);
    }

    @Test
    public void getValueSetEntityUidForTopNodeEntityCode() {
        valueSetDao.getValueSetEntityUidForTopNodeEntityCode("codingScheme", "predicate", "topnode", 0, -1);
    }

    @Test
    public void getPagedValueSetEntities() {
        valueSetDao.getPagedValueSetEntities("code", "codingScheme", "predicate", 0, -1);
    }

    @Test
    public void getValueSetEntityCount() {
        valueSetDao.getValueSetEntityCount("code", "codingscheme", "predicate");
    }

    @Test
    public void getSourceAssertedValueSetsForVSMemberEntityCode() {
        valueSetDao.getSourceAssertedValueSetsForVSMemberEntityCode("code", "relation", "predicate", "codingScheme");
    }

    @Test
    public void getAllValidValueSetTopNodeCodes() {
        valueSetDao.getAllValidValueSetTopNodeCodes("prepertyName", "value", "predicate", "codingScheme");
    }

    @Test
    public void getValueSetEntityProperties() {
        valueSetDao.getValueSetEntityProperties("code", "codingScheme");
    }
}