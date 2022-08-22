/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 *
 * @author Administrator
 */
public class PcInformation {

    private static volatile PcInformation instance;
    private String os;
    private int line;

    public void setLine(int line) {
        this.line = line;
    }

    private PcInformation() {
        this.line = -1;
    }

    public static PcInformation getInstance() {
        PcInformation ins = PcInformation.instance;
        if (ins == null) {
            synchronized (PcInformation.class) {
                ins = PcInformation.instance;
                if (ins == null) {
                    PcInformation.instance = ins = new PcInformation();
                }
            }
        }
        return ins;
    }

    public JSONObject getAllHardwareAddress() {
        JSONObject allHardware = new JSONObject();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = networkInterfaces.nextElement();
                if (ni.isLoopback() || !ni.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                JSONArray ips = new JSONArray();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet6Address || addr == null) {
                        continue;
                    }
                    ips.add(addr.getHostAddress());
                }
                byte[] hardwareAddress = ni.getHardwareAddress();
                if (hardwareAddress != null) {
                    String[] hexadecimalFormat = new String[hardwareAddress.length];
                    for (int i = 0; i < hardwareAddress.length; i++) {
                        hexadecimalFormat[i] = String.format("%02X", hardwareAddress[i]);
                    }
                    allHardware.put(String.join(":", hexadecimalFormat), ips);
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return allHardware;
    }

    public String getHostIp() {
        try {
            if (InetAddress.getLocalHost() != null) {
                return InetAddress.getLocalHost().getHostAddress();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getComputerName() {
        getSystemName();
        if (os.startsWith("Windows")) {
            return getWindowsHostName();
        } else if (os.startsWith("Mac")) {
            return getAppleHostName();
        }
        return null;
    }

    private String getWindowsHostName() {
        try {
            if (InetAddress.getLocalHost() == null) {
                return null;
            }
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getAppleHostName() {
        try {
            String cmd = "networksetup -getcomputername";
            Process proc = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            StringBuilder name = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                name.append(line).append("\r\n");
            }
            return name.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getSystemName() {
        if (os == null) {
            this.os = System.getProperties().getProperty("os.name");
        }
        return os;
    }

    public int getLine() {
        return line;
    }
}
