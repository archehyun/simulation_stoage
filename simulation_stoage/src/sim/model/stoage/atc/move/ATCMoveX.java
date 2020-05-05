package sim.model.stoage.atc.move;

import sim.model.SimEvent;
import sim.model.stoage.atc.SimATC;
import sim.model.stoage.atc.StoageEvent;
import sim.model.stoage.block.BlockManager;
import sim.queue.SimNode;

/**
 * @author LDCC
 *
 */
public class ATCMoveX extends ATCMove {

	@Override
	public void notifyMonitor(String message) {
		//this.notifyMonitor(message);
	}


	public ATCMoveX(String simName, SimATC atc) {
		super(simName, atc);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void notifySimMessage() {
		atc.arrival(atc.getX(), atc.getY());
	}

	@Override
	void moveTP(SimEvent job) {
		do {
			if (atc.getInitX() > atc.getX()) {
				atc.plusX();
			} else if (atc.getInitX() < atc.getX()) {
				atc.minusX();
			}
			try {
				Thread.sleep(atc.getSpeed());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			notifyMonitor(this.getSimName() + ", des:" + getDestination() + ",cuX:" + atc.getX() + ", jobID:" + job.getJobID());

		} while (getDestination() != atc.getX() && isFlag());
	}

	@Override
	void moveDestination(SimEvent job) {
		do {
			if (getDestination() > atc.getX()) {
				atc.plusX();
			} else if (getDestination() < atc.getX()) {
				atc.minusX();
			}
			try {
				Thread.sleep(atc.getSpeed());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			notifyMonitor(this.getSimName() + ", des:" + getDestination() + ",cuX:" + atc.getX() + ", jobID:" + job.getJobID());

		} while (getDestination() != atc.getX() && isFlag());
	}

	@Override
	public void process(SimNode node) {
		StoageEvent job = (StoageEvent) node;

		setDestination((BlockManager.conW + BlockManager.wGap) * job.getX());

		moveDestination(job);
	}

}
