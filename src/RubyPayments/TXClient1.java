/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPayments;

import static RubyPayments.TXUtility.convertToString;
import RubyPaymentWS.PMTService;
import com.neptunesoftware.supernova.ws.client.AccountWebService;
import com.neptunesoftware.supernova.ws.client.AccountWebServiceEndPointPort_Impl;
import com.neptunesoftware.supernova.ws.client.FundsTransferWebService;
import com.neptunesoftware.supernova.ws.client.FundsTransferWebServiceEndPointPort_Impl;
import com.neptunesoftware.supernova.ws.client.TransactionsWebService;
import com.neptunesoftware.supernova.ws.client.TransactionsWebServiceEndPointPort_Impl;
import com.neptunesoftware.supernova.ws.client.TxnProcessWebService;
import com.neptunesoftware.supernova.ws.client.TxnProcessWebServiceEndPointPort_Impl;
import com.neptunesoftware.supernova.ws.common.XAPIException;
import com.neptunesoftware.supernova.ws.common.XAPIRequestBaseObject;
import com.neptunesoftware.supernova.ws.server.account.data.AccountBalanceOutputData;
import com.neptunesoftware.supernova.ws.server.account.data.AccountBalanceRequest;
import com.neptunesoftware.supernova.ws.server.transaction.data.ArrayOfDepositTxnOutputData_Literal;
import com.neptunesoftware.supernova.ws.server.transaction.data.DepositTxnOutputData;
import com.neptunesoftware.supernova.ws.server.transaction.data.GLTransferOutputData;
import com.neptunesoftware.supernova.ws.server.transaction.data.GLTransferRequest;
import com.neptunesoftware.supernova.ws.server.transaction.data.TransactionInquiryRequest;
import com.neptunesoftware.supernova.ws.server.transaction.data.TxnResponseOutputData;
import com.neptunesoftware.supernova.ws.server.transfer.data.FundsTransferOutputData;
import com.neptunesoftware.supernova.ws.server.transfer.data.FundsTransferRequestData;
import com.neptunesoftware.supernova.ws.server.txnprocess.data.XAPIBaseTxnRequestData;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISODate;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

/**
 *
 * @author Pecherk
 */
public final class TXClient1 {

    //private TXProcessor tXProcessor;
    private AccountWebService accountWebService;
    private TransactionsWebService transactionsWebService;
    private FundsTransferWebService transferWebService;
    private TxnProcessWebService processWebService;
    private PMTService pMTService;
    public int Counter;
    String SYSTEM_ERROR = "91";
    TXUtility tXUtility;
    XAPICaller xAPICaller;

    public TXClient1(XAPICaller xaPICaller) {
        //settXProcessor(xAPICaller);
        xAPICaller = xaPICaller;
        if (ChannelUtil.SERVICE_OFFLINE) {
            initializeChannel();
        } else {
            connectToCore();
        }
    }

    private void connectToCore() {
        try {
            Object[] coreBankingNodes = BRController.CoreBankingNodes.toArray();
            if (coreBankingNodes.length > 0) {
                Arrays.sort(coreBankingNodes);
                for (Object cBNode : coreBankingNodes) {
                    accountWebService = new AccountWebServiceEndPointPort_Impl(((CBNode) cBNode).getWsContextURL() + "AccountWebServiceBean?wsdl").getAccountWebServiceSoapPort();
                    transactionsWebService = new TransactionsWebServiceEndPointPort_Impl(((CBNode) cBNode).getWsContextURL() + "TransactionsWebServiceBean?wsdl").getTransactionsWebServiceSoapPort();
                    transferWebService = new FundsTransferWebServiceEndPointPort_Impl(((CBNode) cBNode).getWsContextURL() + "FundsTransferWebServiceBean?wsdl").getFundsTransferWebServiceSoapPort();
                    processWebService = new TxnProcessWebServiceEndPointPort_Impl(((CBNode) cBNode).getWsContextURL() + "TxnProcessWebServiceBean?wsdl").getTxnProcessWebServiceSoapPort();
                    if (isCoreConnected()) {
                        ((CBNode) cBNode).setCounter(((CBNode) cBNode).getCounter() + 1);
                        initialize();
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ExtPayMain.bRLogger.logError("connectToDB()-ERROR", ex);
        }
    }

    private void checkCoreConnection() {
        if (ChannelUtil.SERVICE_OFFLINE) {
            initializeChannel();
        } else {
            connectToCore();
        }
    }

    private boolean isCoreConnected() {
        return ChannelUtil.SERVICE_OFFLINE ? (postChannel != null) : accountWebService != null;
    }

    private void initialize() {
        TXRequest tXRequest = new TXRequest();
        tXRequest.setAccessCode(BRController.ChannelCode);
    }

    private XAPIRequestBaseObject getBaseRequest(XAPIRequestBaseObject requestData, TXRequest tXRequest) {
        checkCoreConnection();

        requestData.setChannelId(BRController.ChannelID);
        requestData.setChannelCode(BRController.ChannelCode);

        requestData.setCardNumber(tXRequest.getAccessCode());
        requestData.setTransmissionTime(System.currentTimeMillis());

        requestData.setOriginatorUserId(BRController.SystemUserID);
        requestData.setTerminalNumber(BRController.ChannelCode);

        requestData.setReference(tXRequest.getReference());
        return requestData;
    }

    public Object queryDepositAccountBalance(TXUtility utils, TXRequest tXRequest) {
        try {

            AccountBalanceRequest accountBalanceRequest = (AccountBalanceRequest) getBaseRequest(new AccountBalanceRequest(), tXRequest);

            accountBalanceRequest.setAccountNumber(tXRequest.getDebitAccount());
            accountBalanceRequest.setChannelId(tXRequest.getChannelId());

            accountBalanceRequest.setChannelCode(tXRequest.getChannelCode());
            accountBalanceRequest.setOriginatorUserId(-99L);

            accountBalanceRequest.setReference(tXRequest.getReference());
            accountBalanceRequest.setTransmissionTime(System.currentTimeMillis());

            xAPICaller.setCall("acctbalreq", convertToString(accountBalanceRequest));

            AccountBalanceOutputData response;
            if (ChannelUtil.SERVICE_OFFLINE) {
                response = new AccountBalanceOutputData();
                forwardTxnToAdaptor(accountBalanceRequest, response, null);
            } else {
                response = accountWebService.findAccountBalance(accountBalanceRequest);
            }
            xAPICaller.setCall("acctbalres", convertToString(response));
            return response;
        } catch (Exception ex) {
            System.err.println("" + ex);
            return ex;
        }
    }

    public Object queryDepositAccountMinistatement(TXUtility utils, TXRequest tXRequest) {
        try {
            TransactionInquiryRequest inquiryRequest = new TransactionInquiryRequest();
            inquiryRequest = (TransactionInquiryRequest) getBaseRequest(inquiryRequest, tXRequest);
            inquiryRequest.setAccountNumber(tXRequest.getDebitAccount());
            xAPICaller.setCall("stmenqreq", convertToString(inquiryRequest));
            ArrayOfDepositTxnOutputData_Literal statTxns;
            if (ChannelUtil.SERVICE_OFFLINE) {
                statTxns = new ArrayOfDepositTxnOutputData_Literal();
                statTxns.setDepositTxnOutputData(utils.retrieveAccountStatement(tXRequest.getDebitAccount()));
            } else {
                statTxns = transactionsWebService.findDepositMiniStatement(inquiryRequest);
                for (DepositTxnOutputData statTxn : statTxns.getDepositTxnOutputData()) {
                    xAPICaller.setCall("stmenqtxn", statTxn.getTxnDescription() + "\t" + statTxn.getDrcrFlag() + "\t"
                            + statTxn.getTxnCcyISOCode() + "\t" + statTxn.getTxnAmount() + "\t" + statTxn.getTxnJournalId() + "\t"
                            + statTxn.getTxnDate());
                }
            }
            return statTxns;
        } catch (Exception ex) {
            System.err.println("" + ex);
            return ex;
        }
    }

    public Object postDpToDpFundsTransfer(TXRequest tXRequest) {
        try {

            checkCoreConnection();
            FundsTransferRequestData transferRequest = new FundsTransferRequestData();
            transferRequest = (FundsTransferRequestData) getBaseRequest(transferRequest, tXRequest);

            transferRequest.setChannelId(tXRequest.getChannelId());
            transferRequest.setChannelCode(tXRequest.getChannelCode());

            transferRequest.setOriginatorUserId(-99L);
            transferRequest.setCurrBUId(-88L);

            transferRequest.setTransmissionTime(System.currentTimeMillis());
            transferRequest.setFromAccountNumber(tXRequest.getAccountNumber1());

            transferRequest.setToAccountNumber(tXRequest.getAccountNumber2());
            transferRequest.setFromCurrencyCode(tXRequest.getCurrencyCode());

            transferRequest.setToCurrencyCode(tXRequest.getCurrencyCode());
            transferRequest.setTxnDescription(tXRequest.getTxnNarration());

            transferRequest.setTransactionAmount(tXRequest.getTxnAmount());
            transferRequest.setAcquiringInstitutionCode("");

            transferRequest.setCardNumber(tXRequest.getAccessCode());
            transferRequest.setForwardingInstitutionCode("");

            transferRequest.setTrack2Data(tXRequest.getReference());
            transferRequest.setUserId(-99L);

            transferRequest.setRetrievalReferenceNumber(tXRequest.getReference());
            transferRequest.setReference(tXRequest.getReference());

            transferRequest.setUserRoleId(-99L);
            transferRequest.setUserLoginId("SYSTEM");
            transferRequest.setCurrBUId(tXRequest.getCurrentBu());

            Calendar calendar = Calendar.getInstance();
            transferRequest.setLocalTransactionTime(calendar);

            xAPICaller.setCall("fundstfrreq", convertToString(transferRequest));

            FundsTransferOutputData response;
            if (ChannelUtil.SERVICE_OFFLINE) {
                response = new FundsTransferOutputData();
                response.setFromAccountNumber(transferRequest.getFromAccountNumber());
                response.setToAccountNumber(transferRequest.getToAccountNumber());
                response.setTransactionAmount(transferRequest.getTransactionAmount());
                forwardTxnToAdaptor(transferRequest, response, "dp2dp");
            } else {
                response = transferWebService.internalDepositAccountTransfer(transferRequest);
            }
            xAPICaller.setCall("fundstfrres", convertToString(response));
            return response;
        } catch (Exception ex) {

            return ex;
        }
    }

    public Object postDepositToGLTransfer(TXRequest tXRequest) {
        try {

            checkCoreConnection();
            XAPIBaseTxnRequestData requestData = new XAPIBaseTxnRequestData();
            requestData = (XAPIBaseTxnRequestData) getBaseRequest(requestData, tXRequest);

            requestData.setAcctNo(tXRequest.getDebitAccount());
            requestData.setContraAcctNo(tXRequest.getCreditAccount());

            requestData.setTxnDescription(tXRequest.getTxnNarration());
            requestData.setTxnAmount(tXRequest.getTxnAmount());

            requestData.setTxnCurrencyCode(tXRequest.getCurrencyCode());
            requestData.setTxnReference(tXRequest.getReference());

            requestData.setUserRoleId(-99L);
            requestData.setUserLoginId("SYSTEM");
            requestData.setCurrBUId(tXRequest.getCurrentBu());

            xAPICaller.setCall("dptogltfrreq", convertToString(requestData));

            TxnResponseOutputData response;
            if (ChannelUtil.SERVICE_OFFLINE) {
                response = new TxnResponseOutputData();
                response.setPrimaryAccountNumber(requestData.getAcctNo());
                response.setRetrievalReferenceNumber("");
                response.setTransactionAmount(requestData.getTxnAmount());
                response.setTransactionCurrencyCode(requestData.getTxnCurrencyCode());
                forwardTxnToAdaptor(requestData, response, "dp2gl");
            } else {
                response = processWebService.postDepositToGLAccountTransfer(requestData);
            }

            xAPICaller.setCall("dptogltfrres", convertToString(response));

            return response;
        } catch (Exception ex) {
            System.err.println("" + ex);
            return ex;
        }
    }

    public Object postGLToDepositTransfer(TXRequest tXRequest) {
        try {

            checkCoreConnection();
            XAPIBaseTxnRequestData requestData = new XAPIBaseTxnRequestData();
            requestData = (XAPIBaseTxnRequestData) getBaseRequest(requestData, tXRequest);

            requestData.setChannelId(tXRequest.getChannelId());
            requestData.setChannelCode("POS");// to replace            

            requestData.setOriginatorUserId(-99L);
            requestData.setUserId(-99L);

            requestData.setTransmissionTime(System.currentTimeMillis());
            requestData.setAcctNo(tXRequest.getAccountNumber2());

            requestData.setContraAcctNo(tXRequest.getAccountNumber1());
            requestData.setTxnDescription(tXRequest.getTxnNarration());

            requestData.setTxnAmount(tXRequest.getTxnAmount());
            requestData.setTxnCurrencyCode(tXRequest.getCurrencyCode());

            requestData.setTxnReference(tXRequest.getReference());
            requestData.setValueDate(tXRequest.getTxnDate());

            requestData.setUserRoleId(-99L);
            requestData.setUserLoginId("SYSTEM");

            requestData.setCurrBUId(tXRequest.getCurrentBu());
            xAPICaller.setCall("gltodptfrreq", convertToString(requestData));

            TxnResponseOutputData response;
            if (ChannelUtil.SERVICE_OFFLINE) {
                response = new TxnResponseOutputData();
                response.setPrimaryAccountNumber(requestData.getAcctNo());
                response.setRetrievalReferenceNumber("");
                response.setTransactionAmount(requestData.getTxnAmount());
                response.setTransactionCurrencyCode(requestData.getTxnCurrencyCode());
                forwardTxnToAdaptor(requestData, response, "gl2dp");
            } else {
                response = processWebService.postGLToDepositAccountTransfer(requestData);
            }

            xAPICaller.setCall("gltodptfrres", convertToString(response));
            return response;
        } catch (Exception ex) {
            return ex;
        }
    }

    public Object processCharge(TXRequest tXRequest) {
        boolean charged = tXRequest.getChargeAmount() == null || BigDecimal.ZERO.compareTo(tXRequest.getChargeAmount()) >= 0;
        try {
            if (!charged) {

                XAPIBaseTxnRequestData requestData = new XAPIBaseTxnRequestData();
                requestData = (XAPIBaseTxnRequestData) getBaseRequest(requestData, tXRequest);

                requestData.setChannelId(tXRequest.getChannelId());
                requestData.setChannelCode(tXRequest.getChannelCode());

                requestData.setOriginatorUserId(-99L);
                requestData.setTransmissionTime(System.currentTimeMillis());

                requestData.setAcctNo(tXRequest.getDebitAccount());//100207000014
                requestData.setContraAcctNo(tXRequest.getChargeCreditLedger());

                requestData.setTxnDescription(tXRequest.getChargeNarration());
                requestData.setTxnAmount(tXRequest.getChargeAmount());

                requestData.setTxnCurrencyCode(tXRequest.getCurrencyCode());
                requestData.setTxnReference(tXRequest.getReference());

                requestData.setUserRoleId(-99L);
                requestData.setUserLoginId("SYSTEM");

                requestData.setCurrBUId(tXRequest.getCurrentBu());
                xAPICaller.setCall("txnchrgreq", convertToString(requestData));
                TxnResponseOutputData response;

                if (ChannelUtil.SERVICE_OFFLINE) {
                    response = new TxnResponseOutputData();
                    response.setPrimaryAccountNumber(requestData.getAcctNo());
                    response.setRetrievalReferenceNumber("");
                    response.setTransactionAmount(requestData.getTxnAmount());
                    response.setTransactionCurrencyCode(requestData.getTxnCurrencyCode());
                    forwardTxnToAdaptor(requestData, response, "dp2gl");
                } else {
                    response = processWebService.postDepositToGLAccountTransfer(requestData);
                }

                xAPICaller.setCall("txnchrgres", convertToString(response));
                xAPICaller.setXapiRespCode(response.getResponseCode());

                if ("00".equals(response.getResponseCode())) {
                    processExciseDuty(tXRequest);
                    charged = true;
                }

                return response;
            } else {
                return "91";
            }
        } catch (Exception ex) {
            return ex;
        }

    }

    public boolean processChannelCharge(TXRequest tXRequest) {
        boolean resp = false;
        boolean charged = tXRequest.getChargeAmount() == null || BigDecimal.ZERO.compareTo(tXRequest.getChargeAmount()) >= 0;
        try {
            if (!charged) {

                XAPIBaseTxnRequestData requestData = new XAPIBaseTxnRequestData();
                requestData = (XAPIBaseTxnRequestData) getBaseRequest(requestData, tXRequest);

                requestData.setChannelId(tXRequest.getChannelId());
                requestData.setChannelCode(tXRequest.getChannelCode());

                requestData.setOriginatorUserId(-99L);
                requestData.setTransmissionTime(System.currentTimeMillis());

                requestData.setAcctNo(tXRequest.getDebitAccount());//100207000014
                requestData.setContraAcctNo(tXRequest.getChargeCreditLedger());

                requestData.setTxnDescription(tXRequest.getChargeNarration());
                requestData.setTxnAmount(tXRequest.getChargeAmount());

                requestData.setTxnCurrencyCode(tXRequest.getCurrencyCode());
                requestData.setTxnReference(tXRequest.getReference());

                requestData.setUserRoleId(-99L);
                requestData.setUserLoginId("SYSTEM");

                requestData.setCurrBUId(tXRequest.getCurrentBu());
                xAPICaller.setCall("txnchrgreq", convertToString(requestData));
                TxnResponseOutputData response;

                if (ChannelUtil.SERVICE_OFFLINE) {
                    response = new TxnResponseOutputData();
                    response.setPrimaryAccountNumber(requestData.getAcctNo());
                    response.setRetrievalReferenceNumber(requestData.getTxnReference());
                    response.setTransactionAmount(requestData.getTxnAmount());
                    response.setTransactionCurrencyCode(requestData.getTxnCurrencyCode());
                    forwardTxnToAdaptor(requestData, response, "dp2gl");
                } else {
                    response = processWebService.postDepositToGLAccountTransfer(requestData);
                }

                xAPICaller.setCall("txnchrgres", convertToString(response));
                xAPICaller.setXapiRespCode(response.getResponseCode());
                if ("00".equals(response.getResponseCode())) {
                    processExciseDuty(tXRequest);
                    resp = true;
                }

            } else {
                return false;
            }
        } catch (Exception ex) {
            xAPICaller.setXapiRespCode(EICodes.SYSTEM_ERROR);
            xAPICaller.logException(ex);
            if (ex instanceof XAPIException) {
                xAPICaller.setXapiRespCode(((XAPIException) ex).getErrorCode());
            }
        }
        return resp;
    }

    public boolean processBillerCharge(TXRequest tXRequest) {
        boolean resp = false;
        boolean charged = tXRequest.getBillerchargeFlatAmt() == null || BigDecimal.ZERO.compareTo(tXRequest.getBillerchargeFlatAmt()) >= 0;
        try {
            if (!charged) {
                XAPIBaseTxnRequestData requestData = new XAPIBaseTxnRequestData();
                requestData = (XAPIBaseTxnRequestData) getBaseRequest(requestData, tXRequest);

                requestData.setChannelId(tXRequest.getChannelId());
                requestData.setChannelCode(tXRequest.getChannelCode());

                requestData.setOriginatorUserId(-99L);
                requestData.setTransmissionTime(System.currentTimeMillis());

                requestData.setAcctNo(tXRequest.getAccountNumber2());//100207000014
                requestData.setContraAcctNo(tXRequest.getBillerincomeGl());

                requestData.setTxnDescription(tXRequest.getBillerchargeDesc());
                requestData.setTxnAmount(tXRequest.getBillerchargeFlatAmt().add(tXRequest.getBillerchargeFlatAmt().multiply(tXRequest.getBillerTaxRate().divide(new BigDecimal(100)))));

                requestData.setTxnCurrencyCode(tXRequest.getCurrencyCode());
                requestData.setTxnReference(tXRequest.getReference());

                requestData.setUserRoleId(-99L);
                requestData.setUserLoginId("SYSTEM");

                requestData.setCurrBUId(tXRequest.getCurrentBu());
                xAPICaller.setCall("billerchrgreq", convertToString(requestData));
                TxnResponseOutputData response;

                if (ChannelUtil.SERVICE_OFFLINE) {
                    response = new TxnResponseOutputData();
                    response.setPrimaryAccountNumber(requestData.getAcctNo());
                    response.setRetrievalReferenceNumber(requestData.getTxnReference());
                    response.setTransactionAmount(requestData.getTxnAmount());
                    response.setTransactionCurrencyCode(requestData.getTxnCurrencyCode());
                    forwardTxnToAdaptor(requestData, response, "dp2gl");
                } else {
                    response = processWebService.postDepositToGLAccountTransfer(requestData);
                }

                xAPICaller.setCall("billerchrgres", convertToString(response));

                xAPICaller.setXapiRespCode(response.getResponseCode());

                if ((response.getResponseCode().equals("00"))) {
                    if (!ChannelUtil.SERVICE_OFFLINE) {
                        xAPICaller.setChargeId(getTagValue((((TxnResponseOutputData) response).getRetrievalReferenceNumber()), "TxnId"));
                    }
                    processBillerExciseDuty(tXRequest);
                    resp = true;
                }
            } else {
                return false;
            }
        } catch (Exception ex) {
            xAPICaller.setXapiRespCode(EICodes.SYSTEM_ERROR);
            xAPICaller.logException(ex);
            if (ex instanceof XAPIException) {
                xAPICaller.setXapiRespCode(((XAPIException) ex).getErrorCode());
            }
        }
        return resp;
    }

    public void processBillerExciseDuty(TXRequest tXRequest) {
        if (BigDecimal.ZERO.compareTo(tXRequest.getBillerchargeFlatAmt()) < 0) {
            try {
                GLTransferRequest glTransferRequest = (GLTransferRequest) getBaseRequest(new GLTransferRequest(), tXRequest);
                glTransferRequest.setFromGLAccountNumber(tXRequest.getBillerincomeGl());

                glTransferRequest.setToGLAccountNumber(tXRequest.getBillerTaxGl());
                glTransferRequest.setChannelId(tXRequest.getChannelId());

                glTransferRequest.setChannelCode(tXRequest.getChannelCode());
                glTransferRequest.setOriginatorUserId(-99L);

                glTransferRequest.setTransmissionTime(System.currentTimeMillis());
                glTransferRequest.setTransactionAmount(tXRequest.getBillerchargeFlatAmt().multiply(tXRequest.getBillerTaxRate().divide(new BigDecimal(100)))); //tXRequest.getBillerTaxRate()

                glTransferRequest.setTransactionCurrencyCode(tXRequest.getCurrencyCode());
                glTransferRequest.setReference(tXRequest.getReference());

                glTransferRequest.setTxnDescription(tXRequest.getBillerTaxDesc());
                glTransferRequest.setUserRoleId(-99L);

                glTransferRequest.setUserLoginId("SYSTEM");
                glTransferRequest.setCurrBUId(tXRequest.getCurrentBu());

                xAPICaller.setCall("billchrgtaxreq", convertToString(glTransferRequest));
                GLTransferOutputData resp;

                if (ChannelUtil.SERVICE_OFFLINE) {
                    resp = new GLTransferOutputData();
                    forwardTxnToAdaptor(glTransferRequest, resp, "gl2gl");
                } else {
                    resp = transactionsWebService.postGLtoGLXfer(glTransferRequest);
                }

                xAPICaller.setCall("billchrgtaxres", convertToString(resp));

            } catch (Exception ex) {
                System.err.println("" + ex);
            }
        }
    }

    public void processExciseDuty(TXRequest tXRequest) {
        if (BigDecimal.ZERO.compareTo(tXRequest.getTaxAmount()) < 0) {
            try {
                GLTransferRequest glTransferRequest = (GLTransferRequest) getBaseRequest(new GLTransferRequest(), tXRequest);
                glTransferRequest.setFromGLAccountNumber(tXRequest.getChargeCreditLedger());

                glTransferRequest.setToGLAccountNumber(tXRequest.getTaxCreditLedger());
                glTransferRequest.setChannelId(tXRequest.getChannelId());

                glTransferRequest.setChannelCode(tXRequest.getChannelCode());
                glTransferRequest.setOriginatorUserId(-99L);

                glTransferRequest.setTransmissionTime(System.currentTimeMillis());
                glTransferRequest.setTransactionAmount(tXRequest.getTaxAmount());

                glTransferRequest.setTransactionCurrencyCode(tXRequest.getCurrencyCode());
                glTransferRequest.setReference(tXRequest.getReference());

                glTransferRequest.setTxnDescription(tXRequest.getTaxNarration());
                glTransferRequest.setUserRoleId(-99L);

                glTransferRequest.setUserLoginId("SYSTEM");
                glTransferRequest.setCurrBUId(tXRequest.getCurrentBu());

                xAPICaller.setCall("chrgtaxreq", convertToString(glTransferRequest));
                GLTransferOutputData resp;

                if (ChannelUtil.SERVICE_OFFLINE) {
                    resp = new GLTransferOutputData();
                    forwardTxnToAdaptor(glTransferRequest, resp, "gl2gl");
                } else {
                    resp = transactionsWebService.postGLtoGLXfer(glTransferRequest);
                }

                xAPICaller.setCall("chrgtaxres", convertToString(resp));

            } catch (Exception ex) {
                System.err.println("" + ex);
            }
        }
    }

    public Object postGltoGl(TXRequest tXRequest) {

        try {
            GLTransferRequest glTransferRequest = (GLTransferRequest) getBaseRequest(new GLTransferRequest(), tXRequest);
            glTransferRequest.setFromGLAccountNumber(tXRequest.getChargeCreditLedger());

            glTransferRequest.setToGLAccountNumber(tXRequest.getTaxCreditLedger());
            glTransferRequest.setChannelId(tXRequest.getChannelId());

            glTransferRequest.setChannelCode(tXRequest.getChannelCode());
            glTransferRequest.setOriginatorUserId(-99L);

            glTransferRequest.setTransmissionTime(System.currentTimeMillis());
            glTransferRequest.setTransactionAmount(BigDecimal.TEN);

            glTransferRequest.setTransactionCurrencyCode(tXRequest.getCurrencyCode());
            glTransferRequest.setReference(tXRequest.getReference());

            glTransferRequest.setTxnDescription(tXRequest.getTaxNarration());
            glTransferRequest.setUserRoleId(-99L);

            glTransferRequest.setUserLoginId("SYSTEM");
            glTransferRequest.setCurrBUId(tXRequest.getCurrentBu());

            xAPICaller.setCall("gltoglreq", convertToString(glTransferRequest));
            GLTransferOutputData resp;

            if (ChannelUtil.SERVICE_OFFLINE) {
                resp = new GLTransferOutputData();
                forwardTxnToAdaptor(glTransferRequest, resp, "gl2gl");
            } else {
                resp = transactionsWebService.postGLtoGLXfer(glTransferRequest);
            }

            xAPICaller.setCall("gltoglres", convertToString(resp));
            return resp;

        } catch (Exception ex) {
            System.err.println("" + ex);
            return ex;
        }
    }

    public String getXapiErrorCode(Exception ex) {
        //String errorCode = EICodes.SYSTEM_ERROR;
        System.err.println("error message " + getTagValue(String.valueOf(ex), "error-code"));
        String errorCode = SYSTEM_ERROR;
        if (ex instanceof XAPIException) {
            System.out.println("exception is an instance ");
            if (((XAPIException) ex).getErrors() != null && "91".equals(errorCode)) {
                errorCode = ((XAPIException) ex).getErrors().length >= 1 ? ((XAPIException) ex).getErrors()[0].getErrorCode() : errorCode;
            }
        }
        return errorCode;
    }

    public static String getTagValue(String xml, String tagName) {
        return xml.split("<" + tagName + ">")[1].split("</" + tagName + ">")[0];
    }

//    private boolean isReversal() {
//        return gettXProcessor().getXapiCaller().isIsReversal();
//    }
    /**
     * @param procCode
     * @param txnAmt
     * @param fromLedger
     * @param mti
     * @param txnDesc
     * @param toLedger
     * @param txnType
     * @param curCode
     * @param reference
     * @return the tXProcessor
     */
    public ISOMsg buildISOMsg(String procCode, String txnAmt, String fromLedger, String toLedger, String txnDesc, String mti,
            String txnType, String curCode, String reference) {

        ISOMsg iso = new ISOMsg(mti);
        try {
            if (procCode != null) {
                iso.set(3, procCode);
            }
            iso.set(4, txnAmt);
            iso.set(7, ISODate.getDateTime(new Date()));
            iso.set(11, BRController.stan());
            if (reference != null) {
                iso.set(43, reference.trim().length() <= 76 ? reference : reference.substring(0, 76));
            }
            if (txnType != null) {
                iso.set(94, txnType);
            }
            if (txnDesc != null) {
                iso.set(45, txnDesc.trim().length() <= 76 ? txnDesc : txnDesc.substring(0, 76));
            }
            iso.set(49, curCode);
            iso.set(102, fromLedger);
            iso.set(103, toLedger);

        } catch (ISOException ex) {
            System.err.println("" + ex);
        }
        return iso;
    }

    private void initializeChannel() {
        if (postChannel == null || !postChannel.isConnected()) {
            postChannel = BRController.getOfflineChannel();
        }
    }

    private void forwardTxnToAdaptor(Object requestData, Object response, String txnType) {
        try {
            initializeChannel();
            if (requestData instanceof AccountBalanceRequest) {
                postChannel.connect();
                if (postChannel.isConnected()) {
                    postChannel.send(buildISOMsg("310000", tdClient.formatIsoAmount(BigDecimal.ZERO),
                            ((AccountBalanceRequest) requestData).getAccountNumber(),
                            null, "Account balance enquiry", "0200", txnType, null, ((AccountBalanceRequest) requestData).getReference()));
                    ISOMsg responseMsg = postChannel.receive();
                    if (responseMsg != null) {
                        if ("00".equalsIgnoreCase(responseMsg.getString(39))) {
                            ((AccountBalanceOutputData) response).setAvailableBalance(tdClient.extractISOAmount(responseMsg));
                        } else {
                            ((AccountBalanceOutputData) response).setAvailableBalance(BigDecimal.ZERO);
                        }
                    } else {
                        ((AccountBalanceOutputData) response).setAvailableBalance(BigDecimal.ZERO);
                    }
                } else {
                    ((AccountBalanceOutputData) response).setAvailableBalance(BigDecimal.ZERO);
                }
            } else if (requestData instanceof FundsTransferRequestData) {
                postChannel.connect();
                if (postChannel.isConnected()) {
                    postChannel.send(buildISOMsg("400000", tdClient.formatIsoAmount(((FundsTransferRequestData) requestData).getTransactionAmount()),
                            ((FundsTransferRequestData) requestData).getFromAccountNumber(),
                            ((FundsTransferRequestData) requestData).getToAccountNumber(),
                            ((FundsTransferRequestData) requestData).getTxnDescription(), "0200", txnType,
                            ((FundsTransferRequestData) requestData).getFromCurrencyCode(), ((FundsTransferRequestData) requestData).getReference()));
                    ISOMsg responseMsg = postChannel.receive();
                    if (responseMsg != null) {
                        if ("00".equalsIgnoreCase(responseMsg.getString(39))) {
                            ((FundsTransferOutputData) response).setResponseCode("00");
                        } else {
                            ((FundsTransferOutputData) response).setResponseCode(responseMsg.getString(42));
                        }
                    } else {
                        ((FundsTransferOutputData) response).setResponseCode(EICodes.SYSTEM_ERROR);
                    }
                } else {
                    ((FundsTransferOutputData) response).setResponseCode(EICodes.XAPI_CONNECTION_TERMINATED);
                }
            } else if (requestData instanceof XAPIBaseTxnRequestData) {
                postChannel.connect();
                if (postChannel.isConnected()) {
                    postChannel.send(buildISOMsg("500000", tdClient.formatIsoAmount(((XAPIBaseTxnRequestData) requestData).getTxnAmount()),
                            ((XAPIBaseTxnRequestData) requestData).getAcctNo(),
                            ((XAPIBaseTxnRequestData) requestData).getContraAcctNo(),
                            ((XAPIBaseTxnRequestData) requestData).getTxnDescription(), "0200", txnType,
                            ((XAPIBaseTxnRequestData) requestData).getTxnCurrencyCode(), ((XAPIBaseTxnRequestData) requestData).getReference()));
                    ISOMsg responseMsg = postChannel.receive();
                    if (responseMsg != null) {
                        if ("00".equalsIgnoreCase(responseMsg.getString(39))) {
                            ((TxnResponseOutputData) response).setResponseCode("00");
                        } else {
                            ((TxnResponseOutputData) response).setResponseCode(responseMsg.getString(42));
                        }
                    } else {
                        ((TxnResponseOutputData) response).setResponseCode(EICodes.SYSTEM_ERROR);
                    }
                } else {
                    ((TxnResponseOutputData) response).setResponseCode(EICodes.XAPI_CONNECTION_TERMINATED);
                }
            } else if (requestData instanceof GLTransferRequest) {
                postChannel.connect();
                if (postChannel.isConnected()) {
                    postChannel.send(buildISOMsg("600000", tdClient.formatIsoAmount(((GLTransferRequest) requestData).getTransactionAmount()),
                            ((GLTransferRequest) requestData).getFromGLAccountNumber(),
                            ((GLTransferRequest) requestData).getToGLAccountNumber(),
                            ((GLTransferRequest) requestData).getTxnDescription(), "0200", txnType,
                            ((GLTransferRequest) requestData).getTransactionCurrencyCode(), ((GLTransferRequest) requestData).getReference()));
                    ISOMsg responseMsg = postChannel.receive();
                    if (responseMsg != null) {
                        if ("00".equalsIgnoreCase(responseMsg.getString(39))) {
                            ((GLTransferOutputData) response).setResponseCode("00");
                        } else {
                            ((GLTransferOutputData) response).setResponseCode(responseMsg.getString(42));
                        }
                    } else {
                        ((GLTransferOutputData) response).setResponseCode(EICodes.SYSTEM_ERROR);
                    }
                } else {
                    ((GLTransferOutputData) response).setResponseCode(EICodes.XAPI_CONNECTION_TERMINATED);
                }
            }
        } catch (IOException | ISOException ex) {
            System.err.println("" + ex);
        } finally {
            disconnectFromHost();
        }
    }

    private final TDUtility tdClient = new TDUtility();
    private ISOChannel postChannel;

    private void disconnectFromHost() {
        try {
            if (postChannel != null && postChannel.isConnected()) {
                postChannel.disconnect();
            }
        } catch (IOException ex) {
            System.err.println("" + ex);
        }
    }

}
