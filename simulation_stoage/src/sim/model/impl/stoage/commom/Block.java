package sim.model.impl.stoage.commom;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 블럭 정보 표혀 클래스
 * @author archehyun
 *
 */
public class Block {

	int x, y;

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
	}

	public float getBlockPersent() throws Exception {
		return getContainerCount() / totalSlot;
	}

	// 전체 컨테이너 수
	public int getContainerCount() throws Exception
	{
		if (containerCount > totalSlot)
			throw new Exception("container error : " + containerCount + ", " + totalSlot);
		return containerCount;
	}

	// 채워져 있는 SLOT 중 최상위 Slot 반환
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
					if(z==tier-1)// 최 상단, 아래 비어있으면 안됨
					{
						if(!block_slots[i][j][z].isEmpty())
						{
							upperSlot.add( block_slots[i][j][z]);
						}
					}
					if(z<tier-1&&!block_slots[i][j][z].isEmpty()&&block_slots[i][j][z+1].isEmpty())// 최 상단, 아래 비어있으면 안됨
					{
						upperSlot.add(block_slots[i][j][z]);
					}
				}
			}
		}
		return upperSlot.get(rn.nextInt(upperSlot.size()));
	}

	// 비어 있는 위치 하나 반환

	/**
	 * @return
	 */
	public Slot selectEmptySlot()
	{
		emptySlot.clear();
		do {
			for (int i = 0; i < bay; i++)
			{
				for (int j = 0; j < row; j++)
				{
					for (int z = 0; z < tier; z++)
					{
						if (z == 0)// 바닥, 위가 비어있어야 함
						{
							if (block_slots[i][j][z].isEmpty() && block_slots[i][j][z + 1].isEmpty() && !block_slots[i][j][z].isUsed()) {
								emptySlot.add(block_slots[i][j][z]);

							}

						} else if (z == tier - 1)// 최 상단, 아래 비어있으면 안됨
						{
							if (block_slots[i][j][z].isEmpty() && !block_slots[i][j][z - 1].isEmpty() && !block_slots[i][j][z].isUsed()) {
								emptySlot.add(block_slots[i][j][z]);
							}
						}
						else //중간 위는 비어 있고 아래는 차있어야 함
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
	}

	/**
	 * @param bay
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
}
