package sim.model.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import sim.model.queue.NomalQueue;
import sim.model.queue.SimNode;
import sim.model.queue.SimQueue;
import sim.view.framework.IFMonitor;

/**
 * @author archehyun
 *
 */
public abstract class SimModel implements IFSimModel, Runnable {

	Logger logger = Logger.getLogger(getClass());

	/**
	 * �ùķ��̼� �� ����Ʈ
	 */
	protected List<SimModel> list;

	public void addSimModel(SimModel model) {
		list.add(model);

	}

	/**
	 * ����� ����Ʈ
	 */
	protected List<IFMonitor> monitors;

	/**
	 * �̺�Ʈ �޼���
	 */
	protected SimEvent eventMessage;

	/**
	 * �ùķ��̼� �� ť
	 */
	private SimQueue queue;

	/**
	 * �ùķ��̼� ������
	 */
	private Thread thread;

	public SimQueue getQueue() {
		return queue;
	}
	/**
	 * �ùķ��̼� �� �̸�
	 */
	protected String simName;

	public String getSimName() {
		return simName;
	}
	public void setSimName(String simName) {
		this.simName = simName;
	}

	/**
	 * thread flag
	 */
	private boolean flag;

	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {

		this.flag = flag;
	}


	public SimModel(String simName)
	{

		this.simName = simName;
		this.queue = new NomalQueue();
		this.list = new LinkedList<SimModel>();
		this.monitors = new LinkedList<>();

		logger.debug("init:" + simName);

	}


	/* (non-Javadoc)
	 * @see sim.model.IFSimModel#append(sim.queue.SimNode)
	 */
	@Override
	public void append(SimNode node)
	{
		queue.append(node);
	}


	/**
	 * ����Ϳ��� �޼��� ���� �޼��� ����
	 *
	 * @param message
	 */
	public void notifyMonitor(String message)
	{
		Iterator<IFMonitor> iter = monitors.iterator();
		/*
		 * TODO �̺�Ʈ ���� ����
		 */
		eventMessage = new SimEvent(0);

		eventMessage.setSimName(getSimName());

		eventMessage.setEventMessage(message);

		while(iter.hasNext())
		{
			IFMonitor monitor= iter.next();
			monitor.updateMonitor(eventMessage);
		}
	}

	/**
	 * ����Ϳ��� �޼��� ���� �޼��� ����
	 *
	 * @param message
	 */
		public void notifyMonitor(SimEvent message) {

		if (monitors == null || message == null)
			return;
		Iterator<IFMonitor> iter = monitors.iterator();
		message.setSimName(getSimName());
		while (iter.hasNext()) {
			IFMonitor monitor = iter.next();
			monitor.updateMonitor(message);
		}
	}

	/**
	 *
	 */
	public abstract void notifySimMessage();

	/**
	 *
	 */
	public abstract void notifySimMessage(SimModel model);


	/**
	 * @param node
	 * @throws InterruptedException
	 */
	public abstract void process(SimNode node) throws InterruptedException;

	@Override
	public void run() {
		while(isFlag())
		{
			SimNode node = queue.poll();
			try {
				process(node);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			notifySimMessage();
		}
	}

	/**
	 * start Simulation
	 */
	public void simStart()
	{
		if(isFlag()==false)
		{
			notifyMonitor("log:start:"+simName+",flag:"+isFlag());


			this.setFlag(true);

			thread = new Thread(this, this.getSimName());
			thread.start();
		}
	}

	/**
	 *stop simulation
	 */
	public void simStop()
	{
		setFlag(false);
		thread = null;
	}
	/**
	 *
	 */
	public void simPluse()
	{
		setFlag(false);
	}
	public void addMonitor(IFMonitor monitor)
	{
		this.monitors.add(monitor);

	}


}
