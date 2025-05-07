package com.interfaces;

/**
 * This interface defines services for user's account management.
 */
public interface UserManagement {
    /**
     * This method is used to log user in into the TIM-system.
     * TDE-CLI gets rights to the users account.
     */
    void login();

    /**
     * This method is used to log user out of the TIM-system.
     * Rights to the users account are removed from TIDE-CLI.
     */
    void logout();

    /**
     *This method is used to access the list of user's courses in TIM-system.
     */
    void courses();

    /**
     * This method is used to download course tasks from user's TIM-courses.
     * Downloaded tasks are appended into user metadata (.timdata file).
     * @param courseDirectory Directory of the course task in TIM-system.
     * @param cmdArgs Variable amount of arguments needed to use services offered by TIDE-CLI.
     */
    void loadExercise(String courseDirectory, String... cmdArgs);
}
