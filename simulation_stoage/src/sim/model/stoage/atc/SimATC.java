package sim.model.stoage.atc;

import sim.model.SimEvent;
import sim.model.SimModel;
import sim.model.stoage.atc.move.ATCMove;
import sim.model.stoage.jobmanager.JobManager;
import sim.queue.SimNode;

/**
 * @author 占쏙옙창占쏙옙
 *
 */
public abstract class SimATC extends SimModel {

	public int workCount;

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

	ATCMove moveXX;

	ATCMove moveYY;

	int initX; // row 기준 위치

	int initY; // bay 기준 위치

	protected int initXpointOnWindows = 0; // row 기준 초기 위치

	protected int initYpointOnWindows = 0; // bay  기준 초기 위치

	protected int currentXpointOnWindows = 0; // row 기준 초기 위치

	protected int currentYpointOnWindows = 0; // bay  기준 초기 위치

	JobManager jobManager = JobManager.getInstance();

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
	 *
	 */
	boolean isIdle=true;

	/**
	 * @return
	 */
	public boolean isIdle() {
		return isIdle;
	}

	public synchronized void arrival(int x, int y)
	{
		if(currentXpointOnWindows ==x && currentYpointOnWindows==y) {

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public SimATC(String simName, int atcID) {
		super(simName);
		this.atcID =atcID;
	}

	@Override
	public void process(SimNode node) {

		SimEvent atcJob = (SimEvent) node;

		moveXX.append(node);
		moveYY.append(node);

		setBusy();
		StoageEvent event = (StoageEvent) atcJob;
		notifyMonitor("loc:process:" + this.getSimName() + "initY:" + this.getInitY() + ",currentY:" + this.getY() + ", Y:" + event.getY());

		atcJob = null;


	}

	public void plusX()
	{
		currentXpointOnWindows++;
	}

	public void minusX()
	{
		currentXpointOnWindows--;
	}

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

	public int getY() {
		// TODO Auto-generated method stub
		return currentYpointOnWindows;
	}

	public boolean isLoad() {
		// TODO Auto-generated method stub
		return isLoad;
	}

	public int getSpeed() {
		// TODO Auto-generated method stub
		return speed;
	}

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

}
