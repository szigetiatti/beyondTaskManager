package com.example.demo.domain.shared;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.jta.JtaTransactionManager;

/**
 * Configuration class for JPA.
 *
 * @author szigeti.attila96@gmail.com
 */
@Configuration
@EnableJpaAuditing
@EntityScan({
    "com.example.demo.**.*",
    "org.springframework.data.jpa.convert.threeten"
})
public class JpaConfig extends JpaBaseConfiguration {

  /**
   * Initialize JpaConfig.
   *
   * @param src   DataSource
   * @param props JpaProperties
   * @param txn   JtaTransactionManager
   */
  protected JpaConfig(DataSource src, JpaProperties props, ObjectProvider<JtaTransactionManager> txn) {
    super(src, props, txn);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
    return new EclipseLinkJpaVendorAdapter();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Map<String, Object> getVendorProperties() {

    final Map<String, Object> props = new HashMap<>();
    props.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, this.getDataSource());

    return props;
  }
}
