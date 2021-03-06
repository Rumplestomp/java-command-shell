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

import static utilities.JShellConstants.APPEND_OPERATOR;
import static utilities.JShellConstants.COMMAND_RECALL_CHAR;
import static utilities.JShellConstants.COMMAND_RECALL_NAME;
import static utilities.JShellConstants.OVERWRITE_OPERATOR;
import containers.CommandArgs;
import containers.CommandDescription;
import filesystem.FileSystem;
import io.Console;
import utilities.Command;
import utilities.CommandManager;
import utilities.ExitCode;

/**
 * The man command.
 *
 * @author greff
 */
public class CmdMan extends Command {

  /**
   * Constructs a new command instance.
   *
   * @param fileSystem The file system that the command uses.
   * @param commandManager The command manager that the command uses.
   */
  public CmdMan(FileSystem fileSystem, CommandManager commandManager) {
    super(NAME, DESCRIPTION, fileSystem, commandManager);
  }

  // Setup command information
  /**
   * The name of the command.
   */
  private static final String NAME = "man";
  /**
   * The description of the command.
   */
  private static final CommandDescription DESCRIPTION =
      new CommandDescription.DescriptionBuilder(
          "Gets documentation for commands.", "man COMMAND").additionalComment(
              "Fun fact: running \"man man\" is called man-ception... which you"
                  + " just discovered.")
              .build();

  /**
   * Executes the man command with the arguments args.
   *
   * @param args The command arguments.
   * @param console The standard console.
   * @param queryConsole The query console.
   * @param errorConsole The error console.
   * @return Returns the ExitCode of the command, SUCCESS or FAILURE.
   */
  @Override
  protected ExitCode run(CommandArgs args, Console<String> console,
      Console<String> queryConsole, Console<String> errorConsole) {
    // Get the command name from the parameters
    String cmdName = args.getCommandParameters()[0];
    // Special case where "man !" is inputed change to "man recall" so the
    // documentation can be found
    if (cmdName.equals(COMMAND_RECALL_CHAR)) {
      cmdName = COMMAND_RECALL_NAME;
    }
    // Get the description of the command
    CommandDescription cmdDesc = commandManager.getCommandDescription(cmdName);

    // If the command does not have a command description object
    if (cmdDesc == null) {
      // Write to an error message
      errorConsole.writeln(
          "Error: No description found for command \"" + cmdName + "\"");
      // Return the failure exit code
      return ExitCode.FAILURE;
    }

    // In the case that there is a command description object
    // Initialize a string builder object
    StringBuilder output = new StringBuilder();

    // Build the header for the command documentation
    output.append("\"").append(cmdName).append("\" Command Documentation\n");

    // Build the description section
    output.append("Description:");
    for (String description : cmdDesc.getDescription()) {
      output.append("\n\t- ").append(description);
    }

    // Build the usage section
    output.append("\n").append("Usage:");
    for (String usage : cmdDesc.getUsages()) {
      output.append("\n\t- ").append(usage);
    }

    // If applicable, build the additional comments section
    if (!cmdDesc.getAdditionalComments().isEmpty()) {
      output.append("\nAdditional Comments:");
      for (String comment : cmdDesc.getAdditionalComments()) {
        output.append("\n\t- ").append(comment);
      }
    }

    // Construct the result string
    String resultStr = output.toString();
    if (!resultStr.isEmpty()) {
      resultStr += "\n";
    }

    // Write all the contents read to the Console and return SUCCESS always
    console.write(resultStr);
    return ExitCode.SUCCESS;
  }

  /**
   * Checks if args is a valid CommandArgs instance for this command.
   *
   * @param args The command arguments.
   * @return Returns true iff args is a valid for this command.
   */
  public boolean isValidArgs(CommandArgs args) {
    // Check that the form matches for the args
    boolean paramsMatches = args.getCommandName().equals(NAME)
        && args.getNumberOfCommandParameters() == 1
        && args.getNumberOfCommandFieldParameters() == 0
        && args.getNumberOfNamedCommandParameters() == 0
        && (args.getRedirectOperator().equals("")
            || args.getRedirectOperator().equals(OVERWRITE_OPERATOR)
            || args.getRedirectOperator().equals(APPEND_OPERATOR));

    // Check that the parameters are not strings
    boolean stringParamsMatches = true;
    for (String p : args.getCommandParameters()) {
      stringParamsMatches = stringParamsMatches && !isStringParam(p);
    }

    // Return the result
    return paramsMatches && stringParamsMatches;
  }
}
