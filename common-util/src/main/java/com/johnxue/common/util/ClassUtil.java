package com.johnxue.common.util;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.johnxue.common.config.AuthorityConfig;

/**
 * class util
 *
 * @author han.xue
 * @since 2017-04-30 10:03：S
 */
public class ClassUtil
{

	private static String tmpPath = "";

	static
	{
		try
		{
			URL url = Thread.currentThread().getContextClassLoader().getResource("");
            Preconditions.checkNotNull(url, "获取项目位置失败");
			File file = new File(url.toURI());
            tmpPath = file.getAbsolutePath() + "\\";
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
	}

	public static List<Class> getAllClasses() throws IOException, ClassNotFoundException
	{

		List<Class> classes = new ArrayList<>();

		for (String item : AuthorityConfig.basePackages)
		{
			item = item.replace(".", "/");
			File rootFile = new File((tmpPath + item).replace("%20", " ").replace("\\", "/"));
			for (String fullName : listFiles(rootFile, new ArrayList<String>()))
			{
				fullName = fullName.replace("/", ".").replace("\\", ".");
				Class<?> loadClass = Thread.currentThread().getContextClassLoader().loadClass(fullName);
				boolean isInterface = loadClass.isInterface();
				if (!isInterface)
					classes.add(loadClass);
			}
		}
		return classes;
	}

	/**
	 * 获取所有文件列表
	 *
	 * @param rootFile 需要遍历的文件
	 * @param fileList 结果集
	 * @throws IOException 文件不存在时抛出此exception
	 */
	private static List<String> listFiles(File rootFile, List<String> fileList) throws IOException
	{
		Preconditions.checkNotNull(rootFile, "加载class出错");
		File[] allFiles = rootFile.listFiles();
		Preconditions.checkNotNull(allFiles, "加载class出错");
		for (File file : allFiles)
		{
			if (file.isDirectory())
			{
				listFiles(file, fileList);
			}
			else
			{
				String path = file.getCanonicalPath();
				String clazz = path.substring(tmpPath.length());
				fileList.add(clazz.replace("//", ".").substring(0, clazz.lastIndexOf(".")));
			}
		}
		return fileList;
	}
}
