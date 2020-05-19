package sim.view.obj;

import java.awt.Color;
import java.awt.Graphics;

import sim.model.core.SimEvent;
import sim.model.impl.stoage.atc.ATCJobManager;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.atc.crossover.CrossOverJobManager;
import sim.model.impl.stoage.commom.BlockManager;
import sim.view.framework.Rectangle;
import sim.view.framework.SimViewObject;

/**
 *
 * ATC ǥ�� �̹��� ��ü
 *
 * @author LDCC
 *
 */
public class SimViewATC extends SimViewObject {

	ATCJobManager manager = CrossOverJobManager.getInstance();

	SimATC atc;

	int row=6;

	//	Ʈ�Ѹ� ������
	int trollySizeW, trollySizeH ;

	// ATC ������
	int atcW, atcH;

	// ǥ�� ������
	int xx, yy, ww, hh;


	/**
	 * ������
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

		atc = manager.getATC(atcID);

		System.out.println(atc);

		atc.updateInitLocationOnWinddows(blockID);

		trollySizeH = BlockManager.conH+2;
		trollySizeW = BlockManager.conW;

		atcW = BlockManager.conW * BlockManager.ROW+4;
		atcH = BlockManager.conH;
	}

	@Override
	public void draw(Graphics g) {

		if(atc!=null)
		{
			// �ε� ���� ǥ��

			if (atc.isLoad()) {
				g.setColor(Color.BLUE);

				/*int xxx = (int) ((atc.getInitXpointOnWindows() + 2 + atc.getX()) * BlockManager.blockRate);
				int yyy = (int) ((atc.getY() + 2) * BlockManager.blockRate);

				int www = (int) ((trollySizeW - 1) * BlockManager.blockRate);
				int hhh = (int) ((trollySizeH - 1) * BlockManager.blockRate);
				g.fillRect(xxx, yyy, www, hhh);*/
			} else {
				g.setColor(Color.ORANGE);
			}

			int xx = (int) ((atc.getX()) * BlockManager.blockRate);
			int yy = (int) ((atc.getY() - 1) * BlockManager.blockRate);

			int ww = (int) (trollySizeW * BlockManager.blockRate);
			int hh = (int) (trollySizeH * BlockManager.blockRate);
			g.fillRect(xx, yy,
					ww, hh);


			/*if(atc.isLoad())
			{
				g.setColor(Color.BLUE);

				int xxx = (int) ((atc.getInitXpointOnWindows() + 2 + atc.getX()) * BlockManager.blockRate);
				int yyy = (int) ((atc.getY() + 2) * BlockManager.blockRate);

				int www = (int) ((trollySizeW - 1) * BlockManager.blockRate);
				int hhh = (int) ((trollySizeH - 1) * BlockManager.blockRate);
				g.fillRect(xxx, yyy, www, hhh);
			}*/

			g.setColor(Color.WHITE);

			int xx1 = (int) (atc.getInitXpointOnWindows() * BlockManager.blockRate);
			int yy1 = (int) ((atc.getY() - 2) * BlockManager.blockRate);
			int ww1 = (int) (atcW * BlockManager.blockRate);
			int hh1 = (int) (atcH * BlockManager.blockRate);
			g.drawRect(xx1, yy1, ww1, hh1);

			/*g.setColor(Col or.BLACK);
			g.drawString(atc.getSimName(), atc.getInitX()+3, atc.getInitY()+atc.getY()+11);*/
		}
	}

	@Override
	public void updateMonitor(SimEvent message) {
		// TODO Auto-generated method stub

	}

	public boolean overlapRectangles(Rectangle r1, Rectangle r2) {
		if (r1.lowerLeft.x < r2.lowerLeft.x + r2.width && r1.lowerLeft.x + r1.width > r2.lowerLeft.x && r1.lowerLeft.y < r2.lowerLeft.y + r2.height && r1.lowerLeft.y + r1.height > r2.lowerLeft.y)
			return true;
		else
			return false;
	}

}
