package security;

import net.minecraft.client.Minecraft;
import security.ui.FailedWindow;

import java.util.ArrayList;

public class HWIDManger {

    public ArrayList<String> hwid_list = null;
    public String HWIDUrl = "";

    public boolean locked = true;

    public HWIDManger(String HWIDUrl)
    {
        hwid_list = new ArrayList<String>();

        this.HWIDUrl = HWIDUrl;
    }

    public boolean isHWIDValid()
    {
        if(HWIDUtils.getHWIDList(this.HWIDUrl).contains(HWIDUtils.getHWID()))
        {
            return true;
        }
        return false;
    }

    public void processVerification()
    {
        if(!isHWIDValid())
        {
            new Thread(() -> {
                new FailedWindow();
            }).start();
            Thread.currentThread().suspend();
        }
    }

}
