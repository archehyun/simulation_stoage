package sim.model.impl.stoage.commom;

import sim.model.core.SimEvent;
import sim.model.core.SimModelManager;
import sim.model.queue.SimNode;

/**
 * @author archehyun
 *
 */
public class BlockManager extends SimModelManager{

	public static float blockRate = (float) 1.0;

	private static BlockManager instance;

	public static int gap = 15;

	public static int bay = 25;

	public static int tier = 5;

	public static final int conH=6;

	public static final int conW=2;

	public static final int ROW=4;

	public static final int hGap=2;

	public static final int wGap=1;

	public static int block = 0;

	public static final int magin = 15;

	public static final int BLOCK_GAP = 50;

	Block blocks[];

	/**
	 * @param blockCount
	 */
	public void setBlockCount(int blockCount)
	{

		this.block = blockCount;
		blocks = new Block[blockCount];

		for(int i=0;i<blockCount;i++)
		{
			blocks[i] = new Block(i, bay, ROW, tier);
		}
	}

	public void blockInit(int blockID) {
		Block block = blocks[blockID];

		for (int j = 0; j < block.getBay(); j++) {
			for (int z = 0; z < block.getRow(); z++) {
				for (int c = 0; c < block.getTier(); c++) {
					if (c < 3) {
						Slot slot = block.getSlot(j, z, c);

						block.setEmpty(slot, false);
					}
				}
			}
		}

	}

	public void blockInit()
	{
		for(int i=0;i<blocks.length;i++)
		{
			blockInit(i);
		}
	}


	private BlockManager(String simName)
	{
		super(simName);
	}
	public static BlockManager getInstance()
	{
		if(instance==null)
			instance = new BlockManager("blockManager");
		return instance;
	}
	@Override
	public void notifySimMessage() {
		// TODO Auto-generated method stub

	}
	@Override
	public void process(SimNode node) {

	}


	public Slot selectEmptySlot(int blockID) {
		// TODO Auto-generated method stub
		return blocks[blockID].selectEmptySlot();
	}

	public Slot selectFilledUpperSlot(int blockID) {
		// TODO Auto-generated method stub
		return blocks[blockID].selectFilledUpperSlot();
	}

	public int getContainerCount(int blockID) throws Exception
	{
		return blocks[blockID].getContainerCount();
	}

	public void setEmpty(int blockID,Slot slot, boolean flag)
	{
		blocks[blockID].setEmpty(slot, flag);
	}


	public Block getBlock(int blockID) {
		// TODO Auto-generated method stub
		return blocks[blockID];
	}

	public int slotCount(int blockID, int bay,int row)
	{
		return blocks[blockID].slotCount(bay, row);
	}

	public Slot getSlot(int blockID, int bay, int row, int tier) throws ArrayIndexOutOfBoundsException {
		return getBlock(blockID).getSlot(bay, row, tier);
	}

	@Override
	public void notifyMonitor(SimEvent message) {

		message.add("blocks", blocks);

		super.notifyMonitor(message);
	}

	public Block[] getBlocks() {
		// TODO Auto-generated method stub
		return blocks;
	}

}
