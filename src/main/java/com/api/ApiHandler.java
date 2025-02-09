package com.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import java.io.File;
/**
 * Tänne tide cli kutsut
 */
public class ApiHandler {

    /**
     * uses the tide-cli to log in the user
     */
    public void login() {
        String command = "tide login";
        try {
            ProcessBuilder pb = new ProcessBuilder(command.split("\\s+"));
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);


        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * uses the tide-cli to log out the user
     */

    public void logout() {
        String command = "tide logout";
        try {
            ProcessBuilder pb = new ProcessBuilder(command.split("\\s+"));
            pb.redirectErrorStream(true);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);


        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * asks tide-cli if there is a login and returns a boolean
     * @return login status in boolean
     */
    public boolean  isLoggedIn() {
        try{

            //TODO: permanent solution to the tide location, possibly permanent state or something similar
            //String command "tide check-login --json"
            String pythonPath = "C:/koodi/ties405/project/CLI/TIDE-CLI/.venv/Scripts/python.exe";
            ProcessBuilder pb = new ProcessBuilder(pythonPath,"C:/koodi/ties405/project/CLI/TIDE-CLI/src/tidecli/main.py","check-login","--json");
            //pb.directory(new File("C:/koodi/ties405/project/CLI/TIDE-CLI/src/tidecli"));
            pb.redirectErrorStream(true);

            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String jsonOutput = reader.lines().collect(Collectors.joining("\n"));
            System.out.println("Raw Output from Python: " + jsonOutput);
            // Parse JSON
            Gson gson = new Gson();
            LoginOutput output = gson.fromJson(jsonOutput, LoginOutput.class);
            if (output.logged_in != null) {return true;}

        } catch (IOException ex) {
            ex.printStackTrace();
        }


        return false;
    }
}

class LoginOutput {
     public String logged_in;
}