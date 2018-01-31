package rpgscenariocreation;

import ihm.RPGScenarioCreationIHM;

/**
 * This project is dedicated to Scenario creation for several tabletop RPG.
 * Will ideally include some learning to adapt to user preference.
 * 
 * @author Nicolas Brax
 */
public class RPGScenarioCreation {
  
  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    }catch(Exception ex){}
    new RPGScenarioCreationIHM().setVisible(true);

    /* THOUGHTS FOR RANDOM GENERATION
      Un scenario c'est :
      > 1. Un protagoniste principal.
        1.1. Etat Civil (sexe, race, age, taille, ...).
        1.2. Statut Social.
        1.3. Motivations.
    
      > 2. Un objectif principal.
        2.1. Nature du contrat (kidnapping, récupération, assassinat, ...).
        2.2. Objet du contrat (personne, objet, ...).
        2.3. Conditions de succès et d'échecs.
    
      > 3. Un lieu principal.
        3.1. Nature du lieu.
        3.2. Accessibilité du lieu.

      Il peut se voir agrémenter de : 
      > 1. Des personnages secondaires, gentils ou méchants.
        1.1. Etat Civil (sexe, race, age, taille, ...).
        1.2. Statut Social.
        1.3. Motivations.
        1.3.1. Motivations annoncées.
        1.3.2. Motivations réelles.
    
      > 2. Des objectifs secondaires.
        2.1. Nature des objectifs.
        2.2. Objet des objectifs.
        2.3. Raisons des objectifs.
        2.4. Succès et échecs.
    
      > 3. Des objectifs/lieux cachés.
        3.1. Nature.
        3.2. Objet.
        3.3. Raisons.
        3.4. Succès et échecs.
    
      > 4. Des objectifs/lieux successifs.
        4.1. Conditions pour poursuivre les objectifs.
        4.2. Chemins divergents ou en ligne droite.
    
      > 5. Des phases d'enquêtes.
        5.1. Enigmes.
        5.2. Interrogation, discussions, ...
        5.3. Recherches
    
      > 6. Une question de moralité.
        6.1. Un dilemme moral.
        6.2. Bien et Mal.
        6.3. Argent et Honneur.
        6.4. ...
    
      Il faut prendre en compte : 
      > 1. L'Univers du jeu et ses spécificités
        1.1. Races disponibles.
        1.2. Accessibilité de la technologie.
        1.3. Accessibilité de la magie.
        1.4. Organisations disponibles.
    
      > 2. La difficulté souhaitée.
        2.1. Facile, normal, difficile, MJ
        2.2. Ajouter de plus en plus de points de construction au scenario.
    
      > 3. Les préférences de joueurs et du MJ.
        3.1. Apprentissage.
        3.2. Score sur les différents points utilisés.
    */
  }
  
}
