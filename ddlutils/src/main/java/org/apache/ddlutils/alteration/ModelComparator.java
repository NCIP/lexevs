/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 *  org.apache.commons.logging.Log
 *  org.apache.commons.logging.LogFactory
 */
package org.apache.ddlutils.alteration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.alteration.AddColumnChange;
import org.apache.ddlutils.alteration.AddForeignKeyChange;
import org.apache.ddlutils.alteration.AddIndexChange;
import org.apache.ddlutils.alteration.AddPrimaryKeyChange;
import org.apache.ddlutils.alteration.AddTableChange;
import org.apache.ddlutils.alteration.ColumnAutoIncrementChange;
import org.apache.ddlutils.alteration.ColumnDataTypeChange;
import org.apache.ddlutils.alteration.ColumnDefaultValueChange;
import org.apache.ddlutils.alteration.ColumnOrderChange;
import org.apache.ddlutils.alteration.ColumnRequiredChange;
import org.apache.ddlutils.alteration.ColumnSizeChange;
import org.apache.ddlutils.alteration.ModelChange;
import org.apache.ddlutils.alteration.PrimaryKeyChange;
import org.apache.ddlutils.alteration.RemoveColumnChange;
import org.apache.ddlutils.alteration.RemoveForeignKeyChange;
import org.apache.ddlutils.alteration.RemoveIndexChange;
import org.apache.ddlutils.alteration.RemovePrimaryKeyChange;
import org.apache.ddlutils.alteration.RemoveTableChange;
import org.apache.ddlutils.alteration.TableChangeImplBase;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.ForeignKey;
import org.apache.ddlutils.model.Index;
import org.apache.ddlutils.model.Table;

public class ModelComparator {
    private final Log _log = LogFactory.getLog(ModelComparator.class);
    private PlatformInfo _platformInfo;
    private boolean _caseSensitive;

    public ModelComparator(PlatformInfo platformInfo, boolean caseSensitive) {
        this._platformInfo = platformInfo;
        this._caseSensitive = caseSensitive;
    }

    public List compare(Database sourceModel, Database targetModel) {
        int fkIdx;
        ArrayList<ModelChange> changes = new ArrayList<ModelChange>();
        int tableIdx = 0;
        while (tableIdx < targetModel.getTableCount()) {
            Table targetTable = targetModel.getTable(tableIdx);
            Table sourceTable = sourceModel.findTable(targetTable.getName(), this._caseSensitive);
            if (sourceTable == null) {
                if (this._log.isInfoEnabled()) {
                    this._log.info((Object)("Table " + targetTable.getName() + " needs to be added"));
                }
                changes.add(new AddTableChange(targetTable));
                fkIdx = 0;
                while (fkIdx < targetTable.getForeignKeyCount()) {
                    changes.add(new AddForeignKeyChange(targetTable, targetTable.getForeignKey(fkIdx)));
                    ++fkIdx;
                }
            } else {
                changes.addAll(this.compareTables(sourceModel, sourceTable, targetModel, targetTable));
            }
            ++tableIdx;
        }
        tableIdx = 0;
        while (tableIdx < sourceModel.getTableCount()) {
            Table sourceTable = sourceModel.getTable(tableIdx);
            Table targetTable = targetModel.findTable(sourceTable.getName(), this._caseSensitive);
            if (targetTable == null && sourceTable.getName() != null && sourceTable.getName().length() > 0) {
                if (this._log.isInfoEnabled()) {
                    this._log.info((Object)("Table " + sourceTable.getName() + " needs to be removed"));
                }
                changes.add(new RemoveTableChange(sourceTable));
                fkIdx = 0;
                while (fkIdx < sourceTable.getForeignKeyCount()) {
                    changes.add(new RemoveForeignKeyChange(sourceTable, sourceTable.getForeignKey(fkIdx)));
                    ++fkIdx;
                }
            }
            ++tableIdx;
        }
        return changes;
    }

    public List compareTables(Database sourceModel, Table sourceTable, Database targetModel, Table targetTable) {
        Column targetColumn;
        ArrayList<TableChangeImplBase> changes = new ArrayList<TableChangeImplBase>();
        int fkIdx = 0;
        while (fkIdx < sourceTable.getForeignKeyCount()) {
            ForeignKey sourceFk = sourceTable.getForeignKey(fkIdx);
            ForeignKey targetFk = this.findCorrespondingForeignKey(targetTable, sourceFk);
            if (targetFk == null) {
                if (this._log.isInfoEnabled()) {
                    this._log.info((Object)("Foreign key " + sourceFk + " needs to be removed from table " + sourceTable.getName()));
                }
                changes.add(new RemoveForeignKeyChange(sourceTable, sourceFk));
            }
            ++fkIdx;
        }
        fkIdx = 0;
        while (fkIdx < targetTable.getForeignKeyCount()) {
            ForeignKey targetFk = targetTable.getForeignKey(fkIdx);
            ForeignKey sourceFk = this.findCorrespondingForeignKey(sourceTable, targetFk);
            if (sourceFk == null) {
                if (this._log.isInfoEnabled()) {
                    this._log.info((Object)("Foreign key " + targetFk + " needs to be created for table " + sourceTable.getName()));
                }
                changes.add(new AddForeignKeyChange(targetTable, targetFk));
            }
            ++fkIdx;
        }
        int indexIdx = 0;
        while (indexIdx < sourceTable.getIndexCount()) {
            Index sourceIndex = sourceTable.getIndex(indexIdx);
            Index targetIndex = this.findCorrespondingIndex(targetTable, sourceIndex);
            if (targetIndex == null) {
                if (this._log.isInfoEnabled()) {
                    this._log.info((Object)("Index " + sourceIndex.getName() + " needs to be removed from table " + sourceTable.getName()));
                }
                changes.add(new RemoveIndexChange(sourceTable, sourceIndex));
            }
            ++indexIdx;
        }
        indexIdx = 0;
        while (indexIdx < targetTable.getIndexCount()) {
            Index targetIndex = targetTable.getIndex(indexIdx);
            Index sourceIndex = this.findCorrespondingIndex(sourceTable, targetIndex);
            if (sourceIndex == null) {
                if (this._log.isInfoEnabled()) {
                    this._log.info((Object)("Index " + targetIndex.getName() + " needs to be created for table " + sourceTable.getName()));
                }
                changes.add(new AddIndexChange(targetTable, targetIndex));
            }
            ++indexIdx;
        }
        HashMap<Column, AddColumnChange> addColumnChanges = new HashMap<Column, AddColumnChange>();
        int columnIdx = 0;
        while (columnIdx < targetTable.getColumnCount()) {
            targetColumn = targetTable.getColumn(columnIdx);
            Column sourceColumn = sourceTable.findColumn(targetColumn.getName(), this._caseSensitive);
            if (sourceColumn == null) {
                if (this._log.isInfoEnabled()) {
                    this._log.info((Object)("Column " + targetColumn.getName() + " needs to be created for table " + sourceTable.getName()));
                }
                AddColumnChange change = new AddColumnChange(sourceTable, targetColumn, columnIdx > 0 ? targetTable.getColumn(columnIdx - 1) : null, columnIdx < targetTable.getColumnCount() - 1 ? targetTable.getColumn(columnIdx + 1) : null);
                changes.add(change);
                addColumnChanges.put(targetColumn, change);
            } else {
                changes.addAll(this.compareColumns(sourceTable, sourceColumn, targetTable, targetColumn));
            }
            ++columnIdx;
        }
        columnIdx = targetTable.getColumnCount() - 1;
        while (columnIdx >= 0) {
            targetColumn = targetTable.getColumn(columnIdx);
            AddColumnChange change = (AddColumnChange)addColumnChanges.get(targetColumn);
            if (change == null) break;
            change.setAtEnd(true);
            --columnIdx;
        }
        Column[] sourcePK = sourceTable.getPrimaryKeyColumns();
        Column[] targetPK = targetTable.getPrimaryKeyColumns();
        if (sourcePK.length == 0 && targetPK.length > 0) {
            if (this._log.isInfoEnabled()) {
                this._log.info((Object)("A primary key needs to be added to the table " + sourceTable.getName()));
            }
            changes.add(new AddPrimaryKeyChange(targetTable, targetPK));
        } else if (targetPK.length == 0 && sourcePK.length > 0) {
            if (this._log.isInfoEnabled()) {
                this._log.info((Object)("The primary key needs to be removed from the table " + sourceTable.getName()));
            }
            changes.add(new RemovePrimaryKeyChange(sourceTable, sourcePK));
        } else if (sourcePK.length > 0 && targetPK.length > 0) {
            boolean changePK = false;
            if (sourcePK.length != targetPK.length) {
                changePK = true;
            } else {
                int pkColumnIdx = 0;
                while (pkColumnIdx < sourcePK.length && !changePK) {
                    if (this._caseSensitive && !sourcePK[pkColumnIdx].getName().equals(targetPK[pkColumnIdx].getName()) || !this._caseSensitive && !sourcePK[pkColumnIdx].getName().equalsIgnoreCase(targetPK[pkColumnIdx].getName())) {
                        changePK = true;
                    }
                    ++pkColumnIdx;
                }
            }
            if (changePK) {
                if (this._log.isInfoEnabled()) {
                    this._log.info((Object)("The primary key of table " + sourceTable.getName() + " needs to be changed"));
                }
                changes.add(new PrimaryKeyChange(sourceTable, sourcePK, targetPK));
            }
        }
        HashMap<Column, Integer> columnPosChanges = new HashMap<Column, Integer>();
        int columnIdx2 = 0;
        while (columnIdx2 < sourceTable.getColumnCount()) {
            Column sourceColumn = sourceTable.getColumn(columnIdx2);
            Column targetColumn2 = targetTable.findColumn(sourceColumn.getName(), this._caseSensitive);
            if (targetColumn2 == null) {
                if (this._log.isInfoEnabled()) {
                    this._log.info((Object)("Column " + sourceColumn.getName() + " needs to be removed from table " + sourceTable.getName()));
                }
                changes.add(new RemoveColumnChange(sourceTable, sourceColumn));
            } else {
                int targetColumnIdx = targetTable.getColumnIndex(targetColumn2);
                if (targetColumnIdx != columnIdx2) {
                    columnPosChanges.put(sourceColumn, new Integer(targetColumnIdx));
                }
            }
            ++columnIdx2;
        }
        if (!columnPosChanges.isEmpty()) {
            changes.add(new ColumnOrderChange(sourceTable, columnPosChanges));
        }
        return changes;
    }

    public List compareColumns(Table sourceTable, Column sourceColumn, Table targetTable, Column targetColumn) {
        ArrayList<TableChangeImplBase> changes = new ArrayList<TableChangeImplBase>();
        if (this._platformInfo.getTargetJdbcType(targetColumn.getTypeCode()) != sourceColumn.getTypeCode()) {
            changes.add(new ColumnDataTypeChange(sourceTable, sourceColumn, targetColumn.getTypeCode()));
        }
        boolean sizeMatters = this._platformInfo.hasSize(sourceColumn.getTypeCode());
        boolean scaleMatters = this._platformInfo.hasPrecisionAndScale(sourceColumn.getTypeCode());
        if (sizeMatters && !StringUtils.equals((String)sourceColumn.getSize(), (String)targetColumn.getSize())) {
            changes.add(new ColumnSizeChange(sourceTable, sourceColumn, targetColumn.getSizeAsInt(), targetColumn.getScale()));
        } else if (scaleMatters && (!StringUtils.equals((String)sourceColumn.getSize(), (String)targetColumn.getSize()) || sourceColumn.getScale() != targetColumn.getScale())) {
            changes.add(new ColumnSizeChange(sourceTable, sourceColumn, targetColumn.getSizeAsInt(), targetColumn.getScale()));
        }
        Object sourceDefaultValue = sourceColumn.getParsedDefaultValue();
        Object targetDefaultValue = targetColumn.getParsedDefaultValue();
        if (sourceDefaultValue == null && targetDefaultValue != null || sourceDefaultValue != null && !sourceDefaultValue.equals(targetDefaultValue)) {
            changes.add(new ColumnDefaultValueChange(sourceTable, sourceColumn, targetColumn.getDefaultValue()));
        }
        if (sourceColumn.isRequired() != targetColumn.isRequired()) {
            changes.add(new ColumnRequiredChange(sourceTable, sourceColumn));
        }
        if (sourceColumn.isAutoIncrement() != targetColumn.isAutoIncrement()) {
            changes.add(new ColumnAutoIncrementChange(sourceTable, sourceColumn));
        }
        return changes;
    }

    private ForeignKey findCorrespondingForeignKey(Table table, ForeignKey fk) {
        int fkIdx = 0;
        while (fkIdx < table.getForeignKeyCount()) {
            ForeignKey curFk = table.getForeignKey(fkIdx);
            if (this._caseSensitive && fk.equals(curFk) || !this._caseSensitive && fk.equalsIgnoreCase(curFk)) {
                return curFk;
            }
            ++fkIdx;
        }
        return null;
    }

    private Index findCorrespondingIndex(Table table, Index index) {
        int indexIdx = 0;
        while (indexIdx < table.getIndexCount()) {
            Index curIndex = table.getIndex(indexIdx);
            if (this._caseSensitive && index.equals(curIndex) || !this._caseSensitive && index.equalsIgnoreCase(curIndex)) {
                return curIndex;
            }
            ++indexIdx;
        }
        return null;
    }
}

