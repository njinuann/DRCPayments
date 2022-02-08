/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPayments;

import java.math.BigDecimal;

/**
 *
 * @author NJINU
 */
public class BillerCharge
{

    private String chargeCode;
    private String chargeDesc;
    private String incomeGl;
    private BigDecimal chargeFlatAmt;
    private String chargeType;
    private BigDecimal chargePCTAmt;
    private String chargeStatus;
    private String taxDesc;
    private String taxGl;
    private BigDecimal taxRate;
    private int txcount;

    /**
     * @return the chargeCode
     */
    public String getChargeCode()
    {
        return chargeCode;
    }

    /**
     * @param chargeCode the chargeCode to set
     */
    public void setChargeCode(String chargeCode)
    {
        this.chargeCode = chargeCode;
    }

    /**
     * @return the chargeDesc
     */
    public String getChargeDesc()
    {
        return chargeDesc;
    }

    /**
     * @param chargeDesc the chargeDesc to set
     */
    public void setChargeDesc(String chargeDesc)
    {
        this.chargeDesc = chargeDesc;
    }

    /**
     * @return the incomeGl
     */
    public String getIncomeGl()
    {
        return incomeGl;
    }

    /**
     * @param incomeGl the incomeGl to set
     */
    public void setIncomeGl(String incomeGl)
    {
        this.incomeGl = incomeGl;
    }

    /**
     * @return the chargeFlatAmt
     */
    public BigDecimal getChargeFlatAmt()
    {
        return chargeFlatAmt;
    }

    /**
     * @param chargeFlatAmt the chargeFlatAmt to set
     */
    public void setChargeFlatAmt(BigDecimal chargeFlatAmt)
    {
        this.chargeFlatAmt = chargeFlatAmt;
    }

    /**
     * @return the chargeType
     */
    public String getChargeType()
    {
        return chargeType;
    }

    /**
     * @param chargeType the chargeType to set
     */
    public void setChargeType(String chargeType)
    {
        this.chargeType = chargeType;
    }

    /**
     * @return the chargePCTAmt
     */
    public BigDecimal getChargePCTAmt()
    {
        return chargePCTAmt;
    }

    /**
     * @param chargePCTAmt the chargePCTAmt to set
     */
    public void setChargePCTAmt(BigDecimal chargePCTAmt)
    {
        this.chargePCTAmt = chargePCTAmt;
    }

    /**
     * @return the chargeStatus
     */
    public String getChargeStatus()
    {
        return chargeStatus;
    }

    /**
     * @param chargeStatus the chargeStatus to set
     */
    public void setChargeStatus(String chargeStatus)
    {
        this.chargeStatus = chargeStatus;
    }

    /**
     * @return the taxDesc
     */
    public String getTaxDesc()
    {
        return taxDesc;
    }

    /**
     * @param taxDesc the taxDesc to set
     */
    public void setTaxDesc(String taxDesc)
    {
        this.taxDesc = taxDesc;
    }

    /**
     * @return the taxGl
     */
    public String getTaxGl()
    {
        return taxGl;
    }

    /**
     * @param taxGl the taxGl to set
     */
    public void setTaxGl(String taxGl)
    {
        this.taxGl = taxGl;
    }

    /**
     * @return the taxRate
     */
    public BigDecimal getTaxRate()
    {
        return taxRate;
    }

    /**
     * @param taxRate the taxRate to set
     */
    public void setTaxRate(BigDecimal taxRate)
    {
        this.taxRate = taxRate;
    }

    /**
     * @return the txcount
     */
    public int getTxcount()
    {
        return txcount;
    }

    /**
     * @param txcount the txcount to set
     */
    public void setTxcount(int txcount)
    {
        this.txcount = txcount;
    }
}
