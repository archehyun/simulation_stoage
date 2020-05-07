package sim.model.stoage.atc;

import sim.model.SimEvent;
import sim.model.SimModel;
import sim.model.stoage.atc.move.ATCMove;
import sim.model.stoage.jobmanager.JobManager;

/**
 * @author archehyun
 *
 */
public abstract class SimATC extends SimModel {

	public static int SEA_SIDE = 100;

	public static int LAND_SIDE = 200;

	protected ATCCommander commander;

	private int workCount;

	private int bay;

	public int getBay() {
		return bay;
	}

	private int row;

	public int getRow() {
		return row;
	}

	public int getInitXpointOnWindows() {
		return initXpointOnWindows;
	}

	public int getInitYpointOnWindows() {
		return initYpointOnWindows;
	}

	protected ATCMove moveXX;

	protected ATCMove moveYY;

	protected int initX; // row 기준 위치

	protected int initY; // bay 기준 위치

	protected int initXpointOnWindows = 0; // row 기준 초기 위치

	protected int initYpointOnWindows = 0; // bay  기준 초기 위치

	protected int currentXpointOnWindows = 0; // row 기준 초기 위치

	protected int currentYpointOnWindows = 0; // bay  기준 초기 위치

	protected JobManager jobManager = JobManager.getInstance();

	/**
	 *
	 * atc bay, row 기준 위치 설정
	 *
	 * @param initX
	 * @param initY
	 */
	public void setInitLocation(int initX, int initY)
	{
		this.setInitX(initX);
		this.setInitY(initY);
	}


	/**
	 *
	 */
	boolean isLoad=false;

	public int getInitX() {
		return initX;
	}

	public void setInitX(int initX) {
		this.initX = initX;

	}

	public int getInitY() {
		return initY;
	}

	public void setInitY(int initY) {
		this.initY = initY;
	}

	public abstract void updateInitLocationOnWinddows(int blockID);

	private int atcID;

	public int getAtcID() {
		return atcID;
	}
	private int speed=100;

	/**
	 *atc idle 여부
	 */
	boolean isIdle=true;

	/**
	 * @return isIdle
	 */
	public boolean isIdle() {
		return isIdle;
	}

	/**
	 * @param x
	 * @param y
	 */
	public synchronized void arrival(int currentXpointOnWindows, int currentYpointOnWindows)
	{
		if (this.currentXpointOnWindows == currentXpointOnWindows && this.currentYpointOnWindows == currentYpointOnWindows) {


			this.isIdle=true;

			notify();
		}
	}


	public synchronized void setBusy(){

		this.isIdle=false;

		while(!isIdle)
		{
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param simName
	 * @param atcID
	 */
	public SimATC(String simName, int atcID) {
		super(simName);
		this.atcID =atcID;
	}


	/**
	 * plus currentXpointOnWindows
	 */
	public void plusX()
	{
		currentXpointOnWindows++;
	}

	/**
	 * minus currentXpointOnWindows
	 */
	public void minusX()
	{
		currentXpointOnWindows--;
	}

	/**
	 *
	 */
	public void plusY()
	{
		currentYpointOnWindows++;
	}

	public void minusY()
	{
		currentYpointOnWindows--;
	}

	@Override
	public void simStart()
	{
		moveXX.simStart();
		moveYY.simStart();
		super.simStart();
	}
	@Override
	public void simStop()
	{
		moveXX.simStop();
		moveYY.simStop();
		super.simStop();
	}


	public int getX() {
		// TODO Auto-generated method stub
		return currentXpointOnWindows;
	}

	/**
	 *
	 *
	 *
	 * @return currentYpointOnWindows
	 */
	public int getY() {

		return currentYpointOnWindows;
	}

	/**
	 * @return isLoad
	 */
	public boolean isLoad() {
		return isLoad;
	}

	/**
	 * @return speed
	 */
	public int getSpeed() {

		return speed;
	}

	/**
	 * @return workCount
	 */
	public int getWorkCount() {
		return workCount;
	}

	public void setLoad(boolean load) {
		isLoad = load;
	}

	@Override
	public void notifySimMessage(SimModel model) {
		// TODO Auto-generated method stub

	}

	public void workTrolly() {
		// TODO trolly work develep
	}

	/**
	 * move to tp
	 */
	public abstract void moveTP(SimEvent job);

	/**
	 * move to destination
	 */
	public abstract void moveDestination(SimEvent job);

	public void plusWorkCount() {
		workCount++;
	}

}
