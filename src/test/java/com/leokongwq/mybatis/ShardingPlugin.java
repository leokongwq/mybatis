package com.leokongwq.mybatis;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.clause.MySqlDeclareConditionStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlLoadXmlStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlPrepareStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.alibaba.druid.util.JdbcConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.executor.BaseExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author : jiexiu
 * @date : 2020-09-08 17:24
 **/
@Intercepts(
		{
//				@Signature(type = PreparedStatementHandler.class, method = "prepare", args = {Connection.class}),
//				@Signature(type = SimpleStatementHandler.class, method = "prepare", args = {Connection.class}),
//				@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})
				@Signature(type = Executor.class, method ="query", args = {
						MappedStatement.class,
						Object.class,
						RowBounds.class,
						ResultHandler.class
				})
		}
)
public class ShardingPlugin implements Interceptor {

	private static final Map<String, String> tableMapping = new HashMap<>();

	static {
		tableMapping.put("role", "role_1");
	}

	private static final MyVisitor visitor = new MyVisitor();

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		BoundSql boundSql = mappedStatement.getBoundSql(invocation.getArgs()[1]);
		String sql = boundSql.getSql();

		Object parameter = invocation.getArgs()[1];

//		sql = SQLUtils.refactor(sql, JdbcConstants.MYSQL, tableMapping);


		List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
		if (CollectionUtils.isNotEmpty(sqlStatements)) {
			for (SQLStatement sqlStatement : sqlStatements) {
				sqlStatement.accept(visitor);
			}
		}
		
		Field field = boundSql.getClass().getDeclaredField("sql");
		field.setAccessible(true);
		ReflectionUtils.setField(field, boundSql, sql);

		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {

	}

	static class MyVisitor extends MySqlASTVisitorAdapter {
		@Override
		public boolean visit(MySqlDeclareConditionStatement x) {
			return false;
		}

		@Override
		public boolean visit(MySqlPrepareStatement x) {
			System.out.println();
			return true;
		}

		@Override
		public boolean visit(MySqlSelectQueryBlock x) {
			return true;
		}
	}
}
