package sim.model.impl.stoage.atc.twin;

import sim.model.core.SimEvent;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.atc.move.ATCLandSideMoveY;
import sim.model.impl.stoage.atc.move.ATCMoveX;
import sim.model.impl.stoage.commom.BlockManager;
import sim.model.impl.stoage.commom.StoageEvent;
import sim.model.queue.SimNode;

public class TwinLandSideATC extends SimATC {



	public TwinLandSideATC(String simName, int atcID, int blockID, float x, float y, float width, float height) {
		super(simName, atcID, blockID, x, y, width, height);
		moveXX = new ATCMoveX(simName + "_x", this);
		moveYY = new ATCLandSideMoveY(simName + "_y", this);

		notifyMonitor("create landSide atc:" + atcID);

	}
	@Override
	public void updateInitLocationOnWinddows(int blockID) {
		initPosition.x = blockID * BlockManager.BLOCK_GAP + BlockManager.magin;

		initPosition.y = getInitY() * (BlockManager.conH + BlockManager.hGap) + BlockManager.magin + BlockManager.conH;

		//position.y = initPosition.y;

	}


	@Override
	public void notifySimMessage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveTP(SimEvent job) {
		// TODO Auto-generated method stub

	}

	/**
	 * @throws InterruptedException
	 *
	 */
	@Override
	public synchronized void plusY() throws InterruptedException {

		atcJobManager.overlapRectangles(this);

		super.plusY();
	}

	/**
	 * @throws InterruptedException
	 *
	 */
	@Override
	public synchronized void minusY() throws InterruptedException {

		atcJobManager.overlapRectangles(this);

		super.minusY();
	}

	@Override
	public void moveDestination(SimEvent job) {
		// TODO Auto-generated method stub

	}

	@Override
	public void process(SimNode node) {

		SimEvent atcJob = (SimEvent) node;

		StoageEvent event = (StoageEvent) atcJob;
		System.out.println("process:" + event.orderType);
		moveXX.append(node);

		moveYY.append(node);

		setBusy();



		notifyMonitor("land:process:" + this.getSimName() + "initY:" + this.getInitY() + ",currentY:" + this.getY() + ", Y:" + event.getY());

		atcJob = null;
	}

	//TODO : SINGLE ATC 占쏙옙占쏙옙
	//TODO : TWIN ATC 占쏙옙占쏙옙
	//TODO : CROSS ATC 占쏙옙占쏙옙
	//TODO : 占쏙옙占�



}
