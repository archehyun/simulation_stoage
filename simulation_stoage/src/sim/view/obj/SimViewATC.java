package sim.view.obj;

import java.awt.Color;
import java.awt.Graphics;

import sim.model.core.SimEvent;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.block.BlockManager;
import sim.model.impl.stoage.manager.ATCJobManager;
import sim.model.impl.stoage.manager.ATCManager;
import sim.view.framework.SimViewObject;
import sim.view.framework.Vector2;

/**
 *
 * ATC ǥ�� �̹��� ��ü
 *
 * @author LDCC
 *
 */
public class SimViewATC extends SimViewObject {

	Color ATC_COLOR = Color.white;

	Color TROLLY_COLOR = Color.orange;

	Color BUSY_COLOR = Color.blue;

	ATCJobManager manager = null;

	SimATC atc;

	int row=6;

	//	troly size;
	int trollySizeW, trollySizeH ;

	// ATC ������
	int atcW, atcH;

	// ǥ�� ������
	int xx, yy, ww, hh;


	/**
	 * draw ATC object
	 *
	 * @param atcID
	 * @param blockID
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public SimViewATC(int atcID, int blockID, int x, int y, int width, int height)
	{
		super(atcID, x, y, width, height);

		manager = ATCManager.getInstance().getATCManager(blockID);

		atc = manager.getATC(atcID);

		atc.updateInitLocationOnWinddows(blockID);

		trollySizeH = BlockManager.conH+2;
		trollySizeW = BlockManager.conW;

		atcW = BlockManager.conW * BlockManager.ROW+4;
		atcH = BlockManager.conH;
	}

	private void drawATCState(Graphics g)
	{
		g.setColor(ATC_COLOR);

		//atc
		Vector2 atcLocation = atc.getLocation();

		int xx = (int) ((atc.getInitXpointOnWindows() + 25) * BlockManager.blockRate);
		int yy2 = (int) ((atcLocation.y - 1) * BlockManager.blockRate);
		switch (atc.getLocationType()) {
		case SimATC.TYPE_SEA:

			break;
		case SimATC.TYPE_LAND:
			yy2 -= 45;

			break;

		default:
			break;
		}

		int ww1 = (int) (atcW * BlockManager.blockRate);
		g.fillRect(xx, yy2, ww1, 5);

		g.fillRect(xx - 2, yy2, 2, 15);
		g.fillRect(xx + ww1, yy2, 2, 15);


		// draw trolly
		if (atc.isLoad()) {
			g.setColor(BUSY_COLOR);

		switch (atc.getLocationType()) {
		case SimATC.TYPE_SEA:

			break;
		case SimATC.TYPE_LAND:
			break;

		default:
			break;
		}

			g.fillRect(xx, yy2 + 5, 5, 5);
		} else {
			g.setColor(TROLLY_COLOR);
		}

		//hosist
		if (atc.isHoist()) {

			float rate = atc.hoistWorkTime / atc.getHoistTime();
			float h = 10f * rate;

			g.fillRect(xx, yy2 + 5, 5, (int) h);
		}

	}
	@Override
	public void draw(Graphics g) {

		if(atc!=null)
		{
			drawATC(g);

			drawATCState(g);

		}
	}

	/**
	 * draw moving atc
	 *
	 * state
	 * busy :blue
	 * idle :yellow
	 *
	 * @param g
	 */
	private void drawATC(Graphics g) {
		if (atc.isLoad()) {
			g.setColor(BUSY_COLOR);


		} else {
			g.setColor(TROLLY_COLOR);
		}

		Vector2 atcLocation = atc.getLocation();

		int xx = (int) ((atcLocation.x) * BlockManager.blockRate);
		int yy = (int) ((atcLocation.y - 1) * BlockManager.blockRate);

		int ww = (int) (trollySizeW * BlockManager.blockRate);
		int hh = (int) (trollySizeH * BlockManager.blockRate);
		g.fillRect(xx, yy, ww, hh);
		//System.out.println("xx:" + xx + ",yy:" + yy);
		g.setColor(Color.WHITE);

		int xx1 = (int) (atc.getInitXpointOnWindows() * BlockManager.blockRate);
		int yy1 = (int) ((atc.getY() - 2) * BlockManager.blockRate);
		int ww1 = (int) (atcW * BlockManager.blockRate);
		int hh1 = (int) (atcH * BlockManager.blockRate);
		g.drawRect(xx1, yy1, ww1, hh1);


		g.setColor(Color.black);
		g.drawString(String.valueOf(atc.getAtcID()), xx1 + ww1, yy1 + 10);
	}

	@Override
	public void updateMonitor(SimEvent message) {
		// TODO Auto-generated method stub

	}

	boolean isCountView = true;

	@Override
	public void setCountView(boolean selected) {
		isCountView = selected;
	}

}
