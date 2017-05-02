package com.johnxue.common.authority;

import com.johnxue.common.annotation.Authority;
import com.johnxue.common.bean.AuthorityInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author han.xue
 * @since 2017-04-30 12:06：S
 */
public class DefaultAuthorityAdapter extends AbstractAuthority {

    @Override
    protected List<AuthorityInfo> doGetAuthorityInfo(List<Class> classes) {
        List<AuthorityInfo> result = new ArrayList<>();
        for (Class clazz : classes) {

            String parentUrl = "/";
            boolean annotationed = clazz.isAnnotationPresent(RequestMapping.class);
            if(annotationed) {
                RequestMapping requestMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
                //TODO 此处由于作者比较懒，只支持单一请求路径
                String[] value = requestMapping.value();
                if (value.length > 0) {
                    parentUrl += value[0] + "/";
                }

            }

            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class) && method.isAnnotationPresent(Authority.class)) {

                    AuthorityInfo authorityInfo = new AuthorityInfo();

                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    Authority authority = method.getAnnotation(Authority.class);
                    //TODO 此处由于作者比较懒，只支持单一的请求方法
                    RequestMethod[] requestMethods = requestMapping.method();
                    if (requestMethods.length == 0) { //未指定请求方式，则默认GET POST两者都可以
                        authorityInfo.setMethod(AuthorityInfo.METHOD_GET + "/" + AuthorityInfo.METHOD_POST);
                    } else {
                        RequestMethod requestMethod = requestMethods[0];
                        if (requestMethod.compareTo(RequestMethod.POST) < 0) {
                            authorityInfo.setMethod(AuthorityInfo.METHOD_GET);
                        } else {
                            authorityInfo.setMethod(AuthorityInfo.METHOD_POST);
                        }
                    }

                    //TODO 此处由于作者比较懒，只支持单一请求路径
                    String[] value = requestMapping.value();
                    if (value.length > 0) {
                        authorityInfo.setUrl(parentUrl + value[0]);
                    } else {//未指定，则设置为方法名称
                        authorityInfo.setUrl(parentUrl + method.getName());
                    }

                    String description = null;
                    try {
                        description = new String(authority.description().getBytes(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    authorityInfo.setDescription(description);

                    result.add(authorityInfo);
                }
            }
        }

        return result;
    }
}
