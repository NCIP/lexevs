package org.lexevs.cts2;

public class ServiceInfo {

	private String serviceName = "LexEVSCTS2Impl";
	private String serviceDescription = "LexEVS CTS2 Implementation";;
	private String serviceProvider = "Mayo Clinic";
	private String serviceVersion = "1.0";

	public ServiceInfo(){
	}

	public void finalize() throws Throwable {
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