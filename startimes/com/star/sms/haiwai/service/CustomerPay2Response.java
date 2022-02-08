
package com.star.sms.haiwai.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.star.sms.model.haiwai.CustomerPayResult2;


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
 *         &lt;element name="out" type="{http://haiwai.model.sms.star.com}CustomerPayResult2"/>
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
    "out"
})
@XmlRootElement(name = "customerPay2Response")
public class CustomerPay2Response {

    @XmlElement(required = true, nillable = true)
    protected CustomerPayResult2 out;

    /**
     * Gets the value of the out property.
     * 
     * @return
     *     possible object is
     *     {@link CustomerPayResult2 }
     *     
     */
    public CustomerPayResult2 getOut() {
        return out;
    }

    /**
     * Sets the value of the out property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomerPayResult2 }
     *     
     */
    public void setOut(CustomerPayResult2 value) {
        this.out = value;
    }

}
