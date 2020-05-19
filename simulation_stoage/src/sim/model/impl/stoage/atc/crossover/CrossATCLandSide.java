package sim.model.impl.stoage.atc.crossover;

import sim.model.core.SimEvent;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.commom.StoageEvent;
import sim.model.queue.SimNode;

/**
 * cross type atc Å¬·¡½º
 * @author LDCC
 *
 */
public class CrossATCLandSide extends SimATC {

	public CrossATCLandSide(String simName, int atcID, int blockID, float x, float y, float width, float height) {
		super(simName, atcID, blockID, x, y, width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateInitLocationOnWinddows(int blockID) {


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
