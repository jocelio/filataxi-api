package com.konfx.config.security;


import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.spring.security.api.JwtAuthenticationProvider;
import com.auth0.spring.security.api.JwtWebSecurityConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value(value = "${auth0.apiAudience}")
	private String apiAudience;
	@Value(value = "${auth0.issuer}")
	private String issuer;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		final JwkProvider jwkProvider = new JwkProviderBuilder(issuer).build();
		final JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(jwkProvider, issuer, apiAudience);

		JwtWebSecurityConfigurer
				.forRS256(apiAudience, issuer, authenticationProvider)
				.configure(http)
				.authorizeRequests()
//				.antMatchers("/tasks").hasAuthority("read:messages")
				.anyRequest().authenticated()
				.and()
				.csrf().disable();

	}
}
