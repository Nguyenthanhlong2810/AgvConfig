package com.aubot.agv.ulti;

import com.aubot.agv.attributes.Attribute;
import com.aubot.agv.attributes.RFIDAttribute;
import com.aubot.agv.attributes.RfidMapAttribute;

/**
 * This class is used to create a modbus telegram handler using for communication
 *
 * @author Khoi
 * @version 1.0
 * @since 2021-04-19
 */
public class MixHandlerBuilder implements HandlerBuilder {

    @Override
    public TelegramHandler createHandler(Attribute attribute, HalfDuplexCommunication.Direction dir) {
        if(attribute instanceof RfidMapAttribute){
            return new RfidMapTelegramHandler((RfidMapAttribute) attribute);
        }else {
            return new ModbusTelegramHandler(attribute, dir);
        }
    }
}
