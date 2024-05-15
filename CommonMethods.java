import java.awt.*;
//import java.awt.event.*;
//import javax.swing.*;

public class CommonMethods {
    public void addComponent(Component componente, Container yourcontainer, GridBagLayout layout, GridBagConstraints gbc, int gridx, int gridy, int gridwidth, int gridheight){

        gbc.gridx = gridx;
        gbc.gridy = gridy;

        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;

        layout.setConstraints(componente, gbc);
        yourcontainer.add(componente);
    }

}
