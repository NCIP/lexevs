/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG.gui.displayResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.gui.LB_GUI;
import org.LexGrid.commonTypes.EntityDescription;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * ConceptTreeView provides a UI to display concepts, in particular,
 * ResolvedConceptReference and the associations between them, in a tree view.
 * 
 * A specified concept can be colored for emphasis.
 * 
 * @author Jason Leisch
 * 
 */
public class ConceptTreeView {
	private static Logger log = LogManager.getLogger("ConceptTreeView");

    private Tree tree;
	private final Shell shell;
	private boolean codesShown = true;
	private final Map<Object, TreeItem> addedTreeItemMap;

	public ConceptTreeView(Shell shell, final Composite parent) {
		this.shell = shell;
		this.addedTreeItemMap = new HashMap<Object, TreeItem>();

		shell.getDisplay().syncExec(new Runnable() {
			public void run() {
				tree = new Tree(parent, SWT.SINGLE | SWT.BORDER);
			}
		});

	}

	public void addSelectionListener(SelectionListener listener) {
		tree.addSelectionListener(listener);
	}

	public void removeSelectionListener(SelectionListener listener) {
		tree.removeSelectionListener(listener);
	}

	public Tree getTree() {
		return tree;
	}

	/**
	 * Set whether to show the concept codes with the node names.
	 * 
	 * @param codesShown
	 */
	public void setCodesShown(final boolean codesShown) {
		this.codesShown = codesShown;
		shell.getDisplay().asyncExec(new Runnable() {
			public void run() {
				tree.setRedraw(false);
				TreeItem[] items = tree.getItems();

				for (TreeItem item : items) {
					updateTreeText(item, codesShown);
				}
				tree.setRedraw(true);
			}
		});

	}

	public void updateTreeForConceptSelection(ResolvedConceptReference rcr) {
		TreeItem[] items = tree.getItems();
		for (TreeItem item : items) {
			updateSubTreeItemsForConceptSelection(rcr, item, 0);
		}
	}

	public void clear() {
		shell.getDisplay().asyncExec(new Runnable() {
			public void run() {
				tree.removeAll();
			}
		});

	}

	public void addTreeItem(final Tree parent, final Object key,
			final String text) {
		shell.getDisplay().syncExec(new Runnable() {
			public void run() {
				TreeItem ti = new TreeItem(parent, SWT.NONE);
				ti.setText(text);
				addedTreeItemMap.put(key, ti);
			}
		});
	}

	public void addTreeItem(final TreeItem parent, final Object key,
			final String text) {
		shell.getDisplay().syncExec(new Runnable() {
			public void run() {
				TreeItem ti = new TreeItem(parent, SWT.NONE);
				ti.setText(text);
				addedTreeItemMap.put(key, ti);
			}
		});
	}

	public TreeItem getKeyedTreeItem(Object key) {
		return addedTreeItemMap.get(key);
	}

	public void addAssociations(final TreeItem parent,
			final AssociationList associations) {
		shell.getDisplay().asyncExec(new Runnable() {
			public void run() {
				tree.setRedraw(false);
				treeAssociations(parent, associations, true);
				tree.setRedraw(true);
			}
		});
	}

	public void expand(final TreeItem treeItem) {
		shell.getDisplay().asyncExec(new Runnable() {
			public void run() {
				treeItem.setExpanded(true);
			}
		});
	}

	public void addConcepts(ResolvedConceptReference[] concepts) {
		tree.setRedraw(false);

		for (int i = 0; i < concepts.length; i++) {
		    ResolvedConceptReference ref = concepts[i];
			TreeItem ti = new TreeItem(tree, SWT.NONE);
			String desc = ref.getEntityDescription() != null ? ref
					.getEntityDescription().getContent()
					: null;
			String nodeName = Graph.getNodeName(ref.getConceptCode(),
					desc, false, " ");

			ti.setData(ref);
			ti.setText(nodeName);
			treeAssociations(ti, ref.getSourceOf(), true);
			treeAssociations(ti, ref.getTargetOf(), false);
		}

		tree.setRedraw(true);
	}

	private void treeAssociations(TreeItem parentNode, AssociationList al,
			boolean down) {
		if (al == null || al.getAssociationCount() <= 0) {
			return;
		}
		Association[] aList = al.getAssociation();
		for (Association association : aList) {
		    
		    int maxPageSize = LB_GUI.MAX_PAGE_SIZE;
		    
		    Iterator<? extends AssociatedConcept> itr =  association.getAssociatedConcepts().iterateAssociatedConcept();
		  
		    List<AssociatedConcept> list = new ArrayList<AssociatedConcept>();
		    
		    for(int i=0;i<maxPageSize && itr.hasNext();i++) {
		        list.add(itr.next());
		    }
		    
		    if(itr.hasNext()) {
		        list.add(new MoreResultsToPageAssociatedConcept());
		    }
		   
			TreeItem associationBranch = new TreeItem(parentNode, SWT.NONE);
			associationBranch.setData(association);
			// Resolve the core association name ...
			StringBuffer edgeText = new StringBuffer();
			if (StringUtils.isNotBlank(association.getDirectionalName())) {
				edgeText.append(association.getDirectionalName());
			} else {
				if (!down)
					edgeText.append("[R]");
				edgeText.append(association.getAssociationName());
			}
			associationBranch.setText(edgeText.toString());

			for (AssociatedConcept ac : list) {
//				 if (addToResults) {
//				 addCodeToDisplayedResults(ac);
//				 }
//				 Node child = graph.addNode(ac);
				TreeItem conceptLeaf = new TreeItem(associationBranch, SWT.NONE);
				/*
				 * Give this tree item some data to associate it with, so that
				 * we can intelligently do stuff with it later.
				 */
				conceptLeaf.setData(ac);
				conceptLeaf.setData("code", ac.getConceptCode());

				EntityDescription desc = ac.getEntityDescription();
				String content = null;
				if (desc != null) {
					content = desc.getContent();
				}
				// String content = desc.getContent();
				if (content != null) {
					content = content.trim();
				}
				String nodeName = Graph.getNodeName(codesShown ? ac
						.getConceptCode() : null,
						desc == null ? null : content, false, " ");

				// Add qualifiers, if available...
				StringBuffer additionalNodeText = new StringBuffer();
				NameAndValueList aqList = ac.getAssociationQualifiers();
				if (aqList != null && aqList.getNameAndValueCount() > 0) {
					additionalNodeText.append(':');
					for (int k = 0; k < aqList.getNameAndValueCount(); k++) {
						NameAndValue nv = aqList.getNameAndValue(k);
						if (k > 0)
							additionalNodeText.append(", ");
						additionalNodeText.append(nv.getName());
						if (StringUtils.isNotBlank(nv.getContent()))
							additionalNodeText.append('(').append(
									nv.getContent()).append(')');
					}
				}

				if (additionalNodeText.length() > 0) {
					nodeName += " (" + additionalNodeText.toString() + ")";
				}
				conceptLeaf.setText(nodeName);

				if (down) {
				    treeAssociations(conceptLeaf, ac.getSourceOf(), down);
				} else {
				    treeAssociations(conceptLeaf, ac.getTargetOf(), down);
				}
			}
		}
	}

	/**
	 * Recursively traverse a tree to find all matching
	 * ResolvedConceptReferences to the one passed in. Any matches that are
	 * found are changed to blue foreground. All others are (re)set to black
	 * foreground. (IE, potentially broken if users have changed their color
	 * scheme, but no more so than other areas of this application).
	 * 
	 * The tree is automatically expanded to ensure any match is not hidden.
	 * 
	 * @param rcr
	 *            the ResolvedConceptReference we're searching for
	 * @param parentItem
	 *            the TreeItem whose children should be recursively searched
	 * @param chainMatchCount
	 *            the number of matches that have been found in the current
	 *            lineage. This is used to prevent infinite recursion. After two
	 *            descendants are e
	 * @return true if the ResolvedConceptReference was found.
	 */
	private boolean updateSubTreeItemsForConceptSelection(
			ResolvedConceptReference rcr, TreeItem parentItem,
			int chainMatchCount) {
		boolean found = false;
		boolean currentIsMatch = false;
		parentItem.setForeground(shell.getDisplay().getSystemColor(
				SWT.COLOR_BLACK));
		Object o = parentItem.getData();
		if (o instanceof ResolvedConceptReference) {
			ResolvedConceptReference itemRcr = (ResolvedConceptReference) o;

			if (itemRcr.getConceptCode().equals(rcr.getConceptCode())) {
				found = true;
				currentIsMatch = true;
				++chainMatchCount;
				log.info("chain match: " + chainMatchCount);
				parentItem.setForeground(shell.getDisplay().getSystemColor(
						SWT.COLOR_BLUE));
			}
		}

		if (chainMatchCount < 2) {
			/*
			 * This avoids infinite recursion by preventing the continued
			 * recursion after a node, X, has n ancestors that are also X. Maybe
			 * n could be passed in to this function, but YAGNI.
			 */
			TreeItem[] items = parentItem.getItems();
			for (TreeItem item : items) {
				if (updateSubTreeItemsForConceptSelection(rcr, item,
						chainMatchCount)) {
					parentItem.setForeground(shell.getDisplay().getSystemColor(
							currentIsMatch ? SWT.COLOR_BLUE : SWT.COLOR_GREEN));
					found = true;
				}
			}

		}
		return found;
	}

	private void updateTreeText(TreeItem parent, boolean codesShown) {
		Object o = parent.getData();
		if (o instanceof ResolvedConceptReference) {
			ResolvedConceptReference rcr = (ResolvedConceptReference) o;
			EntityDescription desc = rcr.getEntityDescription();
			String content = desc.getContent();
			if (content != null) {
				content = content.trim();
			}
			String nodeName = Graph.getNodeName((codesShown ? rcr
					.getConceptCode() : null), desc == null ? null : content,
					false, " ");

			if (o instanceof AssociatedConcept) {
				AssociatedConcept ac = (AssociatedConcept) o;
				// Add qualifiers, if available...
				StringBuffer additionalNodeText = new StringBuffer();
				NameAndValueList aqList = ac.getAssociationQualifiers();
				if (aqList != null && aqList.getNameAndValueCount() > 0) {
					additionalNodeText.append(':');
					for (int k = 0; k < aqList.getNameAndValueCount(); k++) {
						NameAndValue nv = aqList.getNameAndValue(k);
						if (k > 0)
							additionalNodeText.append(", ");
						additionalNodeText.append(nv.getName());
						if (StringUtils.isNotBlank(nv.getContent()))
							additionalNodeText.append('(').append(
									nv.getContent()).append(')');
					}
				}

				if (additionalNodeText.length() > 0) {
					nodeName += " (" + additionalNodeText.toString() + ")";
				}
			}
			parent.setText(nodeName);
		}
		for (TreeItem item : parent.getItems()) {
			updateTreeText(item, codesShown);
		}
	}
}