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
import javax.xml.soap.SOAPException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.DOMException;

public class HeaderHandler implements SOAPHandler<SOAPMessageContext>
{

    TXUtility util = new TXUtility();
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

        if (outboundProperty)
        {
            String NAMESPACE_STARSMS = "NAMESPACE_STARSMS";
            try
            {
                //  MessageFactory messageFactory = MessageFactory.newInstance();
                SOAPMessage soapMessage = smc.getMessage();

                SOAPPart soapPart = (SOAPPart) soapMessage.getSOAPPart();
                SOAPEnvelope envelope = soapPart.getEnvelope();

                SOAPHeader soapheader = envelope.getHeader() != null ? envelope.getHeader() : envelope.addHeader();
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

                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.transform(xmlInput, new StreamResult(out));

                ExtPayMain.bRLogger.logEvent("STARTTIMES PUSH REQUEST", TXUtility.convertToString(out));
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
                //This handler does nothing with the response from the Web Service so
                //we just print out the SOAP message.
                SOAPMessage message = smc.getMessage();
                System.out.println("in the try " + message.getSOAPBody().getAttribute("returnCode"));

                StringWriter out = new StringWriter();
                Source xmlInput = message.getSOAPPart().getContent();

                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");

                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                transformer.transform(xmlInput, new StreamResult(out));
                ExtPayMain.bRLogger.logEvent("OriginalTran ref ", StarttimesProcess.getTranRef());
                ExtPayMain.bRLogger.logEvent("STARTTIMES PUSH RESPONSE", TXUtility.convertToString(out));
                ExtPayMain.bRLogger.logEvent("START DECODING THE RESPONSE >>>> ");

//                String resp2 = message.getSOAPBody().getElementsByTagNameNS("http://haiwai.model.sms.star.com", "returnCode").item(0).getFirstChild().getNodeValue();
//                ExtPayMain.bRLogger.logEvent("RESPOSNSE 2 >>>> " + resp2);
                String resp = message.getSOAPBody().getElementsByTagName("returnCode").item(0).getFirstChild().getNodeValue();
                ExtPayMain.bRLogger.logEvent("RESPOSNSE 1 >>>> " + resp);
                String respmsg = message.getSOAPBody().getElementsByTagName("returnMsg").item(0).getFirstChild().getNodeValue();

                String resptranNo = StarttimesProcess.getTranRef();
                try
                {
                    resptranNo = message.getSOAPBody().getElementsByTagName("transactionlNo").item(0).getFirstChild().getNodeValue() == null ? "XoX" : message.getSOAPBody().getElementsByTagName("transactionlNo").item(0).getFirstChild().getNodeValue();
                }
                catch (Exception ex)
                {
                    ex = null;
                }

                String OrderNo = null;
                timestamp = new java.util.Date();

                timenow = formatter.format(timestamp);
                ExtPayMain.bRLogger.logEvent("INFO", "updating the Startimes resptranNo " + resptranNo);
                ExtPayMain.bRLogger.logEvent("INFO", "updating the Startimes OrderNo " + OrderNo);
                ExtPayMain.bRLogger.logEvent("INFO", "updating the Startimes resp " + resp);
                ExtPayMain.bRLogger.logEvent("INFO", "updating the Startimes timenow " + timenow);

                int retrycount = util.checkRetry(Integer.parseInt(resptranNo));

                ExtPayMain.bRLogger.logEvent("INFO", "RETRY COUNT = " + retrycount + " resp " + resp);

                if ("0".equals(resp))
                {
                    OrderNo = (message.getSOAPBody().getElementsByTagName("orderCode").item(0).getFirstChild().getNodeValue());
                    util.updateutilityRecord("Y", Integer.parseInt(resptranNo), OrderNo, resp, timenow, 0);
                }
                else if (!"0".equals(resp) && (retrycount + 1) <= 3)
                {
                    OrderNo = "NULL";
                    if (respmsg.contains("The same transaction serial number is being processed"))
                    {
                        util.updateutilityRecord2("N", Integer.parseInt(resptranNo), (OrderNo), resp, timenow, (retrycount + 1));
                    }
                    else
                    {
                        util.updateutilityRecord("N", Integer.parseInt(resptranNo), (OrderNo), resp, timenow, (retrycount + 1));
                    }
                }
                else
                {
                    ExtPayMain.bRLogger.logEvent("INFO", "Max no of retry reached.. [" + ("0".equals(resp) ? "SUCCESS!" : "FAIL! ]"));
                    if (respmsg.contains("The same transaction serial number is being processed"))
                    {
                        util.updateutilityRecord2("Y", Integer.parseInt(resptranNo), (OrderNo), resp, timenow, retrycount + 1);
                    }
                    else
                    {
                        util.updateutilityRecord("Y", Integer.parseInt(resptranNo), (OrderNo), resp, timenow, retrycount + 1);
                    }
                }
                ExtPayMain.bRLogger.logEvent("INFO", "Start Times Service processing.. [" + ("0".equals(resp) ? "SUCCESS!" : "FAIL! ]"));

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
