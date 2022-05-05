/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.beanutils.ConvertUtils
 *  org.apache.commons.lang.builder.EqualsBuilder
 *  org.apache.commons.lang.builder.HashCodeBuilder
 */
package org.apache.ddlutils.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.ddlutils.model.ModelException;
import org.apache.ddlutils.model.TypeMap;
import org.apache.ddlutils.util.Jdbc3Utils;

public class Column
implements Cloneable,
Serializable {
    private static final long serialVersionUID = -6226348998874210093L;
    private String _name;
    private String _javaName;
    private String _description;
    private boolean _primaryKey;
    private boolean _required;
    private boolean _autoIncrement;
    private int _typeCode;
    private String _type;
    private String _size;
    private Integer _sizeAsInt;
    private int _scale;
    private String _defaultValue;

    public String getName() {
        return this._name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getJavaName() {
        return this._javaName;
    }

    public void setJavaName(String javaName) {
        this._javaName = javaName;
    }

    public String getDescription() {
        return this._description;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    public boolean isPrimaryKey() {
        return this._primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this._primaryKey = primaryKey;
    }

    public boolean isRequired() {
        return this._required;
    }

    public void setRequired(boolean required) {
        this._required = required;
    }

    public boolean isAutoIncrement() {
        return this._autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this._autoIncrement = autoIncrement;
    }

    public int getTypeCode() {
        return this._typeCode;
    }

    public void setTypeCode(int typeCode) {
        this._type = TypeMap.getJdbcTypeName(typeCode);
        if (this._type == null) {
            throw new ModelException("Unknown JDBC type code " + typeCode);
        }
        this._typeCode = typeCode;
    }

    public String getType() {
        return this._type;
    }

    public void setType(String type) {
        Integer typeCode = TypeMap.getJdbcTypeCode(type);
        if (typeCode == null) {
            throw new ModelException("Unknown JDBC type " + type);
        }
        this._typeCode = typeCode;
        this._type = TypeMap.getJdbcTypeName(this._typeCode);
    }

    public boolean isOfNumericType() {
        return TypeMap.isNumericType(this.getTypeCode());
    }

    public boolean isOfTextType() {
        return TypeMap.isTextType(this.getTypeCode());
    }

    public boolean isOfBinaryType() {
        return TypeMap.isBinaryType(this.getTypeCode());
    }

    public boolean isOfSpecialType() {
        return TypeMap.isSpecialType(this.getTypeCode());
    }

    public String getSize() {
        return this._size;
    }

    public int getSizeAsInt() {
        return this._sizeAsInt == null ? 0 : this._sizeAsInt;
    }

    public void setSize(String size) {
        if (size != null) {
            int pos = size.indexOf(",");
            this._size = size;
            if (pos < 0) {
                this._scale = 0;
                this._sizeAsInt = new Integer(this._size);
            } else {
                this._sizeAsInt = new Integer(size.substring(0, pos));
                this._scale = Integer.parseInt(size.substring(pos + 1));
            }
        } else {
            this._size = null;
            this._sizeAsInt = null;
            this._scale = 0;
        }
    }

    public int getScale() {
        return this._scale;
    }

    public void setScale(int scale) {
        this.setSizeAndScale(this.getSizeAsInt(), scale);
    }

    public void setSizeAndScale(int size, int scale) {
        this._sizeAsInt = new Integer(size);
        this._scale = scale;
        this._size = String.valueOf(size);
        if (scale > 0) {
            this._size = String.valueOf(this._size) + "," + this._scale;
        }
    }

    public int getPrecisionRadix() {
        return this.getSizeAsInt();
    }

    public void setPrecisionRadix(int precisionRadix) {
        this._sizeAsInt = new Integer(precisionRadix);
        this._size = String.valueOf(precisionRadix);
    }

    public String getDefaultValue() {
        return this._defaultValue;
    }

    public Object getParsedDefaultValue() {
        if (this._defaultValue != null && this._defaultValue.length() > 0) {
            try {
                switch (this._typeCode) {
                    case -6: 
                    case 5: {
                        return new Short(this._defaultValue);
                    }
                    case 4: {
                        return new Integer(this._defaultValue);
                    }
                    case -5: {
                        return new Long(this._defaultValue);
                    }
                    case 2: 
                    case 3: {
                        return new BigDecimal(this._defaultValue);
                    }
                    case 7: {
                        return new Float(this._defaultValue);
                    }
                    case 6: 
                    case 8: {
                        return new Double(this._defaultValue);
                    }
                    case 91: {
                        return Date.valueOf(this._defaultValue);
                    }
                    case 92: {
                        return Time.valueOf(this._defaultValue);
                    }
                    case 93: {
                        return Timestamp.valueOf(this._defaultValue);
                    }
                    case -7: {
                        return ConvertUtils.convert((String)this._defaultValue, Boolean.class);
                    }
                }
                if (Jdbc3Utils.supportsJava14JdbcTypes() && this._typeCode == Jdbc3Utils.determineBooleanTypeCode()) {
                    return ConvertUtils.convert((String)this._defaultValue, Boolean.class);
                }
            }
            catch (NumberFormatException ex) {
                return null;
            }
            catch (IllegalArgumentException ex) {
                return null;
            }
        }
        return this._defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this._defaultValue = defaultValue;
    }

    public Object clone() throws CloneNotSupportedException {
        Column result = (Column)super.clone();
        result._name = this._name;
        result._javaName = this._javaName;
        result._primaryKey = this._primaryKey;
        result._required = this._required;
        result._autoIncrement = this._autoIncrement;
        result._typeCode = this._typeCode;
        result._type = this._type;
        result._size = this._size;
        result._defaultValue = this._defaultValue;
        result._scale = this._scale;
        result._size = this._size;
        result._sizeAsInt = this._sizeAsInt;
        return result;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Column) {
            Column other = (Column)obj;
            EqualsBuilder comparator = new EqualsBuilder();
            comparator.append((Object)this._name, (Object)other._name);
            comparator.append(this._primaryKey, other._primaryKey);
            comparator.append(this._required, other._required);
            comparator.append(this._autoIncrement, other._autoIncrement);
            comparator.append(this._typeCode, other._typeCode);
            comparator.append(this.getParsedDefaultValue(), other.getParsedDefaultValue());
            if (this._typeCode == 2 || this._typeCode == 3) {
                comparator.append((Object)this._size, (Object)other._size);
                comparator.append(this._scale, other._scale);
            } else if (this._typeCode == 1 || this._typeCode == 12 || this._typeCode == -2 || this._typeCode == -3) {
                comparator.append((Object)this._size, (Object)other._size);
            }
            return comparator.isEquals();
        }
        return false;
    }

    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 37);
        builder.append((Object)this._name);
        builder.append(this._primaryKey);
        builder.append(this._required);
        builder.append(this._autoIncrement);
        builder.append(this._typeCode);
        builder.append((Object)this._type);
        builder.append(this._scale);
        builder.append(this.getParsedDefaultValue());
        if (!TypeMap.isNumericType(this._typeCode)) {
            builder.append((Object)this._size);
        }
        return builder.toHashCode();
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        result.append("Column [name=");
        result.append(this.getName());
        result.append("; type=");
        result.append(this.getType());
        result.append("]");
        return result.toString();
    }

    public String toVerboseString() {
        StringBuffer result = new StringBuffer();
        result.append("Column [name=");
        result.append(this.getName());
        result.append("; javaName=");
        result.append(this.getJavaName());
        result.append("; type=");
        result.append(this.getType());
        result.append("; typeCode=");
        result.append(this.getTypeCode());
        result.append("; size=");
        result.append(this.getSize());
        result.append("; required=");
        result.append(this.isRequired());
        result.append("; primaryKey=");
        result.append(this.isPrimaryKey());
        result.append("; autoIncrement=");
        result.append(this.isAutoIncrement());
        result.append("; defaultValue=");
        result.append(this.getDefaultValue());
        result.append("; precisionRadix=");
        result.append(this.getPrecisionRadix());
        result.append("; scale=");
        result.append(this.getScale());
        result.append("]");
        return result.toString();
    }
}

