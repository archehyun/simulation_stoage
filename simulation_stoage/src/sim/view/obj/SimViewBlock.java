package sim.view.obj;

import java.awt.Color;
import java.awt.Graphics;

import sim.model.core.SimEvent;
import sim.model.impl.stoage.block.Block;
import sim.model.impl.stoage.block.BlockManager;
import sim.view.framework.SimViewObject;

public class SimViewBlock extends SimViewObject{


	private int blockContainerCount;

	private int xx, yy;

	private Block block;

	BlockManager manager = BlockManager.getInstance();

	int hGap=5;

	private float totalSlot;

	private float persent;

	/**
	 * @param id
	 * @param x
	 * @param y
	 */
	public SimViewBlock(int id, int x, int y)
	{
		super(id, x, y, 0, 0);

		block=manager.getBlock(id);

		totalSlot = block.getTotalSlot();

		manager.addMonitor(this);
	}

	@Override
	public void draw(Graphics g) {

		drawTP(g, BlockManager.blockRate);

		drawBlock(g, BlockManager.blockRate);

		drawStatics(g, BlockManager.blockRate);


	}

	private void drawTP(Graphics g, float blockRate) {

		g.setColor(Color.blue);
		float ww = (BlockManager.conW * blockRate);
		float hh = (BlockManager.conH * blockRate);

		int xx,yy;
		//sea side

		int TP[] = block.getTP();
		for (int j = 0; j < TP.length; j++) {
			xx = (int) ((initX + j * (BlockManager.conW + BlockManager.wGap)) * blockRate);
			yy = (int) ((initY - (BlockManager.conH + BlockManager.hGap)) * blockRate);

			if (TP[j] == Block.FULL_TP)
			{
			g.fillRect(xx, yy, (int) ww, (int) hh);
			} else if (TP[j] == Block.EMPTY_TP) {
				g.drawRect(xx, yy, (int) ww, (int) hh);
			}
		}
		//land side
		for (int j = 0; j < block.getRow(); j++) {
			xx = (int) ((initX + j * (BlockManager.conW + BlockManager.wGap)) * blockRate);
			yy = (int) ((initY + (block.getBay()) * (BlockManager.conH + BlockManager.hGap)) * blockRate);

			if (TP[j] == Block.FULL_TP) {
			g.fillRect(xx, yy, (int) ww, (int) hh);
			} else if (TP[j] == Block.EMPTY_TP) {
				g.drawRect(xx, yy, (int) ww, (int) hh);
			}
		}

	}

	private void drawStatics(Graphics g, float blockRate) {
		g.setColor(Color.white);

		int xx = (int) (initX * blockRate);

		int yy = (int) ((initY + 10 + block.getBay() * (BlockManager.conH + BlockManager.hGap)) * blockRate);

		g.drawString(blockContainerCount + "/" + (int) totalSlot + "(" + (int) persent + "%)", xx, yy);
	}

	private void drawBlock(Graphics g, float blockRate) {

		g.setColor(Color.CYAN);

		for(int i=0;i<block.getBay();i++)
		{
			for(int j=0;j<block.getRow();j++)
			{
				int blockContainerCount = block.slotCount(i, j);

				switch (blockContainerCount) {
				case 2:

					g.setColor(Color.gray);

					break;
				case 3:

					g.setColor(Color.GREEN);

					break;
				case 4:


					g.setColor(Color.BLUE);

				case 5:
					g.setColor(Color.RED);

					break;
				default:

					g.setColor(Color.CYAN);
					break;

				}

				int xx = (int) ((initX + j * (BlockManager.conW + BlockManager.wGap)) * blockRate);
				int yy = (int) ((initY + i * (BlockManager.conH + (i > 0 ? BlockManager.hGap : 0))) * blockRate);

				float ww = (BlockManager.conW * blockRate);
				float hh = (BlockManager.conH * blockRate);


				g.fillRect(xx, yy, (int) ww, (int) hh);

				if (isCountView) {

					g.setColor(Color.black);

					g.drawString(blockContainerCount + "", xx - 1, yy + 11);
				}
			}

			g.setColor(Color.white);
			yy = (int) ((initY + i * (BlockManager.conH + (i > 0 ? BlockManager.hGap : 0))) * blockRate);
			g.drawString(i + "", xx + 5, yy);
		}
	}

	@Override
	public void updateMonitor(SimEvent message) {
		String type = (String) message.get("type");
		if (type.equals("block")) {
			try {
				blockContainerCount = block.getContainerCount();

				persent = (blockContainerCount / totalSlot) * 100;
			} catch (Exception e) {
				persent = 0;
			}

		}
	}

	boolean isCountView;

	@Override
	public void setCountView(boolean selected) {
		isCountView = selected;
	}

}
