package com.aubot.agv.attributes;

import com.aubot.agv.exception.TelegramException;

import java.io.IOException;

/**
 * Class represent for Agv Attribute
 *
 * @param <T> is set based on data type in real AGV
 *
 * @author Khoi
 * @version 1.0
 * @since 2021-04-16
 */
public abstract class Attribute<T> {

    /**
     * value of attribute, depends on generics data type
     */
    protected T value;
    /**
     * Register address of agv attribute
     */
    protected int address;
    /**
     * Attribute name
     */
    protected String name;
    /**
     * indicate that attribute is readable or not
     */
    private final boolean readable;
    /**
     * indicate that attribute is writable or not
     */
    private final boolean writable;
    /**
     * Attribute constructor
     *
     * @param address Attribute address
     * @param readable attribute can read
     * @param writable attribute can write
     */
    public Attribute(int address, String name, boolean readable, boolean writable) {
        this.address = address;
        this.name = name;
        this.readable = readable;
        this.writable = writable;
    }
    /**
     * @return attribute address
     */
    public int getAddress() {
        return address;
    }
    /**
     * @return attribute name
     */
    public String getName() {
        return name;
    }
    /**
     * @return value based on generic data type
     */
    public T getValue() {
        return value;
    }
    /**
     * @param value set value to @param value
     */
    public void setValue(T value) {
        this.value = value;
    }
    /**
     * @return is attribute readable
     */
    public boolean isReadable() {
        return readable;
    }
    /**
     * @return is attribute writable
     */
    public boolean isWritable() {
        return writable;
    }

    /**
     * @return number of register that attribute allocated
     */
    public abstract int getRegisterCount();

    /**
     * Define the way to parse @value to byte array
     *
     * @return byte array parsed from @value
     */
    public abstract byte[] encode() throws IOException;

    /**
     * Define the way to parse byte array to @value
     *
     * @param rawData get from response after communicate with device
     *                the result is assigned to @value
     * @throws TelegramException if parse failed
     */
    public abstract void decode(byte[] rawData) throws TelegramException;

    public Class<?> getValueClass() {
        return value.getClass();
    }
}
