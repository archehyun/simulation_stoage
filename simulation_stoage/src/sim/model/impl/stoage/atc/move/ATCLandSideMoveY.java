package sim.model.impl.stoage.atc.move;

import sim.model.core.SimEvent;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.block.BlockManager;
import sim.model.impl.stoage.commom.JobManager;
import sim.model.impl.stoage.commom.StoageEvent;
import sim.model.queue.SimNode;

/**
 * @author ARCHEHYUN
 *
 */
public class ATCLandSideMoveY extends ATCMove {

	JobManager jobManager = JobManager.getInstance();

	public ATCLandSideMoveY(String simName, SimATC atc) {
		super(simName, atc);
	}

	@Override
	public void moveDestination(SimEvent job) throws InterruptedException {


		do {
			if (getDestination() > atc.getY()) {

				atc.plusY();


			} else if (getDestination() < atc.getY()) {
				atc.minusY();
			}
			try {
				Thread.sleep((long) atc.getSpeed());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			this.notifyMonitor(this.getSimName() + ", des:" + getDestination() + ",cuY:" + atc.getY() + ", jobID:" + job.getJobID());

		} while (getDestination() != atc.getY() && isFlag());
	}



	@Override
	public void moveTP(SimEvent job) throws InterruptedException {

		//System.out.println("move tp:" + getDestination() + "," + atc.getX());
		do {
			if (atc.getInitYpointOnWindows() > atc.getY()) {
				atc.plusY();

			} else if (atc.getInitYpointOnWindows() < atc.getY()) {
				atc.minusY();
			}
			try {
				Thread.sleep((long) atc.getSpeed());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			this.notifyMonitor(this.getSimName() + ", des:" + getDestination() + ",cuY:" + atc.getY() + ", jobID:" + job.getJobID());

		} while (atc.getInitYpointOnWindows() != atc.getY() && isFlag());
	}

	@Override
	public void notifySimMessage() {
		atc.arrival(atc.getX(), atc.getY());
	}

	@Override
	public void notifyMonitor(String message) {
		atc.notifyMonitor(message);
	}

	@Override
	public void process(SimNode node) throws InterruptedException {
		StoageEvent job = (StoageEvent) node;
		setDestination((BlockManager.conH + BlockManager.hGap) * job.getY());

		switch (job.orderType) {
		case StoageEvent.INBOUND:

			moveTP(job);


			// hoist work
			atc.workHoist();

			atc.setLoad(true);

			moveDestination(job);

			// hoist work
			atc.workHoist();

			atc.setLoad(false);
			job.getSlot().setUsed(false);
			job.getSlot().getBlock().setEmpty(job.getSlot(), false);
			atc.arrival();
			atc.plusWorkCount();

			break;
		case StoageEvent.OUTBOUND:

			moveDestination(job);
			atc.workHoist();

			atc.setLoad(true);
			job.getSlot().getBlock().setEmpty(job.getSlot(), true);
			job.getSlot().setUsed(false);
			jobManager = JobManager.getInstance();
			jobManager.release("landSideMove");
			moveTP(job);
			atc.workHoist();

			atc.setLoad(false);
			atc.arrival();
			atc.plusWorkCount();
			break;
		case StoageEvent.COMMAND_MOVE:
			System.out.println("order tye");
			moveDestination(job);
			break;
		default:
			break;
		}

	}


}
