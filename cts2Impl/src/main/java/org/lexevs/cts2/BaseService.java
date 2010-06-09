package org.lexevs.cts2;

public abstract class BaseService {
	private String serviceName;
	private String serviceDescription;
	private String serviceProvider;
	private String serviceVersion;
	
	public BaseService(){
		serviceName = "LexEVS CTS2 Implementation";
		serviceDescription = "LexEVS CTS2 Implementation";
		serviceProvider = "Mayo Clinic";
		serviceVersion = "1.0";
	}
	
	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**
	 * @return the serviceDescription
	 */
	public String getServiceDescription() {
		return serviceDescription;
	}
	/**
	 * @return the serviceProvider
	 */
	public String getServiceProvider() {
		return serviceProvider;
	}
	/**
	 * @return the serviceVersion
	 */
	public String getServiceVersion() {
		return serviceVersion;
	}
}
