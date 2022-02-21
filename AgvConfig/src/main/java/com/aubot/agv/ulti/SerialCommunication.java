package com.aubot.agv.ulti;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.IOException;

public class SerialCommunication implements HalfDuplexCommunication {

  SerialPort port;

  public SerialCommunication(SerialPort port) throws IOException {
    this.port = port;
    port.setBaudRate(115200);
    port.setNumDataBits(8);
    port.setNumStopBits(1);
    port.setParity(0);
//    port.addDataListener(this);
    if (!port.openPort()) {
      throw new IOException("Failed to open port");
    }
  }

  @Override
  public byte[] communicate(byte[] request, int required) throws IOException {
    port.writeBytes(request, request.length);
    int cycle = 0;
    int nread;
    while ((nread = port.bytesAvailable()) < required) {
      try {
        Thread.sleep(10);
      } catch (InterruptedException ignored) {}
      cycle++;
      if (cycle >= 255) {
        throw new IOException("Request timed out");
      }
    }

    byte[] response = new byte[nread];
    port.readBytes(response, nread);

    return response;
  }
}
