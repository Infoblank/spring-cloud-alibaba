package com.ztt.cloudgateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 跨域支持
 */
@Configuration
public class CloudCorsConfiguration {

	@Bean
	public CorsConfiguration corsConfig() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		// 允许跨域的地址使用逗号分割 127.0.0.1:9008,27.0.0.1:9009
		corsConfiguration.addAllowedOrigin(CorsConfiguration.ALL); // 允许访问的域
		corsConfiguration.addAllowedHeader(CorsConfiguration.ALL); // 允许的头信息
		corsConfiguration.addAllowedMethod(CorsConfiguration.ALL); // 允许的HTTP方法
		corsConfiguration.setAllowCredentials(true); // 是否允许证书信息
		corsConfiguration.setMaxAge(3600L); // 预检请求的缓存时间（秒）
		return corsConfiguration;
	}

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public CorsWebFilter corsWebFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig()); // 对所有路径应用跨域配置
		return new CorsWebFilter(source);
	}
}
