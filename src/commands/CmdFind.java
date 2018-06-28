package commands;

import containers.CommandArgs;
import filesystem.Directory;
import filesystem.FileNotFoundException;
import filesystem.FileSystem;
import filesystem.MalformedPathException;
import filesystem.Path;
import io.ErrorConsole;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import utilities.Command;

public class CmdFind extends Command {

  private final String NAME = "find";
  private final String DESCRIPTION =
      "" + "Find Command Documentation\n"
          + "Description:\n"
          + "    - find: finds all files/directories.\n"
          + "            Outputs the found file/directory paths to them."
          + "\n\n"
          + "Usage:\n"
          + "    - find PATH... -type [f|d] -name EXPRESSION\n"
          + "    \n" + "Additional Comments:\n"
          + "    - A blank string is returned if no files are found.\n";
  private final String TYPE_IDENTIFIER = "type";
  private final String NAME_IDENTIFIER = "name";
  private final String TYPE_FILE = "f";
  private final String TYPE_DIR = "d";

  @Override
  public String execute(CommandArgs args) {
    // Store the values of the named parameters
    String type = args.getNamedCommandParameter(TYPE_IDENTIFIER);
    String expression = args
        .getNamedCommandParameter(NAME_IDENTIFIER);

    StringBuilder output = new StringBuilder();

    // Get all the directory paths to be explored
    String[] dirStrPaths = args.getCommandParameters();

    for (String dirStrPath : dirStrPaths) {
      try {
        // Get the current directory
        Path dirPath = new Path(dirStrPath);
        Directory currDir = fileSystem.getDirByPath(dirPath);

        // Initialize the set of paths of the occurrences of <expression>
        Set<String> outputPaths = new HashSet<>();

        // If we're looking for file occurrences
        if (type.equals(TYPE_FILE)) {
          // Search recursively for the file
          outputPaths = findFileInDirectoryStructure(currDir,
              expression);
        }
        // If we're looking for directory occurrences
        else if (type.equals(TYPE_DIR)) {
          // Search recursively for the directory
          outputPaths = findDirectoryInDirectoryStructure(currDir,
              expression);
        }

        // Print out the set as a string with each entry on a new line
        for (String outputPath : outputPaths) {
          output.append(outputPath).append("\n");
        }

      } catch (MalformedPathException | FileNotFoundException e) {
        ErrorConsole.getInstance().writeln("Erorr: invalid file/path");
      }
    }
    // Return the output
    return output.toString();
  }


  /**
   * Gets a set of all absolute paths to instances of files with the name
   * "name"
   *
   * @param dir The current directory
   * @param name The wanted file name
   * @return Returns the set
   */
  private Set<String> findFileInDirectoryStructure(Directory dir,
      String name) throws FileNotFoundException {
    // Initialize references
    FileSystem fs = FileSystem.getInstance();
    Set<String> ret_set = new HashSet<>();

    // If the current directory contains the wanted file then add the absolute
    // path of the file to the return set
    if (dir.containsFile(name)) {
      // Get the absolute path of the direcory
      String dirAbsPath = fs.getAbsolutePathOfDir(dir);

      // If the directory is the root directory
      if (dirAbsPath.equals("/")) {
        // Remove the extra "/" character
        dirAbsPath = "";
      }

      // Add the file's path to the return set
      ret_set.add(dirAbsPath + "/" + name);
    }

    // Iterate through each child directory
    ArrayList<String> childDirNames = dir.listDirNames();
    for (String childDirName : childDirNames) {
      // Get the child directory object
      Directory childDir = dir.getDirByName(childDirName);

      // Call the function recursively again on the child directory and add any
      // instances of the file to the current return set
      ret_set.addAll(findFileInDirectoryStructure(childDir, name));
    }

    // Return the set
    return ret_set;
  }

  /**
   * Gets a set of all absolute paths to instances of directories with the name
   * "name"
   *
   * @param dir The current directory
   * @param name The wanted directory name
   * @return Returns the set
   */
  private Set<String> findDirectoryInDirectoryStructure(Directory dir,
      String name) throws FileNotFoundException {
    // Initialize references
    FileSystem fs = FileSystem.getInstance();
    Set<String> ret_set = new HashSet<>();

    // If the current directory contains the wanted directory then add the
    // absolute path of the file to the return set
    if (dir.containsDir(name)) {
      ret_set.add(fs.getAbsolutePathOfDir(dir) + "/" + name + "/");
    }

    // Iterate through each child directory
    ArrayList<String> childDirNames = dir.listDirNames();
    for (String childDirName : childDirNames) {
      // Get the child directory object
      Directory childDir = dir.getDirByName(childDirName);
      // TODO: make sure this is ok not to error
      // Call the function recursively again on the child directory and add any
      // instances of the directory to the current return set
      ret_set
          .addAll(findDirectoryInDirectoryStructure(childDir, name));
    }

    // Return the set
    return ret_set;
  }

  /**
   * A helper checking if args is a valid CommandArgs instance for this command
   *
   * @param args The command arguments
   * @return Returns true iff args is a valid for this command
   */
  public boolean isValidArgs(CommandArgs args) {
    return args.getCommandName().equals(NAME)
        && args.getCommandParameters().length > 0
        && args.getNumberOfNamedCommandParameters() == 2
        && args.getNamedCommandParameter(TYPE_IDENTIFIER) != null
        && args.getNamedCommandParameter(NAME_IDENTIFIER) != null
        && (args.getNamedCommandParameter(TYPE_IDENTIFIER)
        .equals(TYPE_FILE)
        || args.getNamedCommandParameter(TYPE_IDENTIFIER)
        .equals(TYPE_DIR))
        && args.getRedirectOperator().length() == 0
        && args.getTargetDestination().length() == 0;
  }

  /**
   * Gets the name of the command
   *
   * @return Returns the name of the command
   */
  @Override
  public String getName() {
    return NAME;
  }

  /**
   * Gets the documentation for this command
   *
   * @return The command description
   */
  @Override
  public String getDescription() {
    return DESCRIPTION;
  }
}
