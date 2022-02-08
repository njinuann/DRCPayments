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
public class TVPaymentRequest extends BaseRequest{

    public String decoderNo;

    public String getDecoderNo() {
        return decoderNo;
    }

    public void setDecoderNo(String decoderNo) {
        this.decoderNo = decoderNo;
    }

}
