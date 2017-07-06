package org.lexevs.dao.database.service.valuesets;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class LexEVSTreeItem {
	String _auis;
	String _code;
	boolean _expandable = false;
	String _id;
	String _ns;
	String _text;


    public Map<String, List<LexEVSTreeItem>> _assocToChildMap =
            new TreeMap<String, List<LexEVSTreeItem>>();

        public boolean equals(Object o) {
    		if (o == null) return false;
    		if (!(o instanceof LexEVSTreeItem)) return false;
    		LexEVSTreeItem item = (LexEVSTreeItem) o;
    		if (_ns == null) {
    			return _text.compareTo(item._text) == 0 && _code.compareTo(item._code) == 0;
    		} else {
    			if (item._ns == null) return false;
    			return _text.compareTo(item._text) == 0 && _code.compareTo(item._code) == 0 && _ns.compareTo(item._ns) == 0;
    		}
        }

        public int compareTo(LexEVSTreeItem ti) {
            String c1 = _code;
            String c2 = ti._code;
            if (c1.startsWith("@"))
                return 1;
            if (c2.startsWith("@"))
                return -1;
            int i = c1.compareTo(c2);
            return i != 0 ? i : _text.compareTo(ti._text);
        }

        public LexEVSTreeItem(String code, String text) {
            super();
            _code = code;
            _text = text;
            _auis = null;
            _ns = null;
        }

        public LexEVSTreeItem(String code, String text, String auiText) {
            super();
            _code = code;
            _text = text;
            _auis = auiText;
            _ns = null;
        }

        public LexEVSTreeItem(String code, String text, String ns, String auiText) {
            super();
            _code = code;
            _ns = ns;
            _text = text;
            _auis = auiText;
        }

        public LexEVSTreeItem(String code, String text, String ns, String id, String auiText) {
            super();
            _code = code;
            _ns = ns;
            _text = text;
            _id = id;
            _auis = auiText;
        }

        public void setNs(String ns) {
    		_ns = ns;
    	}

        public void setId(String id) {
    		_id = id;
    	}

        public void addAll(String assocText, List<LexEVSTreeItem> children) {
            for (LexEVSTreeItem item : children)
                addChild(assocText, item);
        }

        public void addChild(String assocText, LexEVSTreeItem child) {
            List<LexEVSTreeItem> children = _assocToChildMap.get(assocText);
            if (children == null) {
                children = new ArrayList<LexEVSTreeItem>();
                _assocToChildMap.put(assocText, children);
            }
            int i;
            if ((i = children.indexOf(child)) >= 0) {
                LexEVSTreeItem existingLexEVSTreeItem = children.get(i);
                for (String assoc : child._assocToChildMap.keySet()) {
                    List<LexEVSTreeItem> toAdd = child._assocToChildMap.get(assoc);
                    if (!toAdd.isEmpty()) {
                        existingLexEVSTreeItem.addAll(assoc, toAdd);
                        existingLexEVSTreeItem._expandable = false;
                    }
                }
            } else
                children.add(child);
        }

        public int hashCode() {
    		int hashcode = 0;
    		if (_code != null) hashcode = hashcode + _code.hashCode();
    		if (_ns != null && _ns.compareTo("na") != 0) hashcode = hashcode + _ns.hashCode();
    		if (_id != null) hashcode = hashcode + _id.hashCode();
    		if (_text != null) hashcode = hashcode + _text.hashCode();
    		if (_auis != null) hashcode = hashcode + _auis.hashCode();
    		if (_expandable) {
    			hashcode = hashcode + "expandable".hashCode();
    		}
    	    return hashcode;
    	}

        public String toString() {
        	String s = _text;
        	if (_code != null && _code.length() > 0)
        		s += " (" + _code + ")";

        	if (_ns != null && _ns.length() > 0 && _ns.compareTo("na") != 0)
        		s += " (" + _ns + ")";

        	return s;
        }

        public static void printTree(LexEVSTreeItem ti, int depth) {
    		if (ti == null) return;

            StringBuffer indent = new StringBuffer();
            for (int i = 0; i < depth * 2; i++) {
                //indent.append("| ");
                indent.append("\t");
    		}

            StringBuffer codeAndText = new StringBuffer();

            codeAndText.append(indent);
            codeAndText.append("").append(ti._code)
                    .append(':').append(ti._text).append(ti._expandable ? " [+]" : "");

            System.out.println(codeAndText);

            for (String association : ti._assocToChildMap.keySet()) {
                List<LexEVSTreeItem> children = ti._assocToChildMap.get(association);
                for (int i=0; i<children.size(); i++) {
    				LexEVSTreeItem childItem = (LexEVSTreeItem) children.get(i);
                    printTree(childItem, depth + 1);
    			}
            }
        }


}
