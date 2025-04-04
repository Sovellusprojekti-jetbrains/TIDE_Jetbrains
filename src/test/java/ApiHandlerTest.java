import static org.mockito.Mockito.*;

import com.api.ApiHandler;
import com.api.TideCommandExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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

}
