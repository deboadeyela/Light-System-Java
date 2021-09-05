
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @Description: GUI class that controls the display and function of the GUI
 */
public class GUI extends JFrame {

    /**
     * ********************** Class Members ***********************
     */
    JSlider lightLevel; // Dynamic lighting of the light
    // Has 5 setting = 0, 1, 2, 3, 4
    JButton status; // Used to display the status of the Light
    JButton lightDisplay; // Used to display and turn on/off the light
    ArrayList<JRadioButton> radioButtons; // Used to store all the radioButtons
    boolean manual = true; // Used to confirm if the user is in Manual mode or Timed mode

    /**
     * ********************** Constructors ***********************
     */
    public GUI() {
        radioButtons = new ArrayList<>();
        initGUI();
    }

    /**
     * ********************** Private Methods ***********************
     */
    /*------------------------------------------------- initGUI -----
    |  Function initGUI
    |
    |  Purpose:  Initializes the GUI and displays it to the user
    |
    |  Parameters: None
    |
    |  Returns: None
    *-------------------------------------------------------------------*/
    private void initGUI() {

        // Sets up window
        this.setSize(400, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Light System");
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        JPanel thePanel = new JPanel(); // Main Panel for Frame
        thePanel.setLayout(new BorderLayout());

        // Creates all the buttons needed and adds them to the main panel
        thePanel.add(createButtons(), BorderLayout.WEST);

        // Set up for the Light Level Slider
        lightLevel = new JSlider(0, 4);
        lightLevel.setValue(0);
        ListenForSlider lForSlider = new ListenForSlider();
        lightLevel.addChangeListener(lForSlider);
        lightLevel.setMajorTickSpacing(1);
        lightLevel.setPaintTicks(true);
        lightLevel.setPaintLabels(true);

        // Adds to the Panel at the bottom
        thePanel.add(lightLevel, BorderLayout.SOUTH);

        JPanel lightPanel = new JPanel(); // Used to store the Light Display

        lightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gridConstraints = new GridBagConstraints();
        gridConstraints.gridy = 0;
        gridConstraints.insets = new Insets(405, 5, 5, 5);

        // Set up for Light Display
        lightDisplay = new JButton("Light OFF");
        lightDisplay.setBackground(Color.GRAY);
        ListenForButton lForButton = new ListenForButton();
        lightDisplay.addActionListener(lForButton);
        lightPanel.add(lightDisplay);

        // Adds to main Panel
        thePanel.add(lightPanel, BorderLayout.CENTER);

        // Displays the GUI
        this.add(thePanel);
        this.setVisible(true);

    }// End intGUI

    /*------------------------------------------------- createButtons -----
    |  Function createButtons
    |
    |  Purpose:  Creates all the buttons on use
    |
    |  Parameters: None
    |
    |  Returns: JPanel - A panel that holds all the buttons and their positions 
    *-------------------------------------------------------------------*/
    private JPanel createButtons() {

        // Sets up the panel
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridBagLayout());
        GridBagConstraints gridConstraints = new GridBagConstraints();
        gridConstraints.gridy = 1;
        gridConstraints.insets = new Insets(40, 5, 5, 5);

        // Creates Light Options for control of the Light
        String[] lightOptions = {"On", "Off"};
        buttons.add(createRadioSwitch(1, lightOptions, "Light Switch"), gridConstraints);

        // Creates Mode options for the type of control light
        gridConstraints.gridy = 2;
        String[] modeOptions = {"Manual", "Timed"};
        buttons.add(createRadioSwitch(0, modeOptions, "Mode Switch"), gridConstraints);

        // Set up for the Status Button
        gridConstraints.gridy = 0;
        status = new JButton("Status");
        ListenForButton lForButton = new ListenForButton();
        status.addActionListener(lForButton);
        buttons.add(status, gridConstraints);

        // Returns the panel
        return buttons;

    }// End createButtions()

    /*------------------------------------------------- createRadioSwitch -----
    |  Function createRadioSwitch
    |
    |  Purpose:  Creates all radio switches needed and returns them in panel
    |
    |  Parameters: int num - The index of the value in the array you want 
    |                         set active as default
    |              String[] options - Holds the amount and value of the options
    |                                 you want as the buttons
    |              String title - The title you want displayed on the border
    |
    |  Returns: JPanel - A panel that holds all the Radio Switches
    *-------------------------------------------------------------------*/
    private JPanel createRadioSwitch(int num, String[] options, String title) {

        // Set up for the panel
        JPanel switchPanel = new JPanel();
        TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
        switchPanel.setBorder(titledBorder);
        JRadioButton option;
        ButtonGroup lightSwitch = new ButtonGroup();
        ListenForRadio lForRadio = new ListenForRadio();

        // Creates needed Radio Button for each in the array sent in
        for (int i = 0; i < options.length; i++) {
            option = new JRadioButton(options[i]);
            option.addActionListener(lForRadio);
            radioButtons.add(option);
            if (i == num) {
                option.setSelected(true);
            }
            lightSwitch.add(option);
            switchPanel.add(option);
        }

        return switchPanel;
    }// end createRadioSwitch()

    /*------------------------------------------------- changeLight -----
    |  Function changeLight
    |
    |  Purpose:  Changes the state of the light depending on input
    |
    |  Parameters: int n - The brightness level of the light
    |
    |  Returns: None
    *-------------------------------------------------------------------*/
    private void changeLight(int n) {

        switch (n) {

            case 0:
                setStatus(Color.GRAY, "Light OFF", n, "Off");
                break;

            case 1:
                setStatus(new Color(255, 255, 204), "Light ON", n, "On");
                break;

            case 2:
                setStatus(new Color(255, 255, 153), "Light ON", n, "On");
                break;

            case 3:
                setStatus(new Color(255, 255, 100), "Light ON", n, "On");
                break;

            case 4:
                setStatus(Color.YELLOW, "Light ON", n, "On");
                break;

        }// end switch

    }// end changeLights()

    /*------------------------------------------------- setStatus -----
    |  Function setStatus
    |
    |  Purpose:  Sets the status of the light
    |
    |  Parameters: Color c - The color of the light
    |              String lightDisText - The text displayed on the light
    |              int lightLvl - The level of the light
    |              String valueTrue - The Value needed true in LightSwitch
    |
    |  Returns: None
    *-------------------------------------------------------------------*/
    private void setStatus(Color c, String lightDisText, int lightLvl, String valueTrue) {

        lightDisplay.setBackground(c);
        lightDisplay.setText(lightDisText);
        lightLevel.setValue(lightLvl);

        for (JRadioButton r : radioButtons) {

            if (r.getActionCommand().equals(valueTrue)) {
                r.setSelected(true);
                break;
            }// end if

        }// end for

    }// end setStatus 

    /*------------------------------------------------- timedLoop -----
    |  Function timedLoop
    |
    |  Purpose:  Starts a timed loop that changes the state of the light
    |
    |  Parameters: None
    |
    |  Returns: None
    *-------------------------------------------------------------------*/
    private void timedLoop() {

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(0);

        // Meathod that is ran every period of time
        Runnable changeOnTimer = new Runnable() {
            int i = 0;

            @Override
            public void run() {

                System.out.println("In Runnable");

                if (manual == false) {
                    if (i == 5) {
                        i = 0;
                    }
                    System.out.println("In IF");
                    changeLight(i);
                    i++;

                }
                if (manual == true) {
                    executor.shutdown();
                }
                System.out.println("After IF");
            }
        };

        // Starts the loop that excecutes the meathod
        executor.scheduleAtFixedRate(changeOnTimer, 0, 1, TimeUnit.SECONDS);
    }// end timedLoop()

    /*------------------------------------------------- printStatus -----
    |  Function printStatus
    |
    |  Purpose:  Prints the status of the gui to the user
    |
    |  Parameters: None
    |
    |  Returns: None
    *-------------------------------------------------------------------*/
    private void printStatus() {

        String str = "";

        // For Light Status
        str += "\n Light = ";
        for (int i = 0; i < 2; i++) {
            if (radioButtons.get(i).getSelectedObjects() != null) {
                str += radioButtons.get(i).getActionCommand();
                if (radioButtons.get(i).getActionCommand().toString().equals("On")) {
                	// For Mode Switch
                    str += "\n Mode Switch = ";
                    for (int j = 2; j < 4; j++) {
                        if (radioButtons.get(j).getSelectedObjects() != null) {
                            str += radioButtons.get(j).getActionCommand();
                           
                        }
                    }
                    // For Light Level
                   str += "\n Light Level = " + lightLevel.getValue();

                   //end str
                   str += "\n";
                }
            }
        }

        

        

        JOptionPane.showMessageDialog(null, str, "Status", JOptionPane.INFORMATION_MESSAGE);

    }// end printStatus()

    /**
     * ********************** Action Handler Classes ***********************
     */
    /**
     *
     * @Description: Description for the class handles the change of state of a
     * slider
     */
    private class ListenForSlider implements ChangeListener {

        @Override
        // Ran when a slider this class is attached to changes state
        public void stateChanged(ChangeEvent e) {

            // Changes the state of the light if true
            if (e.getSource() == lightLevel) {
                System.out.println("Light Level changed to" + lightLevel.getValue());
                changeLight(lightLevel.getValue());
            }// end if

        }// end stateChanged

    }// end class ListenForSlider

    /**
     * @Description: Description for the class handles the change of state of a
     * button
     */
    public class ListenForButton implements ActionListener {

        @Override
        // Ran if a button is pressed this class is attached to
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == status) {
                System.out.println("Status clicked");
                printStatus();
            }// end if

            if (e.getSource() == lightDisplay) {
                if (e.getActionCommand().equals("Light OFF")) {
                    changeLight(4);
                }// end if

                if (e.getActionCommand().equals("Light ON")) {
                    changeLight(0);
                }// end if
            }// end if

        }// end actionPerformed()

    }// end class ListenForButton

    /**
     *@Description: Description for the class handles the change of state of a
     * Radio Button
     */
    public class ListenForRadio implements ActionListener {

        @Override
        // Ran when a radiobutton is pressed this class is attached to
        public void actionPerformed(ActionEvent e) {

            String action = e.getActionCommand();

            switch (action) {
                case "On":
                    System.out.println("On Clicked");
                    changeLight(4);
                    break;
                case "Off":
                    System.out.println("Off Clicked");
                    changeLight(0);
                    break;
                case "Manual":
                    System.out.println("Manual Clicked");
                    manual = true;
                    break;
                case "Timed":
                    System.out.println("Timed Clicked");
                    manual = false;
                    timedLoop();
                    break;
            }// end switch

        }// actionPerformed()

    }// end class ListenForRadio

}// end class GUI
