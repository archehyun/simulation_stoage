package sim.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import sim.queue.SimNode;
import sim.view.IFMonitor;

/**
 * @author ��â��
 *
 */
public abstract class SimModelManager extends SimModel{
	
	/**
	 * �ùķ��̼� �� ����Ʈ
	 */
	protected List<SimModel> list;
	
	protected SimNode node;
	
	protected SimModelManager(String simName) {
		super(simName);
		list = new LinkedList<>();
		
	}
	
	public void addSimModel(SimModel model)
	{
		list.add(model);
		
	}
	
	
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

	
	
	


}
