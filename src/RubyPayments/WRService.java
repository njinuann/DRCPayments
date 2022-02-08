/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RubyPayments;

import RubyPaymentWS.PMTService;
import com.sun.net.httpserver.HttpServer;
//import com.sun.xml.internal.ws.spi.ProviderImpl;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.Endpoint;

/**
 *
 * @author Pecherk
 */
public class WRService {

    HttpServer httpServer;
    Endpoint endpoint = null;
    public void startSoap() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(3950), 100);
            httpServer.setExecutor(new ThreadPoolExecutor(2, 100, 2000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(100)));
            new com.sun.xml.ws.spi.ProviderImpl().createEndpoint(null, new PMTService()).publish(httpServer.createContext("/ruby/PMTService"));
            httpServer.start();
           
        } catch (IOException ex) {
            Logger.getLogger(WRService.class.getName()).log(Level.SEVERE, null, ex);            
        }
    }

    public void stop() throws Exception {
        if (endpoint != null) {
            endpoint.stop();
        }
        httpServer.stop(5);
    }
}
