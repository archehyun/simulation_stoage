package sim.model.impl.stoage.atc.crossover;

import java.util.Timer;
import java.util.TimerTask;

import sim.model.core.SimEvent;
import sim.model.core.SimModel;
import sim.model.impl.stoage.atc.ATCJobManager;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.atc.move.ATCLandSideMoveY;
import sim.model.impl.stoage.atc.move.ATCMoveX;
import sim.model.impl.stoage.commom.BlockManager;
import sim.model.impl.stoage.commom.Slot;
import sim.model.impl.stoage.commom.StoageEvent;
import sim.model.queue.SimNode;

/**
 * cross type atc Ŭ����
 * @author LDCC
 *
 */
public class CrossATCLandSide extends SimATC {

	ATCJobManager atcManager1 = CrossOverJobManager.getInstance();

	MyTimer time;

	public CrossATCLandSide(String simName, int atcID, int blockID, float x, float y, float width, float height) {
		super(simName, atcID, blockID, x, y, width, height);
		moveXX = new ATCMoveX(simName + "_x", this);
		moveYY = new ATCLandSideMoveY(simName + "_y", this);

		time = new MyTimer("");



		notifyMonitor("create landSide atc:" + atcID);

	}

	class ScheduledJob extends TimerTask {

		   @Override
		public void run() {
			if (lastX == CrossATCLandSide.this.getX())
			{
				StoageEvent node = new StoageEvent(0, SimEvent.COMMAND);
				Slot slot = blockManager.getSlot(0, 24, 1, 1);


				node.setSlot(slot);
				node.setCommandType(SimEvent.COMMAND_MOVE);
				node.setATCID(SimATC.LAND_SIDE);

				node.setX(1);

				node.setY(25);
				CrossATCLandSide.this.append(node);
			}
		   }
	}

	int lastX;
	@Override
	public void notifySimMessage(SimModel model) {
		lastX = this.getX();
		//time.schedule(6000);

	}

	@Override
	public void updateInitLocationOnWinddows(int blockID) {
		initPosition.x = blockID * BlockManager.BLOCK_GAP + BlockManager.magin;

		initPosition.y = getInitY() * (BlockManager.conH + BlockManager.hGap) + BlockManager.magin + BlockManager.conH;

		//position.y = initPosition.y;

	}

	class MyTimer {

		ScheduledJob job = new ScheduledJob();
		//Timer timer;
		public MyTimer(String string) {
			//timer = new Timer();
		}

		public void schedule(TimerTask task, long time) {

			try {
				job = new ScheduledJob();
				Timer timer = new Timer();
				timer.schedule(job, time);
			} catch (Exception e) {
				e.printStackTrace();

			}

		}


		private boolean isStarted = true;
	}


	@Override
	public void notifySimMessage() {
		lastX = this.getX();
		//time.schedule( 5000);



	}

	@Override
	public void moveTP(SimEvent job) {
		// TODO Auto-generated method stub

	}

	/**
	 * @throws InterruptedException
	 *
	 */
	@Override
	public synchronized void plusY() throws InterruptedException {

		if (!atcManager1.overlapRectangles(this)) {
			atcManager1.setMove(true);
		}

		super.plusY();
	}

	/**
	 * @throws InterruptedException
	 *
	 */
	@Override
	public synchronized void minusY() throws InterruptedException {

		if (!atcManager1.overlapRectangles(this)) {
			atcManager1.setMove(true);
		}

		super.minusY();
	}

	@Override
	public void moveDestination(SimEvent job) {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(SimNode node) {

		SimEvent atcJob = (SimEvent) node;
		plusWorkCount();
		StoageEvent event = (StoageEvent) atcJob;
		System.out.println("process:" + event.orderType);
		moveXX.append(node);

		moveYY.append(node);

		setBusy();



		notifyMonitor("land:process:" + this.getSimName() + "initY:" + this.getInitY() + ",currentY:" + this.getY() + ", Y:" + event.getY());

		atcJob = null;
	}

	//TODO : SINGLE ATC ����
	//TODO : TWIN ATC ����
	//TODO : CROSS ATC ����
	//TODO : ���

}

