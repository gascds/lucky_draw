package com.lottery.Utils;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

    private static final String DATA_KEY;
    private static Map SUCCESS_MAP;
    private static Map BAD_REQUEST_MAP;
    private static Map FORBIDDEN_MAP;
    private static Map NOT_FOUND_MAP;
    private static Map INTERNAL_ERROR_MAP;

    static{
        DATA_KEY = "data";

        SUCCESS_MAP = new HashMap<String, Object>();
        SUCCESS_MAP.put("code", 200);
        SUCCESS_MAP.put("msg", "Success");

        BAD_REQUEST_MAP = new HashMap<String, Object>();
        BAD_REQUEST_MAP.put("code", 400);
        BAD_REQUEST_MAP.put("msg", "Invalid Query");
        BAD_REQUEST_MAP.put(DATA_KEY, null);

        FORBIDDEN_MAP = new HashMap<String, Object>();
        FORBIDDEN_MAP.put("code", 403);
        FORBIDDEN_MAP.put("msg", "Invalid Ticket");
        FORBIDDEN_MAP.put(DATA_KEY, null);

        NOT_FOUND_MAP = new HashMap<String, Object>();
        NOT_FOUND_MAP.put("code", 404);
        NOT_FOUND_MAP.put("msg", "Request URL Not Found");
        NOT_FOUND_MAP.put(DATA_KEY, null);

        INTERNAL_ERROR_MAP = new HashMap<String, Object>();
        INTERNAL_ERROR_MAP.put("code", 500);
        INTERNAL_ERROR_MAP.put("msg", "Internal Server Error");
        INTERNAL_ERROR_MAP.put(DATA_KEY, null);
    }

    public static Map<String, Object> buildOk(Object data){
        Map<String, Object> map = new HashMap<>();
        map.putAll(SUCCESS_MAP);
        map.put(DATA_KEY, data);
        return map;
    }

    public static Map<String, Object> buildBadRequestError(){
        return BAD_REQUEST_MAP;
    }

    public static Map<String, Object> buildForBiddenError(){
        return FORBIDDEN_MAP;
    }

    public static Map<String, Object> buildNotFoundError(){
        return NOT_FOUND_MAP;
    }

    public static Map<String, Object> buildInternalError(){
        return INTERNAL_ERROR_MAP;
    }

}
