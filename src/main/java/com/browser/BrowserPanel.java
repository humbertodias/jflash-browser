package com.browser;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.events.*;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class BrowserPanel extends JPanel {

  private final JLabel statusBar;
  private final Browser browser;
  private final JTextField address;

  public BrowserPanel(Browser browser) {
    this.browser = browser;

    setLayout(new BorderLayout());

    address = new JTextField();
    address.addActionListener(
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            browser.loadURL(address.getText());
          }
        });

    BrowserView view = new BrowserView(browser);

    statusBar = new JLabel("Press ENTER to browse");

    add(address, BorderLayout.NORTH);
    add(view, BorderLayout.CENTER);
    add(statusBar, BorderLayout.SOUTH);

    eventsToStatusBar();
  }

  private void eventsToStatusBar() {
    browser.addLoadListener(
        new LoadAdapter() {
          @Override
          public void onStartLoadingFrame(StartLoadingEvent event) {
            if (event.isMainFrame()) {
              statusBar.setText("Main frame has started loading");
            }
          }

          @Override
          public void onProvisionalLoadingFrame(ProvisionalLoadingEvent event) {
            if (event.isMainFrame()) {
              statusBar.setText("Provisional load was committed for a frame");
            }
          }

          @Override
          public void onFinishLoadingFrame(FinishLoadingEvent event) {
            if (event.isMainFrame()) {
              System.out.println("Main frame has finished loading");
            }
          }

          @Override
          public void onFailLoadingFrame(FailLoadingEvent event) {
            NetError errorCode = event.getErrorCode();
            if (event.isMainFrame()) {
              statusBar.setText("Main frame has failed loading: " + errorCode);
            }
          }

          @Override
          public void onDocumentLoadedInFrame(FrameLoadEvent event) {
            statusBar.setText("Frame document is loaded.");
          }

          @Override
          public void onDocumentLoadedInMainFrame(LoadEvent event) {
            statusBar.setText("Main frame document is loaded.");
          }
        });
  }

  public JTextField getAddress() {
    return address;
  }
}
