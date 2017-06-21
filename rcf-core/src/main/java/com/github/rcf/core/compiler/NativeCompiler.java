package com.github.rcf.core.compiler;

import javax.tools.*;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by winstone on 2017/6/21.
 */
public class NativeCompiler implements Closeable {

    private final File tempFileFolder;

    private final URLClassLoader classLoader;


    public NativeCompiler(File tempFileFolder){
        this.tempFileFolder = tempFileFolder;
        this.classLoader = createClassLoader(tempFileFolder);
    }


    private static URLClassLoader createClassLoader(File tempLoader){
        try{
           URL[] urls = {tempLoader.toURI().toURL()};
           return new URLClassLoader(urls);
        }catch (Exception e){
            throw new AssertionError(e);
        }
    }

   public Class<?> compile(String className, String javaCode){
        try{
            JavaFileObject sourceFile = new StringJavaFileObject(className,javaCode);
            compileClass(sourceFile);
            return classLoader.loadClass(className);
        }catch (Exception e){
            throw new AssertionError(e);
        }
    }


    private void compileClass(JavaFileObject sourceFile) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = null;
        try {
            fileManager = compiler.getStandardFileManager(collector, Locale.ROOT, null);
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(tempFileFolder));
            JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, collector, null, null, Arrays.asList(sourceFile));
            task.call();
        } finally {
            fileManager.close();
        }
    }


    @Override
    public void close() {
        try {
            classLoader.close();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }


}
