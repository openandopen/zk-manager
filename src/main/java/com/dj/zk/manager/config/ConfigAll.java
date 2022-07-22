package com.dj.zk.manager.config;

import com.dj.zk.manager.config.prop.ZkProperties;
import com.dj.zk.manager.exceptions.GlobalExceptionHandler;
import com.dj.zk.manager.web.filter.AuthFilter;
import com.dj.zk.manager.web.interceptors.UserOptLogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * <p>
 *
 * @author : <a href="mailto:liudejian@tinman.cn">liudejian</a>
 * @version : Ver 1.0
 * @date : 2019-12-04 13:05
 */
@Configuration
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableConfigurationProperties(value = {ZkProperties.class})
@ComponentScan(basePackages = {"com.dj.zk.manager"})
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class},
        scanBasePackages = {"com.dj.zk.manager.action"})
public class ConfigAll implements WebMvcConfigurer  {

    @Autowired
    private ZkProperties zkProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new UserOptLogInterceptor(zkProperties));
        registration.addPathPatterns("/**");
        registration.order(Integer.MIN_VALUE);
        registration.pathMatcher(new AntPathMatcher());
    }

    @Bean
    public CookieLocaleResolver cookieLocaleResolver() {
        return new CookieLocaleResolver();
    }

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }



    @Bean
    public FilterRegistrationBean ucClientFilter() {
        AuthFilter authFilter = new AuthFilter();
        FilterRegistrationBean registration = new FilterRegistrationBean(authFilter);
        String excludeStr = "*.js;*.jpg;*.htm;*.html;*.gif;*.png;*.css;*.swf";
        registration.addInitParameter("excludeUrls",excludeStr);
        registration.addUrlPatterns("/*");
        return registration;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/script/**")
                .addResourceLocations("classpath:/META-INF/resources/script/");

    }


   /*  @Bean
    public ServletRegistrationBean servletRegistrationBean(DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean bean = new ServletRegistrationBean(dispatcherServlet);
        List<String> lists = new ArrayList<>();
        lists.add("*.do");
        lists.add("*.json");
        bean.setUrlMappings(lists);
        return bean;
    }*/

}
