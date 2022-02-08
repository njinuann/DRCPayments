package ERTax;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author NJINU
 */
import RubyPayments.BRController;
import RubyPayments.ExtPayMain;
import RubyPayments.TXUtility;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;

public class JsonServer {

    static TXUtility util = new TXUtility();
    public static String HOSTNAME = BRController.EtaxHostName;
    private static final int PORT = 3920;
    private static final int BACKLOG = 1;

    public static String bankname = "";
    public static String bankToken = "";

    private static final String HEADER_ALLOW = "Allow";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    private static final int STATUS_OK = 200;
    private static final int STATUS_METHOD_NOT_ALLOWED = 405;

    private static final int NO_RESPONSE_LENGTH = -1;

    private static final String METHOD_GET = "GET";
    private static final String METHOD_OPTIONS = "OPTIONS";
    private static final String ALLOWED_METHODS = METHOD_GET + "," + METHOD_OPTIONS;

    public static void txJsongetBourderau() throws IOException {
        try {
            System.err.println(HOSTNAME + " " + PORT);
            final HttpServer server = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT), BACKLOG);
            server.createContext("/getNotePerceptionJson", he -> {
                util.getDbConnection();
                // System.out.println("***********E-Tax get Bourderau Service Running***********");
                try {
                    final Headers headers = he.getResponseHeaders();
                    final String requestMethod = he.getRequestMethod().toUpperCase();
                    switch (requestMethod) {
                        case METHOD_GET:

                            final Map<String, List<String>> requestParameters = getRequestParameters(he.getRequestURI());

                            String regex = "\\[|\\]";

                            System.out.println("parameters1 " + String.valueOf(requestParameters.get("NotePerception")).replaceAll(regex, ""));
                            System.err.println("parameters " + requestParameters.get("param2"));
                            System.err.println("parameters " + requestParameters.get("param3"));
                            System.err.println("parameters " + requestParameters.get("param4"));

                            final String responseBody = writeJson(String.valueOf(requestParameters.get("NotePerception")).replaceAll(regex, ""));//"['Njinu!']" + requestParameters.get("param2") + requestParameters.get("param3");
                            headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
                            final byte[] rawResponseBody = responseBody.getBytes(CHARSET);
                            he.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
                            he.getResponseBody().write(rawResponseBody);
                            break;
                        case METHOD_OPTIONS:
                            headers.set(HEADER_ALLOW, ALLOWED_METHODS);
                            he.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                            break;
                        default:
                            headers.set(HEADER_ALLOW, ALLOWED_METHODS);
                            he.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                            break;
                    }
                } finally {
                    he.close();
                }
            });
            server.createContext("/getAMR", he -> {

                util.getDbConnection();
                try {
                    final Headers headers = he.getResponseHeaders();
                    final String requestMethod = he.getRequestMethod().toUpperCase();
                    switch (requestMethod) {
                        case METHOD_GET:

                            final Map<String, List<String>> requestParameters = getRequestParameters(he.getRequestURI());

                            String regex = "\\[|\\]";

                            System.err.println("parameters1 " + String.valueOf(requestParameters.get("param1")).replaceAll(regex, ""));
                            System.err.println("parameters " + requestParameters.get("param2"));
                            System.err.println("parameters " + requestParameters.get("param3"));
                            System.err.println("parameters " + requestParameters.get("param4"));

                            final String responseBody = writeJson(String.valueOf(requestParameters.get("param1")).replaceAll(regex, ""));//"['Njinu!']" + requestParameters.get("param2") + requestParameters.get("param3");
                            headers.set(HEADER_CONTENT_TYPE, String.format("application/json; charset=%s", CHARSET));
                            final byte[] rawResponseBody = responseBody.getBytes(CHARSET);
                            he.sendResponseHeaders(STATUS_OK, rawResponseBody.length);
                            he.getResponseBody().write(rawResponseBody);
                            break;
                        case METHOD_OPTIONS:
                            headers.set(HEADER_ALLOW, ALLOWED_METHODS);
                            he.sendResponseHeaders(STATUS_OK, NO_RESPONSE_LENGTH);
                            break;
                        default:
                            headers.set(HEADER_ALLOW, ALLOWED_METHODS);
                            he.sendResponseHeaders(STATUS_METHOD_NOT_ALLOWED, NO_RESPONSE_LENGTH);
                            break;
                    }
                } finally {
                    he.close();
                }
            });
            server.start();
        } catch (Exception ex) {
            ExtPayMain.bRLogger.logError("getBorderau-ERROR", ex);
        }
    }

    private static Map<String, List<String>> getRequestParameters(final URI requestUri) {
        final Map<String, List<String>> requestParameters = new LinkedHashMap<>();
        final String requestQuery = requestUri.getRawQuery();
        if (requestQuery != null) {
            final String[] rawRequestParameters = requestQuery.split("[&;]", -1);
            for (final String rawRequestParameter : rawRequestParameters) {
                final String[] requestParameter = rawRequestParameter.split("=", 2);
                final String requestParameterName = decodeUrlComponent(requestParameter[0]);
                requestParameters.putIfAbsent(requestParameterName, new ArrayList<>());
                final String requestParameterValue = requestParameter.length > 1 ? decodeUrlComponent(requestParameter[1]) : null;
                requestParameters.get(requestParameterName).add(requestParameterValue);
            }
        }
        return requestParameters;
    }

    private static String decodeUrlComponent(final String urlComponent) {
        try {
            return URLDecoder.decode(urlComponent, CHARSET.name());
        } catch (final UnsupportedEncodingException ex) {
            ExtPayMain.bRLogger.logError("URLDECODE-ERROR", ex);
            throw new InternalError(ex);
        }
    }

    public static String writeJson(String pNumber) {

        JSONObject obj = new JSONObject();
        try {
            obj.put("number", util.ErTaxsElemValue(pNumber).getP_Number());
            obj.put("Name", util.ErTaxsElemValue(pNumber).getApplicant_Name());
            obj.put("Amount", util.ErTaxsElemValue(pNumber).getAmount_paid());
            obj.put("articleGroup", util.ErTaxsElemValue(pNumber).getBgt_atc_grp());
            obj.put("BudgetNaming", util.ErTaxsElemValue(pNumber).getBgt_atc_naming());
            obj.put("issueDate", util.ErTaxsElemValue(pNumber).getIssue_Date());
            obj.put("PmtPeriod", util.ErTaxsElemValue(pNumber).getPmt_Period());
            obj.put("deadline", util.ErTaxsElemValue(pNumber).getPmt_deadline_date());

            System.out.print(obj);

            return obj.toJSONString();
        } catch (Exception ex) {
            ExtPayMain.bRLogger.logError("ERROR", ex);
            return "{ERROR Encountered}";
        }
    }

}
