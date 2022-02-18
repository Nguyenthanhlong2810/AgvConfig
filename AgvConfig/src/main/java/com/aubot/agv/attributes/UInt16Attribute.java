package com.aubot.agv.attributes;

import com.aubot.agv.exception.TelegramException;

/**
 * Class represent for attribute which allocated in single register
 * Generic data type is Integer, but it is converted to unsigned int 16 bit
 *
 * @author Khoi
 * @version 1.0
 * @since 2021-04-16
 */
public class UInt16Attribute extends Attribute<Integer> {

    public UInt16Attribute(int address, String name, boolean readable, boolean writable) {
        super(address, name, readable, writable);
    }

    @Override
    public int getRegisterCount() {
        return 1;
    }

    @Override
    public byte[] encode() {
        return new byte[] {(byte) (value >> 8), value.byteValue()};
    }

    @Override
    public void decode(byte[] rawData) throws TelegramException {
        this.value = ((rawData[0] & 0xFF) << 8) | (rawData[1] & 0xFF);
    }
}