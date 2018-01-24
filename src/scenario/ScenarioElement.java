package scenario;

import java.util.ArrayList;

/**
 *
 * @author Nicolas Brax
 */
public class ScenarioElement {
  
  /**
   * 
   */
  private String elementId;
  
  /**
   * 
   */
  private ArrayList<String> previousElements;
  
  /**
   * 
   */
  private ArrayList<String> nextElements;
  
  /**
   * 
   */
  private ArrayList<String> core;
  
  
  public ScenarioElement(){
    
  }
  
  public ScenarioElement(String id){
    
  }
  
  public ScenarioElement(String id, ArrayList<String> core){
    
  }

  public String getElementId() {
    return elementId;
  }

  public void setElementId(String elementId) {
    this.elementId = elementId;
  }

  public ArrayList<String> getPreviousElements() {
    return previousElements;
  }

  public void setPreviousElements(ArrayList<String> previousElements) {
    this.previousElements = previousElements;
  }
  
  public void addPreviousElement(String toAdd){
    if(!this.previousElements.contains(toAdd)){
      this.previousElements.add(toAdd);
    }
    else{
      // already in
    }
  }

  public ArrayList<String> getNextElements() {
    return nextElements;
  }

  public void setNextElements(ArrayList<String> nextElements) {
    this.nextElements = nextElements;
  }
  
  public void addNextElement(String toAdd){
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
