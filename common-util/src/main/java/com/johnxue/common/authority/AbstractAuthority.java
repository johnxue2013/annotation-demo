package com.johnxue.common.authority;

import com.johnxue.common.bean.AuthorityInfo;
import com.johnxue.common.util.ClassUtil;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author han.xue
 * @since 2017-04-30 10:02：S
 */
public abstract class AbstractAuthority implements IAuthority {


    @Override
    public boolean export() {
        List<AuthorityInfo> allAuthority = getAllAuthority();
        return doExport(allAuthority);
    }

    @Override
    public List<AuthorityInfo> getAllAuthority() {
        List<Class> allClasses = null;
        try {
            allClasses = ClassUtil.getAllClasses();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        List<AuthorityInfo> authorityInfos = new ArrayList<>();
        if (allClasses == null) {
            return authorityInfos;
        }

        //过滤掉不是Controller的类
        filter(allClasses, Controller.class);

        return doGetAuthorityInfo(allClasses);
    }

    /**
     * filter
     *
     * @param classes    class
     * @param annotation 需要的注解，不是次注解的class将被过滤掉
     */
    private void filter(List<Class> classes, Class<? extends Annotation> annotation) {
        Iterator<Class> iterator = classes.iterator();
        while (iterator.hasNext()) {
            Class clazz = iterator.next();
            if (!clazz.isAnnotationPresent(annotation)) {
                iterator.remove();
            }
        }
    }

    /**
     * 具体获取权限信息交给子类去实现
     *
     * @return List
     */
    protected abstract List<AuthorityInfo> doGetAuthorityInfo(List<Class> classes);

    /**
     * 具体的导出交给子类实现
     *
     * @param authorityInfos 需要导出的权限信息
     * @return 导出成功返回true，失败则返回false
     */
    protected abstract boolean doExport(List<AuthorityInfo> authorityInfos);

}
