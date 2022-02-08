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
//import BillerUpload.BillUploadMain;
import RubyPayments.BRController;
import RubyPayments.TXUtility;
import RubyPayments.ExtPayMain;
import java.io.IOException;
import java.math.BigDecimal;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.BasicConfigurator;
import org.json.JSONException;

public class HttpETRClientPayment
{

    public static String bankName = BRController.BankName;
    public static String bankToken = BRController.BankToken;
    public static String HostName = BRController.EtaxHostName;
    public static String etUrl = BRController.EtaxUrl;
    public static TXUtility util = new TXUtility();

    /**
     * @param txnNO
     * @param effectiveDate
     * @param Amount
     * @param Acct_no
     * @param DocType
     * @param NP
     * @return
     * @throws org.json.JSONException
     */
    public static boolean ETClient(int txnNO, String effectiveDate, BigDecimal Amount, String DocType, String Acct_no, String NP) throws JSONException
    {
        boolean processed = false;
        try
        {
            BasicConfigurator.configure();
            HttpClient client = new DefaultHttpClient();

            HttpPost request;
            request = new HttpPost(etUrl + "/pushPayment/");

            request.setHeader("Bank-Name", bankName);
            request.setHeader("Bank-Token", bankToken);
            request.setHeader("Bank-url", "http://" + HostName + ":3920/getBordereau?bordereau=$data$");
            request.setHeader("Bank-response-type", "JSON");

            String xml = "" + txnNO + "@" + effectiveDate + "@" + Amount + "@" + DocType + "@" + Acct_no + "@" + NP + "";

            HttpEntity entity = new ByteArrayEntity(xml.getBytes("UTF-8"));
            request.setEntity(entity);

            ExtPayMain.bRLogger.logEvent("ETax Push Payment", xml);
            HttpResponse response = client.execute(request);
            String result = EntityUtils.toString(response.getEntity());

            if ("607".equals(result))
            {
                System.out.println("Fail! ");
                util.updtTaxtxnSt("607", "F", null);
            }
            else
            {

                String returntype = splitItem(2, result);
                String txnNP = splitItem(1, result);

                if ("0".equalsIgnoreCase(returntype))
                {
                    System.out.println("Fail! ");
                    util.updtTaxtxnSt(txnNP, "F", null);

                    processed = false;
                    ExtPayMain.bRLogger.logEvent("ERROR", "Transaction No  " + txnNP + " failed " + returntype);

                }
                else
                {
                    System.out.println("Success! ");
                    util.updtTaxtxnSt(txnNP, "P", "Y");

                    processed = true;
                    ExtPayMain.bRLogger.logEvent("INFO", "Transaction No  " + txnNP + " Successiful " + returntype);

                }
            }
        }
        catch (IOException ex)
        {
            processed = false;
            ExtPayMain.bRLogger.logError("ETClient-ERROR", ex);

        }
        return processed;
    }

    public static String splitItem(int position, String bsName)
    {
        //  String temp = splitbillerItem(schcombo.getSelectedItem().toString());
        String temp = bsName;
        String[] splitString = temp.split("\\-");

        for (int i = 0; i < splitString.length; i++)
        {
        }
        if (position == 1)
        {
            return splitString[0];
        }
        else
        {
            return splitString[1];
        }
    }

//    public static void main(String[] args) throws JSONException {
//
//        // 101187768@2015-10-21@94000.00@NP@100101000025@NP000000000000062016
//        ETClient(98409274, "2015-10-21", BigDecimal.valueOf(10000.00), "NP", "100101000025", "NP000000000000072016");
//    }
}
