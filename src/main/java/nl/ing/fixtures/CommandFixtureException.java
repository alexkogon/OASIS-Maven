/**
 * 
 */
package nl.ing.fixtures;

/**
 * @author rvvugt
 *
 */
public class CommandFixtureException extends Exception {

	private static final long serialVersionUID = 7581410236756348098L;

	public CommandFixtureException() {
	}
	
	public CommandFixtureException(String msg) {
		super(msg);
	}
	
	public CommandFixtureException(Exception e) {
		super(e);
	}
	
	public CommandFixtureException(String msg, Exception e) {
		super(msg, e);
	}
	
}
