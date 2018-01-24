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
    this.nextElements = new ArrayList<>();
    this.previousElements = new ArrayList<>();
    this.core = new ArrayList<>();
  }
  
  public ScenarioElement(String id){
    this.elementId = id;
    this.nextElements = new ArrayList<>();
    this.previousElements = new ArrayList<>();
    this.core = new ArrayList<>();
  }
  
  public ScenarioElement(String id, ArrayList<String> core){
    this.elementId = id;
    this.core = core;
    this.nextElements = new ArrayList<>();
    this.previousElements = new ArrayList<>();
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
  
  public void addCore(String toAdd){
    if(!this.core.contains(toAdd)){
      this.core.add(toAdd);
    }
    else{
      // already in
    }
  }
  
  
  
}
