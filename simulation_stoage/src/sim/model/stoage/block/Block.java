package sim.model.stoage.block;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Block {
	/**
	 *
	 */
	int tier;
	public int getTier() {
		return tier;
	}

	public int getRow() {
		return row;
	}

	public int getBay() {
		return bay;
	}

	int row;
	int bay;

	int blockID;

	int totalSlot;

	public int getTotalSlot() {
		return totalSlot;
	}

	public int getBlockID() {
		return blockID;
	}

	int containerCount;

	int block[][][];

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

	public float getBlockPersent() {
		return getContainerCount() / totalSlot;
	}

	// ��ü �����̳� ��
	public int getContainerCount()
	{
		/*int count=0;
		for(int i=0;i<bay;i++)
		{
			for(int j=0;j<row;j++)
			{
				for(int z=0;z<tier;z++)
				{
					count+=block[i][j][z];
				}
			}
		}*/
		return containerCount;
	}

	// ä���� �ִ� SLOT �� �ֻ��� Slot ��ȯ
	Random rn = new Random();
	List<Slot> upperSlot = new LinkedList<Slot>();

	List<Slot> emptySlot = new LinkedList<Slot>();

	public Slot selectFilledUpperSlot()
	{
		upperSlot.clear();

		for(int i=0;i<bay;i++)
		{
			for(int j=0;j<row;j++)
			{
				for(int z=0;z<tier;z++)
				{
					if(z==tier-1)// �� ���, �Ʒ� ��������� �ȵ�
					{
						if(!block_slots[i][j][z].isEmpty())
						{
							upperSlot.add( block_slots[i][j][z]);
						}
					}
					if(z<tier-1&&!block_slots[i][j][z].isEmpty()&&block_slots[i][j][z+1].isEmpty())// �� ���, �Ʒ� ��������� �ȵ�
					{
						upperSlot.add(block_slots[i][j][z]);
					}
				}
			}
		}
		System.out.println("size:"+upperSlot.size());
		return upperSlot.get(rn.nextInt(upperSlot.size()));
	}

	// ��� �ִ� ��ġ �ϳ� ��ȯ

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
						if (z == 0)// �ٴ�, ���� ����־�� ��
						{
							if (block_slots[i][j][z].isEmpty() && block_slots[i][j][z + 1].isEmpty() && !block_slots[i][j][z].isUsed()) {
								emptySlot.add(block_slots[i][j][z]);

							}

						} else if (z == tier - 1)// �� ���, �Ʒ� ��������� �ȵ�
						{
							if (block_slots[i][j][z].isEmpty() && !block_slots[i][j][z - 1].isEmpty() && !block_slots[i][j][z].isUsed()) {
								emptySlot.add(block_slots[i][j][z]);
							}
						}
						else //�߰� ���� ��� �ְ� �Ʒ��� ���־�� ��
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

	public Slot getSlot(int bay, int row, int tier)
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
}
