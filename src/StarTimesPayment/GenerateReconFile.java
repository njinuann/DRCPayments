/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StarTimesPayment;

//import RubyExtPay.BillUploadMain;
import RubyPayments.BRController;
import RubyPayments.EICrypt;
import RubyPayments.TXUtility;
import RubyPayments.ExtPayMain;
import RubyPayments.IDisplay;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 *
 * @author NJINU
 */
public class GenerateReconFile {

    public static String ReconFilePath = BRController.ReconFilePath;
    public static String ftpUser = BRController.STTFtpUser;
    public static String ftpPassword = BRController.STTFtpPass;
    public static String STTServer = BRController.ReconServer;
    public java.util.Date timestamp = new java.util.Date();

    public SimpleDateFormat STformatter = new SimpleDateFormat("yyyyMMdd");

    /**
     * A program that demonstrates how to upload files from local computer to a
     * remote FTP server using Apache Commons Net API.
     *
     * @author www.codejava.net
     */
    public void generateFile() {
        TXUtility util = new TXUtility();
        if (util.generateSTRecon()) {
            uploadFile();
        }
    }

    public void uploadFile() {
        String server = STTServer;//"197.231.253.66";
        int port = 21;
        String user = ftpUser;
        String pass = EICrypt.Decrypt(ftpPassword);

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // APPROACH #1: uploads first file using an InputStream
            // File firstLocalFile = new File("D:/Test/Projects.zip");
            File firstLocalFile = new File(ReconFilePath.concat("/").concat("dz-0100-" + STformatter.format(timestamp) + ".req"));
            //"dz-0100-"+STformatter.format(timestamp)+".req")

            String firstRemoteFile = "dz-0100-" + STformatter.format(timestamp) + ".req";
            boolean done;
            try (InputStream inputStream = new FileInputStream(firstLocalFile)) {
                System.out.println("Start uploading first file");
                done = ftpClient.storeFile(firstRemoteFile, inputStream);
            }
            System.err.println("file path: " + firstRemoteFile);
            if (done) {
                System.out.println("UPLOAD SUCCESSFULL");
                  ExtPayMain.bRLogger.logEvent("UPLOAD-INFO",firstRemoteFile + " has been Uploaded to the Server");

                try {
                    if (ftpClient.isConnected()) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                    }
                } catch (IOException ex) {                    
                      ExtPayMain.bRLogger.logEvent("UPLOAD-ERROR",firstRemoteFile + " Upload Failed"+ex);

                }
            }

//            // APPROACH #2: uploads second file using an OutputStream
//            // File secondLocalFile = new File("E:/Test/Report.doc");
//            File secondLocalFile = new File(ReconFilePath.concat("/").concat("dz-0100-" + STformatter.format(timestamp) + ".req"));
////            String secondRemoteFile = "test/Report.doc";
//            String secondRemoteFile = "dz-0100-" + STformatter.format(timestamp) + ".req";
//            inputStream = new FileInputStream(secondLocalFile);
//
//            System.out.println("Start uploading second file");
//            OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
//            byte[] bytesIn = new byte[4096];
//            int read = 0;
//
//            while ((read = inputStream.read(bytesIn)) != -1) {
//                outputStream.write(bytesIn, 0, read);
//            }
//            inputStream.close();
//            outputStream.close();
//
//            boolean completed = ftpClient.completePendingCommand();
//            if (completed) {
//                System.out.println("The second file is uploaded successfully.");
//            }
        } catch (IOException ex) {
               ExtPayMain.bRLogger.logEvent("UPLOAD-ERROR",ex);
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
