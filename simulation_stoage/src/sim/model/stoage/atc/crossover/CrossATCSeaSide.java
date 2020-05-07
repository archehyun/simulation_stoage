package sim.model.stoage.atc.crossover;

import sim.model.SimEvent;
import sim.model.stoage.atc.SimATC;
import sim.model.stoage.atc.StoageEvent;
import sim.queue.SimNode;

public class CrossATCSeaSide extends SimATC {

	public CrossATCSeaSide(String simName, int atcID) {
		super(simName, atcID);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateInitLocationOnWinddows(int blockID) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifySimMessage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveTP(SimEvent job) {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveDestination(SimEvent job) {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(SimNode node) {

		SimEvent atcJob = (SimEvent) node;

		moveXX.append(node);

		moveYY.append(node);

		setBusy();
		StoageEvent event = (StoageEvent) atcJob;
		notifyMonitor("land:process:" + this.getSimName() + "initY:" + this.getInitY() + ",currentY:" + this.getY() + ", Y:" + event.getY());

		atcJob = null;
	}

}
