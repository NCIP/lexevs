/*
 * Decompiled with CFR 0.152.
 */
package org.apache.ddlutils.util;

public class SqlTokenizer {
    private String _sql;
    private int _lastCharIdx;
    private int _lastDelimiterPos = -1;
    private int _nextDelimiterPos = -1;
    private boolean _finished;

    public SqlTokenizer(String sql) {
        this._sql = sql;
        this._lastCharIdx = sql.length() - 1;
    }

    public boolean hasMoreStatements() {
        if (this._finished) {
            return false;
        }
        if (this._nextDelimiterPos <= this._lastDelimiterPos) {
            this._nextDelimiterPos = this._sql.indexOf(59, this._lastDelimiterPos + 1);
            while (this._nextDelimiterPos >= 0 && this._nextDelimiterPos < this._lastCharIdx) {
                char nextChar = this._sql.charAt(this._nextDelimiterPos + 1);
                if (nextChar == '\r' || nextChar == '\n') break;
                this._nextDelimiterPos = this._sql.indexOf(59, this._nextDelimiterPos + 1);
            }
        }
        return this._nextDelimiterPos >= 0 || this._lastDelimiterPos < this._lastCharIdx;
    }

    public String getNextStatement() {
        String result = null;
        if (this.hasMoreStatements()) {
            if (this._nextDelimiterPos >= 0) {
                result = this._sql.substring(this._lastDelimiterPos + 1, this._nextDelimiterPos);
                this._lastDelimiterPos = this._nextDelimiterPos;
            } else {
                result = this._sql.substring(this._lastDelimiterPos + 1);
                this._finished = true;
            }
        }
        return result;
    }
}

