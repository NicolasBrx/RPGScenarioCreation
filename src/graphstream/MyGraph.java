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
  
  /**
   * This boolean allows to monitor the graph view event catching by denying it
   * if no graph have been linked to the view yet.
   */
  private boolean graphBuilt =false ; 
  
  
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
   * Add a new node with the given identification to the graph. If the node 
   * already exists, it will remove it and replace it by the new one.
   * Verification have to be made in the main IHM and in the scenario representation
   * as MyGraph is just a visualization, nothing more.
   * 
   * @param nodeId The id of the node to add.
   */
  public void addNode(String nodeId){
    if(nodeIds != null && nodeIds.contains(nodeId)){
      removeNode(nodeId);
    }
    Node n = graph.addNode(nodeId);
    nodeIds.add(nodeId);
    n.addAttribute("ui.label",nodeId);
    n.addAttribute("ui.size",NODE_SIZE);
    n.addAttribute("ui.class","unselected");
    if(!graphBuilt){
      graphBuilt = true;
    }
  }
  
  /**
   * Update a node in order to indicates if it is selected or not.
   * 
   * @param nodeId The id of the node to update.
   * @param selected The value of the selection.
   */
  public void updateNode(String nodeId, boolean selected){
    if(selected){
      graph.getNode(nodeId).setAttribute("selected?");
    }
    else{
      graph.getNode(nodeId).removeAttribute("selected?");
    }
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
      if((graph.getNode(fromId).hasAttribute("selected?")) 
      || (graph.getNode(toId).hasAttribute("selected?"))){
        e.setAttribute("selected?");
      }
      e.setAttribute("ui.class",(e.hasAttribute("selected?")?"selected":"unselected"));
    }
    else{
      throw new RPGSCException("The edge is already existing.");
    }
  }
  
  /**
   * Update an edge in order to indicates if it is selected or not.
   * 
   * @param linkId The id of the edge to update.
   * @param selected The value of the selection.
   */
  public void updateLink(String linkId, boolean selected){
    if(selected){
      graph.getEdge(linkId).setAttribute("selected?");
    }
    else{
      graph.getEdge(linkId).removeAttribute("selected?");
    }
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
    if(this.fromViewer == null){
      this.fromViewer = this.viewer.newViewerPipe();
      fromViewer.addViewerListener(this);
      fromViewer.addSink(graph);
    }
  }
  
  /**
   * Update a node and several edges identified by their ids. See also
   * {@link #updateNode(String, boolean)} and {@link #updateNode(String, boolean)}.
   * This function set the select attribute of the node and the edges given in
   * parameters to true and to false for all the other nodes and edges.
   * 
   * @param nodeId The id of the node to update.
   * @param linkIds The ids of the edges to update.
   */
  public void updateNodesAndLinks(String nodeId, ArrayList<String>linkIds){
    for(String s : this.nodeIds){                                               // process all the nodes
      updateNode(s,s.equalsIgnoreCase(nodeId));                                 // select the one in parameter and unselect the others
    }
    for(String s : this.linkIds){                                               // process all the edges
      updateLink(s,linkIds.contains(s));                                        // select the ones in parameter and unselect the others
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
    if(!graphBuilt){return;}
    fromViewer.pump();
  }
  
  /**
   * Catch the mouse click event and propose several features.
   *  - one click of the left button: nothing for now
   *  - two clicks of the left button: select the node and all its attached 
   *    edges, update the main IHM with information on the element it represents
   *  - one click on the right button: feature the link addition mode with the 
   *    first click setting the origin of the edge and the second click setting
   *    the end of the edge that is then created.
   * 
   *  Other features are to be made in the future... maybe.
   *
   * @param e The event catched.
   */
  @Override
  public void mouseClicked(MouseEvent e){
    if(!graphBuilt){return;}
    String nodeId = "";                                                         // check all existing nodes
    for(String id : this.nodeIds){                                              // in order to find the one "under" the cursor
       Object[] xy = graph.getNode(id).getArray("xyz");
       double x = this.viewer.getDefaultView().getCamera()                      // transform the graph coordinate into JPanel coordinate (for X)
               .transformGuToPx((Double)xy[0], (Double)xy[1], 0).x;
       double y = this.viewer.getDefaultView().getCamera()                      // transform the graph coordinate into JPanel coordinate (for Y)
               .transformGuToPx((Double)xy[0], (Double)xy[1], 0).y;
       if((x - NODE_SIZE/2 < e.getPoint().x && e.getPoint().x < x + NODE_SIZE/2)// does not take into account overlapping nodes
       && (y - NODE_SIZE/2 < e.getPoint().y && e.getPoint().y < y + NODE_SIZE/2)// and return true for the first one tested
       ){
         nodeId = id;                                                           // this should be the node under the cursor.
       }//if
    }//for
    if(!"".equals(nodeId)){                                                     // if a node has been found
      if(e.getButton() == MouseEvent.BUTTON1){                                  // click on the left button
        if(e.getClickCount() == 2){                                             // two clicks
        ArrayList<String> nodeLinks = new ArrayList<>();
        for(Edge edge : graph.getNode(nodeId).getEdgeSet()){                    // retrieve all the edges attached to the node
          nodeLinks.add(edge.getId());                          
        }
        updateNodesAndLinks(nodeId,nodeLinks);                                  // update the node and its edges to selected state
        this.ihm.selectElement(nodeId);                                         // display info about the selected element on the main IHM
        }
        else if(e.getClickCount() == 1){                                        // one click
        }
      }// if BUTTON1
      if(e.getButton() == MouseEvent.BUTTON3){                                  // click on the right button
        boolean error = false;                  
        if("".equals(firstSelectedForLinkCreation)){                            // there is no beginning for the edge yet
          firstSelectedForLinkCreation = nodeId;                                // so the node under the cursor is set as the beginning  of the next edge
        }
        else{                                                                   // there is a node selected as the beginning of the next edge
          try{  
            addLink(firstSelectedForLinkCreation,nodeId);                       // thus an edge is created between the previously selected node and the currently one
            this.ihm.addPreviousOrNext(firstSelectedForLinkCreation, nodeId, "next");     // echo the edge creation into the scenario
            this.ihm.addPreviousOrNext(nodeId, firstSelectedForLinkCreation, "previous"); // echo the edge creation into the scenario
            firstSelectedForLinkCreation = "";                                  // reset the selection of the beginning of the edge
          }catch(RPGSCException ex){
            firstSelectedForLinkCreation = "";
            error = true;
          }
        } 
        this.ihm.setFirstForLinkCreation(error ? "This edge is already existing..." : firstSelectedForLinkCreation,error);  // inform the main IHM of the edge creation or 
                                                                                                                            // of an error (mainly an existing edge).
      }//if BUTTON3
    }//if nodeId != ""
  }
  
  /****************************************************************************/
  /** Unused Methods Catching Events                                         **/
  /****************************************************************************/
  
  /**
   * Unused methode here for override prupose... for now.
   * @param id 
   */
  @Override
  public void buttonPushed(String id) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   * Unused methode here for override prupose... for now.
   * @param id 
   */
  @Override
  public void buttonReleased(String id) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  /**
   * Unused methode here for override prupose... for now.
   * @param id 
   */
  @Override
  public void viewClosed(String id) {
  }

  /**
   * Unused methode here for override prupose... for now.
   * @param e 
   */
  @Override
  public void mousePressed(MouseEvent e) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   * Unused methode here for override prupose... for now.
   * @param e 
   */
  @Override
  public void mouseEntered(MouseEvent e) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   * Unused methode here for override prupose... for now.
   * @param e 
   */
  @Override
  public void mouseExited(MouseEvent e) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   * Unused methode here for override prupose... for now.
   * @param e 
   */
  @Override
  public void mouseDragged(MouseEvent e) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  /**
   * Unused methode here for override prupose... for now.
   * @param e 
   */
  @Override
  public void mouseMoved(MouseEvent e) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
}
