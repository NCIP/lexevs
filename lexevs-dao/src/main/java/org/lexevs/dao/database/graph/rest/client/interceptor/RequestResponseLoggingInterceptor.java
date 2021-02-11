
package org.lexevs.dao.database.graph.rest.client.interceptor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import java.io.IOException;
import java.io.InputStream;
 
public class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {
      
    private final Logger log = LoggerFactory.getLogger(this.getClass());
  
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException 
    {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
 
        //Add optional additional headers
        response.getHeaders().add("headerName", "VALUE");
 
        return response;
    }
    
    private String getResponseBody(InputStream in){
    	String body = null;
		try {
			body = IOUtils.toString(in,"UTF-8");
		} catch (IOException e) {
			System.out.println(e.getCause());
		}
    	return body;
    }
  
    private void logRequest(HttpRequest request, byte[] body) throws IOException 
    {

            System.out.println("===========================request begin================================================");
            System.out.println("URI         :" + request.getURI());
            System.out.println("Method      :" + request.getMethod());
            System.out.println("Headers     :" + request.getHeaders());
            System.out.println("Request body:" + new String(body, "UTF-8"));
            System.out.println("==========================request end================================================");

    }
  
    private void logResponse(ClientHttpResponse response) throws IOException 
    {

            System.out.println("============================response begin==========================================");
            System.out.println("Status code  :" + response.getStatusCode());
            System.out.println("Status text  :" + response.getStatusText());
            System.out.println("Headers      :" + response.getHeaders());
            System.out.println("ResponseBody :" + getResponseBody(response.getBody()));
    }
}