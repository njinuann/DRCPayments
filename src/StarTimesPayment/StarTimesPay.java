/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StarTimesPayment;

//import RubyExtPay.BillUploadMain;
import RubyPayments.BRController;
import RubyPayments.ChannelUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author NJINU
 * @co-author Olupot.D
 */
public class StarTimesPay implements Runnable {

    StarttimesProcess stprocess = new StarttimesProcess();

    @Override
    public void run() {
        while (true) {
            System.out.println("***********Star Times Service Running***********");
            stprocess.StarTimesPayment();

            if (ChannelUtil.SERVICE_OFFLINE) {
                runBackup();
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(StarTimesPay.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("***********Star Times Service Completed***********");
        }
    }

    public void runBackup() {
        try (Connection con = DriverManager.getConnection(BRController.CMSchemaJdbcUrl, BRController.CMSchemaName,
                BRController.CMSchemaPassword); Statement stmt = con.createStatement()) {
            try {
                stmt.execute("INSERT INTO " + BRController.offlineCoreSchemaName + ".BL_STARTTIMES SELECT * FROM "
                        + BRController.CMSchemaName + ".BL_STARTTIMES WHERE TRANSACTIONNO IN (SELECT JOURNAL_ID FROM "
                        + BRController.offlineCoreSchemaName + ".ST_JOURNALS) AND TRANSACTIONNO NOT IN (SELECT TRANSACTIONNO FROM "
                        + BRController.offlineCoreSchemaName + ".BL_STARTTIMES)");
            } catch (SQLException ex) {
            }
        } catch (SQLException ex) {

        }
    }
}
