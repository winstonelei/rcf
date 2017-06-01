/**
 * 
 */
package com.github.rcf.tcp.demo.service.impl;

import com.github.rcf.tcp.demo.service.IDemoService;
import com.github.rcf.tcp.demo.service.Simple;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Random;

/**
 * @author winstone
 *
 */
public class DemoServiceImpl implements IDemoService {

	private static final Log LOGGER = LogFactory.getLog(DemoServiceImpl.class);
	Random r = new Random();

	@Override
	public String sayDemo(String params) {
		long start1 = System.currentTimeMillis();

		String code = "06";
		for (int i = 5; i > params.length(); i--) {
			code += "0";
		};
		code += params;

		String url = "http://quotes.money.163.com/" + code + ".html";

		String content = getOneHtml(url);
		String len = String.valueOf(content.length());

		long end1 = System.currentTimeMillis();
		return "len=" + len + " time(ms)=" + (end1 - start1);
	}

	@Override
	public String getParam(String params) {
		return "from server:" + params;
	}

	@Override
	public String getParam(Map<String, Object> map) {
		return "from server:" + map;
	}

	public String getOneHtml(final String htmlurl) {
		URL url;
		String temp;
		final StringBuffer sb = new StringBuffer();
		try {
			url = new URL(htmlurl);
			final BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream(), "utf-8"));// 读取网页全部内容
			while ((temp = in.readLine()) != null) {
				sb.append(temp);
			}
			in.close();
		} catch (Exception e) {
			sb.append("ex=" + e.getMessage());
		}
		return sb.toString();
	}

	@Override
	public Simple getSimple(Simple simple) {
		simple.setAge(simple.getAge()+100);
		return simple;
	}


	public static void main(String[] args) {
		DemoServiceImpl demo = new DemoServiceImpl();
		System.out.println(demo.sayDemo("0"));
		System.out.println(demo.sayDemo("3999"));
	}
}