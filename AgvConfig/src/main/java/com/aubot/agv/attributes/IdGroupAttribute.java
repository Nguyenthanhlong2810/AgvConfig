package com.aubot.agv.attributes;

import com.aubot.agv.exception.TelegramException;

import java.util.AbstractMap;
import java.util.Map;

public class IdGroupAttribute extends Attribute<IdGroupAttribute.IdGroup> {
    /**
     * Attribute constructor
     *
     * @param address  Attribute address
     * @param readable attribute can read
     * @param writable attribute can write
     */
    public IdGroupAttribute(int address, boolean readable, boolean writable) {
        super(address, AgvAttribute.SYNC_ID_GROUP, readable, writable);
    }

    @Override
    public int getRegisterCount() {
        return 1;
    }

    @Override
    public byte[] encode() {
        return new byte[] {(byte) value.group, (byte) value.id};
    }

    @Override
    public void decode(byte[] rawData) throws TelegramException {
        value = new IdGroup(rawData[1], rawData[0]);
    }

    public static class IdGroup {
        private int id;
        private int group;

        public IdGroup(int id, int group) {
            this.id = id;
            this.group = group;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getGroup() {
            return group;
        }

        public void setGroup(int group) {
            this.group = group;
        }
    }
}
