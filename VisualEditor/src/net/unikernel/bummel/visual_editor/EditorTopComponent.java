package net.unikernel.bummel.visual_editor;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxMultiplicity;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Map;
import javax.swing.Action;
import net.unikernel.bummel.basic_elements.BasicElement;
import net.unikernel.bummel.engine.Engine;
import net.unikernel.bummel.jgraph.ElementModel;
import net.unikernel.bummel.jgraph.ElementPort;
import net.unikernel.bummel.project_model.ProjectModel;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//net.unikernel.bummel.visual_editor//Editor//EN",
					 autostore = false)
@TopComponent.Description(preferredID = "EditorTopComponent",
						  //iconBase="SET/PATH/TO/ICON/HERE", 
						  persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "net.unikernel.bummel.visual_editor.EditorTopComponent")
@ActionReference(path = "Menu/Window", position = 0)
@TopComponent.OpenActionRegistration(displayName = "#CTL_NewEditorAction"/*,
									 preferredID = "EditorTopComponent"*/)
public final class EditorTopComponent extends TopComponent
{
	/**
	 * Counter of opened top components.
	 */
	private static int counter = 0;
	private ProjectModel project;
	private Engine engine;
	public EditorTopComponent()
	{
		project=new ProjectModel();
		initComponents();
		setName(NbBundle.getMessage(EditorTopComponent.class, "CTL_EditorTopComponent", ++counter));
		project.setName(NbBundle.getMessage(EditorTopComponent.class, "CTL_EditorTopComponent", counter));
		setToolTipText(NbBundle.getMessage(EditorTopComponent.class, "HINT_EditorTopComponent"));
		//putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
		putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
		putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
		putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
		associateLookup(Lookups.fixed(PaletteFactory.createPalette(new AbstractNode(Children.create(new CategoryChildFactory(), true)), new PaletteActions() {
			@Override
			public Action[] getImportActions()
			{
				return null;
			}
			@Override
			public Action[] getCustomPaletteActions()
			{
				return null;
			}
			@Override
			public Action[] getCustomCategoryActions(Lookup lkp)
			{
				return null;
			}
			@Override
			public Action[] getCustomItemActions(Lookup lkp)
			{
				return null;
			}
			@Override
			public Action getPreferredAction(Lookup lkp)
			{
				return null;
			}
		})));
		
		final mxGraph graph = graphComponent.getGraph();
		//mxGraph graph = new mxGraph(project.getModel());
		project.setModel(graph.getModel());
		//graph.setModel(project.getModel());
		//graphComponent = new mxGraphComponent(graph);
		//graphComponent.refresh();
		//graphComponent.getGraphControl().repaint();
		
		graphComponent.getGraphControl().addMouseListener(new MouseAdapter()
		{
		
			@Override
			public void mouseReleased(MouseEvent e)
			{
				mxCell cell = (mxCell) graphComponent.getCellAt(e.getX(), e.getY());
				
				if (cell != null)
				{
					if(cell instanceof ElementModel)
					{
						Object val = cell.getValue();
						if(val instanceof BasicElement)
						{
							((BasicElement)val).toggleState();
							//((mxGraphModel) graph.getModel()).fireEvent(new mxEventObject(mxEvent.CHANGE)); //cause nullpointer
							engine.start();
						}
					}
				}
			}
		});
		
		
		graph.setMultigraph(false);
		graph.setAllowDanglingEdges(false);
		graph.setDisconnectOnMove(false);
		graph.setCellsResizable(false);
		graph.setCellsEditable(false);
		Map<String, Object> style = graph.getStylesheet().getDefaultEdgeStyle();
		style.put(mxConstants.STYLE_EDGE, mxEdgeStyle.ElbowConnector);
		style.put(mxConstants.STYLE_ENDARROW, "");	//remove arrow
		graphComponent.setConnectable(true);
		graphComponent.setToolTips(true);
		graph.setLabelsVisible(false);
		graphComponent.setFoldingEnabled(false);

		// Enables rubberband selection
		new mxRubberband(graphComponent);
		new mxKeyboardHandler(graphComponent);
		
		mxMultiplicity[] multiplicities = new mxMultiplicity[2];
		
		multiplicities[0] = new mxMultiplicity(false, null, null, null, 0,
				"1", null, "", null, true){
					@Override
					public boolean checkTerminal(mxGraph graph, Object terminal, Object edge)
					{
						return terminal instanceof ElementPort;
					}
				};
		multiplicities[1] = new mxMultiplicity(true, null, null, null, 0,
				"1", null, "", null, true){
					@Override
					public boolean checkTerminal(mxGraph graph, Object terminal, Object edge)
					{
						return terminal instanceof ElementPort;
					}
				};

		graph.setMultiplicities(multiplicities);
		graphComponent.getConnectionHandler().setShowMessageDialogEnabled(false);
	
		// Installs automatic validation (use editor.validation = true
		// if you are using an mxEditor instance)
		graph.getModel().addListener(mxEvent.CHANGE, new mxIEventListener()
		{
			@Override
			public void invoke(Object sender, mxEventObject evt)
			{
				graphComponent.validateGraph();
				engine.start();
			}
		});
		graphComponent.showDirtyRectangle = true;
		graph.getModel().addListener(Engine.CIRCLE_DONE, new mxIEventListener()
		{
			@Override
			public void invoke(Object sender, mxEventObject evt)
			{
				System.out.println(evt.getName());
				graphComponent.getGraph().refresh();
//				graphComponent.refresh();
//				graphComponent.repaint(graphComponent.getViewport().getViewRect());
//				graphComponent.getGraph().getModel().beginUpdate();
//				graphComponent.getGraph().getModel().endUpdate();
//				graphComponent.validateGraph();
			}
		});

		// Initial validation
		graphComponent.validateGraph();
		
		
               
		engine = new Engine(graph);
		System.out.println("Starting engine, rooooarrrr");
		engine.start();
//                try{Thread.sleep(100);}
//                catch(InterruptedException e){}
//		engine.stop();
//		engine.start();
//                try{Thread.sleep(100);}
//                catch(InterruptedException e){}
//		engine.stop();
	}
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        graphComponent = new com.mxgraph.swing.mxGraphComponent();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(graphComponent, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(graphComponent, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.mxgraph.swing.mxGraphComponent graphComponent;
    // End of variables declaration//GEN-END:variables
	@Override
	public void componentOpened()
	{
		// TODO add custom code on component opening
	}
	@Override
	public void componentClosed()
	{
		counter--;
		engine.stop();
		engine = null;
		// TODO add custom code on component closing
	}
	void writeProperties(java.util.Properties p)
	{
		// better to version settings since initial version as advocated at
		// http://wiki.apidesign.org/wiki/PropertyFiles
		p.setProperty("version", "1.0");
		// TODO store your settings
	}
	void readProperties(java.util.Properties p)
	{
		String version = p.getProperty("version");
		// TODO read your settings according to their version
	}
}
