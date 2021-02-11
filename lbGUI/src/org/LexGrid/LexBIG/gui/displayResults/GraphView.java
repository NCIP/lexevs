
package org.LexGrid.LexBIG.gui.displayResults;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.Iterator;

import javax.swing.JPanel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import prefuse.Constants;
import prefuse.Visualization;
import prefuse.action.Action;
import prefuse.action.ActionList;
import prefuse.action.ItemAction;
import prefuse.action.RepaintAction;
import prefuse.action.animate.ColorAnimator;
import prefuse.action.animate.LocationAnimator;
import prefuse.action.animate.QualityControlAnimator;
import prefuse.action.animate.VisibilityAnimator;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.FontAction;
import prefuse.action.layout.CollapsedSubtreeLayout;
import prefuse.action.layout.graph.NodeLinkTreeLayout;
import prefuse.activity.SlowInSlowOutPacer;
import prefuse.controls.FocusControl;
import prefuse.controls.PanControl;
import prefuse.controls.WheelZoomControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.tuple.TupleSet;
import prefuse.render.AbstractShapeRenderer;
import prefuse.render.DefaultRendererFactory;
import prefuse.render.EdgeRenderer;
import prefuse.render.LabelRenderer;
import prefuse.render.LabeledEdgeRenderer;
import prefuse.util.ColorLib;
import prefuse.util.FontLib;
import prefuse.visual.VisualItem;
import prefuse.visual.expression.InGroupPredicate;
import prefuse.visual.sort.TreeDepthItemSorter;

/**
 * Viewer for graphs, based off of one of the prefuse layout examples.
 * 
 * @author <A HREF="mailto:johnson.thomas@mayo.edu">Thomas Johnson</A>
 * @author <A HREF="mailto:leisch.jason@mayo.edu">Jason Leisch</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class GraphView extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final String graph = "graph";
	private static final String nodes = "graph.nodes";
	private static final String edges = "graph.edges";
	private static String m_label;
	Visualization m_vis;
	private LabelRenderer m_nodeRenderer;
	private EdgeRenderer m_edgeRenderer;
	private int m_orientation = Constants.ORIENT_LEFT_RIGHT;
	private Display display;
	java.awt.Frame frame;
	DisplayCodedNodeSet dcns;
	private final Composite swtAwtComponent;
	private Graph currentGraph;
	private NodeLinkTreeLayout treeLayout;

	public GraphView(Composite parent, DisplayCodedNodeSet dcns) {
		swtAwtComponent = new Composite(parent, SWT.EMBEDDED | SWT.BORDER);
		frame = SWT_AWT.new_Frame(swtAwtComponent);

		frame.add(this);
		frame.pack();

		swtAwtComponent.addControlListener(new ControlListener() {

			public void controlResized(ControlEvent arg0) {
				if (display != null) {
					Point p = swtAwtComponent.getSize();
					display.setSize(p.x, p.y);
				}
			}

			public void controlMoved(ControlEvent arg0) {
				// noop
			}

		});

		this.dcns = dcns;

		frame.setVisible(true);

	}

	public void init(Graph g, String label) {
		if (g == null) {
			// create an empty graph.
			g = new Graph(false);
			g.addNode();
		}

		currentGraph = g;

		if (g.getNodeCount() == 0) {
			// prevent errors on an empty graph
			g.addNode();
		}

		// create a new, empty visualization for our data
		m_vis = new Visualization();

		m_label = label;

		m_vis.add(graph, g);

		m_nodeRenderer = new LabelRenderer(m_label);
		m_nodeRenderer.setRenderType(AbstractShapeRenderer.RENDER_TYPE_FILL);
		m_nodeRenderer.setHorizontalAlignment(Constants.LEFT);
		m_nodeRenderer.setHorizontalTextAlignment(Constants.LEFT);
		m_nodeRenderer.setRoundedCorner(8, 8);
		m_edgeRenderer = new LabeledEdgeRenderer(Constants.EDGE_TYPE_CURVE,
				"name", Color.BLACK);

		DefaultRendererFactory rf = new DefaultRendererFactory(m_nodeRenderer,
				m_edgeRenderer);
		rf.add(new InGroupPredicate(edges), m_edgeRenderer);
		m_vis.setRendererFactory(rf);

		// colors
		ItemAction nodeColor = new NodeColorAction(nodes);
		ItemAction textColor = new ColorAction(nodes, VisualItem.TEXTCOLOR,
				ColorLib.rgb(0, 0, 0));
		m_vis.putAction("textColor", textColor);

		ItemAction edgeColor = new ColorAction(edges, VisualItem.STROKECOLOR,
				ColorLib.rgb(200, 200, 200));
		ItemAction edgeColor2 = new ColorAction(edges, VisualItem.FILLCOLOR,
				ColorLib.rgb(200, 200, 200));

		// quick repaint
		ActionList repaint = new ActionList();
		repaint.add(nodeColor);
		repaint.add(new RepaintAction());
		m_vis.putAction("repaint", repaint);

		// full paint
		ActionList fullPaint = new ActionList();
		fullPaint.add(nodeColor);
		m_vis.putAction("fullPaint", fullPaint);

		// animate paint change
		ActionList animatePaint = new ActionList(400);
		animatePaint.add(new ColorAnimator(nodes));
		animatePaint.add(new RepaintAction());
		m_vis.putAction("animatePaint", animatePaint);

		// create the tree layout action
		treeLayout = new NodeLinkTreeLayout(graph, m_orientation, 100, 25, 15);
		m_vis.putAction("treeLayout", treeLayout);

		CollapsedSubtreeLayout subLayout = new CollapsedSubtreeLayout(graph,
				m_orientation);
		m_vis.putAction("subLayout", subLayout);

		AutoPanAction autoPan = new AutoPanAction();

		// create the filtering and layout
		ActionList filter = new ActionList();
		filter.add(new GraphViewFilter(graph, 2, this));
		filter.add(new FontAction(nodes, FontLib
				.getFont("Arial Unicode MS", 16)));
		filter.add(treeLayout);
		filter.add(subLayout);
		filter.add(textColor);
		filter.add(nodeColor);
		filter.add(edgeColor);
		filter.add(edgeColor2);
		m_vis.putAction("filter", filter);

		// animated transition
		ActionList animate = new ActionList(1000);
		animate.setPacingFunction(new SlowInSlowOutPacer());
		animate.add(autoPan);
		animate.add(new QualityControlAnimator());
		animate.add(new VisibilityAnimator(graph));
		animate.add(new LocationAnimator(nodes));
		animate.add(new ColorAnimator(nodes));
		animate.add(new RepaintAction());
		m_vis.putAction("animate", animate);
		m_vis.alwaysRunAfter("filter", "animate");

		// create animator for orientation changes
		ActionList orient = new ActionList(2000);
		orient.setPacingFunction(new SlowInSlowOutPacer());
		orient.add(autoPan);
		orient.add(new QualityControlAnimator());
		orient.add(new LocationAnimator(nodes));
		orient.add(new RepaintAction());
		m_vis.putAction("orient", orient);

		// ------------------------------------------------

		// initialize the display
		display = new Display(m_vis);

		display.setItemSorter(new TreeDepthItemSorter());
		display.addControlListener(new ZoomToFitControl());
		display.addControlListener(new ZoomControl());
		display.addControlListener(new WheelZoomControl());
		display.addControlListener(new PanControl());
		FocusControl fc = new MyFocusControl(1, "filter", dcns);
		fc.setFilter(new InGroupPredicate(nodes));
		display.addControlListener(fc);

		// filter graph and perform layout
		setOrientation(m_orientation);
		m_vis.run("filter");

		add(display);

		Dimension d = frame.getSize();
		display.setSize(d);
		display.pan(d.getWidth() / 2, d.getHeight() / 2);
	}

	protected void increaseSpace() {
		if (treeLayout == null) {
			return;
		}
		treeLayout.setBreadthSpacing(treeLayout.getBreadthSpacing() + 10);
		treeLayout.setDepthSpacing(treeLayout.getDepthSpacing() + 30);
		treeLayout.setSubtreeSpacing(treeLayout.getSubtreeSpacing() + 10);
		display.getVisualization().cancel("orient");
		display.getVisualization().run("treeLayout");
		display.getVisualization().run("orient");
	}

	protected void decreaseSpace() {
		if (treeLayout == null) {
			return;
		}
		treeLayout.setBreadthSpacing(treeLayout.getBreadthSpacing() - 10);
		treeLayout.setDepthSpacing(treeLayout.getDepthSpacing() - 30);
		treeLayout.setSubtreeSpacing(treeLayout.getSubtreeSpacing() - 10);
		display.getVisualization().cancel("orient");
		display.getVisualization().run("treeLayout");
		display.getVisualization().run("orient");
	}

	public void setGraph(Graph g) {
		// update graph
		m_vis.removeGroup(graph);
		m_vis.addGraph(graph, g);
		m_vis.run("filter");
		currentGraph = g;
	}

	public Graph getGraph() {
		return currentGraph;
	}

	public void setOrientation(int orientation) {
		NodeLinkTreeLayout rtl = (NodeLinkTreeLayout) m_vis
				.getAction("treeLayout");
		CollapsedSubtreeLayout stl = (CollapsedSubtreeLayout) m_vis
				.getAction("subLayout");
		switch (orientation) {
		case Constants.ORIENT_LEFT_RIGHT:
			m_nodeRenderer.setHorizontalAlignment(Constants.LEFT);
			m_edgeRenderer.setHorizontalAlignment1(Constants.RIGHT);
			m_edgeRenderer.setHorizontalAlignment2(Constants.LEFT);
			m_edgeRenderer.setVerticalAlignment1(Constants.CENTER);
			m_edgeRenderer.setVerticalAlignment2(Constants.CENTER);
			break;
		case Constants.ORIENT_RIGHT_LEFT:
			m_nodeRenderer.setHorizontalAlignment(Constants.RIGHT);
			m_edgeRenderer.setHorizontalAlignment1(Constants.LEFT);
			m_edgeRenderer.setHorizontalAlignment2(Constants.RIGHT);
			m_edgeRenderer.setVerticalAlignment1(Constants.CENTER);
			m_edgeRenderer.setVerticalAlignment2(Constants.CENTER);
			break;
		case Constants.ORIENT_TOP_BOTTOM:
			m_nodeRenderer.setHorizontalAlignment(Constants.CENTER);
			m_edgeRenderer.setHorizontalAlignment1(Constants.CENTER);
			m_edgeRenderer.setHorizontalAlignment2(Constants.CENTER);
			m_edgeRenderer.setVerticalAlignment1(Constants.BOTTOM);
			m_edgeRenderer.setVerticalAlignment2(Constants.TOP);
			break;
		case Constants.ORIENT_BOTTOM_TOP:
			m_nodeRenderer.setHorizontalAlignment(Constants.CENTER);
			m_edgeRenderer.setHorizontalAlignment1(Constants.CENTER);
			m_edgeRenderer.setHorizontalAlignment2(Constants.CENTER);
			m_edgeRenderer.setVerticalAlignment1(Constants.TOP);
			m_edgeRenderer.setVerticalAlignment2(Constants.BOTTOM);
			break;
		default:
			throw new IllegalArgumentException(
					"Unrecognized orientation value: " + orientation);
		}
		m_orientation = orientation;
		rtl.setOrientation(orientation);
		stl.setOrientation(orientation);
	}

	public void updateOrientation(int orientation) {
		if (display == null)
			return;
		setOrientation(orientation);
		display.getVisualization().cancel("orient");
		display.getVisualization().run("treeLayout");
		display.getVisualization().run("orient");
	}

	public class AutoPanAction extends Action {
		private Point2D m_start = new Point2D.Double();
		private Point2D m_end = new Point2D.Double();
		private Point2D m_cur = new Point2D.Double();
		private int m_bias = 150;

		@Override
		public void run(double frac) {
			TupleSet ts = m_vis.getFocusGroup(Visualization.FOCUS_ITEMS);
			if (ts.getTupleCount() == 0)
				return;

			if (frac == 0.0) {
				int xbias = 0, ybias = 0;
				switch (m_orientation) {
				case Constants.ORIENT_LEFT_RIGHT:
					xbias = m_bias;
					break;
				case Constants.ORIENT_RIGHT_LEFT:
					xbias = -m_bias;
					break;
				case Constants.ORIENT_TOP_BOTTOM:
					ybias = m_bias;
					break;
				case Constants.ORIENT_BOTTOM_TOP:
					ybias = -m_bias;
					break;
				}

				VisualItem vi = (VisualItem) ts.tuples().next();
				m_cur.setLocation(getWidth() / 2, getHeight() / 2);
				display.getAbsoluteCoordinate(m_cur, m_start);
				m_end.setLocation(vi.getX() + xbias, vi.getY() + ybias);
			} else {
				m_cur.setLocation(m_start.getX() + frac
						* (m_end.getX() - m_start.getX()), m_start.getY()
						+ frac * (m_end.getY() - m_start.getY()));
				display.panToAbs(m_cur);
			}
		}
	}

	public static class NodeColorAction extends ColorAction {

		public NodeColorAction(String group) {
			super(group, VisualItem.FILLCOLOR);
		}

		@Override
		public int getColor(VisualItem item) {
			if (m_vis.isInGroup(item, Visualization.SEARCH_ITEMS))
				return ColorLib.rgb(255, 190, 190);
			else if (m_vis.isInGroup(item, Visualization.FOCUS_ITEMS))
				return ColorLib.rgb(198, 229, 229);
			else if (item.getDOI() > -1)
				return ColorLib.rgb(164, 193, 193);
			else
				return ColorLib.gray(200);
		}

	}

	@SuppressWarnings("unchecked")
	public void focusCode(String code, String codeName) {
		if (dcns != null && currentGraph != null) {
			Iterator<VisualItem> it = m_vis.items(nodes);
			while (it.hasNext()) {
				VisualItem vi = it.next();
				String codeToDisplay = dcns.getShowCodesInGraph() ? code : null;
				String name = Graph.getNodeName(codeToDisplay, codeName, false,
						"\n");
				if (vi.get("name").equals(name)) {
					Visualization vis = vi.getVisualization();
					TupleSet ts = vis.getFocusGroup(Visualization.FOCUS_ITEMS);
					ts.setTuple(vi);
					vis.run("filter");

					break;
				}
			}
		}
	}

	public Composite getSwtComposite() {
		return swtAwtComponent;
	}

} // end of class GraphView