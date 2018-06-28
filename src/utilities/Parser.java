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

import containers.CommandArgs;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

  private static final String OVERWRITE_OPERATOR = ">";
  private static final String APPEND_OPERATOR = ">>";
  private static final String TYPE_ARG_OPERATOR = "-";

  /**
   * Parses the given input and returns a CommandArgs instance with the parsed
   * information
   *
   * @param input The user input string
   * @return Returns a CommandArgs instance with the parsed user input or null
   * if the user input is incorrect
   */
  public static CommandArgs parseUserInput(String input) {
    // Trim any leading/trailing whitespaces/tabs from the input
    input = input.trim();

    // Split the user input by quotes, spaces and/or tabs
    // Handles the cases were more than 1 consecutive spaces/tabs are used
    List<String> inputSplit = new ArrayList<>();

    // Apply the regex expression to the input string
    Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*")
        .matcher(input);

    // Add the items from the matcher to the input split list
    while (m.find()) {
      inputSplit.add(m.group(1));
    }

    // If no input parameters are found then return null
    if (inputSplit.size() == 0 || inputSplit.get(0).equals("")) {
      return null;
    }

    // Get command name
    String cmdName = inputSplit.get(0);
    // Initialize an array list for all the command parameter
    List<String> paramsArrayList = new ArrayList<>();
    // Initialize the hash map for the named type parameters
    HashMap<String, String> namedParamsMap = new HashMap<>();
    // Initialize the redirect operator to its empty state
    String redirOperator = "";
    // Initialize the target destination
    String targetDest = "";

    // Iterate through the items after index 0
    for (int i = 1; i < inputSplit.size(); i++) {
      // If a redirect operator is found
      if (inputSplit.get(i).equals(OVERWRITE_OPERATOR)
          || inputSplit.get(i).equals(APPEND_OPERATOR)) {

        // If there is not only a single item after i then the input is invalid
        if (i + 1 != inputSplit.size() - 1) {
          return null;
        }

        // Set the redirect operator
        redirOperator = inputSplit.get(i);
        // Set the target destination (without any quotes)
        targetDest = inputSplit.get(i + 1).replace("\"", "");
        // Break out of the for loop
        break;
      }

      // If the item at index i is a type parameter
      if (inputSplit.size() > 0
          && inputSplit.get(i).startsWith(TYPE_ARG_OPERATOR)) {
        // If there is no item after i then return invalid 
        if (i + 1 >= inputSplit.size()) {
          return null;
        }

        // If the item after is another type parameter then return invalid
        if (inputSplit.get(i + 1).startsWith(TYPE_ARG_OPERATOR)) {
          return null;
        }

        // Remove the - and set the key value
        String key = inputSplit.get(i).replaceFirst("-", "");
        // Remove any quoutes and set the value's value
        String val = inputSplit.get(i + 1).replace("\"", "");

        // Add to the hashmap
        namedParamsMap.put(key, val);

        // Force increment i by 1 (since we already dealt with index i + 1)
        i += 1;
      } else {
        // Add the parameters to the array list (without any quotes
        paramsArrayList.add(inputSplit.get(i).replace("\"", ""));
      }
    }

    // Convert the parameter arraylist to an array
    String[] cmdParams = paramsArrayList.toArray(new String[0]);

    // Instantiate a CommandArgs instance with the parsed user input and return
    // the CommandArgs instance
    return new CommandArgs(cmdName, cmdParams, namedParamsMap,
        redirOperator,
        targetDest);
  }
}
