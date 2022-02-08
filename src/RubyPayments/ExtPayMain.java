/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPayments;

import ERTax.ETPayProcess;
import ERTax.JsonServer;
import static NepPayServer.PostXML.processRequest;
import StarTimesPayment.StarTimesPay;
import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author NJINU
 */
public class ExtPayMain {

    public static RubyPmtUI uiFrame = null;
    public static ReadUploadBiller upload = new ReadUploadBiller();
    public static StarTimesPay startimes = new StarTimesPay();
    public static TxnFetcher txnFetcher = new TxnFetcher();
    public static ETPayProcess ertpay = new ETPayProcess();

    public static final javax.swing.JDialog consoleDialog = new javax.swing.JDialog();
    public static final IDisplay displayArea = new IDisplay();
    public static Color infoColor = new Color(0, 0, 128);
    public static Color errorColor = new Color(192, 0, 0);

    public static BRLogger bRLogger = new BRLogger("rubyPay", "logs");
    public static BRMeter brMeter = null;
    public static int counter = 0;

    public static void main(String[] args) {
        // TODO code application logic here
        setOutput();
        setLookAndFeel();
        new ExtPayMain().execute();

    }

    private void execute() {
        ChannelUtil.readState();
        BRController.initialize();
        startServices();
        showUI();
    }

    public void startServices() {
        try {
            //ExecutorService executor = Executors.newFixedThreadPool(5);

            bRLogger.logEvent("=======<Starting Services>=======");
            new Thread(startimes).start();
            new Thread(ertpay).start();
            new WRService().startSoap();
            JsonServer.txJsongetBourderau();
            processRequest();
            new Thread(upload).start();
            new Thread(txnFetcher).start();
            new Thread(this::cleanUp).start();

        } catch (IOException ex) {
            Logger.getLogger(ExtPayMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void stopServices() throws Exception {
        bRLogger.logEvent("=======<Stopping Services>=======");
        new Thread(upload).interrupt();
        new Thread(startimes).interrupt();
        new Thread(ertpay).interrupt();
        new Thread(txnFetcher).interrupt();
        new WRService().stop();
    }

    public void showUI() {
        SwingUtilities.invokeLater(()
                -> {
            uiFrame = new RubyPmtUI();
            if (brMeter != null) {
                uiFrame.setBrMeter(brMeter);
            } else {
                brMeter = uiFrame.getBrMeter();
            }
            uiFrame.setLocationRelativeTo(null);
            consoleDialog.setVisible(false);
            uiFrame.setVisible(true);
            pauseThread(1000);//pause for ui to be rendered
            brMeter.setConnected(true);
        });
        new Thread(() -> {
            while (true) {
                try {
                    System.gc();
                    Thread.sleep(900000);
                } catch (Exception ex) {
                    ex = null;
                }
            }
        }).start();

    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
    }

    public static void pauseThread(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ex) {
            ex = null;
        }
    }

    public void cleanUp() {
        while (true) {
            try {
                System.gc();
                Thread.sleep(900000);
            } catch (Exception ex) {
                ex = null;
            }
        }
    }

    public static void setOutput() {
        System.setOut(new EIPrint(new EIStream(infoColor)));
        System.setErr(new EIPrint(new EIStream(errorColor)));
        consoleDialog.setTitle("Ruby Bridge");
        CPanel panel = new CPanel();
        javax.swing.GroupLayout consoleDialogLayout = new javax.swing.GroupLayout(consoleDialog.getContentPane());
        consoleDialog.getContentPane().setLayout(consoleDialogLayout);
        consoleDialogLayout.setHorizontalGroup(
                consoleDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        consoleDialogLayout.setVerticalGroup(
                consoleDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        consoleDialog.setSize(500, 500);
        consoleDialog.setUndecorated(true);
        consoleDialog.setLocationRelativeTo(null);
        consoleDialog.setVisible(true);
    }

    public static void shutdown() {
        exit();
    }

    private static void exit() {
        System.exit(0);
    }
}
