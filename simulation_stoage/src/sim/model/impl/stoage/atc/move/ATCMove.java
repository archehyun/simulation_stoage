package sim.model.impl.stoage.atc.move;

import sim.model.core.SimEvent;
import sim.model.core.SimModel;
import sim.model.impl.stoage.atc.SimATC;

public abstract class ATCMove extends SimModel {

	protected SimATC atc;

	private int destination;

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public ATCMove(String simName) {
		super(simName);
		// TODO Auto-generated constructor stub
	}

	public ATCMove(String simName,SimATC atc)
	{
		this(simName);
		this.atc = atc;
	}

	public abstract void moveTP(SimEvent job) throws InterruptedException;

	public abstract void moveDestination(SimEvent job) throws InterruptedException;

	@Override
	public void notifySimMessage(SimModel model) {
		// TODO Auto-generated method stub

	}

}
