
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
 *         &lt;element name="smartCardCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="customerPwd" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="deviceType" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "smartCardCode",
    "customerPwd",
    "deviceType"
})
@XmlRootElement(name = "queryBalance")
public class QueryBalance {

    @XmlElement(required = true, nillable = true)
    protected String transactionNo;
    @XmlElement(required = true, nillable = true)
    protected String payerID;
    @XmlElement(required = true, nillable = true)
    protected String payerPwd;
    @XmlElement(required = true, nillable = true)
    protected String customerCode;
    @XmlElement(required = true, nillable = true)
    protected String smartCardCode;
    @XmlElement(required = true, nillable = true)
    protected String customerPwd;
    @XmlElement(required = true, nillable = true)
    protected String deviceType;

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
     * Gets the value of the customerPwd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerPwd() {
        return customerPwd;
    }

    /**
     * Sets the value of the customerPwd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerPwd(String value) {
        this.customerPwd = value;
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

}
