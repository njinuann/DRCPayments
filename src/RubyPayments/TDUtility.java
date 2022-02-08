/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPayments;

import java.math.BigDecimal;
import org.jpos.iso.ISOMsg;

/**
 *
 * @author HP
 */
public class TDUtility {

    public String padString(String s, int i, char c, boolean leftPad) {
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

    public String formatIsoAmount(BigDecimal amount) {
        String amtStr = padString(amount.abs().setScale(2, BigDecimal.ROUND_DOWN).toPlainString().replace(".", ""), 12, '0', true);
        return amtStr.substring(amtStr.length() - 12);
    }

    public BigDecimal extractISOAmount(ISOMsg sendReceive) {
        if (sendReceive.hasField(54)) {
            if (sendReceive.getString(54) == null || sendReceive.getString(54).length() < 40) {
                return BigDecimal.ZERO;
            }
            String balanceString = sendReceive.getString(54).substring(27);
            BigDecimal bal = new BigDecimal(balanceString.substring(1));
            return balanceString.startsWith("C") ? bal.movePointLeft(2) : bal.movePointLeft(2).negate();
        }
        return BigDecimal.ZERO;
    }

}
