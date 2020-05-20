package sim.view.framework;

/**
 * �簢�� ��� ǥ�� Ŭ����
 *
 * @author LDCC
 *
 */
public class Rectangle {

	public final Vector2 lowerLeft;
	public float width, height;

	public Rectangle(float x, float y, float width, float height) {
		this.lowerLeft = new Vector2(x, y);
		this.width = width;
		this.height = height;
	}

	@Override
	public String toString() {
		return "[" + lowerLeft.x + "," + lowerLeft.y + "," + width + "," + height + "]";
	}

}
