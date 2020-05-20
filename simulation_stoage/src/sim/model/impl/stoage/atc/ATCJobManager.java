package sim.model.impl.stoage.atc;

import java.util.Iterator;
import java.util.Random;

import sim.model.core.SimModel;
import sim.model.core.SimModelManager;

/**
 * @author archehyun
 *
 */
public abstract class ATCJobManager extends SimModelManager {


	int blockID;
	public static int SPEED = 100;

	Random rn =new Random();


	public ATCJobManager(String simName) {
		super(simName);
	}

	/**
	 * @param blockID
	 * @param atcID
	 * @return
	 */
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

	/**
	 * @param atcID
	 * @return ATC
	 */
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

	/**
	 * @return busy atc Count
	 */
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

	/**
	 * @return atcCount
	 */
	public int getATCCount() {
		return list.size();
	}

	public synchronized boolean overlapRectangles(SimATC atc) {
		Iterator<SimModel> iter = list.iterator();


		while (iter.hasNext()) {
			SimATC ONE = (SimATC) iter.next();
			if (ONE.getAtcID() != atc.getAtcID() && ONE.overlapRectangles(atc.bounds)) {
				return true;
			}
		}

		return false;
	}

	public synchronized void setMove(boolean b) {
		Iterator<SimModel> iter = list.iterator();
		while (iter.hasNext()) {
			SimATC ONE = (SimATC) iter.next();
			ONE.setMove(b);
		}
		notifyAll();

	}

	public abstract String getType();
}
