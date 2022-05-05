/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.logging.Log
 *  org.apache.commons.logging.LogFactory
 */
package org.apache.ddlutils;

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PlatformInfo {
    private final Log _log = LogFactory.getLog(PlatformInfo.class);
    private boolean _nullAsDefaultValueRequired = false;
    private boolean _defaultValuesForLongTypesSupported = true;
    private boolean _primaryKeyEmbedded = true;
    private boolean _foreignKeysEmbedded = false;
    private boolean _embeddedForeignKeysNamed = false;
    private boolean _indicesSupported = true;
    private boolean _indicesEmbedded = false;
    private boolean _nonPKIdentityColumnsSupported = true;
    private boolean _defaultValueUsedForIdentitySpec = false;
    private boolean _systemIndicesReturned = true;
    private boolean _systemForeignKeyIndicesAlwaysNonUnique = false;
    private boolean _syntheticDefaultValueForRequiredReturned = false;
    private boolean _identityStatusReadingSupported = true;
    private boolean _sqlCommentsSupported = true;
    private boolean _delimitedIdentifiersSupported = true;
    private boolean _alterTableForDropUsed = false;
    private boolean _identityOverrideAllowed = true;
    private boolean _lastIdentityValueReadable = true;
    private boolean _autoCommitModeForLastIdentityValueReading = true;
    private int _maxTableNameLength = -1;
    private int _maxColumnNameLength = -1;
    private int _maxConstraintNameLength = -1;
    private int _maxForeignKeyNameLength = -1;
    private String _delimiterToken = "\"";
    private String _valueQuoteToken = "'";
    private String _commentPrefix = "--";
    private String _commentSuffix = "";
    private String _sqlCommandDelimiter = ";";
    private HashMap _nativeTypes = new HashMap();
    private HashMap _targetJdbcTypes = new HashMap();
    private HashSet _typesWithNullDefault = new HashSet();
    private HashSet _typesWithSize = new HashSet();
    private HashMap _typesDefaultSizes = new HashMap();
    private HashSet _typesWithPrecisionAndScale = new HashSet();

    public PlatformInfo() {
        this._typesWithNullDefault.add(new Integer(1));
        this._typesWithNullDefault.add(new Integer(12));
        this._typesWithNullDefault.add(new Integer(-1));
        this._typesWithNullDefault.add(new Integer(2005));
        this._typesWithNullDefault.add(new Integer(-2));
        this._typesWithNullDefault.add(new Integer(-3));
        this._typesWithNullDefault.add(new Integer(-4));
        this._typesWithNullDefault.add(new Integer(2004));
        this._typesWithSize.add(new Integer(1));
        this._typesWithSize.add(new Integer(12));
        this._typesWithSize.add(new Integer(-2));
        this._typesWithSize.add(new Integer(-3));
        this._typesWithPrecisionAndScale.add(new Integer(3));
        this._typesWithPrecisionAndScale.add(new Integer(2));
    }

    public boolean isNullAsDefaultValueRequired() {
        return this._nullAsDefaultValueRequired;
    }

    public void setNullAsDefaultValueRequired(boolean requiresNullAsDefaultValue) {
        this._nullAsDefaultValueRequired = requiresNullAsDefaultValue;
    }

    public boolean isDefaultValuesForLongTypesSupported() {
        return this._defaultValuesForLongTypesSupported;
    }

    public void setDefaultValuesForLongTypesSupported(boolean isSupported) {
        this._defaultValuesForLongTypesSupported = isSupported;
    }

    public boolean isPrimaryKeyEmbedded() {
        return this._primaryKeyEmbedded;
    }

    public void setPrimaryKeyEmbedded(boolean primaryKeyEmbedded) {
        this._primaryKeyEmbedded = primaryKeyEmbedded;
    }

    public boolean isForeignKeysEmbedded() {
        return this._foreignKeysEmbedded;
    }

    public void setForeignKeysEmbedded(boolean foreignKeysEmbedded) {
        this._foreignKeysEmbedded = foreignKeysEmbedded;
    }

    public boolean isEmbeddedForeignKeysNamed() {
        return this._embeddedForeignKeysNamed;
    }

    public void setEmbeddedForeignKeysNamed(boolean embeddedForeignKeysNamed) {
        this._embeddedForeignKeysNamed = embeddedForeignKeysNamed;
    }

    public boolean isIndicesSupported() {
        return this._indicesSupported;
    }

    public void setIndicesSupported(boolean supportingIndices) {
        this._indicesSupported = supportingIndices;
    }

    public boolean isIndicesEmbedded() {
        return this._indicesEmbedded;
    }

    public void setIndicesEmbedded(boolean indicesEmbedded) {
        this._indicesEmbedded = indicesEmbedded;
    }

    public boolean isNonPKIdentityColumnsSupported() {
        return this._nonPKIdentityColumnsSupported;
    }

    public void setNonPKIdentityColumnsSupported(boolean supportingNonPKIdentityColumns) {
        this._nonPKIdentityColumnsSupported = supportingNonPKIdentityColumns;
    }

    public boolean isDefaultValueUsedForIdentitySpec() {
        return this._defaultValueUsedForIdentitySpec;
    }

    public void setDefaultValueUsedForIdentitySpec(boolean identitySpecUsesDefaultValue) {
        this._defaultValueUsedForIdentitySpec = identitySpecUsesDefaultValue;
    }

    public boolean isSystemIndicesReturned() {
        return this._systemIndicesReturned;
    }

    public void setSystemIndicesReturned(boolean returningSystemIndices) {
        this._systemIndicesReturned = returningSystemIndices;
    }

    public boolean isSystemForeignKeyIndicesAlwaysNonUnique() {
        return this._systemForeignKeyIndicesAlwaysNonUnique;
    }

    public void setSystemForeignKeyIndicesAlwaysNonUnique(boolean alwaysNonUnique) {
        this._systemForeignKeyIndicesAlwaysNonUnique = alwaysNonUnique;
    }

    public boolean isSyntheticDefaultValueForRequiredReturned() {
        return this._syntheticDefaultValueForRequiredReturned;
    }

    public void setSyntheticDefaultValueForRequiredReturned(boolean returningDefaultValue) {
        this._syntheticDefaultValueForRequiredReturned = returningDefaultValue;
    }

    public boolean getIdentityStatusReadingSupported() {
        return this._identityStatusReadingSupported;
    }

    public void setIdentityStatusReadingSupported(boolean canReadAutoIncrementStatus) {
        this._identityStatusReadingSupported = canReadAutoIncrementStatus;
    }

    public boolean isSqlCommentsSupported() {
        return this._sqlCommentsSupported;
    }

    public void setSqlCommentsSupported(boolean commentsSupported) {
        this._sqlCommentsSupported = commentsSupported;
    }

    public boolean isDelimitedIdentifiersSupported() {
        return this._delimitedIdentifiersSupported;
    }

    public void setDelimitedIdentifiersSupported(boolean areSupported) {
        this._delimitedIdentifiersSupported = areSupported;
    }

    public boolean isAlterTableForDropUsed() {
        return this._alterTableForDropUsed;
    }

    public void setAlterTableForDropUsed(boolean useAlterTableForDrop) {
        this._alterTableForDropUsed = useAlterTableForDrop;
    }

    public boolean isIdentityOverrideAllowed() {
        return this._identityOverrideAllowed;
    }

    public void setIdentityOverrideAllowed(boolean identityOverrideAllowed) {
        this._identityOverrideAllowed = identityOverrideAllowed;
    }

    public boolean isLastIdentityValueReadable() {
        return this._lastIdentityValueReadable;
    }

    public void setLastIdentityValueReadable(boolean lastIdentityValueReadable) {
        this._lastIdentityValueReadable = lastIdentityValueReadable;
    }

    public boolean isAutoCommitModeForLastIdentityValueReading() {
        return this._autoCommitModeForLastIdentityValueReading;
    }

    public void setAutoCommitModeForLastIdentityValueReading(boolean autoCommitModeForLastIdentityValueReading) {
        this._autoCommitModeForLastIdentityValueReading = autoCommitModeForLastIdentityValueReading;
    }

    public int getMaxTableNameLength() {
        return this._maxTableNameLength;
    }

    public int getMaxColumnNameLength() {
        return this._maxColumnNameLength;
    }

    public int getMaxConstraintNameLength() {
        return this._maxConstraintNameLength;
    }

    public int getMaxForeignKeyNameLength() {
        return this._maxForeignKeyNameLength;
    }

    public void setMaxIdentifierLength(int maxIdentifierLength) {
        this._maxTableNameLength = maxIdentifierLength;
        this._maxColumnNameLength = maxIdentifierLength;
        this._maxConstraintNameLength = maxIdentifierLength;
        this._maxForeignKeyNameLength = maxIdentifierLength;
    }

    public void setMaxTableNameLength(int maxTableNameLength) {
        this._maxTableNameLength = maxTableNameLength;
    }

    public void setMaxColumnNameLength(int maxColumnNameLength) {
        this._maxColumnNameLength = maxColumnNameLength;
    }

    public void setMaxConstraintNameLength(int maxConstraintNameLength) {
        this._maxConstraintNameLength = maxConstraintNameLength;
    }

    public void setMaxForeignKeyNameLength(int maxForeignKeyNameLength) {
        this._maxForeignKeyNameLength = maxForeignKeyNameLength;
    }

    public String getDelimiterToken() {
        return this._delimiterToken;
    }

    public void setDelimiterToken(String delimiterToken) {
        this._delimiterToken = delimiterToken;
    }

    public String getValueQuoteToken() {
        return this._valueQuoteToken;
    }

    public void setValueQuoteToken(String valueQuoteChar) {
        this._valueQuoteToken = valueQuoteChar;
    }

    public String getCommentPrefix() {
        return this._commentPrefix;
    }

    public void setCommentPrefix(String commentPrefix) {
        this._commentPrefix = commentPrefix == null ? "" : commentPrefix;
    }

    public String getCommentSuffix() {
        return this._commentSuffix;
    }

    public void setCommentSuffix(String commentSuffix) {
        this._commentSuffix = commentSuffix == null ? "" : commentSuffix;
    }

    public String getSqlCommandDelimiter() {
        return this._sqlCommandDelimiter;
    }

    public void setSqlCommandDelimiter(String sqlCommandDelimiter) {
        this._sqlCommandDelimiter = sqlCommandDelimiter;
    }

    public String getNativeType(int typeCode) {
        return (String)this._nativeTypes.get(new Integer(typeCode));
    }

    public int getTargetJdbcType(int typeCode) {
        Integer targetJdbcType = (Integer)this._targetJdbcTypes.get(new Integer(typeCode));
        return targetJdbcType == null ? typeCode : targetJdbcType;
    }

    public void addNativeTypeMapping(int jdbcTypeCode, String nativeType) {
        this._nativeTypes.put(new Integer(jdbcTypeCode), nativeType);
    }

    public void addNativeTypeMapping(int jdbcTypeCode, String nativeType, int targetJdbcTypeCode) {
        this.addNativeTypeMapping(jdbcTypeCode, nativeType);
        this._targetJdbcTypes.put(new Integer(jdbcTypeCode), new Integer(targetJdbcTypeCode));
    }

    public void addNativeTypeMapping(String jdbcTypeName, String nativeType) {
        try {
            Field constant = Types.class.getField(jdbcTypeName);
            if (constant != null) {
                this.addNativeTypeMapping(constant.getInt(null), nativeType);
            }
        }
        catch (Exception ex) {
            this._log.warn((Object)("Cannot add native type mapping for undefined jdbc type " + jdbcTypeName), (Throwable)ex);
        }
    }

    public void addNativeTypeMapping(String jdbcTypeName, String nativeType, String targetJdbcTypeName) {
        try {
            Field sourceType = Types.class.getField(jdbcTypeName);
            Field targetType = Types.class.getField(targetJdbcTypeName);
            if (sourceType != null && targetType != null) {
                this.addNativeTypeMapping(sourceType.getInt(null), nativeType, targetType.getInt(null));
            }
        }
        catch (Exception ex) {
            this._log.warn((Object)("Cannot add native type mapping for undefined jdbc type " + jdbcTypeName + ", target jdbc type " + targetJdbcTypeName), (Throwable)ex);
        }
    }

    public boolean hasNullDefault(int sqlTypeCode) {
        return this._typesWithNullDefault.contains(new Integer(sqlTypeCode));
    }

    public void setHasNullDefault(int sqlTypeCode, boolean hasNullDefault) {
        if (hasNullDefault) {
            this._typesWithNullDefault.add(new Integer(sqlTypeCode));
        } else {
            this._typesWithNullDefault.remove(new Integer(sqlTypeCode));
        }
    }

    public boolean hasSize(int sqlTypeCode) {
        return this._typesWithSize.contains(new Integer(sqlTypeCode));
    }

    public void setHasSize(int sqlTypeCode, boolean hasSize) {
        if (hasSize) {
            this._typesWithSize.add(new Integer(sqlTypeCode));
        } else {
            this._typesWithSize.remove(new Integer(sqlTypeCode));
        }
    }

    public Integer getDefaultSize(int jdbcTypeCode) {
        return (Integer)this._typesDefaultSizes.get(new Integer(jdbcTypeCode));
    }

    public void setDefaultSize(int jdbcTypeCode, int defaultSize) {
        this._typesDefaultSizes.put(new Integer(jdbcTypeCode), new Integer(defaultSize));
    }

    public void setDefaultSize(String jdbcTypeName, int defaultSize) {
        try {
            Field constant = Types.class.getField(jdbcTypeName);
            if (constant != null) {
                this.setDefaultSize(constant.getInt(null), defaultSize);
            }
        }
        catch (Exception ex) {
            this._log.warn((Object)("Cannot add default size for undefined jdbc type " + jdbcTypeName), (Throwable)ex);
        }
    }

    public boolean hasPrecisionAndScale(int sqlTypeCode) {
        return this._typesWithPrecisionAndScale.contains(new Integer(sqlTypeCode));
    }

    public void setHasPrecisionAndScale(int sqlTypeCode, boolean hasPrecisionAndScale) {
        if (hasPrecisionAndScale) {
            this._typesWithPrecisionAndScale.add(new Integer(sqlTypeCode));
        } else {
            this._typesWithPrecisionAndScale.remove(new Integer(sqlTypeCode));
        }
    }
}

