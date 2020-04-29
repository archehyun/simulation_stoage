package sim.view;

import map.cavas.DrawObject;

public abstract class SimViewObject implements DrawObject{
	
	int simID;
	int initX, initY;
	
	protected int viewRate=100;
	
	public  SimViewObject(int simID, int initX, int initY)
	{
		this.initX = initX;
		this.initY = initY;
		this.simID = simID;
	}

}
