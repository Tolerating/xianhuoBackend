package com.xianhuo.xianhuobackend.interceptor;

import com.xianhuo.xianhuobackend.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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
            handleNotLoginResponse(response);
            return false;
        }

        // token未过期，放行
        try{
            Claims claims = JWTUtil.parseJWT(authorization);
            Date now = new Date();
            if (now.before(claims.getExpiration())) {
                return true;
            }
        }catch (ExpiredJwtException e){
            handleFalseResponse(response);
            return false;

        }
        return true;
    }

//    处理token 过期
    private void handleFalseResponse(HttpServletResponse response) throws Exception {
        response.setStatus(401);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\":\"token过期，请重新登录\",\"code\":\"401\"}");
        response.addHeader("Content-Type" ,
                "application/json; charset=utf-8");
        response.getWriter().flush();
    }

    private void handleNotLoginResponse(HttpServletResponse response) throws Exception {
        response.setStatus(402);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\":\"请登录\",\"code\":\"402\"}");
        response.addHeader("Content-Type" ,
                "application/json; charset=utf-8");
        response.getWriter().flush();
    }
}
