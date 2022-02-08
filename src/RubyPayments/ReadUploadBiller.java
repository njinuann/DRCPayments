/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPayments;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jpos.iso.ISOUtil;

/**
 *
 * @author NJINU
 */
public class ReadUploadBiller implements Runnable
{
    
    TXUtility util = new TXUtility();
    BFValue bfValue = new BFValue();
    //   public static BRLogger bRLogger = new BRLogger("rubyPay", "logs");
    // BillerMain main = new BillerMain();

    public java.util.Date timestamp = new java.util.Date();
    public SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
    private String timenow = null;
    
    public boolean checkPcConfig()
    {
        util.queryPcConfig();
        if (!util.checkLicense())
        {
            JOptionPane.showMessageDialog(null, "License has expired. Application will shut down",
                    "Licence Info", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
            return false;
        }
        else if (!util.queryPcConfig().getDISPLAY_VALUE().equals(BRController.ValueScheme)
                && !util.queryPcConfig().getPARAM_VALUE().equals(BRController.ValueScheme))
        {
            try
            {
                ExtPayMain.stopServices();
            }
            catch (Exception ex)
            {
                ExtPayMain.bRLogger.logError("ERROR ", ex);
            }
            JOptionPane.showMessageDialog(null, "This Application can only be used in FINCA RD CONGO",
                    "Environment Info", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else
        {
            return true;
        }
    }
    
    @Override
    public void run()
    {
        ExcelToDbUpload fileUpload = new ExcelToDbUpload();
        if (checkPcConfig())
        {
            ExtPayMain.bRLogger.logEvent(util.queryPcConfig().getDISPLAY_VALUE());
        }
        else
        {
            System.exit(1);
        }
        while (true)
        {
            File propsDir = new File("INPUT");
            propsDir.mkdir(); // Incase it does not exist
            File reconFileDir = new File("ReconFile");
            reconFileDir.mkdir();
            System.out.println("***********File Upload Service Started***********\r\n");
            
            timestamp = new java.util.Date();
            timenow = formatter.format(timestamp);
            fileUpload.uploadBiller(timenow);
            try
            {
                Thread.sleep(62000);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(ReadUploadBiller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
