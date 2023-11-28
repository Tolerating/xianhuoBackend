package com.xianhuo.xianhuobackend.interceptor;

import com.xianhuo.xianhuobackend.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //跨域放行
        if ("OPTIONS".equals(request.getMethod().toUpperCase())){
            System.out.println("OPTOINS请求");
            return true;
        }

        // 获取请求头中的token
        String authorization = request.getHeader("authorization");
        String uri = request.getRequestURI();
        System.out.println(uri);
        // authorization为空或为携带
        if(authorization == null || authorization.isEmpty()){
            handleFalseResponse(response);
            return false;
        }
        Claims claims = JWTUtil.parseJWT(authorization);
        Date now = new Date();
        // token未过期，放行
        if (now.before(claims.getExpiration())) {
            return true;
        } else {
            //前端响应处理
            handleFalseResponse(response);
            return false;
        }
    }

    private void handleFalseResponse(HttpServletResponse response) throws Exception {
        response.setStatus(401);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\":\"token过期，请重新登录\",\"code\":\"401\"}");
        response.addHeader("Content-Type" ,
                "application/json; charset=utf-8");
        response.getWriter().flush();
    }
}
