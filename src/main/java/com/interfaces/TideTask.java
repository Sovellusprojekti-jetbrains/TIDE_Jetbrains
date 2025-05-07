package com.interfaces;

import com.intellij.openapi.project.Project;

/**
 * This interface defines services that can be asked from an individual tide task.
 */
public interface TideTask {
    /**
     * This method is used to submit an individual tide task to TIM.
     * @param project Project is needed to open output window in the IDE.
     */
    void submit(Project project);

    /**
     * This method is used to revert an individual tide task back to the state of latest submit.
     */
    void resetExercise();

    /**
     * This method is used to open TIM-page of an individual tide task.
     * @param baseURL URL to TIM-server.
     * @param project Project is needed to open browser as file in it, depending on user setting.
     */
    void openInBrowser(String baseURL, Project project);
}
