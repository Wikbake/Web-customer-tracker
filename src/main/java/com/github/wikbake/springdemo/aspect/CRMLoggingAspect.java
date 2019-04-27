package com.github.wikbake.springdemo.aspect;

import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CRMLoggingAspect {

	// setup logger
	private Logger myLogger = Logger.getLogger(getClass().getName());
	
	// setup pointcut declarations 
	@Pointcut("execution(* com.github.wikbake.springdemo.controller.*.*(..))")
	private void forControllerPackage() {}
	
	@Pointcut("execution(* com.github.wikbake.springdemo.service.*.*(..))")
	private void forServicePackage() {}
	
	@Pointcut("execution(* com.github.wikbake.springdemo.dao.*.*(..))")
	private void forDaoPackage() {}
	
	@Pointcut("forControllerPackage() || forServicePackage() || forDaoPackage()")
	private void forAppFlow() {}
	
	// add @Before advice
	@Before("forAppFlow()")
	public void before(JoinPoint theJoinPoint) {
		
		// display method we are caling 
		String theMethod = theJoinPoint.getSignature().toShortString();
		myLogger.info("=====> in @Before: calling method: " + theMethod);
		
		// display the arguments to the method 
		
		// get the arguments 
		Object[] args = theJoinPoint.getArgs();
		
		// loop through and display args
		for (Object tempArg : args)
			myLogger.info("=====>> argument: " + tempArg);
	}
	
	// add @AfterReturning advice 
	@AfterReturning(pointcut="forAppFlow()", returning="theResult")
	public void afterReturning(JoinPoint theJoinPoint, Object theResult) {
		
		// display method we are returning from 
		String theMethod = theJoinPoint.getSignature().toShortString();
		myLogger.info("=====> in @afterReturning: from method: " + theMethod);
		
		// display data returned
		myLogger.info("====>> result: " + theResult);
	}
	
	
}
