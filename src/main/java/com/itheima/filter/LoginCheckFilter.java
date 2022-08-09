package com.itheima.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.common.R;
import com.itheima.utils.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author mimo
 * @description 检查用户是否登录
 * @date 2022-08-04 17:47
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*" )
public class LoginCheckFilter implements Filter {

    //定义路径匹配器
    public static final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        //request response 转型
        HttpServletRequest request =(HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1.获取本次请求的uri
        String requestURI = request.getRequestURI();

        log.info("本次请求的url:{}",requestURI);

        //不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };

        //2.判断是否需要处理
        if (check(urls,requestURI)){
            log.info("不需要处理的url:{}: ",requestURI);
            //拦截器工作放行
            filterChain.doFilter(request,response);
            return;
        }
        //判断session里面有没有值
       if (request.getSession().getAttribute("employee") != null){
           //将id放入线程中
           Long empId = (Long)request.getSession().getAttribute("employee");
           BaseContext.setCurrentId(empId);
           //拦截器工作放行
           filterChain.doFilter(request,response);
           return;
       }
        //判断session里面有没有值
        if (request.getSession().getAttribute("user") != null){
            //将id放入线程中
            Long userId = (Long)request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            //拦截器工作放行
            filterChain.doFilter(request,response);
            return;
        }
       log.info("用户未登录!");
       //如果data.msg == NOTLoGIN and status == 0 则前端自动跳转
       response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
       return;

    }
    //实际判断是否有对应请求
    public static boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = matcher.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
