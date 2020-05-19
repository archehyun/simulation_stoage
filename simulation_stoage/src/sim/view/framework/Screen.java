package sim.view.framework;

import sim.view.SimMain;

public abstract class Screen {

	private SimMain main;

	public Screen(SimMain main) {
		this.main = main;
	}

	public abstract void update(float deltaTime);

	public abstract void present(float deltaTime);

	public abstract void pause();

	public abstract void resume();

	public abstract void dispose();

}
