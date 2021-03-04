package com.examstack.common.util;


import java.io.Serializable;

/**
 * @Description: 出参对象
 * @Author: Wangzt
 * @CreateDate: 2019/8/16 19:43
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class OutputObject implements Serializable {

    /**
     * 响应码
     */
    private String respCode;
    /**
     * 响应信息
     */
    private String respMessage;
    /**
     * 响应数据
     */
    private Object data;

    public OutputObject(String respCode, String respMessage, Object data){

        this.respCode = respCode;
        this.respMessage = respMessage;
        this.data = data;

    }

    /**
     * toString
     *
     * @return
     */
    @Override
    public String toString() {
        return "OutputObject{" +
                "respCode='" + respCode + '\'' +
                ", respMessage='" + respMessage + '\'' +
                ", data=" + data +
                '}';
    }
}
