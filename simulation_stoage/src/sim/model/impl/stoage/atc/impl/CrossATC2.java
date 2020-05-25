package sim.model.impl.stoage.atc.impl;

import sim.model.core.SimEvent;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.atc.move.ATCMove2;
import sim.model.impl.stoage.block.Slot;
import sim.model.impl.stoage.commom.StoageEvent;
import sim.model.queue.SimNode;

public class CrossATC2 extends SimATC {

	StoageEvent activeEvent;

	public CrossATC2(String simName, int id, int blockID, float row, float bay, float width, float height, int type) {
		super(simName, id, blockID, row, bay, width, height, type);

		if (type == CrossATC2.SEA_SIDE) {
			move.setSeaLandType(1);
		} else {
			move.setSeaLandType(1);
		}

		logger.debug("create:" + simName);
	}

	@Override
	public void updateInitLocationOnWinddows(int blockID) {
		//initPosition.x = blockID * BlockManager.BLOCK_GAP + BlockManager.magin;

		//initPosition.y = (getInitBay()) * (BlockManager.conH + BlockManager.hGap) + getInitYpointOnWindows();


		System.out.println("set init y:" + initPosition.y + ", current:" + this.getLocation().y);

	}

	@Override
	public void moveTP(SimEvent job) throws InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveDestination(SimEvent job) throws InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifySimMessage() {
		// TODO Auto-generated method stub

	}

	StoageEvent event;
	@Override
	public void process(SimNode node) throws InterruptedException {

		activeEvent = (StoageEvent) node;
		activeEvent.workStep = 1;
		logger.debug("destination:" + this.getDestination().x + ", " + this.getLocation().x);

		setBusy();
	}

	ATCMove2 move = new ATCMove2(this);

	//int workStep = 0;

	private final int start = 1;

	private final int moveTP = 2;

	private final int hoistDownInTP = 3;

	private final int moveDestination = 4;

	private final int hoistDownWork = 5;

	private final int hoistWork = 6;

	private final int hoistUpWork = 8;

	private void inboundWork(int workStep) {
		switch (workStep) {

		case start:
			logger.info(this.getAtcID() + " move tp:" + this.getInitRow() + ", " + this.getInitBay());
			//MOVE TP

			this.setDestinationLocation(this.getInitRow(), this.getInitBay());
			move.setBayMove(true);
			move.setRowMove(true);
			activeEvent.workStep++;
			break;

		case moveTP:
			if (isArrival()) {

				if (hoistWorkTime < getHoistTime()) {
					hoist = true;
					hoistWorkTime++;

					//					System.out.println("hoist down work:" + activeEvent.getJobID() + ", hoist work time:" + hoistWorkTime);
				} else {
					//hoist down work end and go to next step
					activeEvent.workStep++;
				}


			}

			break;
		case hoistDownInTP:

			if (hoistWorkTime > 0) {
				hoist = true;
				hoistWorkTime--;

				//System.out.println("hoist up work:" + activeEvent.getJobID() + ", hoist work time:" + hoistWorkTime);
			} else {
				hoist = false;
				logger.info("arriva tp and move:" + activeEvent.workStep + "," + activeEvent.getX() + ", y:" + activeEvent.getY());
				activeEvent.workStep++; // go to destination step
				this.setDestinationLocation(activeEvent.getX(), activeEvent.getY());
				this.setLoad(true);
				logger.info("workStep:" + activeEvent.workStep);

				move.setBayMove(true);
				move.setRowMove(true);
			}



			break;
		case moveDestination:

			if (isArrival()) {

				activeEvent.workStep++;
				logger.info("arrival destination: next:" + activeEvent.workStep);
			}
			/*else {
				System.out.println("move");
				//hoist up work
			}*/

			break;
		case hoistDownWork:

			if (hoistWorkTime < getHoistTime()) {
				hoist = true;
				hoistWorkTime++;

				//				System.out.println("hoist down work:" + activeEvent.getJobID() + ", hoist work time:" + hoistWorkTime);
			} else {

				activeEvent.workStep++;
			}
			break;
		case hoistWork:
			if (hoistWorkTime > 0) {
				hoist = true;
				hoistWorkTime--;

				//				System.out.println("hoist down work:" + activeEvent.getJobID() + ", hoist work time:" + hoistWorkTime);
			} else {
				logger.info("work end");
				hoist = false;
				this.setLoad(false);
				hoistWorkTime = 0;
				Slot slot = activeEvent.getSlot();
				slot.setUsed(false);
				slot.getBlock().setEmpty(slot, false);
				this.activeEvent = null;
				this.release();
			}

			break;

		default:

			break;
		}
	}

	@Override
	public void update(double delta) {
		this.deltaTime = delta;

		move.update(delta);
		if (activeEvent != null) {

			//	System.out.println("update:" + activeEvent.getInOutType());
			switch (activeEvent.getInOutType()) {
			case StoageEvent.INBOUND:
				inboundWork(activeEvent.workStep);
				break;
			case StoageEvent.OUTBOUND:
				inboundWork(activeEvent.workStep);
				break;

			default:
				break;
			}
		}



	}


}
