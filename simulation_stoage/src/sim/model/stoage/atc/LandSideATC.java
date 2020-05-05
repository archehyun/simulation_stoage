package sim.model.stoage.atc;

import sim.model.stoage.atc.move.ATCLandSideMoveY;
import sim.model.stoage.atc.move.ATCMoveX;
import sim.model.stoage.block.BlockManager;

public class LandSideATC extends SimATC {

	public LandSideATC(String simName, int atcID) {
		super(simName, atcID);
		moveXX = new ATCMoveX(simName + "_x", this);
		moveYY = new ATCLandSideMoveY(simName + "_y", this);

		notifyMonitor("create landSide atc:" + atcID);

	}

	@Override
	public void setInitLocation(int x, int y) {
		super.setInitLocation(x, y);

	}

	@Override
	public void updateInitLocationOnWinddows(int blockID) {
		initXpointOnWindows = blockID * BlockManager.BLOCK_GAP + BlockManager.magin;

		initYpointOnWindows = initY * (BlockManager.conH + BlockManager.hGap) + BlockManager.magin + BlockManager.conH;
		currentYpointOnWindows = initYpointOnWindows;

	}

	@Override
	public void notifySimMessage() {
		// TODO Auto-generated method stub

	}



}
