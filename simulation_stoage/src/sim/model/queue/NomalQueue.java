package sim.model.queue;

/**
 * @author ��â��
 *
 */
public class NomalQueue extends SimQueue{
	
	
	private static NomalQueue nomalQueue;
	/**
	 * Class constructor
	 * Message Queue Instance�� ���⼭ ������
	 */
	static
	{
		nomalQueue = new NomalQueue();
	}
	
	/*
	 * Outbound Message Queue Instance�� ȹ���� �� �ִ� Ŭ���� �޼���
	 */
	public static NomalQueue getInstance()
	{
		return nomalQueue;
	}
	
	public NomalQueue()
	{
		super();
	}
	
	

	/* ��â�� �߰�
	 * (non-Javadoc)
	 * @see msg.queue.MsgQueue#append(msg.node.MsgNode)
	 */
	public synchronized boolean append(SimNode msgNode)
	{		
		if (super.append(msgNode))
		{
			notifyAll();
		}
		
		return true;
	}
	/* ��â�� �߰�
	 * (non-Javadoc)
	 * @see msg.queue.MsgQueue#append(msg.node.MsgNode)
	 */
	public synchronized SimNode poll() 
	{
		SimNode node = null;
		
		while((node = super.poll()) == null)
		{
			try 
			{
				wait();
			} 
			catch (InterruptedException e) 
			{
				//e.printStackTrace();
			}
		}
		
		return node;
	}

	

}
