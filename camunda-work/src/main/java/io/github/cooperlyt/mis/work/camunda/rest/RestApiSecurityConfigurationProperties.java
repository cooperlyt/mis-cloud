package io.github.cooperlyt.mis.work.camunda.rest;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Complete Security Configuration Properties for Camunda REST Api.
 */
@Getter
@Component
@ConfigurationProperties(prefix = "rest.security")
@Validated
public class RestApiSecurityConfigurationProperties {

	/** 
	 * rest.security.enabled:
	 * 
	 * Rest Security is enabled by default. Switch off by setting this flag to {@code false}.
	 * -- GETTER --
	 *
	 * @return the enabled

	 */
	private Boolean enabled = true;

	/** 
	 * rest.security.provider:
	 * 
	 * The name of the spring.security.oauth2.client.provider to use
	 * -- GETTER --
	 *
	 * @return the provider

	 */
	@NotEmpty
	private String provider;

	/** 
	 * rest.security.required-audience:
	 * 
	 * Required Audience.
	 * -- GETTER --
	 *
	 * @return the requiredAudience

	 */
	@NotEmpty
	private String requiredAudience;
	
	// ------------------------------------------------------------------------

	/**
	 * @param requiredAudience the requiredAudience to set
	 */
	public void setRequiredAudience(String requiredAudience) {
		this.requiredAudience = requiredAudience;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @param provider the provider to set
	 */
	public void setProvider(String provider) {
		this.provider = provider;
	} 

}
