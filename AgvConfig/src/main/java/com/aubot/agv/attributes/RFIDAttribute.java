package com.aubot.agv.attributes;

import com.aubot.agv.exception.TelegramException;

public class RFIDAttribute extends Attribute<String> {

    public RFIDAttribute(int address, boolean readable, boolean writable) {
        super(address, AgvAttribute.RFID, readable, writable);
    }

    @Override
    public int getRegisterCount() {
        return 2;
    }

    @Override
    public byte[] encode() {
        return value.getBytes();
    }

    @Override
    public void decode(byte[] value) throws TelegramException {
        this.value = new String(value);
    }
}
