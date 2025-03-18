import com.intellij.remoterobot.RemoteRobot;
import com.intellij.remoterobot.data.RemoteComponent;
import com.intellij.remoterobot.fixtures.*;
import com.intellij.remoterobot.search.locators.Locator;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static com.intellij.remoterobot.search.locators.Locators.byXpath;
import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the UI, such as text contents of toolwindows and their components.
 */
public class UiTest {
    //opens the remote robot instance into localhost at port 8082.
    //you can check xpath of the components by putting the url into a browser window after running ./gradlew clean runIdeForUiTests.
   private final RemoteRobot remoteRobot = new RemoteRobot("http://127.0.0.1:8082");

    @DefaultXpath(by = "FlatWelcomeFrame type", xpath = "//div[@class='FlatWelcomeFrame']")
    @FixtureName(name = "Welcome Frame")
   public static class WelcomeFrameFixture extends ContainerFixture {
        /**
         * Self-made fixture class for the welcome frame.
         * @param robot the remoterobot
         * @param remoteComponent the remoteComponent
         */
        public WelcomeFrameFixture(@NotNull RemoteRobot robot, @NotNull RemoteComponent remoteComponent) {
            super(robot, remoteComponent);
        }

        // Create New Project

        /**
         * returns the create new project button.
         * @return the button to create a new project.
         */
        public ComponentFixture createNewProjectLink() {
            return find(ComponentFixture.class, byXpath("//div[@defaulticon='createNewProjectTab.svg']"));
        }
    }

    @FixtureName(name = "Idea frame")
    @DefaultXpath(by = "IdeFrameImpl type", xpath = "//div[@class='IdeFrameImpl']")
    public static class IdeaFrame extends CommonContainerFixture {
        /**
         * Self-made fixture class for the basic ide frame.
         * @param robot the remoterobot
         * @param remoteComponent the remoteComponent
         */
        public IdeaFrame(@NotNull RemoteRobot robot, @NotNull RemoteComponent remoteComponent) {
            super(robot, remoteComponent);
        }
        /**
         * checks if ide is in dumb mode.
         * @return true if ide is in dumb mode, false if not.
         */
        public boolean isDumbMode() {
            return callJs(
                    """
                    const frameHelper = com.intellij.openapi.wm.impl.ProjectFrameHelper.getFrameHelper(component)
                    if (frameHelper) {
                        const project = frameHelper.getProject()
                        project ? com.intellij.openapi.project.DumbService.isDumb(project) : true
                    } else { true }
                    """, true);
        }
    }
    /**
     * Tests the text contents of the tide and Courses task tool windows and the buttons.
     */
    @Test
    public void textTest() throws InterruptedException {
        final int sleeptime = 10;
        WelcomeFrameFixture welcomeFrame = remoteRobot.find(WelcomeFrameFixture.class);
        welcomeFrame.createNewProjectLink().click();
        final Locator createButton = byXpath("//div[@text='Create']");
        final ComponentFixture createFixture = remoteRobot.find(ComponentFixture.class, createButton);
        final Locator sampleLocator = byXpath("//div[@text='Add sample code']");
        final ComponentFixture sampleComponent = remoteRobot.find(ComponentFixture.class, sampleLocator);
        sampleComponent.click();
        createFixture.click();
        final IdeaFrame idea = remoteRobot.find(IdeaFrame.class, ofSeconds(10));
        while (idea.isDumbMode()) {
            TimeUnit.SECONDS.sleep(sleeptime);
        }
        final Locator tideButtonLocator = byXpath("//div[@tooltiptext='TIDE Tool Window']");
        final ComponentFixture tideButton = remoteRobot.find(ComponentFixture.class, tideButtonLocator);
        tideButton.click();
        final Locator courseButtonLocator = byXpath("//div[@tooltiptext='Course Task']");
        final ComponentFixture courseButton = remoteRobot.find(ComponentFixture.class, courseButtonLocator);
        while (idea.isDumbMode()) {
            TimeUnit.SECONDS.sleep(sleeptime);
        }
        courseButton.click();
        while (idea.isDumbMode()) {
            TimeUnit.SECONDS.sleep(sleeptime);
        }
        // Remote robot locators that are needed to find the components from the xpath file.

        final Locator tideLocator = byXpath("//div[@class='ToolWindowPane']");
        final Locator settingsLocator = byXpath("//div[@text='Settings']");

        //The components that are used for the tests.
        final ComponentFixture tide = remoteRobot.find(ComponentFixture.class, tideLocator);

        final ComponentFixture settings = remoteRobot.find(ComponentFixture.class, settingsLocator);


        assertTrue(tide.hasText("TIDE Tool Window"), "TIDE Tool Window has wrong text or doesn't render");
        assertTrue(tide.hasText("Courses"), "TIDE Tool Windows Courses tab has wrong text or doesn't render");
        assertTrue(tide.hasText("Login"), "Login button has wrong text or doesn't render");
        assertTrue(tide.hasText("Settings"), "Settings button has wrong text or doesn't render");

        assertTrue(tide.hasText("Course Task"), "Course Task Window has wrong text or doesn't render");
        assertTrue(tide.hasText("Course View"), "Course Task Windows Course View tab has wrong text or doesn't render");
        assertTrue(tide.hasText("Open exercise"), "Open exercise button has wrong text or doesn't render");
        assertTrue(tide.hasText("Submit"),  "Submit button has wrong text or doesn't render");
        assertTrue(tide.hasText("Show output"), "Show output button has wrong text or doesn't render");
        assertTrue(tide.hasText("Reset exercise"),  "Reset exercise button has wrong text or doesn't render");
        settings.click();
        final Locator settingsFrame = byXpath("//div[@class='JFrame']");
        final ComponentFixture settingsWindow = remoteRobot.find(ComponentFixture.class, settingsFrame);
        assertTrue(settingsWindow.hasText("Task download folder"), "Error inside settings window, should say Task download folder");
        assertTrue(settingsWindow.hasText("Browse"), "Error inside settings window,, button should have text Browse");
        assertTrue(settingsWindow.hasText("Save path"), "Error inside settings window, button should have text Save path");
        assertTrue(settingsWindow.hasText("Cancel"), "Error inside settings window, button should have text cancel");
    }

}
