package com.andrecarlos.cursomc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.andrecarlos.cursomc.security.JWTAuthenticationFilter;
import com.andrecarlos.cursomc.security.JWTAuthorizationFilter;
import com.andrecarlos.cursomc.security.JWTUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // Permitirá colocar anotações de pré autorização nos endpoints
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	// Irá pegar a implementação UserDetailsServiceImpl
	@Autowired
	private UserDetailsService userDetailsService; 
	
	@Autowired
	private Environment env;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	private static final String[] PUBLIC_MATCHERS = {
			"/h2-console/**"
	};
	
	private static final String[] PUBLIC_MATCHERS_GET = {
			"/produtos/**",
			"/categorias/**"
	};
	
	private static final String[] PUBLIC_MATCHERS_POST = {
			"/clientes",
			"/auth/forgot**"
	};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// Se estiver no perfil de test, será liberado o acesso ao bd H2
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
            http.headers().frameOptions().disable();
        }
		
		http.cors() // Ativando o Bean corsConfigurationSource (Para mais informações, material de apoio 06-autenticacao)
			.and().csrf().disable(); // Como não armazenamos sessão, desabilitamos o ataque csrf (Baseado no armazenamento de sessão) 
		
		// Permitindo todos os caminhos que estiver no PUBLIC_MATCHERS
		// anyRequest().authenticated() Para todo o resto, exige autenticação
		http.authorizeRequests()
			.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll() // Permitindo autorização de POST (Mesmo que não esteja logado)
			.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll() // Permitindo apenas para o metodo GET	
			.antMatchers(PUBLIC_MATCHERS).permitAll()
			.anyRequest().authenticated();
		
		// Registrando o filtro de autenticação
		// authenticationManager() já é um método disponível da classe WebSecurityConfigurerAdapter
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
		
		// Registrando o filtro de autorização
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
		
		// Assegurando que o backend não criará sessão de usuário
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Quem será capaz de buscar o usuário por email
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
	
	// Permitindo acesso aos endpoints com multiplas fontes com as configurações básicas
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
	
	// Disponibilizando para ser injetado em qualquer lugar do projeto
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
