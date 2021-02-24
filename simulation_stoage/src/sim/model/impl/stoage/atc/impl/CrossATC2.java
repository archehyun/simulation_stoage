package sim.model.impl.stoage.atc.impl;

import sim.model.core.SimEvent;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.atc.move.ATCMove2;
import sim.model.impl.stoage.block.BlockManager;
import sim.model.impl.stoage.block.Slot;
import sim.model.impl.stoage.commom.StoageEvent;
import sim.model.queue.SimNode;

public class CrossATC2 extends SimATC {

	StoageEvent activeEvent;

	public CrossATC2(String simName, int id, int blockID, float row, float bay, float width, float height, int type) {
		super(simName, id, blockID, row, bay, width, height, type);

		if (type == CrossATC2.SEA_SIDE) {
			move.setSeaLandType(-1);
		} else {
			move.setSeaLandType(1);
		}
		event2 = new SimEvent();
		event2.add("type", "atc");
		event2.add("item", this);
		logger.debug("create:" + simName);
	}

	@Override
	public void updateInitLocationOnWinddows(int blockID) {
		initPosition.x = blockID * BlockManager.BLOCK_GAP + 7;

		initPosition.y = (getInitBay()) * (BlockManager.conH + BlockManager.hGap);

		logger.info("init Point:" + initPosition);

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

		this.setInOutType(activeEvent.getInOutType());
		activeEvent.workStep = 1;
		logger.info("destination:" + this.getDestination().x + ", " + this.getLocation().x);

		setBusy();
	}

	ATCMove2 move = new ATCMove2(this);

	private final int STEP1 = 1;

	private final int STEP2 = 2;

	private final int STEP3 = 3;

	private final int STEP4 = 4;

	private final int STEP5 = 5;

	private final int STEP6 = 6;

	private final int hoistUpWork = 8;

	private SimEvent event2;

	@Override
	public ATCMove2 getMovingObject() {
		// TODO Auto-generated method stub
		return move;
	}

	@Override
	public void setATCMove(boolean move) {
		this.move.setBayMove(move);
		this.move.setRowMove(move);
	}


	/**
	 * @param workStep
	 */
	private void inboundWork(int workStep) {

		switch (workStep) {

		case STEP1:
			logger.info(this.getAtcID() + " move tp:" + this.getInitRow() + ", " + this.getInitBay());
			//MOVE TP

			this.setDestinationLocation(this.getInitRow(), this.getInitBay());
			move.setBayMove(true);
			move.setRowMove(true);
			activeEvent.workStep++;

			this.notifyMonitor(event2);
			break;

		case STEP2:
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
		case STEP3:

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
		case STEP4:

			if (isArrival()) {

				activeEvent.workStep++;
				logger.info("arrival destination: next:" + activeEvent.workStep);
			}
			/*else {
				System.out.println("move");
				//hoist up work
			}*/

			break;
		case STEP5:

			if (hoistWorkTime < getHoistTime()) {
				hoist = true;
				hoistWorkTime++;

				//				System.out.println("hoist down work:" + activeEvent.getJobID() + ", hoist work time:" + hoistWorkTime);
			} else {

				activeEvent.workStep++;
			}
			break;
		case STEP6:
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
				blockManager.setEmpty(getBlockID(), slot, false);
				//				slot.setUsed(false);
				slot.getBlock().setEmpty(slot, false);
				jobManager.release(getBlockID(), getLocationType(), activeEvent.getTpIndex());
				this.plusWorkCount();
				this.activeEvent = null;
				this.release();
			}

			break;

		default:

			break;
		}
		this.notifyMonitor(event2);
	}



	@Override
	public void update(double delta) {
		this.deltaTime = delta;


		if (activeEvent != null) {

			//	System.out.println("update:" + activeEvent.getInOutType());
			switch (activeEvent.getInOutType()) {
			case StoageEvent.INBOUND:
				inboundWork(activeEvent.workStep);
				break;
			case StoageEvent.OUTBOUND:
				outboundWork(activeEvent.workStep);
				break;

			default:
				break;
			}
		}

		move.update(delta);



	}

	private void outboundWork(int workStep) {
		switch (workStep) {
		case STEP1:
			logger.info(this.getAtcID() + " move destination:" + this.getInitRow() + ", " + this.getInitBay());
			//MOVE TP

			this.setDestinationLocation(activeEvent.getX(), activeEvent.getY());
			move.setBayMove(true);
			move.setRowMove(true);
			activeEvent.workStep++;
			break;
		case STEP2:
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
		case STEP3:

			if (hoistWorkTime > 0) {
				hoist = true;
				hoistWorkTime--;

				//System.out.println("hoist up work:" + activeEvent.getJobID() + ", hoist work time:" + hoistWorkTime);
			} else {
				hoist = false;
				logger.info("arriva tp and move:" + activeEvent.workStep + "," + activeEvent.getX() + ", y:" + activeEvent.getY());
				activeEvent.workStep++; // go to destination step
				this.setDestinationLocation(this.getInitRow(), this.getInitBay());
				this.setLoad(true);
				logger.info("workStep:" + activeEvent.workStep);

				move.setBayMove(true);
				move.setRowMove(true);
			}

			break;
		case STEP4:

			if (isArrival()) {

				activeEvent.workStep++;
				logger.info("arrival destination: next:" + activeEvent.workStep);
			}
			/*else {
				System.out.println("move");
				//hoist up work
			}*/

			break;
		case STEP5:

			if (hoistWorkTime < getHoistTime()) {
				hoist = true;
				hoistWorkTime++;

				//				System.out.println("hoist down work:" + activeEvent.getJobID() + ", hoist work time:" + hoistWorkTime);
			} else {

				activeEvent.workStep++;
			}
			break;
		case STEP6:
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
				//slot.getBlock().setEmpty(slot, true);
				blockManager.setEmpty(getBlockID(), slot, true);
				jobManager.release(getBlockID(), getLocationType(), activeEvent.getTpIndex());

				this.plusWorkCount();
				this.activeEvent = null;
				this.release();
			}

			break;

		default:
			break;
		}
		this.notifyMonitor(event2);

	}

}
