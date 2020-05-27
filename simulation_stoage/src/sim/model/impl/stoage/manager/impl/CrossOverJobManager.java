package sim.model.impl.stoage.manager.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import sim.model.core.SimEvent;
import sim.model.core.SimModel;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.commom.StoageEvent;
import sim.model.impl.stoage.manager.ATCJobManager;
import sim.model.queue.SimNode;

/**
 *
 * cross over type atc manager
 * @author LDCC
 *
 */
public class CrossOverJobManager extends ATCJobManager {

	public CrossOverJobManager(String simName) {
		super(simName);
	}
	/**
	 * @param event
	 */
	private void commandProcess(SimEvent event) {

		Iterator<SimModel> iter = list.iterator();

		switch (event.getCommandType()) {
		case SimEvent.COMMAND_UPDATE_SPEED:
			float speed = (float) event.get("speed");

			while (iter.hasNext()) {
				SimATC model = (SimATC) iter.next();
				model.setSpeed(speed);
			}
			break;
		case SimEvent.COMMAND_MOVE:

			while (iter.hasNext()) {
				SimATC model = (SimATC) iter.next();

				StoageEvent ee = (StoageEvent) event;

				ee.orderType = StoageEvent.COMMAND_MOVE;
				((StoageEvent) event).getSlot().getBlock().getBlockID();

				if (ee.getInOutType() == ee.getInOutType()) {
					model.append(ee);
					logger.info("append atc order:" + event.getSimName());
				}

				/*if ((model.getAtcID() % 100) == blockId) {

					if (model.getAtcID() == ee.getATCID()) {


					}
				}*/
			}
			break;

		default:
			break;
		}
	}

	private void orderProcess(SimEvent atcJob) {

		atcJob.add("atc", list);

		((StoageEvent) atcJob).getSlot().getBlock().getBlockID();

		setATCWork(atcJob);

		if (atcJob == null)
			System.err.println("error");
		//this.notifyMonitor(atcJob);

	}

	private void setATCWork(SimEvent atcJob) {
		sideWorkACT(atcJob);
		//divied(blockID, atcJob);

		//minWorkACT(blockID, atcJob);
	}
	//SimEvent atcJob;

	private void divied(int blockID, SimEvent atcJob) {
		Iterator<SimModel> iter = list.iterator();

		while (iter.hasNext()) {
			SimATC model = (SimATC) iter.next();

			if ((model.getAtcID() % 100) == blockID) {

				if (model.getAtcID() / 100 == 1) {
					if (((StoageEvent) atcJob).getSlot().getBayIndex() < 12) {

						model.append(atcJob);

						break;
					}
				} else {
					if (((StoageEvent) atcJob).getSlot().getBayIndex() > 12) {

						model.append(atcJob);
						break;
					}
				}
			}
		}
	}

	//
	@Override
	public void process(SimNode node) {

		//		this.node = node;

		SimEvent event = (SimEvent) node;

		switch (event.getEventType()) {

		case SimEvent.TYPE_COMMAND:
			commandProcess(event);
			break;
		case SimEvent.TYPE_ORDER:
			orderProcess(event);
			break;

		default:
			break;
		}

	}



	/**
	 *
	 * 理��� WORK ATC ����
	 *
	 * @param blockID
	 * @param atcJob
	 */
	private void minWorkACT(int blockID, SimEvent atcJob) {

		logger.debug("atc list size:" + list.size());

		list.iterator();

		List<SimATC> li = new LinkedList<SimATC>();

		/*while (iter.hasNext()) {
			SimATC model = (SimATC) iter.next();
			if ((model.getAtcID() % 100) == blockID) {
				li.add(model);
			}
		}*/

		try {
			SimATC first = (SimATC) list.get(0);
		for (int i = 1; i < li.size(); i++) {
			SimATC temp = li.get(i);

			if (first.getWorkCount() > temp.getWorkCount()) {
				first = temp;
			}
		}
		//System.out.println("put order:" + first.getAtcID() + ", count:" + first.getWorkCount() + ", " + li.get(0).getWorkCount() + "," + li.get(1).getWorkCount() + ",busy:" + this.getBusyCount());
		first.append(atcJob);
		} catch (Exception e) {
			System.err.println(list.size());
		}
	}

	/**
	 *
	 * 理��� WORK ATC ����
	 *
	 * @param blockID
	 * @param atcJob
	 */
	private void sideWorkACT(SimEvent atcJob) {


		StoageEvent event = (StoageEvent) atcJob;


		System.out.println("pre : block:" + event.getBlockID() + ", " + event.getSeaLandType());

		synchronized (list) {
			try {
				for (int i = 0; i < list.size(); i++) {
					SimATC temp = (SimATC) list.get(i);

					if (temp.getLocationType() == event.getSeaLandType()) {

						System.out.println("set order block:" + this.getBlockID() + ", locationType:" + temp.getLocationType() + ",atcID:" + temp.getAtcID() + ", " + event.getBlockID() + ", " + event.getBay());
						temp.append(atcJob);
						return;
					} else {
						//System.out.println("not set order" + temp.getLocationType() + "," + event.getSeaLandType());
					}
				}
				System.out.println("not set:jobid:" + event.getJobID() + ",type:" + event.getSeaLandType() + ",block:" + event.getBlockID());
				//System.out.println("put order:" + first.getAtcID() + ", count:" + first.getWorkCount() + ", " + li.get(0).getWorkCount() + "," + li.get(1).getWorkCount() + ",busy:" + this.getBusyCount());

			} catch (Exception e) {
				System.err.println(list.size());
			}
		}

	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "cross";
	}



}
