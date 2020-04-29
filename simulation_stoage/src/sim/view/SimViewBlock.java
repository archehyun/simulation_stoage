package sim.view;

import java.awt.Color;
import java.awt.Graphics;

import map.cavas.DrawObject;
import sim.model.stoage.block.Block;
import sim.model.stoage.block.BlockManager;

public class SimViewBlock extends SimViewObject{
	
	
	
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
				
				g.fillRect(initX+ j*(BlockManager.conW+BlockManager.wGap) , initY+i*(BlockManager.conH+(i>0?BlockManager.hGap:0)), 
						
						BlockManager.conW, BlockManager.conH);
				
				g.setColor(Color.black);
				g.drawString(count+"", initX+2+ j*(BlockManager.conW+BlockManager.wGap) , initY+2+10+i*(BlockManager.conH+(i>0?BlockManager.hGap:0)));
			}	
		}
		
		g.setColor(Color.white);
		
		
		float total = block.getTotalSlot();
		float current= block.getContainerCount();
		
		
		float persent = (current/total)*100;
		g.drawString((int)current+"/"+(int)total+"("+(int)persent+"%)", initX , initY+10+block.getBay()*(BlockManager.conH+BlockManager.hGap));
		
		
		
	}

}
