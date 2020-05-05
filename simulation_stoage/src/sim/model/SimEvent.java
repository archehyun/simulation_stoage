package sim.model;

import java.util.HashMap;

import sim.queue.SimNode;

/**
 * @author 諛�李쏀��
 *
 */
public class SimEvent extends SimNode{

	HashMap<String, Object> map;

	/**
	 *
	 */
	public String simName;

	/**
	 * 占싱븝옙트 타占쏙옙
	 */
	public int eventType;

	/**
	 *
	 */
	private String eventMessage = "";

	public SimEvent() {
		super();
		map = new HashMap<>();

	}

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
		this();
		this.jobID = jobID;
	}

	public void add(String key, Object value)
	{
		map.put(key, value);
	}

	public Object get(String key) {
		return map.get(key);
	}
}
