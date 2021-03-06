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
package unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import commands.CmdCurl;
import containers.CommandArgs;
import filesystem.FileSystem;
import io.BufferedConsole;
import org.junit.Before;
import org.junit.Test;
import utilities.Command;
import utilities.CommandManager;
import utilities.ExitCode;
import utilities.Parser;

public class CmdCurlTest {

  // Create Testing Consoles, a command manager instance, an instance of the
  // mock file system and an instance of the command
  private BufferedConsole<String> tc;
  private BufferedConsole<String> tc_qry;
  private BufferedConsole<String> tc_err;
  private FileSystem fs;
  private CommandManager cm;
  private Command cmd;

  @Before
  // Resets the file system for each test case
  public void reset() {
    tc = new BufferedConsole<>();
    tc_qry = new BufferedConsole<>();
    tc_err = new BufferedConsole<>();
    fs = new MockFileSystem();
    cm = CommandManager.constructCommandManager(tc, tc_qry, tc_err, fs);
    cmd = new CmdCurl(fs, cm);
  }

  @Test
  public void testValidTextURL() {
    CommandArgs args = Parser
        .parseUserInput("curl http://www.cs.cmu.edu/~spok/grimmtmp/073.txt");
    ExitCode cmdExit = cmd.execute(args, tc, tc_qry, tc_err);

    assertEquals(ExitCode.SUCCESS, cmdExit);
    assertTrue(tc.getAllWritesAsString().length() > 0);
  }

  @Test
  public void testValidHTMLURL() {
    CommandArgs args = Parser.parseUserInput(
        "curl https://www.w3.org/Style/Examples/011/firstcss.en.html");
    ExitCode cmdExit = cmd.execute(args, tc, tc_qry, tc_err);

    assertEquals(ExitCode.SUCCESS, cmdExit);
    assertTrue(tc.getAllWritesAsString().length() > 0);
  }

  @Test
  public void testNotFoundURL() {
    CommandArgs args = Parser
        .parseUserInput("curl http://www.ub.edu/gilcub/SIMPLE/simple.html");
    ExitCode cmdExit = cmd.execute(args, tc, tc_qry, tc_err);

    assertEquals(ExitCode.FAILURE, cmdExit);
  }

  @Test
  public void testInvalidURL() {
    CommandArgs args =
        Parser.parseUserInput("curl some_randomInVaLiD_URL.html");
    ExitCode cmdExit = cmd.execute(args, tc, tc_qry, tc_err);

    assertEquals(ExitCode.FAILURE, cmdExit);
  }
}
