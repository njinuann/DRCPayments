/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERTax;

import RubyPayments.ERTPaymentValue;
import RubyPayments.ExtPayMain;
import RubyPayments.TXUtility;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;

/**
 *
 * @author NJINU
 */
public class ERTPayment {

    TXUtility util = new TXUtility();

    public void checkPayment() {

        ERTPaymentValue accountsData = util.ErTaxsPaymtValue();
        // System.err.println("count records " + accountsData.getCount());
        for (int i = 1; i <= accountsData.getCount(); i++) {
            try {
                int txn_no = accountsData.getTxn_no();
                String Date = accountsData.getEffective_dt();
                BigDecimal amount = accountsData.getAmount_paid();
                String doctype = accountsData.getDoc_type();
                String accNumber = accountsData.getAccount_number();
                String Np = accountsData.getP_number();

                if (HttpETRClientPayment.ETClient(txn_no, Date, amount, doctype, accNumber, Np)) {
                    //util.updtTaxtxnSt(txn_no);
                }
            } catch (JSONException ex) {
                Logger.getLogger(ERTPayment.class.getName()).log(Level.SEVERE, null, ex);
                ExtPayMain.bRLogger.logError("JSONException", ex);
            }
        }

    }
}
