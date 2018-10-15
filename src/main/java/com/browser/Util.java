package com.browser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.exec.OS;

public class Util {

  public static URL toURL(String address) {
    URL url;
    try {
      url = URI.create(address).toURL();
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
    return url;
  }

  public static File[] getFilesByExtension(File dir, String extension) {
    File[] list = new File[0];
    if (dir.isDirectory()) {
      list = dir.listFiles((f, s) -> s.endsWith(extension));
    }
    return list;
  }

  public static String getFileExtension(String fileName) {
    String parts[] = ("" + fileName).split("\\.");
    return parts.length > 0 ? parts[parts.length - 1] : null;
  }

  public static String getFileName(String fileName) {
    String parts[] = ("" + fileName).split("/");
    return parts.length > 0 ? parts[parts.length - 1] : null;
  }

  public static final String INITIAL_PAGE = "http://www.mundijuegos.com";

  public static String[] getPluginConfiguration() {
    File dirPlugin = new File("lib");

    List<String> ppapiChromiumConfigurations = new ArrayList();
    File[] pluginFilesByExtension =
        Util.getFilesByExtension(dirPlugin, getPluginAccordingToPlatform());
    for (File pluginFile : pluginFilesByExtension) {
      String ppapiFlashPath = pluginFile.getAbsolutePath();
      String ppapiFlashVersion = pluginFile.getName().split("_")[0];

      ppapiChromiumConfigurations.add("--ppapi-flash-path=" + ppapiFlashPath);
      ppapiChromiumConfigurations.add("--ppapi-flash-version=" + ppapiFlashVersion);
    }
    return ppapiChromiumConfigurations.toArray(new String[0]);
  }

  private static String getPluginAccordingToPlatform() {
    String pluginExtension;
    if (OS.isFamilyMac()) pluginExtension = ".plugin";
    else if (OS.isFamilyMac()) {
      pluginExtension = ".plugin";
    } else if (OS.isFamilyUnix()) {
      pluginExtension = ".so";
    } else if (OS.isFamilyWindows()) {
      pluginExtension = ".dll";
    } else {
      throw new RuntimeException("Invalid platform " + System.getProperty("os.name"));
    }
    return pluginExtension;
  }
}
