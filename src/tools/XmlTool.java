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
   * 
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
   * 
   * @param filename
   * @return 
   */
  public Scenario loadScenario(String filename){
    Scenario toReturn = new Scenario();
    File inputFile = new File(scenarioSaves + filename);
    try{
      SAXBuilder saxBuilder = new SAXBuilder();
      Document document = saxBuilder.build(inputFile);
      Element root = document.getRootElement();
      System.out.println("OK");
      toReturn.setTitle(root.getChildText("title"));
      System.out.println("OK");
      List<Element> elements = root.getChild("elements").getChildren();
      System.out.println("OK");
      String elementId;
      ArrayList<String> coresToAdd = new ArrayList<>();
      for(Element element : elements){
        elementId = element.getAttributeValue("id");
        List<Element> cores = element.getChildren();
        for(Element core : cores){
          coresToAdd.add(core.getText());
        }//for core in cores
        toReturn.addElement(elementId, coresToAdd);
      }//for element in elements
    }
    catch(JDOMException | IOException e){
      //e.printStackTrace();
    }//catch
    return toReturn;
  }
  
  /**
   * 
   * @param toSave 
   */
  public void saveScenario(Scenario toSave){
    
    try{
    File outputFile = new File(scenarioSaves + toSave.getTitle().replaceAll(" ", "") + ".xml");
    if (outputFile.exists()) {
      File renamed = new File(outputFile + ".bk");
      outputFile.renameTo(renamed);
      outputFile.delete();
    }
    outputFile.createNewFile();
    
    DataOutputStream stream = new DataOutputStream(new FileOutputStream(outputFile));
    
    // root
    Element scenario = new Element("scenario");
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
    }//for id in wholeScenario

    scenario.addContent(title);
    scenario.addContent(elements);
    //doc.getRootElement().addContent(player);

    XMLOutputter xmlOut = new XMLOutputter();
    xmlOut.setFormat(Format.getPrettyFormat());
    xmlOut.output(doc, stream);
    stream.close();
      
    }// try
    catch (IOException ioe) {
      System.err.println("ECHEC!");
    }
  }
  
}

/*

// physical
        Element age = new Element("age");
        age.setText(Integer.toString(cts.getCharacterAge()));
        character.addContent(age);
        Element weight = new Element("weight");
        weight.setText(Double.toString(cts.getCharacterWeight()));
        character.addContent(weight);
        Element size = new Element("size");
        size.setText(Double.toString(cts.getCharacterSize()));
        character.addContent(size);
        Element gender = new Element("gender");
        gender.setText(cts.isFemale() ? "femme" : "homme");
        character.addContent(gender);

        // attributes
        HashMap<String, Integer> tmp = cts.getAttributes();
        for (String cle : tmp.keySet()) {
          Element tmpElement = new Element(cle);
          tmpElement.setText(Integer.toString(tmp.get(cle)));
          character.addContent(tmpElement);
        }

        // game type specific
        switch (cts.getGameType()) {
          case "Patient 13":
            character.setAttribute(new Attribute("surname", ((P13_Character) cts).getSurname()));
            Element seniority = new Element("seniority");
            character.addContent(seniority);
            seniority.setText(Integer.toString(((P13_Character) cts).getSeniority()));
            Element supervisor = new Element("supervisor");
            character.addContent(supervisor);
            supervisor.setText(((P13_Character) cts).getSupervisor());
            Element room = new Element("room");
            room.setText(((P13_Character) cts).getRoom());
            character.addContent(room);
            Element lineaments = new Element("lineaments");
            for(String cle : ((P13_Character) cts).getLineaments().keySet()){
              Element tmpL = new Element(cle);
              tmpL.setText(Integer.toString(((P13_Character) cts).getLineaments().get(cle)));
              lineaments.addContent(tmpL);
            }
            character.addContent(lineaments);
            break;
            
          case "AD&D":
            break;
            
          case "Feng Shui":
            break;
            
          case "Shadowrun":
            break;
            
          case "Cthulhu":
            break;

          default:
            break;

*/