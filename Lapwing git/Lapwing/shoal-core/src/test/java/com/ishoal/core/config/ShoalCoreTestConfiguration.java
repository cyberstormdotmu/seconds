package com.ishoal.core.config;

import com.ishoal.payment.buyer.config.ShoalPaymentTestConfiguration;
import com.ishoal.sms.config.ShoalSmsTestConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.orm.hibernate4.SpringSessionContext;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Properties;

@Configuration
@Import({ShoalCoreBeanConfiguration.class, ShoalPaymentTestConfiguration.class, ShoalSmsTestConfiguration.class})
public class ShoalCoreTestConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ShoalCoreTestConfiguration.class);


    @Bean(destroyMethod = "shutdown")
    public EmbeddedDatabase dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPackagesToScan("com.ishoal.core.repository");
        entityManagerFactoryBean.setJpaProperties(new Properties() {
            {
                put("hibernate.current_session_context_class", SpringSessionContext.class.getName());
            }
        });
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter() {
            {
                setDatabase(Database.H2);
                setGenerateDdl(true);
            }
        });
        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public MailSender mailSender() {
        return new MailSender() {
            @Override
            public void send(SimpleMailMessage simpleMessage) throws MailException {
                logger.info("sending an email to "+ simpleMessage.getTo()[0]);
            }

            @Override
            public void send(SimpleMailMessage... simpleMessages) throws MailException {
                if (simpleMessages.length > 0) {
                    logger.info("sending an email to " + simpleMessages[0].getTo()[0]);
                }
            }
        };
    }
}