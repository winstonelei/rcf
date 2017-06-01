package com.github.rcf.tcp.spring.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import com.github.rcf.tcp.spring.config.support.RpcRegistery;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfRegistryParser  implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        // TODO Auto-generated method stub
        String id = element.getAttribute("id");
        String ip = element.getAttribute("ip");
        int port=Integer.parseInt(element.getAttribute("port"));
        int timeout=Integer.parseInt(element.getAttribute("timeout"));
        int codecType=Integer.parseInt(element.getAttribute("codecType"));
        int threadCount=Integer.parseInt(element.getAttribute("threadCount"));
        String group=element.getAttribute("group");

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(RpcRegistery.class);
        beanDefinition.getPropertyValues().addPropertyValue("ip", ip);
        beanDefinition.getPropertyValues().addPropertyValue("port", port);
        beanDefinition.getPropertyValues().addPropertyValue("timeout", timeout);
        beanDefinition.getPropertyValues().addPropertyValue("group", group);
        beanDefinition.getPropertyValues().addPropertyValue("codecType", codecType);
        beanDefinition.getPropertyValues().addPropertyValue("threadCount", threadCount);

        parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);

        return beanDefinition;
    }

}
