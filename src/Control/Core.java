/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import Model.PcInformation;
import Model.Setting;
import View.DisplayClient;
import java.io.IOException;

/**
 *
 * @author Administrator
 */
public class Core {

    private final DisplayClient display;
    private final Setting setting;
    private final Machine machine; 

    public Core() throws IOException {
        this.setting = new Setting("setting/Setting.json");
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
        }
        //</editor-fold>
        this.display = new DisplayClient();
        this.machine = new Machine(setting, display);                            
    }

    public void run() {
        new Thread(machine).start();
        display.setMachine(machine);
        display.setVersion(setting.getVersion());
        showDisplay();
    }

    private void showDisplay() {
        java.awt.EventQueue.invokeLater(() -> {
            display.setVisible(true);
        });
    }

}
