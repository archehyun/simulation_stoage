package sim.model.impl.stoage.atc.impl;

import sim.model.core.SimEvent;
import sim.model.impl.stoage.atc.ATCCommander;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.atc.move.ATCMoveX;
import sim.model.impl.stoage.atc.move.ATCSeaSideMoveY;
import sim.model.impl.stoage.block.BlockManager;
import sim.model.impl.stoage.commom.StoageEvent;
import sim.model.queue.SimNode;

/**
 * @deprecated
 * @author archehyun
 *
 */
@Deprecated
public class CrossSeaSideATC extends SimATC {



	public CrossSeaSideATC(String simName, int atcID, int blockID, float row, float bay, float width, float height, int type) {
		super(simName, atcID, blockID, row, bay, width, height, type);

		moveXX = new ATCMoveX(simName + "_x", this);

		moveYY = new ATCSeaSideMoveY(simName + "_y", this);

		commander = new ATCCommander(simName + "_commander");

		//atcManager1.addSimModel(this);

	}

	@Override
	public void updateInitLocationOnWinddows(int blockID) {

		this.initPosition.x = blockID * BlockManager.BLOCK_GAP + BlockManager.magin;

		this.initPosition.y = getInitBay() * (BlockManager.conH + BlockManager.hGap) + BlockManager.magin - BlockManager.conH;

		//position.y = initPosition.y;

	}

	@Override
	public void notifySimMessage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveTP(SimEvent job) throws InterruptedException {
		moveYY.moveTP(job);
	}

	@Override
	public void moveDestination(SimEvent job) throws InterruptedException {
		moveYY.moveDestination(job);
	}

	@Override
	public void append(SimNode node) {
		super.append(node);
		//commander.append(node);
	}

	@Override
	public void process(SimNode node) throws InterruptedException {

		SimEvent atcJob = (SimEvent) node;

		plusWorkCount();

		moveXX.append(node);
		moveYY.append(node);
		//setBusy();
		//work(node);

		StoageEvent event = (StoageEvent) atcJob;

		notifyMonitor("sea:process:" + this.getSimName() + "initY:" + this.getInitBay() + ",currentY:" + this.getY() + ", Y:" + event.getY());

		atcJob = null;
	}

	/*	*//**
			* @param node
			* @throws InterruptedException
			*//*
				public void work(SimNode node) throws InterruptedException {
				StoageEvent job = (StoageEvent) node;


				moveYY.setDestination((BlockManager.conH + BlockManager.hGap) * job.getY());

				switch (job.orderType) {

				case StoageEvent.INBOUND:
					moveTP(job);

					setLoad(true);

					moveDestination(job);

					hoistWork();

					setLoad(false);

					job.getSlot().setUsed(false);

					job.getSlot().getBlock().setEmpty(job.getSlot(), false);
					break;
				case StoageEvent.OUTBOUND:
					moveDestination(job);
					atc.hoistWork();
					setLoad(true);
					job.getSlot().getBlock().setEmpty(job.getSlot(), true);
					job.getSlot().setUsed(false);
					jobManager = JobManager.getInstance();
					moveTP(job);

					atc.hoistWork();
					setLoad(false);

					break;
				case StoageEvent.MOVE:
					moveDestination(job);
					break;

				default:
					break;
				}
				}*/



	/**
	 * @throws InterruptedException
	 *
	 */
	@Override
	public synchronized void plusY() throws InterruptedException {

		/*if (atcJobManager.overlapRectangles(this)) {
			isMove = false;
		} else {
		}*/
		//
		/*
				while (!isMove) {
					wait();
				}*/

		super.plusY();
	}

	/**
	 * @throws InterruptedException
	 *
	 */
	@Override
	public synchronized void minusY() throws InterruptedException {

		/*	if (atcJobManager.overlapRectangles(this)) {
				isMove = false;
			} else {
			}*/
		/*while (!isMove) {
			wait();
		}*/

		super.minusY();
	}

}
