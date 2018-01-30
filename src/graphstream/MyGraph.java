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
 * Handle the graph on graphstream format as well as mouselistener on it (select node,
 * contextual menu, drawn links, ...)
 * 
 * @author Nicolas Brax
 */
public class MyGraph implements ViewerListener, MouseInputListener{
  
  private final int NODE_SIZE = 15;
  private final int LINK_SIZE = 10;
  
  private Graph graph;
  private View view;
  private Viewer viewer;
  private ViewerPipe fromViewer;
  private ArrayList<String> nodeIds;
  private ArrayList<String> linkIds;
  private RPGScenarioCreationIHM ihm;
  private String firstSelectedForLinkCreation = "";
  
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
  
  public void addNode(String nodeId){
    Node n = graph.addNode(nodeId);
    nodeIds.add(nodeId);
    n.addAttribute("ui.label",nodeId);
    n.addAttribute("ui.size",NODE_SIZE);
    n.addAttribute("ui.class","unselected");
    n.setAttribute("selected?",false);
  }
  
  public void updateNode(String nodeId, boolean selected){
    graph.getNode(nodeId).setAttribute("selected?",selected);
    graph.getNode(nodeId).setAttribute("ui.class", (selected ? "selected" : "unselected"));
  }
  
  public void removeNode(String nodeId){
    ArrayList<String> linksToRemove = new ArrayList<>();
    for(Edge edge : graph.getNode(nodeId).getEdgeSet()){
      linksToRemove.add(edge.getId());
    }
    for(String s : linksToRemove){
      linkIds.remove(s);
      graph.removeEdge(s);
    }
    nodeIds.remove(nodeId);
    graph.removeNode(nodeId);
  }
  
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
  
  public void display(){
    this.viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
    this.viewer.enableAutoLayout();
    this.view = this.viewer.addDefaultView(true);
    this.view.addMouseListener(this);
  }
  
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
  
  public void updateLink(String linkId, boolean selected){
    graph.getEdge(linkId).setAttribute("selected?",selected);
    graph.getEdge(linkId).setAttribute("ui.class", (selected ? "selected" : "unselected"));
  }
  
  public void updateNodesAndLinks(String nodeId, ArrayList<String>linkIds){
    for(String s : this.nodeIds){
      updateNode(s,s.equalsIgnoreCase(nodeId));
    }
    for(String s : this.linkIds){
      updateLink(s,linkIds.contains(s));
    }
  }
  
  public void addPipe(){
    this.fromViewer = this.viewer.newViewerPipe();
    fromViewer.addViewerListener(this);
    fromViewer.addSink(graph);
  }
  
  public boolean isSelected(String nodeId){
    return graph.getNode(nodeId).hasAttribute("selected?");
  }
  
  @Override
  public void mouseReleased(MouseEvent e) {
    fromViewer.pump();
  }
  
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
  
  // unused methods... for now...
  @Override
  public void buttonPushed(String id) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void buttonReleased(String id) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  @Override
  public void viewClosed(String id) {
  }

  @Override
  public void mousePressed(MouseEvent e) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void mouseExited(MouseEvent e) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  
  
}
