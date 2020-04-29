package sim.model;

import sim.queue.SimNode;

/**
 * @author 박창현
 *
 */
public class SimEvent extends SimNode{
	
	/**
	 * 시뮬레이션 모델 이름
	 */
	public String simName;
	
	/**
	 * 이벤트 타입
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
