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

import commands.CmdTree;
import containers.CommandArgs;
import filesystem.Directory;
import filesystem.File;
import filesystem.FileAlreadyExistsException;
import filesystem.FileSystem;
import filesystem.InMemoryFileSystem;
import org.junit.Before;
import org.junit.Test;
import utilities.Command;
import utilities.CommandManager;
import utilities.ExitCode;

public class CmdTreeTest {

  // Create Testing Consoles, a command manager instance, an instance of the
  // mock file system and an instance of the command
  private TestingConsole testOut;
  private TestingConsole testErrOut;
  private FileSystem fs;
  private CommandManager cm;
  private Command cmd;

  @Before
  // Resets the file system for each test case
  public void reset() throws FileAlreadyExistsException {
    testOut = new TestingConsole();
    testErrOut = new TestingConsole();
    fs = new InMemoryFileSystem();
    cm = CommandManager.constructCommandManager(testOut, testErrOut, fs);
    cmd = new CmdTree(fs, cm);

    Directory root = fs.getRoot();
    Directory dir1 = root.createAndAddNewDir("dir1");
    Directory dir2 = root.createAndAddNewDir("dir2");
    File file1 = new File("file1", "file1's contents\n");
    root.addFile(file1);
    File file2 = new File("file2", "file2's contents\n");
    root.addFile(file2);
    File file3 = new File("file3", "file3's contents, dir2\n");
    dir2.addFile(file3);
  }

  @Test
  public void testTree() {
    CommandArgs args = new CommandArgs("tree");

    TestingConsole tc = new TestingConsole();
    TestingConsole tc_err = new TestingConsole();
    ExitCode exitVal = cmd.execute(args, tc, tc_err);

    assertEquals("/\n\tfile2\n\tfile1\n\tdir2\n\t\tfile3\n\tdir1\n\n",
                 tc.getAllWritesAsString());
    assertEquals(exitVal, ExitCode.SUCCESS);
  }
}