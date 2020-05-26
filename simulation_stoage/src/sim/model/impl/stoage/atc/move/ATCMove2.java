package sim.model.impl.stoage.atc.move;

import java.util.Iterator;
import java.util.List;

import sim.model.impl.stoage.atc.SimATC;
import sim.model.impl.stoage.commom.StoageEvent;
import sim.model.impl.stoage.manager.ATCJobManager;
import sim.model.impl.stoage.manager.ATCManager;

public class ATCMove2 {

	public static final int UP = -1;

	public static final int DOWN = 1;

	public static final int STOP = 0;

	public static final int FORWORD = 1;

	public static final int BACKWORD = -1;

	private boolean horizontal = false;

	private int seaLandType = 0;

	SimATC atc;

	ATCJobManager atcJobManager;

	public ATCMove2(SimATC atc) {

		this.atc = atc;

		atcJobManager = ATCManager.getInstance().getATCManager(atc.getBlockID());
	}

	public void setSeaLandType(int type) {
		this.seaLandType = type;
	}


	float bayDirection = 0;
	int x;


	public float getBaySpeed() {

		return (float) (atc.getSpeed() * bayDirection * delta);
	}

	boolean isBayMove = false;

	boolean isRowMove = false;

	private int rowDirection = 0;

	private double delta;

	public int getRowDirection() {
		return rowDirection;
	}

	public void setBayMove(boolean move) {
		this.isBayMove = move;
	}

	private boolean amItooClose(SimATC _other) {
		if (horizontal == true) {
			if (_other.getLocation().y == atc.getLocation().y) {
				if ((_other.getLocation().x - atc.getLocation().x) > 0 && (_other.getLocation().x - atc.getLocation().x) <= 20) {
					return true;
				}
			} else // 상대차는 수직방향 이동중
			{
				if (Math.abs(atc.getLocation().y - _other.getLocation().y) <= 20 && (_other.getLocation().x - atc.getLocation().x) > 0 && (_other.getLocation().x - atc.getLocation().x) <= 15) {
					if (Math.abs(atc.getLocation().y - _other.getLocation().y) <= Math.abs(_other.getLocation().x - atc.getLocation().x)) {
						return true;
					}
				}
			}
		} else // vertical
		{
			if (_other.getLocation().x == atc.getLocation().x) {
				if ((_other.getLocation().y - atc.getLocation().y) > 0 && (_other.getLocation().y - atc.getLocation().y) <= 20) {
					return true;
				}
			} else // 상대차는 수평방향 이동 중
			{

				if (Math.abs(atc.getLocation().x - _other.getLocation().x) <= 20 && (_other.getLocation().y - atc.getLocation().y) > 0 && (_other.getLocation().y - atc.getLocation().y) <= 15) {
					if (Math.abs(atc.getLocation().y - _other.getLocation().y) > Math.abs(_other.getLocation().x - atc.getLocation().x)) {
						return true;
					}

				}

			}
		}
		return false;
	}

	public void update(double delta) {

		this.delta = delta;

		List<SimATC> li = atcJobManager.getATCs();
		SimATC other = null;
		if (atc.getLocationType() == StoageEvent.SEA) {

			synchronized (li) {

				Iterator<SimATC> iter = li.iterator();
				boolean found = false;


				while (iter.hasNext()) {
					other = iter.next();
					if (other != atc && amItooClose(other) == true) {

						other.setCrach(true);

						break;
					}
					else {
						other.setCrach(false);
					}

					if (found == false) {
						other = null;
					}
				}
			}

			/*if (other != null) {
				synchronized (other.getMovingObject()) {

					if (other.isCrash() == true) {

						try {
							other.getMovingObject().wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						//other.setATCMove(false);

					}

				}
			}*/

			if (other == null || amItooClose(other) == false) {
				moving();

			} else {
				System.out.println("wait~~~~~~~~~~~~~~~~~~~");
			}
		}
		else {
			moving();
		}

		/*synchronized (atc.getMovingObject()) {
			// notify for others
			atc.setCrach(false);
			atc.getMovingObject().notifyAll();

			//atc.notifyAll();
		}*/

	}

	private void moving() {
		if (isBayMove) {
			int currentY = (int) atc.getLocation().y;
			float currentY2 = atc.getLocation().y;
			int destinationY = (int) atc.getDestination().y;
			if (destinationY > currentY) {
				this.setBayDirection(DOWN * seaLandType);

				//System.out.println("down:" + destinationY + ", " + currentY);

			} else if (destinationY < currentY) {
				this.setBayDirection(UP * seaLandType);
			} else {
				System.out.println("bay arrival");
				this.setBayDirection(STOP);
				this.setBayMove(false);
			}
				currentY2 += getBaySpeed();

				atc.setLocation(atc.getLocation().x, currentY2);
		}

		if (isRowMove) {
			int currentX = (int) atc.getLocation().x;
			float currentX2 = atc.getLocation().x;
			float destinationX = atc.getDestination().x;
			if (destinationX > currentX) {
				this.setRowDirection(DOWN);

			} else if (destinationX < currentX) {
				this.setRowDirection(UP);
			} else {
				this.setRowDirection(STOP);

				this.setRowMove(false);
				//System.out.println("row arrival");
			}
			currentX2 += getRowSpeed();

			atc.setLocation(currentX2, atc.getLocation().y);

		}
	}

	public float getRowSpeed() {
		return (float) (atc.getSpeed() * rowDirection * delta);
	}

	private void setRowDirection(int rowDirection) {
		this.rowDirection = rowDirection;
	}

	public void setRowMove(boolean isRowMove) {
		this.isRowMove = isRowMove;

	}

	public void setBayDirection(int bayDirection) {
		this.bayDirection = bayDirection;
	}

}
