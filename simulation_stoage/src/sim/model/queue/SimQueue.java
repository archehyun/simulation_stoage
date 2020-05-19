package sim.model.queue;

/**
 * @author 박창현
 *
 */
public abstract  class SimQueue {
	
	
	protected SimNode first; //pointer to the first node of Linked List
	protected SimNode last; //pointer to the last node of Linked List
	
	int size=0;
	
	public SimQueue()
	{
		first = null;
		last = null;
	}
	
	/**
	 * 새로운 Message를 큐 끝에 추가
	 * 
	 * @param newMsg	큐에 추가할 Control Message
	 * @return			true if succeed, otherwise false          
	 */
	public synchronized boolean append(SimNode newMsg)
	{
		
		++size;
		if (first == null)
		{
			first = newMsg;
			last = newMsg;
		}
		else
		{
			newMsg.setPrev(last);
			last.setNext(newMsg);
			last = newMsg;
		}
		
		
		return true;
	}
	
	/**
	 * 큐의 첫번째 Message를 뽑아서 return 함
	 * 
	 * @param 			없음
	 * @return			Message if it exist, otherwise null          
	 */
	public synchronized SimNode poll()
	{
		if(size>0)
			--size;
		
		SimNode retMsg = null;
		
		if (first == null)
		{
			retMsg = null;
		}
		else if (first == last)
		{
			retMsg = first;
			first = null;
			last = null;
		}
		else
		{
			retMsg = first; 
			first = first.getNext();;
			first.setPrev(null);
		}
		
		return retMsg;
	}

	public int getSize() {
		
		return size;
	}
	
	
	

}
