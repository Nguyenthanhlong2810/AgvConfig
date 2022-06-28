package com.aubot.agv.ulti;

import com.aubot.agv.attributes.AgvAttribute;
import com.aubot.agv.attributes.Attribute;
import com.aubot.agv.attributes.AttributeFactory;
import com.aubot.agv.attributes.IdGroupAttribute;
import com.aubot.agv.attributes.StopSource;
import com.aubot.agv.exception.TelegramException;

import java.io.IOException;
import java.util.List;

/**
 * Class is used to get and set information with AGV
 * Instances of this class are initialized by class AgvManager
 *
 * @author Khoi, Duc
 * @version 1.0
 * @since 2021-04-16
 */
public class AgvDevice {
    /**
     * Object is used to create a handler to process data when send or receive message with AGV
     */
    private final HandlerBuilder handlerBuilder;
    /**
     * Object is used to communicate with AGV
     * This communicate is half-duplex
     * Detail is written in class
     */
    private final HalfDuplexCommunication comm;

    public AgvDevice(HalfDuplexCommunication comm, HandlerBuilder handlerBuilder) {
        this.comm = comm;
        this.handlerBuilder = handlerBuilder;
    }

    /**
     * Set value of attribute to AGV
     *
     * @param attribute AGV attribute want to set
     *                  The value is set in @Attribute.value
     * @return true if set value successfully
     * @throws IOException if set failed
     */
    public boolean setAttribute(Attribute attribute) throws IOException {
        if (!attribute.isWritable()) {
            throw new TelegramException(0x02, "Attribute is unwritable");
        }

        return updateAttribute(attribute, HalfDuplexCommunication.Direction.WRITE);
    }

    /**
     * Get value of Attribute from AGV
     *
     * @param attribute AGV attribute want to get
     * @return true if get value successfully, the value is get to @Attribute.value
     * @throws IOException if get failed
     */
    public boolean getAttribute(Attribute attribute) throws IOException {
        if (!attribute.isReadable()) {
            throw new TelegramException(0x02, "Attribute is unreadable");
        }

        return updateAttribute(attribute, HalfDuplexCommunication.Direction.READ);
    }

    /**
     * This method is used to synchronize AGV attribute and attribute was pass to method
     *
     * @param attribute The attribute will be sync with AGV
     * @param dir direction of sync
     *            if direction is READ then assign AGV value to corresponding attribute
     *            if direction is WRITE then assign value of attribute to AGV
     * @return true if update successfully
     * @throws IOException if update failed
     */
    private boolean updateAttribute(Attribute attribute, HalfDuplexCommunication.Direction dir)
            throws IOException {
        TelegramHandler th = handlerBuilder.createHandler(attribute, dir);
        byte[] response;
        if (dir == HalfDuplexCommunication.Direction.READ) {
            response = comm.communicate(th.toRequest(), th.getResponseRequired(HalfDuplexCommunication.Direction.READ));
        } else {
            response = comm.communicate(th.toRequest(), th.getResponseRequired(HalfDuplexCommunication.Direction.WRITE));
        }

        return th.matchRequest(response);
    }

    /**
     * This method is used to get value from attribute
     *
     * @param attributeName references from @interface AgvAttribute
     * @return @value of Attribute
     * @throws IOException get value failed
     */
    private Object getAttributeValue(String attributeName) throws IOException {
        Attribute attribute = AttributeFactory.createAttribute(attributeName);
        getAttribute(attribute);

        return attribute.getValue();
    }

    /**
     *  This function will set value of attribute
     * @param attributeName Name of Bean
     * @param num Value for Attribute
     * @return set value  for Attribute
     * @throws IOException Error Not exsits Bean
     */
    private boolean setAttributeValue(String attributeName, Object num) throws IOException {
        Attribute attribute = AttributeFactory.createAttribute(attributeName);
        attribute.setValue(num);

        return setAttribute(attribute);
    }

    /**
     * Get the current status of Agv
     * @return Running – 0, Stop – 1, Estop – 2
     * @throws IOException Error get Status
     */

    public int getStatus() throws IOException {
        return (int) getAttributeValue(AgvAttribute.STATUS);
    }

    /**
     * Get the current Speed of Agv
     * @return Speed of Agv
     * @throws IOException Error get Speed
     */

    public int getSpeed() throws IOException {
        return (int) getAttributeValue(AgvAttribute.SPEED);
    }

    /**
     * Start running Agv
     * @return Ordered agv to run
     * @throws IOException Error run
     */
    public boolean start() throws IOException {
        return setAttributeValue(AgvAttribute.STATUS,0);
    }

    /**
     *Stop AGV
     * @return Ordered agv to stop
     * @throws IOException Error stop
     */
    public boolean stop() throws IOException {
        return setAttributeValue(AgvAttribute.STATUS,1);
    }

    /**
     *Turn left at an intersection
     * @return  Ordered agv to turn left
     * @throws IOException Error turn left
     */
    public boolean turnLeft() throws IOException {
        return setAttributeValue(AgvAttribute.DIRECTION,1);
    }

    /**
     * Turn Reft at an intersection
     * @return  Ordered agv to turn right
     * @throws IOException Error turn right
     */
    public boolean turnRight() throws IOException {
        return setAttributeValue(AgvAttribute.DIRECTION,2);
    }

    /**
     * Go Straight at an intersection
     * @return  Ordered agv to go strainght
     * @throws IOException Error go straiht
     */
    public boolean goStraight() throws IOException {
        return setAttributeValue(AgvAttribute.DIRECTION,0);
    }

    /**
     * Set Speed for Agv
     * @param speed transmisson in Speed
     * @return State success or not
     * @throws IOException Error set Speed
     */
    public boolean setSpeed(int speed) throws IOException {
        return setAttributeValue(AgvAttribute.SPEED, speed);
    }

    /**
     * Set the distance for the sensor to avoid forward obstructions
     * @param distance transmisson in Distance
     * @return State success or not
     * @throws IOException Error set Distance
     */
    public boolean setOBDistanceTruoc(int distance) throws IOException {
        return setAttributeValue(AgvAttribute.FRONT_DISTANCE_SENSOR,distance);
    }

    /**
     * Set the distance for the sensor to avoid cross-side obstructions
     * @param distance transmisson in Distance
     * @return State success or not
     * @throws IOException Error set Distance
     */
    public boolean setOBDistanceCheo(int distance) throws IOException {
        return setAttributeValue(AgvAttribute.BACK_DISTANCE_SENSOR,distance);
    }

    /**
     * Set the distance for the sensor to avoid side obstructions
     * @param distance transmisson in Distance
     * @return State success or not
     * @throws IOException Error set Distance
     */
    public boolean setOBDistanceCanh(int distance) throws IOException {
        return setAttributeValue(AgvAttribute.LEFT_DISTANCE_SENSOR,distance);
    }

    /**
     * Get the distance for the sensor to avoid forward obstructions
     * @return State success or not
     * @throws IOException Error get OBDistanceTruoc
     */
    public int getOBDistanceTruoc() throws IOException {
        return (int) getAttributeValue(AgvAttribute.FRONT_DISTANCE_SENSOR);
    }

    /**
     * Get the distance for the sensor to avoid cross-side obstructions
     * @return State success or not
     * @throws IOException Error get OBDistanceCheo
     */
    public int getOBDistanceCheo() throws IOException {
        return (int) getAttributeValue(AgvAttribute.BACK_DISTANCE_SENSOR);
    }

    /**
     * Get the distance for the sensor to avoid side obstructions
     * @return State success or not
     * @throws IOException Error get OBDistanceCanh
     */
    public int getOBDistanceCanh() throws IOException {
        return (int) getAttributeValue(AgvAttribute.LEFT_DISTANCE_SENSOR);
    }

    /**
     *  Check whether Agv is running
     * @return state of Agv
     * @throws IOException Error get State
     */
    public boolean isRunning() throws IOException {
        return getStatus() == 0;
    }

    /**
     * Check whether Agv is stopped
     * @return state of Agv
     * @throws IOException Error get State
     */
    public boolean isStop() throws IOException {
        return getStatus() == 1;
    }

    /**
     *  Check whether Agv was bumped
     * @return state of Agv
     * @throws IOException Error get Warning
     */
    public boolean isWarning() throws IOException {
        int warning = (int) getAttributeValue(AgvAttribute.IS_WARNING);

        return warning == 1;
    }

    /**
     * Get the next turn direction
     * @return Straight – 0, Left – 1, Right – 2
     * @throws IOException Error get Direction
     */
    public int getDir() throws IOException {
        return (int) getAttributeValue(AgvAttribute.DIRECTION);
    }

    /**
     * Check whether to turn left at an intersection
     * @return get State success or not
     * @throws IOException Error get State
     */
    public boolean isTurnLeft() throws IOException {
        return getDir() == 1 ;
    }

    /**
     * Check whether to turn right at an intersection
     * @return get State success or not
     * @throws IOException Error get State
     */
    public boolean isTurnRight() throws IOException {
        return getDir() == 2;
    }

    /**
     * Check whether go straight at an intersection
     * @return get State success or not
     * @throws IOException Error get State
     */
    public boolean isGoStraight() throws IOException {
        return getDir() == 0;
    }

    /**
     * Get battery remain of Agv
     * @return battery remain of Agv
     * @throws IOException Error get battery remain
     */
    public int getBatteryRemain() throws IOException {
        return (int) getAttributeValue(AgvAttribute.BATTERY_REMAIN);
    }

    /**
     * Set Volume for Agv
     * @param volume for Agv
     * @return get State success or not
     * @throws IOException  Error get State
     */
    public boolean setVolume(int volume) throws IOException {
        return setAttributeValue(AgvAttribute.MP3_VOLUME,volume);
    }

    /**
     * Get the current volume of Agv
     * @return value of volume
     * @throws IOException Error get Volume
     */
    public int getVolume() throws IOException {
        return (int) getAttributeValue(AgvAttribute.MP3_VOLUME);
    }

    /**
     * Setting tact-time for AGV C450
     * @param tacttime value for AGV C450
     * @return State success or not
     * @throws IOException Error Set Tact-time
     */
    public boolean setTactTime(int tacttime) throws IOException {
        return setAttributeValue(AgvAttribute.TACT_TIME,tacttime);
    }

    /**
     * Get tact-time of AGV C450
     * @return  value tact-time of Agv
     * @throws IOException Error get tact-time
     */
    public int getTactTime() throws IOException {
        return (int) getAttributeValue(AgvAttribute.TACT_TIME);
    }

    /**
     * Set Table length
     * @param length value Length
     * @return State success or not
     * @throws IOException Error set length
     */
    public boolean setLength(int length) throws IOException {
        return setAttributeValue(AgvAttribute.TABLE_LENGTH,length);

    }

    /**
     * get Table Length
     * @return value Length
     * @throws IOException Error get Length
     */
    public int getLength() throws IOException {
        return (int) getAttributeValue(AgvAttribute.TABLE_LENGTH);
    }

    /**
     * Set Curve for Agv
     * @param curve 1: in curve, 0: out curve
     * @return State success or not
     * @throws IOException Error set Curve
     */
    public boolean setCurve(int curve) throws IOException {
        return setAttributeValue(AgvAttribute.IN_CURVE,curve);

    }

    /**
     * Get Curve of Agv
     * @return value Curve
     * @throws IOException  Error get Curve
     */
    public int getCurve() throws IOException {
        return (int) getAttributeValue(AgvAttribute.IN_CURVE);
    }

    /**
     * Turn on, turn off sync feature
     * @param sync 1: turn on,  0: turn off
     * @return State success or not
     * @throws IOException Error get Sync
     */
    public boolean setSync(int sync) throws IOException {
        return setAttributeValue(AgvAttribute.SYNC_ENABLE,sync);
    }

    /**
     * Get State sync feature
     * @return value Sync
     * @throws IOException Error get Sync
     */
    public int getSync() throws IOException {
        return (int) getAttributeValue(AgvAttribute.SYNC_ENABLE);
    }

    /**
     * Perform sensor calib for line detector circuit
     * @param cablib_type 0 - None ; 1 - Calib inline, 2 - Calib outline, 3 - Calib Mark
     * @return State success or not
     * @throws IOException Error set Calib
     */
    public boolean calib(int cablib_type) throws IOException {
        return setAttributeValue(AgvAttribute.CALIB,cablib_type);
    }

    /**
     * Get the State current Calib
     * @return calue Calib
     * @throws IOException Error get Calib status
     */
    public int getCalibStatus() throws IOException {
        return (int) getAttributeValue(AgvAttribute.CALIB);
    }

    /**
     * Get array the Voltage (Voltage, pin11, pin12, pin21, pin22)
     * @return double[]{Voltage, pin11, pin12, pin21, pin22}
     * @throws IOException Error get Voltage
     */
    public double[] getVoltage() throws IOException {
         double voltage = (double) getAttributeValue(AgvAttribute.VOLTAGE);
         double pin11 = (double) getAttributeValue(AgvAttribute.PIN_11_VOLTAGE);
         double pin21 = (double) getAttributeValue(AgvAttribute.PIM_21_VOLTAGE);
         double pin12 = voltage - pin11;
         double pin22 = voltage - pin21;
        return new double[]{voltage, pin11, pin21, pin12, pin22};
    }

    /**
     * Get Current of Agv
     * @return Current of Agv
     * @throws IOException Error get Current
     */
    public double getCurrent() throws IOException {
        return (double) getAttributeValue(AgvAttribute.CURRENT);
    }

    /**
     * Get String of Error code
     * @return Stop Source of Agv
     * @throws IOException Error get StopSource
     */
    public String getStopSource() throws IOException {
        int errorCode = (int) getAttributeValue(AgvAttribute.ERROR_CODE);
        switch (errorCode){
            case 0:
                return StopSource.NONE.toString();

            case 1:
                return StopSource.BUTTON.toString();

            case 2:
                return StopSource.TABLE_STOP.toString();

            case 3:
                return StopSource.OUTLINE.toString();

            case 4:
                return StopSource.TABLE_ESTOP.toString();

            case 5:
                return StopSource.ESTOP.toString();

            case 6:
                return StopSource.LOW_BATTERY.toString();

            case 7:
                return StopSource.LINE_DETECTOR_CIRCUIT.toString();

            case 8:
                return StopSource.CAN_ERROR.toString();

            case 9:
                return StopSource.STOP_SYNCHRONIZED.toString();

            case 10:
                return StopSource.OVER_LOAD.toString();

            default:
                return StopSource.UNKNOWN.toString();

        }
    }

    /**
     * Get the State current Sensor
     * @param sensor boolean array contain sensor status after get
     * @return true if override to sensor array successfully
     * @throws IOException Error get Sensor Status
     */
    public boolean getSensorStatus(boolean[] sensor) throws IOException {
        List<Boolean> sensorStatus = (List<Boolean>) getAttributeValue(AgvAttribute.SENSOR_STATUS);
        for (int i = 0; i < sensor.length; i++) {
            sensor[i] = sensorStatus.get(i);
        }

        return true;
    }

    /**
     * Set Max Current. If the current is greater than the maximum current then Agv will stopped
     * @param maxCur value Max Current
     * @return State success or not
     * @throws IOException Error set MaxCurrent
     */
    public boolean setMaxCurrent(double maxCur) throws IOException {
        if (getCurrent() > maxCur) {
            stop();
        }
            return (boolean) setAttributeValue(AgvAttribute.MAXCURRENT,maxCur);
    }

    /**
     * Get Max Current of Agv
     * @return value Max Current of Agv
     * @throws IOException Error get Max Current
     */
    public double getMaxCurrent() throws IOException {
        return (double) getAttributeValue(AgvAttribute.MAXCURRENT);
    }

    /**
     * Get Volume of C450
     * @param track 1: Start Volume, 2: Arlam Volume, 3: Warning Volume, 4: Swap volume
     * @return value volume C450
     * @throws IOException Error get Volume C450
     */
    public int getVolumeC450(int track) throws IOException {
        switch (track){
            case 1:
                return (int) getAttributeValue(AgvAttribute.START_VOLUME);
            case 2:
                return (int) getAttributeValue(AgvAttribute.ALARM_VOLUME);
            case 3:
                return (int) getAttributeValue(AgvAttribute.WARNING_VOLUME);
            case 4:
                return (int) getAttributeValue(AgvAttribute.SWAP_VOLUME);
            default:
                throw new TelegramException(443, "Unsupported Track");
        }
    }


    /**
     * Set Volume for track of C450
     * @param track 1: Start Volume, 2: Arlam Volume, 3: Warning Volume, 4: Swap volume
     * @param volume value Volume
     * @return State success or not
     * @throws IOException Error set Volume C450
     */
    public boolean setVolumeC450(int track,int volume) throws IOException {
        switch (track){
            case 1:
                return setAttributeValue(AgvAttribute.START_VOLUME,volume);
            case 2:
                return setAttributeValue(AgvAttribute.ALARM_VOLUME,volume);
            case 3:
                return setAttributeValue(AgvAttribute.WARNING_VOLUME,volume);
            case 4:
                return setAttributeValue(AgvAttribute.SWAP_VOLUME,volume);
            default:
                throw new TelegramException(443, "Unsupported Track");
        }
    }

    /**
     * Set Volum for track of Agv
     * @param track transmission in Track
     * @param volume transmission in Volume
     * @return State success or not
     * @throws IOException
     */
    public boolean setMp3Track(int track, int volume) throws IOException {
        boolean mp3Track = setAttributeValue(AgvAttribute.MP3_TRACK,track);
        boolean mp3Volume = setAttributeValue(AgvAttribute.MP3_VOLUME,volume);
        boolean mp3 = mp3Track && mp3Volume;
        return mp3;
    }

    /**
     * @return  Content card RFID
     * @throws IOException Error get Content
     */
    public String getRFID() throws IOException {
        return (String) getAttributeValue(AgvAttribute.RFID);
    }

    /**
     * Setting group and id of Agv
     * @param id transmission in id
     * @param group transmission in group
     * @return State success or not
     * @throws IOException Error set IdGroup
     */
    public boolean setIdGroup(int id, int group) throws IOException {
        return setAttributeValue(AgvAttribute.SYNC_ID_GROUP, new IdGroupAttribute.IdGroup(id, group));
    }

    /**
     *
     * @return IDGroup
     * @throws IOException Error get IDGroup
     */
    public IdGroupAttribute.IdGroup getIdGroup() throws IOException {
        return (IdGroupAttribute.IdGroup) getAttributeValue(AgvAttribute.SYNC_ID_GROUP);
    }

    /**
     *
     * @return Status of Pcb
     * @throws IOException Unsupported
     */
    public boolean getPcbStatus() throws IOException {
        throw new TelegramException(0x54, "Unsupported attribute");
    }

    /**
     * Warning fleet need to repair
     * @param alarm warning turn on or off
     * @return true if set alarm successfully
     * @throws IOException if got problem when set
     */
    public boolean setAlarm(boolean alarm) throws IOException {
        return setAttributeValue(AgvAttribute.IS_ALARM, alarm ? 1 : 0);
    }

    /**
     *
     * @return Is alarm
     * @throws IOException Error get Alarm
     */
    public boolean getAlarm() throws IOException {
        int isAlarm = (int) getAttributeValue(AgvAttribute.IS_ALARM);
        return isAlarm == 1;
    }

    /**
     *
     * @param level transmission in level
     * @return State success or not
     * @throws IOException
     */
    public boolean setLowBatteryLevel(int level) throws IOException {
        return setAttributeValue(AgvAttribute.LOW_BATTERY_LEVEL, level);
    }

    /**
     *
     * @return Low battery Level
     * @throws IOException Error get Low battery Level
     */
    public int getLowBatteryLevel() throws IOException {
        return (int) getAttributeValue(AgvAttribute.LOW_BATTERY_LEVEL);
    }

    /**
     *
     * @param level transmission in level
     * @return State success or not
     * @throws IOException Error set OutBatteryLevel
     */
    public boolean setOutBatteryLevel(int level) throws IOException {
        return setAttributeValue(AgvAttribute.OUT_BATTERY_LEVEL, level);
    }

    /**
     *
     * @return OutBatteryLevel
     * @throws IOException Error get OutBatteryLevel
     */
    public int getOutBatteryLevel() throws IOException {
        return (int) getAttributeValue(AgvAttribute.OUT_BATTERY_LEVEL);
    }
}
