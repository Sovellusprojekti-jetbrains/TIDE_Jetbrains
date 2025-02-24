package com.actions;

import com.intellij.ide.util.PropertiesComponent;

public class MyPluginSettings {

    private static final String KEY = "myPlugin.path"; // Key used to store/retrieve the value



    /**
     *  Method to get the setting value.
     * @return returns the path that is saved in properties. Gives default value if nothing is saved.
     */
    public String getPath() {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        return properties.getValue(KEY, System.getProperty("user.dir")); // Default value if not set
    }

    /**
     * Method to set the path value.
     * @param value the path that is given
     */
    public void setPath(String value) {
        PropertiesComponent properties = PropertiesComponent.getInstance();
        properties.setValue(KEY, value);
    }
}
