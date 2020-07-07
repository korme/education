package korme.xyz.education.common.exceptionHandler;

import com.aliyun.oss.ClientException;
import korme.xyz.education.common.response.RespCode;
import korme.xyz.education.common.response.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/*
* 异常管理、校验管理
* */

@ControllerAdvice
@ResponseBody
public class GlobleExceptionHandler {
    //异常处理(@ControllerAdvice注解注释的controller层和此注解注释的方法,会对所有controller层抛出的异常进行统一处理)
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity ExceptionHandler(HttpServletRequest request, Exception e) {
        if (e instanceof ConstraintViolationException){
            ConstraintViolationException ex = (ConstraintViolationException)e;
            return new ResponseEntity(RespCode.ERROR_INPUT,ex.getMessage());
        }
        else if(e instanceof UnsupportedEncodingException){
            return new ResponseEntity(RespCode.ERROR_INPUT,"编码错误！");
        }
        else if(e instanceof ClientException){
            return new ResponseEntity(RespCode.ERROR_NETWORK);
        }
        else {

            //TODO:测试环境
            e.printStackTrace();
            return new ResponseEntity(RespCode.WRONG,e.toString());

            //TODO:生产环境
            //return new ResponseEntity(RespCode.WRONG);
        }
    }
}
