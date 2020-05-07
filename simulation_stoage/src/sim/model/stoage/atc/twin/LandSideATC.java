package sim.model.stoage.atc.twin;

import sim.model.SimEvent;
import sim.model.stoage.atc.SimATC;
import sim.model.stoage.atc.StoageEvent;
import sim.model.stoage.atc.move.ATCLandSideMoveY;
import sim.model.stoage.atc.move.ATCMoveX;
import sim.model.stoage.block.BlockManager;
import sim.queue.SimNode;

public class LandSideATC extends SimATC {

	public LandSideATC(String simName, int atcID) {
		super(simName, atcID);
		moveXX = new ATCMoveX(simName + "_x", this);
		moveYY = new ATCLandSideMoveY(simName + "_y", this);
		System.out.println("create landatc:" + atcID);

		notifyMonitor("create landSide atc:" + atcID);

	}

	@Override
	public void setInitLocation(int x, int y) {
		super.setInitLocation(x, y);

	}

	@Override
	public void updateInitLocationOnWinddows(int blockID) {
		initXpointOnWindows = blockID * BlockManager.BLOCK_GAP + BlockManager.magin;

		initYpointOnWindows = getInitY() * (BlockManager.conH + BlockManager.hGap) + BlockManager.magin + BlockManager.conH;
		currentYpointOnWindows = initYpointOnWindows;

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

	//TODO : SINGLE ATC 교차
	//TODO : TWIN ATC 교차
	//TODO : CROSS ATC 교차
	//TODO : 통계



}
