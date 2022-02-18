package com.aubot.agv.attributes;

/**
 * Factory class used for create AGV attributes
 *
 * @author Duc
 * @version 1.0
 * @since 2021-04-16
 */
public class AttributeFactory {
    /**
     * Singleton
     */
    private static AttributeFactory instance = null;
    /**
     * Anti creating instance
     */

    /**
     * Get instance
     */
    public static AttributeFactory getInstance() {
        if (instance == null) {
            instance = new AttributeFactory();
        }

        return instance;
    }
    /**
     * Factory
     */
    public static Attribute createAttribute(@AgvAttribute String attributeName) {
        switch (attributeName) {
            case AgvAttribute.STATUS:
                return getInstance().getStatus();
            case AgvAttribute.DIRECTION:
                return getInstance().getDirection();
            case AgvAttribute.SPEED:
                return getInstance().getSpeed();
            case AgvAttribute.OB_DISTANCE_TRUOC:
                return getInstance().getOBDistanceTruoc();
            case AgvAttribute.OB_DISTANCE_CHEO:
                return getInstance().getOBDistanceCheo();
            case AgvAttribute.OB_DISTANCE_CANH:
                return getInstance().getOBDistanceCanh();
            case AgvAttribute.BATTERY_REMAIN:
                return getInstance().getBatteryRemain();
            case AgvAttribute.IS_WARNING:
                return getInstance().getIsWaring();
            case AgvAttribute.RFID:
                return getInstance().getRFID();
            case AgvAttribute.TACT_TIME:
                return getInstance().getTactTime();
            case AgvAttribute.TABLE_LENGTH:
                return getInstance().getTableLength();
            case AgvAttribute.MAXCURRENT:
                return getInstance().getMaxCurrent();
            case AgvAttribute.IN_CURVE:
                return getInstance().getInCurve();
            case AgvAttribute.ERROR_CODE:
                return getInstance().getErrorCode();
            case AgvAttribute.SENSOR_STATUS:
                return getInstance().getSensorStatus();
            case AgvAttribute.CALIB:
                return getInstance().getCalib();
            case AgvAttribute.SYNC_ENABLE:
                return getInstance().getSyncEnable();
            case AgvAttribute.SYNC_ID_GROUP:
                return getInstance().getSyncIDGroup();
            case AgvAttribute.VOLTAGE:
                return getInstance().getVoltage();
            case AgvAttribute.CURRENT:
                return getInstance().getCurrent();
            case AgvAttribute.MP3_VOLUME:
                return getInstance().getMp3Volume();
            case AgvAttribute.PIN_11_VOLTAGE:
                return getInstance().getPin11Voltage();
            case AgvAttribute.PIM_21_VOLTAGE:
                return getInstance().getPin21Voltage();
            case AgvAttribute.LOW_BATTERY_LEVEL:
                return getInstance().getLowBatteryLevel();
            case AgvAttribute.OUT_BATTERY_LEVEL:
                return getInstance().getOutBatteryLevel();
            case AgvAttribute.IS_ALARM:
                return getInstance().getIsAlarm();
            case AgvAttribute.START_VOLUME:
                return getInstance().getStartVolume();
            case AgvAttribute.ALARM_VOLUME:
                return getInstance().getAlarmVolume();
            case AgvAttribute.WARNING_VOLUME:
                return getInstance().getWarningVolume();
            case AgvAttribute.SWAP_VOLUME:
                return getInstance().getSwapVolume();
            case AgvAttribute.MP3_TRACK:
                return getInstance().getMp3Track();
            case AgvAttribute.RFID_MAP:
                return getInstance().getRfidMap();
            default:
                return null;
        }
    }
    /**
     * List of Attribute
     * @return Attribute by name
     */
    public Attribute<?> getStatus() {
        return new UInt16Attribute(1, AgvAttribute.STATUS, true,true);
    }

    public  Attribute<?> getDirection() {
        return new UInt16Attribute(2, AgvAttribute.DIRECTION,true,true);
    }

    public  Attribute<?> getSpeed() {
        return new UInt16Attribute(3, AgvAttribute.SPEED,true,true);
    }

    public Attribute<?> getOBDistanceTruoc() {
        return new UInt16Attribute(4, AgvAttribute.OB_DISTANCE_TRUOC,true,true);
    }

    public Attribute<?> getOBDistanceCheo() {
        return new UInt16Attribute(5, AgvAttribute.OB_DISTANCE_CHEO,true,true);
    }

    public Attribute<?> getOBDistanceCanh() {
        return new UInt16Attribute(6, AgvAttribute.OB_DISTANCE_CANH,true,true);
    }

    public Attribute<?> getBatteryRemain() {
        return new UInt16Attribute(7, AgvAttribute.BATTERY_REMAIN,true,false);
    }

    public Attribute<?> getIsWaring() {
        return new UInt16Attribute(8, AgvAttribute.IS_WARNING,true,false);
    }

    public Attribute<?> getRFID() {
        return new RFIDAttribute(10,true,false);
    }

    public Attribute<?> getTactTime() {
        return new UInt16Attribute(12, AgvAttribute.TACT_TIME,true,true);
    }

    public  Attribute<?> getTableLength() {
        return new UInt16Attribute(13, AgvAttribute.TABLE_LENGTH,true,true);
    }

    public Attribute<?> getMaxCurrent() {
        return new Double16Attribute(14, AgvAttribute.MAXCURRENT,true,true);
    }

    public  Attribute<?> getInCurve() {
        return new UInt16Attribute(15, AgvAttribute.IN_CURVE,true,true);
    }

    public Attribute<?> getErrorCode() {
        return new UInt16Attribute(16, AgvAttribute.ERROR_CODE,true,false);
    }

    public Attribute<?> getSensorStatus() {
        return new SensorAttribute(17,true,false);
    }

    public Attribute<?> getCalib() {
        return new UInt16Attribute(20, AgvAttribute.CALIB,true,true);
    }

    public Attribute<?> getSyncEnable() {
        return new UInt16Attribute(21, AgvAttribute.SYNC_ENABLE,true,true);
    }

    public Attribute<?> getSyncIDGroup() {
        return new IdGroupAttribute(22,true,true);
    }

    public Attribute<?> getVoltage() {
        return new Double16Attribute(23, AgvAttribute.VOLTAGE,true,false);
    }

    public Attribute<?> getCurrent() {
        return new Double16Attribute(24, AgvAttribute.CURRENT,true,false);
    }

    public  Attribute<?> getMp3Volume() {
        return new UInt16Attribute(25, AgvAttribute.MP3_VOLUME,true,true);
    }

    public  Attribute<?> getPin11Voltage() {
        return new Double16Attribute(27, AgvAttribute.PIN_11_VOLTAGE,true,false);
    }

    public  Attribute<?> getPin21Voltage() {
        return new Double16Attribute(28, AgvAttribute.PIM_21_VOLTAGE,true,false);
    }

    public  Attribute<?> getLowBatteryLevel() {
        return new UInt16Attribute(29, AgvAttribute.LOW_BATTERY_LEVEL,true,true);
    }

    public Attribute<?> getOutBatteryLevel() {
        return new UInt16Attribute(30, AgvAttribute.OUT_BATTERY_LEVEL,true,true);
    }

    public  Attribute<?> getIsAlarm() {
        return new UInt16Attribute(31, AgvAttribute.IS_ALARM,true,true);
    }

    public  Attribute<?> getStartVolume() {
        return new UInt16Attribute(32, AgvAttribute.START_VOLUME,true,true);
    }

    public  Attribute<?> getAlarmVolume() {
        return new UInt16Attribute(33, AgvAttribute.ALARM_VOLUME,true,true);
    }

    public  Attribute<?> getWarningVolume() {
        return new UInt16Attribute(34, AgvAttribute.WARNING_VOLUME,true,true);
    }

    public  Attribute<?> getSwapVolume() {
        return new UInt16Attribute(35, AgvAttribute.SWAP_VOLUME,true,true);
    }

    public Attribute<?> getMp3Track(){
        return new UInt16Attribute(36, AgvAttribute.MP3_TRACK,true,true);
    }

    public Attribute<?> getRfidMap() {
        return new RfidMapAttribute();
    }
}
