import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import java.util.List;
import java.util.ArrayList;

public class LOTTOgen extends JFrame implements WindowListener {

    // Variable for the maximum number in a lottery line (LOTTO: 40).
    public static int NUM_OF_BUTTONS = 40;

    public static void main(String Args[]) {
        // Create the window of the program with dimensions, and make it visible.
        LOTTOgen myWindow = new LOTTOgen("LOTTO Generator");
        myWindow.setMinimumSize(new Dimension(800, 450));
        myWindow.setVisible(true);
    }

    // Contains common methods used by more than one class. BAD PRACTICE.
    CommonMethods commonMethods;

    // Main panel to hold everything in the window.
    JPanel windowPane;

    // Panels for the grid of numbers.
    JPanel numpanelOuter; // holds the numpanel.
    JPanel numpanel; // the grid and select/deselect button.
    GridBagLayout thelayout;
    GridBagConstraints gbc;

    // Arraylist of the number buttons in the grid.
    List<NumButton> buttons;

    // Select/deselect all button for the grid of numbers.
    JButton selectButton;
    Boolean selectTogglestate;

    // Panels for the options and submit button. ("generator")
    JPanel genpanelOuter;
    JPanel genpanel;

    // Arraylist of numbers in the random draw pool and label that displays it.
    List<Integer> pool;
    JLabel poolLabel;

    // Arraylist of the randomly drawn line of numbers and label that displays it.
    List<Integer> result;
    JLabel resultLabel;

    public LOTTOgen(String title) {
        // Set window's title.
        super(title);
        addWindowListener(this);

        commonMethods = new CommonMethods();

        // Create window pane (main JPanel) and set background colour.
        windowPane = new JPanel();
        this.add(windowPane);
        windowPane.setBackground(Color.cyan);

        // Create panel to hold the grid panel.
        numpanelOuter = new JPanel();
        windowPane.add(numpanelOuter);

        // Create the grid panel with grid bag layout.
        numpanel = new JPanel();
        numpanelOuter.add(numpanel);
        thelayout = new GridBagLayout();
        numpanel.setLayout(thelayout);
        GridBagConstraints gbc = new GridBagConstraints();

        buttons = new ArrayList<>(); // Number buttons in the grid.

        // Create the select/deselect all button and set it to deselect intially.
        selectButton = new JButton("Deselect All");
        selectTogglestate = true;

        // Create panel to hold the generator panel.
        genpanelOuter = new JPanel();
        windowPane.add(genpanelOuter);
        windowPane.add(genpanelOuter);

        // Create the generator panel.
        genpanel = new JPanel();
        genpanelOuter.add(genpanel);

        // Create the border and background for the number and generator panel.
        EmptyBorder emptyBorder = new EmptyBorder(20, 40, 20, 40);
        BevelBorder bevelBorder = new BevelBorder(BevelBorder.RAISED);
        EtchedBorder etchedBorder = new EtchedBorder(EtchedBorder.RAISED);
        CompoundBorder compoundBorder_num1 = new CompoundBorder(bevelBorder, etchedBorder);
        CompoundBorder compoundBorder_num2 = new CompoundBorder(compoundBorder_num1, emptyBorder);

        // Set the background colour and border for the number and generator panel.
        numpanel.setBackground(Color.orange);
        numpanel.setBorder(compoundBorder_num2);
        genpanel.setBackground(Color.orange);
        genpanel.setBorder(compoundBorder_num2);


        // Configure lists and labels for the draw pool and result.
        pool = new ArrayList<>();
        poolLabel = new JLabel("pool label");
        result = new ArrayList<>();
        resultLabel = new JLabel("result line label");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 15));
        resultLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        // Instantiate the Generator class.
        Generator generator = new Generator(genpanel, pool, result, poolLabel, resultLabel);

        //Use a for loop to add buttons in rows of 5 to the number panel.
        int y = 1;
        int x = 0;
        for (int i = 1; i <= NUM_OF_BUTTONS; ++i) {
            //Initialise a new button object ('instance of the class').
            buttons.add(new NumButton(numpanel, thelayout, gbc, pool, result, poolLabel, resultLabel));

            //Set button's name, in a way that ensures all buttons are same width.
            String name = Integer.toString(i);
            String nobString = Integer.toString(NUM_OF_BUTTONS);
            while (name.length() < nobString.length()) {
                name = "0" + name;
            }

            //run the addobjects function to add the button in the right position.
            buttons.get(i - 1).addButton(name, buttons.get(i - 1).b, x, y, 1, 1);

            //Use an if statement to add the select/deselect all button at the bottom of the grid.
            if (i == NUM_OF_BUTTONS) {
                commonMethods.addComponent(selectButton, numpanel, thelayout, gbc, 0, y + 1, 5, 1);
            }

            //Use an if statement to put the next loop's button in the next row.
            if (i % 5 == 0) {
                ++y;
                x = 0;
            }
            else {
                ++x;
            }
        }      
        
        
        selectButton.addActionListener(event -> {
            //Use a for loop to toggle the button's togglestate until it's the right state.
            for (int i = 1; i <= NUM_OF_BUTTONS; ++i) {
                while (!selectTogglestate && buttons.get(i - 1).togglestate != 2 ||
                selectTogglestate && buttons.get(i - 1).togglestate != 1) {
                    buttons.get(i - 1).togglebutton();
                }
            }

            //Switch the select button's togglestate.
            selectTogglestate = !selectTogglestate;

            //Change the select button's text.
            if (selectTogglestate) {
                selectButton.setText("Deselect All");
            }
            else if (!selectTogglestate) {
                selectButton.setText("Select All");
            }
        });

    }    



    public void windowClosing(WindowEvent e) {
        dispose();
        System.exit(0); //Exit program when you close the window
    }

    public void windowOpened(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
}


