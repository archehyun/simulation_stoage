package sim.model.impl.stoage.commom;


public class UnparserableCommandException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	String command;

	public UnparserableCommandException(String command) {
		super(command);
	}

}
