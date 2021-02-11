
package org.LexGrid.LexBIG.gui.displayResults;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.apache.commons.lang.StringUtils;

import prefuse.data.Edge;
import prefuse.data.Node;
import prefuse.data.Tuple;

/**
 * Overriding the addEdge method of Graph to allow for multiple edges between
 * nodes.
 * 
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:leisch.jason@mayo.edu">Jason Leisch</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class Graph extends prefuse.data.Graph {
	private Hashtable<String, Node> allNodes = new Hashtable<String, Node>();

	public Graph(boolean directed) {
		super(directed);
		this.addColumn("name", String.class);
		this.addColumn("RCR", ResolvedConceptReference.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see prefuse.data.Graph#addEdge(prefuse.data.Node, prefuse.data.Node)
	 */
	public Edge addEdge(Node s, Node t, String name) {
		Edge e = super.getEdge(s, t);
		if (e != null) {
			// means I already have an edge here - just add in the name.
			String curName = e.getString("name");

			// see if the current edge name already exists

			StringTokenizer st = new StringTokenizer(curName, "\n");
			while (st.hasMoreTokens()) {
				if (st.nextToken().equals(name)) {
					return e;
				}
			}

			// doesn't exist, add it

			curName += "\n" + name;
			e.setString("name", curName);
			return e;
		}

		// if I get here, no edge was found. What about reverse?
		e = super.getEdge(t, s);
		if (e != null) {
			// means I already have an edge here - just add in the name.
			String curName = e.getString("name");

			// see if the current edge name already exists

			StringTokenizer st = new StringTokenizer(curName, "\n");
			while (st.hasMoreTokens()) {
				if (st.nextToken().equals("[R]" + name)) {
					return e;
				}
			}

			// doesn't exist, add it
			curName += "\n[R]" + name;
			e.setString("name", curName);
			return e;
		}

		// get here - the edge was not found.

		e = super.addEdge(s, t);
		e.setString("name", name);

		return e;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see prefuse.data.Graph#addNode()
	 */
	public Node addNode(ResolvedConceptReference rcr, boolean showCode) {
		// Limit length for nodes other than the focus item
		// to allow more items in same horizontal space.
		String code = showCode ? rcr.getConceptCode() : null;
		String desc = rcr.getEntityDescription() != null ? rcr
				.getEntityDescription().getContent() : null;
		String nodeName = getNodeName(code, desc, false, "\n");

		if (allNodes.containsKey(nodeName)) {
			return allNodes.get(nodeName);
		} else {
			Node newNode = addNode();

			newNode.setString("name", nodeName);
			newNode.set("RCR", rcr);
			allNodes.put(nodeName, newNode);
			return newNode;
		}
	}

	public Node addNode(String nodeName) {
		if (allNodes.containsKey(nodeName)) {
			return allNodes.get(nodeName);
		} else {
			Node newNode = addNode();
			newNode.setString("name", nodeName);
			allNodes.put(nodeName, newNode);
			return newNode;
		}
	}

	// This is to accomodate Property Link nodes.
	// Source and target nodes may have the same name, and there
	// may be multiples of them, so they need to be stored with a
	// unique id.
	public Node addNode(String nodeName, String propertyLinkId) {
		if (allNodes.containsKey(propertyLinkId)) {
			return allNodes.get(propertyLinkId);
		} else {
			Node newNode = addNode();
			newNode.setString("name", nodeName);
			allNodes.put(propertyLinkId, newNode);
			return newNode;
		}
	}

	/**
	 * Utility method to return the graph based node name for the given concept
	 * reference.
	 * 
	 * @param code
	 *            The concept code to visualize. If null, the code is not
	 *            included in the returned name.
	 * @param desc
	 *            The concept description; can be null.
	 * @param abbreviate
	 *            If true, the text will be abbreviated if it exceeds a
	 *            pre-defined limit (with '...' appended). This can be used to
	 *            accomodate more nodes in horizontal space.
	 * @param separator
	 *            the String to use to separate the code from the description
	 * @return The corresponding node name.
	 */
	public static String getNodeName(String code, String desc,
			boolean abbreviate, String separator) {
		StringBuffer nameBuf = new StringBuffer(128);
		if (code != null)
			nameBuf.append('[').append(code.trim()).append("]").append(
					separator);
		if (StringUtils.isNotBlank(desc))
			nameBuf.append(desc);
		return (abbreviate) ? StringUtils.abbreviate(nameBuf.toString(), 64)
				: nameBuf.toString();
	}

	/**
	 * Force a refresh of all names assigned to graph nodes.
	 */
	@SuppressWarnings("unchecked")
	void refreshNodeNames(boolean showCode, boolean abbreviate) {
		for (Iterator<Tuple> tuples = getNodes().tuples(); tuples.hasNext();) {
			Tuple t = tuples.next();
			ResolvedConceptReference rcr = (ResolvedConceptReference) t
					.get("RCR");
			if (rcr != null) {
				String code = showCode ? rcr.getConceptCode() : null;
				String desc = rcr.getEntityDescription() != null ? rcr
						.getEntityDescription().getContent() : null;
				t.set("name", getNodeName(code, desc, abbreviate, "\n"));
			}
		}
	}
}