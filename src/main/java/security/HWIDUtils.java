package security;

import thefellas.safepoint.Safepoint;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.util.List;

public class HWIDUtils {

    public static String getHWID() {
        try{
            String toEncrypt =  System.getenv("COMPUTERNAME") + System.getProperty("user.name") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(toEncrypt.getBytes());
            StringBuffer hexString = new StringBuffer();

            byte byteData[] = md.digest();

            for (byte aByteData : byteData) {
                String hex = Integer.toHexString(0xff & aByteData);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public static List<String> getHWIDList(String urlS) {
        try {
            final URL url = new URL(urlS);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;

            Safepoint.hwidManager.hwid_list.clear();

            while ((inputLine = in.readLine()) != null) {
                Safepoint.hwidManager.hwid_list.add(inputLine);
            }
        } catch(Exception e) {
           // Safepoint.lo.error("Loading HWIDS Failed!");
        }
        return Safepoint.hwidManager.hwid_list;
    }

}
