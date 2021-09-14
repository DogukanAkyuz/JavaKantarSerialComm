/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.fazecast.jSerialComm.SerialPort;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 *
 * @author Ahmet
 */
public class SerialThread extends Thread {

    public String weight = "";
    public String config = "";
    public String incomingData = "";
    public int firstByte = 0;
    public int lastByte = 0;

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

        firstByte = Integer.parseInt(configs[5]);
        lastByte = Integer.parseInt(configs[6]);

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

    public String parseWeight() {
        if (incomingData.length() > 0) {
            weight = incomingData.substring(firstByte, lastByte);
            //weight = Integer.toString(Integer.parseInt(weight));
            return weight;
        } else {
            return "------";
        }
    }

}
