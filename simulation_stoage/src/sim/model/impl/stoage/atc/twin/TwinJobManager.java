package sim.model.impl.stoage.atc.twin;

import java.util.Iterator;

import sim.model.core.SimEvent;
import sim.model.core.SimModel;
import sim.model.impl.stoage.atc.ATCJobManager;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.commom.StoageEvent;
import sim.model.queue.SimNode;

public class TwinJobManager extends ATCJobManager {

	private static TwinJobManager instance;

	private TwinJobManager(String simName) {
		super(simName);
	}

	public static ATCJobManager getInstance() {
		if (instance == null)
			instance = new TwinJobManager("atcManager");
		return instance;
	}

	private void commandProcess(SimEvent event) {
		switch (event.getCommandType()) {
		case SimEvent.COMMAND_UPDATE_SPEED:
			int speed = (int) event.get("speed");

			System.out.println("update speed:" + speed);
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

		Iterator<SimModel> iter = list.iterator();


		while (iter.hasNext()) {
			SimATC model = (SimATC) iter.next();

			int blockId = ((StoageEvent) atcJob).getSlot().getBlock().getBlockID();


			if ((model.getAtcID() % 100) == blockId) {

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

		if (atcJob == null)
			System.err.println("error");
		this.notifyMonitor(atcJob);

	}

	SimEvent atcJob;

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

}
