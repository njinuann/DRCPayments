/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPayments;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;

/**
 *
 * @author Pecherk
 */
public class TXRequest
{

    private String reference;
    private String accountNumber1;
    private String accountNumber2;
    private String currencyCode;
    private BigDecimal txnAmount;
    private BigDecimal chargeAmount;
    private BigDecimal taxAmount;
    private String txnNarration;
    private String chargeNarration;
    private String chargeDebitAccount;
    private String chargeCreditLedger;
    private String taxCreditLedger;
    private String taxNarration;
    private String channelContraLedger;
    private String toCurrencyCode;
    private Long channelId;
    private String AccessCode;
    private String TxnDate;
    private String channelCode;
    private String referenceType;
    private Long currentBu;
    private String DebitAccount;
    private String CreditAccount;
    private String SystemUserID;
    //====biller=====
    private String billerchargeCd;
    private String billerchargeDesc;
    private String billerincomeGl;
    private BigDecimal billerchargeFlatAmt;
    private String billerchargeType;
    private BigDecimal billerchargePCTAmt;
    private String billerchargeStatus;
    private String billerTaxDesc;
    private String billerTaxGl;
    private BigDecimal billerTaxRate;
    private String accountPmtType;

    /**
     * @return the reference
     */
    public String getReference()
    {
        return reference;
    }

    /**
     * @param reference the reference to set
     */
    public void setReference(String reference)
    {
        this.reference = reference;
    }

    /**
     * @return the currencyCode
     */
    public String getCurrencyCode()
    {
        return currencyCode;
    }

    /**
     * @param currencyCode the currencyCode to set
     */
    public void setCurrencyCode(String currencyCode)
    {
        this.currencyCode = currencyCode;
    }

    /**
     * @return the txnAmount
     */
    public BigDecimal getTxnAmount()
    {
        return txnAmount;
    }

    /**
     * @param txnAmount the txnAmount to set
     */
    public void setTxnAmount(BigDecimal txnAmount)
    {
        this.txnAmount = txnAmount;
    }

    /**
     * @return the chargeAmount
     */
    public BigDecimal getChargeAmount()
    {
        return chargeAmount;
    }

    /**
     * @param chargeAmount the chargeAmount to set
     */
    public void setChargeAmount(BigDecimal chargeAmount)
    {
        this.chargeAmount = chargeAmount;
    }

    /**
     * @return the txnNarration
     */
    public String getTxnNarration()
    {
        return txnNarration;
    }

    /**
     * @param txnNarration the txnNarration to set
     */
    public void setTxnNarration(String txnNarration)
    {
        this.txnNarration = txnNarration;
    }

    /**
     * @return the chargeNarration
     */
    public String getChargeNarration()
    {
        return chargeNarration;
    }

    /**
     * @param chargeNarration the chargeNarration to set
     */
    public void setChargeNarration(String chargeNarration)
    {
        this.chargeNarration = chargeNarration;
    }

    /**
     * @return the chargeDebitAccount
     */
    public String getChargeDebitAccount()
    {
        return chargeDebitAccount;
    }

    /**
     * @param chargeDebitAccount the chargeDebitAccount to set
     */
    public void setChargeDebitAccount(String chargeDebitAccount)
    {
        this.chargeDebitAccount = chargeDebitAccount;
    }

    /**
     * @return the chargeCreditLedger
     */
    public String getChargeCreditLedger()
    {
        return chargeCreditLedger;
    }

    /**
     * @param chargeCreditLedger the chargeCreditLedger to set
     */
    public void setChargeCreditLedger(String chargeCreditLedger)
    {
        this.chargeCreditLedger = chargeCreditLedger;
    }

    /**
     * @return the accountNumber1
     */
    public String getAccountNumber1()
    {
        return accountNumber1;
    }

    /**
     * @param accountNumber1 the accountNumber1 to set
     */
    public void setAccountNumber1(String accountNumber1)
    {
        this.accountNumber1 = accountNumber1;
    }

    /**
     * @return the accountNumber2
     */
    public String getAccountNumber2()
    {
        return accountNumber2;
    }

    /**
     * @param accountNumber2 the accountNumber2 to set
     */
    public void setAccountNumber2(String accountNumber2)
    {
        this.accountNumber2 = accountNumber2;
    }

    /**
     * @return the taxCreditLedger
     */
    public String getTaxCreditLedger()
    {
        return taxCreditLedger;
    }

    /**
     * @param taxCreditLedger the taxCreditLedger to set
     */
    public void setTaxCreditLedger(String taxCreditLedger)
    {
        this.taxCreditLedger = taxCreditLedger;
    }

    /**
     * @return the taxAmount
     */
    public BigDecimal getTaxAmount()
    {
        return taxAmount;
    }

    /**
     * @param taxAmount the taxAmount to set
     */
    public void setTaxAmount(BigDecimal taxAmount)
    {
        this.taxAmount = taxAmount;
    }

    /**
     * @return the taxNarration
     */
    public String getTaxNarration()
    {
        return taxNarration;
    }

    /**
     * @param taxNarration the taxNarration to set
     */
    public void setTaxNarration(String taxNarration)
    {
        this.taxNarration = taxNarration;
    }

    /**
     * @return the channelContraLedger
     */
    public String getChannelContraLedger()
    {
        return channelContraLedger;
    }

    /**
     * @param channelContraLedger the channelContraLedger to set
     */
    public void setChannelContraLedger(String channelContraLedger)
    {
        this.channelContraLedger = channelContraLedger;
    }

    public Long getChannelId()
    {
        return channelId;
    }

    public void setChannelId(Long channelId)
    {
        this.channelId = channelId;
    }

///======== additional 
    public String getDebitAccount()
    {
        return DebitAccount;
    }

    public void setDebitAccount(String DebitAccount)
    {
        this.DebitAccount = DebitAccount;
    }

    public String getCreditAccount()
    {
        return CreditAccount;
    }

    public void setCreditAccount(String CreditAccount)
    {
        this.CreditAccount = CreditAccount;
    }

    public String getSystemUserID()
    {
        return SystemUserID;
    }

    public void setSystemUserID(String SystemUserID)
    {
        this.SystemUserID = SystemUserID;
    }

    public String getAccessCode()
    {
        return AccessCode;
    }

    public void setAccessCode(String AccessCode)
    {
        this.AccessCode = AccessCode;
    }

    public String getTxnDate()
    {
        return TxnDate;
    }

    public void setTxnDate(String TxnDate)
    {
        this.TxnDate = TxnDate;
    }

    public String getChannelCode()
    {
        return channelCode;
    }

    public void setChannelCode(String channelCode)
    {
        this.channelCode = channelCode;
    }

    // after addition
    @Override
    public String toString()
    {
        boolean c = false;
        String str = "TXRequest{ ";
        try
        {
            for (PropertyDescriptor propertyDesc : Introspector.getBeanInfo(TXRequest.class).getPropertyDescriptors())
            {
                Method readMethod = propertyDesc.getReadMethod();
                if (readMethod != null)
                {
                    str += (c ? ", " : "") + propertyDesc.getName() + "=<" + propertyDesc.getReadMethod().invoke(this) + ">";
                    c = true;
                }
            }
        }
        catch (Exception ex)
        {
            ex = null;
        }
        str += " }";
        return str;
    }

    /**
     * @return the toCurrencyCode
     */
    public String getToCurrencyCode()
    {
        return toCurrencyCode;
    }

    /**
     * @param toCurrencyCode the toCurrencyCode to set
     */
    public void setToCurrencyCode(String toCurrencyCode)
    {
        this.toCurrencyCode = toCurrencyCode;
    }

    /**
     * @return the referenceType
     */
    public String getReferenceType()
    {
        return referenceType;
    }

    /**
     * @param referenceType the referenceType to set
     */
    public void setReferenceType(String referenceType)
    {
        this.referenceType = referenceType;
    }

    /**
     * @return the currentBu
     */
    public Long getCurrentBu()
    {
        return currentBu;
    }

    /**
     * @param currentBu the currentBu to set
     */
    public void setCurrentBu(Long currentBu)
    {
        this.currentBu = currentBu;
    }

    /**
     * @return the billerchargeCd
     */
    public String getBillerchargeCd()
    {
        return billerchargeCd;
    }

    /**
     * @param billerchargeCd the billerchargeCd to set
     */
    public void setBillerchargeCd(String billerchargeCd)
    {
        this.billerchargeCd = billerchargeCd;
    }

    /**
     * @return the billerchargeDesc
     */
    public String getBillerchargeDesc()
    {
        return billerchargeDesc;
    }

    /**
     * @param billerchargeDesc the billerchargeDesc to set
     */
    public void setBillerchargeDesc(String billerchargeDesc)
    {
        this.billerchargeDesc = billerchargeDesc;
    }

    /**
     * @return the billerincomeGl
     */
    public String getBillerincomeGl()
    {
        return billerincomeGl;
    }

    /**
     * @param billerincomeGl the billerincomeGl to set
     */
    public void setBillerincomeGl(String billerincomeGl)
    {
        this.billerincomeGl = billerincomeGl;
    }

    /**
     * @return the billerchargeFlatAmt
     */
    public BigDecimal getBillerchargeFlatAmt()
    {
        return billerchargeFlatAmt;
    }

    /**
     * @param billerchargeFlatAmt the billerchargeFlatAmt to set
     */
    public void setBillerchargeFlatAmt(BigDecimal billerchargeFlatAmt)
    {
        this.billerchargeFlatAmt = billerchargeFlatAmt;
    }

    /**
     * @return the billerchargeType
     */
    public String getBillerchargeType()
    {
        return billerchargeType;
    }

    /**
     * @param billerchargeType the billerchargeType to set
     */
    public void setBillerchargeType(String billerchargeType)
    {
        this.billerchargeType = billerchargeType;
    }

    /**
     * @return the billerchargePCTAmt
     */
    public BigDecimal getBillerchargePCTAmt()
    {
        return billerchargePCTAmt;
    }

    /**
     * @param billerchargePCTAmt the billerchargePCTAmt to set
     */
    public void setBillerchargePCTAmt(BigDecimal billerchargePCTAmt)
    {
        this.billerchargePCTAmt = billerchargePCTAmt;
    }

    /**
     * @return the billerchargeStatus
     */
    public String getBillerchargeStatus()
    {
        return billerchargeStatus;
    }

    /**
     * @param billerchargeStatus the billerchargeStatus to set
     */
    public void setBillerchargeStatus(String billerchargeStatus)
    {
        this.billerchargeStatus = billerchargeStatus;
    }

    /**
     * @return the billerTaxDesc
     */
    public String getBillerTaxDesc()
    {
        return billerTaxDesc;
    }

    /**
     * @param billerTaxDesc the billerTaxDesc to set
     */
    public void setBillerTaxDesc(String billerTaxDesc)
    {
        this.billerTaxDesc = billerTaxDesc;
    }

    /**
     * @return the billerTaxGl
     */
    public String getBillerTaxGl()
    {
        return billerTaxGl;
    }

    /**
     * @param billerTaxGl the billerTaxGl to set
     */
    public void setBillerTaxGl(String billerTaxGl)
    {
        this.billerTaxGl = billerTaxGl;
    }

    /**
     * @return the billerTaxRate
     */
    public BigDecimal getBillerTaxRate()
    {
        return billerTaxRate;
    }

    /**
     * @param billerTaxRate the billerTaxRate to set
     */
    public void setBillerTaxRate(BigDecimal billerTaxRate)
    {
        this.billerTaxRate = billerTaxRate;
    }

    /**
     * @return the accountPmtType
     */
    public String getAccountPmtType()
    {
        return accountPmtType;
    }

    /**
     * @param accountPmtType the accountPmtType to set
     */
    public void setAccountPmtType(String accountPmtType)
    {
        this.accountPmtType = accountPmtType;
    }

}
