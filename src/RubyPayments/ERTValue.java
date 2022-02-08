/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPayments;

import java.util.Date;

/**
 *
 * @author NJINU
 */
public class ERTValue {

    public String P_Number;
    public String Applicant_Name;
    public Double Amount_paid;
    public String Bgt_atc_grp;
    public String Bgt_atc_naming;
    public String Issue_Date;
    public String Pmt_Period;
    public String Pmt_deadline_date;
    public String txnNo;
    public int count;

//    public ERTValue(String P_Number) {
//        this.P_Number = P_Number;
//    }

    public String getTxnNo() {
        return txnNo;
    }

    public void setTxnNo(String txnNo) {
        this.txnNo = txnNo;
    }

    
    
    public String getP_Number() {
        return P_Number;
    }

    public void setP_Number(String P_Number) {
        this.P_Number = P_Number;
    }

    public String getApplicant_Name() {
        return Applicant_Name;
    }

    public void setApplicant_Name(String Apllicant_Name) {
        this.Applicant_Name = Apllicant_Name;
    }

    public Double getAmount_paid() {
        return Amount_paid;
    }

    public void setAmount_paid(Double Amount_paid) {
        this.Amount_paid = Amount_paid;
    }

    public String getBgt_atc_grp() {
        return Bgt_atc_grp;
    }

    public void setBgt_atc_grp(String Bgt_atc_grp) {
        this.Bgt_atc_grp = Bgt_atc_grp;
    }

    public String getBgt_atc_naming() {
        return Bgt_atc_naming;
    }

    public void setBgt_atc_naming(String Bgt_atc_naming) {
        this.Bgt_atc_naming = Bgt_atc_naming;
    }

    public String getIssue_Date() {
        return Issue_Date;
    }

    public void setIssue_Date(String Issue_Date) {
        this.Issue_Date = Issue_Date;
    }

    public String getPmt_Period() {
        return Pmt_Period;
    }

    public void setPmt_Period(String Pmt_Period) {
        this.Pmt_Period = Pmt_Period;
    }

   
    public String getPmt_deadline_date() {
        return Pmt_deadline_date;
    }

    public void setPmt_deadline_date(String Pmt_deadline_date) {
        this.Pmt_deadline_date = Pmt_deadline_date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
