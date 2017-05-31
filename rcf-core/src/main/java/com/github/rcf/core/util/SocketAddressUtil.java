package com.github.rcf.core.util;


import com.github.rcf.core.route.RouteServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SocketAddressUtil {
	
	public static List<RouteServer> getInetSocketAddress(Set<InetSocketAddress>addresses){
		List<RouteServer> routeServers=new ArrayList<RouteServer>();
		
		for(InetSocketAddress inetSocketAddress:addresses){
			RouteServer rpcRouteServer=new RouteServer(inetSocketAddress, 1);
			routeServers.add(rpcRouteServer);
		}
		return routeServers;
	}
}
