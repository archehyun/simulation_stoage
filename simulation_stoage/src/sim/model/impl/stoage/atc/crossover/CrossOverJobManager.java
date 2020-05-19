package sim.model.impl.stoage.atc.crossover;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import sim.model.core.SimEvent;
import sim.model.core.SimModel;
import sim.model.impl.stoage.atc.ATCJobManager;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.commom.StoageEvent;
import sim.model.queue.SimNode;

/**
 *
 * cross over type atc manager
 * @author LDCC
 *
 */
public class CrossOverJobManager extends ATCJobManager {

	private static CrossOverJobManager instance;

	private CrossOverJobManager(String simName) {
		super(simName);
	}

	public static ATCJobManager getInstance() {
		if (instance == null)
			instance = new CrossOverJobManager("atcManager");
		return instance;
	}

	/**
	 * @param event
	 */
	private void commandProcess(SimEvent event) {
		switch (event.getCommandType()) {
		case SimEvent.COMMAND_UPDATE_SPEED:
			int speed = (int) event.get("speed");

			Iterator<SimModel> iter = list.iterator();

			while (iter.hasNext()) {
				SimATC model = (SimATC) iter.next();
				model.setSpeed(speed);
			}
			break;

		default:
			break;
		}

	}

	private void orderProcess(SimEvent atcJob) {
		//	atcJob = (SimEvent) node;

		//	System.out.println("command:" + ((StoageEvent) atcJob).getSlot().getBlockID());

		atcJob.add("atc", list);



		int blockID = ((StoageEvent) atcJob).getSlot().getBlock().getBlockID();
		//divied(blockID, atcJob);
		minWorkACT(blockID, atcJob);

		if (atcJob == null)
			System.err.println("error");
		this.notifyMonitor(atcJob);

	}

	SimEvent atcJob;

	private void divied(int blockID, SimEvent atcJob) {
		Iterator<SimModel> iter = list.iterator();

		while (iter.hasNext()) {
			SimATC model = (SimATC) iter.next();

			if ((model.getAtcID() % 100) == blockID) {

				if (model.getAtcID() / 100 == 1) {
					if (((StoageEvent) atcJob).getSlot().getBayIndex() < 12) {

						model.append(atcJob);
						//System.out.println("work atc:" + model.getSimName());
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

		case SimEvent.COMMAND:
			commandProcess(event);
			break;
		case SimEvent.ORDER:
			orderProcess(event);
			break;

		default:
			break;
		}

	}

	public boolean detectCollision(SimATC atc) {

		return true;
	}

	public void minWorkACT(int blockID, SimEvent atcJob) {

		Iterator<SimModel> iter = list.iterator();

		List<SimATC> li = new LinkedList<SimATC>();

		while (iter.hasNext()) {
			SimATC model = (SimATC) iter.next();
			if ((model.getAtcID() % 100) == blockID) {
				li.add(model);
			}
		}


		SimATC first = li.get(0);
		for (int i = 1; i < li.size(); i++) {
			SimATC temp = li.get(i);
			if (first.getWorkCount() > temp.getWorkCount()) {
				first = temp;
			}
		}

		first.append(atcJob);
	}




}
