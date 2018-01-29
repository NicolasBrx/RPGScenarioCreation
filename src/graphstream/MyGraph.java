package graphstream;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.event.MouseInputListener;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

/**
 *
 * @author Nicolas Brax
 */
public class MyGraph implements ViewerListener, MouseInputListener{
  
  private Graph graph;
  private View view;
  private Viewer viewer;
  private ViewerPipe fromViewer;
  private ArrayList<String> nodeIds;
  private ArrayList<String> linkIds;
  
  public MyGraph(){
    System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
    this.graph = new SingleGraph("Scenario visualisation.");
    this.graph.addAttribute("ui.stylesheet", "url('file:" + System.getProperty("user.dir") 
            + "/data/graphstream_ressources/graph_style.css'" + ")");
    this.viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
    this.nodeIds = new ArrayList<>();
    this.linkIds = new ArrayList<>();
  }
  
  public void addNode(String nodeId){
    Node n = graph.addNode(nodeId);
    nodeIds.add(nodeId);
    n.addAttribute("ui.label",nodeId);
    n.addAttribute("ui.class","unselected");
    n.setAttribute("selected?",false);
  }
  
  public void updateNode(String nodeId, boolean selected){
    graph.getNode(nodeId).setAttribute("selected?",selected);
    graph.getNode(nodeId).setAttribute("ui.class", (selected ? "selected" : "unselected"));
  }
  
  public void removeNode(String nodeId){
    graph.removeNode(nodeId);
  }
  
  public void display(){
    viewer.enableAutoLayout();
    this.view = viewer.addDefaultView(true);
    this.view.addMouseListener(this);
  }
  
  public void addLink(String fromId,String toId){
    // TODO: check here if the edge already exists
    Edge e = graph.addEdge(fromId+toId,fromId,toId,true);
    linkIds.add(fromId+toId);
    e.addAttribute("ui.label","toto");
    e.setAttribute("selected?",false);
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
  
  
  ////////////// unused for now
  
  @Override
  public void buttonPushed(String id) {
    System.out.println("Button pushed on node "+id);
    ArrayList<String> nodeLinks = new ArrayList<>();
    for(Edge e : graph.getNode(id).getEdgeSet()){
      nodeLinks.add(e.getId());
    }
    updateNodesAndLinks(id,nodeLinks);
    //TODO: selection onto the main IHM
  }

  @Override
  public void buttonReleased(String id) {
    System.out.println("Button released on node "+id);
  }
  
  @Override
  public void viewClosed(String id) {
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
