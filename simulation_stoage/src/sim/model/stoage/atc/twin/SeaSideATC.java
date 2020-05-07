package sim.model.stoage.atc.twin;

import sim.model.SimEvent;
import sim.model.stoage.atc.ATCCommander;
import sim.model.stoage.atc.SimATC;
import sim.model.stoage.atc.StoageEvent;
import sim.model.stoage.atc.move.ATCMoveX;
import sim.model.stoage.atc.move.ATCSeaSideMoveY;
import sim.model.stoage.block.BlockManager;
import sim.model.stoage.jobmanager.JobManager;
import sim.queue.SimNode;

/**
 * @author archehyun
 *
 */
public class SeaSideATC extends SimATC {

	public SeaSideATC(String simName, int atcID) {
		super(simName, atcID);
		moveXX = new ATCMoveX(simName + "_x", this);

		moveYY = new ATCSeaSideMoveY(simName + "_y", this);

		commander = new ATCCommander(simName + "_commander");

		System.out.println("create seaatc:" + atcID);
	}

	@Override
	public void updateInitLocationOnWinddows(int blockID) {

		initXpointOnWindows = blockID * BlockManager.BLOCK_GAP + BlockManager.magin;

		initYpointOnWindows = getInitY() * (BlockManager.conH + BlockManager.hGap) + BlockManager.magin - BlockManager.conH;

		currentYpointOnWindows = initYpointOnWindows;

	}

	@Override
	public void notifySimMessage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveTP(SimEvent job) {
		moveYY.moveTP(job);
	}

	@Override
	public void moveDestination(SimEvent job) {
		moveYY.moveDestination(job);
	}

	@Override
	public void append(SimNode node) {
		super.append(node);
		//commander.append(node);
	}

	@Override
	public void process(SimNode node) throws InterruptedException {

		SimEvent atcJob = (SimEvent) node;

		moveXX.append(node);

		setBusy();

		work(node);

		StoageEvent event = (StoageEvent) atcJob;

		notifyMonitor("sea:process:" + this.getSimName() + "initY:" + this.getInitY() + ",currentY:" + this.getY() + ", Y:" + event.getY());

		atcJob = null;
	}

	/**
	 * @param node
	 * @throws InterruptedException
	 */
	public void work(SimNode node) throws InterruptedException {
		StoageEvent job = (StoageEvent) node;

		moveYY.setDestination((BlockManager.conH + BlockManager.hGap) * job.getY());

		switch (job.eventType) {

		case StoageEvent.INBOUND:

			moveTP(job);

			setLoad(true);

			moveDestination(job);

			hoistWork();

			setLoad(false);

			job.getSlot().setUsed(false);

			job.getSlot().getBlock().setEmpty(job.getSlot(), false);

			jobManager.release();
			plusWorkCount();
			break;
		case StoageEvent.OUTBOUND:

			moveDestination(job);
			hoistWork();

			setLoad(true);
			job.getSlot().getBlock().setEmpty(job.getSlot(), true);
			job.getSlot().setUsed(false);
			jobManager = JobManager.getInstance();
			jobManager.release();
			moveTP(job);
			hoistWork();
			setLoad(false);
			plusWorkCount();
			break;

		default:
			break;
		}
	}

	private void hoistWork() throws InterruptedException {
		Thread.sleep(500);
	}


}
