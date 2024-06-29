package lottoJPanels;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

public class LottoJPanel extends JPanel {
    GridBagLayout layout;
    GridBagConstraints constraints;

    public LottoJPanel(Color color, Boolean addBorder) {
        layout = new GridBagLayout();
        setLayout(layout);

        constraints = new GridBagConstraints();

        setBackground(color);
        if (addBorder) setBorder(createBorder());
    }

    /**
     * Creates default border to be used in every LottoJPanel.
     * 
     * @return Border 
     */
    private Border createBorder() {
        Border emptyBorder = new EmptyBorder(20, 40, 20, 40);
        Border bevelBorder = new BevelBorder(BevelBorder.RAISED);
        Border etchedBorder = new EtchedBorder(EtchedBorder.RAISED);

        Border compoundBorder1 = new CompoundBorder(bevelBorder, etchedBorder);
        Border compoundBorder2 = new CompoundBorder(compoundBorder1, emptyBorder);

        return compoundBorder2;
    }

    /**
     * Adds a component to the LottoJPanel using the gridbag layout.
     * 
     * @param component
     * @param gridX
     * @param gridY
     * @param gridWidth
     * @param gridHeight
     */
    public void add(
            Component component,
            int gridX,
            int gridY,
            int gridWidth,
            int gridHeight
    ){

        constraints.gridx = gridX;
        constraints.gridy = gridY;

        constraints.gridwidth = gridWidth;
        constraints.gridheight = gridHeight;

        layout.setConstraints(component, constraints);
        this.add(component);
    }
}
