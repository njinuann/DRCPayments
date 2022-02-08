/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NepPayServer;

import ERTax.HttpETRClient;
import static ERTax.HttpETRClient.ETClient;
import RubyPayments.BRController;
import RubyPayments.CNAccount;
import RubyPayments.ExtPayMain;
import RubyPayments.PmtWebserviceData;
import RubyPayments.TXRequest;
import RubyPayments.TXUtility;
import static RubyPayments.TXUtility.convertToString;
import RubyPayments.TaxQueryRequest;
import RubyPayments.TaxResponse;
import RubyPayments.TvQueryRequest;
import RubyPayments.TvResponse;
import StarTimesPayment.HeaderHandlerSTTValidator;
import StarTimesPayment.StarttimesProcess;
import com.sun.net.httpserver.Headers;
import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;

public class PostXML
{

    private StringBuilder string = new StringBuilder();
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    public static String HOSTNAME = BRController.EtaxHostName;
    public static String validationCode = "291";
    private static final int PORT = 3970;
    private static final int STATUS_OK = 200;
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final Long xtferChannelId = 6L;
    private static final String successResponse = "00";
    private static final String invalidError = "91";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_OPTIONS = "OPTIONS";
    private static final String ALLOWED_METHODS = METHOD_GET + "," + METHOD_OPTIONS;
    private static final String HEADER_ALLOW = "Allow";
    private static final int STATUS_METHOD_NOT_ALLOWED = 405;
    public static StarttimesProcess starttimesProcess = new StarttimesProcess();

    private static final int NO_RESPONSE_LENGTH = -1;
    public static TXUtility tXUtility = new TXUtility();

    public static void processRequest() throws IOException
    {

        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 10);
        server.createContext("/getStudentDetail", new MyHandler());
        server.setExecutor(null);
        server.start();
        // System.out.println("***********E-Tax request processor running***********");
    }

    static class MyHandler implements HttpHandler
    {

        @Override
        public void handle(HttpExchange t) throws IOException
        {
            System.err.println("receiving request >>>>>>>> ");
            final Headers headers = t.getResponseHeaders();
            final String requestMethod = t.getRequestMethod().toUpperCase();
            String response;
            switch (requestMethod)
            {
                case METHOD_GET:
                    try
                    {

                        final Map<String, List<String>> requestParameters = getRequestParameters(t.getRequestURI());
                        String regex = "\\[|\\]";

                        String Reference = String.valueOf(requestParameters.get("StudentId")).replaceAll(regex, "");
                        String CurrencyCode = String.valueOf(requestParameters.get("Currency")).replaceAll(regex, "");

                        String PaymentType = String.valueOf(requestParameters.get("PaymentType")).replaceAll(regex, "");
                        String TaxType = String.valueOf(requestParameters.get("TaxType")).replaceAll(regex, "");
                        ExtPayMain.bRLogger.logEvent(" SEARCH", "SEARCH FOR REF [" + Reference + "], CURRENCY =[" + CurrencyCode + "],TYPE <<[" + PaymentType + "]>>, TaxType [" + TaxType + "]");
                        if (null != PaymentType)
                        {
                            switch (PaymentType)
                            {
                                case "TAX":
                                {
                                    CNAccount cNAccount;
                                    cNAccount = tXUtility.queryBillerAccount(TaxType);
                                    TaxQueryRequest taxQueryRequest = new TaxQueryRequest();
                                    taxQueryRequest.setNP_number(Reference);
                                    taxQueryRequest.setCurrency(CurrencyCode);
                                    taxQueryRequest.setUtilityType(PaymentType);
                                    taxQueryRequest.setReference(Reference);
                                    taxQueryRequest.setToAccount(cNAccount.getAccountNumber());

                                    final TaxResponse objResponse = queryTaxPayment(taxQueryRequest, TaxType, cNAccount.getAccountName());
//                                    if (CurrencyCode == null || !CurrencyCode.equals("CDF"))
//                                    {
//                                        response = "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n "
//                                                + "\n The Currency selected is INVALID. Please select the right Currency and try again.\n"
//                                                + "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n&&&& ";
//                                    }
//                                    else 
                                    if (TaxType == null || TaxType.equals(""))
                                    {
                                        response = "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n "
                                                + "\n The Tax Type cannot be empty. Please pick tax type from the Source of funds drop down list and try again.\n"
                                                + "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n&&&& ";
                                    }
                                    else if (!tXUtility.isTaxTypeValid(Integer.parseInt(TaxType)))
                                    {
                                        response = "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬"
                                                + "\n The Tax Type selected is Invalid. Please contact your System Admininstrator.\n"
                                                + "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n&&&&";
                                    }
                                    else if (!TaxType.equals("") || !TaxType.isEmpty())
                                    {
                                        try
                                        {
                                            if (objResponse.getAccountName().equals("NOT AVAILABLE"))
                                            {
                                                response = "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n"
                                                        + "\n The Tax Type record could not be found. Erecette could be inaccesssible. Please contact your System Admininstrator.\n"
                                                        + "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n&&&&";
                                            }
                                            else if ("N".equalsIgnoreCase(BRController.TaxValidationFg))
                                            {
                                                response = "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n"
                                                        + "\n•" + formatString("NP NO") + objResponse.getReference() + "\n"
                                                        + "\n•" + formatString("NAME") + objResponse.getAccountName().toUpperCase() + "\n"
                                                        + "\n•" + ("AMOUNT        = ") + objResponse.getAmount() + "\n"
                                                        + "\n•" + formatString("ARTICLE GROUP") + objResponse.getArticleGroup() + "\n"
                                                        + "\n•" + formatString("ARTICLE NAMING") + objResponse.getBudgetNaming() + "\n"
                                                        + "\n•" + formatString("PERIOD") + objResponse.getPmtPeriod() + "\n"
                                                        + "\n•" + formatString("ISSUE DATE") + objResponse.getIssueDate() + "\n"
                                                        + "\n•" + formatString("DEADLINE") + objResponse.getDeadline() + "\n"
                                                        + "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n"
                                                        + "&" + cNAccount.getAccountNumber() + "&" + objResponse.getAccountName() + "&PAIEMENT IMPOT" + "&CDF";
                                            }
                                            else
                                            {
                                                response = "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n"
                                                        + "\n•" + formatString("NP NO") + objResponse.getReference() + "\n"
                                                        + "\n•" + formatString("NAME") + objResponse.getAccountName().toUpperCase() + "\n"
                                                        + "\n•" + ("AMOUNT        = ") + objResponse.getAmount() + "\n"
                                                        + "\n•" + formatString("ARTICLE GROUP") + objResponse.getArticleGroup() + "\n"
                                                        + "\n•" + formatString("ARTICLE NAMING") + objResponse.getBudgetNaming() + "\n"
                                                        + "\n•" + formatString("PERIOD") + objResponse.getPmtPeriod() + "\n"
                                                        + "\n•" + formatString("ISSUE DATE") + objResponse.getIssueDate() + "\n"
                                                        + "\n•" + formatString("DEADLINE") + objResponse.getDeadline() + "\n"
                                                        + "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n" + "&"
                                                        + cNAccount.getAccountNumber() + "&" + objResponse.getAccountName() + "&PAIEMENT IMPOT" + "&CDF";
                                            }
                                        }
                                        catch (Exception e)
                                        {
                                            ExtPayMain.bRLogger.logError("Tax Processing-ERROR", e);
                                            response = "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n"
                                                    + "\n The request could not be processed. Erecette could be not be running. Please check the Ruby Logs or contact System administrator.\n"
                                                    + "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n&&&&";
                                        }
                                    }
                                    else
                                    {
                                        response = "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n"
                                                + "\n The request could not be processed. Please check Ruby Payments service Logs or contact System administrator.\n"
                                                + "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n&&&&";
                                    }
                                    setHeaders(headers);

                                    final byte[] rawResponseBody = response.getBytes(CHARSET);

                                    t.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
                                    ExtPayMain.bRLogger.logEvent("INFO", response);
                                    t.getResponseBody().write(rawResponseBody);
                                    if (!objResponse.getAccountName().equals("NOT AVAILABLE") && !tXUtility.isTAXtxnLogged(objResponse.getReference(), CurrencyCode))
                                    {
                                        if (tXUtility.logNPTxn(objResponse.getReference(), objResponse.getAccountName(), objResponse.getAmount(), objResponse.getArticleGroup(),
                                                objResponse.getBudgetNaming(), objResponse.getIssueDate(), objResponse.getPmtPeriod(), objResponse.getDeadline(), cNAccount.getAccountNumber(), objResponse.getCurrency()))
                                        {
                                            ExtPayMain.bRLogger.logEvent("logNPTxn-INFO", "Logged Tax information successfully from the Orbit Teller query request");
                                        }
                                        else
                                        {
                                            ExtPayMain.bRLogger.logDebug("logNPTxn-Exception", "Could not Log Tax information successfully from the Orbit Teller query request");

                                        }
                                    }
                                    break;
                                }
                                case "FEE":
                                {
                                    final PmtWebserviceData objResponse = tXUtility.querryPmtData(Reference, CurrencyCode);
                                    if (objResponse.getStudentName().equals("Record not Found"))
                                    {
                                        response = "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n "
                                                + "\nRecord Not Found\n "
                                                + "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n&&&&";
                                    }
                                    else
                                    {
                                        response = "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n"
                                                + "\n•" + formatString("NAME") + objResponse.getStudentName().toUpperCase() + "\n"
                                                + "\n•" + formatString("REFERENCE") + objResponse.getStudentNumber() + "\n"
                                                + "\n•" + formatString("INSTIT. ID") + objResponse.getSchoolId() + "\n"
                                                + "\n•" + formatString("INSTIT. NAME") + objResponse.getSchoolName() + "\n"
                                                + "\n•" + formatString("ACCOUNT NO") + objResponse.getAccountNumber() + "\n"
                                                + "\n•" + formatString("CURRENCY") + objResponse.getCurrency() + "\n"
                                                + "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n"
                                                + "&" + objResponse.getAccountNumber() + "&" + objResponse.getStudentName() + "&FRAIS SCOLAIRE" + "&" + objResponse.getCurrency();
                                    }
                                    setHeaders(headers);

                                    final byte[] rawResponseBody = response.getBytes(CHARSET);
                                    t.sendResponseHeaders(STATUS_OK, rawResponseBody.length);

                                    ExtPayMain.bRLogger.logEvent("INFO", convertToString(response));
                                    t.getResponseBody().write(rawResponseBody);
                                    if (!objResponse.getStudentName().equals("Record not Found") && !tXUtility.isFEEtxnLogged(objResponse.getStudentNumber(), CurrencyCode))
                                    {
                                        if (tXUtility.logFeeTxn(objResponse))
                                        {
                                            ExtPayMain.bRLogger.logEvent("logFeeTxn-INFO", "Logged Fee information successfully from the Orbit-R Teller query request");
                                        }
                                        else
                                        {
                                            ExtPayMain.bRLogger.logDebug("logFeeTxn-Exception", "Could not Log Fee information successfully from the Orbit-R Teller query request");

                                        }
                                    }
                                    break;
                                }
                                case "TV":
                                {
                                    CNAccount cNAccount;
                                    //cNAccount = tXUtility.queryBillerAccount(TaxType);
                                    cNAccount = tXUtility.queryBillerAccount("STT");
                                    TvQueryRequest tvQueryRequest = new TvQueryRequest();

                                    tvQueryRequest.setDecoderNo(Reference);
                                    tvQueryRequest.setUtilityType("Star Times");

                                    tvQueryRequest.setCurrency(CurrencyCode);
                                    System.err.println("For TV an here ");

                                    final TvResponse objResponse = queryTVPayment(tvQueryRequest);
                                    if (CurrencyCode == null || !CurrencyCode.equals("CDF"))
                                    {
                                        response = "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n "
                                                + "\n The Currency selected is INVALID. Please select the right Currency and try again.\n"
                                                + "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n&&&& ";
                                    }
                                    else if (!objResponse.getRespCode().equals("0") || objResponse.getAccountName().isEmpty() || objResponse.getAccountName() == null)
                                    {
                                        response = "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n "
                                                + "\nRecord Not Found\n "
                                                + "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n&&&&";
                                    }
                                    else
                                    {
                                        response = "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n"
                                                + "\n•" + formatString("NAME ") + objResponse.getAccountName() + "\n"
                                                + "\n•" + formatString("DECODER NO.") + objResponse.getReference() + "\n"
                                                + "\n•" + formatString("SUBS. TYPE") + objResponse.getPaymentType() + "\n"
                                                + "\n•" + formatString("INSTIT. NAME") + objResponse.getTvType() + "\n"
                                                + "\n•" + formatString("CURRENCY") + objResponse.getCurrency() + "\n"
                                                + "\n+ ▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬\n"
                                                + "&" + cNAccount.getAccountNumber() + "&" + objResponse.getAccountName() + "&ABONNEMENT TV" + "&CDF";
                                    }
                                    setHeaders(headers);

                                    final byte[] rawResponseBody = response.getBytes(CHARSET);
                                    t.sendResponseHeaders(STATUS_OK, rawResponseBody.length);

                                    ExtPayMain.bRLogger.logEvent("INFO", response);
                                    t.getResponseBody().write(rawResponseBody);
                                    if (objResponse.getRespCode().equals("0") && !tXUtility.isTVtxnLogged(response, CurrencyCode))
                                    {
                                        if (tXUtility.logtvTxn(objResponse, tvQueryRequest))
                                        {
                                            ExtPayMain.bRLogger.logEvent("logNPTxn-INFO", "Logged TV information successfully from the Orbit-R Teller query request");
                                        }
                                        else
                                        {
                                            ExtPayMain.bRLogger.logDebug("logNPTxn-Exception", "Could not Log TV information successfully from the Orbit-R Teller query request");

                                        }
                                    }
                                    break;
                                }
                            }
                        }

                    }
                    catch (NumberFormatException | IOException ex)
                    {
                        ExtPayMain.bRLogger.logError("PostXML-ERROR", ex);

                    }
                case METHOD_OPTIONS:

                    headers.set(HEADER_ALLOW, ALLOWED_METHODS);
                    t.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                    break;
                default:
                    headers.set(HEADER_ALLOW, ALLOWED_METHODS);
                    t.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                    break;
            }
        }
    }

    public static void setHeaders(Headers headers)
    {
        headers.set(HEADER_CONTENT_TYPE, String.format("text/html; charset=%s", CHARSET));
        headers.set("Pragma", "no-cache");

        headers.set("Expires", "0");
        headers.set("Last-Modified", String.valueOf(new Date(0)));

        headers.set("If-Modified-Since", String.valueOf(new Date(0)));
        headers.set("Access-Control-Allow-Origin", "*");

        headers.set("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        headers.set("Cache-control", "no-cache,no-store, must-revalidate, post-check=0, pre-check=0");

    }

    public static TvResponse queryTVPayment(TvQueryRequest acctBalReq)
    {
        TvResponse cNCustProf = new TvResponse();
        TXRequest tXRequest = new TXRequest();

        tXRequest.setReferenceType(acctBalReq.getDecoderNo());
        ExtPayMain.bRLogger.logEvent("INFO", TXUtility.convertToString(acctBalReq));
        try
        {
            starttimesProcess.StarTimesValidate(acctBalReq.getDecoderNo());

            cNCustProf.setAccountName(HeaderHandlerSTTValidator.cNCustProf.getAccountName());
            cNCustProf.setReference(HeaderHandlerSTTValidator.cNCustProf.getReference());
            cNCustProf.setTvType(HeaderHandlerSTTValidator.cNCustProf.getTvType());

            cNCustProf.setChanellId(xtferChannelId);
            cNCustProf.setPaymentType(HeaderHandlerSTTValidator.cNCustProf.getPaymentType());
            cNCustProf.setCurrency(acctBalReq.getCurrency());

            cNCustProf.setRespCode(HeaderHandlerSTTValidator.cNCustProf.getRespCode());
            ExtPayMain.bRLogger.logEvent("INFO", TXUtility.convertToString(cNCustProf));
            if (HeaderHandlerSTTValidator.cNCustProf.getRespCode().equals("0"))
            {
                tXUtility.logTVTransaction(tXRequest, "OTC", "BL01T", "POS", "TV", cNCustProf);
            }

        }
        catch (Exception e)
        {
            ExtPayMain.bRLogger.logError("PostXML-ERROR", e);
        }
        return cNCustProf;
    }

    public static TaxResponse queryTaxPayment(TaxQueryRequest acctBalReq, String taxType, String TaxTypeName)
    {
        TaxResponse taxresp = new TaxResponse();
        String etaxUrl = BRController.EtaxUrl;

        String bankname = BRController.BankName; //"finca";
        String bankToken = BRController.BankToken; //"NhaEI83CVvYsuR/Rx1PCGg==";

        String taxValidationFg = BRController.TaxValidationFg;//"N";     
        ExtPayMain.bRLogger.logEvent("PostXML-queryTaxPayment", TXUtility.convertToString(acctBalReq));
        try
        {
            if (!taxType.equals(validationCode))
            {
                try
                {
                    taxresp.setAccountName(TaxTypeName == null ? "Impot Foncier" : TaxTypeName);
                    taxresp.setReference(acctBalReq.getReference());

                    taxresp.setChanellId(xtferChannelId);
                    taxresp.setCurrency(acctBalReq.getCurrency());

                    taxresp.setAmount(BigDecimal.ZERO);
                    taxresp.setArticleGroup("KINSHASA");

                    taxresp.setBudgetNaming("KINSHASA");
                    taxresp.setIssueDate("N/A");

                    taxresp.setPmtPeriod("N/A");
                    taxresp.setDeadline("N/A");

                    taxresp.setPaymentType("TAX Payment");

                    taxresp.setPaymentType(acctBalReq.getUtilityType());
                    taxresp.setRespCode("00");
                    ExtPayMain.bRLogger.logEvent("NON VALIDATED-ResponseTaxPayment", TXUtility.convertToString(taxresp));

                }
                catch (Exception ex)
                {
                    taxresp.setAccountName("NOT AVAILABLE");
                    taxresp.setReference(acctBalReq.getReference());

                    taxresp.setChanellId(xtferChannelId);
                    taxresp.setCurrency(acctBalReq.getCurrency());

                    taxresp.setAmount(BigDecimal.ZERO);
                    taxresp.setArticleGroup("KINSHASA");

                    taxresp.setBudgetNaming("KINSHASA");
                    taxresp.setIssueDate("N/A");

                    taxresp.setPmtPeriod("N/A");
                    taxresp.setDeadline("N/A");

                    taxresp.setPaymentType("TAX Payment");

                    taxresp.setPaymentType(acctBalReq.getUtilityType());
                    taxresp.setRespCode("91");
                    ExtPayMain.bRLogger.logError("NON VALIDATED-Error", ex);
                    return taxresp;
                }
                return taxresp;
            }
            else
            {
                try
                {
                    String querried = ETClient(acctBalReq.getNP_number(), etaxUrl, bankname, bankToken, 1L);

                    if (querried.equals("Success"))
                    {
                        if (HttpETRClient.getArticleGroup().equals("") || HttpETRClient.getArticleGroup().equals("") || HttpETRClient.getArticleGroup().equals(""))
                        {
                            HttpETRClient.setArticleGroup("N/A");
                        }
                        taxresp.setAccountName(HttpETRClient.getName());
                        taxresp.setReference(HttpETRClient.getNumber());
                        taxresp.setAmount(HttpETRClient.getAmount());

                        taxresp.setArticleGroup(HttpETRClient.getArticleGroup());
                        taxresp.setBudgetNaming(HttpETRClient.getBudgetNaming());

                        taxresp.setIssueDate(HttpETRClient.getIssueDate());
                        taxresp.setPmtPeriod(HttpETRClient.getPmtPeriod());

                        taxresp.setDeadline(HttpETRClient.getDeadline());
                        taxresp.setPaymentType("TAX Payment");

                        taxresp.setCurrency(acctBalReq.getCurrency());
                        taxresp.setRespCode("00");

                        ExtPayMain.bRLogger.logEvent("ERRECETTE-ResponseTaxPayment", TXUtility.convertToString(taxresp));
                        return taxresp;

                    }
                    else
                    {
                        taxresp.setRespCode("91");
                        return taxresp;
                    }
                }
                catch (JSONException ex)
                {
                    taxresp.setAccountName("NOT AVAILABLE");
                    taxresp.setReference(acctBalReq.getReference());

                    taxresp.setChanellId(xtferChannelId);
                    taxresp.setCurrency(acctBalReq.getCurrency());

                    taxresp.setAmount(BigDecimal.ZERO);
                    taxresp.setArticleGroup("KINSHASA");

                    taxresp.setBudgetNaming("KINSHASA");
                    taxresp.setIssueDate("N/A");

                    taxresp.setPmtPeriod("N/A");
                    taxresp.setDeadline("N/A");

                    taxresp.setPaymentType("TAX Payment");

                    taxresp.setPaymentType(acctBalReq.getUtilityType());
                    taxresp.setRespCode("91");
                    ExtPayMain.bRLogger.logError("ERRECETTE-Error", ex);
                    taxresp.setRespCode("91");
                    return taxresp;
                }
            }
        }
        catch (Exception e)
        {
            ExtPayMain.bRLogger.logError("ERRECETTEMAIN-Error", e);
            taxresp.setAccountName("NOT AVAILABLE");
            return taxresp;
        }
    }

    private static Map<String, List<String>> getRequestParameters(final URI requestUri)
    {

        final Map<String, List<String>> requestParameters = new LinkedHashMap<>();
        final String requestQuery = requestUri.getRawQuery();

        if (requestQuery != null)
        {
            final String[] rawRequestParameters = requestQuery.split("[&;]", -1);
            for (final String rawRequestParameter : rawRequestParameters)
            {
                final String[] requestParameter = rawRequestParameter.split("=", 2);
                final String requestParameterName = decodeUrlComponent(requestParameter[0]);
                requestParameters.putIfAbsent(requestParameterName, new ArrayList<>());
                final String requestParameterValue = requestParameter.length > 1 ? decodeUrlComponent(requestParameter[1]) : null;
                requestParameters.get(requestParameterName).add(requestParameterValue);
            }
        }
        return requestParameters;
    }

    private static String decodeUrlComponent(final String urlComponent)
    {
        try
        {
            return URLDecoder.decode(urlComponent, CHARSET.name());
        }
        catch (final UnsupportedEncodingException ex)
        {
            throw new InternalError(ex);
        }
    }

    private static String formatString(String input)
    {
        String padChar = " ",
                pad = "";
        int length = 14;
        for (int i = 0; i < length; i++)
        {
            pad += padChar;
        }
        return input + pad.substring(input.length()) + "= ";
    }
}
