package com.state;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.*;
import com.util.Config;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements persistent state component and provides a service for getting and setting user defined plugin settings.
 */
@Service
@State(
        name = "StateManager",
        storages = @Storage("MySettings.xml")
)
public final class StateManager implements PersistentStateComponent<StateManager.State> {

    /**
     * Separate state class to hold the user defined settings.
     */
    public static class State {

    }

    private State myState = new State(); //Object reference to state class
    private static final int DEFAULTSCROLLSPEED = Config.DEFAULT_SCROLL_SPEED;
    private static final int MAXSCROLLSPEED = Config.MAX_SCROLL_SPEED;
    private static final boolean DEFAULTBROWSERCHOICE = Config.OPEN_IN_BROWSER;

    /**
     * This method is called when updating state class fields and to save the state of the State class when IDE is closed.
     * @return Object reference to State class
     */
    @Override
    public State getState() {
        return myState;
    }

    /**
     * This method is called when IDE is opened to load previous component state.
     * @param state loaded component state
     */
    @Override
    public void loadState(@NotNull State state) {
        myState = state;
    }

    /**
     * Sets new value to path field in State class.
     * @param path File path as a String
     */
    public void setPath(String path) {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        properties.setValue("myPlugin.path", path);
    }

    /**
     * Gets the path fields value from State class.
     * @return File ath as a String
     */
    public String getPath() {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        return properties.getValue("myPlugin.path", System.getProperty("user.dir"));
    }

    /**
     * adds a new value to the list of submitted tasks.
     *
     * @param taskPath path to the task that has been submitted.
     * @param points points given for the submitted subtask
     */
    public void setSubmit(String taskPath, Float points) {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        List<String> submits = getSubmits();
        if (submits == null)  {
            submits = new ArrayList<>();
        }
        if (!submits.contains(taskPath)) {
            List<String> copy = new ArrayList<>(submits);
            copy.add(taskPath);
            properties.setList("myPlugin.submits", copy);
        }
        properties.setValue(taskPath, points, 0.0F);
    }

    /**
     * Gets the path fields value from State class.
     * @return File ath as a String
     */
    public List<String> getSubmits() {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        return properties.getList("myPlugin.submits");
    }

    /**
     * get points of the sumbitted task.
     * @param path the path of the submitted exercise
     * @return points given for the submission
     */
    public Float getPoints(String path) {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        return properties.getFloat(path, 0.0F);
    }


    /**
     * Set scroll speed value used in CourseMainPane.
     * @param newScrollSpeed New scroll speed value
     */
    public void setScrollSpeed(int newScrollSpeed) {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        properties.setValue("myPlugin.scrollSpeed", newScrollSpeed, DEFAULTSCROLLSPEED);
        ActiveState.getInstance().signalScrollSpeedUpdate();
    }


    /**
     * Get scroll speed used in CourseMainPane from settings.
     * @return Scroll speed value
     */
    public int getScrollSpeed() {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        return properties.getInt("myPlugin.scrollSpeed", DEFAULTSCROLLSPEED);
    }


    /**
     * Get max scroll speed.
     * @return The MAXSCROLLSPEED attribute
     */
    public int getMaxScrollSpeed() {
        return MAXSCROLLSPEED;
    }

    /**
     * Set whether to open the TIM view in browser (true) or IDE (false).
     * @param choice Choice to set
     */
    public void setBrowserChoice(boolean choice) {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        properties.setValue("myPlugin.browserChoice", choice);
    }

    /**
     * @return Whether to open the TIM view in browser (true) or IDE (false)
     */
    public boolean getBrowserChoice() {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        return properties.getBoolean("myPlugin.browserChoice", DEFAULTBROWSERCHOICE);
    }

    /**
     * sets the tide installation location.
     * @param path path to the tide folder
     */
    public void setTidePath(String path) {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        properties.setValue("myPlugin.tidepath", path);
    }

    /**
     * get the path to tide installation folder.
     * @return path to the tide installation folder
     */
    public String getTidePath() {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        return properties.getValue("myPlugin.tidepath", "");
    }

    /**
     * Gets the last saved courses as a Json String
     * @return Json String of the saved courses
     */
    public String getCourses() {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        return properties.getValue("myPlugin.courses", "");
    }


    /**
     * saves the Json String containing the courses
     * @param JsonString String containing the courses in Json format
     */
    public void SetCourse(String JsonString) {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        properties.setValue("myPlugin.courses", JsonString);
    }

}
