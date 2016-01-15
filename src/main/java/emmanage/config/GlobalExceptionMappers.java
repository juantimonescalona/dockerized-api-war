package emmanage.config;

import java.io.FileNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

public class GlobalExceptionMappers {
	@Provider
	public static class FileNotFoundExceptionMapper implements ExceptionMapper<FileNotFoundException>{
		@Override
		public Response toResponse(FileNotFoundException e) {
			  return Response.status(404).
					  entity("not found").
					  type("text/plain").
					  build();
		}
		
	}
}
