package com.customfile;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.nio.charset.StandardCharsets;


/**
 * Custom file type class.
 */
public final class CustomFileType implements FileType {
    /**
     * Creating a new custom file type called INSTANCE. TODO:explain better if possible
     */
    public static final CustomFileType INSTANCE = new CustomFileType();

    /**
     * Getter for the view name.
     * @return Name of the view.
     */
    @NotNull
    @Override
    public String getName() {
        return "My Custom View";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "A custom editor view for IntelliJ IDEA";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "custom";
    }

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @NotNull
    @Override
    public String getCharset(@NotNull VirtualFile virtualFile, byte @NotNull [] bytes) {
        return StandardCharsets.UTF_8.name();
    }
}
