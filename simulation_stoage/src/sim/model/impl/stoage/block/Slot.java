package sim.model.impl.stoage.block;

/**
 * @author LDCC
 *
 */
public class Slot {


	boolean used=false;
	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}
	Block block;
	public Block getBlock()
	{
		return block;
	}

	public int getBlockID() {
		return block.getBlockID();
	}

	public int getTierIndex() {
		return tierIndex;
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public int getBayIndex() {
		return bayIndex;
	}
	int tierIndex;
	int rowIndex;
	int bayIndex;
	boolean empty=true;

	public boolean isEmpty() {
		return empty;
	}

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	public Slot(Block block, int bayIndex, int rowIndex, int tierIndex)
	{
		this.block=block;
		this.bayIndex=bayIndex;
		this.rowIndex=rowIndex;
		this.tierIndex=tierIndex;

	}
	@Override
	public String toString()
	{
		return "[" + bayIndex + "-" + rowIndex + "-" + tierIndex + "-" + isEmpty() + "]";
	}
}
