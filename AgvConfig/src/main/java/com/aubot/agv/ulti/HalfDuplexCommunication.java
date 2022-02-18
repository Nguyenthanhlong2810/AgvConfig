package com.aubot.agv.ulti;

import java.io.IOException;

/**
 * This interface is used to communicate with AGV
 * This communication is half-duplex (or semi-duplex)
 *
 * A half-duplex (HDX) system provides communication in both directions, but only one direction
 * at a time, not simultaneously in both directions. Typically, once a party begins receiving a
 * signal, it must wait for the transmission to complete, before replying.
 *
 * @author Khoi
 * @version 1.0
 * @since 2021-04-16
 */
public interface HalfDuplexCommunication {
    /**
     * This method define how to communication
     * Communication works in a way: a request is sent to the connected device, then device send
     * request will wait for response
     *
     * @param request content of request message
     * @return response which connected device send back
     * @throws IOException if got a problem when communicate. Time out waiting for example
     */
    byte[] communicate(byte[] request, int required) throws IOException;

    /**
     * Communication request exists two direction: READ, and WRITE
     * READ mean system request to connected device fetch some specifications and send back to system
     * WRITE mean system will set one (or some) configuration for connected device
     */
    enum Direction {
        READ,
        WRITE;
    }
}
