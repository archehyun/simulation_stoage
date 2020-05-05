package sim.model.stoage.atc.move;

import sim.model.SimEvent;
import sim.model.SimModel;
import sim.model.stoage.atc.SimATC;

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

	abstract void moveTP(SimEvent job);

	abstract void moveDestination(SimEvent job);

	@Override
	public void notifySimMessage(SimModel model) {
		// TODO Auto-generated method stub

	}


}
