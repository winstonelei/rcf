package com.github.rcf.http.server.bean;

import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultHttpResponse;

import java.io.Serializable;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class HttpSeverBean implements Serializable {

    private static final long serialVersionUID = 1066483530993789009L;

    private DefaultHttpResponse response;

    private DefaultHttpContent defaultHttpContent;

    private boolean keepAlive;

    /**
     * @return the response
     */
    public DefaultHttpResponse getResponse() {
        return response;
    }


    public HttpSeverBean(DefaultHttpResponse response,
                         DefaultHttpContent defaultHttpContent, boolean keepAlive) {
        super();
        this.response = response;
        this.defaultHttpContent = defaultHttpContent;
        this.keepAlive = keepAlive;
    }


    /**
     * @param response the response to set
     */
    public void setResponse(DefaultHttpResponse response) {
        this.response = response;
    }

    /**
     * @return the defaultHttpContent
     */
    public DefaultHttpContent getDefaultHttpContent() {
        return defaultHttpContent;
    }

    /**
     * @param defaultHttpContent the defaultHttpContent to set
     */
    public void setDefaultHttpContent(DefaultHttpContent defaultHttpContent) {
        this.defaultHttpContent = defaultHttpContent;
    }


    /**
     * @return the keepAlive
     */
    public boolean isKeepAlive() {
        return keepAlive;
    }


    /**
     * @param keepAlive the keepAlive to set
     */
    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }




}
