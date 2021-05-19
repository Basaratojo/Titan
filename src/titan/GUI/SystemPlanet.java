package titan.GUI;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;

import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import titan.ProbeSimulatorInterface;
import titan.StateInterface;
import titan.Vector3dInterface;
import titan.GUI.ProbeSimulator;
import titan.GUI.Vector3d;
import titan.GUI.State;
import titan.StateInterface;

public class SystemPlanet extends JPanel {

	// TODO add the labels to the planets so we can identify them more easily :)
	// TODO plotting Titan and the earth's moon
	// TODO plot Rocket
	// -->
	// TODO change the coordinates according to the planet.java inside the
	// titan.impl

	private static final long serialVersionUID = 1L;
	private final boolean DEBUG = false;

	private ArrayList<PlanetGUI> allPlanets = new ArrayList<PlanetGUI>();
	private final GUIWelcome upperFrame;
	private Point imageCorner;
	private Point prevPt;
	private ImageIcon icon;
	private StateInterface[] solvedStates;

	public double size = 1;
	public static int delay = 25;
	public boolean stop = false;
	public int currentState = 0;
	// test for push

	public SystemPlanet(GUIWelcome frame, double speed) {

		this.upperFrame = frame; // pass the GUIWelcome object because we need access to it.

		// Source of image:
		// https://www.pexels.com/photo/fine-tip-on-black-surface-3934623/
		// Edited by the group so that the image is all black for a background
		icon = new ImageIcon(this.getClass().getResource("background.jpg"));
		imageCorner = new Point(0, 0);

		// Create listeners
		ClickListener clickListener = new ClickListener();
		DragListener dragListener = new DragListener();

		// add all the listeners
		this.addMouseListener(clickListener);
		this.addMouseMotionListener(dragListener);
		this.addMouseWheelListener(clickListener);
		
		
		solvedStates = simulateOneYear();
		
		
		// PlanetGUI(JPanel parento, String label, int r, int g, int b, double xCoordinate, double yCoordinate, int diameter)
		
		allPlanets.add(new PlanetGUI(this, "SHIP", 255, 20, 147, ((State) solvedStates[0]).getPosition()[0], 20));
		allPlanets.add(new PlanetGUI(this, "SUN", 255, 140, 0, ((State) solvedStates[0]).getPosition()[1], 50));
		allPlanets.add(new PlanetGUI(this, "MOON", 192, 192, 192, ((State) solvedStates[0]).getPosition()[2], 10));
		allPlanets.add(new PlanetGUI(this, "MERCURY", 128, 128, 128, ((State) solvedStates[0]).getPosition()[3], 10));
		allPlanets.add(new PlanetGUI(this, "VENUS", 207, 153, 52, ((State) solvedStates[0]).getPosition()[4], 20));
		allPlanets.add(new PlanetGUI(this, "EARTH", 0, 0, 255, ((State) solvedStates[0]).getPosition()[5], 20));
		allPlanets.add(new PlanetGUI(this, "MARS", 255, 0, 0, ((State) solvedStates[0]).getPosition()[6], 15));
		allPlanets.add(new PlanetGUI(this, "JUPITER", 255, 140, 0, ((State) solvedStates[0]).getPosition()[7], 45));
		allPlanets.add(new PlanetGUI(this, "SATURN", 112, 128, 144, ((State) solvedStates[0]).getPosition()[8], 42));
		allPlanets.add(new PlanetGUI(this, "URANUS", 196, 233, 238, ((State) solvedStates[0]).getPosition()[9], 50));
		allPlanets.add(new PlanetGUI(this, "TITAN", 218, 165, 32, ((State) solvedStates[0]).getPosition()[10], 10));
		allPlanets.add(new PlanetGUI(this, "NEPTUNE", 66, 98, 243, ((State) solvedStates[0]).getPosition()[11], 38));
		
	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		// Repaint background
		icon.paintIcon(this, g, (int) imageCorner.getX(), (int) imageCorner.getY());

		// Repaint planets
		for (PlanetGUI body : allPlanets) {
			body.draw(g, size, getWidth(), getHeight());
			if (body.label.equals("earth") && DEBUG) {
				System.out.println(body.getX());
				System.out.println(body.getY());
			}
		}
	}

	private void gameLoop() {
		
		while (true) { //&& i < solvedStates.length
			if (!stop) {
				// this is the SUN
				// we must update relative to the sun position
				for (int a = 0; a < 12; a++) {
					allPlanets.get(a).update(((State) solvedStates[currentState]).getPosition()[a]);
				}
				
				// Reset the state if we reach the end
				if(currentState == solvedStates.length-1) {
					currentState = 0;
				}
				
				// Do some changes to the ship so that we can see where it is near the end of the trajectory
				if (currentState > 360 && currentState < 366) {
					allPlanets.get(0).setColor(0, 255, 0);
					allPlanets.get(0).setDiameter(20);
					
					System.out.println("X: " + allPlanets.get(0).getX()  );
					System.out.println("Y: " + allPlanets.get(0).getY()  );
					System.out.println("XTitan: " + allPlanets.get(10).getX()  );
					System.out.println("YTitan: " + allPlanets.get(10).getY()  );
				}
				else {
					allPlanets.get(0).setColor(255, 20, 147);
					allPlanets.get(0).setDiameter(100);
				}
				
				currentState += 1;
			}

			repaint();

			try {
				Thread.sleep(delay);
			} catch (InterruptedException ex) {
				//
			}
			if(DEBUG) System.out.println("Simulating state: " + currentState);

		}
	}
	public static StateInterface[] simulateOneYear() {

		Vector3dInterface probe_relative_position = new Vector3d(6371e3, 0, 0);
		Vector3dInterface probe_relative_velocity = new Vector3d(52500.0, -27000.0, 0); // 12.0 months
		double day = 24 * 60 * 60;
		double year = 365.25 * day;
		ProbeSimulator simulator = new ProbeSimulator();
		StateInterface[] states = simulator.trajectoryGUI(probe_relative_position, probe_relative_velocity, year, day);
		return states;

	}

	public void startMe() {
		Thread thread = new Thread() {

			@Override
			public void run() {
				gameLoop();
			}
		};

		thread.start();

	}

	private class ClickListener extends MouseAdapter {

		public void mousePressed(MouseEvent e) {

			// Obtain the point for the drag function
			prevPt = e.getPoint();
		}

		public void mouseWheelMoved(MouseWheelEvent e) {

			// Zoom in using the button
			if (e.getWheelRotation() < 0) {
				upperFrame.zoomIn.doClick();
			}
			// Zoom out using the button
			if (e.getWheelRotation() > 0) {
				upperFrame.zoomOut.doClick();
			}
		}
	}

	private class DragListener extends MouseMotionAdapter {

		public void mouseDragged(MouseEvent e) {

			Point currentPt = e.getPoint(); // Obtain current clicked point

			// Get the current x and y position
			final double currentX = currentPt.getX();
			final double currentY = currentPt.getY();

			// Get the old x and y position (we got prevPt from the ClickListener class)
			final double oldX = prevPt.getX();
			final double oldY = prevPt.getY();

			// Move the planets
			if(stop == true) { // If the GUI is paused, then executed this
				// This basically updates the drawing for the every planet by the difference in mouse position
				for (PlanetGUI planet : allPlanets) {
					planet.translate((double) currentX - oldX, (double) currentY - oldY);
					if (DEBUG) {
						System.out.println("CurrentX: " + currentX);
						System.out.println("Coordinate of the planet: " + planet.getX());
						System.out.println("Name of the planet: " + planet.label);
					}
				}
			}
			else { // else the GUI is running (so planets are moving), then do this
				// Calculate translation vector (difference in mouse position)
				// Because everything is scaled by 1E9, we also move the translation by that
				Vector3d translate = new Vector3d((currentX - oldX)*1E9, (currentY - oldY)*1E9, 0);
				
				// Then update all states by this vector
				for(int a = 0; a < solvedStates.length; a++) {
					
					// Get current positions of the planet and create a new array of the translated planets which is empty
					Vector3d[] positionsForEveryState = ((State) solvedStates[a]).getPosition();
					Vector3d[] positionsNew = new Vector3d[positionsForEveryState.length];
					
					for(int b = 0; b < positionsForEveryState.length; b++) {
						// Set the translated place to the current place plus the translation
						positionsNew[b] = (Vector3d) positionsForEveryState[b].add(translate);
					}
					
					if(DEBUG) System.out.println("State before translating: " + ((State) solvedStates[0]).getPosition()[0].getX()   );
					((State) solvedStates[a]).setPosition(positionsNew); // Sets new positions from translation
					if(DEBUG) System.out.println("State after translating: " +  ((State) solvedStates[0]).getPosition()[0].getX()   );
				}
				
			}

			prevPt = currentPt; // Reset points
			repaint();
		}
	}

}
