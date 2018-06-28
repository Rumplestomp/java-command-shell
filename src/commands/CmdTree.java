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
package commands;

import containers.CommandArgs;
import filesystem.Directory;
import java.util.ArrayList;
import utilities.Command;


public class CmdTree extends Command {

  private final String NAME = "tree";

  @Override
  public String execute(CommandArgs args) {
    Directory root = fileSystem.getRoot();
    String result = (root.getName() + "\n");
    result += (addon(root, 1));
    return result;
  }

  @Override
  public boolean isValidArgs(CommandArgs args) {
    return args.getCommandName().equals(NAME)
        && args.getCommandParameters().length == 0
        && args.getNumberOfNamedCommandParameters() == 0
        && args.getRedirectOperator().equals("")
        && args.getTargetDestination().equals("");
  }

  private String addon(Directory curr, int tabs) {
    // get proper amount of tabs
    StringBuilder spacing = new StringBuilder();
    for (int i = 0; i < tabs; i++) {
      spacing.append("\t");
    }
    // the name of the curr dir gets inserted in the parent recursive call.
    StringBuilder result = new StringBuilder();
    // get the names of all the files in the directory
    ArrayList<String> files = curr.listFiles();
    if (files.size() > 0) {
      for (String name : files) {
        result.append(spacing).append(name).append("\n");
      }
    }
    // now finally get all of the subdirectories
    //HashMap<String, Directory> childs = curr.getChildDirs();
    ArrayList<String> childs = curr.listDirNames();
    for (String key : childs) {
      result.append(spacing).append(key).append("\n");
      //result+=addon(childs.get(key), tabs+1);
      result.append(addon(curr.getDirByName(key), tabs + 1));
    }
    return result.toString();
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getDescription() {
    return "This command prints a tree representation of the entire"
        + "filesystem, starting from the root. Takes in no parameters."
        + "Files and subdirectories within a directory appear on tab ahead,"
        + "listed below the directory name.\n";
  }

}
