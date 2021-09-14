/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import static java.lang.Integer.parseInt;
import java.util.Scanner;

/**
 *
 * @author Ahmet
 */
public class SerialThread extends Thread {

    public String weight = "";
    public String config = "";
    public String incomingData = "";
    public String dataBuffer = "";
    public String line = "";

    public void PortConfig() {
        try {
            File myFile = new File("config.txt");
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine()) {
                config = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String[] configs = config.split(",");

        SerialPort serialPort = SerialPort.getCommPorts()[0];
        serialPort.setBaudRate(Integer.parseInt(configs[1]));
        serialPort.setNumDataBits(Integer.parseInt(configs[2]));
        serialPort.setNumStopBits(Integer.parseInt(configs[3]));
        serialPort.setParity(Integer.parseInt(configs[4]));
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);
        serialPort.openPort();

        Thread SerialPortThread;
        SerialPortThread = new Thread() {
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(20);
                        BufferedReader portReader
                                = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
                        incomingData = portReader.readLine();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        SerialPortThread.start();
    }

    public String veri() {
        return incomingData;
    }

}
