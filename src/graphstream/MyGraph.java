package graphstream;

import java.awt.event.MouseEvent;
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
  
  public MyGraph(){
    System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
    this.graph = new SingleGraph("Scenario visualisation.");
    this.graph.addAttribute("ui.stylesheet", "url('file:" + System.getProperty("user.dir") 
            + "/data/graphstream_ressources/graph_style.css'" + ")");
    this.viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
  }
  
  public void addNode(String nodeId){
    Node n = graph.addNode(nodeId);
    n.addAttribute("ui.label",nodeId);
    n.addAttribute("ui.class","unselected");
  }
  
  public void updateNode(String nodeId, boolean selected){
    if(selected){
      graph.getNode(nodeId).addAttribute("ui.class", "selected");
    }
    else{
      graph.getNode(nodeId).addAttribute("ui.class","unselected");
    }
  }
  
  public void removeNode(String nodeId){
    graph.removeNode(nodeId);
  }
  
  public void display(){
    this.view = viewer.addDefaultView(true);
    this.view.addMouseListener(this);
  }
  
  public void addLink(String fromId,String toId){
    // TODO: check here if the edge already exists
    Edge e = graph.addEdge(fromId+toId,fromId,toId,true);
    e.addAttribute("ui.label","toto");
  }
  
  public void updateLink(String linkId, boolean selected){
    if(selected){
      graph.getEdge(linkId).addAttribute("ui.class", "selected");
    }
    else{
      graph.getEdge(linkId).addAttribute("ui.class","unselected");
    }
  }
  
  public void addPipe(){
    this.fromViewer = this.viewer.newViewerPipe();
    fromViewer.addViewerListener(this);
    fromViewer.addSink(graph);
  }
  
  @Override
  public void mouseReleased(MouseEvent e) {
    fromViewer.pump();
  }
  
  
  ////////////// unused for now
  
  @Override
  public void buttonPushed(String id) {
    System.out.println("Button pushed on node "+id);
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
