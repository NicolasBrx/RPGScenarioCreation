package tools;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import scenario.Scenario;

/**
 * This class is dedicated to the handling of several XML files in order to
 * properly run the RPGScenarioCreation project.
 * 
 * These XML files are used to:
 * - save and load a scenario;
 * - generate a random scenario based on several items
 * 
 * As these files are designed to be part of the project, their names are
 * hard coded and should not be modified. With a little chance, the project
 * will give the ability to modify theses files to stick to the user preference.
 * Maybe.
 * 
 * @author Nicolas Brax
 */
public class XmlTool {
  
  /**
   * Relatively to the project, the path of the folder that contains all the 
   * data files.
   */
  private final String dataPath = System.getProperty("user.dir") + "/data/";
  
  /**
   * The path of the folder that contains all the scenario saved and that can
   * be loaded.
   */
  private final String scenarioSaves = dataPath + "save_data/";
  
  /****************************************************************************/
  /** Constructor Methods                                                    **/
  /****************************************************************************/
  
  /**
   * The Default Constructor that does nothing.
   */
  public XmlTool(){
    // things to do ?
  }
  
  
  /****************************************************************************/
  /** Tool Methods                                                           **/
  /****************************************************************************/
  
  /**
   * This method provides a list of all the scenario stored in the save folder.
   * Each file is named after the title of the scenario so the file name is 
   * then deconstructed in order to provide the right scenario name.
   * 
   * @return An array list containing all the scenario in the save folder.
   */
  public ArrayList<String> getAllScenarioTitles(){
    ArrayList<String> toReturn = new ArrayList<>();
    for(File f : new File(scenarioSaves).listFiles()){
      if(f.isFile()){
        String s = f.getName().substring(0,f.getName().lastIndexOf('.'));   // remove the file extension
        String[] parts = s.split("(?=\\p{Upper})");                         // cut the string on upper case
        String notRawName = "";                                             
        for(int i = 0; i < parts.length ; ++i){
          notRawName += parts[i] + (i == (parts.length - 1) ? "" : " ");    // add each part to the string to return
        }
        toReturn.add(notRawName);
      }
    }
    
    return toReturn;
  }
  
  /**
   * This function retrieve the different elements of a scenario and load them in 
   * the scenario that is returned.
   * 
   * It used the name of the scenario as a parameter. This name is then used
   * to rebuild the filename.
   * 
   * @param notRawFilename the name of the scenario.
   * @return the scenario loaded after the name in parameter.
   */
  public Scenario loadScenario(String notRawFilename){
    Scenario toReturn = new Scenario();                                             // the element to return
    String[] part = notRawFilename.split(" ");                                      // remove all white space in the scenario name
    String rawFilename = "";
    for(int i = 0 ; i < part.length ; ++i){                                         // for each word in the scenario name
      rawFilename += (part[i].substring(0,1).toUpperCase() + part[i].substring(1)); // change the first letter to upper case
                                                                                    // and concatene it
    }
    File inputFile = new File(scenarioSaves + rawFilename + ".xml");                // add the file extension
    
    /* XML BLOCK */ 
    try{
      SAXBuilder saxBuilder = new SAXBuilder();
      Document document = saxBuilder.build(inputFile);
      Element root = document.getRootElement();
      toReturn.setTitle(root.getChildText("title"));
      List<Element> elements = root.getChild("elements").getChildren();
      for(Element element : elements){
        String elementId;
        ArrayList<String> coresToAdd = new ArrayList<>();
        elementId = element.getAttributeValue("id");
        List<Element> cores = element.getChild("cores").getChildren();
        for(Element core : cores){
          coresToAdd.add(core.getText());
        }//for core in cores
        toReturn.addElement(elementId, coresToAdd);
        List<Element> previouses = element.getChild("previousIds").getChildren();
        for(Element previous : previouses){
          toReturn.getElement(elementId).addPreviousElement(previous.getText());
        }
        List<Element> nexts = element.getChild("nextIds").getChildren();
        for(Element next : nexts){
          toReturn.getElement(elementId).addNextElement(next.getText());
        }
      }//for element in elements
    }
    catch(JDOMException | IOException e){
      //e.printStackTrace();
    }//catch
    /* END of XML BLOCK */
    
    return toReturn;
  }
  
  /**
   * This function save a scenario in a file named after the scenario title/name.
   * 
   * @param toSave the scenario to save.
   */
  public void saveScenario(Scenario toSave){
    
    String[] part = toSave.getTitle().split(" ");                                   // remove all white space in the scenario name
    String rawFilename = "";
    for(int i = 0 ; i < part.length ; ++i){                                         // for each word in the scenario name
      rawFilename += (part[i].substring(0,1).toUpperCase() + part[i].substring(1)); // change the first letter to upper case
                                                                                    // and concatene it
    }
    File outputFile = new File(scenarioSaves + rawFilename + ".xml");               // add the file extension
      
    /* XML BLOCK */
    try{
      if (outputFile.exists()) {
       File renamed = new File(outputFile + ".bk");
       outputFile.renameTo(renamed);
       outputFile.delete();
      }
      outputFile.createNewFile();
      DataOutputStream stream = new DataOutputStream(new FileOutputStream(outputFile));
      Element scenario = new Element("scenario");   // root element
      Document doc = new Document(scenario);
      Element title = new Element("title");
      title.setText(toSave.getTitle());
      Element elements = new Element("elements");
      for(String id : toSave.getWholeScenario().keySet()){
        Element element = new Element("element").setAttribute("id", id);
        for(String sCore : toSave.getElement(id).getCore()){
          Element core = new Element("core").setText(sCore);
          element.addContent(core);
        }//for core in getCore
        elements.addContent(element);
        //TODO: save next and before elements if they are ones
      }//for id in wholeScenario
      scenario.addContent(title);
      scenario.addContent(elements);
      XMLOutputter xmlOut = new XMLOutputter();
      xmlOut.setFormat(Format.getPrettyFormat());
      xmlOut.output(doc, stream);
      stream.close();
    }// try
    catch (IOException ioe) {
      System.err.println("ECHEC!");
    }
    /* END of XML BLOCK */
  }
  
}