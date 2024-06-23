import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.border.BevelBorder;

public class Generator {
    public static int RESULT_LINE_SIZE = 6;

    CommonMethods gcommonMethods = new CommonMethods();

    List<Integer> gpool;
    List<Integer> gresult;
    List<Integer> copygpool; //for storing contents of orig when generating result line.
    List<Integer> copygresult; //for storing contents of orig when generating result line.
    List<String> resultLog; //for storing list of result lines with each press of submit button.

    //Options
    JCheckBox strikeButton; //for toggling randomisation
    JToggleButton powerButton; //for toggling inclusion of power ball.
    JPanel linesPanel; //to house the linesLabel and comboLines
    JLabel linesLabel;
    JComboBox<Integer> comboLines; //for deciding the number of result lines to generate.

    //the labels & submit button
    JLabel gpoolLabel;
    JButton submit;
    JLabel gresultLabel;
    String text; //Temporary string for adding text to labels.

    // The container, layout, etc associated with the options and submit button.
    Container gcontainer;
    GridBagLayout glayout;
    GridBagConstraints gconstraints;

    JFrame popFrame; //for any pop up windows, errors or otherwise.

    public Generator(Container yourcontainer, List<Integer> pool, List<Integer> result,
    JLabel poolLabel, JLabel resultLabel) {

        //The arraylists
        gpool = pool;
        copygpool = new ArrayList<>();

        gresult = result;
        copygresult = new ArrayList<>();

        resultLog = new ArrayList<>();

        //Options
        strikeButton = new JCheckBox("Strike Mode (random order)");
        strikeButton.setBackground(Color.orange);

        powerButton = new JToggleButton("Powerball");
        powerButton.setSelected(true);

        linesPanel = new JPanel();
        linesPanel.setBackground(Color.orange);

        linesLabel = new JLabel("Lines: ");

        comboLines = new JComboBox<>();
        for (int i = 1; i <= 12; ++i) {
            comboLines.addItem(i);
        }

        //The labels & submit button
        gpoolLabel = poolLabel;
        submit = new JButton("Submit!");
        gresultLabel = resultLabel;

        gcontainer = yourcontainer;
        glayout = new GridBagLayout();
        gcontainer.setLayout(glayout);
        gconstraints = new GridBagConstraints();


        // Add components to the panel in the right positions.
        // TODO decide if I want to remove the poolLabel and keep it in LOTTOgen.java
        gcommonMethods.addComponent(strikeButton, gcontainer, glayout, gconstraints, 0, 0, 2, 1);
        gcommonMethods.addComponent(powerButton, gcontainer, glayout, gconstraints, 0, 1, 2, 1);
        gcommonMethods.addComponent(linesPanel, gcontainer, glayout, gconstraints, 0, 2, 2, 1);
        gcommonMethods.addComponent(linesLabel, linesPanel, glayout, gconstraints, 0, 2, 1, 1);
        gcommonMethods.addComponent(comboLines, linesPanel, glayout, gconstraints, 1, 2, 1, 1);
        gcommonMethods.addComponent(poolLabel, gcontainer, glayout, gconstraints, 0, 3, 2, 1);
        gcommonMethods.addComponent(submit, gcontainer, glayout, gconstraints, 0, 4, 2, 1);
        gcommonMethods.addComponent(resultLabel, gcontainer, glayout, gconstraints, 0, 5, 2, 1);

        // Add action listener to the submit button to display the result and check for invalid inputs.
        submit.addActionListener(event -> {
            resultLog.clear();

            // Return immediately if invalid input detected.
            if (!checkSelected()) return;

            if ((int) comboLines.getSelectedItem() == 1) { // If only 1 result line to be printed...
                // Generate and display 1 result line and add it to the resultLog list.
                getLine();
                resultLog.add(gresultLabel.getText());
            }
            else { // If more than 1 result line to be printed...
                for (int i = 1; i <= (int) comboLines.getSelectedItem(); ++i) {
                    // Generate a result line that has not been added to result log yet.
                    do {
                        getLine();
                    } while (resultLog.contains(gresultLabel.getText()));

                    // Add this new result line to the resultLog list.
                    resultLog.add(gresultLabel.getText());

                }
                Collections.sort(resultLog); // sorts the result lines alphabetically.

                // Format a temp string for the pop up window to display the result lines.
                String popText = "<html>";
                for (int i = 1; i <= (int) comboLines.getSelectedItem(); ++i) {
                    popText = popText + resultLog.get(i - 1) + "<br/>";
                }
                popText = popText + "</html>";

                // Create and format a label for the pop up window.
                JLabel popLabel = new JLabel(popText, SwingConstants.CENTER);
                popLabel.setFont(new Font("Arial", Font.BOLD, 20));
                popLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

                // Display the pop up window displaying the result lines.
                popFrame = new JFrame();
                JOptionPane.showMessageDialog(popFrame, popLabel,
                "Here are your numbers", JOptionPane.PLAIN_MESSAGE);
            }

        });

        // Add an action listener to the powerball button to add the powerball number to each result line.
        powerButton.addActionListener(event -> {
            // Create pattern and matcher for a 6-number result line.
            Pattern pattern_1 = Pattern.compile("\\d+  \\d+  \\d+  \\d+  \\d+  \\d+  ", Pattern.CASE_INSENSITIVE);
            Matcher matcher_1 = pattern_1.matcher(gresultLabel.getText());

            //If comboLines has 1 selected and gresultLabel has a result line in it...
            if(matcher_1.matches() && !powerButton.isSelected()) {
                // Add the powerball in square brackets to the result line label.
                gresultLabel.setText(gresultLabel.getText() + "[" + String.format("%02d", getPowerBall()) + "]");
            }
            else if (matcher_1.find() && powerButton.isSelected()) {
                gresultLabel.setText(gresultLabel.getText().substring(0, gresultLabel.getText().length() - 4));
            }
        });
    }

    //Carries out the factorial operation (n!).
    public double fact (double n) {
        double i = 1;
        double j = 1;
    
        while (j <= n) {
            i = i * j;
            ++j;
        }
    
        return i;
    }

    //Carries out the combination operation (nCi).
    public double combination (double n, double i) {
        return fact(n) / (fact(i) * fact(n-i));
    }

    // Checks for invalid inputs and handles those errors, returns true if there are none.
    public Boolean checkSelected() {
        if (gresult.size() > RESULT_LINE_SIZE) { //If there are too many green numbers...
            // Display an error pop up window.
            popFrame = new JFrame();
            JOptionPane.showMessageDialog(popFrame, "Number of green numbers must not exceed " + RESULT_LINE_SIZE + ("."),
            "Error: Excess green numbers", JOptionPane.ERROR_MESSAGE);

            return false;
        }

        // Calculate the maximum number of result lines that can be generated with current selections.
        double maxLines = combination(gpool.size(), (RESULT_LINE_SIZE - gresult.size()) );

        // If there is a powerball, multiply the maximum number by 10 to account for 10 possible powerballs.
        if (!powerButton.isSelected() && (gpool.size() + gresult.size() >= 6)) maxLines = maxLines * 10;

        if (maxLines < (int) comboLines.getSelectedItem()) { // If too few yellow and green numbers...
            // Display an error pop up window.
            popFrame = new JFrame();
            JOptionPane.showMessageDialog(popFrame, "<html>With current settings, maximum number of possible result lines is "
            + (int) maxLines + ".<br/> Please select more yellow numbers or deselect green numbers.</html>",
            "Error: Insufficient draw pool of numbers", JOptionPane.ERROR_MESSAGE);

            return false;
        }

        return true;
    }

    //Generates a result line, and updates the labels to display it.
    public void getLine() {
        // Update the "copy" lists to match current draw pool and result lists.
        copygpool.clear();
        copyIndices(gpool, copygpool);
        copygresult.clear();
        copyIndices(gresult, copygresult);

        // Randomly generate numbers from the draw pool until full result line has been generated.
        while (gresult.size() < RESULT_LINE_SIZE) {
            int numDrawn = genRandom(gpool.size());
            gpool.remove(gpool.indexOf(numDrawn));
            gresult.add(numDrawn);
        }

        // Shuffle all the numbers, so that the green numbers added initially are also randomised.
        Collections.shuffle(gresult);
        if (!powerButton.isSelected()) gresult.add(getPowerBall()); //Adds the powerball (this happens for every line)

        updateLabel();

        gpool.clear();
        copyIndices(copygpool, gpool);
        gresult.clear();
        copyIndices(copygresult, gresult);

        /* For testing what is in gpool and gresult afterwards
        System.out.println("pool: " + gpool);
        System.out.println("result: " + gresult);
        */
    }

    // Copies the contents of one list into another.
    public void copyIndices(List<Integer> origArray, List<Integer> copyArray) {
        for (int i = 0; i < origArray.size(); ++i) {
            copyArray.add(origArray.get(i));
        }
    }

    //Picks the index of a random number in gpool.
    public int genRandom(int n) {
        Random rand = new Random();
        int randIndex = rand.nextInt(n);
        return gpool.get(randIndex);
    }

    //this is copied from NumButton. Could possibly just have one method, and in the other class call this method.
    //Or make a completely new class??
    public void updateLabel() {
        //Update text in poolLabel. COMMENTED OUT SO THAT POOL DOESN'T UPDATE WHEN YOU CLICK SUBMIT.
        /*
        Collections.sort(gpool);
        text = "pool: ";
        int iTemp = -10;
        Boolean isChain = false;
        for(int i = 0; i < gpool.size(); i++) {   
            if (iTemp == -10) { //If no numbers have been added yet.
                text = text + gpool.get(i);
                iTemp = gpool.get(i);
            }
            else if (gpool.get(i) == iTemp + 1) { //If the current number is 1 higher than the last.
                if (i == gpool.size() - 1) { //Edge case when at the end of the array.
                    text = text + "-" + gpool.get(i);
                }
                isChain = true;
                iTemp = gpool.get(i);
            }
            else if (isChain) { //If the current number is not 1 higher than the last, and it broke a chain.
                text = text + "-" + iTemp + ", " + gpool.get(i);
                isChain = false;
                iTemp = gpool.get(i);
            }
            else {
                text = text + ", " + gpool.get(i);
                iTemp = gpool.get(i);
            }
            //text = text + bpool.get(i) + ", ";
        }
        gpoolLabel.setText(text);
        */

        //Update text in resultLabel.
        if (!strikeButton.isSelected()) {
            Collections.sort(gresult.subList(0, 6)); //sorts only the first 6 numbers, leaving powerball in place
        }
        text = "";
        for(int i = 0; i < gresult.size(); i++) {   
            /*if (copygresult.contains(gresult.get(i))) {
                text = text + "[" + String.format("%02d", gresult.get(i)) + "]  "; 
            }*/
            if (i == RESULT_LINE_SIZE) { //Powerball
                text = text + "[" + String.format("%02d", gresult.get(i)) + "]";
            }
            else {
                text = text + String.format("%02d", gresult.get(i)) + "  "; //.format is used for 2 digit format
            }

        }
        gresultLabel.setText(text);
    }

    //Returns a random powerball number from 1 t0 10, inclusive.
    public int getPowerBall() {
        Random rand = new Random();
        return (1 + rand.nextInt(10));
    }

}
