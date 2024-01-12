package com.transferz.rc;

public record ResponseCode(
        int response,
        String message
) {
    public static final ResponseCode SUCCESS = new ResponseCode(0, "Success");


    public static final ResponseCode FAILURE = new ResponseCode(1, "Failed");
}
