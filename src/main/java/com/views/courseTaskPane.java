//author Jeremi Kantola
//26.1.2025

package com.views;

import javax.swing.*;

/**
* Hoidetaan kaikki ruudun oikealla puolella olevan tehtävän palautuksen suorittavan ikkunan toiminnalliset sekä graaffiset toiminnot
* Taskpane = Kaikki muut paneelit sisältävä pääpaneeli
* demoTiedot = tidecli kautta saadut Demon numero sekä tehtävän numero sisältävä label
* tehtavaNimi = Tidecli kautta saadun tehtävän nimen sisältävä label.
* avaaTehtava = Nappi, jota painamalla tehtävät avautuvat selaimen ikkunaan
* tehtavaTiedot = Tidecli kautta saadun tehtävän tiedot, ei toimi vielä tidecli puolella.
* pisteLabel = Tehtävän palautuksen jälkeen Tidecli antamat pisteet sisältävä label, ei toimi vielä tidecli puolella
* submitButton = nappi, jota painamalla tehtävä lähetetään tidecli kautta TIM-järjestelmälle.
* showOutputButton = nappi, jota painamalla näytetään tidecli lähettämät tiedot terminaalissa.
* resetButton = nappi, jota painamalla tyhjennetään IDE:n tiedosto ja haetaan siihen tehtävän tiedosto uudestaan.
* submitPane = paneeli, joka sisältää pisteLabelin, submitButtonin sekä showOutputButtonin
* infoPane = paneeli, joka sisältää demoTiedot, tehtäväNimi, avaaTehtävä sekä tehtäväTiedot osat.
*  resetPane = paneeli, joka siältää resetButtonin.
*/
public class courseTaskPane {
    /**
     * The main task pane.
     */
    private JPanel Taskpane;
    /**
     * Information of the exercise.
     */
    private JLabel demoTiedot;
    /**
     * Name of the task.
     */
    private JLabel tehtavaNimi;
    /**
     * Button that opens the task.
     */
    private JButton avaaTehtava;
    /**
     * Label for the task info.
     */
    private JLabel tehtavaTiedot;
    /**
     * Points earned from the task.
     */
    private JLabel pisteLabel;
    /**
     * A submit button.
     */
    private JButton submitButton;
    /**
     * Button that shows the console output.
     */
    private JButton showOutputButton;
    /**
     * Button for resetting.
     */
    private JButton resetButton;
    /**
     * Panel for the submission.
     */
    private JPanel submitPane;
    /**
     * Info panel.
     */
    private JPanel InfoPane;
    /**
     * Reset panel.
     */
    private JPanel resetPane;

    public JPanel getContent() {
        return Taskpane;
    }
}

