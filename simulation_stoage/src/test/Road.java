package test;

import java.awt.Color;
import java.util.Vector;

import javax.swing.JFrame;

public class Road extends JFrame
{

	private Vector<JButtonPlus> registeredCars = null;

	private void launchCar(int x, int y, Color c, boolean horizontal, int interval)
	{
		JButtonPlus b = new JButtonPlus();
		registeredCars.addElement(b);
		b.setBackground(c);
		b.setSize(10, 10);
		b.setLocation(x, y);
		this.add(b);

		(new CarThread(b, horizontal, interval, registeredCars)).start();
	}

	public Road()
	{
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Road Environment");
		this.setSize(600, 400);
		this.setLayout(null);
		registeredCars = new Vector<JButtonPlus>();

		launchCar(100, 200, Color.red, true, 20);
		launchCar(100, 200, Color.YELLOW, true, 30);
		launchCar(200, 200, Color.blue, true, 35);
		launchCar(280, 200, Color.GREEN, true, 60);
		launchCar(300, 80, Color.GRAY, false, 45);
		launchCar(250, 100, Color.GRAY, false, 50);
		launchCar(350, 100, Color.GRAY, false, 55);
		launchCar(330, 90, Color.GRAY, false, 49);
		launchCar(200, 60, Color.GRAY, false, 20);
		launchCar(200, 100, Color.GRAY, false, 20);
		this.setVisible(true);
	}
}
