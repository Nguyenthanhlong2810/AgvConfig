package com.aubot.agv.exception;

public class AttributeException extends TelegramException {
    private final byte[] rawData;
    private final Class<?> clazz;

    public AttributeException(Class<?> clazz, byte[] rawData) {
        super(0xAB);
        this.clazz = clazz;
        this.rawData = rawData;
    }

    @Override
    public String getMessage() {
        StringBuilder builder = new StringBuilder("Can not parse value to ").append(clazz);
        builder.append(". Raw data: ");
        for (byte d : rawData) {
            builder.append(String.format("%02X ", d));
        }

        return builder.toString();
    }
}
