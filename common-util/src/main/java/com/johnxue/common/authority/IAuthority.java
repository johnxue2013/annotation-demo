package com.johnxue.common.authority;

import com.johnxue.common.bean.AuthorityInfo;

import java.util.List;

/**
 * @author han.xue
 * @since 2017-04-29 23:31：S
 */
public interface IAuthority {

    /**
     * 将解析获取的权限信息导出
     * @return 成功返回true，失败返回false
     */
    boolean export();

    /**
     * 获取所有权限信息
     * @return
     */
    List<AuthorityInfo> getAllAuthority();



}
