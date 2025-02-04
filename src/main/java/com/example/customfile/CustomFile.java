package com.example.customfile;

import com.intellij.testFramework.LightVirtualFile;


public class CustomFile extends LightVirtualFile {
    public static final String FILE_TYPE = "my-custom-view";

    public CustomFile() {
        super("My Custom View", CustomFileType.INSTANCE, "");
    }

    @Override
    public boolean isWritable() {
        return false;
    }

}
