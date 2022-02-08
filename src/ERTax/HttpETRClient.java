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
import RubyPayments.ExtPayMain;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.json.JSONException;
import org.json.JSONObject;
import RubyPayments.TXUtility;

public class HttpETRClient {

    public static String Number;
    public static String Name;
    public static BigDecimal Amount;
    public static String articleGroup;
    public static String BudgetNaming;
    public static String PmtPeriod;
    public static String issueDate;
    public static String deadline;
    private static final String NOHEADER = "601";
    private static final String WRONGTOKEN = "602";
    private static final String NOPARAMETER = "603";
    private static final String WRONG_NP_AMR = "604";
    private static final String NotePercetion = "NP";
    private static final String AMR_Number = "AMR";
    private static final String NP_Method = "/getNotePerceptionJson/";
    private static final String AMR_Method = "/getNotePerceptionJson/";

    /**
     * @param Pnom
     * @param etaxUrl
     * @param bankName
     * @param bankToken
     * @param channelId
     * @param args
     * @return
     * @throws org.json.JSONException
     */
    public static String ETClient(String Pnom, String etaxUrl, String bankName, String bankToken, Long channelId) throws JSONException {
        TXUtility util = new TXUtility();
        String querried = null;

        try {
            //  System.err.println("Passed " + Pnom);
            HttpClient client = new DefaultHttpClient();
            HttpGet request;

            if (Pnom.startsWith(NotePercetion)) {

                request = new HttpGet(etaxUrl + NP_Method + Pnom + "");
                request.setHeader("bank-name", bankName);
                request.setHeader("bank-token", bankToken);

            } else if (Pnom.startsWith(AMR_Number)) {

                request = new HttpGet(etaxUrl + AMR_Method + Pnom + "");
                request.setHeader("bank-name", bankName);
                request.setHeader("bank-token", bankToken);

            } else {

                request = new HttpGet(etaxUrl + NP_Method + Pnom + "");
                request.setHeader("bank-name", bankName);
                request.setHeader("bank-token", bankToken);

            }

            HttpResponse response = client.execute(request);
            System.out.println(response.getStatusLine());

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line;

            while ((line = rd.readLine()) != null) {
                System.out.println(line);

                if (line.equalsIgnoreCase(NOHEADER) || line.equalsIgnoreCase(WRONGTOKEN) || line.equalsIgnoreCase(NOPARAMETER) || line.equalsIgnoreCase(WRONG_NP_AMR)) {
                    System.out.println("ERROR! " + line);

                    querried = line;
                    setNumber("0");

                } else {

                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
                    mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);

                    final JSONObject obj = new JSONObject(line);
                    System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(line));
                    Object json = mapper.readValue(line, Object.class);

                    System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json));
                    final JSONObject taxValue = (JSONObject) obj;
                    System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>. setting the variables <<<<<<<<<<<<<<<<<<<<<<<");

                    String numero = (String) taxValue.get("numero");
                    setNumber(numero);

                    setArticleGroup(taxValue.getString("codeIdentification"));//codeIdentification
                    setName("".equals(taxValue.getString("nomRequerant")) ? "N/A" : taxValue.getString("nomRequerant"));//nomRequerant

                    setAmount(BigDecimal.valueOf(taxValue.getDouble("netAPayer")));//netAPayer               
                    setBudgetNaming(taxValue.getString("articleBudgetaire"));

                    setPmtPeriod(taxValue.getString("periodePaiement"));//periodePaiement
                    setIssueDate(taxValue.getString("dateNP"));//dateNP

                    setDeadline("N/A"); //echeancePaiement    
                    System.out.println(getNumber() + " 1. >>>>>>>>>>>>>>>>>>>>>>>>>>>>> txResp.getNumber()<<<<<<<<<<<<<<<<<< - " + channelId);
                    if (channelId == 1) {
                        if (util.logNPTxn(taxValue.getString("numero"),
                                taxValue.getString("nomRequerant"),
                                BigDecimal.valueOf(taxValue.getDouble("netAPayer")),
                                taxValue.getString("codeIdentification"),
                                taxValue.getString("articleBudgetaire"),
                                taxValue.getString("dateNP"),
                                taxValue.getString("periodePaiement"),
                                "N/A", "", "CDF"//taxValue.getString("dateNP"),

                        )) {
                            System.err.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>. end of setting the variables <<<<<<<<<<<<<<<<<<<<<<<");
                        }
                    }
                    System.out.println(taxValue.getString("numero"));
                    System.out.println(taxValue.getString("nomRequerant"));
                    System.out.println(taxValue.getDouble("netAPayer"));
                    System.out.println(taxValue.getString("articleBudgetaire"));
                    System.out.println(taxValue.getString("articleBudgetaire"));
                    System.out.println(taxValue.getString("periodePaiement"));
                    System.out.println(taxValue.getString("dateNP"));
                    System.out.println(taxValue.getString("dateNP"));
                    //System.out.

                    querried = "Success";
                    //    }
                }
            }
        } catch (IOException ex) {
            ExtPayMain.bRLogger.logError("ERROR", ex);
            querried = "Fail";
        }
        return querried;
    }

    public static String getNumber() {
        return Number;
    }

    public static void setNumber(String Number) {
        HttpETRClient.Number = Number;
    }

    public static String getName() {
        return Name;
    }

    public static void setName(String Name) {
        HttpETRClient.Name = Name;
    }

    public static BigDecimal getAmount() {
        return Amount;
    }

    public static void setAmount(BigDecimal Amount) {
        HttpETRClient.Amount = Amount;
    }

    public static String getArticleGroup() {
        return articleGroup;
    }

    public static void setArticleGroup(String articleGroup) {
        HttpETRClient.articleGroup = articleGroup;
    }

    public static String getBudgetNaming() {
        return BudgetNaming;
    }

    public static void setBudgetNaming(String BudgetNaming) {
        HttpETRClient.BudgetNaming = BudgetNaming;
    }

    public static String getPmtPeriod() {
        return PmtPeriod;
    }

    public static void setPmtPeriod(String PmtPeriod) {
        HttpETRClient.PmtPeriod = PmtPeriod;
    }

    public static String getIssueDate() {
        return issueDate;
    }

    public static void setIssueDate(String issueDate) {
        HttpETRClient.issueDate = issueDate;
    }

    public static String getDeadline() {
        return deadline;
    }

    public static void setDeadline(String deadline) {
        HttpETRClient.deadline = deadline;
    }

}
