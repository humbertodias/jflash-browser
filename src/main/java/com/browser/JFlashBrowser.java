package com.browser;

import com.teamdev.jxbrowser.chromium.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.*;
import javax.swing.*;

/**
 * This sample loads a web page with simple Flash content.
 * https://get.adobe.com/flashplayer/otherversions/
 */
public class JFlashBrowser {
  private static HttpPanel httpPanel;
  private static WebSocketPanel webSocketPanel;

  /**
   * Main entry point
   *
   * @param args
   */
  public static void main(String... args) throws IOException {

    System.setProperty("teamdev.license.info", "true");

    BrowserPreferences.setChromiumSwitches(Util.getPluginConfiguration());

    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);

    Browser browser = new Browser();
    BrowserPanel browserPanel = new BrowserPanel(browser);
    httpPanel = new HttpPanel();
    webSocketPanel = new WebSocketPanel();

    tabbedPane.addTab("Browser", browserPanel);
    tabbedPane.addTab("Http", httpPanel);
    tabbedPane.addTab("WebSocket", webSocketPanel);

    JFrame frame = new JFrame("JFlashBrowser");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    tabbedPane.setSelectedIndex(0);
    frame.add(tabbedPane);
    frame.setSize(1024, 768);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

    writeLog("log");
    writeConsoleLog(browser);
    interceptFilesByExtension(browser);

    browserPanel.getAddress().setText(Util.INITIAL_PAGE);
    browserPanel.getAddress().requestFocus();
    browser.loadURL(browserPanel.getAddress().getText());
  }

  private static void writeLog(String logFolderName) throws IOException {
    File logFolder = new File(logFolderName);
    if (!logFolder.exists()) {
      logFolder.mkdir();
    }

    LoggerProvider.setLevel(Level.ALL);

    // Redirect Browser log messages to jxbrowser-browser.log
    redirectLogMessagesToFile(
        LoggerProvider.getBrowserLogger(), logFolderName + "/jxbrowser-browser.log");

    // Redirect IPC log messages to jxbrowser-ipc.log
    redirectLogMessagesToFile(LoggerProvider.getIPCLogger(), logFolderName + "/jxbrowser-ipc.log");

    // Redirect Chromium Process log messages to jxbrowser-chromium.log
    redirectLogMessagesToFile(
        LoggerProvider.getChromiumProcessLogger(), logFolderName + "/jxbrowser-chromium.log");
  }

  private static void writeConsoleLog(Browser browser) {
    browser.addConsoleListener(
        event ->
            LoggerProvider.getBrowserLogger().fine(event.getLevel() + " - " + event.getMessage()));
  }

  private static void redirectLogMessagesToFile(Logger logger, String logFilePath)
      throws IOException {
    FileHandler fileHandler = new FileHandler(logFilePath);
    fileHandler.setFormatter(new SimpleFormatter());

    // Remove default handlers including console handler
    for (Handler handler : logger.getHandlers()) {
      logger.removeHandler(handler);
    }
    logger.addHandler(fileHandler);
  }

  private static void interceptFilesByExtension(Browser browser) {

    NetworkService networkService = browser.getContext().getNetworkService();
    networkService.setResourceHandler(
        params -> {
          httpPanel.addRow(params);
          return true;
        });
  }
}
