/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import FileTool.FileService;
import Model.PcInformation;
import Unicast.commons.Actions.FileTransfer;
import Unicast.commons.Actions.ListPackage;
import Unicast.commons.Actions.MyName;
import Unicast.commons.Actions.SimplePackage;
import Unicast.commons.Interface.ISend;
import View.DisplayClient;
import Unicast.commons.Interface.IObjectReceiver;

/**
 *
 * @author Administrator
 */
public class ClientReceiver implements IObjectReceiver<SimplePackage> {

    private ISend<SimplePackage> handler;
    private final DisplayClient display;
    private final FileService fileService;

    public ClientReceiver(DisplayClient display) {
        this.display = display;
        this.fileService = new FileService();
    }

    @Override
    public void receiver(SimplePackage pkg) {
        switch (pkg.getAction()) {
            case WHO_ARE_U -> {
                PcInformation pcInfo = PcInformation.getInstance();
                this.handler.send(new MyName(pcInfo.getComputerName(),
                        pcInfo.getSystemName(), pcInfo.getLine(),
                        pcInfo.getAllHardwareAddress()));
            }
            case PNs -> {
                ListPackage<String> PNs = (ListPackage<String>) pkg;
                this.display.setPnName(PNs.getData());
            }
            case FILE_TRANSFER -> {
                FileTransfer fileTransfer = (FileTransfer) pkg;
                byte[] data = fileTransfer.getData();
                String path = fileTransfer.getFilePath();
                if (path == null) {
                    return;
                }
                this.fileService.saveFile(path, data);
            }
            default ->
                throw new AssertionError();
        }
    }

    @Override
    public void setHandler(ISend<SimplePackage> handler) {
        this.handler = handler;
    }

}
