package com.customfile;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

public final class HtmlEditorProvider implements FileEditorProvider {
    private static final Logger LOG = Logger.getInstance(HtmlEditorProvider.class);
    private static String urlToLoad = "https://www.google.com"; // Default website

    /**
     * setter for the url that is loaded into the middle view.
     * @param url the url that is being loaded
     */
    public static void setUrl(String url) {
        urlToLoad = url;
    }

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        boolean isAccepted = file.getName().equals("website_view");
        LOG.info("HtmlEditorProvider accept() called for file: " + file.getName() + " | Accepted: " + isAccepted);
        return isAccepted;
    }

    @NotNull
    @Override
    public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        LOG.info("HtmlEditorProvider createEditor() called for file: " + file.getName());
        return new HtmlFileEditor(urlToLoad);
    }

    @NotNull
    @Override
    public String getEditorTypeId() {
        return "custom-html-editor";
    }

    @NotNull
    @Override
    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
    }
}
