package com.examstack.common.util;


/**
 * 响应码
 *
 * @author Jamie Han
 */
public class ReturnCode {

    /**
     * 成功
     */
    public static final String SUCCESS = "200";

    /**
     * 失败
     */
    public static final String FAIL = "400";

    /**
     * 未签证
     */
    public static final String UNAUTHORIZED = "401";

    /**
     * 未找到接口
     */
    public static final String NOT_FOUND = "404";

    /**
     * 服务器内部错误
     */
    public static final String INTERNAL_SERVER_ERROR = "500";

}
