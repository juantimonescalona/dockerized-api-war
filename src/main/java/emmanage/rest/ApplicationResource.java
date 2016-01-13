package emmanage.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import emmanage.model.ApplicationState;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Path("/application")
@Api(value = "application" )
@Produces({MediaType.APPLICATION_JSON})
public class ApplicationResource {
    @GET
    @ApiOperation(value="Gets application state")
    public ApplicationState getApplicationState() {
        return ApplicationState.DEPLOYED;
    }
    
    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value="Sets application state")
    public ApplicationState setApplicationState(ApplicationState state){
    	return state;
    }
}