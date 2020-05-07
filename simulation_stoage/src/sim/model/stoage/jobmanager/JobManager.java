package sim.model.stoage.jobmanager;

import java.util.Random;
import java.util.regex.Pattern;

import sim.model.SimEvent;
import sim.model.SimModelManager;
import sim.model.stoage.atc.ATCJobManager;
import sim.model.stoage.atc.StoageEvent;
import sim.model.stoage.atc.crossover.CrossOverJobManager;
import sim.model.stoage.block.BlockManager;
import sim.model.stoage.block.Slot;
import sim.queue.SimNode;

/**
 * @author archehyun
 *
 */
public class JobManager extends SimModelManager{

	WorkOrderGenerate generate;

	private static JobManager instance;

	private int jobID;

	Random rn = new Random();

	boolean flag=false;

	ATCJobManager atcManager = CrossOverJobManager.getInstance();

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
					e.printStackTrace();
				}
			}
		}

		/**
		 * 주문 생성
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

				node.eventType = StoageEvent.INBOUND;

				notifyMonitor(Integer.toString(blockManager.getContainerCount(blockID)));
			}

			// slot null 오류 발생

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

	/**
	 * 1. commandName : move(M), Work(W)
	 * 2. I-O Type : I, O
	 * 3. BAY : 0~25
	 * 4. ROW : 1~4
	 * 5. TIER : 1~6
	 * EX : W-I-15-1-1
	 * @param tf
	 * @throws UnparserableCommandException
	 */

	String pattern = "^([M]|[W])-([I]|[O])-([1-9]{1}|[0-1]{1}[0-9]{1}|[2]{1}[0-5]{1})-([0-5]{1})-([0-5]{1})$";

	StoageEvent commandNode;
	public void putCommand(String command) throws UnparserableCommandException, ArrayIndexOutOfBoundsException {

		Pattern.compile(command, Pattern.CASE_INSENSITIVE);


		boolean bol = Pattern.matches(pattern, command);
		if (bol == true) {

			String commands[]=command.split("-");
			String inOutType = commands[1];
			int bay = Integer.parseInt(commands[2]);
			int row = Integer.parseInt(commands[3]);
			int tier = Integer.parseInt(commands[4]);

			Slot slot;

			for (int i = 0; i < BlockManager.block; i++) {
				StoageEvent node = new StoageEvent(jobID);

				slot = blockManager.getSlot(i, bay, row, tier);

				notifyMonitor(Integer.toString(blockManager.getContainerCount(i)));

				if (inOutType.equals("I")) {
					node.eventType = StoageEvent.INBOUND;
				} else {
					node.eventType = StoageEvent.OUTBOUND;
				}



				// slot null 오류 발생

				try {
					slot.setUsed(true);

					node.setSlot(slot);

					node.setX(row);

					node.setY(bay);

					System.out.println("input:" + node.getSlot().getBlockID());

					atcManager.append(node);


				} catch (NullPointerException e) {
					System.err.println("error block:" + i);
					e.printStackTrace();
				}

			}


			/*if (n > 3 && blockManager.getContainerCount(blockID) > 0) {
				slot = blockManager.getSlot(blockID, bay, row, tier);

				notifyMonitor(Integer.toString(blockManager.getContainerCount(blockID)));

				node.eventType = StoageEvent.OUTBOUND;

			} else {
				slot = blockManager.getSlot(blockID, bay, row, tier);

				node.eventType = StoageEvent.INBOUND;

				notifyMonitor(Integer.toString(blockManager.getContainerCount(blockID)));
			}
			*/


		} else {
			throw new UnparserableCommandException(command);
		}

	}



}
