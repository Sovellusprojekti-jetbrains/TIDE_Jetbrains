package com.api;

import com.actions.Settings;
import com.course.DemoTask;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class TimDataHandler {
    /**
     * Reads the timdata file that is located in the path.
     * @param pathToFile Path in the settings where the timdata file is located after downloading a task.
     * @return timdata in string format, empy if file was not found
     */
    public String readTimData(String pathToFile) {
        StringBuilder sb = new StringBuilder();
        try {
            String settingsPath = pathToFile + File.separatorChar + ".timdata";
            Path path = Paths.get(settingsPath);
            BufferedReader reader = Files.newBufferedReader(path);
            String line = reader.readLine();
            while (line != null) {
                // read next line
                sb.append(line).append(System.lineSeparator());
                line = reader.readLine();
            }
        } catch (IOException e) {
            com.api.LogHandler.logError("CourseMainPane.readTimData(String pathToFile), lines: 405-413", e);
            com.api.LogHandler.logDebug(new String[]{"402 String pathToFile"}, new String[]{pathToFile});
            System.out.println("File timdata was not found");
        }
        return sb.toString();
    }

    /**
     * method to read demo task data from a timdatafile from under the timdatafile.
     * @param file the file used to find the right timdata file.
     * @return list of the demo tasks that can be found in the timdata file.
     */
    private List<DemoTask> getTimDataDemoTasks(VirtualFile file) {
        TimDataHandler tim = new TimDataHandler();
        JsonHandler json = new JsonHandler();
        VirtualFile parentFile = file.getParent();
        String data = "";
        while (data.isEmpty() && !Objects.equals(parentFile.getCanonicalPath(), Settings.getPath())) {
            data = tim.readTimData(parentFile.getCanonicalPath());
            parentFile = parentFile.getParent();
        }
        return json.jsonToDemotask(data);
    }
}
