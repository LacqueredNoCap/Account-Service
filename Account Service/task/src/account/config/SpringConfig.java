package account.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

//@Configuration
//@ComponentScan("account")
public class SpringConfig {

    @Value("${h2.driverClassName}")
    private String driver;

    @Value("${h2.url}")
    private String url;

    @Value("${h2.username}")
    private String username;

    @Value("${h2.password}")
    private String password;

    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName(driver)
                .url(url)
                .username(username)
                .password(password)
                .build();
    }

}
