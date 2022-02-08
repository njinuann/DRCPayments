/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPayments;

/**
 *
 * @author Pecherk
 */
import static RubyPayments.BRController.xapiUrlMap;
import com.neptunesoftware.supernova.ws.client.security.BasicHTTPAuthenticator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.StringTokenizer;
import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.channel.PostChannel;
import org.jpos.iso.packager.PostPackager;

public class BRController {

    public static int maxLineCount = 200;
    public static long ChannelID = 9L, SystemUserID = -99L;
    private static Properties settings;
    public static final Properties isoCodes = new Properties();
    private static final Properties xapicodes = new Properties();
    public static String XapiUser, XapiPassword /*, LoanRepayAccount*/;
    private static final HashMap<String, String> currencyMap = new HashMap<>();
    public static final HashMap<Integer, String> xapiUrlMap = new HashMap<>();
    public static String JdbcDriverName = "oracle.jdbc.driver.OracleDriver";
    public static String CoreSchemaName, CoreWsdlURL, CMSchemaJdbcUrl, CMSchemaName, onlineCMSchemaName, offlineCMSchemaName, ShowFilePathStatus, onlineCMSchemaJdbcUrl,
            onlineCoreSchemaName, offlineCMSchemaJdbcUrl, offlineCoreSchemaName;
    public static String EtaxUrl, BankName, BankToken, EtaxHostName, STTFtpUser, STTFtpPass, ReconFilePath, FilePath, ReconServer,
            ReconUploadTime, TaxValidationFg;
    public static String PrimaryCurrency, AllowedProductCodes, onlineCMSchemaPassword, offlineCMSchemaPassword, CMSchemaPassword, ValueScheme, StartimesPayerId, StartimesPayerPassword;
    public static String SchChargeUSD, SchChargeCDF, TaxChargeCDF, TaxChargeUSD, TVChargeCDF, ApplyChargeFee, ApplyChargeTAX, ApplyChargeTV;
    public static String EnableDebug = "N", ChannelCode = "MOB";
    public static CallableStatement[] logTxnStatement = null;
    // public static Object[][] CoreBankingNodes = new Object[0][1];
    public static ArrayList<CBNode> CoreBankingNodes = new ArrayList<>();
    //public static String[] CoreNodes;
    public static ArrayList<Object> CoreNodes = new ArrayList<>();
    //public static SupernovaWebServiceEndpointPort[] XAPIConnections = null;
    public static int lastPortIndex = 0;
    // public static TXProcessor tXProcessor;
    //  public static XAPICaller xAPICaller;
//    public static AccountWebService accountWebService;
//    public static TransactionsWebService transactionsWebService;
//    public static FundsTransferWebService fundsTransferWebService;
//    public static TxnProcessWebService txnProcessWebService;

    public static int[] CONTXNCount = null;
    public static String[] IDController = null;
    public static boolean serviceSuspended = false;
    public static Connection[] DBConnections = null;
    public static final int WAIT_FOR_CONNECTION = -1;
    public static final int NO_XAPI_CONNECTIONS = -96;
    public static final String confDir = "conf", logsDir = "logs";
    private static boolean DisplayConsole = true;
    public static int DisplayLines = 500;
    public static BRLogger bRLogger = new BRLogger("rubyPay", "logs");

    //added for offline processing
    public static String onlineSchemaName, offlineSchema;
    private static String offlineAdapterIP;
    private static int offlineAdapterPort;

    public static void initialize() {
        configure();
        loadLibraries();
        openConnections();
    }

    public static void loadLibraries() {
        try {
            Class.forName(JdbcDriverName);
        } catch (Exception ex) {
            System.err.println("" + ex);
        }
        Authenticator.setDefault(new BasicHTTPAuthenticator(XapiUser, XapiPassword));
        System.setProperty("javax.xml.rpc.ServiceFactory", "weblogic.webservice.core.rpc.ServiceFactoryImpl");
        System.setProperty("javax.xml.soap.MessageFactory", "weblogic.webservice.core.soap.MessageFactoryImpl");
    }

    public static void openConnections() {
        new Thread(()
                -> {
            CONTXNCount = new int[xapiUrlMap.size()];
            IDController = new String[xapiUrlMap.size()];
            DBConnections = new Connection[xapiUrlMap.size()];
            logTxnStatement = new CallableStatement[xapiUrlMap.size()];

            for (int i = 0; i < xapiUrlMap.size(); i++) {
                openConnection(i);
            }
            new Thread(new CONWatch()).start();
        }).start();
    }

    public static void configure() {
        FileInputStream in;
        settings = new Properties();

        try {
            new File(logsDir).mkdirs();

            new File(confDir).mkdirs();
            File propsFile = new File(confDir, "settings.prp");
            if (!propsFile.exists()) {
                System.out.println("Missing bridge configuration file. Unable to load bridge settings...");
                bRLogger.logDebug("Missing bridge configuration file. Unable to load bridge settings...");
            }
            in = new FileInputStream(propsFile);
            settings.loadFromXML(in);

            ChannelCode = settings.getProperty("ChannelCode");

            CoreWsdlURL = settings.getProperty("CoreWsdlURL");

            PrimaryCurrency = settings.getProperty("PrimaryCurrency");
            AllowedProductCodes = settings.getProperty("AllowedProductCodes", "0");

            DisplayConsole = "Y".equalsIgnoreCase(settings.getProperty("DisplayConsole"));

            EtaxUrl = settings.getProperty("EtaxUrl");

            BankName = settings.getProperty("BankName");
            BankToken = settings.getProperty("BankToken");

            EtaxHostName = settings.getProperty("EtaxHostName");
            STTFtpUser = settings.getProperty("STTFtpUser");

            STTFtpPass = settings.getProperty("STTFtpPass");
            ReconFilePath = settings.getProperty("ReconFilePath");

            StartimesPayerId = settings.getProperty("StartimesPayerId");
            StartimesPayerPassword = settings.getProperty("StartimesPayerPassword");

            FilePath = settings.getProperty("FilePath");
            ReconServer = settings.getProperty("ReconServer");

            ReconUploadTime = settings.getProperty("ReconUploadTime");
            TaxValidationFg = settings.getProperty("TaxValidationFg");

            ValueScheme = BRCrypt.decrypt(settings.getProperty("ValueScheme"));
            setCoreBankingNodes();

            try {
                DisplayLines = Integer.parseInt(settings.getProperty("DisplayLines"));
            } catch (Exception ex) {
                DisplayLines = 500;
                ExtPayMain.bRLogger.logError("Configure()-ERROR", ex);
            }
            if (!"0".equals(AllowedProductCodes.trim())) {
                StringBuilder buffer = new StringBuilder();
                AllowedProductCodes = AllowedProductCodes.trim().replaceAll(";", ",");
                StringTokenizer tokenizer = new StringTokenizer(AllowedProductCodes, ",");
                while (tokenizer.hasMoreTokens()) {
                    buffer.append(buffer.length() > 0 ? "," : "").append(tokenizer.nextToken());
                }
                AllowedProductCodes = buffer.toString();
            }

            EnableDebug = settings.getProperty("EnableDebug", "N");

            JdbcDriverName = settings.getProperty("JdbcDriverName");
            serviceSuspended = "Y".equalsIgnoreCase(settings.getProperty("SuspendService"));

            XapiUser = BRCrypt.decrypt(settings.getProperty("XapiUser"));
            XapiPassword = BRCrypt.decrypt(settings.getProperty("XapiPassword"));

            SchChargeUSD = settings.getProperty("SchChargeUSD");
            SchChargeCDF = settings.getProperty("SchChargeCDF");

            TaxChargeCDF = settings.getProperty("TaxChargeCDF");
            TaxChargeUSD = settings.getProperty("TaxChargeUSD");
            TVChargeCDF = settings.getProperty("TVChargeCDF");

            ApplyChargeFee = settings.getProperty("ApplyChargeFee");
            ApplyChargeTAX = settings.getProperty("ApplyChargeTAX");

            ApplyChargeTV = settings.getProperty("ApplyChargeTV");
            ShowFilePathStatus = settings.getProperty("ShowFilePathStatus");
            try {
                ChannelID = Long.parseLong(settings.getProperty("ChannelID"));
            } catch (NumberFormatException ex) {
                ExtPayMain.bRLogger.logError("ChannelID-ERROR", ex);
                //System.err.println("" + ex);
            }

            onlineCMSchemaPassword = settings.getProperty("CMSchemaPassword");
            offlineCMSchemaPassword = settings.getProperty("offlineCMSchemaPassword");

            onlineCMSchemaJdbcUrl = settings.getProperty("CMSchemaJdbcUrl");
            offlineCMSchemaJdbcUrl = settings.getProperty("offlineCMSchemaJdbcUrl");

            onlineCMSchemaName = settings.getProperty("CMSchemaName");
            offlineCMSchemaName = settings.getProperty("offlineCMSchemaName", "CHANNELMANAGER");

            onlineCoreSchemaName = settings.getProperty("CoreSchemaName");
            offlineCoreSchemaName = settings.getProperty("offlineCoreSchemaName", "OFFLINE_SCHEMA");

            offlineAdapterIP = settings.getProperty("offlineAdapterIP", "localhost");
            offlineAdapterPort = Integer.parseInt(settings.getProperty("offlineAdapterPort", "6002"));

            reconfigure();

            in.close();
        } catch (IOException ex) {
            ExtPayMain.bRLogger.logError("Configure()MAIN-ERROR", ex);
            // System.err.println("" + ex);
        }
        try {
            File propsFile = new File(confDir, "xapicodes.prp");
            in = new FileInputStream(propsFile);
            xapicodes.loadFromXML(in);
            in.close();
        } catch (Exception ex) {
            ExtPayMain.bRLogger.logError("XapiCodes Config-ERROR", ex);
        }
        try {
            File isoPropsFile = new File(confDir, "isocodes.prp");
            in = new FileInputStream(isoPropsFile);
            isoCodes.loadFromXML(in);
            in.close();
        } catch (Exception ex) {
            ExtPayMain.bRLogger.logError("Iso Codes Error-ERROR", ex);
        }
    }

    static void reconfigure() {
        CMSchemaName = ChannelUtil.SERVICE_OFFLINE ? offlineCMSchemaName : onlineCMSchemaName;
        CoreSchemaName = ChannelUtil.SERVICE_OFFLINE ? offlineCoreSchemaName : onlineCoreSchemaName;
        CMSchemaJdbcUrl = ChannelUtil.SERVICE_OFFLINE ? offlineCMSchemaJdbcUrl : onlineCMSchemaJdbcUrl;
        CMSchemaPassword = ChannelUtil.SERVICE_OFFLINE ? BRCrypt.decrypt(offlineCMSchemaPassword) : BRCrypt.decrypt(onlineCMSchemaPassword);
        closeConnections();
        openConnections();
    }

    public static void saveSettings() {
        try {
            if (settings != null) {
                new File(confDir).mkdirs();
                File propsFile = new File(confDir, "settings.prp");
                settings.setProperty("EnableDebug", EnableDebug);
                settings.setProperty("SERVICE_OFFLINE", ChannelUtil.SERVICE_OFFLINE ? "Y" : "N");
                settings.storeToXML(new FileOutputStream(propsFile), "Bridge Properties");
            }
        } catch (Exception ex) {
            ExtPayMain.bRLogger.logError("Save Settings-ERROR", ex);
            System.err.println("" + ex);
        }
    }

    public static void closeConnections() {
        for (int i = 0; i < xapiUrlMap.size(); i++) {
            closeConnection(i);
        }
    }

    public static void openConnection(int index) {
        CONTXNCount[index] = 0;
        IDController[index] = DBConnections[index] != null ? "N" : "U";
    }

    private static void closeConnection(int index) {
        if (IDController != null) {
            IDController[index] = "U";
        }
    }

    public static void CoreNodes(String urlProperty) {
        int counter = 1;
        StringTokenizer tokenizer = new StringTokenizer(urlProperty, "|");
        while (tokenizer.hasMoreTokens()) {
            CoreNodes.add(counter);
            counter++;
            break;
        }
    }

    private static void setCoreBankingNodes() {
        StringTokenizer tokenizer = new StringTokenizer(CoreWsdlURL, "|");
        while (tokenizer.hasMoreTokens()) {
            CBNode cBNode = new CBNode();
            cBNode.setWsContextURL(tokenizer.nextToken());
            CoreBankingNodes.add(cBNode);
        }
    }

    private static void setCurrencyMap() {
        currencyMap.clear();
        currencyMap.put("036", "AUD");
        currencyMap.put("AUD", "036");
        currencyMap.put("124", "CAD");
        currencyMap.put("CAD", "124");
        currencyMap.put("230", "ETB");
        currencyMap.put("ETB", "230");
        currencyMap.put("IRR", "364");
        currencyMap.put("364", "IRR");
        currencyMap.put("JPY", "392");
        currencyMap.put("392", "JPY");
        currencyMap.put("KES", "404");
        currencyMap.put("404", "KES");
        currencyMap.put("454", "MWK");
        currencyMap.put("MWK", "454");
        currencyMap.put("566", "NGN");
        currencyMap.put("NGN", "566");
        currencyMap.put("643", "RUB");
        currencyMap.put("RUB", "643");
        currencyMap.put("756", "CHF");
        currencyMap.put("800", "UGX");
        currencyMap.put("UGX", "800");
        currencyMap.put("834", "TZS");
        currencyMap.put("TZS", "834");
        currencyMap.put("840", "USD");
        currencyMap.put("USD", "840");
        currencyMap.put("710", "ZAR");
        currencyMap.put("ZAR", "710");
        currencyMap.put("894", "ZMK");
        currencyMap.put("ZMK", "894");
        currencyMap.put("932", "ZWL");
        currencyMap.put("ZWL", "932");
        currencyMap.put("978", "EUR");
        currencyMap.put("EUR", "978");
        currencyMap.put("826", "GBP");
        currencyMap.put("GBP", "826");
        currencyMap.put("646", "RWF");
        currencyMap.put("RWF", "646");
    }

    public synchronized static String getCurrency(String currencyID) {
        Object code = currencyMap.get(currencyID);
        return (code == null ? currencyID : String.valueOf(code));
    }

    public static synchronized String getXapiMessage(String xapiRespCode) {
        return xapicodes.getProperty(xapiRespCode, "Undefined error");
    }

    public static synchronized String mapToIsoCode(String xapiRespCode) {
        return isoCodes.getProperty(xapiRespCode, "91");
    }

    //Added for offline channel acquisition
    static ISOChannel getOfflineChannel() {
        return new PostChannel(offlineAdapterIP, offlineAdapterPort, new PostPackager());
    }

    static class CONWatch implements Runnable {

        @Override
        public void run() {
            while (!serviceSuspended) {
                try {
                    ISOUtil.sleep(10000);
                    for (int i = 0; i < IDController.length; i++) {
                        if (IDController[i].equals("U")) {
                            System.err.println("oprning connections " + i);
                            openConnection(i);
                        }
                    }
                } catch (Exception ex) {
                    ex = null;
                }
            }
        }
    }

    static Random rnd = new Random();

    public static String nows() {
        return new SimpleDateFormat("yyMMddHHMM").format(new Date());
    }

    public static String stan() {
        return String.valueOf(100000 + rnd.nextInt(900000));
    }

    public static String generateUniqueKey() {
        return String.valueOf(1000000000 + rnd.nextInt(900000000));
    }
}
