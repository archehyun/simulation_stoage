package sim.view.framework;

import sim.view.Vector2;

/**
 * 사각형 경계 표현 클래스
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

}
