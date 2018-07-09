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
import commands.CmdExit;
import commands.CmdHistory;
import containers.CommandArgs;
import driver.JShell;
import filesystem.FileSystem;
import filesystem.NonPersistentFileSystem;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import utilities.Command;
import utilities.CommandManager;
import utilities.ExitCode;

public class CmdHistoryTest {
  // Create Testing Consoles, a command manager instance, an instance of the
  // mock file system and an instance of the command
  private TestingConsole tc;
  private TestingConsole tc_err;
  private FileSystem fs;
  private CommandManager cm;
  private Command cmd;

  @Before
  // Resets the file system for each test case
  public void reset() {
    tc = new TestingConsole();
    tc_err = new TestingConsole();
    fs = new NonPersistentFileSystem();
    cm = CommandManager.constructCommandManager(tc, tc_err, fs);
    cmd = new CmdHistory(fs, cm);
  }

  @BeforeClass
  public static void setup() {
    ArrayList<String> hist = JShell.getHistory();
  }

  @Test
  public void testHistory1() {
    String[] params = new String[1];
    params[0] = "1";
    CommandArgs args = new CommandArgs("history", params);

    TestingConsole tc = new TestingConsole();
    TestingConsole tc_err = new TestingConsole();

    ExitCode exc = cmd.execute(args, tc, tc_err);

    assertEquals("", tc.getLastWrite());
    assertEquals(exc, ExitCode.SUCCESS);
  }
}
