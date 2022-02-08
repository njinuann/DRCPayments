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
public class ValHolder {

    static StarttimesProcess starttimesProcess = new StarttimesProcess();

    public static void main(String[] args) {
        System.err.println("setejejeje ");
        try {
            starttimesProcess.StarTimesValidate("02113454546");
            System.err.println("out ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
