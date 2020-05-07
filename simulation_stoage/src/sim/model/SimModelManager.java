package sim.model;

import java.util.Iterator;

import sim.queue.SimNode;
import sim.view.IFMonitor;

/**
 * @author ��â��
 *
 */
public abstract class SimModelManager extends SimModel{



	protected SimNode node;

	protected SimModelManager(String simName) {
		super(simName);

	}



	@Override
	public void addMonitor(IFMonitor monitor)
	{
		super.addMonitor(monitor);
		Iterator<SimModel> iter = list.iterator();
		while(iter.hasNext())
		{
			SimModel model= iter.next();
			model.addMonitor(monitor);
		}


	}

	/**
	 * �ùķ��̼� ����
	 */
	@Override
	public void simStart()
	{
		super.simStart();
		Iterator<SimModel> iter = list.iterator();
		while(iter.hasNext())
		{
			SimModel model= iter.next();
			model.simStart();
		}
	}
	/**
	 *  �ùķ��̼� ����
	 */
	@Override
	public void simStop()
	{
		super.simStop();
		Iterator<SimModel> iter = list.iterator();
		while(iter.hasNext())
		{
			SimModel model= iter.next();
			model.simStop();
		}
	}

	@Override
	public void notifySimMessage(SimModel model) {
		// TODO Auto-generated method stub

	}



}
