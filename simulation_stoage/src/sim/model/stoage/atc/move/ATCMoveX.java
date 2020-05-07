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

	
	

	public ATCMoveX(String simName, SimATC atc) {
		super(simName, atc);
		// TODO Auto-generated constructor stub
	}


	public void notifySimMessage() {
		atc.arrival(atc.getX(), atc.getY());
	}

	@Override
	public void moveTP(SimEvent job) {
		do {
			if (atc.getInitX() > atc.getX()) {
				atc.plusX();
			} else if (atc.getInitX() < atc.getX()) {
				atc.minusX();
			}
			try {
				Thread.sleep(atc.getSpeed());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			notifyMonitor(this.getSimName() + ", des:" + getDestination() + ",cuX:" + atc.getX() + ", jobID:" + job.getJobID());

		} while (getDestination() != atc.getX() && isFlag());
	}

	@Override
	public void moveDestination(SimEvent job) {
		do {
			if (getDestination() > atc.getX()) {
				atc.plusX();
			} else if (getDestination() < atc.getX()) {
				atc.minusX();
			}
			try {
				Thread.sleep(atc.getSpeed());
			} catch (InterruptedException e) {
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
