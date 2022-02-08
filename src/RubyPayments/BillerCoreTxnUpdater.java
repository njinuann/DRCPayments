/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPayments;

/**
 *
 * @author NJINU
 */
public class BillerCoreTxnUpdater {

    TXUtility tXUtility = new TXUtility();

    public void updateTxn() {
        BillsTxn billsTxn = tXUtility.querryBillerCoreTxn();
        if (billsTxn.getCount() > 0) {
            //ExtPayMain.bRLogger.logEvent("BillerCoreTxnUpdater", TXUtility.convertToString(billsTxn.getCount()));
            for (int i = 1; i <= billsTxn.getCount(); i++) {
                ExtPayMain.bRLogger.logEvent("BillerCoreTxnUpdater-event code", billsTxn.getTxnNumber() + " for payment " + billsTxn.getEventCD());
                switch (billsTxn.getEventCD()) {
                    case "BL107":
                        tXUtility.updateCoreBillTaxTxn(billsTxn);
                        break;
                    case "BL106":
                        tXUtility.updateCoreBillTVTxn(billsTxn);
                        break;
                    case "BL105":
                        tXUtility.updateCoreBillFEETxn(billsTxn);
                        break;
                    default:
                        System.err.println("here for other txn " + billsTxn.getEventCD());
                        break;
                }

            }
        }
    }
}
