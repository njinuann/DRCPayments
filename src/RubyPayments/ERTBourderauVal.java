/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPayments;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author NJINU
 */
public class ERTBourderauVal {

    public int txn_no;
    public String effective_dt;
    public BigDecimal Amount_paid;
    public String Doc_type;
    public String Account_number;
    public String P_number;
    public int count;

//    public ERTValue(String P_Number) {
//        this.P_Number = P_Number;
//    }

    public int getTxn_no() {
        return txn_no;
    }

    public void setTxn_no(int txn_no) {
        this.txn_no = txn_no;
    }

  
    public String getEffective_dt() {
        return effective_dt;
    }

    public void setEffective_dt(String effective_dt) {
        this.effective_dt = effective_dt;
    }

    public BigDecimal getAmount_paid() {
        return Amount_paid;
    }

    public void setAmount_paid(BigDecimal Amount_paid) {
        this.Amount_paid = Amount_paid;
    }

    public String getDoc_type() {
        return Doc_type;
    }

    public void setDoc_type(String Doc_type) {
        this.Doc_type = Doc_type;
    }

    public String getAccount_number() {
        return Account_number;
    }

    public void setAccount_number(String Account_number) {
        this.Account_number = Account_number;
    }

    public String getP_number() {
        return P_number;
    }

    public void setP_number(String P_number) {
        this.P_number = P_number;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    

}
