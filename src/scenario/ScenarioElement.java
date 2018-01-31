package scenario;

import java.util.ArrayList;

/**
 * This class represents an unique element of a scenario. An element is defined
 * as a sequence of actions that are taken in order to reach a local objective.
 * 
 * Each element has a unique identifier within the scenario. It can be 
 * preceeded and/or followed by any number of other elements, or by itself in
 * case of a repetitive element.
 * 
 * @author Nicolas Brax
 */
public class ScenarioElement {
  
  /****************************************************************************/
  /** Private Attributes                                                     **/
  /****************************************************************************/
  
  /**
   * The unique element id within the scenario.
   */
  private String elementId;
  
  /**
   * The list of previous element within the scenario.
   */
  private ArrayList<String> previousElements;
  
  /**
   * The list of following element within the scenario.
   */
  private ArrayList<String> nextElements;
  
  /**
   * The core sequence of action of the element.
   */
  private ArrayList<String> core;
  
  
  /****************************************************************************/
  /** Constructor Methods                                                    **/
  /****************************************************************************/
  
  /**
   * Default Constructor
   */
  public ScenarioElement(){
    this.nextElements = new ArrayList<>();
    this.previousElements = new ArrayList<>();
    this.core = new ArrayList<>();
  }
  
  /**
   * Constructor with only an id set.
   * 
   * @param id the id of the element.
   */
  public ScenarioElement(String id){
    this.elementId = id;
    this.nextElements = new ArrayList<>();
    this.previousElements = new ArrayList<>();
    this.core = new ArrayList<>();
  }
  
  /**
   * Whole constructor with the id and the sequence of actions defined for 
   * this element.
   * 
   * @param id   the id of the element within the scenario.
   * @param core the sequence of actions to be taken in this element.
   */
  public ScenarioElement(String id, ArrayList<String> core){
    this.elementId = id;
    this.core = core;
    this.nextElements = new ArrayList<>();
    this.previousElements = new ArrayList<>();
  }
  
  
  /****************************************************************************/
  /** Getter and Setter Methods                                              **/
  /****************************************************************************/

  /**
   * Give the id of the element within the scenario/
   * 
   * @return the id of the element.
   */
  public String getElementId() {
    return elementId;
  }

  /**
   * Modify the unique id of the element within the scenario.
   * 
   * @param elementId the new idenitifcation.
   */
  public void setElementId(String elementId) {
    this.elementId = elementId;
  }

  /**
   * Give the previous elements of the current element within the scenario.
   * 
   * @return null if there is none, an array list of id otherwise
   */
  public ArrayList<String> getPreviousElements() {
    if(this.previousElements.isEmpty()){
      return null;
    }
    else{
      return previousElements;
    }
  }

  /**
   * Modify the previous elements of the current element within the scenario.
   * This set all the previous id in one run.
   * 
   * @param previousElements the list of id to take as previous elements
   */
  public void setPreviousElements(ArrayList<String> previousElements) {
    this.previousElements = previousElements;
  }
  
  /**
   * Add an id as a previous element of the current one within the scenario.
   * 
   * @param toAdd the id of the element to add as a previous one.
   */
  public void addPreviousElement(String toAdd){
    if(!this.previousElements.contains(toAdd)){
      this.previousElements.add(toAdd);
    }
    else{
      // TODO: error or warning
    }
  }
  
  /**
   * Remove an element from the list of previous elements of the current one.
   * 
   * @param elementId the element to remove
   */
  void removePrevious(String elementId) {
    this.previousElements.remove(elementId);
  }
  
  /**
   * Return true if the current element have beforehand elements within the
   * current scenario.
   * 
   * @return true if the element have previous ones in the scenario.
   */
  public boolean hasPrevious(){
    return (this.previousElements != null && !this.previousElements.isEmpty());
  }

  /**
   * Give the next elements of the current element within the scenario.
   * 
   * @return null if there is none, an array list of id otherwise
   */
  public ArrayList<String> getNextElements() {
    if(this.nextElements.isEmpty()){
      return null;
    }
    else{
      return nextElements;
    }
  }

  /**
   * Modify the next elements of the current element within the scenario.
   * This set all the next id in one run.
   * 
   * @param nextElements the list of id to take as previous elements
   */
  public void setNextElements(ArrayList<String> nextElements) {
    this.nextElements = nextElements;
  }
  
  /**
   * Add an id as a next element of the current one within the scenario.
   * 
   * @param toAdd the id of the element to add as a next one.
   */
  public void addNextElement(String toAdd){
    if(!this.nextElements.contains(toAdd)){
      this.nextElements.add(toAdd);
    }
    else{
      // TODO: error or warning
    }
  }
  
  /**
   * Remove an element from the list of next elements of the current one.
   * 
   * @param elementId the element to remove
   */
  void removeNext(String elementId) {
    this.nextElements.remove(elementId);
  }
  
  /**
   * Return true if the current element have consequence elements within the
   * current scenario.
   * 
   * @return true if the element have next ones in the scenario.
   */
  public boolean hasNext(){
    return (this.nextElements != null && !this.nextElements.isEmpty());
  }

  /**
   * Give all the actions to take in order to fulfill the current element within
   * the scenario.
   * 
   * @return an array list of all the core action to take for the current element.
   */
  public ArrayList<String> getCore() {
    return core;
  }

  /**
   * Modify the core actions to take in order to complete the current element
   * within the scenario. All the action are taken in one row in this method.
   * 
   * @param core all the actions to take.
   */
  public void setCore(ArrayList<String> core) {
    this.core = core;
  }
  
  /**
   * Add one action to take in order to complete the current element within
   * the scenario.
   * 
   * @param toAdd one action to add.
   */
  public void addCore(String toAdd){
    if(!this.core.contains(toAdd)){
      this.core.add(toAdd);
    }
  }
}
