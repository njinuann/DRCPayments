/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package StarTimesPayment;

/**
 *
 * @author NJINU
 */
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

public class HeaderHandlerResolver implements HandlerResolver {

    @Override
    public List<Handler> getHandlerChain(PortInfo portInfo) {
        List<Handler> handlerChain = new ArrayList<>();

        HeaderHandler hh = new HeaderHandler();

        handlerChain.add(hh);

        return handlerChain;
    }

}
