package com.leokongwq.mybatis;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;

import java.util.Properties;

/**
 * @author : jiexiu
 * @date : 2020-09-08 17:13
 **/
@Intercepts(
		{
				@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
		}
)
public class MyPlugin implements Interceptor {

	private Properties properties = new Properties();

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		System.out.println(this.getClass().getSimpleName() + " ########## before Executor.update");
		Object object = invocation.proceed();
		System.out.println(this.getClass().getSimpleName() +  " ########## after Executor.update");
		return object;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		if (properties != null && !properties.isEmpty()) {
			this.properties.putAll(properties);
		}
	}
}
