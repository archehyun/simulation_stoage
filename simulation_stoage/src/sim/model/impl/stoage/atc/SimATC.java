package sim.model.impl.stoage.atc;

import sim.model.core.SimEvent;
import sim.model.core.SimModel;
import sim.model.impl.stoage.atc.move.ATCMove;
import sim.model.impl.stoage.atc.move.ATCMove2;
import sim.model.impl.stoage.block.BlockManager;
import sim.model.impl.stoage.commom.JobManager;
import sim.model.impl.stoage.manager.ATCJobManager;
import sim.model.impl.stoage.manager.ATCManager;
import sim.view.framework.Rectangle;
import sim.view.framework.Vector2;

/**
 * @author archehyun
 * @param <Canvas3D>
 *
 */
public abstract class SimATC extends SimModel {

	public int inOutType;

	public int getInOutType() {
		return inOutType;
	}

	public void setInOutType(int inOutType) {
		this.inOutType = inOutType;
	}

	public static final int TYPE_LAND = 1;

	public static final int TYPE_SEA = 0;

	private int locationType;

	public int getLocationType() {
		return locationType;
	}

	public void setLocationType(int locationType) {
		this.locationType = locationType;
	}

	ATCManager atcManager = ATCManager.getInstance();

	protected ATCJobManager atcJobManager;

	//ATC Loation Position;
	public final Vector2 position;

	//initiation Point
	public final Vector2 initPosition = new Vector2();

	// atc bounds
	public final Rectangle bounds;

	public final Vector2 velocity;

	public static final int ATC_STATE_BUSY = 1;

	public static final int ATC_STATE_IDLE = 2;

	public static final int ATC_STATE_MOVING = 3;

	private int blockID;

	public BlockManager blockManager = BlockManager.getInstance();

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

	protected int initRow; // int row

	protected int initBay; // init bay

	protected JobManager jobManager = JobManager.getInstance();

	/**
	 *
	 * atc bay, row initation
	 *
	 * @param initRow
	 * @param initBay
	 */
	public void setInitBlockLocation(float initRow, float initBay)
	{
		logger.info("init:" + row + "," + bay);
		this.setInitRow(initRow);
		this.setInitBay(initBay);
	}


	/**
	 *
	 */
	boolean isLoad=false;

	Vector2 destination;

	public int getInitRow() {
		return initRow;
	}



	public void setInitRow(float initRow) {
		this.initRow = (int) initRow;

	}

	public int getInitBay() {
		return initBay;
	}

	public void setInitBay(float initBay) {
		this.initBay = (int) initBay;
	}

	public abstract void updateInitLocationOnWinddows(int blockID);

	public int getAtcID() {
		return getId();
	}

	/**
	 * atc speed
	 */
	private float speed;

	/**
	 * idle state
	 */
	boolean isIdle=true;

	protected double deltaTime = 1;

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
		/*if (this.position.x == currentXpointOnWindows && this.position.y == currentYpointOnWindows) {
			this.isIdle=true;

			notify();
		}*/
	}

	public synchronized void arrival() {
		this.isIdle = true;

		notifyAll();

	}

	public boolean isArrival() {

		int destinationX = (int) this.getDestination().x;
		int currentX = (int) this.getLocation().x;
		int destinationY = (int) this.getDestination().y;
		int currentY = (int) this.getLocation().y;

		if (destinationX == currentX && destinationY == currentY) {

			return true;
		}
		else
		{
			return false;
		}
	}

	public float hoistTime = 1000f;

	public float getHoistTime() {
		return hoistTime;
	}

	public void setHoistTime(float hoistTime) {
		this.hoistTime = hoistTime;
	}

	public float hoistWorkTime = 0;
	public synchronized void release() {
		this.isIdle = true;

		notifyAll();
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

	public synchronized void setMove(boolean move) {
		this.isMove = true;
		notify();
	}

	public SimATC(String simName, int id, int blockID, float row, float bay, float width, float height, int locationType) {
		super(simName, id);

		this.blockID = blockID;

		this.setLocationType(locationType);

		atcJobManager = atcManager.getATCManager(blockID);

		this.setInitBlockLocation(row, bay);

		this.initPosition.x = row;
		this.initPosition.y = bay;

		this.position = new Vector2(getInitRow() * BlockManager.hGap,

				getInitBay() * (BlockManager.conH + BlockManager.hGap) + BlockManager.conH);

		this.bounds = new Rectangle((int) (position.x - width / 2), (int) (position.y - height / 2), (int) width, (int) height);

		velocity = new Vector2(1, 1);
		destination = new Vector2();

	}


	/**
	 * plus currentXpointOnWindows
	 */
	public void plusX()
	{
		position.add(velocity.x, 0);
		bounds.lowerLeft.add(velocity.x, 0);
	}

	/**
	 * minus currentXpointOnWindows
	 */
	public void minusX()
	{
		position.sub(velocity.x, 0);
		bounds.lowerLeft.sub(velocity.x, 0);
	}

	/**
	 * @throws InterruptedException
	 *
	 */
	public void plusY() throws InterruptedException
	{
		position.add(0, velocity.y);
		bounds.lowerLeft.add(0, this.velocity.y * (float) deltaTime);
	}

	/**
	 * @throws InterruptedException
	 *
	 */
	public void minusY() throws InterruptedException {

		position.sub(0, velocity.y);
		bounds.lowerLeft.sub(0, this.velocity.y * (float) deltaTime);
	}

	@Override
	public void simStart()
	{

		super.simStart();
	}
	@Override
	public void simStop()
	{

		super.simStop();
	}


	/**
	 * @return currentXpointOnWindows
	 */
	public int getX() {
		// TODO Auto-generated method stub
		return (int) (this.position.x);
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
	public float getSpeed() {

		return speed * BlockManager.blockRate;
	}

	/**
	 *
	 * set destinatioin on windows
	 * unit : point
	 * @param x
	 * @param y
	 */
	public void setDestination(float x, float y) {
		destination.set(x, y);
	}

	public void setDestinationLocation(int row, int bay)
	{

		float x = (BlockManager.conW + BlockManager.wGap) * row;
		float y = (BlockManager.conH + BlockManager.hGap) * bay;
		this.setDestination(x, y);
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

	public boolean hoist = false;

	public boolean isHoist() {
		return hoist;
	}
	/**
	 *
	 */
	public void workHoist() {

		try {

			hoist = true;

			System.out.println("host");
			Thread.sleep(000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hoist = false;

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
		System.out.println("update act");
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

	public void setSpeed(float speed) {
		this.speed = speed;

	}

	public boolean overlapRectangles(Rectangle r2) {


		if (bounds.lowerLeft.x < r2.lowerLeft.x + r2.width && bounds.lowerLeft.x + bounds.width > r2.lowerLeft.x && bounds.lowerLeft.y < r2.lowerLeft.y + r2.height && bounds.lowerLeft.y + bounds.height > r2.lowerLeft.y)
			return true;
		else
			return false;
	}

	public Vector2 getLocation() {

		return position;

	}

	public void setLocation(float x, float y) {
		position.set(x, y);

	}



	public Vector2 getDestination() {
		return destination;
	}

	boolean crash = false;
	public boolean isCrash() {
		// TODO Auto-generated method stub
		return crash;
	}

	public void setCrach(boolean crash) {
		this.crash = crash;

	}

	public void setATCMove(boolean b) {
		// TODO Auto-generated method stub

	}

	public ATCMove2 getMovingObject() {
		// TODO Auto-generated method stub
		return null;
	}

}
