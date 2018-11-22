/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.context.annotation;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.regex.Pattern;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.parsing.CompositeComponentDefinition;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.beans.factory.xml.XmlReaderContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AspectJTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Parser for the {@code <context:component-scan/>} element.
 *
 * @author Mark Fisher
 * @author Ramnivas Laddad
 * @author Juergen Hoeller
 * @since 2.5
 */
public class ComponentScanBeanDefinitionParser implements BeanDefinitionParser {

    /**
     * 指定了spring需要扫描的跟目录名称,可以使用”,” “;” “\t\n(回车符)”来分割多个包名
     */
    private static final String BASE_PACKAGE_ATTRIBUTE = "base-package";
    /**
     * 配置扫描资源格式.默认*.class”
     */
    private static final String RESOURCE_PATTERN_ATTRIBUTE = "resource-pattern";
    /**
     * 是否使用默认扫描策略,默认为”true”,会自动扫描指定包下的添加了如下注解的类,@Component, @Repository, @Service,or @Controller
     */
    private static final String USE_DEFAULT_FILTERS_ATTRIBUTE = "use-default-filters";

    /**
     * 是否启用默认配置,默认为”true”,该配置会在BeanDefinition注册到容器后自动注册一些BeanPostProcessors对象到容器中.这些处理器用来处理类中
     *
     * @Required
     * @Autowired,
     * @PostConstruct,
     * @PreDestroy
     * @Resource (如果可用),  JAX-WS’s @WebServiceRef (如果可用), EJB 3’s
     * @EJB (如果可用), and JPA’s 
     * @PersistenceContext and @PersistenceUnit (如果可用) 但是该属性不会处理Spring’s @Transactional 和
     * EJB3中的@TransactionAttribute注解对象,这两个注解是通过<tx:annotation-driven>元素处理过程中对应的BeanPostProcessor来处理的
     */
    private static final String ANNOTATION_CONFIG_ATTRIBUTE = "annotation-config";
    /**
     * beanName生成器
     */
    private static final String NAME_GENERATOR_ATTRIBUTE = "name-generator";
    /**
     * bean的作用范围
     */
    private static final String SCOPE_RESOLVER_ATTRIBUTE = "scope-resolver";
    /**
     * cglib代理、JDK代理、no：不使用代理 ，默认使用cglib代理。
     * 那什么时候需要用到scope代理呢，举个例子，我们知道Bean的作用域scope有singleton，prototype，request,session,
     * 那有这么一种情况，当你把一个session或者request的Bean注入到singleton的Bean中时，因为singleton的Bean在容器启动时就会创建A，
     * 而session的Bean在用户访问时才会创建B，那么当A中要注入B时，有可能B还未创建，这个时候就会出问题，那么代理的时候来了，B如果是个接口，
     * 就用JDK代理，是个类则用cglib代理。
     */
    private static final String SCOPED_PROXY_ATTRIBUTE = "scoped-proxy";
    /**
     * 用来告知哪些类不需要注册成Spring Bean,使用type和expression属性一起协作来定义组件扫描策略
     */
    private static final String EXCLUDE_FILTER_ELEMENT = "exclude-filter";
    /**
     * 用来告知哪些类需要注册成Spring Bean,使用type和expression属性一起协作来定义组件扫描策略
     */
    private static final String INCLUDE_FILTER_ELEMENT = "include-filter";

    /**
     * 五种类型 只有在use-default-filters=false才生效
     * annotation：过滤器扫描使用注解所标注的那些类，通过expression属性指定要扫描的注释
     * assignable：过滤器扫描派生于expression属性所指定类型的那些类
     * aspectj：过滤器扫描与expression属性所指定的AspectJ表达式所匹配的那些类
     * custom：使用自定义的org.springframework.core.type.TypeFliter实现类，该类由expression属性指定
     * regex：过滤器扫描类的名称与expression属性所指定正则表示式所匹配的那些类
     */
    private static final String FILTER_TYPE_ATTRIBUTE = "type";

    private static final String FILTER_EXPRESSION_ATTRIBUTE = "expression";


    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String basePackage = element.getAttribute(BASE_PACKAGE_ATTRIBUTE);
        // 对base-package的值中的${}进行解析并替换
        basePackage = parserContext.getReaderContext().getEnvironment().resolvePlaceholders(basePackage);
        // 解析分隔符
        String[] basePackages = StringUtils.tokenizeToStringArray(basePackage,
                ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);

        // Actually scan for bean definitions and register them.
        //扫描bean并注册它们
        ClassPathBeanDefinitionScanner scanner = configureScanner(parserContext, element);
        Set<BeanDefinitionHolder> beanDefinitions = scanner.doScan(basePackages);
        //注册其他注解的主键。比如AutowiredAnnotationBeanPostProcessor。多个BeanPostProcessor接口实现类
        registerComponents(parserContext.getReaderContext(), beanDefinitions, element);

        return null;
    }

    protected ClassPathBeanDefinitionScanner configureScanner(ParserContext parserContext, Element element) {
        //是否使用默认的过滤器
        boolean useDefaultFilters = true;
        if (element.hasAttribute(USE_DEFAULT_FILTERS_ATTRIBUTE)) {
            useDefaultFilters = Boolean.valueOf(element.getAttribute(USE_DEFAULT_FILTERS_ATTRIBUTE));
        }

        // Delegate bean definition registration to scanner class.
        //创建ClassPathBeanDefinitionScanner对象，用于组装需要扫描的注解
        // useDefaultFilters=true会给includeFilters添加则添加`@Component`、`@Service`、`@Controller`、`@Repository`、
        // `@ManagedBean`、`@Named`添加到includeFilters的集合过滤
        ClassPathBeanDefinitionScanner scanner = createScanner(parserContext.getReaderContext(), useDefaultFilters);
        scanner.setBeanDefinitionDefaults(parserContext.getDelegate().getBeanDefinitionDefaults());
        scanner.setAutowireCandidatePatterns(parserContext.getDelegate().getAutowireCandidatePatterns());

        //判断是否配置resource-pattern扫描资源的模式匹配
        if (element.hasAttribute(RESOURCE_PATTERN_ATTRIBUTE)) {
            scanner.setResourcePattern(element.getAttribute(RESOURCE_PATTERN_ATTRIBUTE));
        }

        try {
            //解析name-generator，设置beanName
            parseBeanNameGenerator(element, scanner);
        } catch (Exception ex) {
            parserContext.getReaderContext()
                    .error(ex.getMessage(), parserContext.extractSource(element), ex.getCause());
        }

        try {
            //配置元数据解析。scope-resolver，scoped-proxy
            parseScope(element, scanner);
        } catch (Exception ex) {
            parserContext.getReaderContext()
                    .error(ex.getMessage(), parserContext.extractSource(element), ex.getCause());
        }
        //配置包含和不包含过滤器。excludeFilters,includeFilters
        parseTypeFilters(element, scanner, parserContext);

        return scanner;
    }

    protected ClassPathBeanDefinitionScanner createScanner(XmlReaderContext readerContext, boolean useDefaultFilters) {
        return new ClassPathBeanDefinitionScanner(readerContext.getRegistry(), useDefaultFilters,
                readerContext.getEnvironment(), readerContext.getResourceLoader());
    }

    protected void registerComponents(
            XmlReaderContext readerContext, Set<BeanDefinitionHolder> beanDefinitions, Element element) {

        Object source = readerContext.extractSource(element);
        CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(), source);

        for (BeanDefinitionHolder beanDefHolder : beanDefinitions) {
            compositeDef.addNestedComponent(new BeanComponentDefinition(beanDefHolder));
        }

        // Register annotation config processors, if necessary.
        boolean annotationConfig = true;
        if (element.hasAttribute(ANNOTATION_CONFIG_ATTRIBUTE)) {
            annotationConfig = Boolean.valueOf(element.getAttribute(ANNOTATION_CONFIG_ATTRIBUTE));
        }
        if (annotationConfig) {
            Set<BeanDefinitionHolder> processorDefinitions =
                    AnnotationConfigUtils.registerAnnotationConfigProcessors(readerContext.getRegistry(), source);
            for (BeanDefinitionHolder processorDefinition : processorDefinitions) {
                compositeDef.addNestedComponent(new BeanComponentDefinition(processorDefinition));
            }
        }

        readerContext.fireComponentRegistered(compositeDef);
    }

    protected void parseBeanNameGenerator(Element element, ClassPathBeanDefinitionScanner scanner) {
        if (element.hasAttribute(NAME_GENERATOR_ATTRIBUTE)) {
            BeanNameGenerator beanNameGenerator = (BeanNameGenerator) instantiateUserDefinedStrategy(
                    element.getAttribute(NAME_GENERATOR_ATTRIBUTE), BeanNameGenerator.class,
                    scanner.getResourceLoader().getClassLoader());
            scanner.setBeanNameGenerator(beanNameGenerator);
        }
    }

    protected void parseScope(Element element, ClassPathBeanDefinitionScanner scanner) {
        // Register ScopeMetadataResolver if class name provided.
        if (element.hasAttribute(SCOPE_RESOLVER_ATTRIBUTE)) {
            if (element.hasAttribute(SCOPED_PROXY_ATTRIBUTE)) {
                throw new IllegalArgumentException(
                        "Cannot define both 'scope-resolver' and 'scoped-proxy' on <component-scan> tag");
            }
            ScopeMetadataResolver scopeMetadataResolver = (ScopeMetadataResolver) instantiateUserDefinedStrategy(
                    element.getAttribute(SCOPE_RESOLVER_ATTRIBUTE), ScopeMetadataResolver.class,
                    scanner.getResourceLoader().getClassLoader());
            scanner.setScopeMetadataResolver(scopeMetadataResolver);
        }

        if (element.hasAttribute(SCOPED_PROXY_ATTRIBUTE)) {
            String mode = element.getAttribute(SCOPED_PROXY_ATTRIBUTE);
            if ("targetClass".equals(mode)) {
                scanner.setScopedProxyMode(ScopedProxyMode.TARGET_CLASS);
            } else if ("interfaces".equals(mode)) {
                scanner.setScopedProxyMode(ScopedProxyMode.INTERFACES);
            } else if ("no".equals(mode)) {
                scanner.setScopedProxyMode(ScopedProxyMode.NO);
            } else {
                throw new IllegalArgumentException("scoped-proxy only supports 'no', 'interfaces' and 'targetClass'");
            }
        }
    }

    protected void parseTypeFilters(Element element, ClassPathBeanDefinitionScanner scanner,
            ParserContext parserContext) {
        // Parse exclude and include filter elements.
        ClassLoader classLoader = scanner.getResourceLoader().getClassLoader();
        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String localName = parserContext.getDelegate().getLocalName(node);
                try {
                    if (INCLUDE_FILTER_ELEMENT.equals(localName)) {
                        TypeFilter typeFilter = createTypeFilter((Element) node, classLoader, parserContext);
                        scanner.addIncludeFilter(typeFilter);
                    } else if (EXCLUDE_FILTER_ELEMENT.equals(localName)) {
                        TypeFilter typeFilter = createTypeFilter((Element) node, classLoader, parserContext);
                        scanner.addExcludeFilter(typeFilter);
                    }
                } catch (ClassNotFoundException ex) {
                    parserContext.getReaderContext().warning(
                            "Ignoring non-present type filter class: " + ex, parserContext.extractSource(element));
                } catch (Exception ex) {
                    parserContext.getReaderContext().error(
                            ex.getMessage(), parserContext.extractSource(element), ex.getCause());
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected TypeFilter createTypeFilter(Element element, ClassLoader classLoader,
            ParserContext parserContext) throws ClassNotFoundException {

        String filterType = element.getAttribute(FILTER_TYPE_ATTRIBUTE);
        String expression = element.getAttribute(FILTER_EXPRESSION_ATTRIBUTE);
        expression = parserContext.getReaderContext().getEnvironment().resolvePlaceholders(expression);
        if ("annotation".equals(filterType)) {
            return new AnnotationTypeFilter((Class<Annotation>) ClassUtils.forName(expression, classLoader));
        } else if ("assignable".equals(filterType)) {
            return new AssignableTypeFilter(ClassUtils.forName(expression, classLoader));
        } else if ("aspectj".equals(filterType)) {
            return new AspectJTypeFilter(expression, classLoader);
        } else if ("regex".equals(filterType)) {
            return new RegexPatternTypeFilter(Pattern.compile(expression));
        } else if ("custom".equals(filterType)) {
            Class<?> filterClass = ClassUtils.forName(expression, classLoader);
            if (!TypeFilter.class.isAssignableFrom(filterClass)) {
                throw new IllegalArgumentException(
                        "Class is not assignable to [" + TypeFilter.class.getName() + "]: " + expression);
            }
            return (TypeFilter) BeanUtils.instantiateClass(filterClass);
        } else {
            throw new IllegalArgumentException("Unsupported filter type: " + filterType);
        }
    }

    @SuppressWarnings("unchecked")
    private Object instantiateUserDefinedStrategy(String className, Class<?> strategyType, ClassLoader classLoader) {
        Object result;
        try {
            result = classLoader.loadClass(className).newInstance();
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Class [" + className + "] for strategy [" +
                    strategyType.getName() + "] not found", ex);
        } catch (Throwable ex) {
            throw new IllegalArgumentException("Unable to instantiate class [" + className + "] for strategy [" +
                    strategyType.getName() + "]: a zero-argument constructor is required", ex);
        }

        if (!strategyType.isAssignableFrom(result.getClass())) {
            throw new IllegalArgumentException("Provided class name must be an implementation of " + strategyType);
        }
        return result;
    }

}
