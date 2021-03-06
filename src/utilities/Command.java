// **********************************************************
// Assignment2:
// Student1:
// UTORID user_name: ursualex
// UT Student #: 1004357199
// Author: Alexander Ursu
//
// Student2:
// UTORID user_name: greffal1
// UT Student #: 1004254497
// Author: Alexander Greff
//
// Student3:
// UTORID user_name: sankarch
// UT Student #: 1004174895
// Author: Chedy Sankar
//
// Student4:
// UTORID user_name: kamins42
// UT Student #: 1004431992
// Author: Anton Kaminsky
//
//
// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC B07 and understand the consequences.
// *********************************************************
package utilities;

import static utilities.JShellConstants.OVERWRITE_OPERATOR;
import containers.CommandArgs;
import containers.CommandDescription;
import filesystem.Directory;
import filesystem.FSElementAlreadyExistsException;
import filesystem.FSElementNotFoundException;
import filesystem.File;
import filesystem.FileSystem;
import filesystem.MalformedPathException;
import filesystem.Path;
import io.BufferedConsole;
import io.Console;
import io.Writable;

/**
 * The abstract command class that all commands inherit from.
 *
 * @author greff
 */
public abstract class Command {

  /**
   * The command's name.
   */
  protected String NAME;

  /**
   * The command's description.
   */
  protected CommandDescription DESCRIPTION;

  /**
   * The file system reference.
   */
  protected FileSystem fileSystem;

  /**
   * The command manager reference.
   */
  protected CommandManager commandManager;

  /**
   * Constructs a new command instance.
   *
   * @param fileSystem The file system the command uses.
   * @param commandManager The command manager the command uses.
   */
  public Command(FileSystem fileSystem, CommandManager commandManager) {
    this.fileSystem = fileSystem;
    this.commandManager = commandManager;
  }

  /**
   * Constructs a new command instance.
   *
   * @param name The name of the command.
   * @param description The description of the command.
   * @param fileSystem The file system the command uses.
   * @param commandManager The command manaeger the command uses.
   */
  public Command(String name, CommandDescription description,
      FileSystem fileSystem, CommandManager commandManager) {

    this(fileSystem, commandManager);
    this.NAME = name;
    this.DESCRIPTION = description;
  }

  /**
   * Executes the the argument check and command function as well has handling
   * file redirecting.
   *
   * @param args The arguments for the command call.
   * @param console The standard console.
   * @param queryConsole The query console.
   * @param errorConsole The error console.
   * @return Returns the exit condition of the command.
   */
  public final ExitCode execute(CommandArgs args, Console<String> console,
      Console<String> queryConsole, Console<String> errorConsole) {
    // Check if the arguments are invalid for the current command
    if (!isValidArgs(args)) {
      errorConsole.writeln("Error: Invalid arguments");
      return ExitCode.FAILURE;
    }

    // Initialize the buffered output console
    BufferedConsole<String> bufferedConsole = new BufferedConsole<>();

    // Run the command and store the exit code
    ExitCode cmdExitCode =
        run(args, bufferedConsole, queryConsole, errorConsole);

    // Construct a string of all the output of the command
    String resultStr = bufferedConsole.getAllWritesAsString();

    // Run the redirect operator, if needed
    ExitCode writeExitCode = ExitCode.SUCCESS;
    if (!args.getRedirectOperator().isEmpty()) {
      writeExitCode = writeToFile(resultStr, args.getRedirectOperator(),
          args.getTargetDestination(), errorConsole);
    }
    // If no redirect operator then just write to the console
    else {
      console.write(resultStr);
    }

    // If both the command and redirect succeeded then return a success
    if (cmdExitCode == ExitCode.SUCCESS && writeExitCode == ExitCode.SUCCESS) {
      return ExitCode.SUCCESS;
    }

    // Else return a failure
    return ExitCode.FAILURE;
  }

  /**
   * Executes the command's function.
   *
   * @param args The arguments for the command call.
   * @param console The standard console.
   * @param queryConsole The query console.
   * @param errorConsole The error console.
   * @return Returns the exit condition of the command.
   */
  protected abstract ExitCode run(CommandArgs args, Console<String> console,
      Console<String> queryConsole, Console<String> errorConsole);

  /**
   * Checks if the given args are valid for this command.
   *
   * @param args The command arguments.
   * @return Returns true iff the args are valid.
   */
  public abstract boolean isValidArgs(CommandArgs args);

  /**
   * Gets the name of the command.
   *
   * @return Returns the name of the command.
   */
  public String getName() {
    return this.NAME;
  }

  /**
   * Gets the CommandDescription object for the command.
   *
   * @return Returns the CommandDescription object for the command.
   */
  public CommandDescription getDescription() {
    return this.DESCRIPTION;
  }

  /**
   * A helper function that writes a string to a file.
   *
   * @param content The string content to write to the file.
   * @param redirectOperator The redirect operator being used.
   * @param targetDestination The file location to write to.
   * @param errOut The error output to use.
   * @return Returns if the write succeeded or not.
   */
  protected ExitCode writeToFile(String content, String redirectOperator,
      String targetDestination, Writable<String> errOut) {
    // Get the File
    File file;
    try {
      file = fileSystem.getFileByPath(new Path(targetDestination));

      // If the file does not exist
    } catch (FSElementNotFoundException e) {
      // Attempt to make the file
      try {
        file = makeFile(targetDestination);
        // Catch if the directory is not found
      } catch (FSElementNotFoundException e1) {
        errOut.writeln("Error: No file/directory found");
        return ExitCode.FAILURE;
        // Catch if the path is invalid
      } catch (MalformedPathException e1) {
        errOut.write("Error: Invalid path \"" + targetDestination + "\"");
        return ExitCode.FAILURE;
        // Catch if the file already exists
      } catch (FSElementAlreadyExistsException e1) {
        errOut.writeln("Error: File/directory already exists");
        return ExitCode.FAILURE;
      } catch (Exception e1) {
        return ExitCode.FAILURE;
      }
    } catch (MalformedPathException e1) {
      errOut.writeln("Error: Not a valid file path");
      return ExitCode.FAILURE;
    }

    // Wipe the file contents if the overwrite operator is given in the args
    if (redirectOperator.equals(OVERWRITE_OPERATOR)) {
      file.clear();
    }

    // Add the string contents to the file
    file.write(content);

    // Reaching this point means that the write to file executed successfully
    return ExitCode.SUCCESS;
  }


  /**
   * Attempts to make a file from the given file path string.
   *
   * @param filePathStr The file path string.
   * @return Returns the created file.
   */
  private File<?> makeFile(String filePathStr) throws MalformedPathException,
      FSElementNotFoundException, FSElementAlreadyExistsException {

    boolean pathIsADirectory = true;
    try {
      fileSystem.getDirByPath(new Path(filePathStr));
    } catch (FSElementNotFoundException e) {
      pathIsADirectory = false;
    }

    if (pathIsADirectory) {
      throw new FSElementAlreadyExistsException();
    }

    boolean absolutePath = filePathStr.startsWith("/");

    // Make the new file
    String[] fileSplit = filePathStr.split("/");

    String fileName = "";
    if (fileSplit.length > 0) {
      fileName = fileSplit[fileSplit.length - 1];
    }

    if (fileName.equals(".") || fileName.equals("..") || fileName.equals("")
        || filePathStr.endsWith("/")) {
      throw new FSElementNotFoundException();
    }

    // Get the index of the last "/"
    int lastSlash = filePathStr.lastIndexOf('/');

    // Get the directory that the file is in
    String dirPathStr =
        (lastSlash > -1) ? filePathStr.substring(0, lastSlash) : "";

    // If the file is in the root
    if (dirPathStr.equals("")) {
      if (absolutePath) {
        dirPathStr = "/";
      } else {
        dirPathStr = fileSystem.getWorkingDirPath();
      }
    }

    // Get the directory at the path
    Directory dirOfFile = fileSystem.getDirByPath(new Path(dirPathStr));

    // Add the file to the directory
    // Return the file
    return dirOfFile.createAndAddNewFile(fileName);
  }

  /**
   * Returns if the the given string is a string parameter (ie it starts and
   * ends with ").
   *
   * @param s The string.
   * @return Returns true iff s is a string parameter.
   */
  protected boolean isStringParam(String s) {
    if (s == null) {
      return false;
    }
    return s.startsWith("\"") && s.endsWith("\"");
  }

  /**
   * Removes the surrounding quotes to a parameter.
   *
   * @param s The original parameter string.
   * @return Returns a string without the string quoutes.
   */
  protected String removeStringQuotes(String s) {
    // If an already non-string string is given then just return the string
    if (!isStringParam(s)) {
      return s;
    }

    // cut the first and last characters out (ie the "<string>" qoutes)
    return s.substring(0, s.length() - 1).substring(1);
  }
}
