package com.github.rcf.tcp.spring.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import com.github.rcf.tcp.spring.config.support.RcfService;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfServiceParser implements BeanDefinitionParser {

    public RcfServiceParser(){}

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        // TODO Auto-generated method stub
        String interfacename = element.getAttribute("interfacename");
        String ref=element.getAttribute("ref");
        String filterRef=element.getAttribute("filterRef");

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(RcfService.class);
        beanDefinition.setLazyInit(false);
        beanDefinition.getPropertyValues().addPropertyValue("interfacename", interfacename);
        beanDefinition.getPropertyValues().addPropertyValue("ref", ref);
        beanDefinition.getPropertyValues().addPropertyValue("filterRef", filterRef);

        parserContext.getRegistry().registerBeanDefinition(interfacename, beanDefinition);
        return beanDefinition;
    }


}
