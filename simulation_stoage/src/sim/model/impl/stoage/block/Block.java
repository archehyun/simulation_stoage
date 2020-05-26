package sim.model.impl.stoage.block;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import sim.model.core.IFSimModel;
import sim.model.queue.SimNode;

/**
 * 占쏙옙 占쏙옙占쏙옙 표占쏙옙 클占쏙옙占쏙옙
 * @author archehyun
 *
 */
public class Block implements IFSimModel {

	public static final int FULL_TP = 1;

	public static final int EMPTY_TP = 0;

	int x, y;

	int TP[];

	public int[] getTP() {
		return TP;
	}

	public int getX() {
		return x;
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

	/**
	 * tier
	 */
	private int tier;

	/**
	 * row
	 */
	int row;

	/**
	 * bay
	 */
	int bay;

	/**
	 * blockID
	 */
	int blockID;

	/**
	 * Total Slot Count
	 */
	int totalSlot;

	public int getTier() {
		return tier;
	}

	public int getRow() {
		return row;
	}

	public int getBay() {
		return bay;
	}



	public int getTotalSlot() {
		return totalSlot;
	}

	public int getBlockID() {
		return blockID;
	}

	/**
	 * total container count
	 */
	int containerCount;

	/**
	 * block array
	 */
	int block[][][];

	/**
	 * slot array
	 */
	Slot block_slots[][][];

	/**
	 * @param bay
	 * @param row
	 * @param tier
	 */
	public Block(int blockID,int bay, int row, int tier) {


		this.blockID = blockID;
		this.bay=bay;
		this.row = row;
		this.tier =tier;

		// init slot count
		totalSlot = bay*row*tier;

		TP = new int[row];

		containerCount =0;

		block = new int[bay][row][tier];

		block_slots = new Slot[bay][row][tier];
		for(int i=0;i<bay;i++)
		{
			for(int j=0;j<row;j++)
			{
				for(int z=0;z<tier;z++)
				{
					block_slots[i][j][z]= new Slot(this,i,j,z);
				}
			}
		}
		System.out.println("init block:" + blockID);
	}

	public float getBlockPersent() throws Exception {
		return getContainerCount() / totalSlot;
	}

	//
	public int getContainerCount() throws Exception
	{
		if (containerCount > totalSlot)
			throw new Exception("container error : " + containerCount + ", " + totalSlot);
		return containerCount;
	}

	// 채占쏙옙占쏙옙 占쌍댐옙 SLOT 占쏙옙 占쌍삼옙占쏙옙 Slot 占쏙옙환
	Random rn = new Random();
	List<Slot> upperSlot = new LinkedList<Slot>();

	List<Slot> emptySlot = new LinkedList<Slot>();

	/**
	 * @return counted empty slot number
	 */
	public Slot selectFilledUpperSlot()
	{
		upperSlot.clear();

		for(int i=0;i<bay;i++)
		{
			for(int j=0;j<row;j++)
			{
				for(int z=0;z<tier;z++)
				{
					if (z == tier - 1)//
					{
						if(!block_slots[i][j][z].isEmpty())
						{
							upperSlot.add( block_slots[i][j][z]);
						}
					}
					if (z < tier - 1 && !block_slots[i][j][z].isEmpty() && block_slots[i][j][z + 1].isEmpty())// 占쏙옙 占쏙옙占�, 占싣뤄옙 占쏙옙占쏙옙占쏙옙占쏙옙占� 占싫듸옙
					{
						upperSlot.add(block_slots[i][j][z]);
					}
				}
			}
		}
		return upperSlot.get(rn.nextInt(upperSlot.size()));
	}

	// 占쏙옙占� 占쌍댐옙 占쏙옙치 占싹놂옙 占쏙옙환

	/**
	 * @return
	 */
	public synchronized Slot selectEmptySlot()
	{
		emptySlot.clear();
		do {
			for (int i = 0; i < bay; i++)
			{
				for (int j = 0; j < row; j++)
				{
					for (int z = 0; z < tier; z++)
					{
						if (z == 0)// 占쌕댐옙, 占쏙옙占쏙옙 占쏙옙占쏙옙羚占쏙옙 占쏙옙
						{
							if (block_slots[i][j][z].isEmpty() && block_slots[i][j][z + 1].isEmpty() && !block_slots[i][j][z].isUsed()) {
								emptySlot.add(block_slots[i][j][z]);

							}

						} else if (z == tier - 1)// 占쏙옙 占쏙옙占�, 占싣뤄옙 占쏙옙占쏙옙占쏙옙占쏙옙占� 占싫듸옙
						{
							if (block_slots[i][j][z].isEmpty() && !block_slots[i][j][z - 1].isEmpty() && !block_slots[i][j][z].isUsed()) {
								emptySlot.add(block_slots[i][j][z]);
							}
						}
						else //占쌩곤옙 占쏙옙占쏙옙 占쏙옙占� 占쌍곤옙 占싣뤄옙占쏙옙 占쏙옙占쌍억옙占� 占쏙옙
						{
							if (!block_slots[i][j][z - 1].isEmpty() && block_slots[i][j][z].isEmpty() && block_slots[i][j][z + 1].isEmpty() && !block_slots[i][j][z].isUsed()) {
								emptySlot.add(block_slots[i][j][z]);
							}
						}
					}
				}
			}
		} while (emptySlot.size() == 0);

		return emptySlot.get(rn.nextInt(emptySlot.size()));
	}

	public void setEmpty(Slot slot, boolean flag)
	{
		slot.setEmpty(flag);

		if(flag)
		{
			deleteContainer();
		}
		else
		{
			insertContainer();
		}
	}

	public Slot getSlot(int bay, int row, int tier) throws ArrayIndexOutOfBoundsException
	{
		return block_slots[bay][row][tier];
	}

	public void insertContainer()
	{
		containerCount++;
	}
	public void deleteContainer()
	{
		containerCount--;

		System.out.println("minus" + containerCount);
	}

	/**
	//	 * @param bay
	 * @param row
	 * @return
	 */
	public int slotCount(int bay, int row)
	{
		Slot tiers[]=block_slots[bay][row];

		int count=0;
		for(int i=0;i<tiers.length;i++)
		{
			if(!tiers[i].isEmpty())
			{
				count++;
			}
		}
		return count;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;

	}

	@Override
	public void append(SimNode node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(double delta) {
		// TODO Auto-generated method stub

	}
}
