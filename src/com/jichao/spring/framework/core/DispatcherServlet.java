package com.jichao.spring.framework.core;

import com.jichao.spring.framework.annotation.Controller;
import com.jichao.spring.framework.annotation.RequestMapping;
import com.jichao.spring.framework.scanner.PackageScanner;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Title：核心控制器
 *
 * @author jichao
 * @date 2018-06-05
 */
@WebServlet(urlPatterns = {"*.do"}, loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    private final static String BASE_PACKAGE = "com.jichao.test";

    private Map<String, Object> classInstanceMap = new HashMap<>();

    private Map<String, Method> methodMap = new HashMap<>();

    public void init() {
        System.out.println("========== jc-spring 开始将Bean扫描到内存中 ==========");
        Map<String, Class<?>> classes = PackageScanner.scanner(BASE_PACKAGE);
        classes.forEach((k, v) -> {
            if (v.isAnnotationPresent(Controller.class)) {
                try {
                    String path = "";
                    if (v.isAnnotationPresent(RequestMapping.class)) {
                        path = ((RequestMapping) v.getAnnotation(RequestMapping.class)).value();
                    }

                    classInstanceMap.put(k, v.newInstance());

                    Method[] methods = v.getMethods();
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            methodMap.put(path + ((RequestMapping) method.getAnnotation(RequestMapping.class)).value(), method);
                        }
                    }
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("========== jc-spring Bean扫描结束 ==========");
    }

    public void service(HttpServletRequest request, HttpServletResponse response) {
        //当前应用的根目录
        String contextPath = request.getContextPath();
        //请求的相对地址
        String uri = request.getRequestURI();

        String mappingPath = uri.substring(uri.indexOf(contextPath) + contextPath.length(), uri.indexOf(".do"));
        Method method = methodMap.get(mappingPath);

        try {
            method.invoke(classInstanceMap.get(method.getDeclaringClass().getName()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
