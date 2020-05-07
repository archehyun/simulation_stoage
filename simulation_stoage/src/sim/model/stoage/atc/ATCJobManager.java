package sim.model.stoage.atc;

import java.util.Iterator;
import java.util.Random;

import sim.model.SimModel;
import sim.model.SimModelManager;

/**
 * @author archehyun
 *
 */
public abstract class ATCJobManager extends SimModelManager {




	Random rn =new Random();


	public ATCJobManager(String simName) {
		super(simName);
	}

	public SimATC getATC(int blockID,int atcID)
	{
		Iterator<SimModel> iter = list.iterator();

		while(iter.hasNext())
		{
			SimATC model = (SimATC) iter.next();

			if(model.getAtcID()==blockID)
			{
				return model;
			}
		}
		return null;
	}

	public SimATC getATC(int atcID)
	{
		Iterator<SimModel> iter = list.iterator();

		while(iter.hasNext())
		{
			SimATC model = (SimATC) iter.next();

			if(model.getAtcID()==atcID)
			{
				return model;
			}
		}
		return null;
	}
	public int getBusyCount() {


		Iterator<SimModel> iter = list.iterator();
		int count=0;
		while(iter.hasNext())
		{
			SimATC model = (SimATC) iter.next();

			if(!model.isIdle())
			{
				count++;
			}
		}

		return count;
	}

	public int getATCCount() {
		return list.size();
	}

	@Override
	public void notifySimMessage(SimModel model) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifySimMessage() {

	}


}
