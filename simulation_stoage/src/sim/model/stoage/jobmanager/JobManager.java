package sim.model.stoage.jobmanager;

import java.util.Random;

import sim.model.SimEvent;
import sim.model.SimModelManager;
import sim.model.stoage.atc.ATCJobManager;
import sim.model.stoage.atc.StoageEvent;
import sim.model.stoage.block.BlockManager;
import sim.model.stoage.block.Slot;
import sim.queue.SimNode;

/**
 * @author ï¿½ï¿½Ã¢ï¿½ï¿½
 *
 */
public class JobManager extends SimModelManager{


	WorkOrderGenerate generate;

	private static JobManager instance;

	private int jobID;

	Random rn = new Random();

	boolean flag=false;

	ATCJobManager atcManager = ATCJobManager.getInstance();

	BlockManager blockManager = BlockManager.getInstance();

	Thread thread;

	private JobManager(String simName) {
		super(simName);
		blockManager.setBlockCount(BlockManager.block);
		generate = new WorkOrderGenerate();

		this.simStart();

	}

	public static JobManager getInstance()
	{
		if(instance == null)
			instance = new JobManager("jobManager");
		return instance;
	}

	@Override
	public void notifySimMessage() {
		// TODO Auto-generated method stub

	}

	public synchronized void release()
	{
		generate.release();;
	}

	public void putOrder() {
		generate.putOrder();
	}


	@Override
	public void process(SimNode node) {

		SimEvent event = (SimEvent) node;
		if(event.getEventMessage().equals("simstart"))
		{
			generate.simStart();
		}
		else if(event.getEventMessage().equals("simstop"))
		{
			generate.simStop();
		}
		node = null;
	}
	class WorkOrderGenerate implements Runnable
	{
		Thread thread;

		boolean ready=false;

		boolean flag=false;

		BlockManager blockManager = BlockManager.getInstance();

		public synchronized void release()
		{
			ready = flag;
			notify();
		}

		public synchronized void ready()
		{
			ready =false;
			while(atcManager.getBusyCount()==atcManager.getATCCount())
			{
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		/**
		 * ÁÖ¹® »ý¼º
		 */
		public void putOrder() {
			StoageEvent node = new StoageEvent(jobID);

			Slot slot;

			int n = rn.nextInt(10);

			int blockID = rn.nextInt(BlockManager.block);

			if (n > 3 && blockManager.getContainerCount(blockID) > 0) {
				slot = blockManager.selectFilledUpperSlot(blockID);


				notifyMonitor(Integer.toString(blockManager.getContainerCount(blockID)));
				node.eventType = StoageEvent.OUTBOUND;
			} else {
				slot = blockManager.selectEmptySlot(blockID);

				//blockManager.setEmpty(blockID,slot, false);
				//System.out.println(blockID+", insert:"+slot+","+blockManager.getContainerCount(blockID));
				notifyMonitor(Integer.toString(blockManager.getContainerCount(blockID)));
				node.eventType = StoageEvent.INBOUND;
			}

			// slot null ¿À·ù ¹ß»ý

			try {
				slot.setUsed(true);

				node.setSlot(slot);
				node.setX(slot.getRowIndex());

				node.setY(slot.getBayIndex());

				atcManager.append(node);

				jobID++;
			} catch (NullPointerException e) {
				System.err.println("error block:" + blockID);
				e.printStackTrace();
			}

		}

		@Override
		public void run() {
			while(flag)
			{
				putOrder();
				ready();


			}

		}

		public void simStart()
		{
			if(!flag)
			{
				System.out.println("start jobmanager");
				flag = true;
				thread = new Thread(this);
				jobID =0;
				thread.start();
			}
		}
		public void simStop()
		{
			flag = false;
			thread =null;
			jobID=0;
		}

	}


}
