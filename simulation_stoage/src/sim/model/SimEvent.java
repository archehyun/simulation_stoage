package sim.model;

import sim.queue.SimNode;

/**
 * @author ��â��
 *
 */
public class SimEvent extends SimNode{
	
	/**
	 * �ùķ��̼� �� �̸�
	 */
	public String simName;
	
	/**
	 * �̺�Ʈ Ÿ��
	 */
	public int eventType;
	
	/**
	 * 
	 */
	private String eventMessage;
	
	
	
	public String getSimName() {
		return simName;
	}

	public void setSimName(String simName) {
		this.simName = simName;
	}	
	
	public String getEventMessage() {
		return eventMessage;
	}

	public void setEventMessage(String eventMessage) {
		this.eventMessage = eventMessage;
	}

	private int jobID;
	
	public int getJobID() {
		return jobID;
	}

	public SimEvent(int jobID) {
		this.jobID = jobID;
	}

}
