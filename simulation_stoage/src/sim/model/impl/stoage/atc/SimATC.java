package sim.model.impl.stoage.atc;

import sim.model.core.SimEvent;
import sim.model.core.SimModel;
import sim.model.impl.stoage.atc.crossover.CrossOverJobManager;
import sim.model.impl.stoage.atc.move.ATCMove;
import sim.model.impl.stoage.commom.BlockManager;
import sim.model.impl.stoage.commom.JobManager;
import sim.view.Vector2;
import sim.view.framework.Rectangle;

/**
 * @author archehyun
 *
 */
public abstract class SimATC extends SimModel {

	//��ü ��ġ
	public final Vector2 position;

	//��ü ��ġ
	public final Vector2 initPosition = new Vector2();

	// ��ü ���
	public final Rectangle bounds;

	public final Vector2 velocity;

	public static final int ATC_STATE_BUSY = 1;

	public static final int ATC_STATE_IDLE = 2;

	public static final int ATC_STATE_MOVING = 3;

	private int blockID;

	BlockManager blockManager = BlockManager.getInstance();

	public static int SEA_SIDE = 100;

	public static int LAND_SIDE = 200;

	protected ATCCommander commander;

	private int workCount;

	private int bay;

	protected boolean isMove = true;

	/**
	 * @return bay
	 */
	public int getBay() {
		return bay;
	}

	ATCJobManager atcJobManager = CrossOverJobManager.getInstance();

	/**
	 * row
	 */
	private int row;

	/**
	 * @return row
	 */
	public int getRow() {
		return row;
	}

	public int getInitXpointOnWindows() {
		return (int) initPosition.x;
	}

	public int getInitYpointOnWindows() {
		return (int) initPosition.y;
	}

	protected ATCMove moveXX;

	protected ATCMove moveYY;

	protected int initX; // row ���� ��ġ

	protected int initY; // bay ���� ��ġ

	//protected int initXpointOnWindows = 0; // row ���� �ʱ� ��ġ

	//protected int initYpointOnWindows = 0; // bay  ���� �ʱ� ��ġ

	//protected int currentXpointOnWindows = 0; // row ���� �ʱ� ��ġ

	//protected int currentYpointOnWindows = 0; // bay  ���� �ʱ� ��ġ

	protected JobManager jobManager = JobManager.getInstance();

	/**
	 *
	 * atc bay, row ���� ��ġ ����
	 *
	 * @param initX
	 * @param initY
	 */
	public void setInitBlockLocation(float initX, float initY)
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

	public void setInitX(float initX) {
		this.initX = (int) initX;

	}

	public int getInitY() {
		return initY;
	}

	public void setInitY(float initY) {
		this.initY = (int) initY;
	}

	public abstract void updateInitLocationOnWinddows(int blockID);

	private int atcID;

	public int getAtcID() {
		return atcID;
	}

	private int speed;

	/**
	 * idle ����
	 */
	boolean isIdle=true;

	private int deltaTime = 1;

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
		if (this.position.x == currentXpointOnWindows && this.position.y == currentYpointOnWindows) {


			this.isIdle=true;

			notify();
		}
	}


	/**
	 *ATC State
	 *1. busy
	 *2. none busy
	 */
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




	public SimATC(String simName, int atcID, int blockID, float row, float bay, float width, float height) {
		super(simName);
		this.atcID =atcID;
		this.blockID = blockID;

		this.setInitBlockLocation(row, bay);

		this.position = new Vector2(getInitX() * BlockManager.hGap,

				getInitY() * (BlockManager.conH + BlockManager.hGap) + BlockManager.magin + BlockManager.conH);


		this.bounds = new Rectangle((int) (position.x - width / 2), (int) (position.y - height / 2), (int) width, (int) height);

		velocity = new Vector2(1, 1);



	}


	/**
	 * plus currentXpointOnWindows
	 */
	public void plusX()
	{
		position.add(velocity.x, 0);
		bounds.lowerLeft.add(velocity.x, 0);
		//currentXpointOnWindows++;
	}

	/**
	 * minus currentXpointOnWindows
	 */
	public void minusX()
	{
		position.sub(velocity.x, 0);
		bounds.lowerLeft.sub(velocity.x, 0);
		//currentXpointOnWindows--;
	}

	/**
	 * @throws InterruptedException
	 *
	 */
	public void plusY() throws InterruptedException
	{

		// ���� �ڸ� ���� Ȯ��

		/*
		 * case  �۾� ���� ���
		 * case
		 */
		// �ڸ��� ���涧���� ���

		// ATC �̵�

		while (!isMove) {
			wait();
		}
		if (atcJobManager.overlapRectangles(this)) {
			System.out.println("�浹");
		} else {
		}
		position.add(0, velocity.y);
		bounds.lowerLeft.add(0, this.velocity.y * deltaTime);
	}

	/**
	 * @throws InterruptedException
	 *
	 */
	public void minusY() throws InterruptedException
	{
		while (!isMove) {
			wait();
		}
		if (atcJobManager.overlapRectangles(this)) {
			System.out.println("�浹");
		}
		else {

		}
		//currentYpointOnWindows--;
		position.sub(0, velocity.y);
		bounds.lowerLeft.sub(0, this.velocity.y * deltaTime);
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


	/**
	 * @return currentXpointOnWindows
	 */
	public int getX() {
		// TODO Auto-generated method stub
		return (int) this.position.x;
	}

	/**
	 *
	 *
	 *
	 * @return currentYpointOnWindows
	 */
	public int getY() {

		return (int) this.position.y;
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

	/**
	 * @param load
	 */
	public void setLoad(boolean load) {
		isLoad = load;
	}

	@Override
	public void notifySimMessage(SimModel model) {
		// TODO Auto-generated method stub

	}

	/**
	 *
	 */
	public void workTrolly() {
		// TODO trolly work develep
	}

	/**
	 * move to tp
	 * @throws InterruptedException
	 */
	public abstract void moveTP(SimEvent job) throws InterruptedException;

	/**
	 * move to destination
	 * @throws InterruptedException
	 */
	public abstract void moveDestination(SimEvent job) throws InterruptedException;

	public void plusWorkCount() {
		workCount++;
		SimEvent event = new SimEvent();
		event.add("type", "block");
		blockManager.notifyMonitor(event);
	}

	/**
	 * @return
	 */
	public int getBlockID() {
		return blockID;
	}

	public void setSpeed(int speed) {
		this.speed = speed;

	}

	public boolean overlapRectangles(Rectangle r2) {
		if (bounds.lowerLeft.x < r2.lowerLeft.x + r2.width && bounds.lowerLeft.x + bounds.width > r2.lowerLeft.x && bounds.lowerLeft.y < r2.lowerLeft.y + r2.height && bounds.lowerLeft.y + bounds.height > r2.lowerLeft.y)
			return true;
		else
			return false;
	}

}
