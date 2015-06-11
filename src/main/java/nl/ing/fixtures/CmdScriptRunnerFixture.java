package nl.ing.fixtures;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This FIT test fixture provides commandline features (shell-like behavior)
 * from a FIT table. It provides features for running commands, creating files,
 * and provides some utility functions. <br/>
 * <br/>
 * The table rows are executed in order, from top to bottom. <br/>
 * <br/>
 * Example FIT table:<br/>
 * <br/>
 * !| script | cmd script runner fixture |<br/>
 * | set directory for test | C:\Test\ |<br/>
 * | run command | Filecopy.bat C:\Test\FileToCopy.txt C:\Test\ResultOfCopy.txt |<br/>
 * | create file | Testfile.txt | with | This is file content! |<br/>
 * | open file | NewTestfile.txt |<br/>
 * | make file executable |<br/>
 * | add line to file | This is line 1 |<br/>
 * | add line to file | This is line 2 |<br/>
 * | add line to file | This is line 3 |<br/>
 * | write and close file |<br/>
 * | wait for | 3 |<br/>
 * | file | Testfile.txt | mutated before | 946080000 |<br/>
 * | file | Testfile.txt | mutated after| 946080000 |<br/>
 * | create executable file | NewFilecopy.bat | with | copy %1 %2|<br/>
 * | create file | FileToBeCopiedByNewScript.txt | with | This is a file for the new copy test. |<br/>
 * | run command | NewFilecopy.bat C:\Test\FileToBeCopiedByNewScript.txt C:\Test\ResultOfTheNewCopy.txt |<br/>
 * 
 * @author Robbert van Vugt (robbert.van.vugt@ing.nl / rvvugt@sogyo.nl)
 *
 */
public class CmdScriptRunnerFixture {

	private static final int lineLength = 20;
	private String filename;
	private boolean isFileOpenForWriting = false;
	private boolean makeExecutable = false;
	private List<String> fileLines;
	private String directory = "";

	/**
	 * Default constructor.
	 */
	public CmdScriptRunnerFixture() {
		System.out.println("---------------------------------------");
		System.out.println("Running CmdScriptRunnerFixture");
	}

	/**
	 * <p>
	 * Sets the directory prefix for the other statements in this FIT test
	 * table. This directory prefix is applied to all other statements where
	 * files or commands are used. This directory prefix does not apply to
	 * command arguments unless these arguments are defined in their own collumn
	 * in the FIT table.<br/>
	 * <br/>
	 * If the directory prefix is set to "C:\Test\":
	 * <ul>
	 * <li>
	 * a file with the name "TestFile.txt" will be referenced as
	 * "C:\Test\TestFile.txt"</li>
	 * <li>
	 * a command with the name "Filecopy.bat" will be referenced as
	 * "C:\Test\Filecopy.bat"</li>
	 * <li>
	 * arguments for a command with the name "Filecopy.bat FileA.txt FileB.txt"
	 * will <b>NOT</b> be prefixed with the directory prefix, so will result in
	 * "C:\Test\Filecopy.bat FileA.txt FileB.txt"</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Markup used in Selenium:<br/>
	 * <br/>
	 * <b>| set directory for test | C:\Test\ |</b>
	 * </p>
	 * 
	 * @param directory
	 */
	public void setDirectoryForTest(String directory) {

		if ( !directory.endsWith("\\") || !directory.endsWith("/") ) {
			directory = directory + "/";
		}
		this.directory = directory;
	}

	/**
	 * <p>
	 * This method executes and waits for a command to complete. If
	 * {@link #setDirectoryForTest(String) setDirectoryForTest} is used the
	 * command will be executed from this location.
	 * </p>
	 * <p>
	 * Markup used in Selenium: <br/>
	 * <br/>
	 * <b>| run command | Filecopy.bat FileA.txt FileB.txt |</b>
	 * </p>
	 * 
	 * @param cmd Name of the command to be executed.
	 * @throws CommandFixtureException Exception thrown when the command cannot be executed.
	 * @see CmdScriptRunnerFixture#setDirectoryForTest(String)
	 *      setDirectoryForTest
	 */
	public void runCommand(String cmd) throws CommandFixtureException {

		cmd = directory + cmd;

		List<String> commandString = new ArrayList<String>();

		StringTokenizer st = new StringTokenizer(cmd);
		while ( st.hasMoreTokens() ) {
			commandString.add(st.nextToken());
		}

		try {
			System.out.println("Running command \"" + commandString.get(0)
					+ "\" with arguments: ");
			for (int i = 1; i < commandString.size(); i++) {
				System.out.println("  arg " + i + ": " + commandString.get(i));
			}
			Runtime.getRuntime().exec(cmd);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new CommandFixtureException(ioe);
		}
	}

	/**
	 * <p>
	 * Created a file with the specified content.
	 * </p>
	 * <p>
	 * Markup used in Selenium: <br/>
	 * <br/>
	 * <b>| create file | Testfile.txt | with | This is file content! |</b>
	 * </p>
	 * 
	 * @param filename Name of the file to be created.
	 * @param contents Contents to be specified within the file.
	 * @throws CommandFixtureException Exception throw when the specified file cannot be created.
	 */
	public void createFileWith(String filename, String contents)
			throws CommandFixtureException {

		filename = directory + filename;

		try {
			System.out.println("Creating file \"" + filename + "\"");

			File file = new File(filename);
			FileWriter writer = new FileWriter(file);
			writer.write(contents);
			writer.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new CommandFixtureException(ioe);
		}
	}

	/**
	 * <p>
	 * Created an executable file with the specified content.
	 * </p>
	 * <p>
	 * Markup used in Selenium: <br/>
	 * <br/>
	 * <b>| create executable file | NewFilecopy.bat | with | copy %1 %2|</b>
	 * </p>
	 * 
	 * @param filename Name of the executable file to be created.
	 * @param contents Contents to be specified within the file.
	 * @throws CommandFixtureException Exception throw when the specified file cannot be created.
	 */
	public void createExecutableFileWith(String filename, String contents)
			throws CommandFixtureException {

		filename = directory + filename;

		try {
			System.out.println("Creating executible file \"" + filename + "\"");

			File file = new File(filename);
			file.setExecutable(true);
			FileWriter writer = new FileWriter(file);
			writer.write(contents);
			writer.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new CommandFixtureException(ioe);
		}
	}

	/**
	 * <p>
	 * This method opens a file for writing.
	 * </p>
	 * <p>
	 * Markup used in Selenium: <br/>
	 * <br/>
	 * <b>| open file | Testfile.txt |</b>
	 * </p>
	 * 
	 * @param filename
	 *            Name of the file. If {@link #setDirectoryForTest(String)
	 *            setDirectoryForTest} is used the filename can be specified
	 *            without a directory location, otherwise the directory should
	 *            be added to the filename.
	 * @throws CommandFixtureException Exception thrown when another file is already opened for writing.
	 * @see CmdScriptRunnerFixture#setDirectoryForTest(String)
	 *      setDirectoryForTest
	 */
	public void openFile(String filename) throws CommandFixtureException {

		if ( this.isFileOpenForWriting ) {
			throw new CommandFixtureException("A file is already opened. It is not possible to open more than one file at any given time.");
		}
		
		filename = directory + filename;

		System.out.println("Opening file \"" + filename);

		this.filename = filename.trim();
		this.isFileOpenForWriting = true;
	}

	/**
	 * <p>
	 * This method adds the specified text to the opened file (
	 * {@link #openFile(String) openFile}).
	 * </p>
	 * <p>
	 * Markup used in Selenium: <br/>
	 * <br/>
	 * <b>| add line to file | This is line 1 |</b>
	 * </p>
	 * 
	 * @param line
	 *            Text to be added to the file.
	 * @throws CommandFixtureException Exception thrown when there is no file opened for writing.
	 * @see CmdScriptRunnerFixture#openFile(String) openFile
	 */
	public void addLineToFile(String line) throws CommandFixtureException {
		
		if ( !this.isFileOpenForWriting ) {
			throw new CommandFixtureException("No open file available for writing.");
		}

		if (line.length() <= CmdScriptRunnerFixture.lineLength) {
			System.out.println("Adding line to file with content: \"" + line
					+ "\"");
		} else {
			System.out
					.println("Adding line to file with content: \""
							+ line.subSequence(0,
									CmdScriptRunnerFixture.lineLength - 1)
							+ "\"...");
		}

		if (this.fileLines == null) {
			this.fileLines = new ArrayList<String>();
		}
		this.fileLines.add(line.trim() + System.getProperty("line.separator"));
	}
	
	/**
	 * <p>
	 * Makes the opened file executable.
	 * </p>
	 * <p>
	 * Markup used in Selenium: <br/>
	 * <br/>
	 * <b>| make file executable |</b>
	 * </p>
	 * 
	 * @throws CommandFixtureException Exception thrown when there is no file opened to make executable.
	 * @see CmdScriptRunnerFixture#openFile(String) openFile
	 */
	public void makeFileExecutable() throws CommandFixtureException {
		
		if ( !this.isFileOpenForWriting ) {
			throw new CommandFixtureException("No open file available to make executable.");
		}
		
		this.makeExecutable = true;
	}
	
	/**
	 * <p>
	 * This method closes the opened file ({@link #openFile(String) openFile}).
	 * </p>
	 * <p>
	 * Markup used in Selenium: <br/>
	 * <br/>
	 * <b>| write and close file |</b>
	 * </p>
	 * 
	 * @throws CommandFixtureException
	 *             Exception thrown when closing the file fails or when no file has been opened for writing.
	 * @see CmdScriptRunnerFixture#openFile(String) openFile
	 */
	public void writeAndCloseFile() throws CommandFixtureException {
		
		if ( !this.isFileOpenForWriting ) {
			throw new CommandFixtureException("No file has been opened for writing.");
		}

		System.out.println("Writing and closing file");
		try {

			File file = new File(this.filename);
			file.setExecutable(this.makeExecutable);
			FileWriter writer = new FileWriter(file);

			if (this.fileLines != null && this.fileLines.size() > 0) {
				for (int i = 0; i < this.fileLines.size(); i++) {
					writer.write(this.fileLines.get(i));
				}
			}
			writer.close();
			this.fileLines = null;
			this.isFileOpenForWriting = false;
			this.makeExecutable = false;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			throw new CommandFixtureException("Error writing and closing file with the name: " + this.filename, ioe);
		}
	}

	/**
	 * <p>
	 * Deletes the specified file.
	 * </p>
	 * <p>
	 * Markup used in Selenium: <br/>
	 * <br/>
	 * <b>| delete file | Testfile.txt |</b>
	 * </p>
	 * 
	 * @param filename Name of the file to be deleted.
	 * @throws CommandFixtureException
	 *             Exception thrown when the specified file does not exists.
	 * @return True if the file is deleted correctly.
	 */
	public boolean deleteFile(String filename) throws CommandFixtureException {
		
		System.out.println("Deleting file \"" + filename);
		
		filename = directory + filename;
		File file = new File(filename);
		if ( file.exists() ) {
			return file.delete();
		} else {
			throw new CommandFixtureException("File \"" + filename + "\" does not exist.");
		}
	}

	/**
	 * <p>
	 * This method checks to see if the specified file has been modified after
	 * the specified time in seconds since the epoch (00:00:00 GMT, January 1,
	 * 1970).
	 * </p>
	 * <p>
	 * Markup used in Selenium: <br/>
	 * <br/>
	 * <b>| file | Testfile.txt | mutated after | 946080000 |</b>
	 * </p>
	 * 
	 * @param filename
	 *            Filename used in the described check.
	 * @param time
	 *            Time, in seconds since the epoch (00:00:00 GMT, January 1,
	 *            1970), used in the described check.
	 * @return True if the specified file has been modified after the specified
	 *         time.
	 */
	public boolean fileMutatedAfter(String filename, int time) {
		
		filename = directory + filename;
		
		File file = new File(filename);
		if ( file.lastModified() > (time * 1000) ) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * <p>
	 * This method checks to see if the specified file has been modified before
	 * the specified time in seconds since the epoch (00:00:00 GMT, January 1,
	 * 1970).
	 * </p>
	 * <p>
	 * Markup used in Selenium: <br/>
	 * <br/>
	 * <b>| file | Testfile.txt | mutated before | 946080000 |</b>
	 * </p>
	 * 
	 * @param filename
	 *            Filename used in the described check.
	 * @param time
	 *            Time, in seconds since the epoch (00:00:00 GMT, January 1,
	 *            1970), used in the described check.
	 * @return True if the specified file has been modified before the specified
	 *         time.
	 */
	public boolean fileMutatedBefore(String filename, int time) {
		
		filename = directory + filename;
		
		File file = new File(filename);
		if ( file.lastModified() < (time * 1000) ) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * <p>
	 * This method will pause the executing of the test for the specified number
	 * of seconds.
	 * </p>
	 * <p>
	 * Markup used in Selenium: <br/>
	 * <br/>
	 * <b>| wait for | 3 |</b>
	 * </p>
	 * 
	 * @param waitInterval
	 *            Interval in seconds used as a pause before continuing with the
	 *            next command.
	 */
	public void waitFor(int waitInterval) {

		try {
			long milliseconds = 1000 * waitInterval;
			Thread.sleep(milliseconds);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}

}
