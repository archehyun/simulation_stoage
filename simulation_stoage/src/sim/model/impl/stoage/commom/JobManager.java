package sim.model.impl.stoage.commom;

import java.util.Random;
import java.util.regex.Pattern;

import sim.model.core.SimEvent;
import sim.model.core.SimModelManager;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.block.BlockManager;
import sim.model.impl.stoage.block.Slot;
import sim.model.impl.stoage.manager.ATCManager;
import sim.model.queue.SimNode;

/**
 * @author archehyun
 *
 */
public class JobManager extends SimModelManager{

	WorkOrderGenerate generate;

	private static JobManager instance;

	private int jobID;

	private Random rn = new Random();

	boolean flag=false;

	ATCManager manager = ATCManager.getInstance();

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

	public synchronized void release(String name)
	{
		System.out.println(name);
		generate.release();;
	}

	/**
	 * put order
	 */
	public void putOrder() {
		try {
			generate.putOrder();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

			minusActiveOrder();
			System.out.println("release:" + activeOrderCount);


		}

		private synchronized void plusActiveOrder() {
			activeOrderCount++;
			System.out.println("plus:" + activeOrderCount);
			notifyAll();
		}

		private synchronized void minusActiveOrder() {
			activeOrderCount--;
			notifyAll();
		}

		int activeOrderCount = 0;

		public synchronized void ready()
		{
			ready = false;
			/*while (activeOrderCount == 4)
				{
					try {
						System.out.println("ready:" + activeOrderCount);
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}*/

			try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
		}

		/**
		 * put order
		 * @throws Exception
		 */
		public void putOrder() throws Exception {
			StoageEvent node = new StoageEvent(jobID,SimEvent.ORDER);

			node.setEventType(SimEvent.ORDER);
			Slot slot;

			int n = rn.nextInt(10);

			int blockID = rn.nextInt(BlockManager.block);

			int containerCount = blockManager.getContainerCount(blockID);

			if (n > 3 && containerCount > 0) {
				slot = blockManager.selectFilledUpperSlot(blockID);

				notifyMonitor(Integer.toString(containerCount));

				node.orderType = StoageEvent.OUTBOUND;

			} else {
				slot = blockManager.selectEmptySlot(blockID);

				node.orderType = StoageEvent.INBOUND;

				notifyMonitor(Integer.toString(blockManager.getContainerCount(blockID)));
			}

			// slot null ���� �߻�

			try {
				slot.setUsed(true);

				node.setSlot(slot);
				node.setX(slot.getRowIndex());

				node.setY(slot.getBayIndex());

				manager.getATCManager(blockID).append(node);


				plusActiveOrder();
				jobID++;


			} catch (NullPointerException e) {
				System.err.println("error block:" + blockID);
				e.printStackTrace();
			}
		}

		@Override
		public void run() {

			activeOrderCount = 0;

			while(flag)
			{
				try {
					ready();
					putOrder();


				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

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

			String commandStr = commands[0];
			String inOutType = commands[1];
			int bay = Integer.parseInt(commands[2]);
			int row = Integer.parseInt(commands[3]);
			int tier = Integer.parseInt(commands[4]);

			Slot slot;

			if (commandStr.equals("W")) {

				for (int i = 0; i < BlockManager.block; i++) {
					StoageEvent node = new StoageEvent(jobID, SimEvent.ORDER);

					slot = blockManager.getSlot(i, bay, row, tier);
					try {
						int containerCount = blockManager.getContainerCount(i);
						notifyMonitor(Integer.toString(containerCount));

						if (inOutType.equals("I")) {
							node.orderType = StoageEvent.INBOUND;
						} else {
							node.orderType = StoageEvent.OUTBOUND;
						}
					} catch (Exception e) {
						System.err.println(e.getMessage());
						continue;
					}

					// slot null ���� �߻�

					try {
						slot.setUsed(true);

						node.setSlot(slot);

						node.setX(row);

						node.setY(bay);


						//	System.out.println("input:" + node.getSlot().getBlockID());

						manager.getATCManager(0).append(node);


					} catch (NullPointerException e) {
						System.err.println("error block:" + i);
						e.printStackTrace();
					}

				}

			} else if (commandStr.equals("M")) {
				//System.out.println("Move");
				StoageEvent node = new StoageEvent(jobID, SimEvent.COMMAND);
				slot = blockManager.getSlot(0, bay, row, tier);

				System.out.println("slot:" + slot);
				node.setSlot(slot);
				node.setCommandType(SimEvent.COMMAND_MOVE);

				if (inOutType.equals("I")) {
					node.setATCID(SimATC.LAND_SIDE);
				} else {
					node.setATCID(SimATC.SEA_SIDE);
				}

				node.setX(row);

				node.setY(bay);
				manager.getATCManager(0).append(node);

			}


		} else {
			throw new UnparserableCommandException(command);
		}

	}



}
