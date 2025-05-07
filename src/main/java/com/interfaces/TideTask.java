package com.interfaces;

import com.intellij.openapi.project.Project;

public interface TideTask {
    /**
     * This method submits an individual tide task to TIM.
     * @param project Project is needed to open output window in the IDE.
     */
    void submit(Project project);

    /**
     * This method reverts an individual tide task back to the state of latest submit.
     */
    void resetExercise();

    /**
     * This method is used to open TIM-page of an individual tide task.
     * @param baseURL URL to TIM-server.
     * @param project Project is needed to open browser as file in it, depending on user setting.
     */
    void openInBrowser(String baseURL, Project project);
}
