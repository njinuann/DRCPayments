/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPaymentWS;

import RubyPayments.BRController;
import RubyPayments.ExtPayMain;
import RubyPayments.SchFeesQueryRequest;
import RubyPayments.TXUtility;
import RubyPayments.TaxQueryRequest;
import RubyPayments.TvQueryRequest;
import RubyPayments.SchFeesResponse;
import RubyPayments.TaxResponse;
import RubyPayments.TvResponse;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author NJINU
 */
@WebService(serviceName = "PMTService")
public class PMTService
{

    BRController brControler = new BRController();

    /**
     * Web service operation
     *
     * @param SchFeesQueryRequest
     * @return
     */
    @WebMethod(operationName = "querySchFeesDetails")
    public SchFeesResponse querySchFeesDetails(@WebParam(name = "querySchFeesDetails") SchFeesQueryRequest SchFeesQueryRequest)
    {
        SchFeesResponse feesResponse = new SchFeesResponse();
        TXUtility txutil = new TXUtility();
        ExtPayMain.brMeter.setValue(-400, "FQ");
        try
        {
            feesResponse = txutil.queryFeePayment(SchFeesQueryRequest);
        }
        catch (Exception x)
        {
            ExtPayMain.bRLogger.logError("ERROR", x);
            throw new IllegalStateException(x);

        }
        ExtPayMain.brMeter.setValue(400, "FQ");
        return feesResponse;
    }

    /**
     * Web service operation
     *
     * @param processSchFeesPayment
     * @return
     */
    @WebMethod(operationName = "processSchFeesPayment")
    public SchFeesResponse processSchFeesPayment(@WebParam(name = "processSchFeesPayment") SchFeePmtRequest processSchFeesPayment)
    {
        SchFeesResponse feesResponse = new SchFeesResponse();
        TXUtility txutil = new TXUtility();
        ExtPayMain.brMeter.setValue(-400, "FP");
        try
        {
            feesResponse = txutil.feePayment(processSchFeesPayment);
        }
        catch (Exception x)
        {
            ExtPayMain.bRLogger.logError("ERROR", x);
            throw new IllegalStateException(x);

        }
        ExtPayMain.brMeter.setValue(400, "FP");
        return feesResponse;
    }

    /**
     * Web service operation
     *
     * @param processTaxPayment
     * @return
     */
    @WebMethod(operationName = "processTaxPayment")
    public TaxResponse processTaxPayment(@WebParam(name = "processTaxPayment") TaxPaymentRequest processTaxPayment)
    {
        TaxResponse taxResponse = new TaxResponse();
        TXUtility txutil = new TXUtility();
        ExtPayMain.brMeter.setValue(-400, "XQ");
        try
        {
            taxResponse = txutil.taxPayment(processTaxPayment);
        }
        catch (Exception x)
        {
            ExtPayMain.bRLogger.logError("ERROR", x);
            throw new IllegalStateException(x);

        }
        ExtPayMain.brMeter.setValue(400, "XQ");
        return taxResponse;
    }

    /**
     * Web service operation
     *
     * @param queryTaxDetails
     * @return
     */
    @WebMethod(operationName = "queryTaxDetails")
    public TaxResponse queryTaxDetails(@WebParam(name = "queryTaxDetails") TaxQueryRequest queryTaxDetails)
    {
        TaxResponse taxResponse = new TaxResponse();
        TXUtility txutil = new TXUtility();
        ExtPayMain.brMeter.setValue(-400, "XP");
        try
        {
            taxResponse = txutil.queryTaxPayment(queryTaxDetails, true);
        }
        catch (Exception x)
        {
            ExtPayMain.bRLogger.logError("ERROR", x);
            throw new IllegalStateException(x);

        }
        ExtPayMain.brMeter.setValue(400, "XP");
        return taxResponse;
    }

    /**
     * Web service operation
     *
     * @param processTvPayment
     * @return
     */
    @WebMethod(operationName = "processTvPayment")
    public TvResponse processTvPayment(@WebParam(name = "processTvPayment") TVPaymentRequest processTvPayment)
    {
        TvResponse tvResponse = new TvResponse();
        TXUtility txutil = new TXUtility();
        ExtPayMain.brMeter.setValue(-400, "TQ");

        try
        {
            tvResponse = txutil.TVPayment(processTvPayment);
        }
        catch (Exception x)
        {
            ExtPayMain.bRLogger.logError("ERROR", x);
            throw new IllegalStateException(x);

        }
        ExtPayMain.brMeter.setValue(400, "TQ");
        return tvResponse;
    }

    /**
     * Web service operation
     *
     * @param queryTvDetails
     * @return
     */
    @WebMethod(operationName = "queryTvDetails")
    public TvResponse queryTvDetails(@WebParam(name = "queryTvDetails") TvQueryRequest queryTvDetails)
    {
        TvResponse tvResponse = new TvResponse();
        TXUtility txutil = new TXUtility();
        ExtPayMain.brMeter.setValue(-400, "TQ");
        try
        {
            tvResponse = txutil.queryTVPayment(queryTvDetails, true);
        }
        catch (Exception x)
        {
            ExtPayMain.bRLogger.logError("ERROR", x);
            throw new IllegalStateException(x);
        }
        ExtPayMain.brMeter.setValue(400, "TQ");
        return tvResponse;
    }

}
