/**
 * 
 */
package com.github.rcf.tcp.demo.service;

import java.util.Map;

/**
 * @author winstone
 *
 */
public interface IDemoService {
	
	public String sayDemo(String params) ;
	
	public String getParam(String params);

	public String getParam(Map<String, Object> map);

	public Simple getSimple(Simple sim);


}
