package sim.view.framework;

import map.cavas.DrawObject;
import sim.view.Vector2;

/**
 * 이미지 표시 클래스
 *
 * @author LDCC
 *
 */
public abstract class SimViewObject implements DrawObject, IFMonitor {

	//객체 위치
	public final Vector2 position;

	//
	private int simID;

	// x y 위치
	public float initX, initY;

	// 객체 경계
	public final Rectangle bounds;

	// 표시 비율

	/**
	 * 표시비율
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


}
