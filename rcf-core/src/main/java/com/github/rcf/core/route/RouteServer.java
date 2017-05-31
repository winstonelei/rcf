package com.github.rcf.core.route;

import java.io.Serializable;
import java.net.InetSocketAddress;

/**
 * Created by winstone on 2017/5/27.
 */
public class RouteServer implements Serializable {

    private static final long serialVersionUID = -5830467756387170272L;

    public InetSocketAddress server ;

    public int weight;


    public RouteServer(InetSocketAddress server, int weight) {
        this.server = server;
        this.weight = weight;
    }

    public InetSocketAddress getServer() {
        return server;
    }

    public void setServer(InetSocketAddress server) {
        this.server = server;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }


}
