
package com.star.sms.haiwai.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.star.sms.model.haiwai.ThirdPartnerRes;


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
 *         &lt;element name="out" type="{http://haiwai.model.sms.star.com}ThirdPartnerRes"/>
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
@XmlRootElement(name = "rechargeAndChangeProductResponse")
public class RechargeAndChangeProductResponse {

    @XmlElement(required = true, nillable = true)
    protected ThirdPartnerRes out;

    /**
     * Gets the value of the out property.
     * 
     * @return
     *     possible object is
     *     {@link ThirdPartnerRes }
     *     
     */
    public ThirdPartnerRes getOut() {
        return out;
    }

    /**
     * Sets the value of the out property.
     * 
     * @param value
     *     allowed object is
     *     {@link ThirdPartnerRes }
     *     
     */
    public void setOut(ThirdPartnerRes value) {
        this.out = value;
    }

}
