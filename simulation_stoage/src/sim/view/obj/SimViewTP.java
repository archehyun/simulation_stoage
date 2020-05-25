package sim.view.obj;

import java.awt.Graphics;

import sim.model.core.SimEvent;
import sim.model.impl.stoage.block.Block;
import sim.model.impl.stoage.block.BlockManager;
import sim.view.framework.SimViewObject;

public class SimViewTP extends SimViewObject {

	BlockManager manager = BlockManager.getInstance();

	float xx, yy, ww, hh;

	private Block block;
	public SimViewTP(int simID, float initX, float initY, float width, float height) {
		super(simID, initX, initY, width, height);
		block = manager.getBlock(simID);
	}

	/**
	 * @param g
	 * @param blockRate
	 */
	private void drawTP(Graphics g, float blockRate) {
		int TP[] = block.getTP();
		for (int j = 0; j < TP.length; j++) {
			xx = ((initX + j * (BlockManager.conW + BlockManager.wGap)) * blockRate);
			yy = ((initY - (BlockManager.conH + BlockManager.hGap)) * blockRate);

			if (TP[j] == Block.FULL_TP) {
				g.fillRect((int) xx, (int) yy, (int) ww, (int) hh);
			} else if (TP[j] == Block.EMPTY_TP) {
				g.drawRect((int) xx, (int) yy, (int) ww, (int) hh);
			}
		}

	}

	@Override
	public void draw(Graphics g) {
		drawTP(g, BlockManager.blockRate);

	}

	@Override
	public void updateMonitor(SimEvent message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setCountView(boolean selected) {
		// TODO Auto-generated method stub

	}

}
