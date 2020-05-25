package sim.model.impl.stoage.atc.move;

import sim.model.impl.stoage.atc.SimATC;

public class ATCMove2 {

	public static final int UP = -1;

	public static final int DOWN = 1;

	public static final int STOP = 0;

	public static final int FORWORD = 1;

	public static final int BACKWORD = -1;

	private int seaLandType = 0;

	SimATC atc;

	public ATCMove2(SimATC atc) {

		this.atc = atc;
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
	public void update(double delta) {

		this.delta = delta;

		if (isBayMove) {
			int currentY = (int) atc.getLocation().y;
			float currentY2 = atc.getLocation().y;
			int destinationY = (int) atc.getDestination().y;
			if (destinationY > currentY) {
				this.setBayDirection(DOWN * seaLandType);

				System.out.println("down:" + destinationY + ", " + currentY);

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
