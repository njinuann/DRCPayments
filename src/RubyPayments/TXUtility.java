/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPayments;

import ERTax.HttpETRClient;
import static ERTax.HttpETRClient.ETClient;
import static NepPayServer.PostXML.starttimesProcess;
import RubyPaymentWS.SchFeePmtRequest;
import RubyPaymentWS.TVPaymentRequest;
import RubyPaymentWS.TaxPaymentRequest;
import StarTimesPayment.HeaderHandlerSTTValidator;
import com.neptunesoftware.supernova.ws.common.XAPIException;
import com.neptunesoftware.supernova.ws.server.account.data.AccountBalanceOutputData;
import com.neptunesoftware.supernova.ws.server.transaction.data.DepositTxnOutputData;
import com.neptunesoftware.supernova.ws.server.transaction.data.TxnResponseOutputData;
import com.neptunesoftware.supernova.ws.server.transfer.data.FundsTransferOutputData;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jpos.iso.ISOMsg;
import org.json.JSONException;

/**
 *
 * @author Pecherk
 */
public final class TXUtility
{

    int conIndex = -1;
    long startTime = 0;
    private Connection dbConnection = null;
    private CallableStatement logFeeTxnStatement = null;
    private CallableStatement logTxnStatement = null;
    private CallableStatement logTaxTxn = null;
    private CallableStatement logTVTxn = null;
    private String txnJournalId, chargeJournal, pmtType;
    private static Long DefaultBuId = null;
    public static String isErecetteTxn = "291";
    private static final String successResponse = "00";
    private static final String invalidError = "91";
    private static String terminalType = "";
    public java.util.Date timestamp = new java.util.Date();
    public SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
    public SimpleDateFormat STformatter = new SimpleDateFormat("yyyyMMdd");
    public SimpleDateFormat callformatter = new SimpleDateFormat("dd MMM yyyy");
    public SimpleDateFormat taxDtformat = new SimpleDateFormat("yyyy-MM-dd");
    public SimpleDateFormat procDtformat = new SimpleDateFormat("dd/MM/yyyy");
    public XAPICaller xapiCaller = new XAPICaller();
    public HashMap<Integer, String> getBillerTc = new HashMap<>();

    public TXUtility()
    {
        setXapiCaller(new XAPICaller());
        BRController.initialize();
    }

    private void connectToDB()
    {
        try
        {
            setDbConnection(DriverManager.getConnection(BRController.CMSchemaJdbcUrl, BRController.CMSchemaName, BRController.CMSchemaPassword));
            setLogTxnStatement(getDbConnection().prepareCall("{call " + BRController.CMSchemaName + ".PSP_EX_LOG_BILLS_TXN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"));
            setLogFeeTxnStatement(getDbConnection().prepareCall("{call " + BRController.CMSchemaName + ".PSP_EX_LOG_FEE_TXN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"));
            setLogTaxTxn(getDbConnection().prepareCall("{call " + BRController.CMSchemaName + ".PSP_EX_LOG_TAX_TXN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"));
            setLogTVTxn(getDbConnection().prepareCall("{call " + BRController.CMSchemaName + ".PSP_EX_LOG_TV_TXN(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}"));
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
                ExtPayMain.bRLogger.logError("connectToDB()-ERROR", ex);
            }
            else
            {
                ExtPayMain.bRLogger.logError("connectToDB()-ERROR", ex);
            }
        }
    }

    /* BILL WS */
    public TvResponse queryTVPayment(TvQueryRequest acctBalReq, boolean isMain)
    {

        TvResponse cNCustProf = new TvResponse();
        TXRequest tXRequest = new TXRequest();

        setXapiCaller(new XAPICaller());
        tXRequest.setAccessCode(acctBalReq.getDecoderNo());
        startTime = System.currentTimeMillis();

        terminalType = (acctBalReq.getChannelId() == 8L ? "POS" : "MOBILE");
        getXapiCaller().setAccessCode(acctBalReq.getAccessCode());

        getXapiCaller().setAccountNo(acctBalReq.getFromAccount());
        getXapiCaller().setAccountPmtType(acctBalReq.getAccountPmtType());

        getXapiCaller().setCurrency(acctBalReq.getCurrency());
        getXapiCaller().setRefNumber(acctBalReq.getReference());

        getXapiCaller().setTxnAmount(BigDecimal.ZERO);
        try
        {
            starttimesProcess.StarTimesValidate(acctBalReq.getDecoderNo());
            cNCustProf.setAccountName(HeaderHandlerSTTValidator.cNCustProf.getAccountName());

            cNCustProf.setReference(HeaderHandlerSTTValidator.cNCustProf.getReference());
            cNCustProf.setTvType(HeaderHandlerSTTValidator.cNCustProf.getTvType());

            cNCustProf.setChanellId(acctBalReq.getChannelId());
            cNCustProf.setPaymentType(HeaderHandlerSTTValidator.cNCustProf.getPaymentType());

            cNCustProf.setCurrency(acctBalReq.getCurrency());
            cNCustProf.setRespCode(HeaderHandlerSTTValidator.cNCustProf.getRespCode());

            //  System.err.println("hello " + HeaderHandlerSTTValidator.cNCustProf.getRespCode());
        }
        catch (Exception e)
        {
            ExtPayMain.bRLogger.logError("PostXML-ERROR", e);
        }

        getXapiCaller().setTxnDescription(acctBalReq.getTranNarration());
        getXapiCaller().setXapiRespCode(cNCustProf.getRespCode());
        getXapiCaller().setMainRequest(acctBalReq);

        getXapiCaller().setMainResponse(cNCustProf);
        getXapiCaller().settXRequest(tXRequest);

        getXapiCaller().setDuration(String.valueOf(System.currentTimeMillis() - startTime) + " Ms");
        if (isMain)
        {
            logTVTransaction(tXRequest, terminalType, "BL01Q", "POS", pmtType, cNCustProf);
            LogEventMessage();
        }
        return cNCustProf;
    }

    public TvResponse TVPayment(TVPaymentRequest acctBalReq)
    {
          ExtPayMain.bRLogger.logEvent("<TVPaymentRequest>",    convertToString(acctBalReq));
        setXapiCaller(new XAPICaller());
        TvResponse tvResponse = new TvResponse();

        TXRequest txRequest = new TXRequest();
        TXClient1 txclient = new TXClient1(getXapiCaller());

        String respCode;
        String respRef;
        int billerProdId;
        Long billerBuId;

        String chargeRefId;
        BigDecimal availBalance, MainBalance;

        Boolean ChargerProcess = false;
        pmtType = "TV";

        startTime = System.currentTimeMillis();
        CNAccount cNAccount;

        cNAccount = queryBillerAccount("STT");
        BillerCharge billerCharge;

        // billerCharge = acctBalReq.getCurrency().equalsIgnoreCase("USD") ? queryBillerCharge(BRController.TVChargeCDF) : queryBillerCharge("714");
        billerProdId = getAccountProductId(cNAccount.getAccountNumber());
        billerBuId = getAccountBuId(acctBalReq.getFromAccount());
        billerCharge = queryBillerCharge(BRController.TVChargeCDF, billerProdId, cNAccount.getAccountNumber());

//        String billerCode = billerCharge.getChargeCode();
//        String billerDesc = billerCharge.getChargeDesc();
//        String billerStatus = billerCharge.getChargeStatus();
//        String billerType = billerCharge.getChargeType();
//        String billerGl = billerCharge.getIncomeGl();
//
//        BigDecimal billFlatAmt = billerCharge.getChargeFlatAmt();
//        BigDecimal billPCTAmt = billerCharge.getChargePCTAmt();
//        String billTaxDesc = billerCharge.getTaxDesc();
//        String billTaxGL = billerCharge.getTaxGl();
//        BigDecimal billTaxRate = billerCharge.getTaxRate();
        TvQueryRequest tvQueryRequest = new TvQueryRequest();
        tvQueryRequest.setDecoderNo(acctBalReq.getDecoderNo());
        tvQueryRequest.setChannelId(acctBalReq.getChannelId());
        tvQueryRequest.setAccessCode(acctBalReq.getAccessCode());
        tvQueryRequest.setFromAccount(acctBalReq.getFromAccount());
        tvQueryRequest.setAccountPmtType(acctBalReq.getAccountPmtType());
        tvQueryRequest.setCurrency(acctBalReq.getCurrency());
        tvQueryRequest.setReference(acctBalReq.getReference());
        txRequest.setReference(acctBalReq.getReference());

        String tvCustName = queryTVPayment(tvQueryRequest, false).getAccountName();
        String queryRespCode = queryTVPayment(tvQueryRequest, false).getRespCode();
        String baquetType = queryTVPayment(tvQueryRequest, false).getPaymentType();

      
        if (!"CDF".equalsIgnoreCase(acctBalReq.getCurrency()))
        {
            tvResponse.setRespCode(EICodes.INVALID_TXN_CURRENCY);
            tvResponse.setAccountName("StarTimes");
            tvResponse.setReference("0");
            ExtPayMain.bRLogger.logEvent("<Response>", "Invalid Currency ~ " + convertToString(tvResponse));
            getXapiCaller().setXapiRespCode(tvResponse.getRespCode());
            LogEventMessage();
            return tvResponse;
        }

        if (!isBillerAcctValid(acctBalReq.getCurrency(), cNAccount.getAccountNumber()))
        {
            tvResponse.setRespCode(EICodes.INACTIVE_CARD_ACCOUNT);
            tvResponse.setAccountName("StarTimes");
            tvResponse.setReference("0");
            ExtPayMain.bRLogger.logEvent("<Response>", "Invalid Currency ~ " + convertToString(tvResponse));
            getXapiCaller().setXapiRespCode(tvResponse.getRespCode());
            LogEventMessage();
            return tvResponse;
        }

        if (acctBalReq.getChargeAmount().compareTo(BigDecimal.ZERO) > 0)
        {
            ChargerProcess = true;
        }

        if (!isGLValid(acctBalReq.getFromAccount()) && !acctBalReq.getAccountPmtType().equals("Agent"))
        {
            acctBalReq.setAccountPmtType("Agent");
            txRequest.setCreditAccount(acctBalReq.getFromAccount());

            txRequest.setAccountNumber1(acctBalReq.getFromAccount());
            terminalType = (acctBalReq.getChannelId() == 8L ? "POS-BRANCH" : "MOBILE");
        }
        else
        {
            txRequest.setCreditAccount(acctBalReq.getFromAccount());
            txRequest.setAccountNumber1(acctBalReq.getFromAccount());
            terminalType = (acctBalReq.getChannelId() == 8L ? "POS-Agent" : "MOBILE");

        }

        if (!(queryRespCode).equalsIgnoreCase("0"))
        {
            tvResponse.setRespCode(EICodes.CARD_DEACTIVATED);
            tvResponse.setAccountName("StarTimes");
            tvResponse.setReference("0");
            getXapiCaller().setXapiRespCode(tvResponse.getRespCode());
            ExtPayMain.bRLogger.logEvent("<Response>", "Invalid Record ~ " + convertToString(tvResponse));
            LogEventMessage();
            return tvResponse;
        }

        txRequest.setAccountNumber1(acctBalReq.getFromAccount());
        txRequest.setChargeNarration(acctBalReq.getChargeNarration());

        txRequest.setAccountNumber2(cNAccount.getAccountNumber());
        txRequest.setReferenceType(acctBalReq.getDecoderNo());

        txRequest.setDebitAccount(acctBalReq.getFromAccount());
        txRequest.setCurrencyCode(acctBalReq.getCurrency());

        txRequest.setTxnAmount(acctBalReq.getTxnAmt());
        txRequest.setTxnNarration(acctBalReq.getTranNarration());

        txRequest.setChargeAmount(acctBalReq.getChargeAmount());
        txRequest.setChargeCreditLedger(acctBalReq.getChargeLedger());

        txRequest.setChargeDebitAccount(acctBalReq.getFromAccount());
        txRequest.setTaxAmount(acctBalReq.getTaxAmount());

        txRequest.setTaxCreditLedger(acctBalReq.getTaxLedger());
        txRequest.setTaxNarration(acctBalReq.getTaxNarration());

        txRequest.setAccessCode(acctBalReq.getAccessCode());
        txRequest.setChannelId(acctBalReq.getChannelId());

        txRequest.setChannelCode(querryServiceCode(acctBalReq.getChannelId()));
        txRequest.setTxnDate(taxDtformat.format(new Date()));
        txRequest.setAccountPmtType(acctBalReq.getAccountPmtType());

        txRequest.setCurrentBu(getCurrentBuid(cNAccount.getAccountNumber()));
        ExtPayMain.bRLogger.logEvent("<TXRequest>", convertToString(txRequest));

        getXapiCaller().setAccessCode(acctBalReq.getAccessCode());
        getXapiCaller().setAccountNo(acctBalReq.getFromAccount());

        getXapiCaller().setAccountPmtType(acctBalReq.getAccountPmtType());
        getXapiCaller().setCurrency(acctBalReq.getCurrency());

        getXapiCaller().setRefNumber(acctBalReq.getReference());
        getXapiCaller().setTxnAmount(acctBalReq.getTxnAmt());

        tvQueryRequest.setChannelId(acctBalReq.getChannelId());
        getXapiCaller().setTxnDescription(acctBalReq.getTranNarration());

        getXapiCaller().setMainRequest(acctBalReq);
        try
        {
            if (!isBalanceSufficient(txRequest))
            {
                tvResponse.setRespCode(EICodes.INSUFFICIENT_FUNDS_1);
                tvResponse.setAccountName("StarTimes");
                tvResponse.setReference("0");
                ExtPayMain.bRLogger.logEvent("<Response>", "INSUFFICIENT BALANCE ~ " + convertToString(tvResponse));
                getXapiCaller().setXapiRespCode(tvResponse.getRespCode());
                LogEventMessage();
                return tvResponse;
            }
            else
            {
                switch (acctBalReq.getAccountPmtType())
                {
                    case "Agent":
                    {
                        try
                        {
                            Object response = txclient.postDpToDpFundsTransfer(txRequest);
                            if (response instanceof FundsTransferOutputData)
                            {
                                respCode = String.valueOf((((FundsTransferOutputData) response).getResponseCode()));
                                //   txnJournalId = getTagValue((((FundsTransferOutputData) response).getRetrievalReferenceNumber()), "TxnId");
                                if (ChannelUtil.SERVICE_OFFLINE)
                                {
                                    txnJournalId = String.valueOf(fetchTxnJournal(acctBalReq.getDecoderNo()));
                                }
                                else
                                {
                                    txnJournalId = String.valueOf(((FundsTransferOutputData) response).getRetrievalReferenceNumber());
                                }
                                tvResponse.setTxnJournalId(txnJournalId);

                                if (successResponse.equals((((FundsTransferOutputData) response).getResponseCode())))
                                {
                                    if (ChargerProcess)
                                    {
                                        if (txclient.processChannelCharge(txRequest))
                                        {
                                            ExtPayMain.bRLogger.logEvent("Channel charge Success", "");
                                        }
                                        else
                                        {
                                            ExtPayMain.bRLogger.logEvent("Channel charge Fail", "");
                                        }
                                    }
                                    if (BRController.ApplyChargeTV.equals("Y"))
                                    {
                                        if (txclient.processBillerCharge(txRequest))
                                        {
                                            ExtPayMain.bRLogger.logEvent("Core charge success", "");
                                            chargeJournal = xapiCaller.getChargeId();
                                        }
                                        else
                                        {
                                            ExtPayMain.bRLogger.logEvent("Core charge Fail", "");
                                            chargeJournal = "0";
                                        }
                                    }
                                    tvResponse.setRespCode(respCode);
                                }

                                Object balanceResponse = txclient.queryDepositAccountBalance(this, txRequest);
                                availBalance = ((AccountBalanceOutputData) balanceResponse).getAvailableBalance();

                                tvResponse.setAccountName(acctBalReq.getUtilityType());
                                tvResponse.setChanellId(acctBalReq.getChannelId());

                                tvResponse.setCurrency(txRequest.getCurrencyCode());
                                tvResponse.setTvType(acctBalReq.getUtilityType());

                                tvResponse.setAccountName(tvCustName);
                                tvResponse.setReference(txRequest.getReference());

                                tvResponse.setDebitAccount(txRequest.getAccountNumber1());
                                tvResponse.setCreditAccount(txRequest.getAccountNumber2());

                                tvResponse.setAccountNumber(txRequest.getAccountNumber1());
                                tvResponse.setAmount(txRequest.getTxnAmount());

                                tvResponse.setBalance(availBalance);
                                tvResponse.setChargeJournalId(xapiCaller.getChargeId());

                                tvResponse.setTvType(baquetType);
                                ExtPayMain.bRLogger.logEvent("<Response>", convertToString(tvResponse));

                                logTransaction(txRequest, respCode, "BL01", terminalType, pmtType);

                            }
                        }
                        catch (Exception e)
                        {
                            if (e instanceof XAPIException)
                            {
                                ExtPayMain.bRLogger.logEvent("ERROR", e);
                                tvResponse.setRespCode(((XAPIException) e).getErrorCode());
                                tvResponse.setAccountName("StarTimes");
                                tvResponse.setReference("0");
                                logTransaction(txRequest, ((XAPIException) e).getErrorCode(), "BL01", terminalType, pmtType);
                            }
                            else
                            {
                                ExtPayMain.bRLogger.logEvent("ERROR", e);
                                tvResponse.setRespCode(getTagValue(String.valueOf(e), "error-code"));
                                tvResponse.setAccountName("StarTimes");
                                tvResponse.setReference("0");
                                logTransaction(txRequest, getTagValue(String.valueOf(e), "error-code"), "BL01", terminalType, pmtType);
                            }
                        }

                        logTVTransaction(txRequest, terminalType, "BL01P", "POS", pmtType, tvResponse);
                        getXapiCaller().setXapiRespCode(tvResponse.getRespCode());
                        getXapiCaller().setMainRequest(acctBalReq);

                        getXapiCaller().setMainResponse(tvResponse);
                        getXapiCaller().settXRequest(txRequest);

                        getXapiCaller().setDuration(String.valueOf(System.currentTimeMillis() - startTime) + " Ms");
                        LogEventMessage();
                        return tvResponse;
                    }
                    case "Branch":
                    {
                        try
                        {
                            Object response = txclient.postGLToDepositTransfer(txRequest);

                            if (response instanceof TxnResponseOutputData)
                            {
                                respCode = String.valueOf((((TxnResponseOutputData) response).getResponseCode()));
                                respRef = String.valueOf((((TxnResponseOutputData) response).getRetrievalReferenceNumber()));

                                if (ChannelUtil.SERVICE_OFFLINE)
                                {
                                    txnJournalId = String.valueOf(fetchTxnJournal(acctBalReq.getDecoderNo()));
                                }
                                else
                                {
                                    txnJournalId = getTagValue((((TxnResponseOutputData) response).getRetrievalReferenceNumber()), "TxnId");
                                }

                                tvResponse.setTxnJournalId(txnJournalId);

                                if (successResponse.equals(respCode))
                                {
                                    if (ChargerProcess)
                                    {
                                        if (txclient.processChannelCharge(txRequest))
                                        {
                                            ExtPayMain.bRLogger.logEvent("Channel charge Success", "");
                                        }
                                        else
                                        {
                                            ExtPayMain.bRLogger.logEvent("Channel charge Fail", "");
                                        }
                                    }
                                    if (BRController.ApplyChargeTV.equals("Y"))
                                    {
                                        if (txclient.processBillerCharge(txRequest))
                                        {
                                            ExtPayMain.bRLogger.logEvent("Core charge success", "");
                                            chargeJournal = xapiCaller.getChargeId();
                                        }
                                        else
                                        {
                                            ExtPayMain.bRLogger.logEvent("Core charge Fail", "");
                                            chargeJournal = "0";
                                        }
                                    }
                                    tvResponse.setRespCode(respCode);
                                }
                                Object balanceResponse = txclient.queryDepositAccountBalance(this, txRequest);
                                availBalance = ((AccountBalanceOutputData) balanceResponse).getAvailableBalance();

                                tvResponse.setAccountName(acctBalReq.getAccountPmtType());
                                tvResponse.setChanellId(txRequest.getChannelId());

                                tvResponse.setCurrency(txRequest.getCurrencyCode());
                                tvResponse.setTvType(baquetType);

                                tvResponse.setAccountName(tvCustName);
                                tvResponse.setReference(txRequest.getReference());

                                tvResponse.setDebitAccount(txRequest.getAccountNumber1());
                                tvResponse.setCreditAccount(txRequest.getAccountNumber2());

                                tvResponse.setAccountNumber(txRequest.getAccountNumber1());
                                tvResponse.setAmount(txRequest.getTxnAmount());

                                tvResponse.setBalance(availBalance);
                                tvResponse.setChargeJournalId(xapiCaller.getChargeId());

                                ExtPayMain.bRLogger.logEvent("<Response>", convertToString(tvResponse));
                                logTransaction(txRequest, respCode, "BL01", terminalType, pmtType);
                            }
                        }
                        catch (Exception e)
                        {
                            if (e instanceof XAPIException)
                            {
                                ExtPayMain.bRLogger.logEvent("ERROR", e);
                                tvResponse.setRespCode(((XAPIException) e).getErrorCode());
                                tvResponse.setAccountName("StarTimes");
                                tvResponse.setReference("0");
                                logTransaction(txRequest, ((XAPIException) e).getErrorCode(), "BL01", terminalType, pmtType);
                            }
                            else
                            {
                                ExtPayMain.bRLogger.logEvent("ERROR", e);
                                tvResponse.setRespCode(getTagValue(String.valueOf(e), "error-code"));
                                tvResponse.setAccountName("StarTimes");
                                tvResponse.setReference("0");
                                logTransaction(txRequest, getTagValue(String.valueOf(e), "error-code"), "BL01", terminalType, pmtType);
                            }
                        }
                        logTVTransaction(txRequest, terminalType, "BL01P", "POS", pmtType, tvResponse);
                        getXapiCaller().setXapiRespCode(tvResponse.getRespCode());
                        getXapiCaller().setMainRequest(acctBalReq);

                        getXapiCaller().setMainResponse(tvResponse);
                        getXapiCaller().settXRequest(txRequest);

                        getXapiCaller().setDuration(String.valueOf(System.currentTimeMillis() - startTime) + " Ms");
                        LogEventMessage();

                        return tvResponse;
                    }
                    default:
                        ExtPayMain.bRLogger.logEvent("ERROR", tvResponse);
                        tvResponse.setRespCode(EICodes.SYSTEM_ERROR);
                        tvResponse.setAccountName("StarTimes");
                        tvResponse.setReference("0");
                        getXapiCaller().setXapiRespCode(tvResponse.getRespCode());
                        LogEventMessage();
                        return tvResponse;
                }
            }
        }
        catch (Exception e)
        {
            ExtPayMain.bRLogger.logEvent("ERROR", tvResponse);
            tvResponse.setRespCode(EICodes.SYSTEM_ERROR);
            tvResponse.setAccountName("StarTimes");
            tvResponse.setReference("0");
            getXapiCaller().setXapiRespCode(tvResponse.getRespCode());
            LogEventMessage();
            return tvResponse;
        }

    }

    public SchFeesResponse queryFeePayment(SchFeesQueryRequest acctBalReq)
    {
        startTime = System.currentTimeMillis();
        setXapiCaller(new XAPICaller());
        SchFeesResponse cNCustProf = new SchFeesResponse();
        ExtPayMain.bRLogger.logEvent("<Request>", convertToString(acctBalReq));

        String StudentName = fetchSchoolDetail(acctBalReq.getStudentId().toUpperCase(), acctBalReq.getSchoolId(), acctBalReq.getCurrency()).get(0);
        String StudentNo = fetchSchoolDetail(acctBalReq.getStudentId(), acctBalReq.getSchoolId(), acctBalReq.getCurrency()).get(1);

        String Crncy = fetchSchoolDetail(acctBalReq.getStudentId(), acctBalReq.getSchoolId(), acctBalReq.getCurrency()).get(2);
        String SchoolName = fetchSchoolDetail(acctBalReq.getStudentId(), acctBalReq.getSchoolId(), acctBalReq.getCurrency()).get(3);

        String AccountNo = fetchSchoolDetail(acctBalReq.getStudentId(), acctBalReq.getSchoolId(), acctBalReq.getCurrency()).get(4);
        String RespCode = fetchSchoolDetail(acctBalReq.getStudentId(), acctBalReq.getSchoolId(), acctBalReq.getCurrency()).get(5);

        getXapiCaller().setAccessCode(acctBalReq.getAccessCode());
        getXapiCaller().setAccountNo(acctBalReq.getFromAccount());

        getXapiCaller().setAccountPmtType(acctBalReq.getAccountPmtType());
        getXapiCaller().setCurrency(acctBalReq.getCurrency());

        getXapiCaller().setRefNumber(acctBalReq.getReference());
        getXapiCaller().setTxnAmount(acctBalReq.getTxnAmt());

        getXapiCaller().setMainRequest(acctBalReq);
        getXapiCaller().setTxnDescription(acctBalReq.getTranNarration());
        try
        {
            cNCustProf.setAccountName(StudentName);
            cNCustProf.setSchoolName(SchoolName);

            cNCustProf.setChanellId(acctBalReq.getChannelId());
            cNCustProf.setCurrency(Crncy);

            cNCustProf.setStudentNo(StudentNo);
            cNCustProf.setReference(acctBalReq.getReference());

            cNCustProf.setCreditAccount(AccountNo);
            cNCustProf.setRespCode(RespCode);
            cNCustProf.setStudentName(StudentName);
            cNCustProf.setTxnJournalId("0");

            ExtPayMain.bRLogger.logEvent("<Response>", convertToString(cNCustProf));
            //return cNCustProf;
        }
        catch (Exception e)
        {
            cNCustProf.setRespCode(EICodes.SYSTEM_ERROR);
            ExtPayMain.bRLogger.logError("ERROR", e);
            //  return cNCustProf;
        }

        getXapiCaller().setXapiRespCode(cNCustProf.getRespCode());
        getXapiCaller().setMainRequest(acctBalReq);

        getXapiCaller().setMainResponse(cNCustProf);
        //getXapiCaller().settXRequest(txRequest);
        getXapiCaller().setDuration(String.valueOf(System.currentTimeMillis() - startTime) + " Ms");
        LogEventMessage();
        return cNCustProf;
    }

    public SchFeesResponse feePayment(SchFeePmtRequest acctBalReq)
    {

        setXapiCaller(new XAPICaller());
        Boolean ChargerProcess = false;
        String respCode = null;
        int billerProdId;
        Long billerBuId;

        BigDecimal availBalance, MainBalance;
        pmtType = "FEE";
        startTime = System.currentTimeMillis();

        SchFeesResponse schFeeResp = new SchFeesResponse();
        TXRequest txRequest = new TXRequest();

        TXClient1 txclient = new TXClient1(getXapiCaller());
        BillerCharge billerCharge;
        getXapiCaller().setCall("OriginalRequest", convertToString(acctBalReq));

        String StudentName = fetchSchoolDetail(acctBalReq.getStudentId().toUpperCase(), acctBalReq.getSchoolId(), acctBalReq.getCurrency()).get(0);
        String StudentNo = fetchSchoolDetail(acctBalReq.getStudentId(), acctBalReq.getSchoolId(), acctBalReq.getCurrency()).get(1);

        String Crncy = fetchSchoolDetail(acctBalReq.getStudentId(), acctBalReq.getSchoolId(), acctBalReq.getCurrency()).get(2);
        String SchoolName = fetchSchoolDetail(acctBalReq.getStudentId(), acctBalReq.getSchoolId(), acctBalReq.getCurrency()).get(3);

        String AccountNo = fetchSchoolDetail(acctBalReq.getStudentId(), acctBalReq.getSchoolId(), acctBalReq.getCurrency()).get(4);

        String schRespCode = fetchSchoolDetail(acctBalReq.getStudentId(), acctBalReq.getSchoolId(), acctBalReq.getCurrency()).get(5);

        billerProdId = getAccountProductId(AccountNo);
        billerBuId = getAccountBuId(acctBalReq.getFromAccount());
        billerCharge = acctBalReq.getCurrency().equalsIgnoreCase("USD") ? queryBillerCharge(BRController.SchChargeUSD, billerProdId, AccountNo)
                : queryBillerCharge(BRController.SchChargeCDF, billerProdId, AccountNo);
        String billerCode = billerCharge.getChargeCode();

        String billerDesc = billerCharge.getChargeDesc();
        String billerStatus = billerCharge.getChargeStatus();

        String billerType = billerCharge.getChargeType();
        String billerGl = billerCharge.getIncomeGl();

        BigDecimal billFlatAmt = billerCharge.getChargeFlatAmt();
        BigDecimal billPCTAmt = billerCharge.getChargePCTAmt();

        String billTaxDesc = billerCharge.getTaxDesc();
        String billTaxGL = unmaskGLAccount(billerCharge.getTaxGl(), billerBuId);
        BigDecimal billTaxRate = billerCharge.getTaxRate();

        if (!schRespCode.equalsIgnoreCase(successResponse))
        {
            schFeeResp.setRespCode(EICodes.INVALID_ACCOUNT);
            ExtPayMain.bRLogger.logEvent("<Response>", "Account Not Found ~ " + convertToString(schFeeResp));
            getXapiCaller().setXapiRespCode(schFeeResp.getRespCode());
            schFeeResp.setAccountName("N/A");
            schFeeResp.setReference("0");
            LogEventMessage();
            return schFeeResp;
        }

        if (acctBalReq.getChargeAmount().compareTo(BigDecimal.ZERO) > 0)
        {
            ChargerProcess = true;
        }

        if (!isGLValid(acctBalReq.getFromAccount()) && !acctBalReq.getAccountPmtType().equals("Agent"))
        {
            acctBalReq.setAccountPmtType("Agent");
            txRequest.setCreditAccount(acctBalReq.getFromAccount());
            terminalType = (acctBalReq.getChannelId() == 8L ? "POS-BRANCH" : "MOBILE");

        }
        else
        {
            txRequest.setCreditAccount(acctBalReq.getFromAccount());
            terminalType = (acctBalReq.getChannelId() == 8L ? "POS-Agent" : "MOBILE");
        }

        if (!isSchFeeValid(acctBalReq.getCurrency(), AccountNo))
        {
            schFeeResp.setRespCode(EICodes.UNSUPPORTED_ACCOUNT_CATEGORY);
            schFeeResp.setAccountName("UNSUPP_ACCOUNT_CATEGORY);\n"
                    + "            schFeeResp.setAccountName(\"SUPORTED_ACCOUNT_CATEGORY");
            schFeeResp.setReference("0");
            ExtPayMain.bRLogger.logEvent("<Response>", "Invalid Currency ~ " + convertToString(schFeeResp));
            getXapiCaller().setXapiRespCode(schFeeResp.getRespCode());
            LogEventMessage();
            return schFeeResp;
        }

        try
        {
            txRequest.setBillerchargeCd(billerCode);
            txRequest.setBillerchargeDesc(billerDesc);

            txRequest.setBillerchargeStatus(billerStatus);
            txRequest.setBillerincomeGl(unmaskGLAccount(billerGl, billerBuId));

            txRequest.setBillerchargeType(billerType);
            txRequest.setBillerchargeFlatAmt(billerType.equalsIgnoreCase("AMT") ? billFlatAmt
                    : ((billPCTAmt.divide(BigDecimal.valueOf(100))).multiply(acctBalReq.getTxnAmt())));

            txRequest.setBillerchargePCTAmt(billPCTAmt);
            txRequest.setBillerTaxDesc(billTaxDesc);

            txRequest.setBillerTaxGl(unmaskGLAccount(billTaxGL, billerBuId));
            txRequest.setBillerTaxRate(billTaxRate);

            txRequest.setReference(acctBalReq.getReference());

            txRequest.setAccountNumber1(acctBalReq.getFromAccount());
            txRequest.setAccountNumber2(AccountNo);

            txRequest.setDebitAccount(acctBalReq.getFromAccount());
            txRequest.setCurrencyCode(acctBalReq.getCurrency());

            txRequest.setToCurrencyCode(Crncy);
            txRequest.setReferenceType(acctBalReq.getStudentId());

            txRequest.setTxnAmount(acctBalReq.getTxnAmt());
            txRequest.setTxnNarration(acctBalReq.getTranNarration());

            txRequest.setChargeAmount(acctBalReq.getChargeAmount());
            txRequest.setChargeCreditLedger(acctBalReq.getChargeLedger());

            txRequest.setChargeDebitAccount(acctBalReq.getFromAccount());
            txRequest.setTaxAmount(acctBalReq.getTaxAmount());

            txRequest.setTaxCreditLedger(acctBalReq.getTaxLedger());
            txRequest.setTaxNarration(acctBalReq.getTaxNarration());

            txRequest.setAccessCode(acctBalReq.getAccessCode());
            txRequest.setChannelId(acctBalReq.getChannelId());

            txRequest.setChannelCode(querryServiceCode(acctBalReq.getChannelId()));
            txRequest.setCurrentBu(getCurrentBuid(AccountNo));

            txRequest.setTxnDate(taxDtformat.format(new Date()));
            txRequest.setChargeNarration(acctBalReq.getChargeNarration());
            txRequest.setAccountPmtType(acctBalReq.getAccountPmtType());

            getXapiCaller().setAccessCode(acctBalReq.getAccessCode());
            getXapiCaller().setAccountNo(acctBalReq.getFromAccount());

            getXapiCaller().setAccountPmtType(acctBalReq.getAccountPmtType());
            getXapiCaller().setCurrency(acctBalReq.getCurrency());

            getXapiCaller().setRefNumber(acctBalReq.getReference());
            getXapiCaller().setTxnAmount(acctBalReq.getTxnAmt());

            getXapiCaller().setMainRequest(acctBalReq);
            getXapiCaller().setTxnDescription(acctBalReq.getTranNarration());

            ExtPayMain.bRLogger.logEvent("<TXRequest>", convertToString(txRequest));

            if (!isBalanceSufficient(txRequest))
            {
                schFeeResp.setRespCode(EICodes.INSUFFICIENT_FUNDS_1);
                schFeeResp.setAccountName("N/A");
                schFeeResp.setReference("0");
                ExtPayMain.bRLogger.logEvent("<Response>", "INSUFFICIENT BALANCE ~ " + convertToString(schFeeResp));
                LogEventMessage();
                return schFeeResp;
            }
            else
            {
                switch (acctBalReq.getAccountPmtType())
                {
                    case "Agent":
                    {
                        try
                        {
                            Object response = txclient.postDpToDpFundsTransfer(txRequest);
                            ExtPayMain.bRLogger.logEvent("<postDpToDpFundsTransfer>", convertToString(response));
                            if (response instanceof FundsTransferOutputData)
                            {
                                respCode = String.valueOf(((FundsTransferOutputData) response).getResponseCode());
                                txnJournalId = String.valueOf(((FundsTransferOutputData) response).getRetrievalReferenceNumber());

                                if (successResponse.equals((((FundsTransferOutputData) response).getResponseCode())))
                                {
                                    if (ChargerProcess)
                                    {
                                        if (txclient.processChannelCharge(txRequest))
                                        {
                                            ExtPayMain.bRLogger.logEvent("Channel charge Success", "");
                                            // LogEventMessage();
                                        }
                                        else
                                        {
                                            ExtPayMain.bRLogger.logEvent("Channel charge Fail", "");
                                        }
                                    }
                                    if (BRController.ApplyChargeFee.equalsIgnoreCase("Y"))
                                    {
                                        if (txclient.processBillerCharge(txRequest))
                                        {
                                            ExtPayMain.bRLogger.logEvent("Core charge success", "");
                                            chargeJournal = xapiCaller.getChargeId();
                                            // LogEventMessage();
                                        }
                                        else
                                        {
                                            ExtPayMain.bRLogger.logEvent("Core charge Fail", "");
                                            chargeJournal = "0";
                                        }
                                    }
                                }
                                Object balanceResponse2 = txclient.queryDepositAccountBalance(this, txRequest);
                                availBalance = ((AccountBalanceOutputData) balanceResponse2).getAvailableBalance();

                                schFeeResp.setTxnJournalId(txnJournalId);
                                schFeeResp.setRespCode(respCode);

                                schFeeResp.setAccountName(StudentName);
                                schFeeResp.setSchoolId(acctBalReq.getSchoolId());

                                schFeeResp.setStudentName(StudentName);
                                schFeeResp.setSchoolName(SchoolName);

                                schFeeResp.setChanellId(acctBalReq.getChannelId());
                                schFeeResp.setCurrency(Crncy);

                                schFeeResp.setStudentNo(StudentNo);

                                schFeeResp.setReference(acctBalReq.getReference());

                                schFeeResp.setDebitAccount(txRequest.getAccountNumber1());
                                schFeeResp.setCreditAccount(txRequest.getAccountNumber2());

                                schFeeResp.setAccountNumber(txRequest.getAccountNumber1());
                                schFeeResp.setAmount(txRequest.getTxnAmount());

                                schFeeResp.setBalance(availBalance);
                                schFeeResp.setChargeJournalId(chargeJournal);
                                respCode = schFeeResp.getRespCode();
                                ExtPayMain.bRLogger.logEvent("<Response>", convertToString(schFeeResp));

                                logTransaction(txRequest, respCode, "BL02", terminalType, pmtType);
                                logFeeTransaction(txRequest, respCode, respCode, terminalType, pmtType, schFeeResp);
                            }
                        }
                        catch (Exception ex)
                        {
                            if (ex instanceof XAPIException || ex instanceof RemoteException)
                            {
                                ExtPayMain.bRLogger.logEvent("ERROR", ex);
                                schFeeResp.setRespCode(((XAPIException) ex).getErrorCode());
                                schFeeResp.setAccountName("N/A");
                                schFeeResp.setReference("0");
                                respCode = schFeeResp.getRespCode();
                                logTransaction(txRequest, ((XAPIException) ex).getErrorCode(), "BL02", terminalType, pmtType);
                                logFeeTransaction(txRequest, respCode, respCode, terminalType, pmtType, schFeeResp);
                            }
                            else
                            {
                                ExtPayMain.bRLogger.logEvent("ERROR", ex);
                                respCode = schFeeResp.getRespCode();
                                schFeeResp.setAccountName("N/A");
                                schFeeResp.setReference("0");
                                logTransaction(txRequest, getTagValue(String.valueOf(ex), "error-code"), "BL02", terminalType, pmtType);
                                logFeeTransaction(txRequest, respCode, respCode, terminalType, pmtType, schFeeResp);
                            }
                        }

                        getXapiCaller().setXapiRespCode(schFeeResp.getRespCode());
                        getXapiCaller().setMainRequest(acctBalReq);

                        getXapiCaller().setMainResponse(schFeeResp);
                        getXapiCaller().settXRequest(txRequest);

                        getXapiCaller().setDuration(String.valueOf(System.currentTimeMillis() - startTime) + " Ms");

                        LogEventMessage();
                        return schFeeResp;
                    }
                    case "Branch":
                    {
                        try
                        {
                            Object response = txclient.postGLToDepositTransfer(txRequest);
                            if (response instanceof TxnResponseOutputData)
                            {
                                respCode = String.valueOf(((TxnResponseOutputData) response).getResponseCode());
                                txnJournalId = getTagValue(String.valueOf(((TxnResponseOutputData) response).getRetrievalReferenceNumber()), "TxnId");

                                if (successResponse.equals((((TxnResponseOutputData) response).getResponseCode())))
                                {
                                    if (ChargerProcess)
                                    {
                                        if (txclient.processChannelCharge(txRequest))
                                        {
                                            ExtPayMain.bRLogger.logEvent("Channel charge Success", "");
                                        }
                                        else
                                        {
                                            ExtPayMain.bRLogger.logEvent("Channel charge Fail", "");
                                        }
                                    }
                                    if (BRController.ApplyChargeFee.equalsIgnoreCase("Y"))
                                    {
                                        if (txclient.processBillerCharge(txRequest))
                                        {
                                            ExtPayMain.bRLogger.logEvent("Core charge success", "");
                                            chargeJournal = xapiCaller.getChargeId();
                                        }
                                        else
                                        {
                                            ExtPayMain.bRLogger.logEvent("Core charge Fail", "");
                                            chargeJournal = "0";
                                        }
                                    }
                                }
                                Object branchBalanceResponse = txclient.queryDepositAccountBalance(this, txRequest);
                                availBalance = ((AccountBalanceOutputData) branchBalanceResponse).getAvailableBalance();

                                schFeeResp.setTxnJournalId(txnJournalId);
                                schFeeResp.setRespCode(respCode);

                                schFeeResp.setAccountName(fetchSchoolDetail(acctBalReq.getStudentId(), acctBalReq.getSchoolId(), acctBalReq.getCurrency()).get(0));
                                schFeeResp.setSchoolName(fetchSchoolDetail(acctBalReq.getStudentId(), acctBalReq.getSchoolId(), acctBalReq.getCurrency()).get(3));

                                schFeeResp.setChanellId(acctBalReq.getChannelId());
                                schFeeResp.setCurrency(fetchSchoolDetail(acctBalReq.getStudentId(), acctBalReq.getSchoolId(), acctBalReq.getCurrency()).get(2));

                                schFeeResp.setStudentName(StudentName);
                                schFeeResp.setStudentNo(fetchSchoolDetail(acctBalReq.getStudentId(), acctBalReq.getSchoolId(), acctBalReq.getCurrency()).get(1));

                                schFeeResp.setDebitAccount(txRequest.getAccountNumber1());
                                schFeeResp.setCreditAccount(txRequest.getAccountNumber2());

                                schFeeResp.setAccountNumber(txRequest.getAccountNumber1());
                                schFeeResp.setAmount(txRequest.getTxnAmount());

                                schFeeResp.setBalance(availBalance);
                                ExtPayMain.bRLogger.logEvent("<Response>", convertToString(schFeeResp));

                                logTransaction(txRequest, respCode, "BL02", terminalType, pmtType);
                                logFeeTransaction(txRequest, respCode, respCode, pmtType, pmtType, schFeeResp);
                            }
                        }
                        catch (Exception ex)
                        {
                            if (ex instanceof XAPIException || ex instanceof RemoteException)
                            {
                                ExtPayMain.bRLogger.logEvent("ERROR", ex);
                                schFeeResp.setRespCode(((XAPIException) ex).getErrorCode());
                                schFeeResp.setAccountName("N/A");
                                schFeeResp.setReference("0");
                                respCode = schFeeResp.getRespCode();
                                logTransaction(txRequest, ((XAPIException) ex).getErrorCode(), "BL02", terminalType, pmtType);
                                logFeeTransaction(txRequest, respCode, respCode, terminalType, pmtType, schFeeResp);
                            }
                            else
                            {
                                ExtPayMain.bRLogger.logEvent("ERROR", ex);
                                respCode = schFeeResp.getRespCode();
                                schFeeResp.setAccountName("N/A");
                                schFeeResp.setReference("0");
                                logTransaction(txRequest, getTagValue(String.valueOf(ex), "error-code"), "BL02", terminalType, pmtType);
                                logFeeTransaction(txRequest, respCode, respCode, terminalType, pmtType, schFeeResp);
                            }
                        }
                        getXapiCaller().setXapiRespCode(schFeeResp.getRespCode());
                        getXapiCaller().setMainRequest(acctBalReq);

                        getXapiCaller().setMainResponse(schFeeResp);
                        getXapiCaller().settXRequest(txRequest);

                        getXapiCaller().setDuration(String.valueOf(System.currentTimeMillis() - startTime) + " Ms");
                        LogEventMessage();
                        return schFeeResp;

                    }

                    default:
                        schFeeResp.setRespCode(EICodes.SYSTEM_ERROR);
                        schFeeResp.setAccountName("N/A");
                        schFeeResp.setReference("0");
                        return schFeeResp;
                }
            }

        }
        catch (Exception e)
        {
            schFeeResp.setRespCode(EICodes.SYSTEM_ERROR);
            ExtPayMain.bRLogger.logEvent("ERROR", e);

            logTransaction(txRequest, "91", "BL02", terminalType, pmtType);
            return schFeeResp;
        }

    }

    public TaxResponse queryTaxPayment(TaxQueryRequest acctBalReq, boolean isMain)
    {
        startTime = System.currentTimeMillis();
        TaxResponse taxresp = new TaxResponse();
        TXRequest txRequest = new TXRequest();
        CNAccount cNAccount;
        setXapiCaller(new XAPICaller());

        terminalType = (acctBalReq.getChannelId() == 8L ? "POS-BRANCH" : "MOBILE");
        pmtType = "TAX";

        String etaxUrl = BRController.EtaxUrl;//"http://172.16.1.249:8080/erecettescpi/resources/generic";
        String bankname = BRController.BankName;//"finca";

        String bankToken = BRController.BankToken;//"NhaEI83CVvYsuR/Rx1PCGg==";        

        if (acctBalReq.getChannelId() == 9)
        {
            if (acctBalReq.getNP_number().startsWith("NP") || acctBalReq.getNP_number().startsWith("AMR"))
            {
                acctBalReq.setUtilityType(isErecetteTxn);
            }
        }

        ExtPayMain.bRLogger.logEvent("INFO", convertToString(acctBalReq));

        getXapiCaller().setAccessCode(acctBalReq.getAccessCode());
        getXapiCaller().setAccountNo(acctBalReq.getFromAccount());

        getXapiCaller().setAccountPmtType(acctBalReq.getAccountPmtType());
        getXapiCaller().setCurrency(acctBalReq.getCurrency());

        getXapiCaller().setRefNumber(acctBalReq.getReference());
        getXapiCaller().setTxnAmount(BigDecimal.ZERO);
        getXapiCaller().setTxnDescription(acctBalReq.getTranNarration());

        if (!isErecetteTxn.equals(acctBalReq.getUtilityType()))
        {

            cNAccount = queryBillerAccount(acctBalReq.getUtilityType());
            taxresp.setNotePerception(acctBalReq.getNP_number());
            taxresp.setAccountName(cNAccount.getAccountName() == null ? "Impot Foncier" : cNAccount.getAccountName());
            taxresp.setReference(acctBalReq.getReference());

            taxresp.setChanellId(acctBalReq.getChannelId());
            taxresp.setCurrency(acctBalReq.getCurrency());

            taxresp.setAmount(BigDecimal.ZERO);
            taxresp.setArticleGroup("KINSHASA");

            taxresp.setBudgetNaming("KINSHASA");
            taxresp.setIssueDate("N/A");

            taxresp.setPmtPeriod("N/A");
            taxresp.setDeadline("N/A");

            taxresp.setTxnJournalId("");
            txRequest.setAccountNumber1(acctBalReq.getFromAccount());

            taxresp.setRespCode(successResponse);
            ExtPayMain.bRLogger.logEvent("INFO", convertToString(taxresp));

            if (isMain)
            {
                logTaxTransaction(txRequest, terminalType, "BL03Q", "POS", pmtType, taxresp);
            }

            getXapiCaller().setXapiRespCode(taxresp.getRespCode());
            getXapiCaller().setMainRequest(acctBalReq);

            getXapiCaller().setMainResponse(taxresp);
            getXapiCaller().settXRequest(txRequest);

            getXapiCaller().setDuration(String.valueOf(System.currentTimeMillis() - startTime) + " Ms");
            LogEventMessage();
            return taxresp;
        }
        else
        {
            try
            {
                String querried = ETClient(acctBalReq.getNP_number(), etaxUrl, bankname, bankToken, 9L);
                if ("Success".equals(querried))
                {

                    taxresp.setAccountName(HttpETRClient.getName());
                    taxresp.setReference(HttpETRClient.getNumber());

                    taxresp.setAmount(HttpETRClient.getAmount());
                    taxresp.setChanellId(acctBalReq.getChannelId());

                    taxresp.setArticleGroup(HttpETRClient.getArticleGroup());
                    taxresp.setBudgetNaming(HttpETRClient.getBudgetNaming());

                    taxresp.setIssueDate(HttpETRClient.getIssueDate());
                    taxresp.setPmtPeriod(HttpETRClient.getPmtPeriod());

                    taxresp.setDeadline(HttpETRClient.getDeadline());
                    taxresp.setPaymentType("TAX Payment");

                    taxresp.setCurrency(acctBalReq.getCurrency());
                    taxresp.setRespCode(successResponse);

                    txRequest.setAccountNumber1(acctBalReq.getFromAccount());
                    taxresp.setTxnJournalId("0");

                    taxresp.setNotePerception(acctBalReq.getNP_number());

                    // ExtPayMain.bRLogger.logEvent("INFO", convertToString(taxresp));
                    getXapiCaller().setXapiRespCode(taxresp.getRespCode());
                    getXapiCaller().setMainRequest(acctBalReq);

                    getXapiCaller().setMainResponse(taxresp);
                    getXapiCaller().settXRequest(txRequest);

                    getXapiCaller().setDuration(String.valueOf(System.currentTimeMillis() - startTime) + " Ms");
                    if (isMain)
                    {
                        logTaxTransaction(txRequest, terminalType, "BL03Q", "POS", pmtType, taxresp);
                    }
                    LogEventMessage();
                    return taxresp;

                }
                else
                {
                    ExtPayMain.bRLogger.logEvent("ERROR[TaxQuery]", convertToString(taxresp));
                    switch (querried)
                    {
                        case "604":
                            taxresp.setRespCode("ACCOUNT_0096");
                            break;
                        case "603":
                            taxresp.setRespCode("EICODE_0010");
                            break;
                        case "602":
                            taxresp.setRespCode("EICODE_0010");
                            break;
                        case "601":
                            taxresp.setRespCode("EICODE_0010");
                            break;
                    }
                    if (isMain)
                    {
                        logTaxTransaction(txRequest, terminalType, "BL03Q", "POS", pmtType, taxresp);
                    }
                    LogEventMessage();
                    return taxresp;
                }
            }
            catch (JSONException ex)
            {
                ExtPayMain.bRLogger.logError("ERROR[queryTaxPayment]", ex);
                taxresp.setRespCode(EICodes.SYSTEM_ERROR);
                LogEventMessage();
                return taxresp;
            }
        }
    }

    public TaxResponse taxPayment(TaxPaymentRequest acctBalReq)
    {
        Boolean ChargerProcess = false;
        BigDecimal availBalance, MainBalance;
        int billerProdId;
        Long billerBuId;
        setXapiCaller(new XAPICaller());

        startTime = System.currentTimeMillis();
        pmtType = "TAX";

        TaxResponse taxResponse = new TaxResponse();
        TXRequest txRequest = new TXRequest();

        System.err.println("" + acctBalReq.getUtilityType());
        BillerCharge billerCharge;
        CNAccount cNAccount;
        cNAccount = queryBillerAccount((acctBalReq.getChannelId() == 8) ? String.valueOf(acctBalReq.getUtilityType()) : String.valueOf(acctBalReq.getTaxTypeId()));

        //billerCharge = acctBalReq.getCurrency().equalsIgnoreCase("USD") ? queryBillerCharge(BRController.SchChargeUSD) : queryBillerCharge(BRController.SchChargeCDF);
        billerProdId = getAccountProductId(cNAccount.getAccountNumber());
        billerBuId = getAccountBuId(acctBalReq.getFromAccount());
        //njnu
        ExtPayMain.bRLogger.logEvent("billerCharge = INFO", BRController.TaxChargeCDF + " .. " + billerProdId);
        //billerCharge = queryBillerCharge(BRController.TaxChargeCDF, billerProdId, cNAccount.getAccountNumber());
        billerCharge = acctBalReq.getCurrency().equalsIgnoreCase("USD") ? queryBillerCharge(BRController.TaxChargeUSD, billerProdId, cNAccount.getAccountNumber()) : queryBillerCharge(BRController.TaxChargeCDF, billerProdId, cNAccount.getAccountNumber());

        ExtPayMain.bRLogger.logEvent("billerCharge = INFO", convertToString(billerCharge));

        String billerCode = billerCharge.getChargeCode();

        String billerDesc = billerCharge.getChargeDesc();
        String billerStatus = billerCharge.getChargeStatus();

        String billerType = billerCharge.getChargeType();
        String billerGl = billerCharge.getIncomeGl();

        BigDecimal billFlatAmt = billerCharge.getChargeFlatAmt();
        BigDecimal billPCTAmt = billerCharge.getChargePCTAmt();

        String billTaxDesc = billerCharge.getTaxDesc();
        String billTaxGL = billerCharge.getTaxGl();
        BigDecimal billTaxRate = billerCharge.getTaxRate();

        System.err.println(acctBalReq.getCurrency() + ">>>>>>>>>>>>.... " + cNAccount.getAccountNumber());
        TaxQueryRequest taxQueryRequest = new TaxQueryRequest();

        if (acctBalReq.getNPNumber().startsWith("NP") || acctBalReq.getNPNumber().startsWith("AMR"))
        {
            taxQueryRequest.setUtilityType("291");
        }
        else
        {
            if (acctBalReq.getChannelId() != 8)
            {
                taxQueryRequest.setUtilityType(String.valueOf(acctBalReq.getUtilityType()));
                taxQueryRequest.setAccessCode(acctBalReq.getAccessCode());
                taxQueryRequest.setFromAccount(acctBalReq.getFromAccount());
                taxQueryRequest.setAccountPmtType(acctBalReq.getAccountPmtType());
                taxQueryRequest.setCurrency(acctBalReq.getCurrency());
                taxQueryRequest.setReference(acctBalReq.getReference());
                taxQueryRequest.setTxnAmt(acctBalReq.getTxnAmt());

            }
            else
            {
                taxQueryRequest.setUtilityType(String.valueOf(acctBalReq.getTaxTypeId()));
            }

        }
        taxQueryRequest.setNP_number(acctBalReq.getNPNumber());
        taxQueryRequest.setChannelId(acctBalReq.getChannelId());

        String TaxeClientName = queryTaxPayment(taxQueryRequest, false).getAccountName();
        TXClient1 txclient = new TXClient1(getXapiCaller());

        ExtPayMain.bRLogger.logEvent("INFO", convertToString(acctBalReq));

        if (acctBalReq.getChargeAmount().compareTo(BigDecimal.ZERO) > 0)
        {
            ChargerProcess = true;
        }
        if (!isGLValid(acctBalReq.getFromAccount()) && !acctBalReq.getAccountPmtType().equals("Agent"))
        {
            acctBalReq.setAccountPmtType("Agent");
            txRequest.setCreditAccount(acctBalReq.getFromAccount());

            txRequest.setAccountNumber1(acctBalReq.getFromAccount());
            terminalType = (acctBalReq.getChannelId() == 8L ? "POS-BRANCH" : "MOBILE");

        }
        else
        {
            txRequest.setCreditAccount(acctBalReq.getFromAccount());
            txRequest.setAccountNumber1(acctBalReq.getFromAccount());

            terminalType = (acctBalReq.getChannelId() == 8L ? "POS-Agent" : "MOBILE");
        }

        if (!isBillerAcctValid(acctBalReq.getCurrency(), cNAccount.getAccountNumber()))
        {
            taxResponse.setRespCode("EICODE_0009");
            taxResponse.setAccountName("N/A");
            taxResponse.setReference("0");
            ExtPayMain.bRLogger.logEvent("<Response>", "Invalid Currency ~ " + convertToString(taxResponse));
            getXapiCaller().setXapiRespCode(taxResponse.getRespCode());
            LogEventMessage();
            return taxResponse;
        }
        try
        {
            txRequest.setBillerchargeCd(billerCode);
            txRequest.setBillerchargeDesc(billerDesc);

            txRequest.setBillerchargeStatus(billerStatus);
            txRequest.setBillerincomeGl(unmaskGLAccount(billerGl, billerBuId));

            txRequest.setBillerchargeType(billerType);

            //switched the compare type to try to prevent NPE
            if (billPCTAmt.compareTo(BigDecimal.ZERO) >= 0)
            {
                txRequest.setBillerchargeFlatAmt("AMT".equalsIgnoreCase(billerType) ? billFlatAmt
                        : ((billPCTAmt.divide(BigDecimal.valueOf(100))).multiply(acctBalReq.getTxnAmt())));
            }
            else
            {
                txRequest.setBillerchargeFlatAmt(BigDecimal.ZERO);
            }
            txRequest.setBillerchargePCTAmt(billPCTAmt);
            txRequest.setBillerTaxDesc(billTaxDesc);

            txRequest.setBillerTaxGl(unmaskGLAccount(billTaxGL, billerBuId));
            txRequest.setBillerTaxRate(billTaxRate);

            txRequest.setAccountNumber2(cNAccount.getAccountNumber());
            txRequest.setReferenceType(acctBalReq.getNPNumber());

            txRequest.setCreditAccount(acctBalReq.getToAccount());
            txRequest.setCurrencyCode(acctBalReq.getCurrency());

            txRequest.setTxnAmount(acctBalReq.getTxnAmt());
            txRequest.setTxnNarration(acctBalReq.getTranNarration());

            txRequest.setChargeAmount(acctBalReq.getChargeAmount());
            txRequest.setChargeCreditLedger(acctBalReq.getChargeLedger());

            txRequest.setChargeDebitAccount(acctBalReq.getToAccount());
            txRequest.setTaxAmount(acctBalReq.getTaxAmount());

            txRequest.setTaxCreditLedger(acctBalReq.getTaxLedger());
            txRequest.setTaxNarration(acctBalReq.getTaxNarration());

            txRequest.setAccessCode(acctBalReq.getAccessCode());
            txRequest.setChannelId(acctBalReq.getChannelId());

            txRequest.setChannelCode(querryServiceCode(acctBalReq.getChannelId()));
            txRequest.setReference(acctBalReq.getReference());

            txRequest.setCurrentBu(getCurrentBuid(cNAccount.getAccountNumber()));
            txRequest.setDebitAccount(acctBalReq.getFromAccount());
            txRequest.setAccountPmtType(acctBalReq.getAccountPmtType());

            getXapiCaller().setAccessCode(acctBalReq.getAccessCode());
            getXapiCaller().setAccountNo(acctBalReq.getFromAccount());

            getXapiCaller().setAccountPmtType(acctBalReq.getAccountPmtType());
            getXapiCaller().setCurrency(acctBalReq.getCurrency());

            getXapiCaller().setRefNumber(acctBalReq.getReference());
            getXapiCaller().setTxnAmount(acctBalReq.getTxnAmt());

            getXapiCaller().setMainRequest(acctBalReq);
            getXapiCaller().setTxnDescription(acctBalReq.getTranNarration());
            ExtPayMain.bRLogger.logEvent("<TXRequest>", convertToString(txRequest));

            if (!isBalanceSufficient(txRequest))
            {
                taxResponse.setRespCode(EICodes.INSUFFICIENT_FUNDS_1);
                taxResponse.setAccountName("N/A");
                taxResponse.setReference("0");
                ExtPayMain.bRLogger.logEvent("<Response>", "INSUFFICIENT BALANCE ~ " + convertToString(taxResponse));
                LogEventMessage();
                return taxResponse;
            }
            else
            {
                switch (acctBalReq.getAccountPmtType())
                {
                    case "Agent":
                    {
                        try
                        {
                            Object response = txclient.postDpToDpFundsTransfer(txRequest);
                            if (response instanceof FundsTransferOutputData)
                            {

                                if (ChannelUtil.SERVICE_OFFLINE)
                                {
                                    txnJournalId = String.valueOf(fetchTxnJournal(acctBalReq.getNPNumber()));
                                }
                                else
                                {
                                    txnJournalId = String.valueOf(((FundsTransferOutputData) response).getRetrievalReferenceNumber());
                                }

                                taxResponse.setTxnJournalId(txnJournalId);
                                taxResponse.setRespCode(String.valueOf(((FundsTransferOutputData) response).getResponseCode()));

                                if (successResponse.equals((((FundsTransferOutputData) response).getResponseCode())))
                                {
                                    if (ChargerProcess)
                                    {
                                        if (txclient.processChannelCharge(txRequest))
                                        {
                                            ExtPayMain.bRLogger.logEvent("Channel charge Success", "");
                                        }
                                        else
                                        {
                                            ExtPayMain.bRLogger.logEvent("Channel charge Fail", "");
                                        }
                                    }
                                    if (BRController.ApplyChargeTAX.equalsIgnoreCase("Y"))
                                    {
                                        if (txclient.processBillerCharge(txRequest))
                                        {
                                            ExtPayMain.bRLogger.logEvent("Core charge success", "");
                                            chargeJournal = xapiCaller.getChargeId();
                                        }
                                        else
                                        {
                                            ExtPayMain.bRLogger.logEvent("Core charge Fail", "");
                                            chargeJournal = "0";
                                        }
                                    }
                                }
                                Object balanceResponse = txclient.queryDepositAccountBalance(this, txRequest);
                                availBalance = ((AccountBalanceOutputData) balanceResponse).getAvailableBalance();

                                taxResponse.setTxnJournalId(((FundsTransferOutputData) response).getRetrievalReferenceNumber());
                                taxResponse.setRespCode(String.valueOf(((FundsTransferOutputData) response).getResponseCode()));

                                taxResponse.setChanellId(txRequest.getChannelId());
                                taxResponse.setCurrency(txRequest.getCurrencyCode());

                                taxResponse.setReference(txRequest.getReference());

                                taxResponse.setDebitAccount(txRequest.getAccountNumber1());
                                taxResponse.setCreditAccount(txRequest.getAccountNumber2());

                                taxResponse.setAccountNumber(txRequest.getAccountNumber1());
                                taxResponse.setAmount(txRequest.getTxnAmount());

                                taxResponse.setNotePerception(acctBalReq.getNPNumber());
                                taxResponse.setChargeJournalId(xapiCaller.getChargeId());
                                if (acctBalReq.getNPNumber().startsWith("NP") || acctBalReq.getNPNumber().startsWith("AMR"))
                                {
                                    taxResponse.setAccountName(TaxeClientName);
                                }
                                else
                                {
                                    taxResponse.setAccountName(cNAccount.getAccountName());
                                }
                                taxResponse.setBalance(availBalance);
                                ExtPayMain.bRLogger.logEvent("INFO", convertToString(taxResponse));

                                logTransaction(txRequest, ((FundsTransferOutputData) response).getResponseCode(), "BL03", terminalType, pmtType);
                                logTaxTransaction(txRequest, terminalType, "BL03P", "POS", pmtType, taxResponse);
                            }
                        }
                        catch (Exception ex)
                        {
                            if (ex instanceof XAPIException)
                            {
                                ExtPayMain.bRLogger.logEvent("ERROR", ex);
                                taxResponse.setRespCode(((XAPIException) ex).getErrorCode());
                                taxResponse.setAccountName("N/A");
                                taxResponse.setReference("0");
                                logTransaction(txRequest, ((XAPIException) ex).getErrorCode(), "BL03", terminalType, pmtType);
                                logTaxTransaction(txRequest, terminalType, "BL03", "POS", pmtType, taxResponse);
                            }
                            else
                            {
                                ExtPayMain.bRLogger.logEvent("ERROR", ex);
                                taxResponse.setRespCode(getTagValue(String.valueOf(ex), "error-code"));
                                taxResponse.setAccountName("N/A");
                                taxResponse.setReference("0");
                                logTransaction(txRequest, (getTagValue(String.valueOf(ex), "error-code")), "BL03", terminalType, pmtType);
                                logTaxTransaction(txRequest, terminalType, "BL03P", "POS", pmtType, taxResponse);
                            }
                        }
                        //logTaxTransaction(txRequest, terminalType, "BL03P", "POS", pmtType, taxResponse);
                        getXapiCaller().setXapiRespCode(taxResponse.getRespCode());
                        getXapiCaller().setMainRequest(acctBalReq);

                        getXapiCaller().setMainResponse(taxResponse);
                        getXapiCaller().settXRequest(txRequest);
                        LogEventMessage();
                        return taxResponse;
                    }
                    case "Branch":
                    {
                        try
                        {
                            String retReference, respCode;
                            Object response = txclient.postGLToDepositTransfer(txRequest);

                            if (response instanceof TxnResponseOutputData)
                            {
                                retReference = getTagValue(String.valueOf(((TxnResponseOutputData) response).getRetrievalReferenceNumber()), "TxnId");
                                respCode = String.valueOf(((TxnResponseOutputData) response).getResponseCode());
                                taxResponse.setRespCode(respCode);
                                txnJournalId = retReference;

                                if (successResponse.equals((((TxnResponseOutputData) response).getResponseCode())))
                                {
                                    if (ChargerProcess)
                                    {
                                        if (txclient.processChannelCharge(txRequest))
                                        {
                                            ExtPayMain.bRLogger.logEvent("Channel charge Success", "");
                                        }
                                        else
                                        {
                                            ExtPayMain.bRLogger.logEvent("Channel charge Fail", "");
                                        }
                                    }
                                    if (BRController.ApplyChargeTAX.equalsIgnoreCase("Y"))
                                    {
                                        if (txclient.processBillerCharge(txRequest))
                                        {
                                            ExtPayMain.bRLogger.logEvent("Core charge success", "");
                                            chargeJournal = xapiCaller.getChargeId();
                                        }
                                        else
                                        {
                                            ExtPayMain.bRLogger.logEvent("Core charge Fail", "");
                                            chargeJournal = "0";
                                        }
                                    }
                                }
                                Object balanceResponse = txclient.queryDepositAccountBalance(this, txRequest);
                                availBalance = ((AccountBalanceOutputData) balanceResponse).getAvailableBalance();

                                if (acctBalReq.getNPNumber().startsWith("NP") || acctBalReq.getNPNumber().startsWith("AMR"))
                                {
                                    taxResponse.setAccountName(TaxeClientName);
                                }
                                else
                                {
                                    taxResponse.setAccountName(cNAccount.getAccountName());
                                }
                                taxResponse.setChanellId(acctBalReq.getChannelId());

                                taxResponse.setCurrency("CDF");

                                taxResponse.setTxnJournalId(retReference);
                                taxResponse.setRespCode(respCode);

                                taxResponse.setDebitAccount(txRequest.getAccountNumber1());
                                taxResponse.setCreditAccount(txRequest.getAccountNumber2());

                                taxResponse.setAccountNumber(txRequest.getAccountNumber1());
                                taxResponse.setAmount(txRequest.getTxnAmount());

                                taxResponse.setBalance(availBalance);
                                ExtPayMain.bRLogger.logEvent("INFO", convertToString(taxResponse));

                                logTransaction(txRequest, respCode, "BL03", terminalType, pmtType);
                                logTaxTransaction(txRequest, terminalType, "BL03P", "POS", pmtType, taxResponse);
                            }
                        }
                        catch (Exception ex)
                        {
                            if (ex instanceof XAPIException)
                            {
                                ExtPayMain.bRLogger.logEvent("ERROR", String.valueOf(ex));
                                taxResponse.setRespCode(((XAPIException) ex).getErrorCode());
                                taxResponse.setAccountName("N/A");
                                taxResponse.setReference("0");
                                logTransaction(txRequest, ((XAPIException) ex).getErrorCode(), "BL03", terminalType, pmtType);
                                logTaxTransaction(txRequest, terminalType, "BL03P", "POS", pmtType, taxResponse);
                            }
                            else
                            {
                                ExtPayMain.bRLogger.logEvent("ERROR", String.valueOf(ex));
                                taxResponse.setRespCode(getTagValue(String.valueOf(ex), "error-code"));
                                taxResponse.setAccountName("N/A");
                                taxResponse.setReference("0");
                                logTransaction(txRequest, getTagValue(String.valueOf(ex), "error-code"), "BL03", terminalType, pmtType);
                                logTaxTransaction(txRequest, terminalType, "BL03P", "POS", pmtType, taxResponse);
                            }
                        }
                        //logTaxTransaction(txRequest, terminalType, "BL03P", "POS", pmtType, taxResponse);
                        getXapiCaller().setXapiRespCode(taxResponse.getRespCode());
                        getXapiCaller().setMainRequest(acctBalReq);

                        getXapiCaller().setMainResponse(taxResponse);
                        getXapiCaller().settXRequest(txRequest);
                        LogEventMessage();
                        return taxResponse;
                    }
                    default:
                        taxResponse.setRespCode(invalidError);
                        logTransaction(txRequest, invalidError, "BL03", terminalType, pmtType);
                        taxResponse.setAccountName("N/A");
                        taxResponse.setReference("0");
                        ExtPayMain.bRLogger.logEvent("ERROR", "TRANSACTION FAILED");

                }
            }

            // logTransaction(txRequest, invalidError, "BL03", terminalType, pmtType);
            return taxResponse;
        }
        catch (Exception e)
        {
            taxResponse.setRespCode(EICodes.SYSTEM_ERROR);
            taxResponse.setAccountName("N/A");
            taxResponse.setReference("0");

            ExtPayMain.bRLogger.logError("ERROR-TRANSACTION FAILED", e);
            return taxResponse;
        }
    }

    private void LogEventMessage()
    {
        ExtPayMain.bRLogger.logEvent(getXapiCaller().toString());
    }

    private boolean isBalanceSufficient(TXRequest tXRequest)
    {
        BigDecimal debitAmount;
        debitAmount = tXRequest.getTxnAmount().add(tXRequest.getChargeAmount());

        try
        {
            if (tXRequest.getChannelId() == 8)
            {
                return true;
            }
            TXClient1 tXClient1 = new TXClient1(xapiCaller);

            Object response = tXClient1.queryDepositAccountBalance(this, tXRequest);
            if (response instanceof AccountBalanceOutputData)
            {
                if (((AccountBalanceOutputData) response).getAvailableBalance().compareTo(debitAmount) >= 0)
                {
                    return true;
                }
                getXapiCaller().setXapiRespCode(EICodes.INSUFFICIENT_FUNDS_1);
            }
            else if (response instanceof XAPIException)
            {
                getXapiCaller().setXapiRespCode(((XAPIException) response).getErrorCode());
            }

        }
        catch (Exception ex)
        {
            if (ex instanceof XAPIException)
            {
                getXapiCaller().setXapiRespCode(((XAPIException) ex).getErrorCode());
            }
            else
            {
                getXapiCaller().setXapiRespCode(EICodes.XAPI_ERROR);
            }
            getXapiCaller().logException(ex);
        }

        return false;
    }

    public static String getTagValue(String xml, String tagName)
    {
        if (xml.contains("java.lang.NullPointerException") || xml.equalsIgnoreCase("null") || xml.isEmpty())
        {
            return invalidError;
        }
        else
        {
            return xml.split("<" + tagName + ">")[1].split("</" + tagName + ">")[0];
        }
    }

    public boolean isGLValid(String acctNo)
    {
        return checkIfExists("SELECT GL_ACCT_NO FROM  " + BRController.CoreSchemaName + ".GL_ACCOUNT WHERE GL_ACCT_NO='" + acctNo + "'");
    }

    public boolean isBillerAcctValid(String currencyCode, String acctNo)
    {
        return checkIfExists("SELECT DISTINCT AC.ACCT_NO FROM  " + BRController.CMSchemaName + ".BL_BILLER_ACCOUNT BL," + BRController.CoreSchemaName + ".ACCOUNT AC," + BRController.CoreSchemaName + ".CURRENCY  CU WHERE BL.BILLER_ACCT_NO = AC.ACCT_NO AND AC.CRNCY_ID = CU.CRNCY_ID AND CU.CRNCY_CD = '" + currencyCode + "' AND AC.ACCT_NO = '" + acctNo + "'");
    }

    public boolean isSchFeeValid(String currencyCode, String acctNo)
    {
        return checkIfExists("SELECT DISTINCT AC.ACCT_NO FROM  " + BRController.CMSchemaName + ".BL_BILLER  BL," + BRController.CoreSchemaName + ".ACCOUNT AC, " + BRController.CoreSchemaName + ".CURRENCY  CU WHERE BL.BILLER_COL_ACCT = AC.ACCT_NO AND AC.CRNCY_ID = CU.CRNCY_ID AND CU.CRNCY_CD = '" + currencyCode + "' AND AC.ACCT_NO = '" + acctNo + "'");
    }

    public boolean isTaxTypeValid(int taxTypeId)
    {
        return checkIfExists("SELECT TAX_ID FROM  " + BRController.CMSchemaName + ".BL_BILLER_TAX_TYPE BTT," + BRController.CoreSchemaName + ".SOURCE_OF_FUNDS_REF SFR WHERE  SFR.SRC_OF_FUNDS_ID = BTT.TAX_ID AND BTT.TAX_ID ='" + taxTypeId + "' AND BTT.REC_ST='A'  AND SFR.REC_ST='A'");
    }

    public boolean isTVtxnLogged(String refValue, String currency)
    {
        return checkIfExists("SELECT SMARTCARDCODE  FROM  " + BRController.CMSchemaName + ".BL_STARTTIMES "
                + "WHERE SMARTCARDCODE = '" + refValue + "'   "
                + "AND EFFECTIVEDT = (SELECT TO_DATE(DISPLAY_VALUE,'dd/mm/yyyy') FROM  " + BRController.CoreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD = 'S02' ) "
                + "AND ORDERNO IS  NULL  AND CURRENCY  = '" + currency + "'"
                + "AND STATUS =2 AND CHANNEL = 'OTC'"
                + "AND (TRANSACTIONNO IS NULL OR TRANSACTIONNO =0) ");
    }

    public boolean isFEEtxnLogged(String refValue, String currency)
    {
        return checkIfExists("SELECT STUDENT_NO  FROM  " + BRController.CMSchemaName + ".BL_SCHOOL_FEE "
                + "WHERE STUDENT_NO = '" + refValue + "' "
                + "AND EFFECTIVEDT = (SELECT TO_DATE(DISPLAY_VALUE,'dd/mm/yyyy') FROM  " + BRController.CoreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD = 'S02' ) "
                + "AND SEQUENCENO IS  NULL  "
                + "AND CURRENCY  = '" + currency + "'"
                + "AND STATUS =2 "
                + "AND CHANNEL = 'OTC'"
                + "AND TXN_NO =0 ");
    }

    public boolean isTAXtxnLogged(String refValue, String currency)
    {
        return checkIfExists("SELECT P_NUMBER  FROM  " + BRController.CMSchemaName + ".BL_ERC_TAX_TXN "
                + "WHERE P_NUMBER = '" + refValue + "' "
                + "AND EFFECTIVEDT = (SELECT TO_DATE(DISPLAY_VALUE,'dd/mm/yyyy') FROM  " + BRController.CoreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD = 'S02' ) "
                + "AND ORDERNO IS NULL  AND CURRENCY  = '" + currency + "'"
                + "AND STATUS ='N' AND ORIG_CHANNEL = 'OTC'"
                + "AND (TXN_NO IS NULL OR TXN_NO =0) ");
    }

    public CNAccount queryAccount(String accountNumber)
    {
        CNAccount cNAccount = new CNAccount();
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT C.ACCT_ID, C.MAIN_BRANCH_ID, U.BU_CD, C.PROD_ID, C.ACCT_NO, C.ACCT_NM, E.CRNCY_CD, C.PROD_CAT_TY "
                    + "FROM  " + BRController.CoreSchemaName + ".ACCOUNT C, " + BRController.CoreSchemaName + ".BUSINESS_UNIT U, " + BRController.CoreSchemaName + ".CURRENCY E "
                    + "WHERE U.BU_ID=C.MAIN_BRANCH_ID AND C.ACCT_NO='" + accountNumber + "' "
                    + "AND C.REC_ST='A' "
                    + "AND C.PROD_CAT_TY='DP' "
                    + "AND C.CRNCY_ID = E.CRNCY_ID"))
            {
                while (rs.next())
                {
                    cNAccount.setAcctId(rs.getLong("ACCT_ID"));
                    cNAccount.setBuId(rs.getLong("MAIN_BRANCH_ID"));
                    cNAccount.setBuCode(rs.getString("BU_CD"));
                    cNAccount.setProductId(rs.getLong("PROD_ID"));
                    cNAccount.setAccountNumber(rs.getString("ACCT_NO"));
                    cNAccount.setAccountType(rs.getString("PROD_CAT_TY"));
                    cNAccount.setShortName(removeSpaces(rs.getString("ACCT_NM")));
                    cNAccount.setAccountName(removeSpaces(rs.getString("ACCT_NM")));
                    cNAccount.setCurrencyCode(rs.getString("CRNCY_CD"));
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ExtPayMain.bRLogger.logError("isTaxTypeValid", ex);
            }
        }
        return cNAccount;
    }

    public ArrayList<String> fetchSchoolDetail(String studentNo, String schoolId, String crncy)
    {
        ArrayList<String> detail = new ArrayList<>();
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT MEMBER_NAME,MEMBER_NO,CURRENCY,A.INSTITUTION_NAME,B.ACCT_NO "
                    + "FROM  " + BRController.CMSchemaName + ".BL_BILLER A, " + BRController.CoreSchemaName + ".ACCOUNT B  "
                    + "WHERE A.BILLER_COL_ACCT = B.ACCT_NO "
                    + "AND MEMBER_NO = '" + studentNo.toUpperCase() + "' "
                    + "AND BILLER_ID = '" + schoolId + "' "
                    + "AND CURRENCY = '" + crncy + "'"))
            {
                if (rs.next())
                {
                    String studentName = (rs.getString(1));
                    String StudentNo = (rs.getString(2));

                    String currency = (rs.getString(3));
                    String schoolName = (rs.getString(4));

                    String AccountNo = (rs.getString(5));

                    detail.add(studentName);
                    detail.add(StudentNo);

                    detail.add(currency);
                    detail.add(schoolName);

                    detail.add(AccountNo);
                    detail.add("00");
                }
                else
                {
                    String studentName = ("");
                    String StudentNo = ("");

                    String currency = ("");
                    String schoolName = ("");

                    String AccountNo = ("");

                    detail.add(studentName);
                    detail.add(StudentNo);

                    detail.add(currency);
                    detail.add(schoolName);

                    detail.add(AccountNo);
                    detail.add("ACCOUNT_0002");
                }
            }

        }
        catch (Exception ex)
        {
            detail.add("91");
            ExtPayMain.bRLogger.logError("fetchSchoolDetail", ex);

        }
        return detail;
    }

    public CNAccount queryBillerAccount(String abrv)
    {
        CNAccount cNAccount = new CNAccount();
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT BILLER_ACCT_NO,ACCT_ID,ACCOUNT_NAME,ACCOUNT_ABVR "
                    + "FROM  " + BRController.CMSchemaName + ".BL_BILLER_ACCOUNT  WHERE ACCOUNT_ABVR LIKE '" + abrv + "%'"))
            {
                while (rs.next())
                {
                    cNAccount.setAccountNumber(rs.getString(1));
                    cNAccount.setAcctId(rs.getLong(2));
                    cNAccount.setAccountName(rs.getString(3));
                    cNAccount.setAccountType(rs.getString(4));
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ExtPayMain.bRLogger.logError("queryBillerAccount", ex);
            }
        }
        return cNAccount;
    }

    public BillerCharge queryBillerCharge(String chargeCd, int prodId, String acctNo)
    {
        BillerCharge billerCharge;
        billerCharge = queryAccountBillCharge(chargeCd, prodId, acctNo);
        if (billerCharge.getTxcount() > 0)
        {
            return billerCharge;
        }
        else
        {
            billerCharge = new BillerCharge();
            System.err.println(prodId + " w >>>>>>>>>> " + chargeCd);

            System.err.println("XXX>>>   SELECT A.CHRG_CD,A.CHRG_DESC ,B.INCOME_GL_ACCT_NO,B.CHRG_AMT,A.CHRG_TY,B.CHRG_PCT,B.REC_ST,C.TAX_DESC,C.GL_ACCT_NO,C.FLAT_RATE "
                    + "FROM  " + BRController.CoreSchemaName + ".CHARGE A," + BRController.CoreSchemaName + ".PRODUCT_CHARGE B "
                    + "LEFT OUTER JOIN " + BRController.CoreSchemaName + ".PRODUCT_TAX C ON  C.PROD_ID = B.PROD_ID "
                    + "WHERE A.CHRG_CD = '" + chargeCd + "' "
                    + "AND A.CHRG_ID = B.CHRG_ID "
                    + "AND C.TAX_DESC LIKE 'TVA%'"
                    + "AND B.PROD_ID = " + prodId + " ");
            try
            {
                try (ResultSet rs = executeQueryToResultSet("SELECT A.CHRG_CD,A.CHRG_DESC ,B.INCOME_GL_ACCT_NO,B.CHRG_AMT,A.CHRG_TY,B.CHRG_PCT,B.REC_ST,C.TAX_DESC,C.GL_ACCT_NO,C.FLAT_RATE "
                        + "FROM  " + BRController.CoreSchemaName + ".CHARGE A," + BRController.CoreSchemaName + ".PRODUCT_CHARGE B "
                        + "LEFT OUTER JOIN " + BRController.CoreSchemaName + ".PRODUCT_TAX C ON  C.PROD_ID = B.PROD_ID "
                        + "WHERE A.CHRG_CD = '" + chargeCd + "' "
                        + "AND A.CHRG_ID = B.CHRG_ID "
                        + "AND C.TAX_DESC LIKE 'TVA%'"
                        + "AND B.PROD_ID = " + prodId + " "))
                {
                    while (rs.next())
                    {
                        billerCharge.setChargeCode(rs.getString(1));
                        billerCharge.setChargeDesc(rs.getString(2));
                        billerCharge.setIncomeGl(rs.getString(3));
                        billerCharge.setChargeFlatAmt(rs.getBigDecimal(4));
                        billerCharge.setChargeType(rs.getString(5));
                        billerCharge.setChargePCTAmt(rs.getBigDecimal(6));
                        billerCharge.setChargeStatus(rs.getString(7));
                        billerCharge.setTaxDesc(rs.getString(8));
                        billerCharge.setTaxGl(rs.getString(9));
                        billerCharge.setTaxRate(rs.getBigDecimal(10));

                    }
                }
            }
            catch (Exception ex)
            {
                if (getXapiCaller() != null)
                {
                    getXapiCaller().logException(ex);
                }
                else
                {
                    ExtPayMain.bRLogger.logError("queryBillerCharge", ex);
                }
            }
        }
        return billerCharge;
    }

    public BillerCharge queryAccountBillCharge(String chargeCd, int prodId, String acctNo)
    {
        System.err.println(prodId + " C >>>>>>>>>> " + chargeCd);
        BillerCharge billerCharge = new BillerCharge();
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT A.CHRG_CD,A.CHRG_DESC ,B.INCOME_GL_ACCT_NO,D.CHRG_AMT,D.CHRG_TY, "
                    + "D.CHRG_PCT,B.REC_ST,C.TAX_DESC,C.GL_ACCT_NO,C.FLAT_RATE "
                    + "FROM  " + BRController.CoreSchemaName + ".CHARGE A," + BRController.CoreSchemaName + ".ACCOUNT_CHARGE D, " + BRController.CoreSchemaName + ".PRODUCT_CHARGE B  "
                    + "LEFT OUTER JOIN " + BRController.CoreSchemaName + ".PRODUCT_TAX C ON  C.PROD_ID = B.PROD_ID  "
                    + "WHERE A.CHRG_CD = '" + chargeCd + "'  "
                    + "AND A.CHRG_ID = B.CHRG_ID  "
                    + "AND (C.TAX_DESC LIKE 'TVA%') "
                    + "AND B.REC_ST = 'A' "
                    + "AND B.PROD_ID = " + prodId + " "
                    + "AND B.PROD_CHRG_ID = D.PROD_CHRG_ID "
                    + "AND D.ACCT_ID = (SELECT ACCT_ID FROM  " + BRController.CoreSchemaName + ".ACCOUNT WHERE ACCT_NO = '" + acctNo + "')"))
            {
                billerCharge.setTxcount(getRowCount(rs));
                while (rs.next())
                {
                    billerCharge.setChargeCode(rs.getString(1));
                    billerCharge.setChargeDesc(rs.getString(2));
                    billerCharge.setIncomeGl(rs.getString(3));
                    billerCharge.setChargeFlatAmt(rs.getBigDecimal(4));
                    billerCharge.setChargeType(rs.getString(5));
                    billerCharge.setChargePCTAmt(rs.getBigDecimal(6));
                    billerCharge.setChargeStatus(rs.getString(7));
                    billerCharge.setTaxDesc(rs.getString(8));
                    billerCharge.setTaxGl(rs.getString(9));
                    billerCharge.setTaxRate(rs.getBigDecimal(10));

                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ExtPayMain.bRLogger.logError("queryBillerCharge", ex);
            }
        }
        return billerCharge;
    }

    public String unmaskGLAccount(String glAccount, long buId)
    {
        if (glAccount != null)
        {
            if (glAccount.contains("***"))
            {
                try
                {
                    try (ResultSet rs = executeQueryToResultSet("SELECT GL_PREFIX_CD FROM  " + BRController.CoreSchemaName + ".BUSINESS_UNIT WHERE BU_ID=" + buId))
                    {
                        if (rs.next())
                        {
                            glAccount = rs.getString("GL_PREFIX_CD") + glAccount.substring(glAccount.indexOf("***") + 3);
                        }
                    }
                }
                catch (Exception ex)
                {
                    if (getXapiCaller() != null)
                    {
                        getXapiCaller().logException(ex);
                    }
                    else
                    {
                        ExtPayMain.bRLogger.logError("ERROR", ex);
                    }
                }
            }
        }
        return glAccount;
    }

    public long getAccountId(String accountNumber)
    {
        long acctId = 0L;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT ACCT_ID FROM  " + BRController.CoreSchemaName + ".ACCOUNT WHERE ACCT_NO='" + accountNumber + "'"))
            {
                if (rs.next())
                {
                    acctId = rs.getLong("ACCT_ID");
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ExtPayMain.bRLogger.logError("ERROR", ex);
            }
        }
        return acctId;
    }

    public int getAccountProductId(String accountNumber)
    {
        int productId = 0;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT PROD_ID FROM  " + BRController.CoreSchemaName + ".ACCOUNT WHERE ACCT_NO='" + accountNumber + "'"))
            {
                if (rs.next())
                {
                    productId = rs.getInt("PROD_ID");
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ExtPayMain.bRLogger.logError("ERROR", ex);
            }
        }
        return productId;
    }

    public String getAccountProfileName(String accountNumber)
    {
        String profileName = null;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT ACCT_NM FROM  " + BRController.CoreSchemaName + ".V_ACCOUNTS WHERE ACCT_NO='" + accountNumber + "'"))
            {
                if (rs.next())
                {
                    profileName = rs.getString("ACCT_NM");
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ExtPayMain.bRLogger.logError("ERROR", ex);
            }
        }
        return profileName;
    }

    public String querryServiceCode(Long channelID)
    {

        String channelCode = null;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT CHANNEL_CD FROM  " + BRController.CoreSchemaName + ".SERVICE_CHANNEL WHERE CHANNEL_ID=" + channelID + ""))
            {
                if (rs.next())
                {
                    channelCode = rs.getString("CHANNEL_CD");
                }
                else
                {
                    ExtPayMain.bRLogger.logEvent("INFO", "CHANNEL NOT FOUND");
                }
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR", ex);

        }
        return channelCode;
    }

    public String removeSpaces(String text)
    {
        text = text == null ? "" : text;
        StringBuilder buffer = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(text);
        try
        {
            while (tokenizer.hasMoreTokens())
            {
                buffer.append(" ").append(tokenizer.nextToken());
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ExtPayMain.bRLogger.logError("ERROR", ex);
            }
        }
        return buffer.toString().trim();
    }

    public String fetchJournalId(String accountNumber, BigDecimal txnAmount)
    {
        String journalId = null;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT MAX(TRAN_JOURNAL_ID) TRAN_JOURNAL_ID FROM  " + BRController.CoreSchemaName + ".TXN_JOURNAL WHERE ACCT_NO='" + accountNumber + "' AND TRAN_AMT=" + txnAmount + " AND SYS_CREATE_TS > TO_DATE(SYSDATE-1)"))
            {
                if (rs.next())
                {
                    journalId = rs.getString("TRAN_JOURNAL_ID");
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ExtPayMain.bRLogger.logError("ERROR", ex);
            }
        }
        return journalId;
    }

    public boolean upsertCustomField(long fieldId, long parentId, long fieldValue)
    {
        if (executeUpdate("MERGE INTO " + BRController.CoreSchemaName + ".UDS_FIELD_VALUE D USING (SELECT (SELECT NEXT_NO + 1 FROM  " + BRController.CoreSchemaName + ".ENTITY WHERE ENTITY_NM = 'UDS_FIELD_VALUE') AS UDS_FIELD_VALUE_ID, " + fieldId + " AS FIELD_ID, " + parentId + " AS PARENT_ID, '" + fieldValue + "' AS FIELD_VALUE, 'A' AS REC_ST, 1 AS VERSION_NO, SYSDATE AS ROW_TS, 'SYSTEM' AS USER_ID, SYSDATE AS CREATE_DT, 'SYSTEM' AS CREATED_BY, SYSDATE AS SYS_CREATE_TS FROM  DUAL) S ON (D.FIELD_ID = S.FIELD_ID AND D.PARENT_ID=S.PARENT_ID) WHEN MATCHED THEN UPDATE SET D.FIELD_VALUE = S.FIELD_VALUE WHEN NOT MATCHED THEN INSERT (UDS_FIELD_VALUE_ID, FIELD_ID, PARENT_ID, FIELD_VALUE, REC_ST, VERSION_NO, ROW_TS, USER_ID, CREATE_DT, CREATED_BY, SYS_CREATE_TS) VALUES(S.UDS_FIELD_VALUE_ID, S.FIELD_ID, S.PARENT_ID, S.FIELD_VALUE, S.REC_ST, S.VERSION_NO, S.ROW_TS, S.USER_ID, S.CREATE_DT, S.CREATED_BY, S.SYS_CREATE_TS)", true))
        {
            return updateEntityId();
        }
        return false;
    }

    public String getChannelContraGL(long channelId, long buId)
    {
        String drContraGL = null;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT GL_DR_ACCT FROM  " + BRController.CoreSchemaName + ".SERVICE_CHANNEL WHERE CHANNEL_ID=" + channelId))
            {
                if (rs.next())
                {
                    drContraGL = rs.getString("GL_DR_ACCT");
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ExtPayMain.bRLogger.logError("ERROR", ex);
            }
        }
        return unmaskGLAccount(drContraGL, buId);
    }

    public Long getDefaultBuid()
    {
        if (DefaultBuId == null)
        {
            try
            {
                try (ResultSet rs = executeQueryToResultSet("SELECT ORIGIN_BU_ID FROM  " + BRController.CoreSchemaName + ".SERVICE_CHANNEL WHERE CHANNEL_ID=" + BRController.ChannelID))
                {
                    if (rs.next())
                    {
                        DefaultBuId = rs.getLong("ORIGIN_BU_ID");
                    }
                }
            }
            catch (Exception ex)
            {
                if (getXapiCaller() != null)
                {
                    getXapiCaller().logException(ex);
                }
                else
                {
                    ExtPayMain.bRLogger.logError("ERROR", ex);
                }
            }
        }
        return DefaultBuId;
    }

    public Long getCurrentBuid(String AcctNumber)
    {
        if (DefaultBuId == null)
        {
            try
            {
                try (ResultSet rs = executeQueryToResultSet("SELECT MAIN_BRANCH_ID FROM  " + BRController.CoreSchemaName + ".ACCOUNT WHERE ACCT_NO='" + AcctNumber + "'"))
                {
                    if (rs.next())
                    {
                        DefaultBuId = rs.getLong("MAIN_BRANCH_ID");
                    }
                }
            }
            catch (Exception ex)
            {
                if (getXapiCaller() != null)
                {
                    getXapiCaller().logException(ex);
                }
                else
                {
                    ExtPayMain.bRLogger.logError("ERROR", ex);
                }
            }
        }
        return DefaultBuId;
    }

    private boolean updateEntityId()
    {
        return executeUpdate("UPDATE " + BRController.CoreSchemaName + ".ENTITY SET NEXT_NO=(SELECT MAX(UDS_FIELD_VALUE_ID)+1 FROM  " + BRController.CoreSchemaName + ".UDS_FIELD_VALUE) WHERE ENTITY_NM = 'UDS_FIELD_VALUE'", true);
    }

    private String formatDate(Date date)
    {
        if (date != null)
        {
            return "TO_DATE('" + new SimpleDateFormat("dd-MM-yyyy").format(date) + "','DD-MM-YYYY')";
        }
        return null;
    }

    public Object[][] queryBusinessUnits()
    {
        return executeQueryToArray("SELECT BU_ID, BU_NM FROM  " + BRController.CoreSchemaName + ".BUSINESS_UNIT WHERE REC_ST='A' ORDER BY BU_NO");
    }

    public Object[][] queryCurrencies()
    {
        return executeQueryToArray("SELECT CRNCY_CD, CRNCY_NM FROM  " + BRController.CoreSchemaName + ".CURRENCY WHERE REC_ST='A' ORDER BY CRNCY_CD");
    }

    public Object[][] queryProducts()
    {
        return executeQueryToArray("SELECT PROD_ID, PROD_DESC FROM  " + BRController.CoreSchemaName + ".PRODUCT WHERE REC_ST='A' ORDER BY PROD_CD");
    }

    public Object[][] queryProducts(int productId)
    {
        return executeQueryToArray("SELECT PROD_ID, PROD_DESC FROM  " + BRController.CoreSchemaName + ".PRODUCT WHERE REC_ST='A' AND PROD_ID=" + productId + " ORDER BY PROD_CD");
    }

    public Date getCurrentDate()
    {
        Date currentDate = new Date();
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT SYSDATE FROM  DUAL"))
            {
                if (rs.next())
                {
                    currentDate = rs.getDate(1);
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ExtPayMain.bRLogger.logError("ERROR", ex);
            }
        }
        return currentDate;
    }

    public String capitalize(String name)
    {
        if (name != null)
        {
            try
            {
                StringBuilder builder = new StringBuilder();
                for (String word : name.toLowerCase().split("\\s"))
                {
                    builder.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase()).append(" ");
                }
                return builder.toString();
            }
            catch (Exception ex)
            {
                if (getXapiCaller() != null)
                {
                    getXapiCaller().logException(ex);
                }
                else
                {
                    ExtPayMain.bRLogger.logError("ERROR", ex);
                }
                return name;
            }
        }
        return name;
    }

    public void selectItemByCode(JComboBox box, String code)
    {
        for (int i = 0; i < box.getItemCount(); i++)
        {
            if (box.getItemAt(i).toString().startsWith(code + "~"))
            {
                box.setSelectedIndex(i);
                return;
            }
        }
    }

    public String replaceAll(String message, String holder, String replacement)
    {
        if (message != null)
        {
            replacement = replacement == null ? "<>" : replacement;
            while (message.contains(holder) && !replacement.equals(holder))
            {
                message = message.replace(holder, replacement);
            }
        }
        return message;
    }

    //======== addition for the collection and etax payment =====
    public boolean logNPTxn(String P_Number, String Applicant_Name, BigDecimal Amount_paid, String Bgt_atc_grp, String Bgt_atc_naming, String Issue_Date, String Pmt_Period, String Pmt_deadline_date, String AccountNo, String currency)
    {
        System.err.println("Bgt_atc_grp " + Bgt_atc_grp);
        if (Bgt_atc_grp.equals("") || Bgt_atc_grp == null || Bgt_atc_grp.isEmpty())
        {
            Bgt_atc_grp = "N/A";
        }
        try
        {
            return executeUpdate("Insert into " + BRController.CMSchemaName + ".BL_ERC_TAX_TXN ("
                    + " P_Number, Applicant_Name, Amount_paid, Bgt_atc_grp, Bgt_atc_naming, Issue_Date, "
                    + "Pmt_Period, Pmt_deadline_date,Orig_Channel,np_validate_time,EFFECTIVEDT,STATUS,ACCOUNTNUMBER,TXN_NO,CURRENCY )"
                    + "VALUES"
                    + "('" + P_Number + "','" + Applicant_Name.replaceAll("'", "") + "',"
                    + "" + Amount_paid + ",'" + Bgt_atc_grp.replaceAll("'", "") + "','"
                    + Bgt_atc_naming.replaceAll("'", "") + "','" + Issue_Date + "',"
                    + "'" + Pmt_Period.replaceAll("'", "") + "','"
                    + Pmt_deadline_date + "','OTC', to_date('" + procDtformat.format(timestamp) + "','dd/MM/yyyy'), "
                    + "to_date('" + getProcessingDate() + "','dd/MM/yyyy'),'N','" + AccountNo + "',0,'" + currency + "')", true);
        }
        catch (Exception e)
        {
            ExtPayMain.bRLogger.logError("logNPTxn - ERROR", e);
            return false;
        }
    }

    public boolean logtvTxn(TvResponse tvResponse, TvQueryRequest tvQueryRequest)
    {
        try
        {
            return executeUpdate("INSERT INTO " + BRController.CMSchemaName + ".BL_STARTTIMES ( "
                    + " TRANSACTIONNO,  PAYERID,  PAYERPWD,  CUSTOMER_CODE,  CUSTOMERNAME,  CUSTOMERTEL, "
                    + " SMARTCARDCODE,  FEE,  RECEIPTCODE,  DEVICETYPE,  TRANSFERTIME,  EMAIL, "
                    + " UPDATEDFG,  EFFECTIVEDT,  ORDERNO,  RESPONSETIME,  CHANNEL,  USERCHANNEL, "
                    + " STATUS,  RETRYCOUNT,  BILLERNAME,CURRENCY) "
                    + " VALUES"
                    + "(0,  '" + BRController.StartimesPayerId + "',  '" + BRController.StartimesPayerPassword + "', '',NVL('" + tvResponse.getAccountName() + "','Star Times'),  0,  "
                    + " '" + tvQueryRequest.getDecoderNo() + "', NVL('" + tvResponse.getAmount() + "',0), '0', '0',  to_date('" + procDtformat.format(timestamp) + "','dd/MM/yyyy'),  '', "
                    + " 'N',  to_date('" + getProcessingDate() + "','dd/MM/yyyy'),  '',  '',  'OTC',   '', "
                    + " '2',  '',  '" + tvResponse.getPaymentType() + "', '" + tvQueryRequest.getCurrency() + "')", true);
        }
        catch (Exception e)
        {
            ExtPayMain.bRLogger.logError("logTVTxn - ERROR", e);
            return false;
        }
    }

    public boolean logFeeTxn(PmtWebserviceData pmtWebserviceData)
    {
        try
        {
            return executeUpdate("INSERT INTO " + BRController.CMSchemaName + ".BL_SCHOOL_FEE "
                    + " (TXN_NO,  BILLER_ID,  INSTITUTION_NAME,  STUDENT_NO,  STUDENT_NAME, "
                    + " CUSTOMERTEL,  GRADE_LEVEL,  FEE,  SECTION,  CATEGORY,    EFFECTIVEDT,  "
                    + " RESPONSETIME,  CHANNEL,  USERCHANNEL,  STATUS,   CURRENCY ,ACCOUNT_NO "
                    + " ) "
                    + " VALUES "
                    + " (0,'" + pmtWebserviceData.getSchoolId() + "','" + pmtWebserviceData.getSchoolName() + "','" + pmtWebserviceData.getStudentNumber() + "',"
                    + "'" + pmtWebserviceData.getStudentName() + "','','" + pmtWebserviceData.getStudentGrade() + "',0,'" + pmtWebserviceData.getStudentSection() + "',"
                    + "'" + pmtWebserviceData.getStudentCategory() + "',TO_DATE('" + getProcessingDate() + "','DD/MM/YYYY'),'" + procDtformat.format(timestamp) + "','OTC','OTC','2', "
                    + "'" + pmtWebserviceData.getCurrency() + "','" + pmtWebserviceData.getAccountNumber() + "') ", true);
        }
        catch (Exception e)
        {
            ExtPayMain.bRLogger.logError("logFeeTxn - ERROR", e);
            return false;
        }

    }

    public boolean backUpTable(String thistime)
    {
        //  return executeUpdate("CREATE TABLE " + BRController.CMSchemaName + ".BL_BILLER_BKP AS Select BILLER_COL_ACCT,BILLER_ID,MEMBER_NO,MEMBER_NAME,MEMBER_MOBILE_NUMBER,INSTITUTION_NAME,'"+STformatter.format(getCurrentDate())+"' AS UPDATEDATE  FROM  " + BRController.CMSchemaName + ".BL_BILLER", true);
        return executeUpdate("INSERT INTO " + BRController.CMSchemaName + ".BL_BILLER_BKP Select BILLER_COL_ACCT,BILLER_ID,MEMBER_NO,MEMBER_NAME,MEMBER_MOBILE_NUMBER,INSTITUTION_NAME,'" + formatter.format(getCurrentDate()) + "' AS UPDATEDATE  FROM  " + BRController.CMSchemaName + ".BL_BILLER", true);

    }

    public String getBillerAcct(String thisname)
    {
        String act_no = "";
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT BILLER_ACCT_NO FROM  " + BRController.CMSchemaName + ".BL_BILLER_ACCOUNT WHERE ACCOUNT_NAME =  '" + thisname + "'"))
            {
                if (rs.next())
                {
                    act_no = rs.getString(1);
                }
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR", ex);
        }
        return act_no;

    }

    public boolean truncateTable(String biller_id)
    {
        System.out.println(">> backing up table  " + splitbillerItem(biller_id));
        String Biller_id = splitbillerItem(biller_id);
        return executeUpdate("delete from " + BRController.CMSchemaName + ".BL_BILLER where biller_id = '" + Biller_id + "'", true);
    }

    public String splitbillerItem(String bsName)
    {
        String temp = bsName;
        String[] splitString = temp.split("\\.");

        for (int i = 0; i < splitString.length; i++)
        {
        }
        return splitString[0];
    }

    public boolean updateBillerTable(BFValue bfValue)
    {
        return executeUpdate("INSERT INTO " + BRController.CMSchemaName + ".BL_BILLER "
                + "(BILLER_COL_ACCT,BILLER_ID,MEMBER_NO,MEMBER_NAME,MEMBER_MOBILE_NUMBER,INSTITUTION_NAME,CURRENCY,GRADE_LEVEL,SECTION,CATEGORY)  "
                + "VALUES('" + bfValue.getBILLER_COL_ACCT() + "','" + bfValue.getBILLER_ID() + "','" + bfValue.getMEMBER_NO() + "','"
                + bfValue.getMEMBER_NAME() + "','" + bfValue.getMEMBER_MOBILE_NUMBER() + "','" + bfValue.getMEMBER_ID_NO() + "','"
                + bfValue.getCURRENCY() + "','" + bfValue.getGRADE_LEVEL() + "','" + bfValue.getSECTION() + "','" + bfValue.getCATEGORY() + "')", true);
    }

    public boolean updateCoreBillTaxTxn(BillsTxn billsTxn)
    {
        return executeUpdate("UPDATE " + BRController.CMSchemaName + ".BL_ERC_TAX_TXN "
                + "SET TXN_NO = " + billsTxn.getTxnNumber() + ", "
                + "AMOUNT_PAID = " + billsTxn.getAmount() + ", "
                + "CHANNEL = 'OTC', "
                + "STATUS = 'S', "
                + "USERCHANNEL = '" + billsTxn.getUser() + "', "
                + "SEQUENCENO ='" + billsTxn.getTranRefTxt() + "', "
                + "LOG_TYPE = 'BL107', "
                + "TXN_DESC = '" + billsTxn.getDescription() + "' "
                + "WHERE ACCOUNTNUMBER = '" + billsTxn.getAcct_no() + "' "
                + "AND P_NUMBER = '" + billsTxn.getReferenceNo() + "'  "
                + "AND TO_CHAR(EFFECTIVEDT,'DD/MM/YYYY')= (SELECT  TO_CHAR(TO_DATE(DISPLAY_VALUE,'DD/MM/YYYY'),'DD/MM/YYYY')  FROM  " + BRController.CoreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD = 'S02') "
                + "AND STATUS = 'N' AND ORIG_CHANNEL = 'OTC' AND AMOUNT_PAID =0 AND TXN_NO = 0 AND CURRENCY = '" + billsTxn.getCurrency() + "'", true);
    }

    public boolean updateCoreBillTVTxn(BillsTxn billsTxn)
    {
        return executeUpdate("UPDATE " + BRController.CMSchemaName + ".BL_STARTTIMES "
                + "SET TRANSACTIONNO = " + billsTxn.getTxnNumber() + ", "
                + "FEE = " + billsTxn.getAmount() + ", "
                + "CHANNEL = 'OTC', "
                + "STATUS = '3', "
                + "USERCHANNEL = '" + billsTxn.getUser() + "', "
                + "SEQUENCENO ='" + billsTxn.getTranRefTxt() + "', "
                + "TXN_DESC = '" + billsTxn.getDescription() + "' "
                + "WHERE  SMARTCARDCODE = '" + billsTxn.getReferenceNo() + "'  "
                + "AND TO_CHAR(EFFECTIVEDT,'DD/MM/YYYY')< = (SELECT  TO_CHAR(TO_DATE(DISPLAY_VALUE,'DD/MM/YYYY'),'DD/MM/YYYY')  FROM  " + BRController.CoreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD = 'S02' ) "
                + "AND STATUS = '2' AND CHANNEL = 'OTC' AND FEE =0 AND TRANSACTIONNO = 0 AND CURRENCY = '" + billsTxn.getCurrency() + "'", true);

    }

    public boolean updateCoreBillFEETxn(BillsTxn billsTxn)
    {
        System.err.println(billsTxn.getReferenceNo() + "UPDATNG FEES " + billsTxn.getTxnNumber());

        return executeUpdate("UPDATE " + BRController.CMSchemaName + ".BL_SCHOOL_FEE "
                + "SET TXN_NO = " + billsTxn.getTxnNumber() + ", "
                + "FEE = " + billsTxn.getAmount() + ", "
                + "CHANNEL = 'OTC',"
                + "STATUS = '00',"
                + "USERCHANNEL = '" + billsTxn.getUser() + "',"
                + "SEQUENCENO ='" + billsTxn.getTranRefTxt() + "', "
                + "TXN_DESC = '" + billsTxn.getDescription() + "' "
                + "WHERE ACCOUNT_NO = '" + billsTxn.getAcct_no() + "' "
                + "AND STUDENT_NO = '" + billsTxn.getReferenceNo() + "'  "
                + "AND TO_CHAR(EFFECTIVEDT,'DD/MM/YYYY')= (SELECT  TO_CHAR(TO_DATE(DISPLAY_VALUE,'DD/MM/YYYY'),'DD/MM/YYYY')  FROM  " + BRController.CoreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD = 'S02') "
                + "AND STATUS = '2' AND CHANNEL = 'OTC' AND FEE =0 AND TXN_NO = 0 AND CURRENCY = '" + billsTxn.getCurrency() + "'", true);

    }

    public boolean InsertBillerActRecord(String bill_acct_no, String acctName, String Abbv)
    {
        int act_rm = acctId(bill_acct_no);
        if (act_rm == 0)
        {
            JOptionPane.showMessageDialog(ExtPayMain.uiFrame, " Invalid Account Number.", "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        else
        {
            return executeUpdate("INSERT INTO " + BRController.CMSchemaName + ".BL_BILLER_ACCOUNT (BILLER_ACCT_NO,ACCT_ID,ACCOUNT_NAME,ACCOUNT_ABVR)  "
                    + "VALUES('" + bill_acct_no + "'," + act_rm + ",'" + acctName + "','" + Abbv + "')", true);
        }
    }

    public boolean updateBillerActRecord(String bill_acct_no, String acctName, String Abbv)
    {
        int act_rm = acctId(bill_acct_no);
        return executeUpdate("UPDATE  " + BRController.CMSchemaName + ".BL_BILLER_ACCOUNT "
                + "SET BILLER_ACCT_NO = '" + bill_acct_no + "', ACCT_ID = " + act_rm + ","
                + "ACCOUNT_NAME = '" + acctName + "',ACCOUNT_ABVR = '" + Abbv + "' WHERE ACCOUNT_ABVR = '" + Abbv + "' ", true);
    }

    public String queryBillerName(String billerAcctNo)
    {
        String act_no = "";
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT ACCOUNT_NAME FROM   " + BRController.CMSchemaName + ".BL_BILLER_ACCOUNT WHERE  BILLER_ACCT_NO =  '" + billerAcctNo + "'"))
            {
                if (rs.next())
                {
                    act_no = rs.getString(1);
                }
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR", ex);
        }
        return act_no;
    }

    public String queryTaxName(int taxTypeId)
    {
        String taxName = "";
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT TAX_TY_DESC FROM   " + BRController.CMSchemaName + ".BL_BILLER_TAX_TYPE WHERE  TAX_ID =  '" + taxTypeId + "'"))
            {
                if (rs.next())
                {
                    taxName = rs.getString(1);
                }
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR", ex);
        }
        return taxName;
    }

    public boolean insertTaxTypeInfo(int taxType)
    {
        return executeUpdate("INSERT INTO " + BRController.CMSchemaName + ".BL_BILLER_TAX_TYPE(TAX_ID,TAX_TY,TAX_TY_DESC,REC_ST) select SRC_OF_FUNDS_ID,SRC_OF_FUNDS_TY,SRC_OF_FUNDS_DESC,REC_ST FROM  " + BRController.CoreSchemaName + ".SOURCE_OF_FUNDS_REF WHERE SRC_OF_FUNDS_ID =" + taxType + " and REC_ST='A' ", true);
    }

    public boolean updateTaxTypeInfo(int taxTypeId, String desc, String status)
    {
        return executeUpdate("UPDATE " + BRController.CMSchemaName + ".BL_BILLER_TAX_TYPE SET TAX_ID = " + taxTypeId + ", TAX_TY_DESC = '" + desc + "', REC_ST='" + status + "' WHERE TAX_ID = " + taxTypeId + "", true);
    }

    public boolean insertBillerCC(int chargeCode)
    {
        String chgName = chargeNameget(chargeCode);
        return executeUpdate("INSERT INTO " + BRController.CMSchemaName + ".BILLER_CC"
                + "(CHARGE_CODE,CHARGE_NAME)  "
                + "VALUES(" + chargeCode + ",'" + chgName + "')", true);
    }

    public Object[][] querybillerAccount(String acctNo)
    {
        return executeQueryToArray("SELECT BILLER_ACCT_NO, ACCOUNT_NAME FROM  " + BRController.CMSchemaName + ".BL_BILLER_ACCOUNT WHERE BILLER_ACCT_NO = '" + acctNo + "'");
    }

    public ResultSet querybillertxn()
    {
        return executeQueryToResultSet("SELECT TXN_NO AS TRANSACTIONNO,STUDENT_NO||' ['||STUDENT_NAME||']' AS STUDENTNAME,FEE AS AMOUNTPAID,CURRENCY,EFFECTIVEDT,CHANNEL,STATUS "
                + "FROM  " + BRController.CMSchemaName + ".BL_SCHOOL_FEE WHERE EFFECTIVEDT >= (SELECT TO_DATE(DISPLAY_VALUE,'dd/mm/yyyy') FROM  " + BRController.CoreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD = 'S01') AND FEE >0 ORDER BY EFFECTIVEDT desc");
    }

    public ResultSet queryTvtxn()
    {
        return executeQueryToResultSet("SELECT TRANSACTIONNO,SMARTCARDCODE||' ['||CUSTOMERNAME||']' AS CUSTOMERNAME,FEE AS AMOUNTPAID,CURRENCY,EFFECTIVEDT,CHANNEL,STATUS "
                + "FROM  " + BRController.CMSchemaName + ".BL_STARTTIMES "
                + "WHERE EFFECTIVEDT >= (SELECT TO_DATE(DISPLAY_VALUE,'dd/mm/yyyy') FROM  " + BRController.CoreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD = 'S01') order by EFFECTIVEDT desc");
    }

    public ResultSet queryTaxTxn()
    {
        return executeQueryToResultSet("SELECT TXN_NO AS TRANSACTIONNO,APPLICANT_NAME AS APPLICANTNAME,AMOUNT_PAID AS AMOUNTPAID,CURRENCY,EFFECTIVEDT,CHANNEL,STATUS "
                + "FROM  " + BRController.CMSchemaName + ".BL_ERC_TAX_TXN WHERE EFFECTIVEDT >= (SELECT TO_DATE(DISPLAY_VALUE,'dd/mm/yyyy') FROM  " + BRController.CoreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD = 'S01') AND AMOUNT_PAID > 0 order by EFFECTIVEDT desc");
    }

//TAX_ID,TAX_TY_DESC
    public Object[][] queryTaxType(int taxTypeId)
    {
        return executeQueryToArray("SELECT BTT.TAX_ID,SFR.SRC_OF_FUNDS_DESC "
                + "FROM  " + BRController.CMSchemaName + ".BL_BILLER_TAX_TYPE BTT," + BRController.CoreSchemaName + ".SOURCE_OF_FUNDS_REF SFR WHERE  SFR.SRC_OF_FUNDS_ID = BTT.TAX_ID AND BTT.TAX_ID ='" + taxTypeId + "' AND BTT.REC_ST='A'  AND SFR.REC_ST='A'");
    }

    public Object[][] verifyBillerAccount(String acctNo)
    {
        return executeQueryToArray("SELECT ACCT_NO,ACCT_NM "
                + "FROM  " + BRController.CoreSchemaName + ".ACCOUNT WHERE ACCT_NO = '" + acctNo + "' AND REC_ST='A' ");
    }

    public Object[][] verifyTaxType(int taxTypeId)
    {
        return executeQueryToArray("SELECT BTT.TAX_ID,SFR.SRC_OF_FUNDS_DESC "
                + "FROM  " + BRController.CMSchemaName + ".BL_BILLER_TAX_TYPE BTT," + BRController.CoreSchemaName + ".SOURCE_OF_FUNDS_REF SFR  "
                + "WHERE SFR.SRC_OF_FUNDS_ID = BTT.TAX_ID  AND BTT.TAX_ID = '" + taxTypeId + "' AND BTT.REC_ST='A' ");
    }

    public Object[][] selectTaxTypes()
    {
        return executeQueryToArray("SELECT BTT.TAX_ID,SFR.SRC_OF_FUNDS_DESC "
                + "FROM  " + BRController.CMSchemaName + ".BL_BILLER_TAX_TYPE BTT," + BRController.CoreSchemaName + ".SOURCE_OF_FUNDS_REF SFR WHERE  SFR.SRC_OF_FUNDS_ID = BTT.TAX_ID AND BTT.REC_ST='A'");
    }

    public Object[][] selectTaxTypesId(int taxTypeId)
    {
        return executeQueryToArray("SELECT BTT.TAX_ID,SFR.SRC_OF_FUNDS_DESC,BTT.REC_ST "
                + "FROM  " + BRController.CMSchemaName + ".BL_BILLER_TAX_TYPE BTT," + BRController.CoreSchemaName + ".SOURCE_OF_FUNDS_REF SFR "
                + "WHERE  SFR.SRC_OF_FUNDS_ID = BTT.TAX_ID  AND BTT.TAX_ID = " + taxTypeId + " AND BTT.REC_ST='A'");
    }

    public Object[][] selectbillerAccount()
    {
        return executeQueryToArray("SELECT BILLER_ACCT_NO, ACCOUNT_NAME FROM  " + BRController.CMSchemaName + ".BL_BILLER_ACCOUNT");
    }

    public Object[][] selectTaxAccount(int taxType)
    {
        return executeQueryToArray("SELECT BILLER_ACCT_NO, ACCOUNT_NAME FROM  " + BRController.CMSchemaName + ".BL_BILLER_ACCOUNT WHERE BILLER_ACCT_NO='" + taxType + "'");
    }

    public Object[][] selectBlAcctFiltered(String AcctNo)
    {
        return executeQueryToArray("SELECT BILLER_ACCT_NO, ACCOUNT_NAME,ACCOUNT_ABVR FROM  " + BRController.CMSchemaName + ".BL_BILLER_ACCOUNT WHERE BILLER_ACCT_NO='" + AcctNo + "'");
    }

    public Object[][] selectTaxAcctFiltered(String taxDesc)
    {
        return executeQueryToArray("SELECT TAX_ID, TAX_TY_DESC "
                + "FROM  " + BRController.CMSchemaName + ".BL_BILLER_TAX_TYPE "
                + "WHERE TAX_TY_DESC='" + taxDesc + "'");
    }

    public Object[][] selectSchoolName(String AcctNo)
    {
        return executeQueryToArray("SELECT DISTINCT INSTITUTION_NAME from " + BRController.CMSchemaName + ".BL_BILLER_ACCOUNT a," + BRController.CMSchemaName + ".BL_BILLER b WHERE a.BILLER_ACCT_NO=b.BILLER_COL_ACCT AND A.BILLER_ACCT_NO='" + AcctNo + "'");
    }

    public String chargeNameget(int codeCC)
    {
        String code = "";
        try
        {
            try (ResultSet rs = executeQueryToResultSet("select description from " + BRController.CMSchemaName + ".ad_gb_cc a where charge_code = " + codeCC + ""))
            {
                if (rs.next())
                {
                    code = rs.getString(1);
                }
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR", ex);
        }
        return code;
    }

    public boolean deleteValidateParam(String fg, String tc)
    {
        return executeUpdate("DELETE FROM   " + BRController.CMSchemaName + ".BL_BILLER_VALIDATE  WHERE BILLER_TC = '" + tc + "", true);
    }

    public boolean deleteDuplicates()
    {
        System.out.println(">>checking duplicates");
        return executeUpdate("delete from " + BRController.CMSchemaName + ".BL_biller where member_no in "
                + "(select x.member_no from " + BRController.CMSchemaName + ".BL_biller x group by x.member_no,x.biller_id,currency having count(x.member_no )>1 and count(x.biller_id)>1 and count(currency)>1 )", true);
    }

    public boolean deleteDuptable()
    {
        return executeUpdate("delete from " + BRController.CMSchemaName + ".BL_BILLER_UPLOAD_DUPLICATES", true);
    }

    public boolean addValidateParam(String fg, String tc)
    {
        return executeUpdate("insert into " + BRController.CMSchemaName + ".BL_BILLER_VALIDATE( VALIDATE_FG,BILLER_TC) values('" + fg + "','" + tc + "')", true);
    }

    public boolean insertDup()
    {
        if (duplicatesChecker() > 0)
        {
            ExtPayMain.bRLogger.logEvent("Upload-Duplicates", duplicatesChecker() + "Duplicate Records Found and deleted. Refer to the main file in the Archive Folder:");

        }
        return executeUpdate("insert into " + BRController.CMSchemaName + ".BL_BILLER_UPLOAD_DUPLICATES (BILLER_COL_ACCT,BILLER_ID,MEMBER_NO,MEMBER_NAME,MEMBER_MOBILE_NUMBER,INSTITUTION_NAME,lOG_DATE,CURRENCY) "
                + "select distinct BILLER_COL_ACCT,BILLER_ID,MEMBER_NO,MEMBER_NAME,MEMBER_MOBILE_NUMBER,INSTITUTION_NAME,'" + getCurrentDate() + "',CURRENCY from " + BRController.CMSchemaName + ".BL_biller x group by BILLER_COL_ACCT,BILLER_ID,MEMBER_NO,MEMBER_NAME,MEMBER_MOBILE_NUMBER,INSTITUTION_NAME,CURRENCY   "
                + "having count(x.member_no )>1 and count(x.biller_id)>1 and count(currency)>1", true);

    }

    public BFValue queryPcConfig()
    {
        BFValue bfValue = new BFValue();
        try
        {
            try (ResultSet rs = executeQueryToResultSet("select PARAM_VALUE,DISPLAY_VALUE from " + BRController.CoreSchemaName + ".ctrl_parameter where param_cd = 'S04'"))
            {
                if (rs.next())
                {
                    bfValue.setDISPLAY_VALUE(rs.getString("PARAM_VALUE"));
                    bfValue.setPARAM_VALUE(rs.getString("DISPLAY_VALUE"));
                }
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR", ex);
        }
        return bfValue;
    }

    public int acctId(String acct)
    {
        int act_no = 0;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT ACCT_ID FROM  " + BRController.CoreSchemaName + ".ACCOUNT A WHERE ACCT_NO = '" + acct + "'"))
            {
                if (rs.next())
                {
                    act_no = rs.getInt("ACCT_ID");
                }
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR", ex);
        }
        return act_no;
    }

    public String billerAcctNo(String acct)
    {
        String act_no = "";
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT ACCT_NO FROM  " + BRController.CoreSchemaName + ".ACCOUNT A," + BRController.CMSchemaName + ".BL_BILLER B WHERE A.ACCT_NO =B.BILLER_COL_ACCOUNT AND A.ACCT_NM = '" + acct + "'"))
            {
                if (rs.next())
                {
                    act_no = rs.getString("ACCT_NO");
                }
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR", ex);
        }
        return act_no;
    }

    public STValue startTimesElemValue()
    {
        STValue stValue = new STValue();
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT transactionNo ,payerID ,payerPwd ,NVL(customer_code,' ')customer_code,"
                    + "NVL(customerName,'')customerName,NVL(customerTel,'')customerTel,smartCardCode ,fee,NVL(receiptCode,'')receiptCode,NVL(deviceType,'')deviceType ,transferTime ,NVL(email,'')email "
                    + "from " + BRController.CMSchemaName + ".BL_starttimes where  (updatedfg = 'Null' or updatedfg = '' or updatedfg = 'N' OR UPDATEDFG IS NULL) and fee > 0 and transactionNo > 0"))
            {
                if (rs != null)
                {
                    stValue.setCount(getRowCount(rs));
                    if (rs.next())
                    {
                        stValue.setCustName(rs.getString("customerName").trim());
                        stValue.setCustcode(rs.getString("customer_code"));
                        stValue.setCustTel(rs.getString("customerTel").trim());
                        stValue.setPayerId(rs.getString("payerID").trim());
                        stValue.setPayerPwd(rs.getString("payerPwd").trim());
                        stValue.setCustRecCode(rs.getString("receiptCode").trim());
                        stValue.setTranNo(rs.getString("transactionNo").trim());
                        stValue.setDevType(rs.getString("deviceType").trim());
                        stValue.setSmartCardno(rs.getString("smartCardCode").trim());
                        //stValue.setCustRecCode(rs.getString("smartCardCode").trim());
                        //stValue.setEmail(rs.getString("email").trim());
                        stValue.setFee((rs.getDouble("fee")));
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR", ex);
        }
        return stValue;
    }

    public int duplicatesChecker()
    {
        int countTxn = 0;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("select count(*) from " + BRController.CMSchemaName + ".BL_biller x where biller_id = '100001' group by x.member_no,x.biller_id,x.currency having count(x.member_no )>1 and count(x.biller_id)>1 and count(x.currency)>1"))
            {
                while (rs.next())
                {
                    int Count = (rs.getInt(1));
                    countTxn = Count;
                }
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR", ex);
        }
        return countTxn;
    }

    public String getProcessingDate()
    {
        String currentDate = procDtformat.format(new Date());
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT TO_CHAR(TO_DATE(DISPLAY_VALUE,'DD/MM/YYYY'),'DD/MM/YYYY') AS CURRENT_DATE FROM  " + BRController.CoreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD = 'S02'"))
            {
                if (rs.next())
                {
                    currentDate = rs.getString("CURRENT_DATE");
                }
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR-getProcessingDate()", ex);
        }
        return currentDate;
    }

    public ArrayList<String> checkBillerName()
    {
        ArrayList<String> act_name = new ArrayList<>();
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT DISTINCT B.ACCT_NM,A.BILLER_ID  FROM  " + BRController.CMSchemaName + ".BL_BILLER A," + BRController.CoreSchemaName + ".ACCOUNT B where A.BILLER_COL_ACCT = B.ACCT_NO"))
            {

                while (rs.next())
                {
                    String colAct = (rs.getString(1).concat("~").concat(rs.getString(2)));
                    act_name.add(colAct);
                }
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR", ex);
        }
        return act_name;
    }

    public ArrayList<String> checkTaxType()
    {
        ArrayList<String> act_name = new ArrayList<>();
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT DISTINCT A.TAX_ID,A.TAX_TY_DESC  FROM  " + BRController.CMSchemaName + ".BL_BILLER_TAX_TYPE A," + BRController.CoreSchemaName + ".SOURCE_OF_FUNDS_REF B where A.TAX_ID = B.SRC_OF_FUNDS_ID AND A.REC_ST = 'A' AND B.REC_ST = 'A'"))
            {

                while (rs.next())
                {
                    String colAct = (rs.getString(1).concat("~").concat(rs.getString(2)));
                    act_name.add(colAct);
                }
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR[checkTaxType]", ex);
        }
        return act_name;
    }

    public ArrayList<String> TaxTypeCombo()
    {
        ArrayList<String> act_name = new ArrayList<>();
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT DISTINCT A.TAX_TY_DESC,A.TAX_ID  "
                    + "FROM  " + BRController.CMSchemaName + ".BL_BILLER_TAX_TYPE A," + BRController.CoreSchemaName + ".SOURCE_OF_FUNDS_REF B where A.TAX_ID = B.SRC_OF_FUNDS_ID AND A.REC_ST = 'A' AND B.REC_ST = 'A' ORDER BY A.TAX_ID"))
            {
                while (rs.next())
                {
                    String colAct = (rs.getString(1));
                    act_name.add(colAct);
                }
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR[checkTaxType]", ex);
        }
        return act_name;
    }

    public String getReconHeader()
    {
        String header = "";
        try
        {
            try (ResultSet rs = executeQueryToResultSet("Select  max(transferTime) startTime,min(transferTime)cutoffTime,sum(fee)totAmt, count(*)tranCount from " + BRController.CMSchemaName + ".bl_starttimes where updatedfg = 'Y'  and status = '0' and effectivedt = '" + STformatter.format(getProcessingDate()) + "' "))
            {
                if (rs.next())
                {
                    String Starttime = new SimpleDateFormat("yyyyMMddHH24mmss").format(rs.getTimestamp("startTime"));
                    String cutoftime = new SimpleDateFormat("yyyyMMddHHmmss").format(rs.getTimestamp("cutoffTime"));
                    Double totAmt = rs.getDouble("totAmt");
                    int tottxn = rs.getInt("trancount");
                    header = Starttime.concat("\\t").concat(cutoftime).concat("\\t").concat(String.valueOf(totAmt).concat("\\t").concat(String.valueOf(tottxn)).concat("\\n"));
                }
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR[getReconHeader]", ex);
        }
        return header;
    }

    public boolean generateSTRecon()
    {
        int checkNo = 0;
        Boolean done = false;
        String record;
        StringBuilder buffer = new StringBuilder();
        String datetoday = STformatter.format(getProcessingDate());
        try
        {
            if (getDbConnection() == null)
            {
                connectToDB();
            }
            System.err.println("format date call " + callformatter.format(timestamp));
            CallableStatement stmt = getDbConnection().prepareCall("{call CSP_ST_RECON_FILE(?)}");

            stmt.setString(1, datetoday);
            ResultSet rset = stmt.executeQuery();

            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMinimumIntegerDigits(1);
            nf.setMinimumFractionDigits(2);

            nf.setMaximumFractionDigits(2);
            nf.setMaximumIntegerDigits(10);

            nf.setGroupingUsed(false);

            buffer.append(getReconHeader()).append("\r\n");
            while (rset.next())
            {
                System.err.println("1" + rset.getTimestamp(6));
                record = String.valueOf(rset.getInt(1)).concat("\\t")
                        + (rset.getString(2).trim().concat("\\t"))
                        + nf.format(rset.getDouble(3)).concat("\\t")
                        + (rset.getString(4)).concat("\\t")
                        + (rset.getString(5).trim().concat("\\t"))
                        + new SimpleDateFormat("yyyyMMddHHmmss").format(rset.getTimestamp(6))
                        + "\\n";
                buffer.append(record).append("\r\n");

            }

            if (buffer.length() > 0)
            {

                if (writeOutputToFile(buffer, "reconpath", "dz-0100-" + STformatter.format(timestamp) + ".req"))
                {
                    ExtPayMain.bRLogger.logEvent("INFO", "StarTimes Recon File Extracted!");
                    done = true;
                }
                else
                {
                    ExtPayMain.bRLogger.logEvent("INFO", "StarTimes Recon File Extraction Failed!");
                }
            }
            else
            {
                done = false;
                ExtPayMain.bRLogger.logEvent("INFO", "StarTimes Recon File is Empty!");
            }
            if (done)
            {
                stmt.close();
                rset.close();
                dispose();
            }
        }
        catch (Exception ex)
        {

            ExtPayMain.bRLogger.logError("ERROR[generateSTRecon]", ex);
        }
        return done;
    }

    public boolean revBillerTxn()
    {

        Boolean done = false;
        String record;
        StringBuilder buffer = new StringBuilder();
        String datetoday = STformatter.format(getProcessingDate());
        try
        {
            if (getDbConnection() == null)
            {
                connectToDB();
            }
            System.err.println("format date call " + callformatter.format(timestamp));
            CallableStatement stmt = getDbConnection().prepareCall("{? = call psp_tl_tc_Rev_Biller()}");
            stmt.registerOutParameter(1, Types.INTEGER);
            ResultSet rset = stmt.executeQuery();

            while (rset.next())
            {
                System.err.println("1" + rset.getInt(1));
                if (rset.getInt(1) == 0)
                {
                    done = true;
                }
                else
                {
                    done = true;
                }
            }
            if (done)
            {
                stmt.close();
                rset.close();
                dispose();
            }
            else
            {
                stmt.close();
                rset.close();
                dispose();
                ExtPayMain.bRLogger.logDebug("ERROR", "Error Reversing");
            }
        }
        catch (Exception e)
        {
            ExtPayMain.bRLogger.logError("ERROR[revBillerTxn]", e);
        }
        return done;
    }

    public boolean updateutilityRecord(String fg, int tranNo, String orderNo, String respCode, String TStime, int retryCount)
    {
        System.err.println("flag " + tranNo + " retry count " + retryCount);
        return executeUpdate("UPDATE " + BRController.CMSchemaName + ".BL_STARTTIMES "
                + "SET UPDATEDFG = '" + fg + "',ORDERNO = '" + orderNo + "',STATUS = '" + respCode + "',"
                + "RESPONSETIME = '" + TStime + "',RETRYCOUNT = " + retryCount + "  "
                + "WHERE TRANSACTIONNO = " + tranNo + "", true);
    }

    public boolean updateutilityRecord2(String fg, int tranNo, String orderNo, String respCode, String TStime, int retryCount)
    {
        System.err.println("flag " + tranNo + " retry count " + retryCount);
        return executeUpdate("UPDATE " + BRController.CMSchemaName + ".BL_STARTTIMES "
                + "SET UPDATEDFG = '" + fg + "',STATUS = '3',"
                + "RESPONSETIME = '" + TStime + "',RETRYCOUNT = " + retryCount + "  "
                + "WHERE TRANSACTIONNO = " + tranNo + "", true);
    }

    public int checkRetry(int tranNo)
    {
        int retryCount = 0;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("select NVL(retryCount,0)retryCount "
                    + "from " + BRController.CMSchemaName + ".bl_starttimes  where transactionNo = " + tranNo + ""))
            {
                if (rs.next())
                {
                    retryCount = rs.getInt("retryCount");
                }
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR[checkRetry]", ex);

        }
        return retryCount;
    }

    public boolean checkLicense()
    {
        boolean isValid = true;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT TO_NUMBER(TO_DATE(DISPLAY_VALUE,'DD/MM/YYYY')-(SELECT TO_DATE(DISPLAY_VALUE,'DD/MM/YYYY') FROM "
                    + BRController.CoreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD='S02')) AS DAYS FROM " + BRController.CoreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD='S769'"))
            {
                if (rs.next())
                {
                    if (rs.getDouble(1) < 0)
                    {
                        JOptionPane.showMessageDialog(null, "Software license has expired. Login will be denied.", "License Expired", JOptionPane.ERROR_MESSAGE);
                        isValid = false;
                    }
                }
            }

        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logEvent(ex);
        }
        return isValid;
    }

    public void generateExcelreport(String filter, String billerType, String Biller_ID)
    {
        System.out.println("Start generating...");
        System.out.println("filter ... " + filter);
        System.out.println("billerType ... " + billerType);
        try
        {
            if (billerType.equalsIgnoreCase("SCHOOL"))
            {
                ResultSet rs = executeQueryToResultSet("SELECT BL.BILLER_COL_ACCT,BL.BILLER_ID,BL.MEMBER_NO,BL.MEMBER_NAME,"
                        + "BL.MEMBER_MOBILE_NUMBER,BL.INSTITUTION_NAME,BL.GRADE_LEVEL,BL.SECTION,BL.CATEGORY, "
                        + "BTL.TXN_JRNL_ID,BTL.TXN_AMOUNT,BTL.TXN_DATE_TIME,BL.CURRENCY "
                        + "FROM  " + BRController.CMSchemaName + ".BL_BILLER_TRAN_LOG BTL," + BRController.CMSchemaName + ".BL_BILLER BL  WHERE BTL.CONTRA_ACCOUNT = BL.BILLER_COL_ACCT "
                        + "AND BTL.BILLER_REF_NUMBER = BL.MEMBER_NO "
                        + "AND BTL.PROC_CODE = 'BL02' "
                        + "" + filter + "");

                HSSFWorkbook hwb = new HSSFWorkbook();
                HSSFSheet sheet = hwb.createSheet("new sheet");

                HSSFRow rowhead = sheet.createRow((short) 0);
                rowhead.createCell((short) 0).setCellValue("BILLER_COL_ACCT");
                rowhead.createCell((short) 1).setCellValue("BILLER_ID");
                rowhead.createCell((short) 2).setCellValue("STUDENT_NO");
                rowhead.createCell((short) 3).setCellValue("STUDENT_NAME");
                rowhead.createCell((short) 4).setCellValue("PARENT_MOBILE_NUMBER");
                rowhead.createCell((short) 5).setCellValue("INSTITUTION NAME");
                rowhead.createCell((short) 6).setCellValue("GRADE_LEVEL");
                rowhead.createCell((short) 7).setCellValue("SECTION");
                rowhead.createCell((short) 8).setCellValue("CATEGORY");
                rowhead.createCell((short) 9).setCellValue("TRANSACTION NO");
                rowhead.createCell((short) 10).setCellValue("AMOUNT");
                rowhead.createCell((short) 11).setCellValue("DATETIME");
                rowhead.createCell((short) 12).setCellValue("CURRENCY");

                int index = 1;
                int sno = 0;
                String name = "";
                while (rs.next())
                {
                    sno++;

                    HSSFRow row = sheet.createRow((short) index);
                    row.createCell((short) 0).setCellValue(rs.getString(1));
                    row.createCell((short) 1).setCellValue(rs.getString(2));
                    row.createCell((short) 2).setCellValue(rs.getString(3));
                    row.createCell((short) 3).setCellValue(rs.getString(4));
                    row.createCell((short) 4).setCellValue(rs.getString(5));
                    row.createCell((short) 5).setCellValue(rs.getString(6));
                    row.createCell((short) 6).setCellValue(rs.getString(7));
                    row.createCell((short) 7).setCellValue(rs.getString(8));
                    row.createCell((short) 8).setCellValue(rs.getString(9));
                    row.createCell((short) 9).setCellValue(rs.getString(10));
                    row.createCell((short) 10).setCellValue(rs.getString(11));
                    row.createCell((short) 11).setCellValue(rs.getString(12));
                    row.createCell((short) 12).setCellValue(rs.getString(13));
                    index++;

                }
                String filename = billerType.concat(Biller_ID).concat(".xls");
                File repDir = new File("Reports");
                repDir.mkdir(); // Incase it does not exist     
                try (FileOutputStream os = new FileOutputStream(new File("Reports/" + filename)))
                {
                    hwb.write(os);
                }

                System.out.println("Final for school....");
            }
        }
        catch (SQLException | IOException ex)
        {
            ExtPayMain.bRLogger.logError("ERROR[generateExcelreport]", ex);
        }

    }

    public void generateTVreport(String filter, String billerType)
    {

        System.out.println("biller type " + billerType + " fileter    " + filter);
        try
        {
            if (billerType.equalsIgnoreCase("TV"))
            {

                ResultSet rs = executeQueryToResultSet("select customer_Code, transferTime,case when channel = 8 then 'POS' when channel = 9 then 'MOBILE' else 'OTC' end as channel,status,fee,transactionNo,orderNo from " + BRController.CMSchemaName + ".BL_starttimes btl WHERE billername = 'STARTTIMES' " + filter + "");

                HSSFWorkbook hwb = new HSSFWorkbook();
                HSSFSheet sheet = hwb.createSheet("new sheet");

                HSSFRow rowhead = sheet.createRow((short) 0);
                rowhead.createCell((short) 0).setCellValue("CARD NUMBER");
                rowhead.createCell((short) 1).setCellValue("DATE");
                rowhead.createCell((short) 2).setCellValue("CHANNEL");
                rowhead.createCell((short) 3).setCellValue("STATUS");
                rowhead.createCell((short) 4).setCellValue("AMOUNT");
                rowhead.createCell((short) 5).setCellValue("TRANSACTION NO");
                rowhead.createCell((short) 6).setCellValue("ORDER NO");

                int index = 1;
                int sno = 0;
                String name = "";
                while (rs.next())
                {
                    sno++;

                    HSSFRow row = sheet.createRow((short) index);
                    row.createCell((short) 0).setCellValue(rs.getString(1));
                    row.createCell((short) 1).setCellValue(rs.getString(2));
                    row.createCell((short) 2).setCellValue(rs.getString(3));
                    row.createCell((short) 3).setCellValue(rs.getString(4));
                    row.createCell((short) 4).setCellValue(rs.getString(5));
                    row.createCell((short) 5).setCellValue(rs.getString(6));
                    row.createCell((short) 6).setCellValue(rs.getString(7));

                    index++;

                }

                String filename = "STARTIMES.xls";
                File repDir = new File("Reports");
                repDir.mkdir(); // Incase it does not exist     
                try (FileOutputStream os = new FileOutputStream(new File("Reports/" + filename)))
                {
                    hwb.write(os);
                }

                System.out.println("Final....");
            }
        }
        catch (SQLException | IOException ex)
        {
            ExtPayMain.bRLogger.logError("ERROR[generateTVreport]", ex);
        }

    }
//-----ertax values ----

    public ERTValue ErTaxsElemValue(String pNumber)
    {
        ERTValue ertValue = new ERTValue();
        try
        {
            try (ResultSet rs = executeQueryToResultSet("select P_Number,Applicant_Name,Amount_paid,Bgt_atc_grp,Bgt_atc_naming,Issue_Date,Pmt_Period,Pmt_deadline_date"
                    + " from " + BRController.CMSchemaName + ".S_ERC_TAX where P_Number = '" + pNumber + "'"))
            {
                ertValue.setCount(getRowCount(rs));
                if (rs.next())
                {
                    ertValue.setP_Number(rs.getString(1).trim());
                    ertValue.setApplicant_Name(rs.getString(2).trim());
                    ertValue.setAmount_paid(rs.getDouble(3));
                    ertValue.setBgt_atc_grp(rs.getString(4).trim());
                    ertValue.setBgt_atc_naming(rs.getString(5).trim());
                    ertValue.setIssue_Date(taxDtformat.format(rs.getDate(6)));
                    ertValue.setPmt_Period(rs.getString(7).trim());
                    ertValue.setPmt_deadline_date(taxDtformat.format(rs.getDate(8)));

                }
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR[ErTaxsElemValue]", ex);

        }
        return ertValue;
    }

    public PmtWebserviceData querryPmtData(String pNumber, String currency)
    {
        PmtWebserviceData pmtWebserviceData = new PmtWebserviceData();
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT BL.MEMBER_NO,BL.MEMBER_NAME,BL.BILLER_ID,AC.ACCT_NM,"
                    + "BL.CURRENCY,BL.BILLER_COL_ACCT,BL.GRADE_LEVEL,BL.SECTION,BL.CATEGORY "
                    + "FROM  " + BRController.CMSchemaName + ".BL_BILLER BL ," + BRController.CoreSchemaName + ".ACCOUNT AC WHERE BL.BILLER_COL_ACCT = AC.ACCT_NO AND BL.MEMBER_NO ='" + pNumber.toUpperCase() + "' AND BL.CURRENCY = '" + currency + "'"))
            {

                if (rs.next())
                {
                    pmtWebserviceData.setStudentNumber(rs.getString(1).trim());
                    pmtWebserviceData.setStudentName(rs.getString(2).trim());
                    pmtWebserviceData.setSchoolId(rs.getString(3).trim());
                    pmtWebserviceData.setSchoolName(rs.getString(4).trim());
                    pmtWebserviceData.setCurrency(rs.getString(5).trim());
                    pmtWebserviceData.setAccountNumber(rs.getString(6).trim());
                    pmtWebserviceData.setStudentGrade(rs.getString(7).trim());
                    pmtWebserviceData.setStudentSection(rs.getString(8).trim());
                    pmtWebserviceData.setStudentCategory(rs.getString(9).trim());
                }
                else
                {
                    pmtWebserviceData.setStudentNumber("Record not Found");
                    pmtWebserviceData.setStudentName("Record not Found");
                    pmtWebserviceData.setSchoolId("Record not Found");
                    pmtWebserviceData.setSchoolName("Record not Found");
                    pmtWebserviceData.setCurrency("Record not Found");
                    pmtWebserviceData.setAccountNumber("Record not Found");
                }
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR[querryPmtData]", ex);
        }
        return pmtWebserviceData;
    }

    public BillsTxn querryBillerCoreTxn()
    {
        BillsTxn billsTxn = new BillsTxn();
        try
        {
            try (ResultSet rs = executeQueryToResultSet(
                    "SELECT DISTINCT  TRAN_JOURNAL_ID,DEPOSITOR_PAYEE_NM,TRAN_AMT,ACCT_NO,EVENT_CD,CREATED_BY,TRAN_REF_TXT,TRAN_DESC,TRAN_DT, "
                    + " CASE WHEN ACCT_CRNCY_ID = 830 THEN 'CDF' ELSE 'USD' END AS CURRENCY "
                    + " FROM   " + BRController.CoreSchemaName + ".TXN_JOURNAL TXJ, " + BRController.CMSchemaName + ".BL_STARTTIMES BLS "
                    + " WHERE TRAN_JOURNAL_ID NOT IN (BLS.TRANSACTIONNO) AND DEPOSITOR_PAYEE_NM = BLS.SMARTCARDCODE  AND EVENT_CD = 'BL106'  AND TXJ.REC_ST = 'A' AND BLS.FEE = 0"
                    + " AND ACCT_CRNCY_ID = (SELECT   CRNCY_ID FROM   " + BRController.CoreSchemaName + ".CURRENCY WHERE   CRNCY_CD = BLS.CURRENCY)  "
                    + " AND TRAN_DT = (SELECT   TO_DATE (DISPLAY_VALUE, 'DD/MM/YYYY') FROM   " + BRController.CoreSchemaName + ".CTRL_PARAMETER WHERE   PARAM_CD = 'S02') "
                    + " UNION ALL "
                    + " SELECT DISTINCT  TRAN_JOURNAL_ID,DEPOSITOR_PAYEE_NM,TRAN_AMT,ACCT_NO,EVENT_CD,CREATED_BY,TRAN_REF_TXT,TRAN_DESC,TRAN_DT, "
                    + " CASE WHEN ACCT_CRNCY_ID = 830 THEN 'CDF' ELSE 'USD' END AS CURRENCY "
                    + " FROM   " + BRController.CoreSchemaName + ".TXN_JOURNAL TXJ, " + BRController.CMSchemaName + ".BL_ERC_TAX_TXN BTT "
                    + " WHERE  TRAN_JOURNAL_ID NOT IN (BTT.TXN_NO) AND DEPOSITOR_PAYEE_NM = BTT.P_NUMBER "
                    + " AND EVENT_CD = 'BL107'  AND TXJ.REC_ST = 'A'  AND ACCT_CRNCY_ID = (SELECT   CRNCY_ID FROM   " + BRController.CoreSchemaName + ".CURRENCY WHERE   CRNCY_CD = BTT.CURRENCY) "
                    + " AND SRC_OF_FUNDS_ID IN (2911, 292, 293, 294, 295, 296, 297, 298) AND TRAN_DT = (SELECT   TO_DATE (DISPLAY_VALUE, 'DD/MM/YYYY') "
                    + " FROM   " + BRController.CoreSchemaName + ".CTRL_PARAMETER WHERE   PARAM_CD = 'S02')  "
                    + " UNION ALL "
                    + " SELECT  DISTINCT  TRAN_JOURNAL_ID,DEPOSITOR_PAYEE_NM,TRAN_AMT,ACCT_NO,EVENT_CD,CREATED_BY,TRAN_REF_TXT,TRAN_DESC,TRAN_DT, "
                    + " CASE WHEN ACCT_CRNCY_ID = 830 THEN 'CDF' ELSE 'USD' END AS CURRENCY "
                    + " FROM  " + BRController.CoreSchemaName + ".TXN_JOURNAL TXJ, " + BRController.CMSchemaName + ".BL_SCHOOL_FEE BSF  "
                    + " WHERE TRAN_JOURNAL_ID NOT IN (BSF.TXN_NO) AND DEPOSITOR_PAYEE_NM = BSF.STUDENT_NO AND EVENT_CD = 'BL105'   AND TXJ.REC_ST = 'A' "
                    + " AND ACCT_CRNCY_ID = (SELECT CRNCY_ID  FROM " + BRController.CoreSchemaName + ".CURRENCY WHERE CRNCY_CD = BSF.CURRENCY)  "
                    + " AND TRAN_DT = (SELECT TO_DATE (DISPLAY_VALUE, 'DD/MM/YYYY') FROM  " + BRController.CoreSchemaName + ".CTRL_PARAMETER WHERE PARAM_CD = 'S02')"))//'BL105','BL106',
            {
                billsTxn.setCount(getRowCount(rs));
                while (rs.next())
                {
                    billsTxn.setTxnNumber(rs.getLong(1));
                    billsTxn.setReferenceNo(rs.getString(2).trim());
                    billsTxn.setAmount(BigDecimal.valueOf(rs.getDouble(3)));
                    billsTxn.setAcct_no(rs.getString(4).trim());
                    billsTxn.setEventCD(rs.getString(5));
                    billsTxn.setUser(rs.getString(6));
                    billsTxn.setTranRefTxt(rs.getString(7));
                    billsTxn.setDescription(rs.getString(8));
                    billsTxn.setTranDate(rs.getString(9));
                    billsTxn.setCurrency(rs.getString(10));
                }

            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR[querryBillerCoreTxn]", ex);
        }
        return billsTxn;
    }

    public ERTBourderauVal ErTaxsPaymentElem(String pNumber)
    {
        ERTBourderauVal ertValue = new ERTBourderauVal();
        int txnNo = Integer.parseInt(pNumber);
        try
        {
            try (ResultSet rs = executeQueryToResultSet("select txn_no,effectivedt,amount_paid,"
                    + "case when p_number like 'NP%' then 'NP' when p_number like 'AMR%' then 'AMR' end as doc_type,"
                    + "AccountNumber,p_number from " + BRController.CMSchemaName + ".BL_ERC_TAX_TXN  where updatedfg = 'Y' and verified = NULL and status in ('S','F')  and txn_no = " + txnNo + ""))
            {
                ertValue.setCount(getRowCount(rs));
                if (rs.next())
                {
                    ertValue.setTxn_no(rs.getInt(1));
                    ertValue.setEffective_dt(taxDtformat.format(rs.getDate(2)));
                    ertValue.setAmount_paid(rs.getBigDecimal(3));
                    ertValue.setDoc_type(rs.getString(4).trim());
                    ertValue.setAccount_number(rs.getString(5).trim());
                    ertValue.setP_number(rs.getString(6));

                }
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR[ErTaxsPaymentElem]", ex);
        }
        return ertValue;
    }

    public ERTPaymentValue ErTaxsPaymtValue()
    {
        ERTPaymentValue ertValue = new ERTPaymentValue();
        try
        {
            try (ResultSet rs = executeQueryToResultSet("select txn_no,effectivedt,amount_paid,"
                    + "case when p_number like 'NP%' then 'NP' when p_number like 'AMR%' then 'AMR' end as doc_type,"
                    + "AccountNumber,p_number from  " + BRController.CMSchemaName + ".BL_ERC_TAX_TXN  where updatedfg = 'Y' and verified IS NULL "
                    + "and status in ('S')  and txn_no IS NOT NULL and (p_number like 'NP%' or p_number like 'AMR%')"))
            {
                if (rs != null)
                {
                    ertValue.setCount(getRowCount(rs));
                    if (rs.next())
                    {
                        ertValue.setTxn_no(rs.getInt(1));
                        ertValue.setEffective_dt(taxDtformat.format(rs.getDate(2)));
                        ertValue.setAmount_paid(rs.getBigDecimal(3));
                        ertValue.setDoc_type(rs.getString(4).trim());
                        ertValue.setAccount_number(rs.getString(5).trim());
                        ertValue.setP_number(rs.getString(6));
                    }
                }
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR[ErTaxsPaymtValue]", ex);
        }
        return ertValue;

    }

    public void generateTaxreport(String filter, String billerType)
    {
        try
        {
            ResultSet rs = executeQueryToResultSet("select P_Number,Applicant_Name,Issue_Date,Amount_paid,Bgt_atc_naming,"
                    + "Bgt_atc_grp,Pmt_Period,Pmt_deadline_date,AccountNumber, EffectiveDt,Txn_No,Status "
                    + "from " + BRController.CMSchemaName + ".BL_ERC_TAX_TXN btl where  P_Number is not null" + filter + "");

            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("new sheet");

            HSSFRow rowhead = sheet.createRow((short) 0);
            rowhead.createCell((short) 0).setCellValue("PERCEPTION NOTE");
            rowhead.createCell((short) 1).setCellValue("APPLICANT NAME");
            rowhead.createCell((short) 2).setCellValue("ISSUE DATE");
            rowhead.createCell((short) 3).setCellValue("AMOUNT PAID");
            rowhead.createCell((short) 4).setCellValue("BUDGET ARTICLE NAMING");
            rowhead.createCell((short) 5).setCellValue("BUDGET ARTICLE GROUP");
            rowhead.createCell((short) 6).setCellValue("PAYMENT PERIOD");
            rowhead.createCell((short) 7).setCellValue("DEADLINE DATE");
            rowhead.createCell((short) 8).setCellValue("ACCOUNT NUMBER");
            rowhead.createCell((short) 9).setCellValue("EFFECTIVE DATE");
            rowhead.createCell((short) 10).setCellValue("TRANSACTION ID");
            rowhead.createCell((short) 11).setCellValue("STATUS");

            int index = 1;
            int sno = 0;
            String name = "";
            while (rs.next())
            {
                sno++;
                HSSFRow row = sheet.createRow((short) index);
                row.createCell((short) 0).setCellValue(rs.getString(1));
                row.createCell((short) 1).setCellValue(rs.getString(2));
                row.createCell((short) 2).setCellValue(rs.getString(3));
                row.createCell((short) 3).setCellValue(rs.getString(4));
                row.createCell((short) 4).setCellValue(rs.getString(5));
                row.createCell((short) 5).setCellValue(rs.getString(6));
                row.createCell((short) 6).setCellValue(rs.getString(7));
                row.createCell((short) 7).setCellValue(rs.getString(8));
                row.createCell((short) 8).setCellValue(rs.getString(9));
                row.createCell((short) 9).setCellValue(rs.getString(10));
                row.createCell((short) 10).setCellValue(rs.getString(11));
                row.createCell((short) 11).setCellValue(rs.getString(12));

                index++;

            }
            System.err.println("in the tax rept3");
            String filename = "TAX" + taxDtformat.format(new Date()) + ".xls";
            System.err.println("in the tax rept4" + filename);
            File repDir = new File("Reports");
            repDir.mkdir(); // Incase it does not exist     
            try (FileOutputStream os = new FileOutputStream(new File("Reports/" + filename)))
            {
                hwb.write(os);
            }

            System.out.println("Final....");
            //   }
        }
        catch (SQLException | IOException ex)
        {
            ExtPayMain.bRLogger.logError("ERROR[generateTaxreport]", ex);
        }

    }

    public void updtTaxtxnSt(String PNo, String status, String verified)
    {
        executeUpdate("update " + BRController.CMSchemaName + ".BL_ERC_TAX_TXN set status = '" + status + "' , verified = '" + verified + "' where p_number = '" + PNo + "'", true);
    }

    private boolean writeOutputToFile(StringBuilder buffer, String directory, String fileName)
    {
        try
        {
            new File(directory).mkdirs();
            File file = new File(directory, fileName);
            try (PrintWriter fileWriter = new PrintWriter(file))
            {
                fileWriter.write(buffer.toString());

                fileWriter.flush();
            }
            return true;
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR[writeOutputToFile]", ex);

            return false;
        }

    }

    public int countPendingAlerts()
    {
        int count = 0;
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT REC_ST FROM  " + BRController.CMSchemaName + ".ALERTS WHERE REC_ST='P'"))
            {
                count = getRowCount(rs);
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ExtPayMain.bRLogger.logError("ERROR[countPendingAlerts]", ex);
            }
        }
        return count;
    }

    public long getAccountBuId(String accountNumber)
    {
        long buId = getDefaultBuid();

        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT MAIN_BRANCH_ID FROM  " + BRController.CoreSchemaName + ".ACCOUNT WHERE ACCT_NO='" + accountNumber + "'"))
            {
                if (rs.next())
                {
                    buId = rs.getLong("MAIN_BRANCH_ID");
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ExtPayMain.bRLogger.logError("ERROR[getAccountBuId]", ex);
            }
        }
        return buId;
    }

    public String getGlAccount(String ledgerNumber)
    {
        long buId = -88;
        String glAcctNo = "";
        try
        {
            try (ResultSet rs = executeQueryToResultSet("SELECT GL_ACCT_NO FROM  " + BRController.CoreSchemaName + ".GL_ACCOUNT WHERE LEDGER_NO= '" + Integer.parseInt(ledgerNumber) + "' AND BU_ID = " + buId + ""))
            {
                if (rs.next())
                {
                    glAcctNo = rs.getString("GL_ACCT_NO");
                }
            }
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ExtPayMain.bRLogger.logError("ERROR[getGlAccount]", ex);
            }
        }
        return glAcctNo;
    }

    public String padString(String s, int i, char c, boolean leftPad)
    {
        StringBuilder buffer = new StringBuilder(s);
        int j = buffer.length();
        if (i > 0 && i > j)
        {
            for (int k = 0; k <= i; k++)
            {
                if (leftPad)
                {
                    if (k < i - j)
                    {
                        buffer.insert(0, c);
                    }
                }
                else if (k > j)
                {
                    buffer.append(c);
                }
            }
        }
        return buffer.toString().substring(0, i);
    }

    public void swapAccounts(TXRequest tXRequest)
    {
        String debitAcct = tXRequest.getAccountNumber2();
        tXRequest.setAccountNumber2(tXRequest.getAccountNumber1());
        tXRequest.setAccountNumber1(debitAcct);
    }

//    public boolean isAccountRestricted(String accountNumber) {
//        boolean accountRestricted = true;
//        try {
//            try (ResultSet resultSet = executeQueryToResultSet("SELECT ACCT_NO FROM  " + BRController.CoreSchemaName + ".V_ACCOUNTS WHERE PROD_CAT_TY='DP' AND ACCT_NO='" + accountNumber + "' AND PROD_CD IN (" + BRController.AllowedProductCodes + ")")) {
//                if (resultSet.next()) {
//                    accountRestricted = false;
//                } else {
//                   // getXapiCaller().setXapiRespCode(EICodes.TRANSACTION_NOT_ALLOWED_FOR_ACCOUNT);
//                }
//            }
//        } catch (Exception ex) {
//           // getXapiCaller().setXapiRespCode(EICodes.TRANSACTION_NOT_ALLOWED_FOR_ACCOUNT);
//            getXapiCaller().logException(ex);
//        }
//        return accountRestricted;
//    }
    public String fetchJournalId(TXRequest tXRequest)
    {
        String journalId = null;
        try
        {
            try (ResultSet resultSet = executeQueryToResultSet("SELECT MAX(TRAN_JOURNAL_ID) TRAN_JOURNAL_ID FROM  " + BRController.CoreSchemaName + ".TXN_JOURNAL WHERE ACCT_NO='" + tXRequest.getAccountNumber1() + "' AND TRAN_AMT=" + tXRequest.getTxnAmount() + " AND TRAN_DESC='" + tXRequest.getTxnNarration() + "' AND SYS_CREATE_TS > TO_DATE(SYSDATE-1)"))
            {
                if (resultSet.next())
                {
                    journalId = resultSet.getString("TRAN_JOURNAL_ID");
                }
            }
        }
        catch (Exception ex)
        {
            getXapiCaller().logException(ex);
        }
        return journalId;
    }

    public boolean checkIfExists(String query)
    {
        boolean exists = false;
        try
        {
            try (ResultSet rs = executeQueryToResultSet(query))
            {
                exists = rs != null && rs.next();
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("ERROR", ex);
        }
        return exists;
    }

    public Object[][] executeQueryToArray(String query)
    {
        return rsToArray(executeQuery(query, true));
    }

    public ResultSet executeQueryToResultSet(String query)
    {
        return executeQuery(query, true);
    }

    public int getRowCount(ResultSet rs)
    {
        int records = 0;
        try
        {
            rs.last();
            records = rs.getRow();
            rs.beforeFirst();
        }
        catch (Exception ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ExtPayMain.bRLogger.logError("ERROR", ex);
            }
        }
        return records;
    }

    private Object[][] rsToArray(ResultSet rs)
    {
        if (rs != null)
        {
            try
            {
                int row = 0, records = getRowCount(rs), fields = rs.getMetaData().getColumnCount();
                Object[][] results = (records == 0) ? new Object[0][0] : new Object[records][fields];
                while (rs.next())
                {
                    for (int col = 0; col < fields; col++)
                    {
                        results[row][col] = rs.getObject(col + 1);
                    }
                    row++;
                }
                try
                {
                    rs.close();
                }
                catch (Exception ex)
                {
                    if (getXapiCaller() != null)
                    {
                        getXapiCaller().logException(ex);
                    }
                    else
                    {
                        ExtPayMain.bRLogger.logError("ERROR", ex);
                    }
                }
                return results;
            }
            catch (Exception ex)
            {
                if (getXapiCaller() != null)
                {
                    getXapiCaller().logException(ex);
                }
                else
                {
                    ExtPayMain.bRLogger.logError("ERROR", ex);
                }
            }
        }
        return new Object[0][0];
    }

    private ResultSet executeQuery(String query, boolean retry)
    {
        try
        {
            if ("Y".equalsIgnoreCase(BRController.EnableDebug))
            {
                ExtPayMain.bRLogger.logEvent(query);

            }
            if (getDbConnection() == null)
            {
                connectToDB();
            }
            else if (getDbConnection().isClosed())
            {
                connectToDB();
            }
            if (getDbConnection() != null)
            {
                return getDbConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY).executeQuery(query);
            }
        }
        catch (Exception ex)
        {
            if (String.valueOf(ex.getMessage()).contains("ORA-01000"))
            {
                dispose();
                if (retry)
                {
                    return executeQuery(query, false);
                }
            }
            else
            {
                if (getXapiCaller() != null)
                {
                    getXapiCaller().logException(ex);
                }
                else
                {
                    ExtPayMain.bRLogger.logError("ERROR", ex);
                }
            }
        }
        return null;
    }

    public boolean executeUpdate(String update, boolean retry)
    {
        try
        {
            if ("Y".equalsIgnoreCase(BRController.EnableDebug))
            {
                ExtPayMain.bRLogger.logEvent("logTransaction", update);
            }
            if (getDbConnection() == null)
            {
                connectToDB();
            }
            else if (getDbConnection().isClosed())
            {
                connectToDB();
            }
            if (getDbConnection() != null)
            {
                update = update.replaceAll("'null'", "NULL").replaceAll("'NULL'", "NULL");
                getDbConnection().createStatement().executeUpdate(update);
                if (ChannelUtil.SERVICE_OFFLINE)
                {
                    logScript(update);
                }
                return true;
            }
        }
        catch (Exception ex)
        {
            if (String.valueOf(ex.getMessage()).contains("ORA-01000"))
            {
                dispose();
                if (retry)
                {
                    return executeUpdate(update, false);
                }
            }
            else
            {
                if (getXapiCaller() != null)
                {
                    getXapiCaller().logException(ex);
                }
                else
                {
                    ExtPayMain.bRLogger.logError("executeUpdate", ex);
                }
            }
        }
        return false;
    }

    private void logScript(String script)
    {
        try
        {
            if ("Y".equalsIgnoreCase(BRController.EnableDebug))
            {
                ExtPayMain.bRLogger.logEvent("logTransaction", script);
            }
            if (getDbConnection() != null)
            {
                try (PreparedStatement ps = getDbConnection().prepareStatement(
                        "INSERT INTO " + BRController.CoreSchemaName + ".UPDATE_SCRIPT (SCRIPT, STATUS) VALUES(?, ?)"))
                {
                    ps.setString(1, script);
                    ps.setString(2, "N");
                    ps.executeUpdate();
                }
            }
        }
        catch (SQLException ex)
        {
            if (getXapiCaller() != null)
            {
                getXapiCaller().logException(ex);
            }
            else
            {
                ExtPayMain.bRLogger.logError("ERROR", ex);
            }
        }
    }

    public void dispose()
    {
        try
        {
            if (getLogTxnStatement() != null)
            {
                getLogTxnStatement().close();
            }
            if (getDbConnection() != null)
            {
                getDbConnection().close();
            }
        }
        catch (Exception ex)
        {
            setLogTxnStatement(null);
            setDbConnection(null);
        }
    }

//    private void logTransaction(TXRequest tXRequest, String procCode, String utilType,String respCode) {
//        logTransaction(tXRequest, respCode, procCode, utilType);
//    }
    private void logTransaction(TXRequest tXRequest, String respCode, String procCode, String utilType, String pmtType)
    {
        try
        {
            // System.err.println("getReferenceType " + tXRequest.getReferenceType());
            connectToDB();
            getLogTxnStatement().setString(1, tXRequest.getReference());
            getLogTxnStatement().setLong(2, tXRequest.getChannelId());
            getLogTxnStatement().setString(3, tXRequest.getAccessCode());

            getLogTxnStatement().setString(4, tXRequest.getAccountNumber1());
            getLogTxnStatement().setString(5, tXRequest.getAccountNumber2());

            getLogTxnStatement().setString(6, tXRequest.getCurrencyCode());
            getLogTxnStatement().setBigDecimal(7, tXRequest.getTxnAmount());

            getLogTxnStatement().setBigDecimal(8, tXRequest.getChargeAmount());
            getLogTxnStatement().setString(9, tXRequest.getTxnNarration());

            getLogTxnStatement().setString(10, respCode);
            getLogTxnStatement().setString(11, "");
            //getLogTxnStatement().setString(15, BRController.getXapiMessage(respCode));
            getLogTxnStatement().setString(12, successResponse.equals(respCode) ? "APPROVED" : "REJECTED");

            getLogTxnStatement().setString(13, respCode);
            getLogTxnStatement().setString(14, txnJournalId);

            getLogTxnStatement().setString(15, chargeJournal);
            getLogTxnStatement().setString(16, procCode);

            getLogTxnStatement().setString(17, utilType);
            getLogTxnStatement().setString(18, "N");
            getLogTxnStatement().setString(19, pmtType);
            getLogTxnStatement().setString(20, tXRequest.getReferenceType());

            getLogTxnStatement().execute();

        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("logTransaction", ex);

        }
    }

    private void logFeeTransaction(TXRequest tXRequest, String respCode, String procCode, String utilType, String pmtType, SchFeesResponse feesResponse)
    {
        try
        {

            connectToDB();
            getLogFeeTxnStatement().setString(1, feesResponse.getTxnJournalId());
            getLogFeeTxnStatement().setString(2, feesResponse.getSchoolId());
            getLogFeeTxnStatement().setString(3, feesResponse.getSchoolName());

            getLogFeeTxnStatement().setString(4, feesResponse.getStudentNo());
            getLogFeeTxnStatement().setString(5, feesResponse.getStudentName());

            getLogFeeTxnStatement().setString(6, "0");
            getLogFeeTxnStatement().setString(7, "grade");

            getLogFeeTxnStatement().setBigDecimal(8, tXRequest.getTxnAmount());
            getLogFeeTxnStatement().setString(9, "section");

            getLogFeeTxnStatement().setString(10, "cat");
            getLogFeeTxnStatement().setDate(11, (java.sql.Date) getCurrentDate());
            getLogFeeTxnStatement().setDate(12, (java.sql.Date) getCurrentDate());

            getLogFeeTxnStatement().setString(13, utilType);
            getLogFeeTxnStatement().setString(14, utilType);

            getLogFeeTxnStatement().setString(15, feesResponse.getChargeJournalId());
            getLogFeeTxnStatement().setString(16, respCode);//feesResponse.getRespCode());0

            getLogFeeTxnStatement().setString(17, feesResponse.getCurrency());
            getLogFeeTxnStatement().setString(18, feesResponse.getCreditAccount());
            getLogFeeTxnStatement().setString(19, "0");

            getLogFeeTxnStatement().execute();

        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("logFEETransaction", ex);
        }
    }

    private void logTaxTransaction(TXRequest tXRequest, String terminalType, String procCode, String utilType, String pmtType, TaxResponse taxResponse)
    {
        try
        {
            // System.err.println("getReferenceType1 " + (timestamp) + " / " + pmtType + " / " + procCode);
            connectToDB();
            getLogTaxTxn().setString(1, procCode);
            getLogTaxTxn().setString(2, taxResponse.getNotePerception());
            getLogTaxTxn().setString(3, taxResponse.getAccountName());

            getLogTaxTxn().setBigDecimal(4, taxResponse.getAmount());
            getLogTaxTxn().setString(5, taxResponse.getArticleGroup());

            getLogTaxTxn().setString(6, taxResponse.getBudgetNaming());
            getLogTaxTxn().setString(7, taxResponse.getIssueDate());

            getLogTaxTxn().setString(8, taxResponse.getPmtPeriod());
            getLogTaxTxn().setString(9, taxResponse.getDeadline());

            getLogTaxTxn().setString(10, tXRequest.getAccountNumber2());
            getLogTaxTxn().setString(11, "");//updated

            getLogTaxTxn().setString(12, "");//veridied

            getLogTaxTxn().setDate(13, (java.sql.Date) getCurrentDate());
            getLogTaxTxn().setString(14, "");//order no

            getLogTaxTxn().setString(15, "");//resppnse time
            getLogTaxTxn().setString(16, terminalType.substring(0, 3));//channel

            getLogTaxTxn().setString(17, terminalType.substring(0, 3));//userchannel
            getLogTaxTxn().setLong(18, 0);
            getLogTaxTxn().setString(19, "");//status
            getLogTaxTxn().setString(20, terminalType.substring(0, 3));
            getLogTaxTxn().setString(21, taxResponse.getTxnJournalId());
            getLogTaxTxn().setString(22, "");
            getLogTaxTxn().setString(23, tXRequest.getCurrencyCode());

            getLogTaxTxn().execute();

        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("logTransaction", ex);

        }
    }

    public void logTVTransaction(TXRequest tXRequest, String terminalType, String procCode, String utilType, String pmtType, TvResponse tvResponse)
    {
        try
        {
            // System.err.println("getReferenceType1 " + (timestamp) + " / " + pmtType + " / " + procCode);
            connectToDB();
            getLogTVTxn().setString(1, procCode);
            getLogTVTxn().setString(2, tvResponse.getTxnJournalId());
            getLogTVTxn().setString(3, BRController.StartimesPayerId);
            getLogTVTxn().setString(4, BRController.StartimesPayerPassword);

            getLogTVTxn().setString(5, tXRequest.getReferenceType());
            getLogTVTxn().setString(6, tvResponse.getAccountName());

            getLogTVTxn().setString(7, "0");
            getLogTVTxn().setString(8, tXRequest.getReferenceType());

            getLogTVTxn().setBigDecimal(9, tXRequest.getTxnAmount());
            getLogTVTxn().setString(10, "0");

            getLogTVTxn().setString(11, "0");
            getLogTVTxn().setDate(12, (java.sql.Date) getCurrentDate());//updated

            getLogTVTxn().setString(13, "");//email

            getLogTVTxn().setString(14, "");
            getLogTVTxn().setDate(15, (java.sql.Date) getCurrentDate());//updated

            getLogTVTxn().setString(16, "");//orderNo
            getLogTVTxn().setString(17, "");//response time

            getLogTVTxn().setString(18, terminalType.substring(0, 3));//userchannel
            getLogTVTxn().setString(19, terminalType.substring(0, 3));//userchannel
            getLogTVTxn().setString(20, "");//status
            getLogTVTxn().setLong(21, 0);
            getLogTVTxn().setString(22, "");//status
            getLogTVTxn().setString(23, tXRequest.getCurrencyCode());//status

            getLogTVTxn().execute();

        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("logTransaction", ex);
        }
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
     * @return the xapiCaller
     */
    public XAPICaller getXapiCaller()
    {
        return xapiCaller;
    }

    /**
     * @param xapiCaller the xapiCaller to set
     */
    public void setXapiCaller(XAPICaller xapiCaller)
    {
        this.xapiCaller = xapiCaller;
    }

    /**
     * @return the logTaxTxn
     */
    public CallableStatement getLogTaxTxn()
    {
        return logTaxTxn;
    }

    /**
     * @param logTaxTxn the logTaxTxn to set
     */
    public void setLogTaxTxn(CallableStatement logTaxTxn)
    {
        this.logTaxTxn = logTaxTxn;
    }

    /**
     * @return the logTVTxn
     */
    public CallableStatement getLogTVTxn()
    {
        return logTVTxn;
    }

    /**
     * @param logTVTxn the logTVTxn to set
     */
    public void setLogTVTxn(CallableStatement logTVTxn)
    {
        this.logTVTxn = logTVTxn;
    }

    /**
     * @return the dbConnection
     */
    public Connection getDbConnection()
    {
        return dbConnection;
    }

    /**
     * @param dbConnection the dbConnection to set
     */
    public void setDbConnection(Connection dbConnection)
    {
        this.dbConnection = dbConnection;
    }

    /**
     * @return the logTxnStatement
     */
    public CallableStatement getLogTxnStatement()
    {
        return logTxnStatement;
    }

    /**
     * @param logTxnStatement the logTxnStatement to set
     */
    public void setLogTxnStatement(CallableStatement logTxnStatement)
    {
        this.logTxnStatement = logTxnStatement;
    }

    /**
     * @return the logFeeTxnStatement
     */
    public CallableStatement getLogFeeTxnStatement()
    {
        return logFeeTxnStatement;
    }

    /**
     * @param logFeeTxnStatement the logFeeTxnStatement to set
     */
    public void setLogFeeTxnStatement(CallableStatement logFeeTxnStatement)
    {
        this.logFeeTxnStatement = logFeeTxnStatement;
    }

    public String fetchTxnJournal(String smartCardNo)
    {
        try (ResultSet rs = executeQueryToResultSet("SELECT TRAN_JOURNAL_ID FROM OFFLINE_SCHEMA.V_JOURNALS");)
        {
            if (rs.next())
            {
                BigDecimal tranJournal = rs.getBigDecimal(1);
                executeUpdate("INSERT INTO OFFLINE_SCHEMA.ST_JOURNALS (SMART_CARD_NO, JOURNAL_ID, CREATE_DT) VALUES('"
                        + smartCardNo + "', " + tranJournal + ", SYSDATE)", true);
                return tranJournal.toPlainString();
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("logTransaction", ex);
        }
        return BRController.generateUniqueKey();
    }

    public DepositTxnOutputData[] retrieveAccountStatement(String debitAccount)
    {
        DepositTxnOutputData[] depositTxnOutputData = null;
        try (ResultSet rs = executeQueryToResultSet("SELECT * FROM " + BRController.CoreSchemaName + ".OFFLINE_STMNT WHERE ACCOUNT_NUMBER = '" + debitAccount + "' AND  ROWNUM <= 5 ORDER BY TXN_DATE_TIME DESC"))
        {
            if (rs.isBeforeFirst())
            {
                int rowCount = getRowCount(rs);
                depositTxnOutputData = new DepositTxnOutputData[rowCount];
                int index = 0;
                while (rs.next())
                {
                    DepositTxnOutputData dpt = new DepositTxnOutputData();
                    dpt.setTxnDate(rs.getDate("TXN_DATE_TIME").getTime());
                    dpt.setTxnDescription(rs.getString("DESCRIPTION"));
                    dpt.setTxnAmount(rs.getBigDecimal("TXN_AMOUNT"));
                    dpt.setTxnCcyISOCode(rs.getString("CURRENCY_CODE"));
                    if (debitAccount.equalsIgnoreCase(rs.getString("ACCOUNT_NUMBER")))
                    {
                        dpt.setDrcrFlag("DR");
                    }
                    else
                    {
                        dpt.setDrcrFlag("CR");
                    }
                    depositTxnOutputData[index] = dpt;
                    index++;
                }
            }
            else
            {
                depositTxnOutputData = new DepositTxnOutputData[0];
            }
        }
        catch (Exception ex)
        {
            ExtPayMain.bRLogger.logError("logTransaction", ex);
        }
        return depositTxnOutputData;
    }

    public String formatIsoAmount(BigDecimal amount)
    {
        String amtStr = padString(amount.abs().setScale(2, BigDecimal.ROUND_DOWN).toPlainString().replace(".", ""), 12, '0', true);
        return amtStr.substring(amtStr.length() - 12);
    }

    public BigDecimal extractISOAmount(ISOMsg sendReceive)
    {
        if (sendReceive.hasField(54))
        {
            String balanceString = sendReceive.getString(54);
            if (balanceString == null)
            {
                return BigDecimal.ZERO;
            }
            BigDecimal bal = new BigDecimal(balanceString.substring(28));
            return balanceString.startsWith("C") ? bal.movePointLeft(2) : bal
                    .movePointLeft(2).negate();
        }
        return BigDecimal.ZERO;
    }

}
