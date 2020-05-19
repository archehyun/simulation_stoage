package sim.model.queue;

/**
 * @author 박창현
 *
 */
public class NomalQueue extends SimQueue{
	
	
	private static NomalQueue nomalQueue;
	/**
	 * Class constructor
	 * Message Queue Instance를 여기서 생성함
	 */
	static
	{
		nomalQueue = new NomalQueue();
	}
	
	/*
	 * Outbound Message Queue Instance를 획득할 수 있는 클래스 메서드
	 */
	public static NomalQueue getInstance()
	{
		return nomalQueue;
	}
	
	public NomalQueue()
	{
		super();
	}
	
	

	/* 박창현 추가
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
	/* 박창현 추가
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
