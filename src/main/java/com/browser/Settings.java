package com.browser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.exec.OS;

public class Settings {

  public static final String INITIAL_PAGE = "http://www.webthrower.com/portfolio/narnia.htm";

  public static String[] getPluginConfiguration() {
    File dirPlugin = new File("lib");

    List<String> ppapiChromiumConfigurations = new ArrayList();
    File[] pluginFilesByExtension = getFilesByExtension(dirPlugin, getPluginAccordingToPlatform());
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

  private static File[] getFilesByExtension(File dir, String extension) {
    File[] list = new File[0];
    if (dir.isDirectory()) {
      list = dir.listFiles((f, s) -> s.endsWith(extension));
    }
    return list;
  }
}
