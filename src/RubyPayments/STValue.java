/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPayments;

import java.util.ArrayList;

/**
 *
 * @author NJINU
 */
public class STValue {

    public String custName;
    public String custcode;
    public String custTel;
    public String payerId;
    public String custRecCode;
    public String tranNo;
    public String devType;
    public String payerPwd;
    public String smartCardno;
    public String email;
    public Double fee;
    public int count;
//    private ArrayList<TLLabel> labels = new ArrayList<>();

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustcode() {
        return custcode;
    }

    public void setCustcode(String custcode) {
        this.custcode = custcode;
    }

    public String getCustTel() {
        return custTel;
    }

    public void setCustTel(String custTel) {
        this.custTel = custTel;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getCustRecCode() {
        return custRecCode;
    }

    public void setCustRecCode(String custRecCode) {
        this.custRecCode = custRecCode;
    }

    public String getTranNo() {
        return tranNo;
    }

    public void setTranNo(String tranNo) {
        this.tranNo = tranNo;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getPayerPwd() {
        return payerPwd;
    }

    public void setPayerPwd(String payerPwd) {
        this.payerPwd = payerPwd;
    }

    public String getSmartCardno() {
        return smartCardno;
    }

    public void setSmartCardno(String smartCardno) {
        this.smartCardno = smartCardno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

//    public ArrayList<TLLabel> getLabels() {
//        return labels;
//    }
//
//    public void setLabels(ArrayList<TLLabel> labels) {
//        this.labels = labels;
//    }
    
    
    
}
