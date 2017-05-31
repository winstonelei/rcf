package com.github.rcf.core.client.invocation;

import com.github.rcf.client.service.api.ClientServiceApi;
import com.github.rcf.core.client.RcfRpcClient;
import com.github.rcf.core.client.factory.RcfRpcClientFactory;
import com.github.rcf.core.loadBlance.LoadBalance;
import com.github.rcf.core.loadBlance.RandomLoadBalance;
import com.github.rcf.core.route.RouteServer;
import com.github.rcf.core.util.SocketAddressUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public abstract  class AbstarctRcfInvocationHandler implements
        InvocationHandler  {

    private String group;

    private int timeout;

    private String targetInstanceName;

    private int codecType;

    private int protocolType;

    public AbstarctRcfInvocationHandler(
            String group, int timeout,
            String targetInstanceName, int codecType, int protocolType) {
        super();
        this.group = group;
        this.timeout = timeout;
        this.targetInstanceName = targetInstanceName;
        this.codecType = codecType;
        this.protocolType = protocolType;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        // TODO Auto-generated method stub
        RcfRpcClient client = null;

        String [] groups=group.split(",");

        Random r=new Random();

        int i=r.nextInt(groups.length);

        Set<InetSocketAddress> addresses = ClientServiceApi.getInstance().getServersByGroup(groups[i]);

        List<RouteServer> servers= SocketAddressUtil.getInetSocketAddress(addresses);

        //通过负载均衡算法实现
		/*r=new Random();
		int j=r.nextInt(servers.size());
		InetSocketAddress server = servers.get(j).getServer();
*/
        LoadBalance loadBalance = new RandomLoadBalance();
        RouteServer rpcRouteServer = loadBalance.select(servers,null);
        InetSocketAddress server = rpcRouteServer.getServer();

        client = getClientFactory().getClient(server.getAddress().getHostAddress(), server.getPort());
        String methodName = method.getName();
        String[] argTypes = createParamSignature(method.getParameterTypes());
        Object result= client.invokeImpl(targetInstanceName, methodName, argTypes, args, timeout, codecType, protocolType);
        System.out.println("得到结果="+result);

        System.out.println(server.getAddress().getHostAddress()+",port:"+server.getPort());
        return result;
    }

    private String[] createParamSignature(Class<?>[] argTypes) {
        if (argTypes == null || argTypes.length == 0) {
            return new String[] {};
        }
        String[] paramSig = new String[argTypes.length];
        for (int x = 0; x < argTypes.length; x++) {
            paramSig[x] = argTypes[x].getName();
        }
        return paramSig;
    }


    public abstract RcfRpcClientFactory getClientFactory();
}
