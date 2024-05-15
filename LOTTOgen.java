import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import java.util.ArrayList;

public class LOTTOgen extends JFrame implements WindowListener {
    CommonMethods commonMethods = new CommonMethods();

    JPanel windowPane;

    JPanel numpanelOuter;
    JPanel numpanel;
    GridBagLayout thelayout;
    GridBagConstraints gbc;

    JLabel numTitle; //the title above the grid of numbers, currently unused

    JButton selectButton; //Select/deselect all button
    Boolean selectTogglestate;

    JPanel genpanelOuter;
    JPanel genpanel;

    ArrayList<NumButton> buttons = new ArrayList<NumButton>(); //arraylist of the number buttons in the grid.

    ArrayList<Integer> pool = new ArrayList<Integer>(); //arraylist of numbers in the random draw pool.
    ArrayList<Integer> result = new ArrayList<Integer>(); //arraylist of randomly drawn line of numbers.

    //components to be accessed by Generator and NumButton.
    JLabel poolLabel;
    JLabel resultLabel;

    public static int NUM_OF_BUTTONS = 40;

    public static void main(String Args[]) {
        LOTTOgen myWindow = new LOTTOgen("LOTTO Generator");
        //myWindow.setPreferredSize(new Dimension(400,600));
        myWindow.setMinimumSize(new Dimension(800, 450));
        myWindow.setVisible(true);
    }

    public LOTTOgen(String title) { 
        super(title);
        addWindowListener(this);

        //Big panel to hold everything in the window, for the background colour.
        windowPane = new JPanel();
        this.add(windowPane);
        windowPane.setBackground(Color.cyan);

        //The panel for the grid of numbers.
        numpanelOuter = new JPanel();
        windowPane.add(numpanelOuter);
        windowPane.add(numpanelOuter);

        numpanel = new JPanel();
        numpanelOuter.add(numpanel);
        thelayout = new GridBagLayout();
        numpanel.setLayout(thelayout);
        GridBagConstraints gbc = new GridBagConstraints();

        //Creating the border around numpanel.
        EmptyBorder emptyBorder = new EmptyBorder(20, 40, 20, 40);
        BevelBorder bevelBorder = new BevelBorder(BevelBorder.RAISED);
        EtchedBorder etchedBorder = new EtchedBorder(EtchedBorder.RAISED);
        CompoundBorder compoundBorder_num1 = new CompoundBorder(bevelBorder, etchedBorder);
        CompoundBorder compoundBorder_num2 = new CompoundBorder(compoundBorder_num1, emptyBorder);
        numpanel.setBackground(Color.orange);
        numpanel.setBorder(compoundBorder_num2);

        numTitle = new JLabel(" ");

        //Select/deselect all button
        selectButton = new JButton("Deselect All");
        selectTogglestate = true; //Because all buttons are selected initially.

        //The panel for the generator stuff.
        genpanelOuter = new JPanel();
        windowPane.add(genpanelOuter);
        windowPane.add(genpanelOuter);

        genpanel = new JPanel();
        genpanelOuter.add(genpanel);

        genpanel.setBackground(Color.orange);
        genpanel.setBorder(compoundBorder_num2);


        poolLabel = new JLabel("pool label");
        resultLabel = new JLabel("result line label");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 15));
        resultLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        Generator generator = new Generator(genpanel, pool, result, poolLabel, resultLabel);
        
        //ommonMethods.addComponent(numTitle, numpanel, thelayout, gbc, 2, 0, 1, 1);

        //Use a for loop to add buttons in rows of 5.
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
        System.exit(0); //Exit program when you close the window?
    }

    public void windowOpened(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
}


