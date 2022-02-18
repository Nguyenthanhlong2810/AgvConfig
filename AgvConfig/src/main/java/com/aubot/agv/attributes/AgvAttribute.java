package com.aubot.agv.attributes;

/**
 * The interface define a list of agv attribute
 * use for AttributeFactory create Attribute
 *
 * @author Duc
 * @version 1.0
 * @since 2021-04-19
 */
public @interface AgvAttribute {

    String STATUS = "Status";

    String DIRECTION = "Direction";

    String SPEED = "Speed";

    String OB_DISTANCE_TRUOC = "OB Distance Truoc";

    String OB_DISTANCE_CHEO = "OB Distance Cheo";

    String OB_DISTANCE_CANH = "OB Distance Canh";

    String BATTERY_REMAIN = "Battery Remain";

    String IS_WARNING = "Is Warning";

    String RFID = "RFID";

    String TACT_TIME = "Tact Time";

    String TABLE_LENGTH = "Table Length";

    String MAXCURRENT = "Max Current";

    String IN_CURVE = "In Curve";

    String ERROR_CODE = "Error Code";

    String SENSOR_STATUS = "Sensor status";

    String CALIB = "Calib";

    String SYNC_ENABLE = "Sync Enable";

    String SYNC_ID_GROUP = "Sync ID Group";

    String VOLTAGE = "Voltage";

    String CURRENT = "Current";

    String MP3_VOLUME = "Mp3 Volume";

    String PIN_11_VOLTAGE = "Pin 11 Voltage";

    String PIM_21_VOLTAGE = "Pin 21 Voltage";

    String LOW_BATTERY_LEVEL = "Low Battery Level";

    String OUT_BATTERY_LEVEL = "Out Battery Level";

    String IS_ALARM = "Is Alarm";

    String START_VOLUME = "Start Volume";

    String ALARM_VOLUME = "Alarm Volume";

    String WARNING_VOLUME = "Warning Volume";

    String SWAP_VOLUME = "Swap Volume";

    String MP3_TRACK = "Mp3 Track";

    String RFID_MAP = "RFID Map";

}
