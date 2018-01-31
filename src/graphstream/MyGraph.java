package graphstream;

import ihm.RPGScenarioCreationIHM;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.event.MouseInputListener;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;
import tools.RPGSCException;

/**
 * This class handles a graph based on the Graphstream library with the addition
 * of a MouseListener in order to be integrated into the main IHM of the project
 * RPGScenarioCreation.
 * 
 * In this graph, and according to the project, each node represents an element of
 * the scenario and the links between the nodes represent the previous/next 
 * relations existing between the elements. The edge are directed for easy use.
 * 
 * This allows a quick overview of the whole scenario and the elements that have
 * to be followed and to complete it.
 * 
 * This also help to create the scenario by the mouse input allowing to create and
 * remove elements (edges or nodes) while its running, thus adapting to the 
 * actual flow of the scenario.
 * 
 * @author Nicolas Brax
 */
public class MyGraph implements ViewerListener, MouseInputListener{
  
  /****************************************************************************/
  /** Private Attributes                                                     **/
  /****************************************************************************/
  
  /**
   * The node size in pixel that is used by the graph viewer.
   */
  private final int NODE_SIZE = 15;
  
  /**
   * The edge size in pixel that is used by the graph viewer.
   */
  private final int LINK_SIZE = 10;
  
  /**
   * The graph reprensenting, in this project, one scenario.
   */
  private Graph graph;
  
  /**
   * The view of the graph as given by Graphtream.
   */
  private View view;
  
  /**
   * The viewer of the graph as given by Graphtream. 
   */
  private Viewer viewer;
  
  /**
   * The Event Handler of the graph as given by Graphtream.
   */
  private ViewerPipe fromViewer;
  
  /**
   * A list of all the nodes in the graph identified by their id.
   */
  private ArrayList<String> nodeIds;
  
  /**
   * A list of all the edges in the graph identified by their id.
   */
  private ArrayList<String> linkIds;
  
  /**
   * A reference to the main IHM of the project in order to pass information
   * to the user (error and warning mainly).
   */
  private RPGScenarioCreationIHM ihm;
  
  /**
   * An easy access for edge mouse creation. This attribute will not be used
   * and thus will no be given getter and setter methods.
   */
  private String firstSelectedForLinkCreation = "";
  
  
  /****************************************************************************/
  /** Constructor Methods                                                    **/
  /****************************************************************************/
  
  /**
   * Build a graph reprensenting a scenario and store a reference to the main
   * IHM in order to pass information to the user. 
   * 
   * @param ihm The main IHM of the project RPGScenarioCreation
   */
  public MyGraph(RPGScenarioCreationIHM ihm){
    System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
    this.graph = new SingleGraph("Scenario visualisation.");
    this.graph.addAttribute("ui.stylesheet", "url('file:" + System.getProperty("user.dir") 
            + "/data/graphstream_ressources/graph_style.css'" + ")");
    this.graph.addAttribute("ui.quality");
    this.graph.addAttribute("ui.antialias");
    this.nodeIds = new ArrayList<>();
    this.linkIds = new ArrayList<>();
    this.ihm = ihm;
  }
  
  
  /****************************************************************************/
  /** Node Management Methods                                                **/
  /****************************************************************************/
  
  /**
   * Add a new node with the given identification to the graph.
   * 
   * @param nodeId The id of the node to add.
   */
  public void addNode(String nodeId){
    Node n = graph.addNode(nodeId);
    nodeIds.add(nodeId);
    n.addAttribute("ui.label",nodeId);
    n.addAttribute("ui.size",NODE_SIZE);
    n.addAttribute("ui.class","unselected");
    n.setAttribute("selected?",false);
  }
  
  /**
   * Update a node in order to indicates if it is selected or not.
   * 
   * @param nodeId The id of the node to update.
   * @param selected The value of the selection.
   */
  public void updateNode(String nodeId, boolean selected){
    graph.getNode(nodeId).setAttribute("selected?",selected);
    graph.getNode(nodeId).setAttribute("ui.class", (selected ? "selected" : "unselected"));
  }
  
  /**
   * Remove the node with the given id from the graph. This method also removes
   * all the edges attached to this node.
   * 
   * @param nodeId  The id of the node to remove.
   */
  public void removeNode(String nodeId){
    ArrayList<String> linksToRemove = new ArrayList<>();                        // list all the edges linked to the node
    for(Edge edge : graph.getNode(nodeId).getEdgeSet()){
      linksToRemove.add(edge.getId());
    }
    for(String s : linksToRemove){                                              // remove them
      linkIds.remove(s);                                                        // from the list of edges
      graph.removeEdge(s);                                                      // as well as from the graph itself
    }
    nodeIds.remove(nodeId);                                                     // then remove the node from the list
    graph.removeNode(nodeId);                                                   // and from the graph
  }
  
  /**
   * Access the selection state of the node. Return true if it is selected, 
   * false otherwise.
   * 
   * @param nodeId The id of the node to test.
   * @return True if it is selected, false otherwise.
   */
  public boolean isSelected(String nodeId){
    return graph.getNode(nodeId).hasAttribute("selected?");
  }
  
  
  /****************************************************************************/
  /** Edge Management Methods                                                **/
  /****************************************************************************/
  
  /**
   * 
   * @param fromId
   * @param toId
   * @throws RPGSCException 
   */
  public void addLink(String fromId,String toId) throws RPGSCException{
    if(!linkIds.contains(fromId+toId)){
      Edge e = graph.addEdge(fromId+toId,fromId,toId,true);
      linkIds.add(fromId+toId);
      e.addAttribute("ui.size",LINK_SIZE);
      e.setAttribute("selected?",false);
    }
    else{
      throw new RPGSCException("The node is already existing.");
    }
  }
  
  /**
   * Update an edge in order to indicates if it is selected or not.
   * 
   * @param linkId The id of the edge to update.
   * @param selected The value of the selection.
   */
  public void updateLink(String linkId, boolean selected){
    graph.getEdge(linkId).setAttribute("selected?",selected);
    graph.getEdge(linkId).setAttribute("ui.class", (selected ? "selected" : "unselected"));
  }
  
  
  /****************************************************************************/
  /** Graphic Management Methods                                             **/
  /****************************************************************************/
  
  /**
   * Build a JPanel containing a view of the graph. This is done to be integrated
   * into another User Interface in another thread.
   * 
   * @param x The wanted length of the JPanel.
   * @param y The wanted width of the JPanel.
   * @return A ViewPanel extending JPanel containing the graph view.
   */
  public ViewPanel getView(int x, int y){
    this.viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
    this.viewer.enableAutoLayout();
    ViewPanel toReturn = this.viewer.addDefaultView(false);
    toReturn.setMaximumSize(new Dimension(x,y));
    toReturn.setMinimumSize(new Dimension(x,y));
    toReturn.setPreferredSize(new Dimension(x,y));
    toReturn.addMouseListener(this);
    return toReturn;
  }
  
  /**
   * This method display a separate JFrame embedding the graph visualisation. 
   * This runs into its own separate Thread.
   */
  public void display(){
    this.viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
    this.viewer.enableAutoLayout();
    this.view = this.viewer.addDefaultView(true);
    this.view.addMouseListener(this);
  }
  
  /**
   * Adds a graphic handler to the graph view according to the 
   * Graphstream Libreary.
   */
  public void addPipe(){
    this.fromViewer = this.viewer.newViewerPipe();
    fromViewer.addViewerListener(this);
    fromViewer.addSink(graph);
  }
  
  /**
   * Update a node and several edges identified by their ids. See also
   * {@link #updateNode(String, boolean)} and {@link #updateNode(String, boolean)}.
   * 
   * @param nodeId The id of the node to update.
   * @param linkIds The ids of the edges to update.
   */
  public void updateNodesAndLinks(String nodeId, ArrayList<String>linkIds){
    for(String s : this.nodeIds){
      updateNode(s,s.equalsIgnoreCase(nodeId));
    }
    for(String s : this.linkIds){
      updateLink(s,linkIds.contains(s));
    }
  }
  
  
  /****************************************************************************/
  /** Handling Methods for Mouse Events                                      **/
  /****************************************************************************/
  
  /**
   * This method catches the mouse released event and indicates to the 
   * graphstream viewer to process its events.
   * 
   * @param e The Mouse Event.
   */
  @Override
  public void mouseReleased(MouseEvent e) {
    fromViewer.pump();
  }
  
  /**
   * 
   * @param e 
   */
  @Override
  public void mouseClicked(MouseEvent e){
    
    String nodeId = "";
    for(String id : this.nodeIds){
       Object[] xy = graph.getNode(id).getArray("xyz");
       double x = this.viewer.getDefaultView().getCamera().transformGuToPx((Double)xy[0], (Double)xy[1], 0).x;
       double y = this.viewer.getDefaultView().getCamera().transformGuToPx((Double)xy[0], (Double)xy[1], 0).y;
       if((x - NODE_SIZE/2 < e.getPoint().x && e.getPoint().x < x + NODE_SIZE/2)
       && (y - NODE_SIZE/2 < e.getPoint().y && e.getPoint().y < y + NODE_SIZE/2)
       ){
         nodeId = id;
       }//if
    }//for
    if(!"".equals(nodeId)){
      if(e.getButton() == MouseEvent.BUTTON1){
        if(e.getClickCount() == 2){
        ArrayList<String> nodeLinks = new ArrayList<>();
        for(Edge edge : graph.getNode(nodeId).getEdgeSet()){
          nodeLinks.add(edge.getId());
        }
        updateNodesAndLinks(nodeId,nodeLinks);
        this.ihm.selectElement(nodeId);
        }
        else if(e.getClickCount() == 1){
        }
      }// if BUTTON1
      if(e.getButton() == MouseEvent.BUTTON3){
        boolean error = false;
        if("".equals(firstSelectedForLinkCreation)){
          firstSelectedForLinkCreation = nodeId; 
        }
        else{
          try{
            addLink(firstSelectedForLinkCreation,nodeId);
            this.ihm.addPreviousOrNext(firstSelectedForLinkCreation, nodeId, "next");
            this.ihm.addPreviousOrNext(nodeId, firstSelectedForLinkCreation, "previous");
            firstSelectedForLinkCreation = "";
          }
          catch(RPGSCException ex){
            firstSelectedForLinkCreation = "";
            error = true;
          }
        } 
        this.ihm.setFirstForLinkCreation(error ? "This link is already existing..." : firstSelectedForLinkCreation,error);
      }
    }//if nodeId != ""
  }
  
  /****************************************************************************/
  /** Unused Methods Catching Events                                         **/
  /****************************************************************************/
  
  /**
   * 
   * @param id 
   */
  @Override
  public void buttonPushed(String id) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   * 
   * @param id 
   */
  @Override
  public void buttonReleased(String id) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  /**
   * 
   * @param id 
   */
  @Override
  public void viewClosed(String id) {
  }

  /**
   * 
   * @param e 
   */
  @Override
  public void mousePressed(MouseEvent e) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   * 
   * @param e 
   */
  @Override
  public void mouseEntered(MouseEvent e) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   * 
   * @param e 
   */
  @Override
  public void mouseExited(MouseEvent e) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   * 
   * @param e 
   */
  @Override
  public void mouseDragged(MouseEvent e) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   * 
   * @param e 
   */
  @Override
  public void mouseMoved(MouseEvent e) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
