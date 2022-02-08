
package com.crm.beans.dto;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.crm.beans.dto package. 
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

    private final static QName _USSDApiResponseResultcode_QNAME = new QName("http://dto.beans.crm.com", "resultcode");
    private final static QName _USSDApiResponseData_QNAME = new QName("http://dto.beans.crm.com", "data");
    private final static QName _USSDApiResponseErrormessage_QNAME = new QName("http://dto.beans.crm.com", "errormessage");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.crm.beans.dto
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link USSDApiResponse }
     * 
     */
    public USSDApiResponse createUSSDApiResponse() {
        return new USSDApiResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dto.beans.crm.com", name = "resultcode", scope = USSDApiResponse.class)
    public JAXBElement<String> createUSSDApiResponseResultcode(String value) {
        return new JAXBElement<String>(_USSDApiResponseResultcode_QNAME, String.class, USSDApiResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dto.beans.crm.com", name = "data", scope = USSDApiResponse.class)
    public JAXBElement<Object> createUSSDApiResponseData(Object value) {
        return new JAXBElement<Object>(_USSDApiResponseData_QNAME, Object.class, USSDApiResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://dto.beans.crm.com", name = "errormessage", scope = USSDApiResponse.class)
    public JAXBElement<String> createUSSDApiResponseErrormessage(String value) {
        return new JAXBElement<String>(_USSDApiResponseErrormessage_QNAME, String.class, USSDApiResponse.class, value);
    }

}
