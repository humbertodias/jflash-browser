package com.browser;

import com.teamdev.jxbrowser.chromium.ResourceParams;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class HttpPanel extends JPanel {
  private final JTable table;
  DefaultTableModel model = new DefaultTableModel();

  public HttpPanel() {

    setLayout(new BorderLayout());

    model.addColumn("Method");
    model.addColumn("Type");
    model.addColumn("URL");

    // Initializing the JTable
    table = new JTable(model);

    // adding it to JScrollPane
    JScrollPane sp = new JScrollPane(table);
    add(sp);
  }

  public void addRow(ResourceParams resourceParam) {
    String[] row =
        new String[] {
          resourceParam.getMethod(), resourceParam.getResourceType().name(), resourceParam.getURL()
        };
    addRow(row);
  }

  public void addRow(String[] row) {
    model.addRow(row);
  }
}
