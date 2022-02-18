package com.aubot.agv.ulti;
;
import com.aubot.agv.exception.TelegramException;

import java.io.IOException;

/**
 * This interface used to define the way to convert request to byte array for sending to device
 * and handle response match request and get data
 *
 * @author Khoi
 * @version 1.0
 * @since 2021-04-16
 */
public interface TelegramHandler {
    /**
     * Define the way to convert request frame to byte array
     *
     * @return request as byte array
     */
    byte[] toRequest() throws IOException;

    /**
     * Define the way to compare response with previous sent request
     *
     * @param response content of message return from connected device
     * @return true if response match request
     * @throws TelegramException if not match
     */
    boolean matchRequest(byte[] response) throws IOException;
}
