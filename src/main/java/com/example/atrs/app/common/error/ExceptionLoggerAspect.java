package com.example.atrs.app.common.error;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.terasoluna.gfw.common.exception.ExceptionLogger;

import org.springframework.stereotype.Component;

@Component
@Aspect
public class ExceptionLoggerAspect {
	private final ExceptionLogger exceptionLogger;

	public ExceptionLoggerAspect(ExceptionLogger exceptionLogger) {
		this.exceptionLogger = exceptionLogger;
	}

	@Around("@within(org.springframework.stereotype.Service)")
	public Object log(ProceedingJoinPoint pjp) throws Throwable {
		try {
			return pjp.proceed();
		}
		catch (Exception e) {
			this.exceptionLogger.log(e);
			throw e;
		}
	}
}
