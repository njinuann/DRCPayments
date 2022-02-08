
package com.star.sms.model.haiwai.condition;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.star.sms.model.haiwai.condition package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RechargeAndChangeProductConditionPayerID_QNAME = new QName("http://condition.haiwai.model.sms.star.com", "payerID");
    private final static QName _RechargeAndChangeProductConditionTransactionNo_QNAME = new QName("http://condition.haiwai.model.sms.star.com", "transactionNo");
    private final static QName _RechargeAndChangeProductConditionProductNo_QNAME = new QName("http://condition.haiwai.model.sms.star.com", "productNo");
    private final static QName _RechargeAndChangeProductConditionSmartCardCode_QNAME = new QName("http://condition.haiwai.model.sms.star.com", "smartCardCode");
    private final static QName _RechargeAndChangeProductConditionTransferTime_QNAME = new QName("http://condition.haiwai.model.sms.star.com", "transferTime");
    private final static QName _RechargeAndChangeProductConditionPayerPwd_QNAME = new QName("http://condition.haiwai.model.sms.star.com", "payerPwd");
    private final static QName _RechargeAndChangeProductConditionFee_QNAME = new QName("http://condition.haiwai.model.sms.star.com", "fee");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.star.sms.model.haiwai.condition
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RechargeAndChangeProductCondition }
     * 
     */
    public RechargeAndChangeProductCondition createRechargeAndChangeProductCondition() {
        return new RechargeAndChangeProductCondition();
    }

    /**
     * Create an instance of {@link SubscriberQueryCondition }
     * 
     */
    public SubscriberQueryCondition createSubscriberQueryCondition() {
        return new SubscriberQueryCondition();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://condition.haiwai.model.sms.star.com", name = "payerID", scope = RechargeAndChangeProductCondition.class)
    public JAXBElement<String> createRechargeAndChangeProductConditionPayerID(String value) {
        return new JAXBElement<String>(_RechargeAndChangeProductConditionPayerID_QNAME, String.class, RechargeAndChangeProductCondition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://condition.haiwai.model.sms.star.com", name = "transactionNo", scope = RechargeAndChangeProductCondition.class)
    public JAXBElement<String> createRechargeAndChangeProductConditionTransactionNo(String value) {
        return new JAXBElement<String>(_RechargeAndChangeProductConditionTransactionNo_QNAME, String.class, RechargeAndChangeProductCondition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://condition.haiwai.model.sms.star.com", name = "productNo", scope = RechargeAndChangeProductCondition.class)
    public JAXBElement<String> createRechargeAndChangeProductConditionProductNo(String value) {
        return new JAXBElement<String>(_RechargeAndChangeProductConditionProductNo_QNAME, String.class, RechargeAndChangeProductCondition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://condition.haiwai.model.sms.star.com", name = "smartCardCode", scope = RechargeAndChangeProductCondition.class)
    public JAXBElement<String> createRechargeAndChangeProductConditionSmartCardCode(String value) {
        return new JAXBElement<String>(_RechargeAndChangeProductConditionSmartCardCode_QNAME, String.class, RechargeAndChangeProductCondition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://condition.haiwai.model.sms.star.com", name = "transferTime", scope = RechargeAndChangeProductCondition.class)
    public JAXBElement<String> createRechargeAndChangeProductConditionTransferTime(String value) {
        return new JAXBElement<String>(_RechargeAndChangeProductConditionTransferTime_QNAME, String.class, RechargeAndChangeProductCondition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://condition.haiwai.model.sms.star.com", name = "payerPwd", scope = RechargeAndChangeProductCondition.class)
    public JAXBElement<String> createRechargeAndChangeProductConditionPayerPwd(String value) {
        return new JAXBElement<String>(_RechargeAndChangeProductConditionPayerPwd_QNAME, String.class, RechargeAndChangeProductCondition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://condition.haiwai.model.sms.star.com", name = "fee", scope = RechargeAndChangeProductCondition.class)
    public JAXBElement<Double> createRechargeAndChangeProductConditionFee(Double value) {
        return new JAXBElement<Double>(_RechargeAndChangeProductConditionFee_QNAME, Double.class, RechargeAndChangeProductCondition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://condition.haiwai.model.sms.star.com", name = "payerID", scope = SubscriberQueryCondition.class)
    public JAXBElement<String> createSubscriberQueryConditionPayerID(String value) {
        return new JAXBElement<String>(_RechargeAndChangeProductConditionPayerID_QNAME, String.class, SubscriberQueryCondition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://condition.haiwai.model.sms.star.com", name = "transactionNo", scope = SubscriberQueryCondition.class)
    public JAXBElement<String> createSubscriberQueryConditionTransactionNo(String value) {
        return new JAXBElement<String>(_RechargeAndChangeProductConditionTransactionNo_QNAME, String.class, SubscriberQueryCondition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://condition.haiwai.model.sms.star.com", name = "smartCardCode", scope = SubscriberQueryCondition.class)
    public JAXBElement<String> createSubscriberQueryConditionSmartCardCode(String value) {
        return new JAXBElement<String>(_RechargeAndChangeProductConditionSmartCardCode_QNAME, String.class, SubscriberQueryCondition.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://condition.haiwai.model.sms.star.com", name = "payerPwd", scope = SubscriberQueryCondition.class)
    public JAXBElement<String> createSubscriberQueryConditionPayerPwd(String value) {
        return new JAXBElement<String>(_RechargeAndChangeProductConditionPayerPwd_QNAME, String.class, SubscriberQueryCondition.class, value);
    }

}
