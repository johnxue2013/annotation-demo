package com.johnxue.common.authority;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Controller;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import com.johnxue.common.bean.AuthorityInfo;
import com.johnxue.common.config.AuthorityConfig;
import com.johnxue.common.util.ClassUtil;

/**
 * @author han.xue
 * @since 2017-04-30 10:02：S
 */
public abstract class AbstractAuthority implements IAuthority {


    @Override
    public boolean export() {
        List<AuthorityInfo> allAuthority = getAllAuthority();
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

            for (AuthorityInfo authorityInfo : allAuthority) {
                Files.append(authorityInfo.toString() + "\n", file, Charset.forName("UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
     * @param classes class
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

}
