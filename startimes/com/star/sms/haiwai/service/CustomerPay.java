
package com.star.sms.haiwai.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="transactionNo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="payerID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="payerPwd" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="customerCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="customerName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="customerTel" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="smartCardCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="fee" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         &lt;element name="receiptCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="deviceType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="transferTime" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "transactionNo",
    "payerID",
    "payerPwd",
    "customerCode",
    "customerName",
    "customerTel",
    "smartCardCode",
    "fee",
    "receiptCode",
    "deviceType",
    "transferTime"
})
@XmlRootElement(name = "customerPay")
public class CustomerPay {

    @XmlElement(required = true, nillable = true)
    protected String transactionNo;
    @XmlElement(required = true, nillable = true)
    protected String payerID;
    @XmlElement(required = true, nillable = true)
    protected String payerPwd;
    @XmlElement(required = true, nillable = true)
    protected String customerCode;
    @XmlElement(required = true, nillable = true)
    protected String customerName;
    @XmlElement(required = true, nillable = true)
    protected String customerTel;
    @XmlElement(required = true, nillable = true)
    protected String smartCardCode;
    @XmlElement(required = true, type = Double.class, nillable = true)
    protected Double fee;
    @XmlElement(required = true, nillable = true)
    protected String receiptCode;
    @XmlElement(required = true, nillable = true)
    protected String deviceType;
    @XmlElement(required = true, nillable = true)
    protected String transferTime;

    /**
     * Gets the value of the transactionNo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransactionNo() {
        return transactionNo;
    }

    /**
     * Sets the value of the transactionNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransactionNo(String value) {
        this.transactionNo = value;
    }

    /**
     * Gets the value of the payerID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayerID() {
        return payerID;
    }

    /**
     * Sets the value of the payerID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayerID(String value) {
        this.payerID = value;
    }

    /**
     * Gets the value of the payerPwd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayerPwd() {
        return payerPwd;
    }

    /**
     * Sets the value of the payerPwd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayerPwd(String value) {
        this.payerPwd = value;
    }

    /**
     * Gets the value of the customerCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * Sets the value of the customerCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerCode(String value) {
        this.customerCode = value;
    }

    /**
     * Gets the value of the customerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets the value of the customerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerName(String value) {
        this.customerName = value;
    }

    /**
     * Gets the value of the customerTel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerTel() {
        return customerTel;
    }

    /**
     * Sets the value of the customerTel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerTel(String value) {
        this.customerTel = value;
    }

    /**
     * Gets the value of the smartCardCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSmartCardCode() {
        return smartCardCode;
    }

    /**
     * Sets the value of the smartCardCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSmartCardCode(String value) {
        this.smartCardCode = value;
    }

    /**
     * Gets the value of the fee property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getFee() {
        return fee;
    }

    /**
     * Sets the value of the fee property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setFee(Double value) {
        this.fee = value;
    }

    /**
     * Gets the value of the receiptCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiptCode() {
        return receiptCode;
    }

    /**
     * Sets the value of the receiptCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiptCode(String value) {
        this.receiptCode = value;
    }

    /**
     * Gets the value of the deviceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * Sets the value of the deviceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeviceType(String value) {
        this.deviceType = value;
    }

    /**
     * Gets the value of the transferTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransferTime() {
        return transferTime;
    }

    /**
     * Sets the value of the transferTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransferTime(String value) {
        this.transferTime = value;
    }

}
