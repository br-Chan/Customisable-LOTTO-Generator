import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class NumButton {
    CommonMethods bcommonMethods;

    // The number button with its state and associated number.
    JButton b;
    int togglestate;
    int bnumber; // 1 -> NUM_OF_BUTTONS.

    // The container, layout, etc that are associated with the button.
    Container bcontainer;
    GridBagLayout blayout;
    GridBagConstraints bconstraints;

    List<Integer> bpool;
    JLabel bpoolLabel;

    List<Integer> bresult;
    JLabel bresultLabel;

    public NumButton (Container yourcontainer, GridBagLayout layout, GridBagConstraints constraints,
    List<Integer> pool, List<Integer> result, JLabel poolLabel, JLabel resultLabel) {
        bcommonMethods  = new CommonMethods();
        
        // Set the number button's initial text and togglestate.
        b = new JButton("Default text");
        togglestate = 1; //at 2 it will be green.

        bcontainer = yourcontainer;
        blayout = layout;
        bconstraints = constraints;
        bpool = pool;
        bresult = result;

        bpoolLabel = poolLabel;
        bresultLabel = resultLabel;

    }


    // Toggles number button between 3 states: gray, yellow and green,
    // and adds/removes the button's number to/from the pool and result lists.
    public void toggleButton() {
        //Switch the button's visuals and state.
        switch (togglestate) {
            case 1 : 
                // Add button's number to draw pool.
                bpool.add(bnumber);

                b.setBackground(Color.yellow);
                b.setBorder(BorderFactory.createLineBorder(Color.black));

                togglestate = 2;
                break;

            case 2 : 
                // Move button's number from draw pool to result.
                bpool.remove(bpool.indexOf(bnumber));
                bresult.add(bnumber);

                b.setBackground(Color.green);
                b.setBorder(new BevelBorder(BevelBorder.RAISED));

                togglestate = 3;
                break;

            case 3 :
                // Remove button's number from result.
                bresult.remove(bresult.indexOf(bnumber));

                b.setBackground(Color.lightGray);
                b.setBorder(new BevelBorder(BevelBorder.LOWERED));

                togglestate = 1;
                break;
            
            default :
                System.out.println("toggleButton method error. togglestate = " + togglestate);
        }


        // Update the text in the draw pool label.

        Collections.sort(bpool);
        String text = "pool: "; // text to add to the label.

        // To update the text in the draw pool label, 'chains' of consecutive numbers must be identified
        // so that they may be listed in the form 'm-n'. Chains are to be separated by commas.

        int iTemp = -1; // the previous number added to the label.
        Boolean isChain = false; // whether or not the current number is 1 higher than the last.

        for(int i = 0; i < bpool.size(); i++) {
            if (iTemp == -1) { // If no numbers have been added yet...
                // Add the current number.
                text = text + bpool.get(i);
            }
            else if (bpool.get(i) == iTemp + 1) { // If the current number is exactly 1 higher than the last...
                // Handle edge case when at the end of the array.
                if (i == bpool.size() - 1) {
                    text = text + "-" + bpool.get(i);
                }

                isChain = true;
            }
            else if (isChain) { // If the current number is not 1 higher than the last, breaking the chain...
                // Add the second half of the chain and the current number to the text string.
                text = text + "-" + iTemp + ", " + bpool.get(i);
                isChain = false;
            }
            else {
                // Add the current number to the text string.
                text = text + ", " + bpool.get(i);
            }
            
            iTemp = bpool.get(i);
        }
        bpoolLabel.setText(text);


        //Update the text in the result label.

        Collections.sort(bresult);
        text = " "; // clear the text string.

        for(int i = 0; i < bresult.size(); i++) {   
            text = text + bresult.get(i) + " ";
        }
        bresultLabel.setText(text);

        /*
            CODE FOR DEBUGGING pool and result lists, DELETE WHEN NOT NEEDED ANYMORE.

            System.out.print("pool: ");
            for(int i = 0; i < bpool.size(); i++) {   
                System.out.print(bpool.get(i) + " ");
            }  
            System.out.println("");
            System.out.print("result line: ");
            for(int i = 0; i < bresult.size(); i++) {   
                System.out.print(bresult.get(i) + " ");
            }  
            System.out.println("");
        */

    }

    // Adds the number button to the grid of numbers in the window.
    // A similar method is in Generator that is also contained within this specialised method.
    // At a later time a universal method can be created in a different class.
    public void addButton(String text, Component componente, int gridx, int gridy, int gridwidth, int gridheight){
        //Set button's text (handled in LOTTOgen), and initialise bnumber that is the integer equivalent.
        b.setText(text);
        bnumber = Integer.parseInt(text);

        //Set the button's other visuals, initially in gray state.
        b.setPreferredSize(new Dimension(40, 26));
        b.setBorder(BorderFactory.createLineBorder(Color.black));
        b.setBackground(Color.lightGray); // so bnumber will be in the draw pool list after first toggle.
        b.setForeground(Color.black);

        // Toggle the button for the first time, toggling its state to yellow.
        toggleButton();

        // Add action listener to the number button to toggle the button when clicked.
        b.addActionListener(event -> {
            toggleButton();
        });

        // Add the button to the grid in the inputted position.
        bcommonMethods.addComponent(componente, bcontainer, blayout, bconstraints, gridx, gridy, gridwidth, gridheight);

    }

    
}


