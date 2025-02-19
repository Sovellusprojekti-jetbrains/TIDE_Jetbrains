import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.ComponentFixture;
import com.intellij.remoterobot.search.locators.Locator;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the UI, such as text contents of toolwindows and their components.
 */
public class UiTest {
    RemoteRobot remoteRobot = new RemoteRobot("http://127.0.0.1:8082");

    Locator tideLocator = byXpath("//div[@class='TabPanel'][.//div[@text='Courses']]");
    Locator courseTaskLocator = byXpath("//div[@class='TabPanel'][.//div[@text='Course Task']]");
    Locator loginLocator = byXpath("//div[@text='Login']");
    Locator settingsLocator = byXpath("//div[@text='Settings']");
    Locator openButtonLocator = byXpath("//div[@text='Open exercise']");
    Locator submitButtonLocator = byXpath("//div[@text='Submit']");
    Locator outputButtonLocator = byXpath("//div[@text='Show output']");
    Locator resetButtonLocator = byXpath("//div[@text='Reset exercise']");

    ComponentFixture tide = remoteRobot.find(ComponentFixture.class, tideLocator);
    ComponentFixture courses = remoteRobot.find(ComponentFixture.class, courseTaskLocator);
    ComponentFixture login = remoteRobot.find(ComponentFixture.class,loginLocator);
    ComponentFixture settings = remoteRobot.find(ComponentFixture.class,settingsLocator);
    ComponentFixture openButton = remoteRobot.find(ComponentFixture.class,openButtonLocator);
    ComponentFixture submitButton = remoteRobot.find(ComponentFixture.class,submitButtonLocator);
    ComponentFixture outputButton = remoteRobot.find(ComponentFixture.class,outputButtonLocator);
    ComponentFixture resetButton = remoteRobot.find(ComponentFixture.class,resetButtonLocator);




    @Test
    public void textTest() {
        assertTrue(tide.hasText("TIDE Tool Window"));
        assertTrue(tide.hasText("Courses"));
        assertTrue(login.hasText("Login"));
        assertTrue(settings.hasText("Settings"));

        assertTrue(courses.hasText("Course Task"));
        assertTrue(courses.hasText("Course View"));
        assertTrue(openButton.hasText("Open exercise"));
        assertTrue(submitButton.hasText("Submit"));
        assertTrue(outputButton.hasText("Show output"));
        assertTrue(resetButton.hasText("Reset exercise"));

    }

}

//div[@class='TabPanel'][.//div[@text='Courses']]
//div[@text='Login']
//div[@text='Settings']
//div[@text='Course View']
//div[@text='Open exercise']
//div[@text='Submit']
//div[@text='reset exercise']

