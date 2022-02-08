
package com.star.sms.model.haiwai;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.star.sms.model.haiwai package. 
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

    private final static QName _ProductInfoProductNo_QNAME = new QName("http://haiwai.model.sms.star.com", "productNo");
    private final static QName _ProductInfoProductDesc_QNAME = new QName("http://haiwai.model.sms.star.com", "productDesc");
    private final static QName _SubscriberQueryResultCanOrderProductInfos_QNAME = new QName("http://haiwai.model.sms.star.com", "canOrderProductInfos");
    private final static QName _SubscriberQueryResultOrderedProductsDesc_QNAME = new QName("http://haiwai.model.sms.star.com", "orderedProductsDesc");
    private final static QName _SubscriberQueryResultCustomerName_QNAME = new QName("http://haiwai.model.sms.star.com", "customerName");
    private final static QName _SubscriberQueryResultReturnMsg_QNAME = new QName("http://haiwai.model.sms.star.com", "returnMsg");
    private final static QName _SubscriberQueryResultTransactionlNo_QNAME = new QName("http://haiwai.model.sms.star.com", "transactionlNo");
    private final static QName _SubscriberQueryResultReturnCode_QNAME = new QName("http://haiwai.model.sms.star.com", "returnCode");
    private final static QName _SubscriberQueryResultSubscriberStatus_QNAME = new QName("http://haiwai.model.sms.star.com", "subscriberStatus");
    private final static QName _SubscriberQueryResultBalance_QNAME = new QName("http://haiwai.model.sms.star.com", "balance");
    private final static QName _SubscriberQueryResultSmartCardCode_QNAME = new QName("http://haiwai.model.sms.star.com", "smartCardCode");
    private final static QName _CustomerPayResult2OrderCode_QNAME = new QName("http://haiwai.model.sms.star.com", "orderCode");
    private final static QName _CustomerPayDto2ReceiptCode_QNAME = new QName("http://haiwai.model.sms.star.com", "receiptCode");
    private final static QName _CustomerPayDto2PayerID_QNAME = new QName("http://haiwai.model.sms.star.com", "payerID");
    private final static QName _CustomerPayDto2TransactionNo_QNAME = new QName("http://haiwai.model.sms.star.com", "transactionNo");
    private final static QName _CustomerPayDto2CustomerCode_QNAME = new QName("http://haiwai.model.sms.star.com", "customerCode");
    private final static QName _CustomerPayDto2Email_QNAME = new QName("http://haiwai.model.sms.star.com", "email");
    private final static QName _CustomerPayDto2CustomerTel_QNAME = new QName("http://haiwai.model.sms.star.com", "customerTel");
    private final static QName _CustomerPayDto2DeviceType_QNAME = new QName("http://haiwai.model.sms.star.com", "deviceType");
    private final static QName _CustomerPayDto2PayerPwd_QNAME = new QName("http://haiwai.model.sms.star.com", "payerPwd");
    private final static QName _CustomerPayDto2Fee_QNAME = new QName("http://haiwai.model.sms.star.com", "fee");
    private final static QName _CustomerPayDto2TransferTime_QNAME = new QName("http://haiwai.model.sms.star.com", "transferTime");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.star.sms.model.haiwai
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CustomerPayDto2 }
     * 
     */
    public CustomerPayDto2 createCustomerPayDto2() {
        return new CustomerPayDto2();
    }

    /**
     * Create an instance of {@link ThirdPartnerRes }
     * 
     */
    public ThirdPartnerRes createThirdPartnerRes() {
        return new ThirdPartnerRes();
    }

    /**
     * Create an instance of {@link CustomerPayResult2 }
     * 
     */
    public CustomerPayResult2 createCustomerPayResult2() {
        return new CustomerPayResult2();
    }

    /**
     * Create an instance of {@link SubscriberQueryResult }
     * 
     */
    public SubscriberQueryResult createSubscriberQueryResult() {
        return new SubscriberQueryResult();
    }

    /**
     * Create an instance of {@link ProductInfo }
     * 
     */
    public ProductInfo createProductInfo() {
        return new ProductInfo();
    }

    /**
     * Create an instance of {@link ArrayOfProductInfo }
     * 
     */
    public ArrayOfProductInfo createArrayOfProductInfo() {
        return new ArrayOfProductInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "productNo", scope = ProductInfo.class)
    public JAXBElement<String> createProductInfoProductNo(String value) {
        return new JAXBElement<String>(_ProductInfoProductNo_QNAME, String.class, ProductInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "productDesc", scope = ProductInfo.class)
    public JAXBElement<String> createProductInfoProductDesc(String value) {
        return new JAXBElement<String>(_ProductInfoProductDesc_QNAME, String.class, ProductInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfProductInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "canOrderProductInfos", scope = SubscriberQueryResult.class)
    public JAXBElement<ArrayOfProductInfo> createSubscriberQueryResultCanOrderProductInfos(ArrayOfProductInfo value) {
        return new JAXBElement<ArrayOfProductInfo>(_SubscriberQueryResultCanOrderProductInfos_QNAME, ArrayOfProductInfo.class, SubscriberQueryResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "orderedProductsDesc", scope = SubscriberQueryResult.class)
    public JAXBElement<String> createSubscriberQueryResultOrderedProductsDesc(String value) {
        return new JAXBElement<String>(_SubscriberQueryResultOrderedProductsDesc_QNAME, String.class, SubscriberQueryResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "customerName", scope = SubscriberQueryResult.class)
    public JAXBElement<String> createSubscriberQueryResultCustomerName(String value) {
        return new JAXBElement<String>(_SubscriberQueryResultCustomerName_QNAME, String.class, SubscriberQueryResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "returnMsg", scope = SubscriberQueryResult.class)
    public JAXBElement<String> createSubscriberQueryResultReturnMsg(String value) {
        return new JAXBElement<String>(_SubscriberQueryResultReturnMsg_QNAME, String.class, SubscriberQueryResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "transactionlNo", scope = SubscriberQueryResult.class)
    public JAXBElement<String> createSubscriberQueryResultTransactionlNo(String value) {
        return new JAXBElement<String>(_SubscriberQueryResultTransactionlNo_QNAME, String.class, SubscriberQueryResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "returnCode", scope = SubscriberQueryResult.class)
    public JAXBElement<String> createSubscriberQueryResultReturnCode(String value) {
        return new JAXBElement<String>(_SubscriberQueryResultReturnCode_QNAME, String.class, SubscriberQueryResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "subscriberStatus", scope = SubscriberQueryResult.class)
    public JAXBElement<Integer> createSubscriberQueryResultSubscriberStatus(Integer value) {
        return new JAXBElement<Integer>(_SubscriberQueryResultSubscriberStatus_QNAME, Integer.class, SubscriberQueryResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "balance", scope = SubscriberQueryResult.class)
    public JAXBElement<Double> createSubscriberQueryResultBalance(Double value) {
        return new JAXBElement<Double>(_SubscriberQueryResultBalance_QNAME, Double.class, SubscriberQueryResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "smartCardCode", scope = SubscriberQueryResult.class)
    public JAXBElement<String> createSubscriberQueryResultSmartCardCode(String value) {
        return new JAXBElement<String>(_SubscriberQueryResultSmartCardCode_QNAME, String.class, SubscriberQueryResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "orderCode", scope = CustomerPayResult2 .class)
    public JAXBElement<String> createCustomerPayResult2OrderCode(String value) {
        return new JAXBElement<String>(_CustomerPayResult2OrderCode_QNAME, String.class, CustomerPayResult2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "returnMsg", scope = CustomerPayResult2 .class)
    public JAXBElement<String> createCustomerPayResult2ReturnMsg(String value) {
        return new JAXBElement<String>(_SubscriberQueryResultReturnMsg_QNAME, String.class, CustomerPayResult2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "transactionlNo", scope = CustomerPayResult2 .class)
    public JAXBElement<String> createCustomerPayResult2TransactionlNo(String value) {
        return new JAXBElement<String>(_SubscriberQueryResultTransactionlNo_QNAME, String.class, CustomerPayResult2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "returnCode", scope = CustomerPayResult2 .class)
    public JAXBElement<String> createCustomerPayResult2ReturnCode(String value) {
        return new JAXBElement<String>(_SubscriberQueryResultReturnCode_QNAME, String.class, CustomerPayResult2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "receiptCode", scope = CustomerPayDto2 .class)
    public JAXBElement<String> createCustomerPayDto2ReceiptCode(String value) {
        return new JAXBElement<String>(_CustomerPayDto2ReceiptCode_QNAME, String.class, CustomerPayDto2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "payerID", scope = CustomerPayDto2 .class)
    public JAXBElement<String> createCustomerPayDto2PayerID(String value) {
        return new JAXBElement<String>(_CustomerPayDto2PayerID_QNAME, String.class, CustomerPayDto2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "transactionNo", scope = CustomerPayDto2 .class)
    public JAXBElement<String> createCustomerPayDto2TransactionNo(String value) {
        return new JAXBElement<String>(_CustomerPayDto2TransactionNo_QNAME, String.class, CustomerPayDto2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "customerCode", scope = CustomerPayDto2 .class)
    public JAXBElement<String> createCustomerPayDto2CustomerCode(String value) {
        return new JAXBElement<String>(_CustomerPayDto2CustomerCode_QNAME, String.class, CustomerPayDto2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "customerName", scope = CustomerPayDto2 .class)
    public JAXBElement<String> createCustomerPayDto2CustomerName(String value) {
        return new JAXBElement<String>(_SubscriberQueryResultCustomerName_QNAME, String.class, CustomerPayDto2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "email", scope = CustomerPayDto2 .class)
    public JAXBElement<String> createCustomerPayDto2Email(String value) {
        return new JAXBElement<String>(_CustomerPayDto2Email_QNAME, String.class, CustomerPayDto2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "customerTel", scope = CustomerPayDto2 .class)
    public JAXBElement<String> createCustomerPayDto2CustomerTel(String value) {
        return new JAXBElement<String>(_CustomerPayDto2CustomerTel_QNAME, String.class, CustomerPayDto2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "deviceType", scope = CustomerPayDto2 .class)
    public JAXBElement<String> createCustomerPayDto2DeviceType(String value) {
        return new JAXBElement<String>(_CustomerPayDto2DeviceType_QNAME, String.class, CustomerPayDto2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "payerPwd", scope = CustomerPayDto2 .class)
    public JAXBElement<String> createCustomerPayDto2PayerPwd(String value) {
        return new JAXBElement<String>(_CustomerPayDto2PayerPwd_QNAME, String.class, CustomerPayDto2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "fee", scope = CustomerPayDto2 .class)
    public JAXBElement<Double> createCustomerPayDto2Fee(Double value) {
        return new JAXBElement<Double>(_CustomerPayDto2Fee_QNAME, Double.class, CustomerPayDto2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "smartCardCode", scope = CustomerPayDto2 .class)
    public JAXBElement<String> createCustomerPayDto2SmartCardCode(String value) {
        return new JAXBElement<String>(_SubscriberQueryResultSmartCardCode_QNAME, String.class, CustomerPayDto2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "transferTime", scope = CustomerPayDto2 .class)
    public JAXBElement<String> createCustomerPayDto2TransferTime(String value) {
        return new JAXBElement<String>(_CustomerPayDto2TransferTime_QNAME, String.class, CustomerPayDto2 .class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "returnMsg", scope = ThirdPartnerRes.class)
    public JAXBElement<String> createThirdPartnerResReturnMsg(String value) {
        return new JAXBElement<String>(_SubscriberQueryResultReturnMsg_QNAME, String.class, ThirdPartnerRes.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "transactionlNo", scope = ThirdPartnerRes.class)
    public JAXBElement<String> createThirdPartnerResTransactionlNo(String value) {
        return new JAXBElement<String>(_SubscriberQueryResultTransactionlNo_QNAME, String.class, ThirdPartnerRes.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://haiwai.model.sms.star.com", name = "returnCode", scope = ThirdPartnerRes.class)
    public JAXBElement<String> createThirdPartnerResReturnCode(String value) {
        return new JAXBElement<String>(_SubscriberQueryResultReturnCode_QNAME, String.class, ThirdPartnerRes.class, value);
    }

}
