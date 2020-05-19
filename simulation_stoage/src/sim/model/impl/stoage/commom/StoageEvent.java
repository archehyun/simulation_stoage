package sim.model.impl.stoage.commom;

import sim.model.core.SimEvent;

public class StoageEvent extends SimEvent{

	public static final int INBOUND=0;

	public static final int OUTBOUND=1;


	public int orderType;

	public StoageEvent(int jobID, int eventTyper) {
		super(jobID, eventTyper);
	}

	private int atcID;

	/**
	 *À§Ä¡
	 */
	private int x,y;

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getATCID()
	{
		return atcID;
	}

	public void setATCID(int atcID) {
		this.atcID =atcID;

	}

	Slot slot;

	public void setSlot(Slot slot) {
		this.slot =slot;
	}
	public Slot getSlot()
	{
		return slot;
	}




}
