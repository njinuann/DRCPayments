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
public class TvQueryRequest extends BaseRequest{
  
    private String DecoderNo;
    
    public String getDecoderNo() {
        return DecoderNo;
    }

    public void setDecoderNo(String DecoderNo) {
        this.DecoderNo = DecoderNo;
    }

}
