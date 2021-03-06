package com.zero.smart_power_diagnosis_platform.common.error;

/**
 * @author lmwis
 * @description:通用错误接口
 * @date 2019-08-28 20:29
 * @Version 1.0
 */
public interface CommonError {
    public int getErrorCode();

    public String getErrorMsg();

    public CommonError setErrMsg(String errMsg);
}
