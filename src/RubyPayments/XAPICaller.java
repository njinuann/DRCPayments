/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPayments;

import com.neptunesoftware.supernova.ws.common.XAPIException;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Pecherk
 */
public class XAPICaller
{

    private BigDecimal txnAmount = BigDecimal.ZERO;
    private String txnDescription = "";
    private String duration = "";
    private String connectionID = "";
    private ArrayList<Exception> exceptionsList = new ArrayList();
    private String refNumber = "";
    private String accountNo = "";
    private String ChargeId = "";
    private boolean isOurTerminal = true;
    private boolean isOurCustomer = true;
    private boolean isOffline = false;
    private boolean isReversal = false;
    private String isoRespCode = "",currency = "", accountPmtType = "",accessCode = "";
    private String extraIndent = "     ";
    private String xapiRespCode = "";
    private TXRequest tXRequest = new TXRequest();
    private Object mainRequest = new Object();
    private Object mainResponse = new Object();
    private final HashMap<String, Object> callsMap = new HashMap<>();

    public void logException(Exception ex)
    {
        getExceptionsList().add(ex);
    }

    @Override
    public String toString()
    {
        String indent = "";
        StringBuilder sbuf = new StringBuilder();
        sbuf.append(indent).append("<exec>\n");
        sbuf.append(indent).append(getExtraIndent()).append("<xapiconnid>Connection id [ ").append(getConnectionID()).append(" ]</xapiconnid>\n");
        sbuf.append(indent).append(getExtraIndent()).append("<refnumber>").append(getRefNumber()).append("</refnumber>\n");
        sbuf.append(indent).append(getExtraIndent()).append("<accountno>").append(getAccountNo()).append("</accountno>\n");
        sbuf.append(indent).append(getExtraIndent()).append("<txnamount>").append(getTxnAmount().toPlainString()).append("</txnamount>\n");
        sbuf.append(indent).append(getExtraIndent()).append("<xapitxndesc>").append(getTxnDescription()).append("</xapitxndesc>\n");
        sbuf.append(indent).append(getExtraIndent()).append("<currency>").append(getCurrency()).append("</currency>\n");
        sbuf.append(indent).append(getExtraIndent()).append("<accountPmtType>").append(getAccountPmtType()).append("</accountPmtType>\n");
        sbuf.append(indent).append(getExtraIndent()).append("<accessCode>").append(getAccessCode()).append("</accessCode>\n");
        sbuf.append(indent).append(getExtraIndent()).append("<mainrequest>").append(convertToString(getMainRequest())).append("</mainrequest>\n");
        sbuf.append(indent).append(getExtraIndent()).append("<txrequest>").append(convertToString(gettXRequest())).append("</txrequest>\n");

        String[] callKeys = callsMap.keySet().toArray(new String[callsMap.size()]);
        Arrays.sort(callKeys);       
        for (Object key : callKeys)
        {
            sbuf.append(indent).append(getExtraIndent()).append("<").append(String.valueOf(key).replaceAll("\\d", "")).append(">").append(cleanText(callsMap.get(key).toString())).append("</").append(String.valueOf(key).replaceAll("\\d", "")).append(">\n");
        }

        for (Object exception : getExceptionsList().toArray())
        {
            if (exception != null)
            {
                if (exception instanceof XAPIException)
                {
                    sbuf.append(indent).append(getExtraIndent()).append("<exception>").append(exception.toString()).append("</exception>\n");
                }
                else
                {
                    sbuf.append(indent).append(getExtraIndent()).append("<exception>");
                    sbuf.append(indent).append(getExtraIndent()).append(getExtraIndent()).append("<class>").append(((Exception) exception).getClass().getSimpleName()).append("</class>\n");
                    String emsg = (((Exception) exception).getMessage() == null) ? "" : ((Exception) exception).getMessage();

                    if (emsg.contains("\r\n"))
                    {
                        sbuf.append(indent).append(getExtraIndent()).append(getExtraIndent()).append("<message>");
                        sbuf.append(indent).append(getExtraIndent()).append(getExtraIndent()).append(getExtraIndent()).append(((Exception) exception).getMessage().replaceAll("\r\n", "\r\n" + indent + getExtraIndent() + getExtraIndent() + getExtraIndent()));
                        sbuf.append(indent).append(getExtraIndent()).append(getExtraIndent()).append("</message>");
                    }
                    else
                    {
                        sbuf.append(indent).append(getExtraIndent()).append(getExtraIndent()).append("<message>").append(((Exception) exception).getMessage()).append("</message>\n");
                    }
                    sbuf.append(indent).append(getExtraIndent()).append(getExtraIndent()).append("<stacktrace>\n");
                    for (StackTraceElement s : ((Throwable) exception).getStackTrace())
                    {
                        sbuf.append(indent).append(getExtraIndent()).append(getExtraIndent()).append(getExtraIndent()).append("at ").append(s.toString());
                    }
                    sbuf.append(indent).append(getExtraIndent()).append(getExtraIndent()).append("</stacktrace>\n");
                    sbuf.append(indent).append(getExtraIndent()).append("</exception>\n");
                }
            }
        }
        sbuf.append(indent).append(getExtraIndent()).append("<mainresponse>").append(convertToString(getMainResponse())).append("</mainresponse>\n");
        sbuf.append(indent).append(getExtraIndent()).append("<xapirespcode>").append(getXapiRespCode()).append("</xapirespcode>\n");
        sbuf.append(indent).append(getExtraIndent()).append("<duration>").append(getDuration()).append("</duration>\n");
        sbuf.append(indent).append("</exec>");
        return sbuf.toString();
    }

    public void dump(PrintStream p, String indent)
    {
        p.println(indent + "<exec>");
        p.println(indent + getExtraIndent() + "<xapiconnid>Connection id [ " + getConnectionID() + " ]</xapiconnid>");
        p.println(indent + getExtraIndent() + "<refnumber>" + getRefNumber() + "</refnumber>");
        p.println(indent + getExtraIndent() + "<accountno>" + getAccountNo() + "</accountno>");
        p.println(indent + getExtraIndent() + "<txnamount>" + getTxnAmount().toPlainString() + "</txnamount>");
        p.println(indent + getExtraIndent() + "<xapitxndesc>" + getTxnDescription() + "</xapitxndesc>");
        p.println(indent + getExtraIndent() + "<accountPmtType>" + (isIsOurTerminal()) + "</accountPmtType>");
        p.println(indent + getExtraIndent() + "<currency>" + (isIsOurCustomer()) + "</currency>");
        p.println(indent + getExtraIndent() + "<isoffline>" + (isIsOffline()) + "</isoffline>");
        p.println(indent + getExtraIndent() + "<isreversal>" + (isIsReversal() ? "Yes" : "No") + "</isreversal>");
        p.println(indent + getExtraIndent() + "<txrequest>" + gettXRequest().toString() + "</txrequest>");

        String[] callKeys = callsMap.keySet().toArray(new String[callsMap.size()]);
        Arrays.sort(callKeys);
        for (Object key : callKeys)
        {
            p.println(indent + getExtraIndent() + "<" + String.valueOf(key).replaceAll("\\d", "") + ">" + cleanText(callsMap.get(key).toString()) + "</" + String.valueOf(key).replaceAll("\\d", "") + ">");
        }

        for (Object exception : getExceptionsList().toArray())
        {
            if (exception != null)
            {
                if (exception instanceof XAPIException)
                {
                    p.println(indent + getExtraIndent() + "<exception>" + exception.toString() + "</exception>");
                }
                else
                {
                    p.println(indent + getExtraIndent() + "<exception>");
                    p.println(indent + getExtraIndent() + getExtraIndent() + "<class>" + ((Exception) exception).getClass().getSimpleName() + "</class>");
                    String emsg = (((Exception) exception).getMessage() == null) ? "" : ((Exception) exception).getMessage();

                    if (emsg.contains("\r\n"))
                    {
                        p.println(indent + getExtraIndent() + getExtraIndent() + "<message>");
                        p.println(indent + getExtraIndent() + getExtraIndent() + getExtraIndent() + ((Exception) exception).getMessage().replaceAll("\r\n", "\r\n" + indent + getExtraIndent() + getExtraIndent() + getExtraIndent()));
                        p.println(indent + getExtraIndent() + getExtraIndent() + "</message>");
                    }
                    else
                    {
                        p.println(indent + getExtraIndent() + getExtraIndent() + "<message>" + ((Exception) exception).getMessage() + "</message>");
                    }
                    p.println(indent + getExtraIndent() + getExtraIndent() + "<stacktrace>");
                    for (StackTraceElement s : ((Throwable) exception).getStackTrace())
                    {
                        p.println(indent + getExtraIndent() + getExtraIndent() + getExtraIndent() + "at " + s.toString());
                    }
                    p.println(indent + getExtraIndent() + getExtraIndent() + "</stacktrace>");
                    p.println(indent + getExtraIndent() + "</exception>");
                }
            }
        }

        p.println(indent + getExtraIndent() + "<xapirespcode>" + getXapiRespCode() + "</xapirespcode>");
        p.println(indent + getExtraIndent() + "<responsecode>" + getIsoRespCode() + "</responsecode>");
        p.println(indent + getExtraIndent() + "<duration>" + getDuration() + "</duration>");
        p.print(indent + "</exec>");
    }

    private String cleanText(String text)
    {
        String line = "", buffer = "";
        text = (text != null) ? text : "";

        InputStream is = new ByteArrayInputStream(text.getBytes());
        BufferedReader bis = new BufferedReader(new InputStreamReader(is));
        try
        {
            while (line != null)
            {
                buffer += line;
                line = bis.readLine();
            }
        }
        catch (IOException ex)
        {
            return buffer;
        }

        return buffer;
    }
   public static String convertToString(Object object)
    {
        boolean empty = true;
        Class<?> beanClass = object.getClass();
        String text = beanClass.getSimpleName() + "{ ";
        try
        {
            if (!(object instanceof String))
            {
                for (PropertyDescriptor propertyDesc : Introspector.getBeanInfo(beanClass).getPropertyDescriptors())
                {
                    if (!"class".equalsIgnoreCase(propertyDesc.getName()))
                    {
                        Method readMethod = propertyDesc.getReadMethod();
                        if (readMethod != null)
                        {
                            Object value = propertyDesc.getReadMethod().invoke(object);
                            if (value != null ? value.getClass().isArray() : false)
                            {
                                text += "<[\r\n";
                                for (Object item : (Object[]) value)
                                {
                                    text += convertToString(item) + "\r\n";
                                    empty = false;
                                }
                                text += "]>";
                            }
                            else
                            {
                                text += (empty ? "" : ", ") + propertyDesc.getName() + "=<" + value + ">";
                                empty = false;
                            }
                        }
                    }
                }
            }
            if (object instanceof List)
            {
                boolean append = false;
                text += (empty ? "" : ", ") + "items=<[ ";
                for (Object item : ((List) object).toArray())
                {
                    text += (append ? ", " : "") + convertToString(item);
                    append = true;
                }
                empty = false;
                text += " ]>";
            }
            if (object instanceof Map)
            {
                boolean append = false;
                text += (empty ? "" : ", ") + "items=<[ ";
                for (Object key : ((Map) object).keySet())
                {
                    text += (append ? ", " : "") + key + "=<" + convertToString(((Map) object).get(key)) + ">";
                    append = true;
                }
                empty = false;
                text += " ]>";
            }
        }
        catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
            System.err.println("" + ex);
        }
        return empty ? String.valueOf(object) : text + " }";
    }
    /**
     * @return the txnAmount
     */
    public BigDecimal getTxnAmount()
    {
        return txnAmount;
    }

    /**
     * @param txnAmount the txnAmount to set
     */
    public void setTxnAmount(BigDecimal txnAmount)
    {
        this.txnAmount = txnAmount;
    }

    /**
     * @return the txnDescription
     */
    public String getTxnDescription()
    {
        return txnDescription;
    }

    /**
     * @param txnDescription the txnDescription to set
     */
    public void setTxnDescription(String txnDescription)
    {
        this.txnDescription = txnDescription;
    }

    /**
     * @return the duration
     */
    public String getDuration()
    {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(String duration)
    {
        this.duration = duration;
    }

    /**
     * @return the connectionID
     */
    public String getConnectionID()
    {
        return connectionID;
    }

    /**
     * @param connectionID the connectionID to set
     */
    public void setConnectionID(String connectionID)
    {
        this.connectionID = connectionID;
    }

    /**
     * @return the exceptionsList
     */
    private ArrayList<Exception> getExceptionsList()
    {
        return exceptionsList;
    }

    /**
     * @return the refNumber
     */
    public String getRefNumber()
    {
        return refNumber;
    }

    /**
     * @param refNumber the refNumber to set
     */
    public void setRefNumber(String refNumber)
    {
        this.refNumber = refNumber;
    }

    /**
     * @return the accountNo
     */
    public String getAccountNo()
    {
        return accountNo;
    }

    /**
     * @param accountno the accountNo to set
     */
    public void setAccountNo(String accountno)
    {
        this.accountNo = accountno;
    }

    /**
     * @return the isOurTerminal
     */
    public boolean isIsOurTerminal()
    {
        return isOurTerminal;
    }

    /**
     * @param isOurTerminal the isOurTerminal to set
     */
    public void setIsOurTerminal(boolean isOurTerminal)
    {
        this.isOurTerminal = isOurTerminal;
    }

    /**
     * @return the isOurCustomer
     */
    public boolean isIsOurCustomer()
    {
        return isOurCustomer;
    }

    /**
     * @param isOurCustomer the isOurCustomer to set
     */
    public void setIsOurCustomer(boolean isOurCustomer)
    {
        this.isOurCustomer = isOurCustomer;
    }

    /**
     * @return the isOffline
     */
    public boolean isIsOffline()
    {
        return isOffline;
    }

    /**
     * @param isOffline the isOffline to set
     */
    public void setIsOffline(boolean isOffline)
    {
        this.isOffline = isOffline;
    }

    /**
     * @return the isReversal
     */
    public boolean isIsReversal()
    {
        return isReversal;
    }

    /**
     * @param isReversal the isReversal to set
     */
    public void setIsReversal(boolean isReversal)
    {
        this.isReversal = isReversal;
    }

    /**
     * @return the isoRespCode
     */
    public String getIsoRespCode()
    {
        return isoRespCode;
    }

    /**
     * @param isoRespCode the isoRespCode to set
     */
    public void setIsoRespCode(String isoRespCode)
    {
        this.isoRespCode = isoRespCode;
    }

    /**
     * @return the extraIndent
     */
    public String getExtraIndent()
    {
        return extraIndent;
    }

    /**
     * @param extraIndent the extraIndent to set
     */
    public void setExtraIndent(String extraIndent)
    {
        this.extraIndent = extraIndent;
    }

    /**
     * @return the xapiRespCode
     */
    public String getXapiRespCode()
    {
        return xapiRespCode;
    }

    /**
     * @param xapiRespCode the xapiRespCode to set
     */
    public void setXapiRespCode(String xapiRespCode)
    {
        this.xapiRespCode = xapiRespCode;
    }

    public void setCall(String callRef, Object callObject)
    {
        this.callsMap.put(callsMap.size() + callRef, callObject);
    }

    /**
     * @return the tXRequest
     */
    public TXRequest gettXRequest()
    {
        return tXRequest;
    }

    /**
     * @param tXRequest the tXRequest to set
     */
    public void settXRequest(TXRequest tXRequest)
    {
        this.tXRequest = tXRequest;
    }

    /**
     * @return the mainRequest
     */
    public Object getMainRequest()
    {
        return mainRequest;
    }

    /**
     * @param mainRequest the mainRequest to set
     */
    public void setMainRequest(Object mainRequest)
    {
        this.mainRequest = mainRequest;
    }

    /**
     * @return the mainResponse
     */
    public Object getMainResponse()
    {
        return mainResponse;
    }

    /**
     * @param mainResponse the mainResponse to set
     */
    public void setMainResponse(Object mainResponse)
    {
        this.mainResponse = mainResponse;
    }

    /**
     * @return the currency
     */
    public String getCurrency()
    {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    /**
     * @return the accountPmtType
     */
    public String getAccountPmtType()
    {
        return accountPmtType;
    }

    /**
     * @param accountPmtType the accountPmtType to set
     */
    public void setAccountPmtType(String accountPmtType)
    {
        this.accountPmtType = accountPmtType;
    }

    /**
     * @return the accessCode
     */
    public String getAccessCode()
    {
        return accessCode;
    }

    /**
     * @param accessCode the accessCode to set
     */
    public void setAccessCode(String accessCode)
    {
        this.accessCode = accessCode;
    }

    /**
     * @return the ChargeId
     */
    public String getChargeId()
    {
        return ChargeId;
    }

    /**
     * @param ChargeId the ChargeId to set
     */
    public void setChargeId(String ChargeId)
    {
        this.ChargeId = ChargeId;
    }
}
