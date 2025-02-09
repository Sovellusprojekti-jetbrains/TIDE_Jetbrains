package com.customfile;

import com.intellij.testFramework.LightVirtualFile;

/**
 * Funky custom file thing that needs to be explained.
 */
public class CustomFile extends LightVirtualFile {
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
