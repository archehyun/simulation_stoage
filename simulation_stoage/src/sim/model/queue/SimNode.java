package sim.model.queue;

/**
 *
 * Queue ³ëµå
 * @author LDCC
 *
 */
public class SimNode {

public static final short NULL = -9999;

	private SimNode prev; //doubly linked list
	private SimNode next; //doubly linked list

	public SimNode()
	{
		prev = null;
		next = null;
	}

	public SimNode getPrev()
	{
		return prev;
	}

	public void setPrev(SimNode prev)
	{
		this.prev = prev;
	}

	public SimNode getNext()
	{
		return next;
	}

	public void setNext(SimNode next)
	{
		this.next = next;
	}

}
