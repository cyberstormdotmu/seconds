package com.tatva.aop;

import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.util.StopWatch;

/**
 * 
 * @author Sushant - Victoy Loves Preparation
 *
 */
@Aspect
public class ApplicationLoggingAspect {

	 long startTime = 0;
	 long finishTime = 0;
	 Logger log = Logger.getLogger(this.getClass().getName());
	 
	/*@Override
	public Object invoke(MethodInvocation args) throws Throwable {
		System.out.println("Method invoked with params:" + args.getArguments()[0]);
		return args.proceed();
	}*/

	@AfterReturning(pointcut = "execution(* com.spring.controller.*.*(..))",returning= "returnValue")
	public void afterReturning(JoinPoint joinPoint, Object returnValue) throws Throwable {
		finishTime = System.currentTimeMillis();
		double totalDuration = finishTime - startTime;
		log.debug(" Call afterReturning(JoinPoint joinPoint, Object returnValue) method - execution time:"+totalDuration/1000+" seconds. ");
		System.out.println("AfterReturning Finished executing method " + joinPoint.getSignature().getName()
				   + " on object " + joinPoint.getClass().getName() + " in "
				   + totalDuration / 1000 + " seconds. Return Value is --"+returnValue );
	}
	
	@After("execution(* com.tatva.controller.*.*(..))")
	public void after(JoinPoint joinPoint)	throws Throwable {
		startTime = System.currentTimeMillis();
		System.out.println("After Executing method " + joinPoint.getSignature().getName() + " on object " + joinPoint.getClass().getName());
	}
	
	@Before("execution(* com.tatva.controller.*.*(..))")
	public void before(JoinPoint joinPoint)	throws Throwable {
		startTime = System.currentTimeMillis();
		System.out.println("Before Executing method " + joinPoint.getSignature().getName()+ " on object " + joinPoint.getClass().getName());
	}

	@AfterThrowing(pointcut = "execution(* com.tatva.controller.*.*(..))",  throwing= "error")
	public void afterThrowing(Throwable error){
		log.error(error);
	}

	/*@Around("execution(* com.tatva.controller.*.*(..))")
	public Object logArond(ProceedingJoinPoint joinPoint) throws Throwable
	{
		System.out.println("logAround() is running!");
		System.out.println("hijacked method : " + joinPoint.getSignature().getName());
		System.out.println("hijacked arguments : " + Arrays.toString(joinPoint.getArgs()));
	 
		System.out.println("Around before is running!");
		Object object = joinPoint.proceed(); //continue on the intercepted method
		System.out.println("Around after is running!");
		return object;
	}*/
	
	/*@Around("execution(* com.tatva.controller.*.*(..))")
	public Object logTimeMethod(ProceedingJoinPoint joinPoint) throws Throwable {

			StopWatch stopWatch = new StopWatch();
			stopWatch.start();

			Object retVal = joinPoint.proceed();

			stopWatch.stop();

			StringBuffer logMessage = new StringBuffer();
			logMessage.append(joinPoint.getTarget().getClass().getName());
			logMessage.append(".");
			logMessage.append(joinPoint.getSignature().getName());
			logMessage.append("(");
			
			Object[] args = joinPoint.getArgs();
			for (int i = 0; i < args.length; i++) {
				logMessage.append(args[i]).append(",");
			}
			if (args.length > 0) {
				logMessage.deleteCharAt(logMessage.length() - 1);
			}

			logMessage.append(")");
			logMessage.append(" execution time: ");
			logMessage.append(stopWatch.getTotalTimeMillis());
			logMessage.append(" ms");
			log.info(logMessage.toString());
			return retVal;
	}*/
}
