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
        final int sleeptime = 120;
        final int searchtime = 20;
        WelcomeFrameFixture welcomeFrame = remoteRobot.find(WelcomeFrameFixture.class);
        welcomeFrame.createNewProjectLink().click();
        final Locator createButton = byXpath("//div[@text='Create']");
        final ComponentFixture createFixture = remoteRobot.find(ComponentFixture.class, createButton, ofSeconds(searchtime));
        final Locator sampleLocator = byXpath("//div[@text='Add sample code']");
        final ComponentFixture sampleComponent = remoteRobot.find(ComponentFixture.class, sampleLocator,ofSeconds(searchtime));
        sampleComponent.click();
        createFixture.click();
        final IdeaFrame idea = remoteRobot.find(IdeaFrame.class, ofSeconds(searchtime));
        TimeUnit.SECONDS.sleep(searchtime);
        while (idea.isDumbMode()) {
            TimeUnit.SECONDS.sleep(sleeptime);
        }
        final Locator tideButtonLocator = byXpath("//div[@tooltiptext='TIDE Tool Window']");
        final ComponentFixture tideButton = remoteRobot.find(ComponentFixture.class, tideButtonLocator,ofSeconds(searchtime));
        tideButton.click();
        /* Uncomment when you can bypass or automate logging into tim through browser.
        final Locator courseButtonLocator = byXpath("//div[@tooltiptext='Course Task']");
        final ComponentFixture courseButton = remoteRobot.find(ComponentFixture.class, courseButtonLocator);
        while (idea.isDumbMode()) {
            TimeUnit.SECONDS.sleep(sleeptime);
        }
        courseButton.click();
        while (idea.isDumbMode()) {
            TimeUnit.SECONDS.sleep(sleeptime);
        }
         */
        // Remote robot locators that are needed to find the components from the xpath file.
        while (idea.isDumbMode()) {
            TimeUnit.SECONDS.sleep(sleeptime);
        }
        final Locator tideLocator = byXpath("//div[@class='ToolWindowPane']");
        final Locator settingsLocator = byXpath("//div[@text='Settings']");

        //The components that are used for the tests.
        final ComponentFixture tide = remoteRobot.find(ComponentFixture.class, tideLocator,ofSeconds(searchtime));

        final ComponentFixture settings = remoteRobot.find(ComponentFixture.class, settingsLocator,ofSeconds(searchtime));


        assertTrue(tide.hasText("TIDE Tool Window"), "TIDE Tool Window has wrong text or doesn't render");
        assertTrue(tide.hasText("Courses"), "TIDE Tool Windows Courses tab has wrong text or doesn't render");
        assertTrue(tide.hasText("Login"), "Login button has wrong text or doesn't render");
        assertTrue(tide.hasText("Settings"), "Settings button has wrong text or doesn't render");
        /* Uncomment when you can bypass or automate logging into tim trough browser.
        assertTrue(tide.hasText("Course Task"), "Course Task Window has wrong text or doesn't render");
        assertTrue(tide.hasText("Course View"), "Course Task Windows Course View tab has wrong text or doesn't render");
        assertTrue(tide.hasText("Open exercise"), "Open exercise button has wrong text or doesn't render");
        assertTrue(tide.hasText("Submit"),  "Submit button has wrong text or doesn't render");
        assertTrue(tide.hasText("Show output"), "Show output button has wrong text or doesn't render");
        assertTrue(tide.hasText("Reset exercise"),  "Reset exercise button has wrong text or doesn't render");
         */
        settings.click();
        TimeUnit.SECONDS.sleep(sleeptime);
        //final Locator settingsFrame = byXpath("//div[@class='LoadingDecoratorLayeredPane']");
        final Locator settingsLabel = byXpath("//div[@text='Task download folder:']");
        final Locator browseLabel = byXpath("//div[@tooltiptext='Use GUI to select download folder']");
      //  final ComponentFixture settingsWindow = remoteRobot.find(ComponentFixture.class, settingsFrame);
        final ComponentFixture settingsText = remoteRobot.find(ComponentFixture.class, settingsLabel);
        final ComponentFixture browseText = remoteRobot.find(ComponentFixture.class, browseLabel);
        assertTrue(settingsText.hasText("Task download folder:"), "Error inside settings window, should say Task download folder");
        assertTrue(browseText.hasText("Browse"), "Error inside settings window, button should have text Browse");
    }

}
