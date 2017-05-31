/**
 * 
 */
package com.github.rcf.core.server.process;

import com.github.rcf.core.bean.RcfRequest;
import com.github.rcf.core.bean.RcfResponse;

/**
 * @author winstone
 *
 */
public abstract class AbstractRcfRpcTcpServerHandler implements RpcServerHandler {

	public abstract RcfResponse handleRequest(RcfRequest request, int codecType, int procotolType );

}
