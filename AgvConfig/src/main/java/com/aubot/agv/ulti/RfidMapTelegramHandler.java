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
        int length = data.length + 1;
        outputStream.write((byte) length);
        outputStream.write((byte) (length >> 8));
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
        int length = (response[2] << 8) | response[1];
        if(length != (n - 4)){
            throw new IOException("Data length is not match");
        }
        if(response[n - 1] != ETX){
            throw new IOException("STX is not match");
        }
        if(response[3] != TYPE){
            throw new IOException("TYPE is not match");
        }

        return response[4] == 1;
    }

    @Override
    public int getResponseRequired(HalfDuplexCommunication.Direction dir) {
        return 6;
    }
}
