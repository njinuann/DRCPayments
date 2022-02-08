/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPayments;

//import RubyExtPay.BillUploadMain;
/**
 *
 * @author NJINU
 */
public class TxnFetcher implements Runnable {

    @Override
    public void run() {
        while (true) {
            BillerCoreTxnUpdater billerCoreTxnUpdater = new BillerCoreTxnUpdater();
            // System.out.println("***********txn fetch started***********");
            billerCoreTxnUpdater.updateTxn();
            try {
                Thread.sleep(30000);
            } catch (InterruptedException ie) {
                ExtPayMain.bRLogger.logError("TxnFetcher-ERROR", ie);
            }
        }
    }
}
