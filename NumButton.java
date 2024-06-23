import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class NumButton {
    CommonMethods bcommonMethods;

    JButton b = new JButton("Default text");
    int togglestate;
    int bnumber; //The number of the button pressed when using getActionCommand.

    Container bcontainer;
    GridBagLayout blayout;
    GridBagConstraints bconstraints;
    List<Integer> bpool;
    List<Integer> bresult;
    
    JLabel bpoolLabel;
    JLabel bresultLabel;

    String text; //For adding text to labels.

    //Create NumButton's constructor.
    public NumButton (Container yourcontainer, GridBagLayout layout, GridBagConstraints constraints,
    List<Integer> pool, List<Integer> result, JLabel poolLabel, JLabel resultLabel) {
        bcommonMethods  = new CommonMethods();

        togglestate = 1; //at 2 it will be green.

        bcontainer = yourcontainer;
        blayout = layout;
        bconstraints = constraints;
        bpool = pool;
        bresult = result;

        bpoolLabel = poolLabel;
        bresultLabel = resultLabel;

    }


    //Toggles button between 3 states. Takes no parameters but heavily relies on int togglestate.
    public void togglebutton() {
        //Switch the button's visuals and state.
        switch (togglestate) { 
            case 1 : 
                bpool.add(bnumber); //move to pool
                b.setBackground(Color.yellow); //colour to yellow
                b.setBorder(BorderFactory.createLineBorder(Color.black));
                togglestate = 2; //state to 2
                break;

            case 2 : 
                bpool.remove(bpool.indexOf(bnumber)); //remove from pool
                bresult.add(bnumber); //move to result line
                b.setBackground(Color.green); //colour to green
                b.setBorder(new BevelBorder(BevelBorder.RAISED));
                togglestate = 3; //state to 3
                break;

            case 3 : //move out.
                bresult.remove(bresult.indexOf(bnumber));
                b.setBackground(Color.lightGray);
                b.setBorder(new BevelBorder(BevelBorder.LOWERED));
                togglestate = 1;
                break;
            
            default :
                System.out.println("togglebutton method error. togglestate = " + togglestate);
        }

        //Update text in poolLabel.
        Collections.sort(bpool);
        text = "pool: ";
        int iTemp = -10;
        Boolean isChain = false;
        for(int i = 0; i < bpool.size(); i++) {   
            if (iTemp == -10) { //If no numbers have been added yet.
                text = text + bpool.get(i);
                iTemp = bpool.get(i);
            }
            else if (bpool.get(i) == iTemp + 1) { //If the current number is 1 higher than the last.
                if (i == bpool.size() - 1) { //Edge case when at the end of the array.
                    text = text + "-" + bpool.get(i);
                }
                isChain = true;
                iTemp = bpool.get(i);
            }
            else if (isChain) { //If the current number is not 1 higher than the last, and it broke a chain.
                text = text + "-" + iTemp + ", " + bpool.get(i);
                isChain = false;
                iTemp = bpool.get(i);
            }
            else {
                text = text + ", " + bpool.get(i);
                iTemp = bpool.get(i);
            }
            //text = text + bpool.get(i) + ", ";
        }
        bpoolLabel.setText(text);

        //Update text in resultLabel.
        Collections.sort(bresult);
        text = " ";
        for(int i = 0; i < bresult.size(); i++) {   
            text = text + bresult.get(i) + " ";
        }
        bresultLabel.setText(text);

                    /*
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

    //Adds a toggle button to the grid of numbers. A similar method is in Generator that is also contained within this specialised
    //method. At a later time a universal method can be created in a different class.
    public void addButton(String text, Component componente, int gridx, int gridy, int gridwidth, int gridheight){
        //Set the button's given text, and set variable bnumber that is the integer equivalent.
        b.setText(text);
        bnumber = Integer.parseInt(text.replaceAll(" ", ""));

        //Set the button's other visuals.
        b.setPreferredSize(new Dimension(40, 26));
        b.setBorder(BorderFactory.createLineBorder(Color.black));
        //b.setBorder(new BevelBorder(BevelBorder.RAISED));
        b.setBackground(Color.lightGray); //at yellow bnumber is initially in pool.
        b.setForeground(Color.black);

        togglebutton();

        //Add action listener to the button, to toggle button when clicked.
        b.addActionListener(event -> {
            togglebutton();
        });

        bcommonMethods.addComponent(componente, bcontainer, blayout, bconstraints, gridx, gridy, gridwidth, gridheight);

    }

    
}


