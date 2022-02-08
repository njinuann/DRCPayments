
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
 *         &lt;element name="smartCardNum" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="payerCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="payerPwd" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "smartCardNum",
    "payerCode",
    "payerPwd"
})
@XmlRootElement(name = "validateAndQuerySubscriberInfo")
public class ValidateAndQuerySubscriberInfo {

    @XmlElement(required = true, nillable = true)
    protected String smartCardNum;
    @XmlElement(required = true, nillable = true)
    protected String payerCode;
    @XmlElement(required = true, nillable = true)
    protected String payerPwd;

    /**
     * Gets the value of the smartCardNum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSmartCardNum() {
        return smartCardNum;
    }

    /**
     * Sets the value of the smartCardNum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSmartCardNum(String value) {
        this.smartCardNum = value;
    }

    /**
     * Gets the value of the payerCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPayerCode() {
        return payerCode;
    }

    /**
     * Sets the value of the payerCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPayerCode(String value) {
        this.payerCode = value;
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

}
