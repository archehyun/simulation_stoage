package sim.model.stoage.atc.move;

import sim.model.SimEvent;
import sim.model.stoage.atc.SimATC;
import sim.model.stoage.atc.StoageEvent;
import sim.model.stoage.block.BlockManager;
import sim.model.stoage.jobmanager.JobManager;
import sim.queue.SimNode;

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
	public void moveDestination(SimEvent job) {


		do {
			if (getDestination() > atc.getY()) {

				atc.plusY();


			} else if (getDestination() < atc.getY()) {
				atc.minusY();
			}
			try {
				Thread.sleep(atc.getSpeed());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			this.notifyMonitor(this.getSimName() + ", des:" + getDestination() + ",cuY:" + atc.getY() + ", jobID:" + job.getJobID());

		} while (getDestination() != atc.getY() && isFlag());
	}



	@Override
	public void moveTP(SimEvent job) {

		//System.out.println("move tp:" + getDestination() + "," + atc.getX());
		do {
			if (atc.getInitYpointOnWindows() > atc.getY()) {
				atc.plusY();

			} else if (atc.getInitYpointOnWindows() < atc.getY()) {
				atc.minusY();
			}
			try {
				Thread.sleep(atc.getSpeed());
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

		switch (job.eventType) {
		case StoageEvent.INBOUND:

			moveTP(job);

			// hoist work
			Thread.sleep(500);

			atc.setLoad(true);

			moveDestination(job);

			// hoist work
			Thread.sleep(500);

			atc.setLoad(false);
			job.getSlot().setUsed(false);
			job.getSlot().getBlock().setEmpty(job.getSlot(), false);

			jobManager.release();

			atc.plusWorkCount();

			break;
		case StoageEvent.OUTBOUND:

			moveDestination(job);
			Thread.sleep(500);

			atc.setLoad(true);
			job.getSlot().getBlock().setEmpty(job.getSlot(), true);
			job.getSlot().setUsed(false);
			jobManager = JobManager.getInstance();
			jobManager.release();
			moveTP(job);
			Thread.sleep(500);

			atc.setLoad(false);
			atc.plusWorkCount();
			break;

		default:
			break;
		}

	}


}
