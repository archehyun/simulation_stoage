package sim.view;

import java.awt.Color;
import java.awt.Graphics;

import sim.model.stoage.block.Block;
import sim.model.stoage.block.BlockManager;

public class SimViewBlock extends SimViewObject{


	int xx, yy, ww, hh;
	Block block;

	BlockManager manager = BlockManager.getInstance();

	int hGap=5;

	public SimViewBlock(int blockID, int x, int y)
	{
		super(blockID, x, y);
		block=manager.getBlock(blockID);
	}

	@Override
	public void draw(Graphics g) {


		g.setColor(Color.CYAN);

		for(int i=0;i<block.getBay();i++)
		{
			for(int j=0;j<block.getRow();j++)
			{
				int count=block.slotCount(i, j);

				switch (count) {
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

				g.drawString(count+"", xx ,yy );
			}
		}

		g.setColor(Color.white);


		float total = block.getTotalSlot();
		float current= block.getContainerCount();

		xx = (int) (initX * BlockManager.blockRate);

		yy = (int) ((initY + 10 + block.getBay() * (BlockManager.conH + BlockManager.hGap)) * BlockManager.blockRate);

		float persent = (current/total)*100;
		g.drawString((int) current + "/" + (int) total + "(" + (int) persent + "%)", xx, yy);



	}

}
