package ins.marianao.shipments.fxml.services;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import ins.marianao.shipments.fxml.manager.ResourceManager;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public abstract class ServiceDeleteBase<T> extends Service<Void>{

	protected T entity;
	protected String[] path;
	
	public ServiceDeleteBase(T entity, String[] path) throws Exception {
		if (entity == null) throw new Exception(ResourceManager.getInstance().getText("error.service.entity.null"));
		if (path == null || path.length == 0)  throw new Exception(ResourceManager.getInstance().getText("error.service.path.null")); 
		this.entity = entity;
		this.path = path;
	}

	@Override
	protected Task<Void> createTask() {
		
		return new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				Client client = ResourceManager.getInstance().getWebClient();

				WebTarget webTarget = client.target(ResourceManager.getInstance().getParam("web.service.host.url"));
				
				for (String part : path) {
					webTarget = webTarget.path(part);
				}
				
				Invocation.Builder invocationBuilder =  webTarget.request();

				try {
					Response response = invocationBuilder.delete();

					if (response.getStatus() != Response.Status.OK.getStatusCode()) 
						throw new Exception(ResourceManager.getInstance().responseErrorToString(response));
					
				} catch (ResponseProcessingException e) {
					e.printStackTrace();
					throw new Exception(ResourceManager.getInstance().getText("error.service.response.processing")+" "+e.getMessage());
				} catch (ProcessingException e) {
					e.printStackTrace();
					throw new Exception(ResourceManager.getInstance().getText("error.service.processing")+" "+e.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
				
				return null;
			}
		};
	}
	
}
