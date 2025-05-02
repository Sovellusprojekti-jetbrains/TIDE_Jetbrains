import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.api.ApiHandler;
import com.api.TideCommandExecutor;
import com.course.SubTask;
import com.intellij.openapi.vfs.VirtualFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiHandlerTest {
    private ApiHandler apiHandler;
    private TideCommandExecutor tideCommandExecutorMock;

    /**
     * Sets up the needed mocks for testing.
     */
    @BeforeEach
    public void setUp() {
        // Mock the TideCommandExecutor singleton
        tideCommandExecutorMock = mock(TideCommandExecutor.class);
        apiHandler = new ApiHandler();
        apiHandler.setTideCommandExecutor(tideCommandExecutorMock); // If this is a static field, we need to use a setter or reflection
    }

    /**
     * Tests the login function by checking if the Command Executor was called.
     */
    @Test
    public void testLogin() {
        // Call the login method
        apiHandler.login();

        // Verify that TideCommandExecutor.INSTANCE.login() was called once
        verify(tideCommandExecutorMock, times(1)).login();
    }

    /**
     * Tests the logout function by checking if the Command Executor was called.
     */
    @Test
    public void testLogout() {
        apiHandler.logout();
        verify(tideCommandExecutorMock, times(1)).logout();
    }

    /**
     * Tests the courses function by checking if the Command Executor was called.
     */
    @Test
    public void testCourses() {
        apiHandler.courses();
        verify(tideCommandExecutorMock, times(1)).fetchCoursesAsync();
    }

    /**
     * Tests the exercise load function by checking if the Command Executor was called.
     */
    @Test
    public void testLoadExercise() throws IOException, InterruptedException {
        String courseDirectory = "courseDir";
        String[] cmdArgs = {"arg1", "arg2"};

        apiHandler.loadExercise(courseDirectory, cmdArgs);

        verify(tideCommandExecutorMock, times(1)).loadExercise(courseDirectory, cmdArgs);
    }

    /**
     * Tests the exercise reset function by checking if the Command Executor was called.
     */
    @Test
    public void testResetExercise() throws IOException, InterruptedException {
        SubTask task = new SubTask();
        String courseDirectory = "courseDir";

        apiHandler.resetSubTask(task, courseDirectory);

        verify(tideCommandExecutorMock, times(1)).resetSubTask(task, courseDirectory);
    }

    /**
     * Tests the exercise submit function by checking if the Command Executor was called.
     */
    @Test
    public void testSubmitExercise() {
        VirtualFile file = null;

        apiHandler.submitExercise(file);

        verify(tideCommandExecutorMock, times(1)).submitExercise(file);
    }

    /**
     * Test the handleCommandLine function and if the tide-cli works or not.
     * TODO: Remove disabled annotation during local testing.
     */
    @Test
    public void testHandleCommandLine() throws IOException, InterruptedException {
        List<String> command = new ArrayList<>();
        command.add("tide");
        String result = TideCommandExecutor.INSTANCE.handleCommandLineTest(command);
        String expect = "Usage: tide [OPTIONS] COMMAND [ARGS]...\n"
                + "\n"
                + "  CLI tool for downloading and submitting TIM tasks.\n"
                + "\n"
                + "Options:\n"
                + "  --help  Show this message and exit.\n"
                + "\n"
                + "Commands:\n"
                + "  check-login  Check login status, prints the username when logged in.\n"
                + "  courses      List all courses.\n"
                + "  login        Log in the user and saves the token to the keyring.\n"
                + "  logout       Log out the user and deletes the token from the keyring.\n"
                + "  submit       Enter the path of a task folder or a file to submit the...\n"
                + "  task         Task related commands.\n";
        assertEquals(expect, result, "Should return tide help message");
    }

}
