package com.xy.util;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;

/**
 * 
 * RequestUtil
 * request 请求工具
 * @version 1.0
 * @since JDK1.8
 * @author yanyahui
 * @company 洛阳矩阵软件有限公司
 * @copyright (c) 2018 lymatrix Co'Ltd Inc. All rights reserved.
 * @date 2018年10月18日 上午11:24:15
 */
public class RequestUtil {
    public static HttpServletRequest getRequest() {
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return req;
    }

    public static boolean isAjax(HttpServletRequest request) {
        String accept = request.getHeader("accept");
        if (accept != null && accept.indexOf("application/json") != -1) {
            return true;
        }

        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1) {
            return true;
        }
        return false;
    }

    /**
     * 
     * 获取请求ip
     *
     * @return
     *
     */
    public static String getIpAddress() {
        HttpServletRequest request = getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("http_client_ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
        }
        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    /**
     * 简单的封装Request参数对象
     */
    @SuppressWarnings("rawtypes")
    public static Dict req2Map(HttpServletRequest request) {
        Dict param = Dict.create();
        Enumeration pe = request.getParameterNames();
        while (pe.hasMoreElements()) {
            String k = pe.nextElement().toString();
            String v = request.getParameter(k);
            param.put(k, (StrUtil.isNotBlank(v) ? v : null));
        }
        return param;
    }
}
