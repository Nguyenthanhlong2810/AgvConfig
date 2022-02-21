package com.aubot.agv.exception;

import com.aubot.agv.ulti.ModbusTelegramHandler;

public class ModbusException extends TelegramException {

    private final ModbusTelegramHandler handler;
    private final ErrType errType;
    private final Object expected;
    private final Object realValue;

    public ModbusException(ErrType type, ModbusTelegramHandler handler, Object expected, Object realValue) {
        super(type.getCode());
        this.errType = type;
        this.handler = handler;
        this.expected = expected;
        this.realValue = realValue;
    }

    @Override
    public String getMessage() {
        StringBuilder builder = new StringBuilder("Failed to match response to request: ");
        builder.append(handler.getDirection().name())
                .append(" ")
                .append(handler.getAttribute().getName())
                .append(" - ");
        String template = errType.toString() + ". Expected: %s, received: %s";
        switch (errType) {
            case ADDRESS:
                builder.append(String.format(template,
                        String.format("0x%02X", ModbusTelegramHandler.getAddressCode()),
                        String.format("0x%02X", realValue)));
                break;
            case FUNCTION_CODE:
                builder.append(String.format(template,
                        String.format("0x%02X", expected),
                        String.format("0x%02X", realValue)));
                break;
            case CRC:
                builder.append(String.format(template,
                        String.format("0x%04X", expected),
                        String.format("0x%04X", realValue)));
                break;
            case WRITE_ADDRESS:
            case WRONG_VALUE:
            case REGISTER_COUNT:
            case BYTE_COUNT:
                builder.append(String.format(template,
                        expected.toString(),
                        expected.toString()));

            default:
                return this.getClass().getName() + ": Unknown error";
        }

        return builder.toString();
    }

    public enum ErrType {
        ADDRESS(0x01),
        FUNCTION_CODE(0x09),
        BYTE_COUNT(0x1A),
        CRC(0x1B),
        REGISTER_COUNT(0x1C),
        WRITE_ADDRESS(0x2C),
        WRONG_VALUE(0x3C);

        private final int code;

        ErrType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        @Override
        public String toString() {
            switch (this) {
                case ADDRESS:
                    return "Invalid address code";
                case FUNCTION_CODE:
                    return "Invalid read/write direction";
                case BYTE_COUNT:
                    return "Data length does not match";
                case CRC:
                    return "CRC check does not match";
                case REGISTER_COUNT:
                    return "Number of register does not match";
                case WRITE_ADDRESS:
                    return "Address of written register does not match";
                case WRONG_VALUE:
                    return "Wrong value was written";
            }
            return "???";
        }
    }
}
