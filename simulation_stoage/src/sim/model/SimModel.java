package sim.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import sim.queue.NomalQueue;
import sim.queue.SimNode;
import sim.queue.SimQueue;
import sim.view.IFMonitor;

/**
 * @author 박창현
 *
 */
public abstract class SimModel implements IFSimModel, Runnable{


	/**
	 * 모니터 리스트
	 */
	protected List<IFMonitor> monitors;

	/**
	 * 이벤트 메세지
	 */
	protected SimEvent eventMessage;

	/**
	 * 시뮬레이션 모델 큐
	 */
	private SimQueue queue;

	Thread thread;

	public SimQueue getQueue() {
		return queue;
	}
	/**
	 * 시뮬레이션 모델 이름
	 */
	private String simName;

	public String getSimName() {
		return simName;
	}
	public void setSimName(String simName) {
		this.simName = simName;
	}
	/**
	 * 스레드 플레그
	 */
	private boolean flag;

	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {

		this.flag = flag;
	}
	/**
	 * 시뮬레이션 모델 리스트
	 */
	protected List<IFSimModel> list;

	public SimModel(String simName)
	{
		this.setSimName(simName);
		this.queue = new NomalQueue();
		list = new LinkedList<IFSimModel>();
		monitors = new LinkedList<>();

	}


	@Override
	public void append(SimNode node)
	{
		queue.append(node);
	}


	/**
	 * 모니터에게 메세지 전파 메세지 전파
	 *
	 * @param message
	 */
	public void notifyMonitor(String message)
	{
		Iterator<IFMonitor> iter = monitors.iterator();

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
	 * 모니터에게 메세지 전파 메세지 전파
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
	 */
	public abstract void process(SimNode node);

	@Override
	public void run() {
		while(isFlag())
		{
			SimNode node = queue.poll();
			process(node);
			notifySimMessage();
		}
	}
	/**
	 * 시뮬레이션 시작
	 */
	public void simStart()
	{
		if(isFlag()==false)
		{
			notifyMonitor("log:start:"+simName+",flag:"+isFlag());


			this.setFlag(true);

			thread = new Thread(this);
			thread.start();
		}
	}
	/**
	 *
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
