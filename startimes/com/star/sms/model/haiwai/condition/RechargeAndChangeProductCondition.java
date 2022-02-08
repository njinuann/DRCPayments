
package com.star.sms.model.haiwai.condition;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RechargeAndChangeProductCondition complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RechargeAndChangeProductCondition">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="fee" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="payerID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="payerPwd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="productNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "RechargeAndChangeProductCondition", propOrder = {
    "fee",
    "payerID",
    "payerPwd",
    "productNo",
    "smartCardCode",
    "transactionNo",
    "transferTime"
})
public class RechargeAndChangeProductCondition {

    @XmlElementRef(name = "fee", namespace = "http://condition.haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<Double> fee;
    @XmlElementRef(name = "payerID", namespace = "http://condition.haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> payerID;
    @XmlElementRef(name = "payerPwd", namespace = "http://condition.haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> payerPwd;
    @XmlElementRef(name = "productNo", namespace = "http://condition.haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> productNo;
    @XmlElementRef(name = "smartCardCode", namespace = "http://condition.haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> smartCardCode;
    @XmlElementRef(name = "transactionNo", namespace = "http://condition.haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> transactionNo;
    @XmlElementRef(name = "transferTime", namespace = "http://condition.haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> transferTime;

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
     * Gets the value of the productNo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getProductNo() {
        return productNo;
    }

    /**
     * Sets the value of the productNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setProductNo(JAXBElement<String> value) {
        this.productNo = value;
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
