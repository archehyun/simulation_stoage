package sim.view.framework;

import map.cavas.DrawObject;

/**
 * �̹��� ǥ�� Ŭ����
 *
 * @author LDCC
 *
 */
public abstract class SimViewObject implements DrawObject, IFMonitor {

	//��ü ��ġ
	public final Vector2 position;

	//
	private int simID;

	// x y ��ġ
	public float initX, initY;

	// ��ü ���
	public final Rectangle bounds;

	// ǥ�� ����

	/**
	 * ǥ�ú���
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
