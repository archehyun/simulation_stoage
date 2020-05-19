package sim.model.core;

import java.util.HashMap;

import sim.model.queue.SimNode;

/**
 * ??뮬�???��?? ?�벤?? ?��????
 *
 * @author archehyun
 * @SEE
 * @since 2020.05.17
 *
 *
 */
public class SimEvent extends SimNode{

	/* SimEvent type
	 *
	 *
	 * command
	 * order
	 */
	public static final int COMMAND = 1;

	public static final int ORDER = 2;

	/*
	 * command type
	 */

	public static final int COMMAND_UPDATE_SPEED = 1;

	HashMap<String, Object> map;

	/**
	 *
	 */
	public String simName;

	/**
	 *?�벤?? ????
	 */
	private int eventType;

	/**
	 *  command type
	 */
	private int commandType;

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

	/**
	 * job id
	 */
	private int jobID;



	/**
	 * @return jobiD
	 */
	public int getJobID() {
		return jobID;
	}

	public SimEvent(int jobID) {
		this();
		this.jobID = jobID;
	}

	public SimEvent(int jobID, int eventType) {
		this(jobID);
		this.setEventType(eventType);
	}

	public void add(String key, Object value)
	{
		map.put(key, value);
	}

	public Object get(String key) {
		return map.get(key);
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;

	}

	public int getEventType() {
		return this.eventType;
	}

	public int getCommandType() {
		// TODO Auto-generated method stub
		return commandType;
	}

	public void setCommandType(int commandType) {
		this.commandType = commandType;
	}
}
