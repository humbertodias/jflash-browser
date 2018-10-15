package com.browser;

import com.teamdev.jxbrowser.chromium.LoggerProvider;
import com.teamdev.jxbrowser.chromium.ResourceParams;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.io.FileUtils;

public class HttpPanel extends JPanel {
  private final File downloadFolder;
  private JButton openDownloadFolder = new JButton("0");
  private JTextField filterExtension = new JTextField("swf,dcr,js,png,mp3,wav");
  private final JTable table;
  private DefaultTableModel model = new DefaultTableModel();
  private final DefaultTableCellRenderer renderer;
  private int rowCounter = 1;
  private int rowCounterDownloaded = 1;

  public HttpPanel() {

    setLayout(new BorderLayout());

    renderer = new DefaultTableCellRenderer();
    model.addColumn("#");
    model.addColumn("Downloaded");
    model.addColumn("Method");
    model.addColumn("Type");
    model.addColumn("Protocol");
    model.addColumn("Host");
    model.addColumn("Path");
    model.addColumn("Parameters");

    JPanel northPanel = new JPanel();
    northPanel.setLayout(new BorderLayout());
    northPanel.add(filterExtension, BorderLayout.CENTER);
    northPanel.add(openDownloadFolder, BorderLayout.EAST);

    add(northPanel, BorderLayout.NORTH);

    // Initializing the JTable
    table = new JTable(model);
    // adding it to JScrollPane
    JScrollPane sp = new JScrollPane(table);
    add(sp);

    addListener();

    openDownloadFolder.setToolTipText("Open download folder");
    filterExtension.setToolTipText("Extension to download, separated by comma");
    downloadFolder = new File("download");
    downloadFolder.mkdir();
  }

  private void addListener() {
    openDownloadFolder.addActionListener(
        e -> {
          try {
            Desktop.getDesktop().open(downloadFolder);
          } catch (IOException ex) {
            JOptionPane.showMessageDialog(table, ex.getMessage());
          }
        });
  }

  public void addRow(ResourceParams resourceParam) {
    boolean hasToDownload = hasToDownload(resourceParam);
    addRowOnTable(resourceParam, hasToDownload);
    if (hasToDownload) download(resourceParam);
  }

  private void addRowOnTable(ResourceParams resourceParam, boolean hasToDownload) {
    URL url = Util.toURL(resourceParam.getURL());
    Object[] row = {
      String.format("%d", rowCounter++),
      hasToDownload,
      resourceParam.getMethod(),
      resourceParam.getResourceType().name(),
      url.getProtocol(),
      url.getHost(),
      url.getPath(),
      url.getQuery()
    };
    model.addRow(row);
  }

  public java.util.List<String> getFilterExtensions() {
    String[] extensions = ("" + filterExtension.getText()).split(",");
    return Arrays.asList(extensions);
  }

  private boolean hasToDownload(ResourceParams params) {
    List<String> extensions = getFilterExtensions();
    URL url = Util.toURL(params.getURL());

    String resourceName = Util.getFileName(url.getFile());
    String resourceExtension = Util.getFileExtension(resourceName);
    return extensions.contains(resourceExtension);
  }

  private void download(ResourceParams params) {
    openDownloadFolder.setText(String.format("%d", rowCounterDownloaded++));

    URL url = Util.toURL(params.getURL());

    String resourceName = Util.getFileName(url.getFile());
    String resourceExtension = Util.getFileExtension(resourceName);

    File outDir = new File(downloadFolder, resourceExtension);

    if (!outDir.exists()) outDir.mkdir();
    File downloadFile = new File(outDir, resourceName);

    Runnable download =
        () -> {
          try {
            FileUtils.copyURLToFile(url, downloadFile);
          } catch (IOException e) {
            LoggerProvider.getBrowserLogger().severe(e.getMessage());
          }
        };
    new Thread(download).start();
  }
}
