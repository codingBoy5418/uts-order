package org.uts.global.errorCode;

/**
 * @Description 业务错误码
 * @Author codBoy
 * @Date 2024/8/6 21:35
 */
public enum BusinessErrorCode {

    //******************************* 订单业务 ******************************//
    ORDER_IS_NOT_EXIST("020001", "订单不存在"),

    ORDER_QUERY_ILLEGAL("020002", "订单查询非法"),

    ;


    //错误码
    private String code;

    //错误消息
    private String errorMsg;

    BusinessErrorCode(String code, String errorMsg) {
        this.code = code;
        this.errorMsg = errorMsg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
