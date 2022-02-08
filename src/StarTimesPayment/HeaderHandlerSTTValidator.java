/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StarTimesPayment;

/**
 *
 * @author NJINU
 */
//import RubyExtPay.BillUploadMain;
import RubyPayments.ExtPayMain;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import RubyPayments.TXUtility;
import RubyPayments.TvResponse;
import javax.xml.soap.SOAPException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.DOMException;

public class HeaderHandlerSTTValidator implements SOAPHandler<SOAPMessageContext>
{

    TXUtility util = new TXUtility();
    public static TvResponse cNCustProf = new TvResponse();
    public java.util.Date timestamp = new java.util.Date();
    public SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:sss");
    private String timenow = null;
    String formatedtime = formatter.format(timestamp);

    @Override
    public boolean handleMessage(SOAPMessageContext smc)
    {

        /*
         <soapenv:Header>
         <CALLCENTER_USERNAME xmlns="NAMESPACE_STARSMS">StarCallCenter</CALLCENTER_USERNAME>
         <CALLCENTER_PASSWORD xmlns="NAMESPACE_STARSMS">StarCallCenter</CALLCENTER_PASSWORD>
         <CALLCENTER_VERSION xmlns="NAMESPACE_STARSMS">1.5</CALLCENTER_VERSION>
         </soapenv:Header>
         */
        System.err.println("show property " + MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outboundProperty.booleanValue())
        {

            String NAMESPACE_STARSMS = "NAMESPACE_STARSMS";

            try
            {
              //  MessageFactory messageFactory = MessageFactory.newInstance();
                SOAPMessage soapMessage = smc.getMessage();

                SOAPPart soapPart = (SOAPPart) soapMessage.getSOAPPart();
                // SOAP Envelope
                SOAPEnvelope envelope = soapPart.getEnvelope();
                //Soap Header
                SOAPHeader soapheader = envelope.getHeader() != null ? envelope.getHeader() : envelope.addHeader();

//                envelope.removeNamespaceDeclaration("SOAP-ENV");
//                envelope.setPrefix("soapenv");
                Name headerContextName = envelope.createName(NAMESPACE_STARSMS, "CALLCENTER_USERNAME", "http://service.haiwai.sms.star.com");
                SOAPHeaderElement soapHeaderElement = soapheader.addHeaderElement(headerContextName);
                soapHeaderElement.setMustUnderstand(false);

                SOAPElement hUsername = soapheader.addChildElement(new QName(NAMESPACE_STARSMS, "CALLCENTER_USERNAME"));
                hUsername.addTextNode("StarCallCenter");

                SOAPElement hPassword = soapheader.addChildElement(new QName(NAMESPACE_STARSMS, "CALLCENTER_PASSWORD"));
                hPassword.addTextNode("StarCallCenter");

                SOAPElement hVersion = soapheader.addChildElement(new QName(NAMESPACE_STARSMS, "CALLCENTER_VERSION"));
                hVersion.addTextNode("1.5");

                soapMessage.saveChanges();

                StringWriter out = new StringWriter();
                Source xmlInput = soapMessage.getSOAPPart().getContent();

                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                //        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.transform(xmlInput, new StreamResult(out));
                System.out.println(out);
            }
            catch (SOAPException | DOMException | IllegalArgumentException | TransformerException e)
            {

                ExtPayMain.bRLogger.logError("handleMessage-ERROR", e);

            }

        }
        else
        {
            try
            {

                SOAPMessage message = smc.getMessage();
                System.out.println("in the try " + message.getSOAPBody().getAttribute("returnCode"));

                StringWriter out = new StringWriter();
                Source xmlInput = message.getSOAPPart().getContent();
                
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                //        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.transform(xmlInput, new StreamResult(out));
               
                System.out.println(out);              
                String resp = message.getSOAPBody().getElementsByTagName("returnCode").item(0).getFirstChild().getNodeValue();
                
                System.err.println("RESPONSE CODE   " + resp);
                if (resp.equals("0") || resp.equals("00"))
                {
                    String respmsg = message.getSOAPBody().getElementsByTagName("returnMsg").item(0).getFirstChild().getNodeValue();
                    String resptranNo = message.getSOAPBody().getElementsByTagName("transactionlNo").item(0).getFirstChild().getNodeValue();

                    String customerName = message.getSOAPBody().getElementsByTagName("customerName").item(0).getFirstChild().getNodeValue();
                    String orderedProductsDesc = message.getSOAPBody().getElementsByTagName("orderedProductsDesc").item(0).getFirstChild().getNodeValue();

                    String smartCardCode = message.getSOAPBody().getElementsByTagName("smartCardCode").item(0).getFirstChild().getNodeValue();
                    String balance = message.getSOAPBody().getElementsByTagName("balance").item(0).getFirstChild().getNodeValue();

                    String subscriberStatus = message.getSOAPBody().getElementsByTagName("subscriberStatus").item(0).getFirstChild().getNodeValue();

                    timestamp = new java.util.Date();
                    timenow = formatter.format(timestamp);

                    System.out.println("updating the Startimes\n customerName ==> " + customerName
                            + "\n smartCardCode  ==> " + smartCardCode
                            + "\n balance  ==> " + balance
                            + "\n orderedProductsDesc   ==> " + orderedProductsDesc
                            + "\n subscriberStatus   ==> " + subscriberStatus
                            + "\n resptranNo   ==> " + resptranNo
                            + "\n returnCode   ==> " + resp
                            + "\n respmsg   ==> " + respmsg);

                    cNCustProf.setAccountName(customerName);
                    cNCustProf.setReference(smartCardCode);
                    cNCustProf.setTvType("STARTIMES");

                    cNCustProf.setChanellId(8L);
                    cNCustProf.setPaymentType(orderedProductsDesc);
                    cNCustProf.setCurrency("CDF");
                    
                    cNCustProf.setRespCode(resp);

                }
                else
                {
                    cNCustProf.setAccountName("Not Found");
                    cNCustProf.setReference("Not Found");
                    cNCustProf.setTvType("Not Found");

                    cNCustProf.setChanellId(8L);
                    cNCustProf.setPaymentType("Not Found");
                    cNCustProf.setCurrency("CDF");
                    cNCustProf.setRespCode("EICODE_0006");
                }
                System.out.println("Start Times Service processing.. [" + ("0".equals(resp) ? "sucesss" : "fail ]"));
            }
            catch (SOAPException | IllegalArgumentException | TransformerException | DOMException ex)
            {
                ExtPayMain.bRLogger.logError("ERROR", ex);

            }
        }

        return outboundProperty;

    }

    @Override
    public Set getHeaders()
    {
        // The code below is added on order to invoke Spring secured WS.
        // Otherwise,
        // http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd
        // won't be recognised 
        final QName securityHeader = new QName(
                "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd",
                "Security", "wsse");

        final HashSet headers = new HashSet();
        headers.add(securityHeader);

        return headers;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context)
    {
        //throw new UnsupportedOperationException("Not supported yet.");
        return true;
    }

    @Override
    public void close(MessageContext context)
    {
//throw new UnsupportedOperationException("Not supported yet.");
    }

}
