
package com.star.sms.service.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.star.sms.service.model package. 
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

    private final static QName _BalanceInfoBalance_QNAME = new QName("http://model.service.sms.star.com", "balance");
    private final static QName _BalanceInfoSmartCardCode_QNAME = new QName("http://model.service.sms.star.com", "smartCardCode");
    private final static QName _BalanceInfoTELDealID_QNAME = new QName("http://model.service.sms.star.com", "TELDealID");
    private final static QName _BalanceInfoReturnCode_QNAME = new QName("http://model.service.sms.star.com", "returnCode");
    private final static QName _BalanceInfoBillAmount_QNAME = new QName("http://model.service.sms.star.com", "billAmount");
    private final static QName _BalanceInfoReturnMsg_QNAME = new QName("http://model.service.sms.star.com", "returnMsg");
    private final static QName _BalanceInfoPayType_QNAME = new QName("http://model.service.sms.star.com", "payType");
    private final static QName _BalanceInfoCustomerName_QNAME = new QName("http://model.service.sms.star.com", "customerName");
    private final static QName _BalanceInfoCustomerCode_QNAME = new QName("http://model.service.sms.star.com", "customerCode");
    private final static QName _CustomerPayResultFee_QNAME = new QName("http://model.service.sms.star.com", "fee");
    private final static QName _CustomerPayResultCustomerContectTel_QNAME = new QName("http://model.service.sms.star.com", "customerContectTel");
    private final static QName _CustomerPayResultOrderCode_QNAME = new QName("http://model.service.sms.star.com", "orderCode");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.star.sms.service.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BalanceInfo }
     * 
     */
    public BalanceInfo createBalanceInfo() {
        return new BalanceInfo();
    }

    /**
     * Create an instance of {@link CustomerPayResult }
     * 
     */
    public CustomerPayResult createCustomerPayResult() {
        return new CustomerPayResult();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.service.sms.star.com", name = "balance", scope = BalanceInfo.class)
    public JAXBElement<Double> createBalanceInfoBalance(Double value) {
        return new JAXBElement<Double>(_BalanceInfoBalance_QNAME, Double.class, BalanceInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.service.sms.star.com", name = "smartCardCode", scope = BalanceInfo.class)
    public JAXBElement<String> createBalanceInfoSmartCardCode(String value) {
        return new JAXBElement<String>(_BalanceInfoSmartCardCode_QNAME, String.class, BalanceInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.service.sms.star.com", name = "TELDealID", scope = BalanceInfo.class)
    public JAXBElement<String> createBalanceInfoTELDealID(String value) {
        return new JAXBElement<String>(_BalanceInfoTELDealID_QNAME, String.class, BalanceInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.service.sms.star.com", name = "returnCode", scope = BalanceInfo.class)
    public JAXBElement<String> createBalanceInfoReturnCode(String value) {
        return new JAXBElement<String>(_BalanceInfoReturnCode_QNAME, String.class, BalanceInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.service.sms.star.com", name = "billAmount", scope = BalanceInfo.class)
    public JAXBElement<Double> createBalanceInfoBillAmount(Double value) {
        return new JAXBElement<Double>(_BalanceInfoBillAmount_QNAME, Double.class, BalanceInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.service.sms.star.com", name = "returnMsg", scope = BalanceInfo.class)
    public JAXBElement<String> createBalanceInfoReturnMsg(String value) {
        return new JAXBElement<String>(_BalanceInfoReturnMsg_QNAME, String.class, BalanceInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.service.sms.star.com", name = "payType", scope = BalanceInfo.class)
    public JAXBElement<Integer> createBalanceInfoPayType(Integer value) {
        return new JAXBElement<Integer>(_BalanceInfoPayType_QNAME, Integer.class, BalanceInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.service.sms.star.com", name = "customerName", scope = BalanceInfo.class)
    public JAXBElement<String> createBalanceInfoCustomerName(String value) {
        return new JAXBElement<String>(_BalanceInfoCustomerName_QNAME, String.class, BalanceInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.service.sms.star.com", name = "customerCode", scope = BalanceInfo.class)
    public JAXBElement<String> createBalanceInfoCustomerCode(String value) {
        return new JAXBElement<String>(_BalanceInfoCustomerCode_QNAME, String.class, BalanceInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.service.sms.star.com", name = "balance", scope = CustomerPayResult.class)
    public JAXBElement<Double> createCustomerPayResultBalance(Double value) {
        return new JAXBElement<Double>(_BalanceInfoBalance_QNAME, Double.class, CustomerPayResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.service.sms.star.com", name = "smartCardCode", scope = CustomerPayResult.class)
    public JAXBElement<String> createCustomerPayResultSmartCardCode(String value) {
        return new JAXBElement<String>(_BalanceInfoSmartCardCode_QNAME, String.class, CustomerPayResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.service.sms.star.com", name = "TELDealID", scope = CustomerPayResult.class)
    public JAXBElement<String> createCustomerPayResultTELDealID(String value) {
        return new JAXBElement<String>(_BalanceInfoTELDealID_QNAME, String.class, CustomerPayResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.service.sms.star.com", name = "fee", scope = CustomerPayResult.class)
    public JAXBElement<Double> createCustomerPayResultFee(Double value) {
        return new JAXBElement<Double>(_CustomerPayResultFee_QNAME, Double.class, CustomerPayResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.service.sms.star.com", name = "returnCode", scope = CustomerPayResult.class)
    public JAXBElement<String> createCustomerPayResultReturnCode(String value) {
        return new JAXBElement<String>(_BalanceInfoReturnCode_QNAME, String.class, CustomerPayResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.service.sms.star.com", name = "returnMsg", scope = CustomerPayResult.class)
    public JAXBElement<String> createCustomerPayResultReturnMsg(String value) {
        return new JAXBElement<String>(_BalanceInfoReturnMsg_QNAME, String.class, CustomerPayResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.service.sms.star.com", name = "customerName", scope = CustomerPayResult.class)
    public JAXBElement<String> createCustomerPayResultCustomerName(String value) {
        return new JAXBElement<String>(_BalanceInfoCustomerName_QNAME, String.class, CustomerPayResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.service.sms.star.com", name = "customerContectTel", scope = CustomerPayResult.class)
    public JAXBElement<String> createCustomerPayResultCustomerContectTel(String value) {
        return new JAXBElement<String>(_CustomerPayResultCustomerContectTel_QNAME, String.class, CustomerPayResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.service.sms.star.com", name = "customerCode", scope = CustomerPayResult.class)
    public JAXBElement<String> createCustomerPayResultCustomerCode(String value) {
        return new JAXBElement<String>(_BalanceInfoCustomerCode_QNAME, String.class, CustomerPayResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://model.service.sms.star.com", name = "orderCode", scope = CustomerPayResult.class)
    public JAXBElement<String> createCustomerPayResultOrderCode(String value) {
        return new JAXBElement<String>(_CustomerPayResultOrderCode_QNAME, String.class, CustomerPayResult.class, value);
    }

}
