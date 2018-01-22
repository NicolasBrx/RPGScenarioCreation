package rpgscenariocreation;

import java.util.HashMap;

/**
 *
 * @author Nicolas Brax
 */
public class Scenario {
  
  private HashMap<Integer,ScenarioElement> scenario;
  
  public Scenario(){
    
  }
  
  public ScenarioElement getElement(int elementId){
    return scenario.get(elementId);
  }
  
  public HashMap<Integer,ScenarioElement> getWholeScenario(){
    return this.scenario;
  }
  
  public void addElement(ScenarioElement element){
    this.scenario.put(element.getElementId(), element);
  }
  
}
