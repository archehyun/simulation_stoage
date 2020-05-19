package sim.view;

import sim.model.core.SimEvent;
import sim.model.impl.stoage.atc.ATCJobManager;
import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.atc.crossover.CrossOverJobManager;
import sim.model.impl.stoage.atc.twin.TwinJobManager;
import sim.model.impl.stoage.atc.twin.TwinLandSideATC;
import sim.model.impl.stoage.atc.twin.TwinSeaSideATC;
import sim.model.impl.stoage.commom.Block;
import sim.model.impl.stoage.commom.BlockManager;
import sim.model.impl.stoage.commom.JobManager;
import sim.model.impl.stoage.commom.UnparserableCommandException;
import sim.view.framework.SimCanvas;

/**
 * @author  ARCHEHYUN
 *
 */
public class SimMain {

	SimCanvas canvas;

	public void render() {

	}



	public void setCanvas(SimCanvas canvas) {
		this.canvas = canvas;
	}

	ATCJobManager atcManager1 = CrossOverJobManager.getInstance();

	ATCJobManager atcManager2 = TwinJobManager.getInstance();

	JobManager jobManager = JobManager.getInstance();

	BlockManager blockManager = BlockManager.getInstance();


	/**
	 *
	 */
	public void init()
	{

		// block init
		for (int blockID = 0; blockID < BlockManager.block; blockID++) {
			Block blocks = blockManager.getBlock(blockID);
			blocks.setLocation(blockID * BlockManager.BLOCK_GAP + BlockManager.magin, BlockManager.magin);
			canvas.addObject(blocks);

		}

		// ATC Init
		for (int blockID = 0; blockID < BlockManager.block; blockID++) {
			SimATC atc1 = new TwinSeaSideATC("atc_sea-" + blockID, blockID + SimATC.SEA_SIDE, blockID, 0, 0, 0, 0);
			atc1.setInitBlockLocation(0, 0);
			atc1.setSpeed(ATCJobManager.SPEED);

			SimATC atc2 = new TwinLandSideATC("atc_land-" + blockID, blockID + SimATC.LAND_SIDE, blockID, 0, 25, 0, 0);
			atc2.setInitBlockLocation(0, 25);
			atc2.setSpeed(ATCJobManager.SPEED);

			atcManager1.addSimModel(atc1);
			atcManager1.addSimModel(atc2);

			canvas.addObject(atc1);
			canvas.addObject(atc2);
		}


	}

	/**
	 *start simulation
	 *init block view, atc viw
	 */
	public void simulationStart()
	{
		atcManager1.simStart();
		canvas.start();
	}

	/**
	 * simulation stop
	 */
	public void simulationStop()
	{
		SimEvent event = new SimEvent(0);
		event.setEventMessage("simstop");
		jobManager.append(event);
		atcManager1.simStop();
	}

	public SimMain() {


	}


	public void blockInit() {

		blockManager.blockInit();
		SimEvent event = new SimEvent();
		event.add("type", "block");
		blockManager.notifyMonitor(event);


	}

	public void putOrder() {
		jobManager.putOrder();

	}

	public void append(SimEvent event) {
		jobManager.append(event);

	}

	public void putCommand(String command) throws ArrayIndexOutOfBoundsException, UnparserableCommandException {
		jobManager.putCommand(command);
	}

}
