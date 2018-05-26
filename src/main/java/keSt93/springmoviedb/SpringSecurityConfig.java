package keSt93.springmoviedb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private Environment environment;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl=new JdbcTokenRepositoryImpl();
        jdbcTokenRepositoryImpl.setDataSource(getDataSource());

        http.csrf().disable()
                .authorizeRequests()
                    // Resources
                    .antMatchers("/css/**").permitAll()
                    .antMatchers("/js/**").permitAll()
                    .antMatchers("/img/**").permitAll()
                    .antMatchers("/target/**").permitAll()
                    // Public Sites
                    .antMatchers("/*").permitAll()
                    .antMatchers("/login/*").permitAll()
                    .antMatchers("/logout/*").permitAll()
                    .antMatchers("/m/***").hasAnyRole("USER", "OBSERVER")
                    .antMatchers("/m/movies/**").hasAnyRole("USER", "OBSERVER")
                    // .antMatchers("/m/series/**").hasAnyRole("ROLE_USER", "ROLE_OBSERVER")
                    // .antMatchers("/m/addmovie/**").hasAnyRole("ROLE_USER", "ROLE_OBSERVER")
                    // .antMatchers("/actions/**").hasAnyRole("ROLE_USER")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .defaultSuccessUrl("/m/user/current")
                    .loginPage("/")
                    .permitAll()
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/")
                    .permitAll()
                    .and()
                .rememberMe()
                    .rememberMeParameter("remember-me")
                    .tokenRepository(jdbcTokenRepositoryImpl)
                    .and()
                .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(getDataSource())
                .usersByUsernameQuery("SELECT username, password, enabled from user where username=?")
                .authoritiesByUsernameQuery("SELECT id_user, role from user_roles where id_user=(SELECT id from user where username=?)");
    }

    public DataSource getDataSource() {
        DriverManagerDataSource userSource = new DriverManagerDataSource();
        userSource.setDriverClassName("com.mysql.jdbc.Driver");
        userSource.setUrl(environment.getProperty("spring.datasource.url"));
        userSource.setUsername(environment.getProperty("spring.datasource.username"));
        userSource.setPassword(environment.getProperty("spring.datasource.password"));
        return userSource;
    }
}