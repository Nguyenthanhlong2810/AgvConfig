package com.aubot.agv.attributes;

import com.aubot.agv.exception.TelegramException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RfidMapAttribute extends Attribute<List<RfidProperties>> {

    public RfidMapAttribute() {
        super(0, AgvAttribute.RFID_MAP, false, true);
    }

    @Override
    public int getRegisterCount() {
        return 0;
    }

    @Override
    public byte[] encode() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write((byte) value.size());
        for(RfidProperties rfidProp : value){
            int id = Integer.parseInt(rfidProp.getId().substring(1));
            int extraCard = rfidProp.isExtraCards() ? 1 : 0;
            int exConnection = rfidProp.isExConnection() ? 1 : 0;
            int timeStop = rfidProp.getStopTime();
            int timeWaiting = rfidProp.getConnWaitingTime();

            outputStream.write(convertIntToBytes(id,4));
            outputStream.write(convertIntToBytes(extraCard,1));
            outputStream.write(convertIntToBytes(exConnection,1));
            outputStream.write(convertIntToBytes(timeStop,4));
            outputStream.write(convertIntToBytes(timeWaiting,4));

        }
        return outputStream.toByteArray();
    }

    @Override
    public void decode(byte[] rawData) throws TelegramException {
        this.value = new ArrayList<>();
    }

    private byte[] convertIntToBytes(int num, int capacity) {
        byte[] bytes = new byte[capacity];
        int offset = 0;
        for (int i = 0; i < capacity; i++) {
           bytes[i] = (byte) (num >> (i * 8));
        }
        return bytes;
    }

}
