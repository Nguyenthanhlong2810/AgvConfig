package com.aubot.agv.attributes;

import com.aubot.agv.exception.TelegramException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SensorAttribute extends Attribute<List<Boolean>> {
    /**
     * Attribute constructor
     *
     * @param address  Attribute address
     * @param readable attribute can read
     * @param writable attribute can write
     */
    public SensorAttribute(int address, boolean readable, boolean writable) {
        super(address, AgvAttribute.SENSOR_STATUS, readable, writable);
    }

    @Override
    public int getRegisterCount() {
        return 3;
    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }

    @Override
    public void decode(byte[] rawData) throws TelegramException {
        List<Boolean> sensorStatus = new ArrayList<>();
        int count = 0;
        for (byte b : rawData) {
            for (int i = 0; i < 8; i++) {
                int s = b >> count & 0x01;
                sensorStatus.add(s == 1);
                count++;
            }
        }

        value = sensorStatus;
    }
}
