package com.aubot.agv.ulti;

import com.aubot.agv.attributes.RfidMapAttribute;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RfidMapTelegramHandler implements TelegramHandler {
    private static final byte STX = (byte) 0xFD;
    private static final byte ETX = (byte) 0xFE;
    private static final byte TYPE = (byte) 2;
    RfidMapAttribute rfidMapAttribute;

    RfidMapTelegramHandler(RfidMapAttribute rfidMapAttribute) {
        this.rfidMapAttribute = rfidMapAttribute;
    }

    @Override
    public byte[] toRequest() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(STX);
        byte[] data = rfidMapAttribute.encode();
        outputStream.write((byte) (data.length + 1));
        outputStream.write(TYPE);
        outputStream.write(data);
        outputStream.write(ETX);
        return outputStream.toByteArray();
    }

    @Override
    public boolean matchRequest(byte[] response) throws IOException {
        int n = response.length;
        if(response[0] != STX){
            throw new IOException("STX is not match");
        }
        if(response[1] != (n - 3)){
            throw new IOException("Number of RFID is not match");
        }
        if(response[n - 1] != ETX){
            throw new IOException("STX is not match");
        }
        if(response[2] != TYPE){
            throw new IOException("TYPE is not match");
        }

        return response[3] == 1;
    }
}
