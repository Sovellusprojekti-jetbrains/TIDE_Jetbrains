package com.factories;

import com.customfile.CustomFile;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.views.MyCustomFileEditor;
import org.jetbrains.annotations.NotNull;

public class MyCustomFileEditorProvider implements FileEditorProvider {
    @Override
    public boolean accept(@NotNull final Project project, @NotNull final VirtualFile file) {
        return file instanceof CustomFile;
    }

    @NotNull
    @Override
    public FileEditor createEditor(@NotNull final Project project, @NotNull final VirtualFile file) {
        return new MyCustomFileEditor(file);
    }

    @NotNull
    @Override
    public String getEditorTypeId() {
        return "my-custom-editor";
    }

    @NotNull
    @Override
    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
    }
}
