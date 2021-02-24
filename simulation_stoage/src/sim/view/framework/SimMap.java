package sim.view.framework;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;

import map.cavas.DefaultCanvas;
import map.cavas.DrawObject;

/**
 * @deprecated
 * @author archehyun
 *
 */
@Deprecated
public class SimMap extends DefaultCanvas implements Runnable{

	Thread renderingThread;


	public SimMap() {
		renderingThread = new Thread(this);
		renderingThread.start();
	}
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void run() {
		while(true)
		{
			setFps(16);
			update();
			repaint();

		}
	}

	public void setFps(long fps) {
		try {
			renderingThread.sleep(fps);
		} catch (InterruptedException e) {

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
