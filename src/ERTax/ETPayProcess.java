/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERTax;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author NJINU
 */
public class ETPayProcess implements Runnable {

    ERTPayment ERTprocess = new ERTPayment();

    @Override
    public void run() {
        while (true) {
            ERTprocess.checkPayment();
            try {
                Thread.sleep(12000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ETPayProcess.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
