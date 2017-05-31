package com.github.rcf.http.impl; /**
 * 
 */

import com.github.rcf.http.IDemoService;

import java.util.List;
import java.util.Map;

/**
 * @author winstone
 *
 * 访问地址
 * http://127.0.0.1:10009/Demo/demoServiceImpl/sayDemo?name=zhangsan&age=18
 */
public class DemoServiceImpl implements IDemoService {

	/* (non-Javadoc)
	 * @see test.cross.plateform.service.IDemoService#sayDemo(java.lang.String, java.lang.String)
	 */
	@Override
	public String sayDemo(String name, String age) {
		// TODO Auto-generated method stub
		System.out.println("得到的值====》》》》"+name+"--------"+age);
		return name+"--------"+age;
	}

	/* (non-Javadoc)
	 * @see test.cross.plateform.service.IDemoService#sayDemoByParamsMap(java.util.Map)
	 */
	@Override
	public void sayDemoByParamsMap(Map<String, Object> params) {
		// TODO Auto-generated method stub
		System.out.println(params);
	}

	@Override
	public void sayDemoByParamsCollections(List<Object> params) {
		// TODO Auto-generated method stub
		System.out.println(params);
	}

	@Override
	public void sayDemoByParamsArray(Object[] params) {
		// TODO Auto-generated method stub
		System.out.println(params);
	}


}
