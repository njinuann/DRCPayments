/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPayments;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 *
 * @author NJINU
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class TaxQueryRequest extends BaseRequest {

    public String NP_number;
    private Long taxTypeId;

    public String getNP_number() {
        return NP_number;
    }

    public void setNP_number(String NP_number) {
        this.NP_number = NP_number;
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

}
