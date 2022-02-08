/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StarTimesPayment;

import RubyPayments.BRController;
import com.star.sms.haiwai.service.IHaiWaiElectronicPaymentService;
import com.star.sms.haiwai.service.IHaiWaiElectronicPaymentServicePortType;
import com.star.sms.model.haiwai.CustomerPayDto2;
import com.star.sms.model.haiwai.ObjectFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.JAXBElement;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;
import RubyPayments.TXUtility;
import RubyPayments.ExtPayMain;
import RubyPayments.STValue;
import com.star.sms.model.haiwai.condition.SubscriberQueryCondition;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 *
 * @author NJINU
 */
public class StarttimesProcess
{

    TXUtility util = new TXUtility();
    private static String tranRef;

    public void StarTimesPayment()
    {
        StringBuilder sb = new StringBuilder().append(":\n");
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss ");
            Date date = new Date();
            CustomerPayDto2 customerPayDto2 = new CustomerPayDto2();
            STValue accountsData = util.startTimesElemValue();
            ObjectFactory creator = new ObjectFactory();
            for (int i = 1; i <= accountsData.getCount(); i++)
            {
                try
                {
                    ExtPayMain.bRLogger.logEvent("StarttimesProcess", "For the fetch " + accountsData.getTranNo() + " _ " + sdf.format(date));
                    JAXBElement<String> custName = creator.createCustomerPayDto2CustomerName(accountsData.getCustName());
                    JAXBElement<String> custcode = creator.createCustomerPayDto2CustomerCode(accountsData.getCustcode());
                    JAXBElement<String> custTel = creator.createCustomerPayDto2CustomerTel(accountsData.getCustTel());
                    JAXBElement<String> payerId = creator.createCustomerPayDto2PayerID(BRController.StartimesPayerId);
                    JAXBElement<String> payerPwd = creator.createCustomerPayDto2PayerPwd(BRController.StartimesPayerPassword);
                    JAXBElement<String> custRecCode = creator.createCustomerPayDto2ReceiptCode("");
                    JAXBElement<String> tranNo = creator.createCustomerPayDto2TransactionNo(accountsData.getTranNo());
                    JAXBElement<String> devType = creator.createCustomerPayDto2DeviceType("");
                    JAXBElement<String> smartCardno = creator.createCustomerPayDto2SmartCardCode(accountsData.getSmartCardno());
                    JAXBElement<String> email = creator.createCustomerPayDto2Email("");
                    JAXBElement<Double> fee = creator.createCustomerPayDto2Fee(accountsData.getFee());
                    JAXBElement<String> tfrTime = creator.createCustomerPayDto2TransferTime(sdf.format(date));

                    customerPayDto2.setCustomerCode(custcode);
                    customerPayDto2.setCustomerName(custName);
                    customerPayDto2.setCustomerTel(custTel);
                    customerPayDto2.setDeviceType(devType);
                    customerPayDto2.setEmail(email);
                    customerPayDto2.setFee(fee);
                    customerPayDto2.setPayerID(payerId);
                    customerPayDto2.setPayerPwd(payerPwd);
                    customerPayDto2.setReceiptCode(custRecCode);
                    customerPayDto2.setSmartCardCode(smartCardno);
                    customerPayDto2.setTransactionNo(tranNo);
                    customerPayDto2.setTransferTime(tfrTime);
                    
                    setTranRef(accountsData.getTranNo());

                    HeaderHandlerResolver handlerResolver = new HeaderHandlerResolver();
                    IHaiWaiElectronicPaymentService service = new IHaiWaiElectronicPaymentService();

                    service.setHandlerResolver(handlerResolver);
                    IHaiWaiElectronicPaymentServicePortType port = service.getIHaiWaiElectronicPaymentServiceHttpPort();

                    ExtPayMain.bRLogger.logEvent("customerPayDto2 ", convertToString(customerPayDto2, ""));
                    port.customerPay2(customerPayDto2);
                    System.err.println("port.customerPay2(customerPayDto2)    " + port.customerPay2(customerPayDto2).getReturnCode());

                }
                catch (Exception e)
                {
                    ExtPayMain.bRLogger.logError("handling message", e);
                }
            }
        }
        catch (Exception e)
        {
            if (e instanceof SOAPFaultException)
            {
                sb.append(createSoapFaultMessage((SOAPFaultException) e));
                System.out.println(sb.append(createSoapFaultMessage((SOAPFaultException) e)));
                ExtPayMain.bRLogger.logError("ERROR", e);
            }
            else
            {
                ExtPayMain.bRLogger.logError("ERROR", e);
            }
        }
    }

    public void StarTimesValidate(String smartCardCode)
    {
        StringBuilder sb = new StringBuilder().append(":\n");
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss ");
            Date date = new Date();

            SubscriberQueryCondition querryCondition = new SubscriberQueryCondition();
            ObjectFactory creator = new ObjectFactory();
            try
            {
                JAXBElement<String> payerId = creator.createCustomerPayDto2PayerID(BRController.StartimesPayerId);
                JAXBElement<String> payerPwd = creator.createCustomerPayDto2PayerPwd(BRController.StartimesPayerPassword);
                JAXBElement<String> tranNo = creator.createCustomerPayDto2TransactionNo("0");
                JAXBElement<String> smartCardno = creator.createCustomerPayDto2SmartCardCode(smartCardCode);

                querryCondition.setPayerID(payerId);
                querryCondition.setPayerPwd(payerPwd);
                querryCondition.setSmartCardCode(smartCardno);
                querryCondition.setTransactionNo(tranNo);

                HeaderHandlerResolverValidate handlerResolver = new HeaderHandlerResolverValidate();
                IHaiWaiElectronicPaymentService service = new IHaiWaiElectronicPaymentService();

                service.setHandlerResolver(handlerResolver);
                IHaiWaiElectronicPaymentServicePortType port = service.getIHaiWaiElectronicPaymentServiceHttpPort();

                port.querySubscriberInfo(querryCondition);
            }
            catch (Exception e)
            {
                ExtPayMain.bRLogger.logError("hadling message", e);
            }
        }
        catch (Exception e)
        {
            if (e instanceof SOAPFaultException)
            {
                sb.append(createSoapFaultMessage((SOAPFaultException) e));
                ExtPayMain.bRLogger.logDebug("SOAPFaultException", sb.append(createSoapFaultMessage((SOAPFaultException) e)));
            }
            else
            {
                ExtPayMain.bRLogger.logError("ERROR", e);
            }
        }
    }

    private String createSoapFaultMessage(SOAPFaultException failure)
    {
        SOAPFault fault = failure.getFault();
        StringBuilder message = new StringBuilder("?????? ??????????");
        Name code = fault.getFaultCodeAsName();
        if (code != null)
        {
            message.append(" (").append(code.getLocalName()).append(')');
        }
        message.append(":\n");
        message.append(fault.getFaultString());
        return message.toString();
    }

    private String convertToString(Object object, String tag)
    {
        String text = "";
        boolean empty = true;
        tag = decapitalize(tag);
        Class<?> beanClass = object != null ? object.getClass() : String.class;
        try
        {
            if (object != null)
            {
                for (MethodDescriptor methodDescriptor : Introspector.getBeanInfo(beanClass).getMethodDescriptors())
                {
                    if ("toString".equalsIgnoreCase(methodDescriptor.getName()) && beanClass == methodDescriptor.getMethod().getDeclaringClass())
                    {
                        return tag != null ? (tag + "=<" + String.valueOf(object) + ">") : String.valueOf(object);
                    }
                }

                tag = tag == null ? beanClass.getSimpleName() : tag;

                if (object instanceof List)
                {
                    boolean append = false;
                    text += (empty ? "" : ", ");
                    for (Object item : ((List) object).toArray())
                    {
                        if (item != null)
                        {
                            text += (append ? ", " : "") + convertToString(item, null);
                            append = true;
                        }
                    }
                    empty = false;
                }
                else if (object instanceof Map)
                {
                    boolean append = false;
                    text += (empty ? "" : ", ");
                    for (Object key : ((Map) object).keySet())
                    {
                        text += (append ? ", " : "") + convertToString(((Map) object).get(key), String.valueOf(key));
                        append = true;
                    }
                    empty = false;
                }
                else if (object instanceof byte[])
                {
                    return tag != null ? (tag + "=<" + new String((byte[]) object) + ">") : String.valueOf(object);
                }
                else if (beanClass.isArray())
                {
                    text += tag + "=<[\r\n";
                    for (Object item : (Object[]) object)
                    {
                        text += convertToString(item, null) + "\r\n";
                    }
                    text += "]>";
                }
                else
                {
                    for (PropertyDescriptor propertyDesc : Introspector.getBeanInfo(beanClass).getPropertyDescriptors())
                    {
                        Method readMethod = propertyDesc.getReadMethod();
                        if (readMethod != null)
                        {
                            Object value = propertyDesc.getReadMethod().invoke(object);
                            if (!(value instanceof Class))
                            {
                                text += (empty ? "" : ", ") + convertToString(value, propertyDesc.getName());
                                empty = false;
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return empty ? (tag != null ? (tag + "=<" + String.valueOf(object) + ">") : String.valueOf(object)) : (tag != null ? (tag + "=<[ " + text + " ]>") : text);
    }

    public String capitalize(String text)
    {
        if (text != null ? text.length() > 0 : false)
        {
            StringBuilder builder = new StringBuilder();
            for (String word : text.toLowerCase().split("\\s"))
            {
                builder.append(word.substring(0, 1).toUpperCase()).append(word.length() > 1 ? word.substring(1).toLowerCase() : "").append(" ");
            }
            return builder.toString().trim();
        }
        return text;
    }

    public String capitalize(String text, int minLen)
    {
        if (text != null)
        {
            StringBuilder builder = new StringBuilder();
            for (String word : text.split("\\s"))
            {
                builder.append(word.length() > minLen ? capitalize(word) : word).append(" ");
            }
            return builder.toString().trim();
        }
        return text;
    }

    public String decapitalize(String text)
    {
        if (text != null ? text.length() > 0 : false)
        {
            StringBuilder builder = new StringBuilder();
            for (String word : text.toLowerCase().split("\\s"))
            {
                builder.append(word.substring(0, 1).toLowerCase()).append(word.substring(1)).append(" ");
            }
            return builder.toString().trim();
        }
        return text;
    }

    /**
     * @return the tranRef
     */
    public static String getTranRef()
    {
        return tranRef;
    }

    /**
     * @param aTranRef the tranRef to set
     */
    public static void setTranRef(String aTranRef)
    {
        tranRef = aTranRef;
    }
}
