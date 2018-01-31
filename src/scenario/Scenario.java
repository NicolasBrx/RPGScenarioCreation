package scenario;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class allows to handle a scenario for a tabletop rpg. A scenario is 
 * represented by a sequence of unique elements, each one representing a sequence
 * of actions to follow.
 * 
 * @author Nicolas Brax
 */
public class Scenario {
  
  /****************************************************************************/
  /** Private Attributes                                                     **/
  /****************************************************************************/
  
  /**
   * The title of the scenario as defined by the user.
   */
  private String title;
  
  /**
   * A map of all the element constituting the current scenario. Each element is
   * referenced by a unique identificator within the scenario. This id can be
   * generated by the scenario.
   */
  private HashMap<String,ScenarioElement> scenario;
  
  /**
   * A counter of element used to generate a unique id for the element of 
   * the scenario.
   */
  private static long idCounter = 0;
  
  
  /****************************************************************************/
  /** Constructor Methods                                                    **/
  /****************************************************************************/
  
  /**
   * Default Consructor.
   * Set the title to an empty character string and initialize the element map.
   */
  public Scenario(){
    this.title = "";
    this.scenario = new HashMap<>();
  }
  
  
  /****************************************************************************/
  /** Getter,Setter,Check and Removal Methods                                **/
  /****************************************************************************/
  
  /**
   * Give the scenario element identified by the id given as a parameter.
   * 
   * @param elementId The id of the element to retrieve.
   * @return The scenario element with the correponding id.
   */
  public ScenarioElement getElement(String elementId){
    return this.scenario.get(elementId);
  }
  
  /**
   * Return a Map of all the scenario elements of the current scenario, each one 
   * identified and accessed by its id. The map is constituted as follows:
   * <elementId,scenarioElement>
   * 
   * @return A HashMap containing all the elements of the scenario.
   */
  public HashMap<String,ScenarioElement> getWholeScenario(){
    return this.scenario;
  }
  
  /**
   * This method allows the addition of an element by specifying its id and its
   * core sequence of actions.
   * 
   * @param elementId The id of the element.
   * @param core The core sequence of actions.
   * @return The id of the element added to the scenario.
   */
  public String addElement(String elementId, ArrayList<String> core){
    this.scenario.put(elementId, new ScenarioElement(elementId,core));
    return elementId;
  }
  
  /**
   * This method allows the addition of an element by specifying its its
   * core sequence of actions. The id is auto generated by the method.
   * 
   * @param core The core sequence of actions.
   * @return The id of the element added to the scenario.
   */
  public String addElement(ArrayList<String> core){
    String newId = createID();
    this.scenario.put(newId, new ScenarioElement(newId,core));
    return newId;
  }
  
  /**
   * Remove an element from the current scenario. The element is specified by
   * its id. The method also check the previous and next elements of the 
   * specified one in order to remove its reference from them.
   * 
   * @param elementId The id of the element to remove.
   */
  public void removeElement(String elementId){
    if(this.scenario.get(elementId).hasNext()){                                 // if the element has next elements
      for(String next : this.scenario.get(elementId).getNextElements()){
        this.scenario.get(next).removePrevious(elementId);                      // remove it as a previous from the next ones
      }
    }
    if(this.scenario.get(elementId).hasPrevious()){                             // if the element has previous elements
      for(String previous : this.scenario.get(elementId).getPreviousElements()){
        this.scenario.get(previous).removeNext(elementId);                      // remove it as a next from the previous ones
      }
    }
    this.scenario.remove(elementId);                                            // remove the element
  }
  
  /**
   * Give the current title of the scenario.
   * 
   * @return The current title of the scenario.
   */
  public String getTitle(){
    return this.title;
  }
  
  /**
   * Modifies the title of the scenario according to the one given as a 
   * parameter.
   * 
   * @param title The new title of the scenario.
   */
  public void setTitle(String title){
    this.title = title;
  }
  
  
  /****************************************************************************/
  /** Tool Methods                                                           **/
  /****************************************************************************/
  
  /**
   * Increment a counter in order to provide a unique identification for a new
   * element. The ID are set in a range from 001 to 999.
   *
   * @return A new unique identification string.
   */
  public static synchronized String createID()
  {
    return ("Element" + (idCounter++ < 9 ? "00":(idCounter < 100 ? "0" : "")) 
            + String.valueOf(idCounter));
  } 
  
  /**
   * This function allows to check if an identification string is already in use
   * within the current scenario.
   * 
   * @param elementId The element id to check.
   * @return True if it exists in the element map, false otherwise.
   */
  public boolean existInScenario(String elementId){
    return this.scenario.containsKey(elementId);
  }
  
  
  /**
   * A string version of the current scenario. This is dedicated to testing.
   * 
   * @return A string version of the current sceanrio.
   */
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
