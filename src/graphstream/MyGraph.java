package graphstream;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

/**
 *
 * @author Nicolas Brax
 */
public class MyGraph {
  
  private Graph graph;
  
  public MyGraph(){
    System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
    this.graph = new SingleGraph("Scenario visualisation.");
    this.graph.addAttribute("ui.stylesheet", "url('file:" + System.getProperty("user.dir") 
            + "/data/graphstream_ressources/graph_style.css'" + ")");
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
    graph.display();
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
  
}
