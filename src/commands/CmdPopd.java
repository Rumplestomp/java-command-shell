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
import containers.CommandDescription;
import filesystem.DirectoryStack;
import io.Writable;
import utilities.Command;
import utilities.ExitCode;

public class CmdPopd extends Command {

  private final String NAME = "popd";
  private CommandDescription DESCRIPTION = null; // TODO: initialize

  @Override
  public ExitCode execute(CommandArgs args, Writable out, Writable errOut) {
    DirectoryStack dirStack = DirectoryStack.getInstance();
    // get the most recently added directory off the stack
    if (!dirStack.empty()) {
      // get the path of the most recent directory
      String mostRecent = dirStack.pop();
      String[] arg = {mostRecent};
      // make command args to call the cd command with
      CommandArgs cdArgs = new CommandArgs("cd", arg);
      // execute the cd command to go to the directory popped off
      // the stack
      commandManager.executeCommand(cdArgs);
      // the command does not need to print anything
      return ExitCode.SUCCESS;
    } else {
      errOut.writeln("Error: The directory stack is empty.");
      return ExitCode.FAILURE;
    }
  }

  @Override
  public boolean isValidArgs(CommandArgs args) {
    // this command does not take any arguments
    return args.getCommandName().equals(NAME)
        && args.getCommandParameters().length == 0
        && args.getNumberOfNamedCommandParameters() == 0
        && args.getRedirectOperator().equals("")
        && args.getTargetDestination().equals("");
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public CommandDescription getDescription() {
    return DESCRIPTION;
    /*
     * return "popd Command Documentation\n" + "Description:\n" +
     * "    - popd: removes the directory at the top of the " +
     * "directory stack and changes the current working " +
     * "directory to the removed directory.\n" + "\n Usage: popd";
     */
    // TODO: remove
  }

}
