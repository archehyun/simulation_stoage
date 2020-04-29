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
	

	public SimViewATC(int atcID, int x, int y) 
	{	
		super(atcID, x,y);
		atc = manager.getATC(atcID);
		
		atc.setInitLocation(x, y);
		
		trollySizeH = BlockManager.conH+2;
		trollySizeW = BlockManager.conW;
		
		atcW = BlockManager.conW * BlockManager.ROW+4; 
		atcH = BlockManager.conH;
	}

	@Override
	public void draw(Graphics g) {


		if(atc!=null)
		{	
			//Æ®·Ñ¸®
			g.setColor(Color.ORANGE);
			g.fillRect(atc.getInitX()+atc.getX(), atc.getInitY()+atc.getY()-1,
					trollySizeW, trollySizeH);
			
			
			if(atc.isLoad())
			{
			g.setColor(Color.BLUE);
			g.fillRect(atc.getInitX()+2+atc.getX(), atc.getInitY()+atc.getY()+2,
					trollySizeW-3, trollySizeH-3);
			}
			
			g.setColor(Color.WHITE);
			g.drawRect(atc.getInitX(), atc.getInitY()+atc.getY()-2, atcW, atcH);
			
			/*g.setColor(Col or.BLACK);
			g.drawString(atc.getSimName(), atc.getInitX()+3, atc.getInitY()+atc.getY()+11);*/
		}

	}

}
