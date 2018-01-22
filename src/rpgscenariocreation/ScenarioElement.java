package rpgscenariocreation;

import java.util.ArrayList;

/**
 *
 * @author Nicolas Brax
 */
public class ScenarioElement {
  
  /**
   * 
   */
  private int elementId;
  
  /**
   * 
   */
  private ArrayList<Integer> previousElements;
  
  /**
   * 
   */
  private ArrayList<Integer> nextElements;
  
  /**
   * 
   */
  private ArrayList<String> core;
  
  
  public ScenarioElement(){
    
  }
  
  public ScenarioElement(int id){
    
  }

  public int getElementId() {
    return elementId;
  }

  public void setElementId(int elementId) {
    this.elementId = elementId;
  }

  public ArrayList<Integer> getPreviousElements() {
    return previousElements;
  }

  public void setPreviousElements(ArrayList<Integer> previousElements) {
    this.previousElements = previousElements;
  }
  
  public void addPreviousElement(int toAdd){
    if(!this.previousElements.contains(toAdd)){
      this.previousElements.add(toAdd);
    }
    else{
      // already in
    }
  }

  public ArrayList<Integer> getNextElements() {
    return nextElements;
  }

  public void setNextElements(ArrayList<Integer> nextElements) {
    this.nextElements = nextElements;
  }
  
  public void addNextElement(int toAdd){
    if(!this.nextElements.contains(toAdd)){
      this.nextElements.add(toAdd);
    }
    else{
      // already in
    }
  }

  public ArrayList<String> getCore() {
    return core;
  }

  public void setCore(ArrayList<String> core) {
    this.core = core;
  }
  
  public void addCoreElement(String toAdd){
    if(!this.core.contains(toAdd)){
      this.core.add(toAdd);
    }
    else{
      // already in
    }
  }
  
  
  
}
