
package com.star.sms.model.haiwai;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SubscriberQueryResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SubscriberQueryResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="balance" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="canOrderProductInfos" type="{http://haiwai.model.sms.star.com}ArrayOfProductInfo" minOccurs="0"/>
 *         &lt;element name="customerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderedProductsDesc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="returnCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="returnMsg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="smartCardCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subscriberStatus" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="transactionlNo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubscriberQueryResult", propOrder = {
    "balance",
    "canOrderProductInfos",
    "customerName",
    "orderedProductsDesc",
    "returnCode",
    "returnMsg",
    "smartCardCode",
    "subscriberStatus",
    "transactionlNo"
})
public class SubscriberQueryResult {

    @XmlElementRef(name = "balance", namespace = "http://haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<Double> balance;
    @XmlElementRef(name = "canOrderProductInfos", namespace = "http://haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<ArrayOfProductInfo> canOrderProductInfos;
    @XmlElementRef(name = "customerName", namespace = "http://haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> customerName;
    @XmlElementRef(name = "orderedProductsDesc", namespace = "http://haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> orderedProductsDesc;
    @XmlElementRef(name = "returnCode", namespace = "http://haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> returnCode;
    @XmlElementRef(name = "returnMsg", namespace = "http://haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> returnMsg;
    @XmlElementRef(name = "smartCardCode", namespace = "http://haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> smartCardCode;
    @XmlElementRef(name = "subscriberStatus", namespace = "http://haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<Integer> subscriberStatus;
    @XmlElementRef(name = "transactionlNo", namespace = "http://haiwai.model.sms.star.com", type = JAXBElement.class, required = false)
    protected JAXBElement<String> transactionlNo;

    /**
     * Gets the value of the balance property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Double }{@code >}
     *     
     */
    public JAXBElement<Double> getBalance() {
        return balance;
    }

    /**
     * Sets the value of the balance property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Double }{@code >}
     *     
     */
    public void setBalance(JAXBElement<Double> value) {
        this.balance = value;
    }

    /**
     * Gets the value of the canOrderProductInfos property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfProductInfo }{@code >}
     *     
     */
    public JAXBElement<ArrayOfProductInfo> getCanOrderProductInfos() {
        return canOrderProductInfos;
    }

    /**
     * Sets the value of the canOrderProductInfos property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfProductInfo }{@code >}
     *     
     */
    public void setCanOrderProductInfos(JAXBElement<ArrayOfProductInfo> value) {
        this.canOrderProductInfos = value;
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
     * Gets the value of the orderedProductsDesc property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOrderedProductsDesc() {
        return orderedProductsDesc;
    }

    /**
     * Sets the value of the orderedProductsDesc property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOrderedProductsDesc(JAXBElement<String> value) {
        this.orderedProductsDesc = value;
    }

    /**
     * Gets the value of the returnCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getReturnCode() {
        return returnCode;
    }

    /**
     * Sets the value of the returnCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setReturnCode(JAXBElement<String> value) {
        this.returnCode = value;
    }

    /**
     * Gets the value of the returnMsg property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getReturnMsg() {
        return returnMsg;
    }

    /**
     * Sets the value of the returnMsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setReturnMsg(JAXBElement<String> value) {
        this.returnMsg = value;
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
     * Gets the value of the subscriberStatus property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public JAXBElement<Integer> getSubscriberStatus() {
        return subscriberStatus;
    }

    /**
     * Sets the value of the subscriberStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Integer }{@code >}
     *     
     */
    public void setSubscriberStatus(JAXBElement<Integer> value) {
        this.subscriberStatus = value;
    }

    /**
     * Gets the value of the transactionlNo property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getTransactionlNo() {
        return transactionlNo;
    }

    /**
     * Sets the value of the transactionlNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setTransactionlNo(JAXBElement<String> value) {
        this.transactionlNo = value;
    }

}
