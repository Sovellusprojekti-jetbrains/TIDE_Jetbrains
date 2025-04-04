import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.api.ApiHandler;
import com.api.TideCommandExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

public class ApiHandlerTest {
    private ApiHandler apiHandler;
    private TideCommandExecutor tideCommandExecutorMock;

    @BeforeEach
    public void setUp() {
        // Mock the TideCommandExecutor singleton
        tideCommandExecutorMock = mock(TideCommandExecutor.class);
        apiHandler = new ApiHandler();
        apiHandler.setTideCommandExecutor(tideCommandExecutorMock); // If this is a static field, we need to use a setter or reflection
    }

    @Test
    public void testLogin() {
        // Call the login method
        apiHandler.login();

        // Verify that TideCommandExecutor.INSTANCE.login() was called once
        verify(tideCommandExecutorMock, times(1)).login();
    }

    @Test
    public void testLogout() {
        apiHandler.logout();
        verify(tideCommandExecutorMock, times(1)).logout();
    }

    @Test
    public void testCourses() {
        apiHandler.courses();
        verify(tideCommandExecutorMock, times(1)).fetchCoursesAsync();
    }

    @Test
    public void testLoadExercise() throws IOException, InterruptedException {
        String courseDirectory = "courseDir";
        String[] cmdArgs = {"arg1", "arg2"};

        apiHandler.loadExercise(courseDirectory, cmdArgs);

        verify(tideCommandExecutorMock, times(1)).loadExercise(courseDirectory, cmdArgs);
    }

}
