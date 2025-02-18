package com.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import org.jetbrains.annotations.NotNull;

public class ResetExercise extends AnAction {
    /**
     * Placeholder function for reset exercise menu option.
     * TODO: Call a method to infer relative path and reset the exercise
     * @param e AnActionEvent resulting from user interaction
     */
    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
        String path = FileEditorManager
                .getInstance(e.getProject())
                .getSelectedEditor()
                .getFile()
                .getPath();
        // annetaan jollekin tehtäväksi selvittää
        // relatiivinen polku ja nollata tehtävä
        System.out.println(path);
    }
}
