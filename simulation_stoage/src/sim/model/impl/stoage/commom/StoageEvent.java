package sim.model.impl.stoage.commom;

import sim.model.core.SimEvent;
import sim.model.impl.stoage.block.Slot;

public class StoageEvent extends SimEvent{

	public static final int INBOUND=0;

	public static final int OUTBOUND=1;

	public static final int LAND_OUTBOUND = 2;

	public static final int LAND_INBOUND = 3;

	public static final int SEA = 0;

	public static final int LAND = 1;

	private int inOutType;

	public int getInOutType() {
		return inOutType;
	}

	public void setInOutType(int inOutType) {
		this.inOutType = inOutType;
	}

	private int seaLandType;

	public int getSeaLandType() {
		return seaLandType;
	}

	public void setSeaLandType(int seaLandType) {
		this.seaLandType = seaLandType;
	}

	public int orderType;

	public StoageEvent(int jobID, int eventTyper) {
		super(jobID, eventTyper);
	}

	private int atcID;



	private int x,y;

	public int getX() {
		return this.x;
	}

	public int getRow() {
		return this.getX();
	}

	public int getBay() {
		return this.getY();
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

	private int blockID;

	public int workStep;

	public void setSlot(Slot slot) {
		this.slot =slot;
	}
	public Slot getSlot()
	{
		return slot;
	}

	public void setBlockID(int blockID) {
		this.blockID = blockID;

	}

	public int getBlockID() {
		// TODO Auto-generated method stub
		return blockID;
	}

	int tpIndex;

	public void setTPIndex(int tpIndex) {
		this.tpIndex = tpIndex;
	}


}
