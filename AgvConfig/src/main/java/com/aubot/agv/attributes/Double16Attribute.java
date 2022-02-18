package com.aubot.agv.attributes;

import com.aubot.agv.exception.TelegramException;

public class Double16Attribute extends Attribute<Double> {
    public Double16Attribute(int address, String name, boolean readable, boolean writable) {
        super(address, name, readable, writable);
    }

    @Override
    public int getRegisterCount() {
        return 1;
    }

    @Override
    public byte[] encode() {
        return new byte[0];
    }

    @Override
    public void decode(byte[] value) throws TelegramException {
        this.value = (value[0] & 0xFF) + (value[1] & 0xFF) * 0.01;
    }
}
