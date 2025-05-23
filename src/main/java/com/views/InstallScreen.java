package com.views;

import com.intellij.openapi.ui.Messages;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


public class InstallScreen {
    private JButton installAutomaticallyButton;
    private JButton installManuallyButton;
    private JPanel installPane;
    private JPanel jpanelText;
    private JLabel jpanelLabel;
    private Boolean visible = false;
    private static JFrame frame = null;


    /**
     * Handles the actions concerning the installation of the Tide-CLI.
     */
    public InstallScreen() {
        jpanelLabel.setText("<html> Tide-Cli was not detected in the path. Would you like to install it?" +
                "alternatively you can set location of Tide-Cli in the setting <html\">");

        installAutomaticallyButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                installAutomaticallyButton.setEnabled(false);
                if (System.getProperty("os.name").contains("Windows")) {
                    //TODO: Maybe let the user decide where to install?
                    //TODO: Would require an install wizard.
                    String username = System.getProperty("user.name");
                    String path = "c:/Users/" + username + "/tide";
                    new File(path).mkdirs();
                    ProcessBuilder process = new ProcessBuilder();
                    process.directory(new File(path));
                    InputStream inputStream = getClass().getResourceAsStream("/scripts/installTideCLI.ps1");

                    try {
                        Path tempScript = Files.createTempFile("temp-script", ".ps1");
                        Files.copy(inputStream, tempScript, StandardCopyOption.REPLACE_EXISTING);
                        tempScript.toFile().deleteOnExit();
                        process.command(
                                "powershell.exe", "-ExecutionPolicy", "Bypass", "-File", tempScript.toString()
                        );
                        process.redirectErrorStream(true);
                        Process pb = process.start();
                        int exitCode = pb.waitFor();

                        if (exitCode == 0) {
                            Messages.showMessageDialog(
                                    "Tide installed successfully to your home folder. Please restart your IDE",
                                    "Installation Complete",
                                    Messages.getInformationIcon()
                            );
                        } else {
                            Messages.showMessageDialog(
                                    "Something went wrong with the install",
                                    "Installation Failed",
                                    Messages.getErrorIcon()
                            );
                        }
                    } catch (IOException | InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    return;
                }
                if (System.getProperty("os.name").contains("Linux")) {
                    //Automatic install not possible on Linux
                    //Would require too many dependencies to be installed
                    //technically possible if user has Python, Poetry and Pyenv
                    Messages.showMessageDialog(
                            "Automatic installation is not possible in Linux, please install Tide-CLI manually",
                            "No Linux Support For Installation",
                            Messages.getInformationIcon()
                    );
                    return;
                }
                if (System.getProperty("os.name").contains("Mac OS X")) {
                    //TODO: test this on a mac to make sure it works

                    try {
                        ProcessBuilder process = new ProcessBuilder();
                        process.command("curl -kLSs "
                                + "https://github.com/TIDE-project/TIDE-CLI/releases/latest/download/tide-macos-latest.zip"
                                + " -o tide.zip");
                        process.start();
                        process.command("unzip tide.zip");
                        process.start();
                        process.command("chmod +x tide");
                        process.start();
                        process.command("mv tide /usr/local/bin");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    return;
                }
            }
        });
        installManuallyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        Desktop.getDesktop().browse(
                                new URI("https://tim.jyu.fi/view/kurssit/tie/proj/2024/tide/dokumentit/"
                                        + "kayttoohjeet/tide-komentorivityokalun-asennusohjeet"));
                    } catch (IOException | URISyntaxException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        });


    }

    /**
     * returns the Jpanel that is made.
     * @return Jpanel that is made
     */
    public JPanel getContent() {
        return this.installPane;
    }
}
