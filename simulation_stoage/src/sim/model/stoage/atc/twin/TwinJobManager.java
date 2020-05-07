package sim.model.stoage.atc.twin;

import java.util.Iterator;

import sim.model.SimEvent;
import sim.model.SimModel;
import sim.model.stoage.atc.ATCJobManager;
import sim.model.stoage.atc.SimATC;
import sim.model.stoage.atc.StoageEvent;
import sim.queue.SimNode;

/**
 * @author archehyun
 * @version 0.1
 *
 *
 */
public class TwinJobManager extends ATCJobManager {

	private static TwinJobManager instance;

	public TwinJobManager(String simName) {
		super(simName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void process(SimNode node) {

		this.node = node;

		SimEvent atcJob = (SimEvent) node;

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

	public static ATCJobManager getInstance() {
		// TODO Auto-generated method stub
		return null;
	}

}
