package sim.model.core;

import java.util.Iterator;

import sim.model.queue.SimNode;
import sim.view.framework.IFMonitor;

/**
 * @author 박창현
 *
 */
public abstract class SimModelManager extends SimModel{



	protected SimNode node;

	protected SimModelManager(String simName) {
		super(simName);

	}



	/* (non-Javadoc)
	 * @see sim.model.SimModel#addMonitor(sim.view.IFMonitor)
	 */
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

	/*
	 * 시뮬레이션 시작
	 * 하위 모듈 시작
	 *
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
	 *  시뮬레이션 종료
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

	@Override
	public void notifySimMessage() {

	}



}
