package emmanage.rest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;

import emmanage.model.LogInfo;
import emmanage.service.LogsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/logs")
@Api(value = "logs" )
@Produces({MediaType.APPLICATION_JSON})
public class LogsResource {

	@Autowired
	LogsService logService;
	
	@Context
    UriInfo uriInfo;
	
    @GET
    @ApiOperation(value="Gets available extension files")
    public List<LogInfo> getLogs() throws IOException {
    	List<File> files = logService.getFiles();
    	List<LogInfo> retVal = files.stream().map((x) -> new LogInfo(x,uriInfo)).collect(Collectors.toList());
		return retVal;
    }
    
    @GET
    @Path("/{id:.*\\.log}")
    @ApiOperation(value="Gets metadata about a particular log file")
    @ApiResponses( value={
    		@ApiResponse(code=200, message = "success", response=LogInfo.class),
    		@ApiResponse(code=404, message = "cannot read log file")})
    public LogInfo getLogInfo(@PathParam("id") String id) throws IOException{
    	return new LogInfo(logService.getFile(id), uriInfo);
    }
    
    @GET
    @Path("/{id:.*\\.log}/data")
    @Produces({MediaType.TEXT_PLAIN})
    @ApiOperation(value="Gets a particular log file contents")
    @ApiResponses( value={
    		@ApiResponse(code=200, message = "success"),
    		@ApiResponse(code=404, message = "cannot read log file")})
    public Response getData(@PathParam("id") String id) throws IOException{
        return Response.ok(logService.getFile(id))
                .header("Content-Disposition", "attachment; filename=\""+id+"\"")
                .build();
    }
}