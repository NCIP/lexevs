/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.collections.Closure
 */
package org.apache.ddlutils.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.apache.commons.collections.Closure;
import org.apache.ddlutils.DdlUtilsException;

public class CallbackClosure
implements Closure {
    private Object _callee;
    private Class[] _parameterTypes;
    private Object[] _parameters;
    private int _callbackTypePos = -1;
    private Map _callbacks = new HashMap();

    public CallbackClosure(Object callee, String callbackName, Class[] parameterTypes, Object[] parameters) {
        this._callee = callee;
        if (parameterTypes == null || parameterTypes.length == 0) {
            this._parameterTypes = new Class[1];
            this._parameters = new Object[1];
            this._callbackTypePos = 0;
        } else {
            this._parameterTypes = new Class[parameterTypes.length];
            this._parameters = new Object[parameterTypes.length];
            int idx = 0;
            while (idx < parameterTypes.length) {
                if (parameterTypes[idx] == null) {
                    if (this._callbackTypePos >= 0) {
                        throw new IllegalArgumentException("The parameter types may contain null only once");
                    }
                    this._callbackTypePos = idx;
                } else {
                    this._parameterTypes[idx] = parameterTypes[idx];
                    this._parameters[idx] = parameters[idx];
                }
                ++idx;
            }
            if (this._callbackTypePos < 0) {
                throw new IllegalArgumentException("The parameter types need to a null placeholder");
            }
        }
        Class<?> type = callee.getClass();
        do {
            Method[] methods;
            if ((methods = type.getDeclaredMethods()) == null) continue;
            int idx = 0;
            while (idx < methods.length) {
                Method method = methods[idx];
                Class[] paramTypes = methods[idx].getParameterTypes();
                method.setAccessible(true);
                if (method.getName().equals(callbackName) && this.typesMatch(paramTypes) && this._callbacks.get(paramTypes[this._callbackTypePos]) == null) {
                    this._callbacks.put(paramTypes[this._callbackTypePos], methods[idx]);
                }
                ++idx;
            }
        } while ((type = type.getSuperclass()) != null && !type.equals(Object.class));
    }

    private boolean typesMatch(Class[] methodParamTypes) {
        if (methodParamTypes == null || this._parameterTypes.length != methodParamTypes.length) {
            return false;
        }
        int idx = 0;
        while (idx < this._parameterTypes.length) {
            if (idx != this._callbackTypePos && !this._parameterTypes[idx].equals(methodParamTypes[idx])) {
                return false;
            }
            ++idx;
        }
        return true;
    }

    public void execute(Object obj) throws DdlUtilsException {
        LinkedList queue = new LinkedList();
        queue.add(obj.getClass());
        while (!queue.isEmpty()) {
            Class<?>[] baseInterfaces;
            Class type = (Class)queue.removeFirst();
            Method callback = (Method)this._callbacks.get(type);
            if (callback != null) {
                try {
                    this._parameters[this._callbackTypePos] = obj;
                    callback.invoke(this._callee, this._parameters);
                    return;
                }
                catch (InvocationTargetException ex) {
                    throw new DdlUtilsException(ex.getTargetException());
                }
                catch (IllegalAccessException ex) {
                    throw new DdlUtilsException(ex);
                }
            }
            if (type.getSuperclass() != null && !type.getSuperclass().equals(Object.class)) {
                queue.add(type.getSuperclass());
            }
            if ((baseInterfaces = type.getInterfaces()) == null) continue;
            int idx = 0;
            while (idx < baseInterfaces.length) {
                queue.add(baseInterfaces[idx]);
                ++idx;
            }
        }
    }
}

