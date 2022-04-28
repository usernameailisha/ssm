package com.wh.demo.interceptor;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.pagehelper.util.StringUtil;
import com.wh.demo.constant.SystemConstant;
import com.wh.demo.entity.CheckResult;
import com.wh.demo.utils.JwtUtils;
import com.wh.demo.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static sun.misc.Version.print;

/**
 * 自定义的拦截器，用来验证token
 */
public class SysInterceptor implements HandlerInterceptor {

    //使用日志
    private final static Logger logger = LoggerFactory.getLogger(SysInterceptor.class);

    //在请求到达控制器之前，进行拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("执行了拦截的核心方法");
        //获取页面的请求地址
        String contextPath = request.getRequestURI();
        logger.info("路径"+contextPath);
        //1.验证请求是否是请求方法的类型
        if(handler instanceof HandlerMethod){
            //2.从请求中获取token
            String token = request.getHeader("token");
            System.out.println("token = " + token);
            //3.验证token是否为空
            if(StringUtils.isEmpty(token)){
                logger.info("验证失败！");
                print(response, R.error(SystemConstant.JWT_ERRCODE_NULL,"签名验证不存在！"));
                return false;
            }else{
                //4.解析验证token
                CheckResult checkResult = JwtUtils.validateJWT(token);
                //5.如果验证成功
                if(checkResult.isSuccess()){
                    //通过拦截器
                    logger.info("签名验证通过...");
                    return true;
                }else{
                    //6.验证不成功
                    switch (checkResult.getErrCode()){
                        case SystemConstant.JWT_ERRCODE_EXPIRE :
                            logger.info("签名失效！");
                            print(response,R.error(SystemConstant.JWT_ERRCODE_EXPIRE,"签名失效！"));
                            break;
                        case SystemConstant.JWT_ERRCODE_FAIL :
                            logger.info("签名验证不通过！");
                            print(response,R.error(SystemConstant.JWT_ERRCODE_FAIL,"签名验证不通过！"));
                            break;
                        default: break;
                    }
                    return false;
                }
            }
        }
        //跨越请求第一次不经过if
        return true;
    }

    //自定义响应方法，将请求处理结果响应回浏览器
    public void print(HttpServletResponse response, Object message) {
        try {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setHeader("Cache-Control", "no-cache, must-revalidate");
            PrintWriter writer = response.getWriter();
            writer.write(message.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

