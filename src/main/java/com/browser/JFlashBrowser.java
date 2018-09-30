package com.browser;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserPreferences;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import org.apache.commons.exec.OS;

/**
 * This sample loads a web page with simple Flash content.
 * https://get.adobe.com/flashplayer/otherversions/
 */
public class JFlashBrowser {
    public static void main(String... args) {

        System.setProperty("teamdev.license.info", "true");
        String ppapiFlashPath = new File("lib",getPluginName()).getAbsolutePath();
        System.out.println("ppapiFlashPath:" + ppapiFlashPath);
        BrowserPreferences.setChromiumSwitches(
                "--ppapi-flash-path=" + ppapiFlashPath,
                "--ppapi-flash-version=20.0.0.270");
        Browser browser = new Browser();
        BrowserView view = new BrowserView(browser);

        JFrame frame = new JFrame("JFlashBrowser");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JTextField address = new JTextField("http://wwww.mundijuegos.com");
        address.addActionListener( new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                browser.loadURL(address.getText());
            }
        });
        frame.add(address, BorderLayout.NORTH);
        frame.add(view, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        String url = "http://www.webthrower.com/portfolio/narnia.htm";
        url = "http://www.mundijuegos.com";
        browser.loadURL(url);
    }

    private static String getPluginName(){
        if ( OS.isFamilyUnix() ){
            return "libpepflashplayer.so";
        }
        else if (OS.isFamilyMac()){
            return "PepperFlashPlayer.plugin";
        }
        else if (OS.isFamilyWindows()){
            return "pepflashplayer.dll";
        }
        throw new RuntimeException("Invalid platform!");
    }
}