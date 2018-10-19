package com.xy.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet Filter implementation class Myfilter
 */
public class Myfilter implements Filter {

    /**
     * Default constructor. 
     */
    public Myfilter() {
        System.out.println("全局过滤器开启");
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		System.out.println("全局过滤器销毁");
	}

	/**
	 * @see 
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
	    HttpServletRequest re = (HttpServletRequest) request;
	    System.out.println("--" + re.getRequestURL());
	    request.setAttribute("ctx", re.getContextPath());
	    System.out.println();
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	    System.out.println("全局过滤器初始化");
	}

}
