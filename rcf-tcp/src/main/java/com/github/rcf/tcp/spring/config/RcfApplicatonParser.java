package com.github.rcf.tcp.spring.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import com.github.rcf.tcp.spring.config.support.RcfApplication;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfApplicatonParser implements BeanDefinitionParser {


    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String id = element.getAttribute("id");
        String address=element.getAttribute("address");
        String clientid=element.getAttribute("clientid");  //用于标识不同客户端,也可不配
        String flag=element.getAttribute("flag");
        String timeout=element.getAttribute("timeout");

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(RcfApplication.class);
        beanDefinition.setLazyInit(false);
        beanDefinition.getPropertyValues().addPropertyValue("address", address);
        beanDefinition.getPropertyValues().addPropertyValue("clientid", clientid);
        beanDefinition.getPropertyValues().addPropertyValue("flag", Integer.parseInt(flag));
        beanDefinition.getPropertyValues().addPropertyValue("timeout", Integer.parseInt(timeout));

        parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);
        return beanDefinition;
    }
}
