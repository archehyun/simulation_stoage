package sim.view;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;

import map.cavas.DefaultCanvas;
import map.cavas.DrawObject;

public class SimMap extends DefaultCanvas implements Runnable{

	Thread thread;
	
	public SimMap() {
		thread = new Thread(this);
		thread.start();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void run() {
		while(true)
		{	
			update();
			repaint();

			try {
				Thread.sleep(17);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void drawTime(Graphics g)
	{
		g.setColor(Color.orange);
		g.drawString(currentTime, endX-100, 35);
	}


	@Override
	public void paint(Graphics g) {

		g.setColor(Color.black);

		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		drawGrayGrid(g);
		//drawGrid(g);
		//drawTime(g);

		if(isPointShow())
		{
			//drawLine(g,pointList);
			//drawLine(g,resultList);
		}
		Iterator<DrawObject> iter = draw.iterator();
		while(iter.hasNext())
		{
			iter.next().draw(g);
		}

	}
	
	private void update()
	{	
		updateMapInfo();
		/*resultList = manager.getResultList();
		pointList =manager.getPointList();
		pointMoveList = manager.getPointMoveList();
		tagLocationList = manager.getTagLocationList();*/
	}

}
