package sim.view;

import java.awt.Color;
import java.awt.Graphics;

import sim.model.stoage.atc.ATCJobManager;
import sim.model.stoage.atc.SimATC;
import sim.model.stoage.block.BlockManager;

public class SimViewATC extends SimViewObject{
	ATCJobManager manager = ATCJobManager.getInstance();

	SimATC atc;

	int row=6;

	int trollySizeW, trollySizeH ;

	int atcW, atcH;

	int xx, yy, ww, hh;


	public SimViewATC(int atcID, int blockID, int x, int y)
	{
		super(atcID, x,y);

		atc = manager.getATC(atcID);

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
			//Ʈ�Ѹ�

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

			int xx = (int) ((atc.getInitXpointOnWindows() + atc.getX()) * BlockManager.blockRate);
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

}
