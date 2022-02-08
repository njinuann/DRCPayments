/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPaymentWS;

import RubyPayments.BaseRequest;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author NJINU
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TaxPaymentRequest extends BaseRequest {

    public String NPNumber;
    private String taxTypeName;
    private Long taxTypeId;

    public String getNPNumber() {
        return NPNumber;
    }

    public void setNPNumber(String NPNumber) {
        this.NPNumber = NPNumber;
    }

    /**
     * @return the taxTypeId
     */
    public Long getTaxTypeId() {
        return taxTypeId;
    }

    /**
     * @param taxTypeId the taxTypeId to set
     */
    public void setTaxTypeId(Long taxTypeId) {
        this.taxTypeId = taxTypeId;
    }

    /**
     * @return the taxTypeName
     */
    public String getTaxTypeName() {
        return taxTypeName;
    }

    /**
     * @param taxTypeName the taxTypeName to set
     */
    public void setTaxTypeName(String taxTypeName) {
        this.taxTypeName = taxTypeName;
    }

}
