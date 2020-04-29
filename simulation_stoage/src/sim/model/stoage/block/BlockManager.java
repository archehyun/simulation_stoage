package sim.model.stoage.block;

import sim.model.SimModelManager;
import sim.queue.SimNode;

public class BlockManager extends SimModelManager{
	
	private static BlockManager instance;
	
	public static final int conH=6;
	
	public static final int conW=2;

	public static final int ROW=4;
	
	public static final int hGap=2;
	
	public static final int wGap=1;

	public static final int block = 4;
	
	Block blocks[];
	
	public void setBlockCount(int count)
	{
		blocks = new Block[count];
		
		for(int i=0;i<count;i++)
		{
			blocks[i] = new Block(i, 20, 4, 5);
		}
	}
	
	public void init()
	{
		for(int i=0;i<blocks.length;i++)
		{
			Block block = blocks[i];
			
			for(int j=0;j<block.getBay();j++)
			{
				for(int z=0;z<block.getRow();z++)
				{
					for(int c=0;c<block.getTier();c++)
					{
						if(c<3)
						{
							Slot slot=block.getSlot(j, z, c);
							
							block.setEmpty(slot, false);
						}
					}
					
				}
				
			}
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
		// TODO Auto-generated method stub
		
	}


	public Slot selectEmptySlot(int blockID) {
		// TODO Auto-generated method stub
		return blocks[blockID].selectEmptySlot();
	}
	
	public Slot selectFilledUpperSlot(int blockID) {
		// TODO Auto-generated method stub
		return blocks[blockID].selectFilledUpperSlot();
	}
	
	public int getContainerCount(int blockID)
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

}
