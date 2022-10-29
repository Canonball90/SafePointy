package security.ui;

import security.HWIDUtils;

import javax.swing.*;

public class SuccessfullWindow {

    private int width = 700;
    private int height = 600;

    public SuccessfullWindow()
    {
        JFrame frame = new JFrame("HWID Verification");

        frame.setSize(width, height);

        JLabel label = new JLabel("HWID Verification was Successfull. Have a nice day :)" + HWIDUtils.getHWID());

        label.setLocation(100, 200);

        frame.add(label);

        frame.setVisible(true);

    }
}
