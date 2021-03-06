package com.github.rcf.core.compiler;

import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.net.URI;

/**
 * Created by winstone on 2017/6/21.
 */
public class StringJavaFileObject  extends SimpleJavaFileObject{

    private static final String SCHEME = "string:///";
    private final String code;

    protected StringJavaFileObject(String className, String code) {
        super(URI.create(SCHEME + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return code;
    }
}
