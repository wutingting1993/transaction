package com.mutil.transaction.config;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by WuTing on 2017/12/5.
 */
@Component
@Aspect
@Order(1)
public class DataSourceAspect {
	private static final Logger logger = LoggerFactory.getLogger(DataSourceAspect.class);

	@Before(value = "execution(* com.mutil.transaction.service.*.*(..))")
	public void before(JoinPoint point) {
		DataSource annotation = this.getDataSourceAnnotation(point);
		if (annotation != null) {
			String dataSourceName = this.getDataSourceName(annotation);
			DataSourceContextHolder.setDataSourceType(dataSourceName);
			logger.debug("Set DataSource : {} > {}", dataSourceName, point.getSignature());
		}
	}

	@After(value = "execution(* com.mutil.transaction.service.*.*(..))")
	public void restoreDataSource(JoinPoint point) {

		DataSource annotation = this.getDataSourceAnnotation(point);
		if (annotation != null) {
			String dataSourceName = this.getDataSourceName(annotation);
			logger.debug("Revert DataSource : {} > {}", dataSourceName, point.getSignature());
		}
		DataSourceContextHolder.clearDataSourceType();
	}

	private String getDataSourceName(DataSource annotation) {
		String dataSourceName = annotation.value();
		if (!dataSourceName.toLowerCase().contains("datasource")){
			dataSourceName+="DataSource";
		}
		return dataSourceName;
	}

	private DataSource getDataSourceAnnotation(JoinPoint point) {
		DataSource annotation = null;
		Class[] parameterTypes = ((MethodSignature)point.getSignature()).getMethod().getParameterTypes();
		String methodName = point.getSignature().getName();
		try {
			Method method = point.getTarget().getClass().getMethod(methodName, parameterTypes);
			if (method.isAnnotationPresent(DataSource.class)) {
				annotation = method.getAnnotation(DataSource.class);
			} else {
				annotation = point.getTarget().getClass().getAnnotation(DataSource.class);
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return annotation;
	}
}
