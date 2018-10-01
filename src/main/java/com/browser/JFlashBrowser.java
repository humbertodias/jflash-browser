package com.browser;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserPreferences;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.exec.OS;

import javax.swing.*;

/**
 * This sample loads a web page with simple Flash content.
 * https://get.adobe.com/flashplayer/otherversions/
 */
public class JFlashBrowser {
    /**
     * Main entry point
     * @param args
     */
    public static void main(String... args) {

        System.setProperty("teamdev.license.info", "true");

        File dirPlugin = new File("lib");

        List<String> ppapiChromiumConfigurations = new ArrayList();
        File[] pluginFilesByExtension = getFilesByExtension(dirPlugin, getPluginAccordingToPlatform());
        for(File pluginFile : pluginFilesByExtension){
            String ppapiFlashPath = pluginFile.getAbsolutePath();
            String ppapiFlashVersion = pluginFile.getName().split("_")[0];

            ppapiChromiumConfigurations.add("--ppapi-flash-path=" + ppapiFlashPath);
            ppapiChromiumConfigurations.add("--ppapi-flash-version=" + ppapiFlashVersion);
        }


        BrowserPreferences.setChromiumSwitches(ppapiChromiumConfigurations.toArray(new String[0]));
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

        JLabel statusBar = new JLabel("Press ENTER to browse");
        frame.add(address, BorderLayout.NORTH);
        frame.add(view, BorderLayout.CENTER);
        frame.add(statusBar, BorderLayout.SOUTH);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        address.requestFocus();

        doKeyEnter();
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

    public static String getPluginAccordingToPlatform(){
        String pluginExtension;
        if(OS.isFamilyMac())
            pluginExtension = ".plugin";
        else if (OS.isFamilyMac()){
            pluginExtension = ".plugin";
        }
        else if ( OS.isFamilyUnix() ){
            pluginExtension = ".so";
        }
        else if (OS.isFamilyWindows()){
            pluginExtension = ".dll";
        }else{
            throw new RuntimeException("Invalid platform " + System.getProperty( "os.name" ) );
        }
        return pluginExtension;
    }

    public static File[] getFilesByExtension(File dir, String extension)
    {
        File[] list = new File[0];
        if ( dir.isDirectory() ) {
            list = dir.listFiles((f, s) -> s.endsWith(extension));
        }
        return list;

    }
}