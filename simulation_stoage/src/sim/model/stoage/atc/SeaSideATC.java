package sim.model.stoage.atc;

import sim.model.stoage.atc.move.ATCMoveX;
import sim.model.stoage.atc.move.ATCSeaSideMoveY;
import sim.model.stoage.block.BlockManager;

public class SeaSideATC extends SimATC {

	public SeaSideATC(String simName, int atcID) {
		super(simName, atcID);
		moveXX = new ATCMoveX(simName + "_x", this);
		moveYY = new ATCSeaSideMoveY(simName + "_y", this);

		System.out.println("create atc:" + atcID);
	}

	@Override
	public void updateInitLocationOnWinddows(int blockID) {
		initXpointOnWindows = blockID * BlockManager.BLOCK_GAP + BlockManager.magin;

		initYpointOnWindows = initY * (BlockManager.conH + BlockManager.hGap) + BlockManager.magin - BlockManager.conH;
		currentYpointOnWindows = initYpointOnWindows;

	}

	@Override
	public void notifySimMessage() {
		// TODO Auto-generated method stub

	}



}
