package sim.view.obj;

import java.awt.Color;
import java.awt.Graphics;

import sim.model.core.SimEvent;
import sim.model.impl.stoage.commom.Block;
import sim.model.impl.stoage.commom.BlockManager;
import sim.view.framework.SimViewObject;

public class SimViewBlock extends SimViewObject{


	private int blockContainerCount;

	private int xx, yy, ww, hh;

	private Block block;

	BlockManager manager = BlockManager.getInstance();

	int hGap=5;

	private float totalSlot;

	private float persent;

	/**
	 * @param blockID
	 * @param x
	 * @param y
	 */
	public SimViewBlock(int blockID, int x, int y)
	{
		super(blockID, x, y, 0, 0);

		block=manager.getBlock(blockID);

		totalSlot = block.getTotalSlot();

		manager.addMonitor(this);
	}

	@Override
	public void draw(Graphics g) {


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

				xx = (int) ((initX + j * (BlockManager.conW + BlockManager.wGap)) * BlockManager.blockRate);
				yy = (int) ((initY + i * (BlockManager.conH + (i > 0 ? BlockManager.hGap : 0))) * BlockManager.blockRate);

				ww = (int) (BlockManager.conW * BlockManager.blockRate);
				hh = (int) (BlockManager.conH * BlockManager.blockRate);


				g.fillRect(xx, yy, ww, hh);

				g.setColor(Color.black);

				xx = (int) ((initX + 2 + j * (BlockManager.conW + BlockManager.wGap)) * BlockManager.blockRate);
				yy = (int) ((initY + 2 + 10 + i * (BlockManager.conH + (i > 0 ? BlockManager.hGap : 0))) * BlockManager.blockRate);

				g.drawString(blockContainerCount + "", xx - 5, yy);
			}

			g.setColor(Color.white);
			yy = (int) ((initY + i * (BlockManager.conH + (i > 0 ? BlockManager.hGap : 0))) * BlockManager.blockRate);
			g.drawString(i + "", xx + 5, yy);
		}

		g.setColor(Color.white);

		xx = (int) (initX * BlockManager.blockRate);

		yy = (int) ((initY + 10 + block.getBay() * (BlockManager.conH + BlockManager.hGap)) * BlockManager.blockRate);

		g.drawString(blockContainerCount + "/" + (int) totalSlot + "(" + (int) persent + "%)", xx, yy);


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

}
