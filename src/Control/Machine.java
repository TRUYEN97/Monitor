/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import Model.AllKeyword;
import Model.Setting;
import Unicast.Client.Client;
import Unicast.commons.Actions.SimplePackage;
import View.DisplayClient;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author Administrator
 */
public class Machine implements Runnable {

    private final Setting setting;
    private final Client<SimplePackage> client;

    public Machine(Setting setting, DisplayClient display) throws IOException {
        this.setting = setting;
        this.client = new Client(new ClientReceiver(display));
        new Timer(1000, (e) -> {
            if (!this.client.isConnect()) {
                String host = setting.getString(AllKeyword.SERVER_HOST);
                int port = setting.getInteger(AllKeyword.SERVER_PORT);
                this.client.connect(host, port);
                display.showServerAddr(String.format("%s - %s", host, port));
            }
            display.setServerConnect(this.client.isConnect());
        }).start();
    }

    @Override
    public void run() {
        while (true) {
            if (client.isConnect()) {
                client.run();
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            }
        }
    }

    public void send(SimplePackage simplePackage) {
        if (!this.client.send(simplePackage)) {
            JOptionPane.showMessageDialog(null, "Request to server failed!");
        }
    }

}
