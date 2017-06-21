package com.github.rcf.core.compiler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by winstone on 2017/6/21.
 */
public abstract  class AbstractAccessAdaptive implements  Compiler {

    private static final Pattern PAKAGE_PATTERN = Pattern.compile("package\\s+([$_a-zA-Z][$_a-zA-Z0-9\\.]*);");
    
    
    private static final Pattern CLASS_PATTERN = Pattern.compile("class\\s+([$_a-zA-Z][$_a-zA-Z0-9]*)\\s+");


    protected ClassLoader overrideThreadContextClassLoader(ClassLoader loader) {
        Thread currentThread = Thread.currentThread();
        ClassLoader threadContextClassLoader = currentThread.getContextClassLoader();
        if (loader != null && !loader.equals(threadContextClassLoader)) {
            currentThread.setContextClassLoader(loader);
            return threadContextClassLoader;
        } else {
            return null;
        }
    }

    protected ClassLoader getClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
        }
        if (cl == null) {
            cl = AbstractAccessAdaptive.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                }
            }
        }
        return cl;
    }

    private String report(Throwable e) {
        StringWriter w = new StringWriter();
        PrintWriter p = new PrintWriter(w);
        p.print(e.getClass().getName() + ": ");
        if (e.getMessage() != null) {
            p.print(e.getMessage() + "\n");
        }
        p.println();
        try {
            e.printStackTrace(p);
            return w.toString();
        } finally {
            p.close();
        }
    }


    @Override
    public Class<?> compile(String javaCode, ClassLoader classloader) {
       javaCode = javaCode.trim();
       //校验javacode package
        Matcher matcher = PAKAGE_PATTERN.matcher(javaCode);
        String pkg;
        if(matcher.find()){
            pkg = matcher.group(1);
        }else{
            pkg = "";
        }
        matcher = CLASS_PATTERN.matcher(javaCode);
        String cls;
        if(matcher.find()){
            cls = matcher.group(1);
        }else{
            throw  new IllegalArgumentException("no such class name in "+ javaCode );
        }
        String className = pkg!=null&& pkg.length()>0 ? pkg+"."+cls:cls;
        try{
            return Class.forName(className,true,(classloader!=null?classloader:getClassLoader()));
        }catch(ClassNotFoundException e){
            if (!javaCode.endsWith("}")) {
                throw new IllegalStateException("the java code not ends with \"}\", code: \n" + javaCode + "\n");
            }
            try {
                return doCompile(className, javaCode);
            } catch (RuntimeException t) {
                throw t;
            } catch (Throwable t) {
                throw new IllegalStateException("failed to compile class, cause: " + t.getMessage() + ", class: " + className + ", code: \n" + javaCode + "\n, stack: " + report(t));
            }
        }

    }


    protected abstract  Class<?> doCompile(String clsName, String javaSource) throws Throwable;

}
