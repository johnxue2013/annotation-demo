package com.johnxue.common.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 权限信息bean
 *
 * @author han.xue
 * @since 2017-04-30 09:52：S
 */
public class AuthorityInfo {

    public static final String METHOD_POST = "POST";
    public static final String METHOD_GET = "GET";

    /**
     * 权限的描述
     */
    private String description;
    /**
     * 权限路径
     */
    private String url;
    /**
     * 请求方式
     */
    private String method;

    public AuthorityInfo() {
    }

    public AuthorityInfo(String description, String url, String method) {
        this.description = description;
        this.url = url;
        this.method = method;
    }

    public AuthorityInfo(String description, String url) {
        this.description = description;
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,ToStringStyle.NO_CLASS_NAME_STYLE);

    }
}
