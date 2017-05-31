package com.github.rcf.http.spring.config;

import com.github.rcf.http.spring.config.support.RcfHttpService;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Created by winstone on 2017/5/30 0030.
 */
public class RcfRpcHttpServiceParser implements BeanDefinitionParser {

    public RcfRpcHttpServiceParser(){}


    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String projectname = element.getAttribute("projectname");
        String ref=element.getAttribute("ref");
        String filterRef=element.getAttribute("filterRef");
        String httpType=element.getAttribute("httpType");
        String returnType=element.getAttribute("returnType");

        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(RcfHttpService.class);
        beanDefinition.setLazyInit(false);
        beanDefinition.getPropertyValues().addPropertyValue("projectname", projectname);
        beanDefinition.getPropertyValues().addPropertyValue("ref", ref);
        beanDefinition.getPropertyValues().addPropertyValue("filterRef", filterRef);
        beanDefinition.getPropertyValues().addPropertyValue("httpType", httpType);
        beanDefinition.getPropertyValues().addPropertyValue("returnType", returnType);

        parserContext.getRegistry().registerBeanDefinition(projectname, beanDefinition);
        return beanDefinition;
    }

}
