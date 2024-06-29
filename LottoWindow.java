import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;

import lottoJPanels.LottoJPanel;
import lottoJPanels.NumPanel;

public class LottoWindow extends JFrame {
    LottoJPanel mainPanel;
    LottoJPanel numPanel;
    LottoJPanel optionsPad;
    LottoJPanel submitPad;

    public LottoWindow(String title) {

        super(title);

        mainPanel = new LottoJPanel(Color.cyan, false);
        this.add(mainPanel);

        numPanel = new NumPanel(Color.orange, true);
        mainPanel.add(numPanel);
        



        numPanel.add(new JButton("Hello I am funny and cool!"), 0, 0, 1, 1);
        numPanel.add(new JButton("Bye!"), 1, 1, 1, 1);

        setMinimumSize(new Dimension(800, 450));
        //setLayout(null);
        setVisible(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
    }
}
