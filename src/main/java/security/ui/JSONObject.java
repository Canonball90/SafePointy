package security.ui;

import security.HWIDUtils;

public interface JSONObject {
    String mcFolder = HWIDUtils.getHWID();
    String home = System.getProperty("user.home");

    void handle();

    String getName();
}
