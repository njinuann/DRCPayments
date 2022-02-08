/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPaymentWS;

import RubyPayments.BaseResponse;
import RubyPayments.ExtPayMain;
import RubyPayments.SchFeesQueryRequest;
import RubyPayments.TXUtility;
import RubyPayments.TaxQueryRequest;
import RubyPayments.TvQueryRequest;
import com.sun.net.httpserver.HttpExchange;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author NJINU
 */
@WebService(serviceName = "extPayment")
public class ExtPayment {

    HttpExchange httpExchange;
    TXUtility txutil = new TXUtility();
    PmtQuerryReq prequest = new PmtQuerryReq();

    @WebMethod(operationName = "querryTvCustomerDetails")
    public BaseResponse tvQueryRequest(@XmlElement(required = true) @WebParam(name = "tvQueryRequest") TvQueryRequest paymentRequest) {
        paymentRequest.setChannelId(9L);
        try {
            return txutil.queryTVPayment(paymentRequest, false);
        } catch (Exception x) {
            ExtPayMain.bRLogger.logError("ERROR", x);
            throw new IllegalStateException(x);
        }
    }

    @WebMethod(operationName = "processTvPayment")
    public BaseResponse processTvPayment(@XmlElement(required = true) @WebParam(name = "tvPaymentRequest") TVPaymentRequest paymentRequest) {
        try {
            return txutil.TVPayment(paymentRequest);
        } catch (Exception x) {
            ExtPayMain.bRLogger.logError("ERROR", x);
            throw new IllegalStateException(x);
        }
    }

    @WebMethod(operationName = "querrySchFeesDetails")
    public BaseResponse schFeesQueryRequest(@XmlElement(required = true) @WebParam(name = "schFeesQueryRequest") SchFeesQueryRequest paymentRequest) {
        try {
            return txutil.queryFeePayment(paymentRequest);
        } catch (Exception x) {
            ExtPayMain.bRLogger.logError("ERROR", x);
            throw new IllegalStateException(x);
        }
    }

    @WebMethod(operationName = "processSchFeesPayment")
    public BaseResponse processSchFeesPayment(@XmlElement(required = true) @WebParam(name = "schFeePmtRequest") SchFeePmtRequest paymentRequest) {
        try {
            return txutil.feePayment(paymentRequest);
        } catch (Exception x) {
            ExtPayMain.bRLogger.logError("ERROR", x);
            throw new IllegalStateException(x);
        }
    }

    @WebMethod(operationName = "querryTaxDetails")
    public BaseResponse taxQueryRequest(@XmlElement(required = true) @WebParam(name = "taxQueryRequest") TaxQueryRequest paymentRequest) {

        try {
            return txutil.queryTaxPayment(paymentRequest, false);
        } catch (Exception x) {
            ExtPayMain.bRLogger.logError("ERROR", x);
            throw new IllegalStateException(x);

        }
    }

    @WebMethod(operationName = "processTaxPayment")
    public BaseResponse processTaxPayment(@XmlElement(required = true) @WebParam(name = "taxPaymentRequest") TaxPaymentRequest txpaymentRequest) {
        try {
            return txutil.taxPayment(txpaymentRequest);
        } catch (Exception x) {
            ExtPayMain.bRLogger.logError("ERROR", new IllegalStateException(x));
            throw new IllegalStateException(x);
        }
    }

//    public BaseResponse customerProfMob(@WebParam(name = "mobileNo") String mobileNo) {
//
//        try {
//            return txutil.queryCustProfileMOB(mobileNo);
//        } catch (Exception x) {
//            throw new IllegalStateException(x);
//
//        }
//    }
//
//    public BaseResponse customerProfIdNum(@WebParam(name = "idNumber") String idNumber) {
//
//        try {
//            return txutil.queryCustProfId(idNumber);
//        } catch (Exception x) {
//            throw new IllegalStateException(x);
//
//        }
//    }
}
