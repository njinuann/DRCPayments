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
public class BillsTxn
{

    private Long txnNumber;
    private String referenceNo;
    private BigDecimal amount;
    private String acct_no;
    private String createDate;
    private String eventCD;
    private String user;
    private String tranRefTxt;
    private String description;
    private String currency;
    private String tranDate;
    
    private int Count;

    /**
     * @return the txnNumber
     */
    public Long getTxnNumber()
    {
        return txnNumber;
    }

    /**
     * @param txnNumber the txnNumber to set
     */
    public void setTxnNumber(Long txnNumber)
    {
        this.txnNumber = txnNumber;
    }

    /**
     * @return the referenceNo
     */
    public String getReferenceNo()
    {
        return referenceNo;
    }

    /**
     * @param referenceNo the referenceNo to set
     */
    public void setReferenceNo(String referenceNo)
    {
        this.referenceNo = referenceNo;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount()
    {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    /**
     * @return the acct_no
     */
    public String getAcct_no()
    {
        return acct_no;
    }

    /**
     * @param acct_no the acct_no to set
     */
    public void setAcct_no(String acct_no)
    {
        this.acct_no = acct_no;
    }

    /**
     * @return the Count
     */
    public int getCount()
    {
        return Count;
    }

    /**
     * @param Count the Count to set
     */
    public void setCount(int Count)
    {
        this.Count = Count;
    }

    /**
     * @return the createDate
     */
    public String getCreateDate()
    {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }

    /**
     * @return the eventCD
     */
    public String getEventCD()
    {
        return eventCD;
    }

    /**
     * @param eventCD the eventCD to set
     */
    public void setEventCD(String eventCD)
    {
        this.eventCD = eventCD;
    }

    /**
     * @return the user
     */
    public String getUser()
    {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user)
    {
        this.user = user;
    }

    /**
     * @return the tranRefTxt
     */
    public String getTranRefTxt()
    {
        return tranRefTxt;
    }

    /**
     * @param tranRefTxt the tranRefTxt to set
     */
    public void setTranRefTxt(String tranRefTxt)
    {
        this.tranRefTxt = tranRefTxt;
    }

    /**
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return the currency
     */
    public String getCurrency()
    {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    /**
     * @return the tranDate
     */
    public String getTranDate()
    {
        return tranDate;
    }

    /**
     * @param tranDate the tranDate to set
     */
    public void setTranDate(String tranDate)
    {
        this.tranDate = tranDate;
    }

}
