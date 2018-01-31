package ihm;

import graphstream.MyGraph;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import scenario.Scenario;
import tools.RPGSCException;
import tools.XmlTool;

/**
 * Main user interface class for creation and management of a Scenario for
 * tabletop rpg or various history.
 * 
 * @author Nicolas Brax
 */
public class RPGScenarioCreationIHM extends javax.swing.JFrame {

  /****************************************************************************/
  /** Private Attributes                                                     **/
  /****************************************************************************/
  
  /**
   * The current scenario to be created and displayed into the user interface.
   */
  private Scenario myScenario = new Scenario();
  
  /**
   * A variable to handle the several id to be displayed into the left list of
   * the user interface.
   */
  private DefaultListModel elementList = new DefaultListModel();
  
  /**
   * An attribute to indicate the scenario state, i.e. wether it is saved or not.
   */
  private boolean saveNeeded;
  
  /**
   * The object that contains the graph representing the current loaded or
   * created scenario.
   */
  private MyGraph scenarioGraph;
  
  
  /****************************************************************************/
  /** Constructor Methods                                                    **/
  /****************************************************************************/
  
  /**
   * Default Constructor
   * Creates new form RPGScenarioCreationIHM
   */
  public RPGScenarioCreationIHM() {
    initComponents();                                                           // auto-generated
    
    this.setTitle("RPGScenario Management v.1.0.0");                            // title of the frame and software
    URL iconURL = getClass().getResource("favicon.png");                        // icon for the frame
    ImageIcon icon = new ImageIcon(iconURL);
    this.setIconImage(icon.getImage());
    
    XmlTool xml = new XmlTool();                                                // retrieving all the available scenarii
    for(String s : xml.getAllScenarioTitles()){
      jcbbScenarioChoice.addItem(s);                                            // to be put into a combo box
    }
    
    jlblScenarioTitle.setText("");                                              // no scenario is loaded yet
    jlblLinkCreation.setVisible(false);                                         // this label is just a visual support
                                                                                // while creating links between nodes
    
    scenarioGraph = new MyGraph(this);                                          // graph viewer initialisation
    jpanelGraphView.setLayout(new GridLayout());
    jpanelGraphView.setMaximumSize(new Dimension(495,600));
    jpanelGraphView.setMinimumSize(new Dimension(495,600));
    jpanelGraphView.setPreferredSize(new Dimension(495,600));
    jpanelGraphView.add(scenarioGraph.getView(495,600));
    pack();
    repaint();
    
    jcbbDifficulty.setEnabled(false);                                           // because it is not implemented yet.
    jtextNbElement.setEnabled(false);                                           // because it is not implemented yet.
    jbtnRandomGeneration.setEnabled(false);                                     // because it is not implemented yet.
    
    this.saveNeeded = false;                                                    // initial state, no save is needed obviously
  }
  
  /**
   * Give the current scenario related to the launched user interface.
   * 
   * @return the current scenario
   */
  public Scenario getScenario(){
    return this.myScenario;
  }
  
  /**
   * Save the current scenario into an xml file named after the title of the
   * scenario. Pretty straightforward.
   */
  public void saveScenario(){
    XmlTool xml = new XmlTool();
    xml.saveScenario(this.myScenario);
    save(false);                                                                // scenario is saved
  }
  
  /**
   * Load data of the element with the given id into the left side of the UI.
   * 
   * @param id the id of the element to display.
   */
  public void selectElement(String id){
    jtxtElementId.setText(this.myScenario.getElement(id).getElementId());
    jtxtElementCore.setText(this.myScenario.getElement(id).getCore().get(0));
    Document doc = jtxtElementCore.getDocument();
    try{
      doc.remove(0, doc.getLength());
    }catch(BadLocationException e){e.printStackTrace();}
    for(String s : this.myScenario.getElement(id).getCore()){
      try{
        doc.insertString(doc.getLength(),s,null);
        doc.insertString(doc.getLength(),"\r\n",null);
      }catch(BadLocationException e){e.printStackTrace();}
    }//for s in getCore
  }
  
  /**
   * Add a previous or a next element tothe element with the elementId id. This
   * method uses a string parameter to indicate if the element to add is previous
   * or next to the current one.
   * This method is called from the MyGraph class that handles graph visualisation
   * and link addition using the user interface.
   * 
   * @param elementId the id of the element to update
   * @param PNId      the id to add as a previous or a next element
   * @param mode      "next" or "previous" according to what is added
   */
  public void addPreviousOrNext(String elementId, String PNId, String mode){
    if(mode.equalsIgnoreCase("previous")){
      this.myScenario.getElement(elementId).addPreviousElement(PNId);
    }
    else if(mode.equalsIgnoreCase("next")){
      this.myScenario.getElement(elementId).addNextElement(PNId);
    }
    else{
      // TODO: error or warning
    }
    save(true);                                                                 // the scenario as been modified
  }
  
  /**
   * This method allows the MyGraph class to inform the user that one node
   * has been selected and is registered as the first node in the upcoming
   * edge creation.
   * This method is just and only a support for visual aid and error.
   * 
   * @param nodeId the id of the node, aka the element, that is considered 
   *                as the first element
   * @param error  if an error is thrown, it is displayed here.
   */
  public void setFirstForLinkCreation(String nodeId, boolean error){
    if(error){
      jlblLinkCreation.setVisible(true);
      jlblLinkCreation.setText(nodeId);
    }
    else{
      if(nodeId.equals("")){
        jlblLinkCreation.setVisible(false);
      }
      else{
        jlblLinkCreation.setVisible(true);
        jlblLinkCreation.setText("First element selected for link creation: " + nodeId);
      }
    }
  }
  
  /**
   * Tool method to handle save indication of the scenario. This just set a boolean
   * attribute of the class as well as the frame title for visual indication
   * 
   * @param save indicate if the scenario needs to be saved (true) or no (false)
   */
  public void save(boolean save){
    this.saveNeeded = save;
    if(save){
      if(!this.getTitle().contains("*")){
        this.setTitle("*" + this.getTitle());
      }  
    }
    else{
      this.setTitle(this.getTitle().substring(1));
    }
  }

  /****************************************************************************/
  /** The generated methods from NetBeans Swing Interface                    **/
  /****************************************************************************/
  
  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jpanelGraphView = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jtxtElementId = new javax.swing.JTextField();
    jScrollPane1 = new javax.swing.JScrollPane();
    jtxtElementCore = new javax.swing.JTextArea();
    jScrollPane2 = new javax.swing.JScrollPane();
    jlistElement = new javax.swing.JList<>();
    jbtnAddElement = new javax.swing.JButton();
    jbtnRemoveElement = new javax.swing.JButton();
    jbtnRandomGeneration = new javax.swing.JButton();
    jLabel3 = new javax.swing.JLabel();
    jtextNbElement = new javax.swing.JTextField();
    jLabel4 = new javax.swing.JLabel();
    jcbbDifficulty = new javax.swing.JComboBox<>();
    jcbbScenarioChoice = new javax.swing.JComboBox<>();
    jLabel5 = new javax.swing.JLabel();
    jLabel6 = new javax.swing.JLabel();
    jtextScenarioTitle = new javax.swing.JTextField();
    jbtnLoadScenario = new javax.swing.JButton();
    jButton1 = new javax.swing.JButton();
    jbtnQuit = new javax.swing.JButton();
    jlblScenarioTitle = new javax.swing.JLabel();
    jButton3 = new javax.swing.JButton();
    jlblLinkCreation = new javax.swing.JLabel();
    jMenuBar1 = new javax.swing.JMenuBar();
    jMenuHelp = new javax.swing.JMenu();
    Instruction = new javax.swing.JMenuItem();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setMinimumSize(new java.awt.Dimension(1024, 677));
    setResizable(false);

    jpanelGraphView.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

    javax.swing.GroupLayout jpanelGraphViewLayout = new javax.swing.GroupLayout(jpanelGraphView);
    jpanelGraphView.setLayout(jpanelGraphViewLayout);
    jpanelGraphViewLayout.setHorizontalGroup(
      jpanelGraphViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 0, Short.MAX_VALUE)
    );
    jpanelGraphViewLayout.setVerticalGroup(
      jpanelGraphViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 596, Short.MAX_VALUE)
    );

    jLabel1.setText("Element Id: ");

    jLabel2.setText("Element Core: ");

    jtxtElementCore.setColumns(20);
    jtxtElementCore.setLineWrap(true);
    jtxtElementCore.setRows(5);
    jScrollPane1.setViewportView(jtxtElementCore);

    jlistElement.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jlistElementMouseClicked(evt);
      }
    });
    jScrollPane2.setViewportView(jlistElement);

    jbtnAddElement.setText("Add Element");
    jbtnAddElement.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbtnAddElementActionPerformed(evt);
      }
    });

    jbtnRemoveElement.setText("Remove Element");
    jbtnRemoveElement.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbtnRemoveElementActionPerformed(evt);
      }
    });

    jbtnRandomGeneration.setText("Random Generation");

    jLabel3.setText("Number of Element:");

    jLabel4.setText("Difficulty:");

    jcbbDifficulty.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Piece of Cake", "Easy", "Normal", "Hard", "Hell on Earth" }));

    jLabel5.setText("Choose a Scenario or make a new one.");

    jLabel6.setText("Scenario Title: ");

    jbtnLoadScenario.setText("Load Scenario");
    jbtnLoadScenario.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbtnLoadScenarioActionPerformed(evt);
      }
    });

    jButton1.setText("Save Scenario");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    jbtnQuit.setText("Quit");
    jbtnQuit.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jbtnQuitActionPerformed(evt);
      }
    });

    jlblScenarioTitle.setText("jLabel7");
    jlblScenarioTitle.setMaximumSize(new java.awt.Dimension(495, 16));
    jlblScenarioTitle.setMinimumSize(new java.awt.Dimension(495, 16));
    jlblScenarioTitle.setPreferredSize(new java.awt.Dimension(495, 16));

    jButton3.setText("Clean Fields");
    jButton3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton3ActionPerformed(evt);
      }
    });

    jlblLinkCreation.setText("First element for link creation: ");
    jlblLinkCreation.setMaximumSize(new java.awt.Dimension(495, 16));
    jlblLinkCreation.setMinimumSize(new java.awt.Dimension(495, 16));
    jlblLinkCreation.setPreferredSize(new java.awt.Dimension(495, 16));

    jMenuHelp.setText("Help");

    Instruction.setText("Instructions");
    Instruction.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        InstructionActionPerformed(evt);
      }
    });
    jMenuHelp.add(Instruction);

    jMenuBar1.add(jMenuHelp);

    setJMenuBar(jMenuBar1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
            .addGroup(layout.createSequentialGroup()
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel1)
                .addComponent(jLabel6))
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jtxtElementId, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jtextScenarioTitle)))
            .addComponent(jLabel2)
            .addComponent(jLabel5)
            .addComponent(jcbbScenarioChoice, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jbtnLoadScenario, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbtnAddElement, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE))
              .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jbtnRemoveElement, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
          .addComponent(jbtnRandomGeneration, javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jLabel3)
              .addComponent(jLabel4))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
              .addComponent(jcbbDifficulty, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jtextNbElement, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jlblScenarioTitle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jpanelGraphView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jlblLinkCreation, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
          .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
            .addGap(0, 0, Short.MAX_VALUE)
            .addComponent(jbtnQuit)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(199, 199, 199)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jbtnQuit)
              .addComponent(jlblLinkCreation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
          .addGroup(layout.createSequentialGroup()
            .addGap(17, 17, 17)
            .addComponent(jLabel5)
            .addGap(18, 18, 18)
            .addComponent(jcbbScenarioChoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jbtnLoadScenario)
            .addGap(71, 71, 71)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel6)
              .addComponent(jtextScenarioTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel1)
              .addComponent(jtxtElementId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel2)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jbtnAddElement, javax.swing.GroupLayout.Alignment.TRAILING)
              .addComponent(jbtnRemoveElement))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jButton1)
              .addComponent(jButton3))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel3)
              .addComponent(jtextNbElement, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
              .addComponent(jLabel4)
              .addComponent(jcbbDifficulty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
            .addComponent(jbtnRandomGeneration)
            .addGap(19, 19, 19))
          .addGroup(layout.createSequentialGroup()
            .addComponent(jlblScenarioTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jpanelGraphView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(34, 34, 34)))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jbtnAddElementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAddElementActionPerformed
    if(this.myScenario.getTitle().isEmpty()){                                   // if the scenario has no title stored
      this.myScenario.setTitle(jtextScenarioTitle.getText());
    }

    ArrayList<String> tmpCore = new ArrayList<>();                              // retrieve the core components of an element
    Document doc = jtxtElementCore.getDocument();
    try{
      String tmp = doc.getText(0,doc.getLength());
      String[] parts = tmp.split("\n");
      for(int i = 0 ; i < parts.length ; ++i){
        tmpCore.add(parts[i]);
      }
    }catch(BadLocationException e){e.printStackTrace();}// TODO: better error
    
    String id;                                                                  // temporary String for id use
    boolean created = false;                                                    // temporary boolean to know if element has been added
    if(jtxtElementId.getText().isEmpty()){                                      // if the id is not set
      id = this.myScenario.addElement(tmpCore,false);                           // one will be auto generated
      if(id.equalsIgnoreCase("already existing")){                              // element already exists in the scenario
        int dialogButton = JOptionPane.YES_NO_OPTION;                           // propose to do force erase the existing element
        int dialogResult = JOptionPane.showConfirmDialog (null, "Element is already existing, force replace?","Warning",dialogButton);
        if(dialogResult == JOptionPane.YES_OPTION){
          id = this.myScenario.addElement(tmpCore,true);
          created = true;
        }
      }//if already existing element
      else{
        created = true;
      }
    }
    else{
      id = this.myScenario.addElement(jtxtElementId.getText(),tmpCore,false);   
      if(id.equalsIgnoreCase("already existing")){                              // element already exists in the scenario
        int dialogButton = JOptionPane.YES_NO_OPTION;                           // propose to do force erase the existing element
        int dialogResult = JOptionPane.showConfirmDialog (null, "Element is already existing, force replace?","Warning",dialogButton);
        if(dialogResult == JOptionPane.YES_OPTION){
          id = this.myScenario.addElement(jtxtElementId.getText(),tmpCore,true);
          created = true;
        }
      }//if already existing element
      else{
        created = true;
      }
    }
    if(created){
      if(!elementList.contains(id)){                                            // if the id is not already in the list
        elementList.addElement(id);                                             // add it
        jlistElement.setModel(elementList);                                     // and update the view
      }
    
      if(jlblScenarioTitle.getText().equalsIgnoreCase("")){                     // set the title of the scenario if it
        jlblScenarioTitle.setText(this.myScenario.getTitle());                  // has not been made before
      }
    
      scenarioGraph.addNode(id);                                                // add the element as a new node in the graph 
                                                                                // representing the scenario
    
      jtxtElementId.setText("");                                                // erase all the field for further inputs
      try{
        doc.remove(0, doc.getLength());
      }catch(BadLocationException e){e.printStackTrace();}
      scenarioGraph.addPipe();                                                  // add the listeners on the graph visualisation
      save(true);                                                               // the scenario has been modified
    }
  }//GEN-LAST:event_jbtnAddElementActionPerformed

  private void jbtnRemoveElementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnRemoveElementActionPerformed
    this.myScenario.removeElement(jtxtElementId.getText());                     // remove the element from the scenario
    elementList.removeElement(jtxtElementId.getText());                         // remove the id from the list
    scenarioGraph.removeNode(jtxtElementId.getText());                          // remove the node from the graph visualisation
    // indicate that a scenario save is needed
    save(true);                                                                 // the scenario has been modified
  }//GEN-LAST:event_jbtnRemoveElementActionPerformed

  private void jlistElementMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jlistElementMouseClicked
    if(evt.getClickCount() == 2){                                               // only if double click on the element
      String selected = jlistElement.getSelectedValue();                        // temporary attribute for easy handle
      selectElement(selected);                                                  // fill the left part of the user interface
  
      ArrayList<String> linkIds = new ArrayList<>();                            // update the graphic part via the class MyGraph
      if(this.myScenario.getElement(selected).hasNext()){                       // if the element has next elements
        for(String next : myScenario.getElement(selected).getNextElements()){
          linkIds.add(selected+next);                                           // the outcoming links to update
        }//for next
      }//if hasNext
       if(this.myScenario.getElement(selected).hasPrevious()){                  // if the element has previous elements
        for(String previous : myScenario.getElement(selected).getPreviousElements()){
          linkIds.add(previous+selected);                                       // the incoming links to update
        }//for previous
      }//if hasPrevious
      scenarioGraph.updateNodesAndLinks(                                        
              this.myScenario.getElement(selected).getElementId(),linkIds);     // update the view of the node and attached links
    }//if clickCount == 2
  }//GEN-LAST:event_jlistElementMouseClicked

  private void jbtnLoadScenarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnLoadScenarioActionPerformed
    XmlTool xml = new XmlTool();
    this.myScenario = xml.loadScenario((String)jcbbScenarioChoice.getSelectedItem()); // load the scenario set in the combo box
    jtxtElementId.setText("");                                                  // no display to do beforehand
    jtxtElementCore.setText("");
    jtextScenarioTitle.setText("");
    elementList.clear();
    jlblScenarioTitle.setText(this.myScenario.getTitle());                      // indicate the scenario name/title
    for(String id : this.myScenario.getWholeScenario().keySet()){               // for each element in the scenario
      elementList.addElement(id);                                               // add the id to the list
      scenarioGraph.addNode(id);                                                // add the related node in the graph
    }//for id -> add node
    for(String id : this.myScenario.getWholeScenario().keySet()){               // for each element in the scenario
      if(this.myScenario.getElement(id).hasNext()){                             // if the node has next elements
        for(String next : myScenario.getElement(id).getNextElements()){         // for each next elements of the node
          try{
            scenarioGraph.addLink(id,next);                                     // add a link between the two nodes
          }catch(RPGSCException e){
            System.err.println("The link between " + id + " and " + next + " is already"
                    + " created. This is not possible while loading an existing"
                    + "scenario. Program will now quit.");
            System.exit(9);
          }
        }// for next
      }// if hasNext()
    }//for id -> addLink
    scenarioGraph.addPipe();                                                    // add the listeners on the graph visualisation
    jlistElement.setModel(elementList);                                         // update the list view of the elements of the scenario
  }//GEN-LAST:event_jbtnLoadScenarioActionPerformed

  private void jbtnQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnQuitActionPerformed
    if(this.saveNeeded){                                                        // if a save is needed while closing
      int dialogButton = JOptionPane.YES_NO_OPTION;                             // proposes to do so
      int dialogResult = JOptionPane.showConfirmDialog (null, "Would you like to save before quit?","Warning",dialogButton);
      if(dialogResult == JOptionPane.YES_OPTION){
        saveScenario();
      }
    }
    System.exit(0);  
  }//GEN-LAST:event_jbtnQuitActionPerformed

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    saveScenario();                                                             // save the scenario, nothing more
  }//GEN-LAST:event_jButton1ActionPerformed

  private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    Document doc = jtxtElementCore.getDocument();                               // erase all the data in the several fields used to input an element
    jtxtElementId.setText("");
    try{
      doc.remove(0, doc.getLength());
    }
    catch(BadLocationException e){
      e.printStackTrace();
    }
  }//GEN-LAST:event_jButton3ActionPerformed

  private void InstructionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InstructionActionPerformed
    // Just a message to quick instructions...
    final String s = "Welcome to the Scenario Management Interface.<br /><br />"
            + "Here you can work on scenario for tabletop rpg (as intended initially for this software) or for everything else. " 
            + "A scenario is seen as several component linked ones with each other by causes and consequences. One component would then "
            + "be seen as a sequence of actions taken in order to reach one goal that will, or will not, help to reach the scenario global aim. <br /><br />"
            + "You can create an element by filling its course of actions in the dedicated field. The id of the element is facultative and will be "
            + "auto-generated if none is set. "
            + "Each element is represented by a node on the graphical view. Righ click on a node allows to create a link between two nodes (or a loop on the "
            + "current node) to symbolize cause (start of the link) and consequence (end of the link).<br /><br />"
            + ""
            + "You can then select nodes, see the links and several other things.<br /><br />"
            + ""
            + "Message the author for every improvement you might think of. Cheers.";
    final String html1 = "<html><body style='width: ";
    final String html2 = "px'>";
    JOptionPane.showMessageDialog(null, new JLabel(html1 + "400" + html2 + s));
  }//GEN-LAST:event_InstructionActionPerformed

  /**
   * Launching the thread containing the user interface
   * (auto-generated)
   * 
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(RPGScenarioCreationIHM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(RPGScenarioCreationIHM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(RPGScenarioCreationIHM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(RPGScenarioCreationIHM.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new RPGScenarioCreationIHM().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JMenuItem Instruction;
  private javax.swing.JButton jButton1;
  private javax.swing.JButton jButton3;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JMenuBar jMenuBar1;
  private javax.swing.JMenu jMenuHelp;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JButton jbtnAddElement;
  private javax.swing.JButton jbtnLoadScenario;
  private javax.swing.JButton jbtnQuit;
  private javax.swing.JButton jbtnRandomGeneration;
  private javax.swing.JButton jbtnRemoveElement;
  private javax.swing.JComboBox<String> jcbbDifficulty;
  private javax.swing.JComboBox<String> jcbbScenarioChoice;
  private javax.swing.JLabel jlblLinkCreation;
  private javax.swing.JLabel jlblScenarioTitle;
  private javax.swing.JList<String> jlistElement;
  private javax.swing.JPanel jpanelGraphView;
  private javax.swing.JTextField jtextNbElement;
  private javax.swing.JTextField jtextScenarioTitle;
  private javax.swing.JTextArea jtxtElementCore;
  private javax.swing.JTextField jtxtElementId;
  // End of variables declaration//GEN-END:variables
}