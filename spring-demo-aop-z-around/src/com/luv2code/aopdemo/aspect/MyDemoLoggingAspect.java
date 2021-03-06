package com.luv2code.aopdemo.aspect;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.luv2code.aopdemo.Account;

@Aspect
@Component
@Order(3)
public class MyDemoLoggingAspect {
	
	@Around("execution(* com.luv2code.aopdemo.service.*.getFortune(..))")
	public Object aroundGetFortune(ProceedingJoinPoint proccJointPoint) throws Throwable {
		
		String method = proccJointPoint.getSignature().toShortString();
		
		System.out.println("\n ========>>> Executing @Around advice on " + method);
		
		long begin = System.currentTimeMillis();
		
		Object result = proccJointPoint.proceed();
		
		long end = System.currentTimeMillis();
		long duration = end - begin;
		
		System.out.println("\n ========>>> Duration: " + duration / 1000.0 + " seconds");
		
		return result;
		
	}
	
	@After("execution(* com.luv2code.aopdemo.dao.AccountDAO.findAccounts(..))")
	public void afterFindAccountsAdvice(JoinPoint theJoinPoint) {
		
		String method = theJoinPoint.getSignature().toShortString();
		
		System.out.println("\n ========>>> Executing @After advice on " + method);
		
	}
	
	@AfterThrowing(pointcut="execution(* com.luv2code.aopdemo.dao.AccountDAO.findAccounts(..))", throwing = "theExc")
	public void afterThrowingFindAccountsAdvice(JoinPoint theJoinPoint, Throwable theExc) {
		
		String method = theJoinPoint.getSignature().toShortString();
		
		System.out.println("\n ========>>> Executing @AfterThrowing advice on " + method);
		
		System.out.println("\n ========>>> Exception " + theExc);
		
	}
	
	@AfterReturning(pointcut="execution(* com.luv2code.aopdemo.dao.AccountDAO.findAccounts(..))", returning = "result")
	public void afterReturningFindAccountsAdvice(JoinPoint theJoinPoint, List<Account> result) {
		
		String method = theJoinPoint.getSignature().toShortString();
		
		System.out.println("\n ========>>> Executing @AfterReturning advice on " + method);
		
		System.out.println("\n ========>>> Result" + result);
		
		convertToUpperCaseName(result);
		System.out.println("\n ========>>> Result" + result);
	}
	
	private void convertToUpperCaseName(List<Account> result) {
		for (Account acc : result) {
			String upperName = acc.getName().toUpperCase();
			acc.setName(upperName);
		}
		
	}

	@Before("com.luv2code.aopdemo.aspect.LuvAopExpressions.forDaoMethodsExcludeGettersAndSetters()")
	public void beforAddAccountAdvice(JoinPoint theJoinPoint) {
		
			
		System.out.println("\n ========>>> Executing @Before advice on addAccount(with param)");
		
		MethodSignature methodSig = (MethodSignature) theJoinPoint.getSignature();
		
		System.out.println("Method: " + methodSig);
		
		Object[] args = theJoinPoint.getArgs();
		
		for (Object tempArg : args) {
			System.out.println(tempArg);
			
			if (tempArg instanceof Account) {
				
				Account account = (Account) tempArg;
				
				System.out.println("Name: " + account.getName());
				System.out.println("Level: " + account.getLevel());
			}
		}
	}
	
}
