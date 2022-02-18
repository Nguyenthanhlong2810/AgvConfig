package com.aubot.agv.ulti;

import com.aubot.agv.attributes.Attribute;

/**
 * This interface define the way to create a telegram handler for handling request and response
 * in half-duplex communication
 *
 * @author Khoi
 * @version 1.0
 * @since 2021-04-19
 */
public interface HandlerBuilder {

    TelegramHandler createHandler(Attribute attribute,
                                  HalfDuplexCommunication.Direction dir);
}
