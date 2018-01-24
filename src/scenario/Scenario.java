package scenario;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Nicolas Brax
 */
public class Scenario {
  
  private String title;
  private HashMap<String,ScenarioElement> scenario;
  
  public Scenario(){
    this.title = "";
    this.scenario = new HashMap<>();
  }
  
  public ScenarioElement getElement(String elementId){
    return scenario.get(elementId);
  }
  
  public HashMap<String,ScenarioElement> getWholeScenario(){
    return this.scenario;
  }
  
  public void addElement(String elementId, ArrayList<String> core){
    this.scenario.put(elementId, new ScenarioElement(elementId,core));
  }
  
  public void removeElement(String elementId){
    this.scenario.remove(elementId);
  }
  
  public String getTitle(){
    return this.title;
  }
  
  public void setTitle(String title){
    this.title = title;
  }
  
  @Override
  public String toString(){
    String toReturn = this.title + "\r\n";
    for(String id : this.scenario.keySet()){
      toReturn += "ElementId: " + id + "\r\n";
      for(String core : this.scenario.get(id).getCore()){
        toReturn += " - " + core + "\r\n";
      }
      toReturn += "\r\n------\r\n";
    }
    return toReturn;
  }
  
}
