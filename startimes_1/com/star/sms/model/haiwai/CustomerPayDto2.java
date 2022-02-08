
package com.star.sms.model.haiwai;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CustomerPayDto2 complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CustomerPayDto2">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="customerCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerTel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deviceType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fee" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="payerID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="payerPwd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="receiptCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="smartCardCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transactionNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transferTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CustomerPayDto2", propOrder = {
    "customerCode",
    "customerName",
    "customerTel",
    "deviceType",
    "email",
    "fee",
    "payerID",
    "payerPwd",
    "receiptCode",
    "smartCardCode",
    "transactionNo",
    "transferTime"
})
public class CustomerPayDto2 {

    @XmlElementRef(name = "customerCode", namespace = "http://haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> customerCode;
    @XmlElementRef(name = "customerName", namespace = "http://haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> customerName;
    @XmlElementRef(name = "customerTel", namespace = "http://haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> customerTel;
    @XmlElementRef(name = "deviceType", namespace = "http://haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> deviceType;
    @XmlElementRef(name = "email", namespace = "http://haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> email;
    @XmlElementRef(name = "fee", namespace = "http://haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<Double> fee;
    @XmlElementRef(name = "payerID", namespace = "http://haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> payerID;
    @XmlElementRef(name = "payerPwd", namespace = "http://haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> payerPwd;
    @XmlElementRef(name = "receiptCode", namespace = "http://haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> receiptCode;
    @XmlElementRef(name = "smartCardCode", namespace = "http://haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> smartCardCode;
    @XmlElementRef(name = "transactionNo", namespace = "http://haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> transactionNo;
    @XmlElementRef(name = "transferTime", namespace = "http://haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> transferTime;

    /**
     * Gets the value of the customerCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCustomerCode() {
        return customerCode;
    }

    /**
     * Sets the value of the customerCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCustomerCode(JAXBElement<String> value) {
        this.customerCode = value;
    }

    /**
     * Gets the value of the customerName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCustomerName() {
        return customerName;
    }

    /**
     * Sets the value of the customerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCustomerName(JAXBElement<String> value) {
        this.customerName = value;
    }

    /**
     * Gets the value of the customerTel property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCustomerTel() {
        return customerTel;
    }

    /**
     * Sets the value of the customerTel property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCustomerTel(JAXBElement<String> value) {
        this.customerTel = value;
    }

    /**
     * Gets the value of the deviceType property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getDeviceType() {
        return deviceType;
    }

    /**
     * Sets the value of the deviceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setDeviceType(JAXBElement<String> value) {
        this.deviceType = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEmail(JAXBElement<String> value) {
        this.email = value;
    }

    /**
     * Gets the value of the fee property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Double }{@code >}
     *     
     */
    public JAXBElement<Double> getFee() {
        return fee;
    }

    /**
     * Sets the value of the fee property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Double }{@code >}
     *     
     */
    public void setFee(JAXBElement<Double> value) {
        this.fee = value;
    }

    /**
     * Gets the value of the payerID property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPayerID() {
        return payerID;
    }

    /**
     * Sets the value of the payerID property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPayerID(JAXBElement<String> value) {
        this.payerID = value;
    }

    /**
     * Gets the value of the payerPwd property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getPayerPwd() {
        return payerPwd;
    }

    /**
     * Sets the value of the payerPwd property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setPayerPwd(JAXBElement<String> value) {
        this.payerPwd = value;
    }

    /**
     * Gets the value of the receiptCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getReceiptCode() {
        return receiptCode;
    }

    /**
     * Sets the value of the receiptCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setReceiptCode(JAXBElement<String> value) {
        this.receiptCode = value;
    }

    /**
     * Gets the value of the smartCardCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSmartCardCode() {
        return smartCardCode;
    }

    /**
     * Sets the value of the smartCardCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSmartCardCode(JAXBElement<String> value) {
        this.smartCardCode = value;
    }

    /**
     * Gets the value of the transactionNo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTransactionNo() {
        return transactionNo;
    }

    /**
     * Sets the value of the transactionNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTransactionNo(JAXBElement<String> value) {
        this.transactionNo = value;
    }

    /**
     * Gets the value of the transferTime property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTransferTime() {
        return transferTime;
    }

    /**
     * Sets the value of the transferTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTransferTime(JAXBElement<String> value) {
        this.transferTime = value;
    }

}
