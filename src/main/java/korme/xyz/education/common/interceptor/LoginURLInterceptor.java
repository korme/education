package korme.xyz.education.common.interceptor;

import korme.xyz.education.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginURLInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //todo:生产环境取消注释
        /*String uri = request.getRequestURI();
        Object object = request.getSession().getAttribute("userId");
        if (null == object){
            response.sendRedirect(request.getContextPath()+"/user/loginError");
            return false;
        }
        */
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null){
            if(! (modelAndView.getView() instanceof RedirectView)){
                String basePath = request.getContextPath();
                //modelAndView.addObject("basePath",basePath);
                request.setAttribute("basePath",basePath);
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        System.out.println("ex is" + e);
    }
}
