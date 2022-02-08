/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPayments;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author NJINU
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BaseResponse {

    public String accountNumber;
    public String paymentType;
    public String reference;
    public String respCode;
    public String Currency;
    public String debitAccount;
    public String creditAccount;
    public BigDecimal amount;
    public BigDecimal balance;
    public long ChanellId;
    public String txnJournalId;
    public String chargeJournalId;

    public String AccountName;

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String AccountName) {
        this.AccountName = AccountName;
    }

    public long getChanellId() {
        return ChanellId;
    }

    public void setChanellId(long ChanellId) {
        this.ChanellId = ChanellId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(String debitAccount) {
        this.debitAccount = debitAccount;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public String getTxnJournalId() {
        return txnJournalId;
    }

    public void setTxnJournalId(String txnJournalId) {
        this.txnJournalId = txnJournalId;
    }

    public String getChargeJournalId() {
        return chargeJournalId;
    }

    public void setChargeJournalId(String chargeJournalId) {
        this.chargeJournalId = chargeJournalId;
    }

   

}
