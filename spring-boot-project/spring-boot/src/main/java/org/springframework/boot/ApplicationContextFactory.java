/*
 * Copyright 2012-2022 the original author or authors.
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

package org.springframework.boot;

import java.util.function.Supplier;

import org.springframework.beans.BeanUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.support.SpringFactoriesLoader;

/**
 * 用于创建{@link SpringApplication}使用的{@link ConfigurableApplicationContext}的策略接口。
 * 创建的上下文应该以默认形式返回，{@code SpringApplication}负责配置和刷新上下文。
 */
@FunctionalInterface
public interface ApplicationContextFactory {

	/**
	 * 一个默认的{@link ApplicationContextFactory}实现，它将为{@link WebApplicationType}创建一个适当的上下文。
	 */
	ApplicationContextFactory DEFAULT = (webApplicationType) -> {
		try {
			for (ApplicationContextFactory candidate : SpringFactoriesLoader
					.loadFactories(ApplicationContextFactory.class, ApplicationContextFactory.class.getClassLoader())) {
				ConfigurableApplicationContext context = candidate.create(webApplicationType);
				if (context != null) {
					return context;
				}
			}
			return new AnnotationConfigApplicationContext();
		}
		catch (Exception ex) {
			throw new IllegalStateException("Unable create a default ApplicationContext instance, "
					+ "you may need a custom ApplicationContextFactory", ex);
		}
	};

	/**
	 * 为{@link SpringApplication}创建{@link ConfigurableApplicationContext 应用程序上下文}，根据给定的{@code webApplicationType}。
	 */
	ConfigurableApplicationContext create(WebApplicationType webApplicationType);

	/**
	 * 创建一个{@code ApplicationContextFactory}，它将通过其主构造函数实例化给定的{@code contextClass}来创建上下文。
	 */
	static ApplicationContextFactory ofContextClass(Class<? extends ConfigurableApplicationContext> contextClass) {
		return of(() -> BeanUtils.instantiateClass(contextClass));
	}

	/**
	 * 创建一个{@code ApplicationContextFactory}，它将通过调用给定的{@link Supplier}来创建上下文。
	 */
	static ApplicationContextFactory of(Supplier<ConfigurableApplicationContext> supplier) {
		return (webApplicationType) -> supplier.get();
	}

}
