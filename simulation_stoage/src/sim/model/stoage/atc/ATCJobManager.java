package sim.model.stoage.atc;

import java.util.Iterator;
import java.util.Random;

import sim.model.SimEvent;
import sim.model.SimModel;
import sim.model.SimModelManager;
import sim.queue.SimNode;

/**
 * @author ��â��
 *
 */
public class ATCJobManager extends SimModelManager{

	private static ATCJobManager instance;

	private ATCJobManager(String simName) {
		super(simName);
	}
	public static ATCJobManager getInstance()
	{
		if(instance == null)
			instance = new ATCJobManager("atcManager");
		return instance;
	}
	Random rn =new Random();

	@Override
	public void notifySimMessage() {


	}
	@Override
	public void process(SimNode node) {

		//System.out.println("log:process:"+this.getSimName()+","+this.getQueue().getSize());
		this.node = node;

		SimEvent atcJob = (SimEvent) node;

		atcJob.add("atc", list);

		Iterator<SimModel> iter = list.iterator();

		while(iter.hasNext())
		{
			SimATC model = (SimATC) iter.next();

			int blockId = ((StoageEvent) atcJob).getSlot().getBlock().getBlockID();


			if ((model.getAtcID() % 100) == blockId)
			{

				if (model.getAtcID() / 100 == 1) {
					if (((StoageEvent) atcJob).getSlot().getBayIndex() < 12) {

						model.append(atcJob);
						break;
					}
				} else {
					if (((StoageEvent) atcJob).getSlot().getBayIndex() > 12) {

						model.append(atcJob);
						break;
					}
				}

			}
		}

		if (atcJob == null)
			System.err.println("error");
		this.notifyMonitor(atcJob);

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
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public void notifySimMessage(SimModel model) {
		// TODO Auto-generated method stub

	}


}
