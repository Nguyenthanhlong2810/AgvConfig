package com.aubot.agv.exception;

import java.io.IOException;

public class TelegramException extends IOException {

    private int errCode;

    public TelegramException(int errCode) {
        this.errCode = errCode;
    }

    public TelegramException(int errCode, String message) {
        super(message);
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }
}
