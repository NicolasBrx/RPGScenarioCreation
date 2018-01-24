package scenario;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Nicolas Brax
 */
public class Scenario {
  
  private HashMap<String,ScenarioElement> scenario;
  
  public Scenario(){
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
  
}
