package security.ui;

import security.HWIDUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class FailedWindow {

    private int width = 700;
    private int height = 600;

    public FailedWindow()
    {
        JFrame frame = new JFrame("HWID Verification");

        JOptionPane.showMessageDialog(frame, "We got an error during hwid checking" + "\n" + "HWID: " + HWIDUtils.getHWID() + "\n(Copied to clipboard)", "Can't verify your HWID", JOptionPane.PLAIN_MESSAGE, UIManager.getIcon("OptionPane.warningIcon"));

        copyToClipboard();
    }

    public static void copyToClipboard() {
        StringSelection selection = new StringSelection(HWIDUtils.getHWID());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
    }

}
