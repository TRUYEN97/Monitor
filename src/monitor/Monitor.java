/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package monitor;

import Control.Core;
import java.io.IOException;

/**
 *
 * @author Administrator
 */
public class Monitor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new Core().run();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
