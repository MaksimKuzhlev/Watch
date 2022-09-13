package com.example.kuzhlev.security

import com.example.kuzhlev.views.Login
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import javax.servlet.annotation.WebServlet
import javax.sql.DataSource


@Configuration
@EnableWebSecurity
class WebSecurityConfig (@Autowired private val dataSource: DataSource,@Autowired private val passwordEncoder:PasswordEncoder) : VaadinWebSecurityConfigurerAdapter() {


    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        super.configure(http)
        setLoginView(http,Login::class.java)
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .passwordEncoder(passwordEncoder)
            .usersByUsernameQuery("select username, password, active from usr where username=?")
            .authoritiesByUsernameQuery("select username, role from usr where username=?")
            .rolePrefix("ROLE_")

    }

    @Throws(Exception::class)
    override fun configure(web: WebSecurity) {
        web
            .ignoring()
            .antMatchers(
                "/images/**",
                "/watch/**"
            )
    }


}