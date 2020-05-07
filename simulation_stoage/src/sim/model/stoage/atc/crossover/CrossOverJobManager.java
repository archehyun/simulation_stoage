package sim.model.stoage.atc.crossover;

import java.util.Iterator;

import sim.model.SimEvent;
import sim.model.SimModel;
import sim.model.stoage.atc.ATCJobManager;
import sim.model.stoage.atc.SimATC;
import sim.model.stoage.atc.StoageEvent;
import sim.queue.SimNode;

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

	SimEvent atcJob;
	@Override
	public void process(SimNode node) {

		this.node = node;

		atcJob = (SimEvent) node;

		System.out.println("command:" + ((StoageEvent) atcJob).getSlot().getBlockID());

		atcJob.add("atc", list);

		Iterator<SimModel> iter = list.iterator();


		while (iter.hasNext()) {
			SimATC model = (SimATC) iter.next();

			int blockId = ((StoageEvent) atcJob).getSlot().getBlock().getBlockID();


			if ((model.getAtcID() % 100) == blockId) {

				if (model.getAtcID() / 100 == 1) {
					if (((StoageEvent) atcJob).getSlot().getBayIndex() < 12) {

						model.append(atcJob);
						System.out.println("work atc:" + model.getSimName());
						break;
					}
				} else {
					if (((StoageEvent) atcJob).getSlot().getBayIndex() > 12) {

						model.append(atcJob);
						System.out.println("work atc:" + model.getSimName());
						break;
					}
				}
			}
		}

		if (atcJob == null)
			System.err.println("error");
		this.notifyMonitor(atcJob);

	}

}
