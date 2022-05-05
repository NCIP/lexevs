/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.beanutils.DynaBean
 *  org.apache.commons.logging.Log
 *  org.apache.commons.logging.LogFactory
 */
package org.apache.ddlutils.io;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ddlutils.DatabaseOperationException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.dynabean.SqlDynaClass;
import org.apache.ddlutils.io.DataSink;
import org.apache.ddlutils.io.DataSinkException;
import org.apache.ddlutils.io.Identity;
import org.apache.ddlutils.io.WaitingObject;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Reference;
import org.apache.ddlutils.model.Table;

public class DataToDatabaseSink
implements DataSink {
    private final Log _log = LogFactory.getLog(DataToDatabaseSink.class);
    private Platform _platform;
    private Database _model;
    private Connection _connection;
    private boolean _haltOnErrors = true;
    private boolean _ensureFkOrder = true;
    private boolean _useBatchMode = false;
    private ArrayList _batchQueue = new ArrayList();
    private int _batchSize = 1024;
    private HashSet _fkTables = new HashSet();
    private HashSet _tablesWithSelfIdentityReference = new HashSet();
    private HashSet _tablesWithRequiredSelfReference = new HashSet();
    private HashMap _identityMap = new HashMap();
    private ArrayList<WaitingObject> _waitingObjects = new ArrayList<WaitingObject>();

    public DataToDatabaseSink(Platform platform, Database model) {
        this._platform = platform;
        this._model = model;
        int tableIdx = 0;
        while (tableIdx < model.getTableCount()) {
            Table table = model.getTable(tableIdx);
            ForeignKey selfRefFk = table.getSelfReferencingForeignKey();
            if (selfRefFk != null) {
                Column[] pkColumns = table.getPrimaryKeyColumns();
                int idx = 0;
                while (idx < pkColumns.length) {
                    if (pkColumns[idx].isAutoIncrement()) {
                        this._tablesWithSelfIdentityReference.add(table);
                        break;
                    }
                    ++idx;
                }
                idx = 0;
                while (idx < selfRefFk.getReferenceCount()) {
                    if (selfRefFk.getReference(idx).getLocalColumn().isRequired()) {
                        this._tablesWithRequiredSelfReference.add(table);
                        break;
                    }
                    ++idx;
                }
            }
            ++tableIdx;
        }
    }

    public boolean isHaltOnErrors() {
        return this._haltOnErrors;
    }

    public void setHaltOnErrors(boolean haltOnErrors) {
        this._haltOnErrors = haltOnErrors;
    }

    public boolean isEnsureFkOrder() {
        return this._ensureFkOrder;
    }

    public void setEnsureForeignKeyOrder(boolean ensureFkOrder) {
        this._ensureFkOrder = ensureFkOrder;
    }

    public boolean isUseBatchMode() {
        return this._useBatchMode;
    }

    public void setUseBatchMode(boolean useBatchMode) {
        this._useBatchMode = useBatchMode;
    }

    public int getBatchSize() {
        return this._batchSize;
    }

    public void setBatchSize(int batchSize) {
        this._batchSize = batchSize;
    }


    public void end() throws DataSinkException {
        this.purgeBatchQueue();
        try {
            this._connection.close();
        }
        catch (SQLException ex) {
            throw new DataSinkException(ex);
        }
        if (!this._waitingObjects.isEmpty()) {
            if (this._log.isDebugEnabled()) {
                for (WaitingObject obj : this._waitingObjects) {
                    Table table = this._model.getDynaClassFor(obj.getObject()).getTable();
                    Identity objId = this.buildIdentityFromPKs(table, obj.getObject());
                    this._log.debug((Object)("Row " + objId + " is still not written because it depends on these yet unwritten rows"));
                    Iterator fkIt = obj.getPendingFKs();
                    while (fkIt.hasNext()) {
                        Identity pendingFkId = (Identity)fkIt.next();
                        this._log.debug((Object)("  " + pendingFkId));
                    }
                }
            }
            if (this._waitingObjects.size() == 1) {
                throw new DataSinkException("There is one row still not written because of missing referenced rows");
            }
            throw new DataSinkException("There are " + this._waitingObjects.size() + " rows still not written because of missing referenced rows");
        }
    }


    public void start() throws DataSinkException {
        this._fkTables.clear();
        this._waitingObjects.clear();
        if (this._ensureFkOrder) {
            int tableIdx = 0;
            while (tableIdx < this._model.getTableCount()) {
                Table table = this._model.getTable(tableIdx);
                int fkIdx = 0;
                while (fkIdx < table.getForeignKeyCount()) {
                    ForeignKey curFk = table.getForeignKey(fkIdx);
                    this._fkTables.add(curFk.getForeignTable());
                    ++fkIdx;
                }
                ++tableIdx;
            }
        }
        try {
            this._connection = this._platform.borrowConnection();
        }
        catch (DatabaseOperationException ex) {
            throw new DataSinkException((Throwable)((Object)ex));
        }
    }


    public void addBean(DynaBean bean) throws DataSinkException {
        Table table = this._model.getDynaClassFor(bean).getTable();
        Identity origIdentity = this.buildIdentityFromPKs(table, bean);
        if (this._ensureFkOrder && table.getForeignKeyCount() > 0) {
            WaitingObject waitingObj = new WaitingObject(bean, origIdentity);
            int idx = 0;
            while (idx < table.getForeignKeyCount()) {
                ForeignKey fk = table.getForeignKey(idx);
                Identity fkIdentity = this.buildIdentityFromFK(table, fk, bean);
                if (fkIdentity != null && !fkIdentity.equals(origIdentity)) {
                    Identity processedIdentity = (Identity)this._identityMap.get(fkIdentity);
                    if (processedIdentity != null) {
                        this.updateFKColumns(bean, fkIdentity.getForeignKeyName(), processedIdentity);
                    } else {
                        waitingObj.addPendingFK(fkIdentity);
                    }
                }
                ++idx;
            }
            if (waitingObj.hasPendingFKs()) {
                if (this._log.isDebugEnabled()) {
                    StringBuffer msg = new StringBuffer();
                    msg.append("Defering insertion of row ");
                    msg.append(this.buildIdentityFromPKs(table, bean).toString());
                    msg.append(" because it is waiting for:");
                    Iterator it = waitingObj.getPendingFKs();
                    while (it.hasNext()) {
                        msg.append("\n  ");
                        msg.append(it.next().toString());
                    }
                    this._log.debug((Object)msg.toString());
                }
                this._waitingObjects.add(waitingObj);
                return;
            }
        }
        this.insertBeanIntoDatabase(table, bean);
        if (this._log.isDebugEnabled()) {
            this._log.debug((Object)("Inserted bean " + origIdentity));
        }
        if (this._ensureFkOrder && this._fkTables.contains(table)) {
            Identity newIdentity = this.buildIdentityFromPKs(table, bean);
            ArrayList<DynaBean> finishedObjs = new ArrayList<DynaBean>();
            this._identityMap.put(origIdentity, newIdentity);
            ArrayList<Identity> identitiesToCheck = new ArrayList<Identity>();
            identitiesToCheck.add(origIdentity);
            while (!identitiesToCheck.isEmpty() && !this._waitingObjects.isEmpty()) {
                Identity curIdentity = (Identity)identitiesToCheck.get(0);
                Identity curNewIdentity = (Identity)this._identityMap.get(curIdentity);
                identitiesToCheck.remove(0);
                finishedObjs.clear();
                Iterator waitingObjIt = this._waitingObjects.iterator();
                while (waitingObjIt.hasNext()) {
                    WaitingObject waitingObj = (WaitingObject)waitingObjIt.next();
                    Identity fkIdentity = waitingObj.removePendingFK(curIdentity);
                    if (fkIdentity != null) {
                        this.updateFKColumns(waitingObj.getObject(), fkIdentity.getForeignKeyName(), curNewIdentity);
                    }
                    if (waitingObj.hasPendingFKs()) continue;
                    waitingObjIt.remove();
                    finishedObjs.add(waitingObj.getObject());
                }
                for (DynaBean finishedObj : finishedObjs) {
                    Table tableForObj = this._model.getDynaClassFor(finishedObj).getTable();
                    Identity objIdentity = this.buildIdentityFromPKs(tableForObj, finishedObj);
                    this.insertBeanIntoDatabase(tableForObj, finishedObj);
                    Identity newObjIdentity = this.buildIdentityFromPKs(tableForObj, finishedObj);
                    this._identityMap.put(objIdentity, newObjIdentity);
                    identitiesToCheck.add(objIdentity);
                    if (!this._log.isDebugEnabled()) continue;
                    this._log.debug((Object)("Inserted deferred row " + objIdentity));
                }
            }
        }
    }

    private void insertBeanIntoDatabase(Table table, DynaBean bean) throws DataSinkException {
        if (this._useBatchMode) {
            this._batchQueue.add(bean);
            if (this._batchQueue.size() >= this._batchSize) {
                this.purgeBatchQueue();
            }
        } else {
            this.insertSingleBeanIntoDatabase(table, bean);
        }
    }

    private void purgeBatchQueue() throws DataSinkException {
        if (!this._batchQueue.isEmpty()) {
            try {
                this._platform.insert(this._connection, this._model, this._batchQueue);
                if (!this._connection.getAutoCommit()) {
                    this._connection.commit();
                }
                if (this._log.isDebugEnabled()) {
                    this._log.debug((Object)("Inserted " + this._batchQueue.size() + " rows in batch mode "));
                }
            }
            catch (Exception ex) {
                if (this._haltOnErrors) {
                    this._platform.returnConnection(this._connection);
                    throw new DataSinkException(ex);
                }
                this._log.warn((Object)("Exception while inserting " + this._batchQueue.size() + " rows via batch mode into the database"), (Throwable)ex);
            }
            this._batchQueue.clear();
        }
    }

    private void insertSingleBeanIntoDatabase(Table table, DynaBean bean) throws DataSinkException {
        try {
            boolean needTwoStepInsert = false;
            ForeignKey selfRefFk = null;
            if (!this._platform.isIdentityOverrideOn() && this._tablesWithSelfIdentityReference.contains(table)) {
                Identity fkIdentity;
                selfRefFk = table.getSelfReferencingForeignKey();
                Identity pkIdentity = this.buildIdentityFromPKs(table, bean);
                if (pkIdentity.equals(fkIdentity = this.buildIdentityFromFK(table, selfRefFk, bean))) {
                    if (this._tablesWithRequiredSelfReference.contains(table)) {
                        throw new DataSinkException("Can only insert rows with fk pointing to themselves when all fk columns can be NULL (row pk is " + pkIdentity + ")");
                    }
                    needTwoStepInsert = true;
                }
            }
            if (needTwoStepInsert) {
                ArrayList<Object> fkValues = new ArrayList<Object>();
                int idx = 0;
                while (idx < selfRefFk.getReferenceCount()) {
                    String columnName = selfRefFk.getReference(idx).getLocalColumnName();
                    fkValues.add(bean.get(columnName));
                    bean.set(columnName, null);
                    ++idx;
                }
                this._platform.insert(this._connection, this._model, bean);
                idx = 0;
                while (idx < selfRefFk.getReferenceCount()) {
                    bean.set(selfRefFk.getReference(idx).getLocalColumnName(), fkValues.get(idx));
                    ++idx;
                }
                this._platform.update(this._connection, this._model, bean);
            } else {
                this._platform.insert(this._connection, this._model, bean);
            }
            if (!this._connection.getAutoCommit()) {
                this._connection.commit();
            }
        }
        catch (Exception ex) {
            if (this._haltOnErrors) {
                this._platform.returnConnection(this._connection);
                throw new DataSinkException(ex);
            }
            this._log.warn((Object)"Exception while inserting a row into the database", (Throwable)ex);
        }

    }

    private String getFKName(Table owningTable, ForeignKey fk) {
        if (fk.getName() != null && fk.getName().length() > 0) {
            return fk.getName();
        }
        StringBuffer result = new StringBuffer();
        result.append(owningTable.getName());
        result.append("[");
        int idx = 0;
        while (idx < fk.getReferenceCount()) {
            if (idx > 0) {
                result.append(",");
            }
            result.append(fk.getReference(idx).getLocalColumnName());
            ++idx;
        }
        result.append("]->");
        result.append(fk.getForeignTableName());
        result.append("[");
        idx = 0;
        while (idx < fk.getReferenceCount()) {
            if (idx > 0) {
                result.append(",");
            }
            result.append(fk.getReference(idx).getForeignColumnName());
            ++idx;
        }
        result.append("]");
        return result.toString();
    }

    private Identity buildIdentityFromPKs(Table table, DynaBean bean) {
        Identity identity = new Identity(table);
        Column[] pkColumns = table.getPrimaryKeyColumns();
        int idx = 0;
        while (idx < pkColumns.length) {
            identity.setColumnValue(pkColumns[idx].getName(), bean.get(pkColumns[idx].getName()));
            ++idx;
        }
        return identity;
    }

    private Identity buildIdentityFromFK(Table owningTable, ForeignKey fk, DynaBean bean) {
        Identity identity = new Identity(fk.getForeignTable(), this.getFKName(owningTable, fk));
        int idx = 0;
        while (idx < fk.getReferenceCount()) {
            Reference reference = fk.getReference(idx);
            Object value = bean.get(reference.getLocalColumnName());
            if (value == null) {
                return null;
            }
            identity.setColumnValue(reference.getForeignColumnName(), value);
            ++idx;
        }
        return identity;
    }

    private void updateFKColumns(DynaBean bean, String fkName, Identity identity) {
        Table sourceTable = ((SqlDynaClass)bean.getDynaClass()).getTable();
        Table targetTable = identity.getTable();
        ForeignKey fk = null;
        int idx = 0;
        while (idx < sourceTable.getForeignKeyCount()) {
            ForeignKey curFk = sourceTable.getForeignKey(idx);
            if (curFk.getForeignTableName().equalsIgnoreCase(targetTable.getName()) && fkName.equals(this.getFKName(sourceTable, curFk))) {
                fk = curFk;
                break;
            }
            ++idx;
        }
        if (fk != null) {
            idx = 0;
            while (idx < fk.getReferenceCount()) {
                Reference curRef = fk.getReference(idx);
                Column sourceColumn = curRef.getLocalColumn();
                Column targetColumn = curRef.getForeignColumn();
                bean.set(sourceColumn.getName(), identity.getColumnValue(targetColumn.getName()));
                ++idx;
            }
        }
    }
}

