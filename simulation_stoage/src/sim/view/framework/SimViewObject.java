package sim.view.framework;

import map.cavas.DrawObject;

/**
 * 占싱뱄옙占쏙옙 표占쏙옙 클占쏙옙占쏙옙
 *
 * @author LDCC
 *
 */
public abstract class SimViewObject implements DrawObject, IFMonitor {

	//location view object
	public final Vector2 position;

	//
	private int simID;

	// x y initition location
	public float initX, initY;

	// 占쏙옙체 占쏙옙占�
	public final Rectangle bounds;

	// 표占쏙옙 占쏙옙占쏙옙

	/**
	 * 표占시븝옙占쏙옙
	 *
	 * @DEFAULT 100
	 */
	protected int viewRate=100;

	public SimViewObject(int simID, float initX, float initY, float width, float height)
	{
		this.initX = initX;
		this.initY = initY;
		this.simID = simID;
		position = new Vector2(initX, initY);
		this.bounds = new Rectangle((int) (initX - width / 2), (int) (initY - height / 2), (int) width, (int) height);
	}

	public abstract void setCountView(boolean selected);

}
