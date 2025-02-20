import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.fixtures.ComponentFixture;
import com.intellij.remoterobot.search.locators.Locator;
import org.junit.jupiter.api.Test;
import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the UI, such as text contents of toolwindows and their components.
 */
public class UiTest {
    //opens the remote robot instance into localhost at port 8082.
    //you can check xpath of the components by putting the url into a browser window after running ./gradlew clean runIdeForUiTests.
   private final RemoteRobot remoteRobot = new RemoteRobot("http://127.0.0.1:8082");
    // Remote robot locators that are needed to find the components from the xpath file.
    private final Locator tideLocator = byXpath("//div[@class='TabPanel'][.//div[@text='Courses']]");
    private final Locator courseTaskLocator = byXpath("//div[@class='TabPanel'][.//div[@text='Course Task']]");
    private final Locator loginLocator = byXpath("//div[@text='Login']");
    private final Locator settingsLocator = byXpath("//div[@text='Settings']");
    private final Locator openButtonLocator = byXpath("//div[@text='Open exercise']");
    private final Locator submitButtonLocator = byXpath("//div[@text='Submit']");
    private final Locator outputButtonLocator = byXpath("//div[@text='Show output']");
    private final  Locator resetButtonLocator = byXpath("//div[@text='Reset exercise']");
    //The components that are used for the tests.
    private final ComponentFixture tide = remoteRobot.find(ComponentFixture.class, tideLocator);
    private final ComponentFixture courses = remoteRobot.find(ComponentFixture.class, courseTaskLocator);
    private final ComponentFixture login = remoteRobot.find(ComponentFixture.class, loginLocator);
    private final ComponentFixture settings = remoteRobot.find(ComponentFixture.class, settingsLocator);
    private final ComponentFixture openButton = remoteRobot.find(ComponentFixture.class, openButtonLocator);
    private final ComponentFixture submitButton = remoteRobot.find(ComponentFixture.class, submitButtonLocator);
    private final ComponentFixture outputButton = remoteRobot.find(ComponentFixture.class, outputButtonLocator);
    private final ComponentFixture resetButton = remoteRobot.find(ComponentFixture.class, resetButtonLocator);


    /**
     * Tests the text contents of the tide and Courses task tool windows and the buttons.
     */
    @Test
    public void textTest() {
        assertTrue(tide.hasText("TIDE Tool Window"), "TIDE Tool Window has wrong text or doesn't render");
        assertTrue(tide.hasText("Courses"), "TIDE Tool Windows Courses tab has wrong text or doesn't render");
        assertTrue(login.hasText("Login"), "Login button has wrong text or doesn't render");
        assertTrue(settings.hasText("Settings"), "Settings button has wrong text or doesn't render");

        assertTrue(courses.hasText("Course Task"), "Course Task Window has wrong text or doesn't render");
        assertTrue(courses.hasText("Course View"), "Course Task Windows Course View tab has wrong text or doesn't render");
        assertTrue(openButton.hasText("Open exercise"), "Open exercise button has wrong text or doesn't render");
        assertTrue(submitButton.hasText("Submit"),  "Submit button has wrong text or doesn't render");
        assertTrue(outputButton.hasText("Show output"), "Show output button has wrong text or doesn't render");
        assertTrue(resetButton.hasText("Reset exercise"),  "Reset exercise button has wrong text or doesn't render");

    }

}

//div[@class='TabPanel'][.//div[@text='Courses']]
//div[@text='Login']
//div[@text='Settings']
//div[@text='Course View']
//div[@text='Open exercise']
//div[@text='Submit']
//div[@text='reset exercise']

