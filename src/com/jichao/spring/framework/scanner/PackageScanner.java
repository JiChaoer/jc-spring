package com.jichao.spring.framework.scanner;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Title：扫描工具类
 *
 * @author jichao
 * @date 2018-05-25
 */
public class PackageScanner {

    /**
     * 扫描器
     *
     * @param packageName
     * @return Map<全限定类名, 类的反射实例>
     */
    public static Map<String, Class<?>> scanner(String packageName) {
        Map<String, Class<?>> classes = new HashMap<String, Class<?>>();
        packageName = packageName.replace(".", "/");
        try {
            //根目录
            String root = packageName;
            //所要扫描的根目录下所有的目录
            Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageName);
            //遍历所有目录进行类扫描
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                if (url.getProtocol().equals("file")) {
                    //越过路径URL前的斜杠-> 例子： file:/E:/jc-spring/.. 从E:/jc-spring/..作为文件路径
                    File folder = new File(url.getPath().substring(1));
                    recursiveScanFolder(folder, root, classes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classes;
    }

    /**
     * 递归扫描指定文件夹下所有类
     *
     * @param folder
     * @param root
     * @param classes
     * @return
     */
    public static Map<String, Class<?>> recursiveScanFolder(File folder, String root, Map<String, Class<?>> classes) {
        File[] files = folder.listFiles();
        root = root.replace("/", "\\");
        for (File file : files) {
            if (file.isDirectory()) {
                recursiveScanFolder(file, file.getPath().substring(file.getPath().lastIndexOf(root)), classes);
            } else if (file.getPath().endsWith(".class")) {
                String className = file.getPath().substring(file.getPath().lastIndexOf(root), file.getPath().lastIndexOf(".class"));
                className = className.replace("\\", ".");
                try {
                    classes.put(className, Class.forName(className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return classes;
    }
}
