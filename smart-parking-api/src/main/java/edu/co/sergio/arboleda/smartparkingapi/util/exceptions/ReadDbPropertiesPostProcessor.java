package edu.co.sergio.arboleda.smartparkingapi.util.exceptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
public class ReadDbPropertiesPostProcessor implements BeanPostProcessor {

	private final Log log = LogFactory.getLog(this.getClass());
	@Autowired
	ConfigurableEnvironment environment;
	@Value("${config_table:config}")
	private String tableName;

	@Override
	public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {

		if (bean instanceof DataSource) {
			PreparedStatement preparedStatement = null;
			try {
				DataSource dataSourceBean = (DataSource) bean;

				Connection connection = DataSourceUtils.getConnection(dataSourceBean);

				Map<String, Object> propertySource = new HashMap<>();
				if (StringUtils.isNotEmpty(tableName)) {
					preparedStatement = connection.prepareStatement("SELECT name, value FROM " + tableName);
					ResultSet rs = preparedStatement.executeQuery();

					while (rs.next()) {
						String propName = rs.getString("name");
						propertySource.put(propName, rs.getString("value"));
					}

					environment.getPropertySources().addFirst(new MapPropertySource("application", propertySource));
				}

			} catch (Exception e) {
				log.error(e.getMessage(), e);
			} finally {
				if (preparedStatement != null) {
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						log.error(e.getMessage(), e);
					}
				}
			}
		}
		return bean;
	}

}
