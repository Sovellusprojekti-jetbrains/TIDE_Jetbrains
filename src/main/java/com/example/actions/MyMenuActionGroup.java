package com.example.actions;

import com.intellij.openapi.actionSystem.*;
import org.jetbrains.annotations.NotNull;

public class MyMenuActionGroup extends DefaultActionGroup {

    public MyMenuActionGroup() {
        add(new MyFirstAction());

    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;  // EDT (Event Dispatch Thread) is a safe choice for UI updates
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        // You can add custom logic here to enable/disable actions dynamically
    }
}
