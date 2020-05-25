package sim.model.impl.stoage.atc.move;

import sim.model.core.SimEvent;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.commom.StoageEvent;
import sim.model.queue.SimNode;

public class ATCLandSideCrossMoveY extends ATCMove {


	public ATCLandSideCrossMoveY(String simName, SimATC atc) {
		super(simName, atc);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void moveTP(SimEvent job) throws InterruptedException {
		do {
			if (atc.getInitYpointOnWindows() > atc.getY()) {
				atc.plusY();

			} else if (atc.getInitYpointOnWindows() < atc.getY()) {
				atc.minusY();
			}
			move();

			this.notifyMonitor(this.getSimName() + ", des:" + getDestination() + ",cuY:" + atc.getY() + ", jobID:" + job.getJobID());

		} while (atc.getInitYpointOnWindows() != atc.getY() && isFlag());

	}

	private void move() {
		try {
			Thread.sleep((long) atc.getSpeed());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void moveDestination(SimEvent job) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifySimMessage() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see sim.model.SimModel#process(sim.queue.SimNode)
	 */
	@Override
	public void process(SimNode node) throws InterruptedException {

		StoageEvent job = (StoageEvent) node;
		moveTP(job);

		atc.workHoist();

		atc.setLoad(true);

		moveDestination(job);

		// hoist work
		atc.workHoist();

		atc.setLoad(false);
		job.getSlot().setUsed(false);
		job.getSlot().getBlock().setEmpty(job.getSlot(), false);

		//jobManager.release();

		atc.plusWorkCount();


	}

}
