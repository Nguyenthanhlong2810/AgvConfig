package com.aubot.agv.ulti;

import com.aubot.agv.attributes.Attribute;
import com.aubot.agv.exception.ModbusException;
import com.aubot.agv.exception.TelegramException;

import java.io.IOException;
import java.util.Arrays;

import static com.aubot.agv.exception.ModbusException.ErrType;

/**
 * This class is used to handle data and for request and response in communication
 *
 * @author Khoi
 * @version 1.0
 * @since 2021-04-16
 */
public class ModbusTelegramHandler implements TelegramHandler {
    /**
     * Header of modbus message
     */
    private static final int HEADER = 0x01;
    /**
     * Length of request send to device
     */
    private static final int REQUEST_LENGTH = 8;
    /**
     * An attribute represent for a information container in communication
     * On a read request, data value return in response will be assign to attribute
     * Otherwise a write request, an value in attribute will be send to device
     * for setting configurations or specifications
     */
    private Attribute attribute;
    /**
     * Direction of handler, also the direction of communication
     */
    private HalfDuplexCommunication.Direction dir;

    /**
     * Handler constructor
     *
     * @param attribute an attribute used for contain value get from device, or send to device
     * @param dir a direction of communication
     */
    public ModbusTelegramHandler(Attribute attribute, HalfDuplexCommunication.Direction dir) {
        this.attribute = attribute;
        this.dir = dir;
    }

    /**
     * @return a byte represent direction of communication
     */
    public int getFunctionCode() {
        switch (dir) {
            case READ:
                return 0x03;
            case WRITE:
                return 0x06;
        }
        return 0;
    }
    /**
     * @return header of message
     */
    public static int getAddressCode() {
        return HEADER;
    }
    /**
     * @return direction of handler
     */
    public HalfDuplexCommunication.Direction getDirection() {
        return dir;
    }

    /**
     * @return attribute
     */
    public Attribute getAttribute() {
        return attribute;
    }

    /**
     * This method is used to process data value and build a frame according Modbus-16 standard
     *
     * There a two type of request: READ and WRITE
     * A read request works in a following structure: a byte array
     *      | HEADER |  FC  | SA | NoR | CRC |
     *      |  0x01  | 0x03 |    |     |     |
     * SA: start address, connected device will fetch data from from register with this address. (2 bytes)
     * NoR: number of register which be fetched
     * CRC: Cyclic Redundancy Check, this modbus communication use CRC 16 bit (2 bytes)
     *
     * A write single register request works in a following structure: a byte array
     *      | HEADER |  FC  | ADDRESS | VALUE | CRC |
     *      |  0x01  | 0x06 |         |       |     |
     * ADDRESS: register address want to write value
     * VALUE: the value which will be written to device register
     *
     * @return a complete request to send to connected device
     */
    @Override
    public byte[] toRequest() throws IOException {
        byte[] request = new byte[REQUEST_LENGTH];
        int address = attribute.getAddress();
        //Add request header
        request[0] = HEADER;

        //add communication direction
        request[1] = (byte) getFunctionCode();

        //add start address
        request[2] = (byte) (address >> 8);
        request[3] = (byte) address;

        switch (dir) {
            //if a read request, add number of register
            case READ:
                int registerCount = attribute.getRegisterCount();
                request[4] = (byte) (registerCount >> 8);
                request[5] = (byte) registerCount;
                break;
            //if a write request, add attribute value to
            case WRITE:
                byte[] value = attribute.encode();
                request[4] = value[0];
                request[5] = value[1];
                break;
        }
        //Add crc check at the end of request
        int crc = getModbusCrc16(request, request.length - 2);
        request[6] = (byte) crc;
        request[7] = (byte) (crc >> 8);

        return request;
    }

    /**
     * This method is used to match response with previous sent modbus request
     *
     * @param response content of message want to match
     * @return true if match successfully
     * @throws TelegramException if got problem when checking match
     */
    @Override
    public boolean matchRequest(byte[] response) throws IOException {
        // Check HEADER
        if (response[0] != HEADER) {
            throw new ModbusException(ErrType.ADDRESS, this, HEADER, response[0]);
        }

        // Check direction
        if (response[1] != getFunctionCode()) {
            throw new ModbusException(ErrType.FUNCTION_CODE, this, getFunctionCode(), response[1]);
        }

        // Check response CRC
        int length = response.length;
        int crc = ((response[length - 1] & 0xFF) << 8) | (response[length - 2] & 0xFF);
        int resCrc = getModbusCrc16(response, length - 2);
        if (crc != resCrc) {
            throw new ModbusException(ErrType.CRC, this, crc, resCrc);
        }

        switch (dir) {
            // If read response
            case READ:
                /* Check match between length of main content in response with number of register
                    in request
                     data length = number of register x 2 */
                int byteCount = response[2];
                if (attribute.getRegisterCount() * 2 != byteCount) {
                    throw new ModbusException(ErrType.REGISTER_COUNT, this,
                            attribute.getRegisterCount(), byteCount);
                }

                // Check length of main content match with data length return from response
                byte[] bytesValue = Arrays.copyOfRange(response, 3, length - 2);
                if (bytesValue.length != byteCount) {
                    throw new ModbusException(ErrType.BYTE_COUNT, this,
                            byteCount, bytesValue.length);
                } else {
                    attribute.decode(bytesValue);
                }

                break;
            case WRITE:
                // Check for a complete match of the request
                int address = ((response[2] & 0xFF) << 8) | (response[3] & 0xFF);
                if (address != attribute.getAddress()) {
                    throw new ModbusException(ErrType.WRITE_ADDRESS, this,
                            attribute.getAddress(), address);
                }

                byte[] oldValue = attribute.encode();
                if (response[4] != oldValue[0] || response[5] != oldValue[1]) {
                    throw new ModbusException(ErrType.WRONG_VALUE, this,
                            attribute.getValue(), response[4] << 8 | response[5]);
                }
                break;
        }
        return true;
    }

    /**
     * Calculate and return 16 bit CRC check based on byte array data
     *
     * @param data content of data to calculate CRC
     * @param length length of @data
     * @return 16 bit CRC as integer
     */
    private int getModbusCrc16(byte[] data, int length) {
        int crc = 0xFFFF;
        for (int i = 0; i < length; i++) {
            crc ^= data[i] & 0xFF;
            for (int j = 8; j != 0; j--) {
                if ((crc & 0x0001) != 0) {
                    crc >>= 1;
                    crc ^= 0xA001;
                } else {
                    crc >>= 1;
                }
            }
        }
        return crc;
    }
}
