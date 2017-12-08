package com.mutil.transaction.config;

import java.lang.reflect.Method;
import java.util.Arrays;

import javax.transaction.UserTransaction;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.jta.JtaTransactionManager;

/**
 * Created by WuTing on 2017/12/6.
 */
@Aspect
@Component
public class MultiTransactionalAspect {

	private static final Logger logger = LoggerFactory.getLogger(MultiTransactionalAspect.class);

	@Around(value = "@annotation(com.mutil.transaction.config.MultiTransactional)")
	public Object transactional(ProceedingJoinPoint point) throws Exception {
		String methodName = point.getSignature().getName();
		Class[] parameterTypes = ((MethodSignature)point.getSignature()).getMethod().getParameterTypes();
		UserTransaction tran = null;
		Object result = null;
		MultiTransactional multiTransactional = null;
		try {
			Method method = point.getTarget().getClass().getMethod(methodName, parameterTypes);

			if (method.isAnnotationPresent(MultiTransactional.class)) {
				multiTransactional = method.getAnnotation(MultiTransactional.class);
				JtaTransactionManager transactionManager = SpringContextUtil.getBean(JtaTransactionManager.class);
				tran = transactionManager.getUserTransaction();
				tran.begin();
				logger.warn(methodName + ", transaction begin");
				result = point.proceed();
				tran.commit();
				logger.warn(methodName + ", transaction commit");
			}

		} catch (Throwable e) {
			logger.error(e.getMessage(), e);

			if (tran != null) {
				Class<? extends Throwable>[] rollbackExcptions = multiTransactional.rollbackFor();
				Class<? extends Throwable>[] noRollbackExcptions = multiTransactional.noRollbackFor();
				boolean rollback = isPresent(e, rollbackExcptions);
				boolean noRollback = isPresent(e, noRollbackExcptions);

				if (rollback || !noRollback) {
					tran.rollback();
					logger.warn(methodName + ", transaction rollback");
				} else {
					tran.commit();
					logger.warn(methodName + ", transaction commit");
				}
			}
		}

		return result;
	}

	private boolean isPresent(Throwable e, Class<? extends Throwable>[] excptions) {
		return Arrays.stream(excptions)
			.filter(exception -> e.getClass().isAssignableFrom(exception) || e.getClass().equals(exception))
			.findAny()
			.isPresent();
	}
}
