/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPayments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Properties;

/**
 *
 * @author HP
 */
public final class ChannelUtil {

    public static boolean SERVICE_OFFLINE;

    public static synchronized String formatIsoAmount(BigDecimal amount) {
        String amtStr = padString(amount.abs().setScale(2, BigDecimal.ROUND_DOWN).toPlainString().replace(".", ""), 12,
                '0', true);
        return amtStr.substring(amtStr.length() - 12);
    }

    public static synchronized String padString(String s, int i, char c, boolean leftPad) {
        StringBuilder buffer = new StringBuilder(s);
        int j = buffer.length();
        if (i > 0 && i > j) {
            for (int k = 0; k <= i; k++) {
                if (leftPad) {
                    if (k < i - j) {
                        buffer.insert(0, c);
                    }
                } else if (k > j) {
                    buffer.append(c);
                }
            }
        }
        return buffer.toString();
    }

    public static synchronized BigDecimal extractISOAmount(String balanceString) {
        if (balanceString != null) {
            BigDecimal bal = new BigDecimal(balanceString.substring(
                    (balanceString.contains("C") ? balanceString.lastIndexOf("C") : balanceString.lastIndexOf("D"))
                    + 1));
            return balanceString.contains("C") ? bal.movePointLeft(2) : bal.movePointLeft(2).negate();
        }
        return BigDecimal.ZERO;
    }

    public static void saveState(String status) {
        try (FileOutputStream fos = new FileOutputStream("conf/status.prp")) {
            Properties setup = new Properties();
            setup.setProperty("SERVICE_OFFLINE", status);
            setup.storeToXML(fos, "Updated on " + new Date());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readState() {

        File state = new File("conf", "status.prp");
        if (!state.exists()) {
            saveState("N");
        }
        try (FileInputStream fis = new FileInputStream(state)) {
            Properties props = new Properties();
            props.loadFromXML(fis);
            ChannelUtil.SERVICE_OFFLINE = props.getProperty("SERVICE_OFFLINE", "N").equalsIgnoreCase("Y");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createIfNotExists() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
