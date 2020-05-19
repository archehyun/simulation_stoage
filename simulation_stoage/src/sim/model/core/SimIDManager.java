package sim.model.core;

import java.util.LinkedList;
import java.util.List;

/**
 * @author archehyun
 *
 */
public class SimIDManager {

	private static SimIDManager instance;

	/**
	 *sim model list
	 */
	private List simModelList;

	private SimIDManager()
	{
		simModelList = new LinkedList<SimModel>();
	}

	public static SimIDManager getInstance()
	{
		if(instance ==null)
			instance = new SimIDManager();

		return instance;
	}

	public int addSimModel(SimModel model)
	{
		simModelList.add(model);

		return 0;
	}

}
