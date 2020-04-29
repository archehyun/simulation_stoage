package sim.model.stoage.atc;

import sim.model.SimEvent;
import sim.model.SimModel;
import sim.model.stoage.block.BlockManager;
import sim.model.stoage.jobmanager.JobManager;
import sim.queue.SimNode;

/**
 * @author ¹ÚÃ¢Çö
 *
 */
public class SimATC extends SimModel{
	JobManager jobManager = JobManager.getInstance();		
	public void setInitLocation(int initX, int initY)
	{
		this.setInitX(initX);
		this.setInitY(initY);
	}
	
	boolean isLoad=false;
	
	int initX;
	
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
	int initY;
	
	private int atcID;
	
	public int getAtcID() {
		return atcID;
	}
	private int speed=100;

	private int currentX=0;

	private int currentY=0;

	MoveX moveX;

	MoveY moveY;
	
	boolean isIdle=true;
	
	public boolean isIdle() {
		return isIdle;
	}

	private synchronized void arrival(int x, int y)
	{
		if(currentX ==x && currentY==y) {
			
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
		moveX = new MoveX(this.getSimName()+"_x");
		moveY = new MoveY(this.getSimName()+"_y");		
	}

	@Override
	public void notifySimMessage() {

	}

	@Override
	public void process(SimNode node) {
		
		SimEvent atcJob = (SimEvent) node;
		
		moveX.append(node);
		moveY.append(node);
	
		setBusy();
		
		
		int jobID = atcJob.getJobID();
		atcJob = null;
		

		
		notifyMonitor("loc:process:"+this.getSimName()+","+jobID+",size:"+this.getQueue().getSize());
		
				
	}
	
	void plusX()
	{
		currentX++;
	}

	void minusX()
	{
		currentX--;
	}
	
	void plusY()
	{
		currentY++;
	}

	void minusY()
	{
		currentY--;
	}
	
	public void simStart()
	{
		moveX.simStart();
		moveY.simStart();
		super.simStart();
	}
	public void simStop()
	{
		moveX.simStop();
		moveY.simStop();
		super.simStop();
	}

	class MoveX extends SimModel
	{		
		public void notifyMonitor(String message)
		{
			SimATC.this.notifyMonitor(message);
		}
		
		int destinationX;		

		public MoveX(String simName) {
			super(simName);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void notifySimMessage() {
			arrival(currentX, currentY);
		}
		
		void moveTP(SimEvent job)
		{
			do
			{
				if(initX>currentX)
				{
					plusX();
				}
				else if(initX<currentX)
				{
					minusX();
				}
				try {
					Thread.sleep(speed+5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				notifyMonitor(this.getSimName()+", des:"+destinationX+",cuX:"+currentX +", jobID:"+job.getJobID());
				
			}
			while(destinationX !=currentX&&isFlag());
		}

		void moveDestinationX(SimEvent job)
		{
			do
			{
				if(destinationX>currentX)
				{
					plusX();
				}
				else if(destinationX<currentX)
				{
					minusX();
				}
				try {
					Thread.sleep(speed+5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				notifyMonitor(this.getSimName()+", des:"+destinationX+",cuX:"+currentX +", jobID:"+job.getJobID());
				
			}
			while(destinationX !=currentX&&isFlag());
		}

		@Override
		public void process(SimNode node) {
			StoageEvent job = (StoageEvent) node;

			destinationX = (BlockManager.conW+BlockManager.wGap)*job.getX();
			
			moveDestinationX(job);
		}
	}
	class MoveY extends SimModel
	{
		int destinationY;
		
		public MoveY(String simName) {
			super(simName);
			// TODO Auto-generated constructor stub
		}
		public void notifyMonitor(String message)
		{
			SimATC.this.notifyMonitor(message);
		}
		void moveTP(SimEvent job)
		{
			do
			{
				if(initY>currentY)
				{
					plusY();
					
				}
				else if(initY<currentY)
				{
					minusY();
				}
				try {
					Thread.sleep(speed);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				this.notifyMonitor(this.getSimName()+", des:"+destinationY+",cuY:"+currentY+", jobID:"+job.getJobID());
				

			}
			while(initY !=currentY&&isFlag());
		}
		void moveDestinationY(SimEvent job)
		{
			do
			{
				if(destinationY>currentY)
				{
					plusY();
					
				}
				else if(destinationY<currentY)
				{
					minusY();
				}
				try {
					Thread.sleep(speed);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				this.notifyMonitor(this.getSimName()+", des:"+destinationY+",cuY:"+currentY+", jobID:"+job.getJobID());
				

			}
			while(destinationY !=currentY&&isFlag());
		}
		
		@Override
		public void notifySimMessage() {
			arrival(currentX, currentY);
		}

		@Override
		public void process(SimNode node) {
			StoageEvent job = (StoageEvent) node;
			destinationY = (BlockManager.conH+BlockManager.hGap)*job.getY();
			
			switch (job.eventType) {
			case StoageEvent.INBOUND:
				
				moveTP(job);
				isLoad=true;
				moveDestinationY(job);				
				
				isLoad=false;
				job.getSlot().setUsed(false);
				job.getSlot().getBlock().setEmpty(job.getSlot(), false);
				
						
				jobManager.release();
				
				
				break;
			case StoageEvent.OUTBOUND:
				
				moveDestinationY(job);
				isLoad=true;
				job.getSlot().getBlock().setEmpty(job.getSlot(), true);
				job.getSlot().setUsed(false);
				jobManager = JobManager.getInstance();				
				jobManager.release();
				moveTP(job);
				isLoad=false;
				
				break;	

			default:
				break;
			}
			
			
		}

	}
	public int getX() {
		// TODO Auto-generated method stub
		return currentX;
	}
	
	public int getY() {
		// TODO Auto-generated method stub
		return currentY;
	}

	public boolean isLoad() {
		// TODO Auto-generated method stub
		return isLoad;
	}
	
	

}
