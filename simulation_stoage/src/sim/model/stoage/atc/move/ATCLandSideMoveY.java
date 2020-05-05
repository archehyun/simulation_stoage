package sim.model.stoage.atc.move;

import sim.model.SimEvent;
import sim.model.stoage.atc.SimATC;
import sim.model.stoage.atc.StoageEvent;
import sim.model.stoage.block.BlockManager;
import sim.model.stoage.jobmanager.JobManager;
import sim.queue.SimNode;

/**
 * @author LDCC
 *
 */
public class ATCLandSideMoveY extends ATCMove {

	JobManager jobManager = JobManager.getInstance();

	public ATCLandSideMoveY(String simName, SimATC atc) {
		super(simName, atc);
	}

	@Override
	void moveDestination(SimEvent job) {

		System.out.println("move dt:" + getDestination() + "," + atc.getY());
		do {
			if (getDestination() > atc.getY()) {

				atc.plusY();


			} else if (getDestination() < atc.getY()) {
				atc.minusY();
			}
			try {
				Thread.sleep(atc.getSpeed());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.notifyMonitor(this.getSimName() + ", des:" + getDestination() + ",cuY:" + atc.getY() + ", jobID:" + job.getJobID());

		} while (getDestination() != atc.getY() && isFlag());
	}

	@Override
	public void notifyMonitor(String message) {
		atc.notifyMonitor(message);
	}

	@Override
	void moveTP(SimEvent job) {

		System.out.println("move tp:" + getDestination() + "," + atc.getX());
		do {
			if (atc.getInitYpointOnWindows() > atc.getY()) {
				atc.plusY();

			} else if (atc.getInitYpointOnWindows() < atc.getY()) {
				atc.minusY();
			}
			try {
				Thread.sleep(atc.getSpeed());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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
	public void process(SimNode node) {
		StoageEvent job = (StoageEvent) node;

		setDestination((BlockManager.conH + BlockManager.hGap) * job.getY());

		switch (job.eventType) {
		case StoageEvent.INBOUND:

			moveTP(job);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			atc.setLoad(true);
			moveDestination(job);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			atc.setLoad(false);
			job.getSlot().setUsed(false);
			job.getSlot().getBlock().setEmpty(job.getSlot(), false);

			jobManager.release();

			atc.workCount++;

			break;
		case StoageEvent.OUTBOUND:

			moveDestination(job);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			atc.setLoad(true);
			job.getSlot().getBlock().setEmpty(job.getSlot(), true);
			job.getSlot().setUsed(false);
			jobManager = JobManager.getInstance();
			jobManager.release();
			moveTP(job);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			atc.setLoad(false);
			atc.workCount++;
			break;

		default:
			break;
		}

	}


}
