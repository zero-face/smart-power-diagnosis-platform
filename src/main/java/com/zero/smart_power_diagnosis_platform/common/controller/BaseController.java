package com.zero.smart_power_diagnosis_platform.common.controller;



import com.zero.smart_power_diagnosis_platform.common.error.BusinessException;
import com.zero.smart_power_diagnosis_platform.common.error.EmBusinessError;
import com.zero.smart_power_diagnosis_platform.common.error.ErrorMsgType;
import com.zero.smart_power_diagnosis_platform.common.response.CommonReturnType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lmwis
 * @description:基本的controller类，异常处理和部分校验
 * @date 2019-08-28 20:27
 * @Version 1.0
 */
public class BaseController {
    protected static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    public static final String CONTENT_TYPE_FORMED = "application/x-www-form-urlencoded";

    // 定义exceptionHandler来解决controller层中未被吸收的exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CommonReturnType handlerException(HttpServletRequest request, Exception ex) {
        ErrorMsgType responseData ;
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            responseData = packErrorCommonReturnType(businessException.getErrorCode()
                    , businessException.getErrorMsg());
        } else if (ex instanceof HttpMessageNotReadableException) { // 序列化异常
            logger.error(ex.getMessage());
            responseData = packErrorCommonReturnType(EmBusinessError.JSON_SEQUENCE_WRONG.getErrorCode()
                    , EmBusinessError.JSON_SEQUENCE_WRONG.getErrorMsg());
        } else {
            logger.error(ex.getMessage());
            responseData = packErrorCommonReturnType(EmBusinessError.UNKNOWN_ERROR.getErrorCode()
                    , ex.getMessage());
        }
        logger.error("{"+responseData.toString()+"}");
        return CommonReturnType.fail(responseData);
    }

    protected ErrorMsgType packErrorCommonReturnType(int errorCode, String errorMsg){
        return new ErrorMsgType(errorCode,errorMsg);
    }


    /**
     * 判断字符串是否为空
     *
     * @param args 校验参数
     * @return
     * @throws BusinessException
     */
    protected boolean validateNull(String... args) throws BusinessException {
        for (String s : args) {
            if (StringUtils.isEmpty(s)) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
            }
        }
        return true;
    }

    /**
     * 判断字符串是否为空字符串，int or long是否为0，对象是否为null
     *
     * @param args 校验参数
     * @return
     * @throws BusinessException
     */
    protected boolean validateNull(Object... args) throws BusinessException {
        for (Object o : args) {
            if ((o instanceof String && StringUtils.equals(o.toString(), ""))
                    || (o instanceof Integer && new Integer(o.toString()) == 0)
                    || (o instanceof Long && new Long(o.toString()) == 0)
                    || o == null) {
                return false;
            }
        }
        return true;
    }

//    /**
//     * MetronicMeta 封装 ，产生表头
//     * 默认按照id升序排列
//     *
//     * @param pageable  分页器
//     * @param totalPage 总页数
//     * @param size      每一页数据条数
//     * @return
//     */
//    protected MetronicMeta generatorMeta(Pageable pageable, Integer totalPage, Integer size) {
//        MetronicMeta meta = new MetronicMeta();
//        meta.setPage(pageable.getPageNumber());
//        meta.setPerpage(pageable.getPageSize());
//        meta.setSort("asc");
//        meta.setTotal(size);
//        meta.setPages(totalPage);
//        meta.setField("id");
//        return meta;
//    }
}
