package sim.model;

import java.util.LinkedList;
import java.util.List;

public class SimIDManager {
	
	private static SimIDManager instance;
	List list;
	
	private SimIDManager()
	{
		list = new LinkedList<SimModel>();
	}
	
	public static SimIDManager getInstance()
	{
		if(instance ==null)
			instance = new SimIDManager();
		
		return instance;
	}
	
	public int addSimModel(SimModel model)
	{
		list.add(model);
		
		return 0;
	}

}
