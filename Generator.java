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
    CommonMethods gcommonMethods = new CommonMethods();

    List<Integer> gpool;
    List<Integer> gresult;
    List<Integer> copygpool = new ArrayList<Integer>(); //for storing contents of orig when generating result line.
    List<Integer> copygresult = new ArrayList<Integer>(); //for storing contents of orig when generating result line.
    List<String> resultLog; //for storing list of result lines with each press of submit button.

    //Options
    JCheckBox strikeButton; //for toggling randomisation
    JToggleButton powerButton; //for toggling inclusion of power ball.
    JPanel linesPanel; //to house the linesLabel and comboLines
    JLabel linesLabel;
    JComboBox<Integer> comboLines; //for deciding the number of result lines to generate.
    String popText; //Temporary string for adding text to the pop up window when displaying result lines.

    //the labels & button
    JLabel gpoolLabel;
    JButton submit;
    JLabel gresultLabel;
    String text; //Temporary string for adding text to labels.

    Container gcontainer;
    GridBagLayout glayout;
    GridBagConstraints gconstraints;

    JFrame popFrame; //for any pop up windows, errors or otherwise.

    public static int RESULT_LINE_SIZE = 6;

    public Generator(Container yourcontainer, List<Integer> pool, List<Integer> result,
    JLabel poolLabel, JLabel resultLabel) {

        //The arraylists
        gpool = pool;
        gresult = result;
        resultLog = new ArrayList<String>();

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

        //The labels & button
        gpoolLabel = poolLabel;
        submit = new JButton("Submit!");
        gresultLabel = resultLabel;

        gcontainer = yourcontainer;
        glayout = new GridBagLayout();
        gcontainer.setLayout(glayout);
        gconstraints = new GridBagConstraints();

        //Might remove the poolLabel and keep it in LOTTOgen.java
        //addComponent(selectButton, gcontainer, glayout, gconstraints, 0, 0, 1, 1);
        //gconstraints.fill = GridBagConstraints.HORIZONTAL;
        gcommonMethods.addComponent(strikeButton, gcontainer, glayout, gconstraints, 0, 0, 2, 1);
        gcommonMethods.addComponent(powerButton, gcontainer, glayout, gconstraints, 0, 1, 2, 1);
        
        gcommonMethods.addComponent(linesPanel, gcontainer, glayout, gconstraints, 0, 2, 2, 1);
        //gconstraints.anchor = GridBagConstraints.EAST;
        gcommonMethods.addComponent(linesLabel, linesPanel, glayout, gconstraints, 0, 2, 1, 1);
        //gconstraints.anchor = GridBagConstraints.WEST;
        gcommonMethods.addComponent(comboLines, linesPanel, glayout, gconstraints, 1, 2, 1, 1);
        //gconstraints.anchor = GridBagConstraints.CENTER;
        gcommonMethods.addComponent(poolLabel, gcontainer, glayout, gconstraints, 0, 3, 2, 1);
        gcommonMethods.addComponent(submit, gcontainer, glayout, gconstraints, 0, 4, 2, 1);
        gcommonMethods.addComponent(resultLabel, gcontainer, glayout, gconstraints, 0, 5, 2, 1);

        submit.addActionListener(event -> {
            resultLog.clear();

            if (!checkSelected()) return;

            if ((int) comboLines.getSelectedItem() == 1) {
                getLine();
                resultLog.add(gresultLabel.getText());
            }
            else { //if it's more than 1
                popText = "<html>";
                for (int i = 1; i <= (int) comboLines.getSelectedItem(); ++i) {
                    do {
                        getLine();
                        if (resultLog.contains(gresultLabel.getText())) System.out.println("copy!");
                        else System.out.println("not copy!");
                    } while (resultLog.contains(gresultLabel.getText()));

                    resultLog.add(gresultLabel.getText());                    
                    //popText = popText + resultLog + "<br/>";

                }
                Collections.sort(resultLog); //This sorts the result lines, but only alphabetically.
                for (int i = 1; i <= (int) comboLines.getSelectedItem(); ++i) {
                    popText = popText + resultLog.get(i - 1) + "<br/>";
                }
                popText = popText + "</html>";
                //System.out.println(popText);

                JLabel popLabel = new JLabel(popText, SwingConstants.CENTER);
                popLabel.setFont(new Font("Arial", Font.BOLD, 20));
                popLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

                popFrame = new JFrame();
                JOptionPane.showMessageDialog(popFrame, popLabel,
                "Here are your numbers", JOptionPane.PLAIN_MESSAGE);
            }

        });

        powerButton.addActionListener(event -> {
            //If comboLines has 1 selected and gresultLabel has a result line in it, add on a powerball.
            Pattern pattern_1 = Pattern.compile("\\d+  \\d+  \\d+  \\d+  \\d+  \\d+  ", Pattern.CASE_INSENSITIVE); //result line
            Matcher matcher_1 = pattern_1.matcher(gresultLabel.getText());
            //System.out.println(gresultLabel.getText());

            if(matcher_1.matches() && !powerButton.isSelected()) {
                gresultLabel.setText(gresultLabel.getText() + "[" + String.format("%02d", getPowerBall()) + "]");
            }
            else if (matcher_1.find() && powerButton.isSelected()) {
                gresultLabel.setText(gresultLabel.getText().substring(0, gresultLabel.getText().length() - 4));
            }
        });
    }

    //Carries out a factorial of num.
    public double fact (double num) {
        double i = 1;
        double j = 1;
    
        while (j <= num) {
            i = i * j;
            ++j;
        }
    
        return i;
    }

    //Carries out combination (nCi)
    public double combination (double n, double i) {
        return fact(n) / (fact(i) * fact(n-i));
    }

    public Boolean checkSelected() {
        if (gresult.size() > RESULT_LINE_SIZE) { //Too many greens
            //error pop up
            popFrame = new JFrame();
            JOptionPane.showMessageDialog(popFrame, "Number of green numbers must not exceed " + RESULT_LINE_SIZE + ("."),
            "Error: Excess green numbers", JOptionPane.ERROR_MESSAGE);

            return false;
        }

        double maxLines = combination(gpool.size(), (RESULT_LINE_SIZE - gresult.size()) );
        if (!powerButton.isSelected() && (gpool.size() + gresult.size() >= 6)) maxLines = maxLines * 10;
        //System.out.println(gpool.size() + " C " + (RESULT_LINE_SIZE - gresult.size()));

        if (maxLines < (int) comboLines.getSelectedItem()) {//Too few selected
            //error pop up
            popFrame = new JFrame();
            JOptionPane.showMessageDialog(popFrame, "<html>With current settings, maximum number of possible result lines is "
            + (int) maxLines + ".<br/> Please select more yellow numbers or deselect green numbers.</html>",
            "Error: Insufficient draw pool of numbers", JOptionPane.ERROR_MESSAGE);

            return false;
        }

        return true;
    }

    //Generates a result line, and updates the labels.
    public void getLine() {
                copygpool.clear();
                copyIndices(gpool, copygpool);
                copygresult.clear();
                copyIndices(gresult, copygresult);

                while (gresult.size() < RESULT_LINE_SIZE) {
                    int numDrawn = genRandom(gpool.size());
                    gpool.remove(gpool.indexOf(numDrawn));
                    gresult.add(numDrawn);
                }
                Collections.shuffle(gresult); //Shuffles all numbers, impt for when strike is on, randomises order of green numbers
                if (!powerButton.isSelected()) gresult.add(getPowerBall()); //Adds the powerball (this happens for every line)
                //System.out.println("gresult: " + gresult);

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
