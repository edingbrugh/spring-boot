/*
 * Copyright 2012-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.autoconfigure;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.repository.Repository;

/**
 * 指示一个{@link Configuration 配置}类，它声明一个或多个{@link Bean @Bean}方法，还触发{@link EnableAutoConfiguration auto-configuration}
 * 和{@link ComponentScan 组件扫描}。这是一个方便的注释，相当于声明{@code @Configuration}、{@code @EnableAutoConfiguration}
 * 和{@code @ComponentScan}。
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {

	/**
	 * 排除特定的自动配置类，以便它们永远不会被应用
	 * @return 要排除的类
	 */
	@AliasFor(annotation = EnableAutoConfiguration.class)
	Class<?>[] exclude() default {};

	/**
	 * 排除特定的自动配置类名，这样它们就永远不会被应用。
	 * @since 1.3.0
	 */
	@AliasFor(annotation = EnableAutoConfiguration.class)
	String[] excludeName() default {};

	/**
	 * 扫描带注释的组件的基本包。使用{@link #scanBasePackageClasses}
	 * 作为基于字符串的包名的类型安全替代方案。<p> <strong>注意:<strong>此设置仅为
	 * {@link ComponentScan @ComponentScan}的别名。它对{@code @Entity}扫描或Spring Data {@link Repository}扫描没有影响。
	 * 对于那些你应该添加{@link org.springframework.boot.autoconfigure.domain.EntityScan @EntityScan}和{@code @Enable…存储库}注释。
	 * @return base packages to scan
	 * @since 1.3.0
	 */
	@AliasFor(annotation = ComponentScan.class, attribute = "basePackages")
	String[] scanBasePackages() default {};

	/**
	 * {@link #scanBasePackages} 的类型安全替代方案，用于指定要扫描带注释的组件的包。每个指定类的包将被扫描。
	 * <p>考虑在每个包中创建一个特殊的no-op标记类或接口，除了被这个属性引用之外没有其他用途。
	 * <p> <strong>注意:<strong>此设置仅为{@link ComponentScan @ComponentScan}的别名。
	 * 它对{@code @Entity}扫描或Spring Data {@link Repository}扫描没有影响。
	 * 对于那些你应该添加{@link org.springframework.boot.autoconfigure.domain.EntityScan @EntityScan}和{@code @Enable…存储库}注释。
	 * @return base packages to scan
	 * @since 1.3.0
	 */
	@AliasFor(annotation = ComponentScan.class, attribute = "basePackageClasses")
	Class<?>[] scanBasePackageClasses() default {};

	/**
	 * 用于在Spring容器中命名检测到的组件的{@link BeanNameGenerator}类。
	 * {@link BeanNameGenerator}接口本身的默认值表明用于处理这个{@code @SpringBootApplication}
	 * 注释的扫描器应该使用它继承的bean名称生成器，例如默认的{@link AnnotationBeanNameGenerator}
	 * 或在引导时提供给应用程序上下文的任何自定义实例。@return {@link BeanNameGenerator}使用
	 */
	@AliasFor(annotation = ComponentScan.class, attribute = "nameGenerator")
	Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;

	/**
	 * 指定是否应该代理{@link Bean @Bean}方法以强制执行Bean生命周期行为，例如，即使在用户代码中直接调用{@code @Bean}方法时，
	 * 也要返回共享的单例Bean实例。这个特性需要方法拦截，通过运行时生成的CGLIB子类实现，它有一些限制，比如配置类和它的方法不允许声明{@code final}。
	 * 默认值是{@code true}，允许在配置类内进行“跨bean引用”，也允许外部调用此配置的{@code @Bean}方法，例如从另一个配置类。如果不需要这样做，
	 * 因为这个特定配置的每个{@code @Bean}方法都是自包含的，并且被设计为用于容器使用的普通工厂方法，那么将这个标志切换为{@code false}，
	 * 以避免CGLIB子类处理。<p>关闭bean方法拦截有效地单独处理{@code @Bean}方法，就像在非{@code @Configuration}类上声明时一样。
	 * “@Bean Lite模式”(参见{@link Bean @Bean的javadoc})。因此，它在行为上等同于删除{@code @Configuration}构造型。
	 * @since 2.2
	 * @return whether to proxy {@code @Bean} methods
	 */
	@AliasFor(annotation = Configuration.class)
	boolean proxyBeanMethods() default true;

}
