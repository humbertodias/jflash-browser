package com.browser;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserPreferences;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FilenameFilter;

import org.apache.commons.exec.OS;

/**
 * This sample loads a web page with simple Flash content.
 * https://get.adobe.com/flashplayer/otherversions/
 */
public class JFlashBrowser {
    public static void main(String... args) {

        System.setProperty("teamdev.license.info", "true");
        File dirPlugin = new File("lib");
        File pluginFile = new File(dirPlugin, getPluginName(dirPlugin));
        String ppapiFlashPath = pluginFile.getAbsolutePath();
        String ppapiFlashVersion = pluginFile.getName().split("_")[0];
        System.out.println("ppapiFlashPath:" + ppapiFlashPath);
        BrowserPreferences.setChromiumSwitches(
                "--ppapi-flash-path=" + ppapiFlashPath,
                "--ppapi-flash-version=" + ppapiFlashVersion);
        Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);

        JFrame frame = new JFrame("JFlashBrowser");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JTextField address = new JTextField("http://www.webthrower.com/portfolio/narnia.htm");
        address.addActionListener( new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                browser.loadURL(address.getText());
            }
        });

        JLabel statusBar = new JLabel(ppapiFlashVersion + " - " + ppapiFlashPath);
        frame.add(address, BorderLayout.NORTH);
        frame.add(view, BorderLayout.CENTER);
        frame.add(statusBar, BorderLayout.SOUTH);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        address.requestFocus();

        doKeyEnter();
    }

    private static String getPluginName(File dir){
        if ( OS.isFamilyUnix() ){
            return getFirstFileByExtension(dir, ".so");
        }
        else if (OS.isFamilyMac()){
            return getFirstFileByExtension(dir, ".plugin");
        }
        else if (OS.isFamilyWindows()){
            return getFirstFileByExtension(dir, ".dll");
        }
        throw new RuntimeException("Invalid platform!");
    }

    private static void doKeyEnter(){
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.delay(1000);
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static String getFirstFileByExtension(File dir, String extension)
    {
        if ( dir.isDirectory() )
        {

            String[] list = dir.list((f, s) -> s.endsWith(extension));

            if ( list.length > 0 )
            {
                return list[0];
            }
        }

        return "";

    }
}