package com.customfile;

import com.intellij.testFramework.LightVirtualFile;

/**
 * A custom file that inherits virtual file and is used by the virtual file system.
 * Virtual file system allows the usage of files no matter the location.
 * the information is kept in a virtual snapshot, where it is read when needed.
 */
public class CustomFile extends LightVirtualFile {
    /**
     * The type of the custom file TODO: explain a bit more.
     *
     */
    public static final String FILE_TYPE = "my-custom-view";

    /**
     * Custom file constructor.
     */
    public CustomFile() {
        super("My Custom View", CustomFileType.INSTANCE, "");
    }

    /**
     * File is not writable.
     * @return No.
     */
    @Override
    public boolean isWritable() {
        return false;
    }

}
