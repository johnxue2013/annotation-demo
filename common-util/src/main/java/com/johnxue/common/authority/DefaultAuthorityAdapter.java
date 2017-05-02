package com.johnxue.common.authority;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import com.johnxue.common.config.AuthorityConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.johnxue.common.annotation.Authority;
import com.johnxue.common.bean.AuthorityInfo;

/**
 * @author han.xue
 * @since 2017-04-30 12:06：S
 */
public class DefaultAuthorityAdapter extends AbstractAuthority {


    @Override
    protected List<AuthorityInfo> doGetAuthorityInfo(List<Class> classes) {

        //如果有需要可以考虑使用缓存，而不是每次都去load class，此处暂未使用缓存
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

//                    String description = null;
//                    try {
//                        description = new String(authority.description().getBytes(), "UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }

                    String description =authority.description();
                    authorityInfo.setDescription(description);

                    result.add(authorityInfo);
                }
            }
        }

        return result;
    }

    @Override
    protected boolean doExport(List<AuthorityInfo> authorityInfos) {
        Preconditions.checkNotNull(AuthorityConfig.destination, "输出文件路径不能为空");
        File file = new File(AuthorityConfig.destination);
        try {
            if (file.exists()) {
                boolean success = file.delete();
                if (!success) {//删除文件失败
                    return false;
                }
            } else {
                //判断目标文件所在的目录是否存在
                if(!file.getParentFile().exists()) {
                    //如果目标文件所在的目录不存在，则创建父目录
                    if(!file.getParentFile().mkdirs()) {
                        return false;//创建目标文件所在目录失败！
                    }
                }
                //创建目标文件
                boolean success = file.createNewFile();
                if (!success) {//创建文件失败
                    return false;
                }
            }

            for (AuthorityInfo authorityInfo : authorityInfos) {
                Files.append(authorityInfo.toString() + "\n", file, Charset.forName("UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
