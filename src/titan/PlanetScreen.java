package titan;

import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PlanetScreen {
	
	public PlanetScreen() {
		JFrame planetFrame  = new JFrame();
		planetFrame.setSize(1600, 1600);
		planetFrame.setTitle("Journey to Titan");
		planetFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    JPanel panel = new JPanel(new GridBagLayout());
	    panel.setBackground(new Color(0, 0, 0));
	    
	    SunComponent component = new SunComponent();
	    PlanetComponent planet1 = new PlanetComponent();
	    
	    planetFrame.add(component);
	    planetFrame.add(planet1);
	    
	    planetFrame.setVisible(true);
	    
	}

	
    
    
}
