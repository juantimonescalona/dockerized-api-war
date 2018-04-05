package emmanage.rest;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import emmanage.model.LogInfo;
import emmanage.service.LogsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ResponseHeader;

@Path("/logs")
@Api(value = "logs" )
@Produces({MediaType.APPLICATION_JSON})
public class LogsResource {

	@Autowired
	LogsService logService;
	
	@Context
    UriInfo uriInfo;
	
    @GET
    @ApiOperation(value="Gets available log files")
    public List<LogInfo> getLogs() throws IOException {
    	List<File> files = logService.getFiles();
    	List<LogInfo> retVal = files.stream().map((x) -> new LogInfo(x,uriInfo,true)).collect(Collectors.toList());
		return retVal;
    }

	@GET
	@Path("/dummy")
	@Produces({MediaType.TEXT_PLAIN})
	@ApiOperation(value="Gets metadata about a log file")
	@ApiResponses( value={
			@ApiResponse(code=200, message = "success", response=Response.class),
			@ApiResponse(code=404, message = "Error")})
	public Response getDummy(@PathParam("operacion") @ApiParam("Operacion a recibir, con valores OK, KO, WAIT o con parametro vacio devuelve una de las 3") String operacion) throws IOException{
		ResponseBuilder responseBuilder;
		responseBuilder = Response.ok().status(Status.NO_CONTENT);

    	return responseBuilder.build();
	}
    
    @GET
    @Path("/{id:.*\\.log}")
    @ApiOperation(value="Gets metadata about a log file")
    @ApiResponses( value={
    		@ApiResponse(code=200, message = "success", response=LogInfo.class),
    		@ApiResponse(code=404, message = "cannot read log file")})
    public LogInfo getLogInfo(@PathParam("id") @ApiParam("log id") String id) throws IOException{
    	return new LogInfo(logService.getFile(id), uriInfo, false);
    }
    
    @GET
    @Path("/{id:.*\\.log}/data")
    @Produces({MediaType.TEXT_PLAIN})
    @ApiOperation(value="Gets log file contents, optionally from an offset position specified.")
    @ApiResponses( value={
    		@ApiResponse(code=200, message = "success",
    				responseHeaders = {
    						@ResponseHeader(
    								name="Link",
    								response = String.class,
    								description="two rfc5988 links, the one with rel=next let you get next chunk of data"
    						)
    				}
    		),
    		@ApiResponse(code=204, message = "no data available (an offset)"),
    		@ApiResponse(code=404, message = "cannot read log file")}
    		)
    public Response getData(@PathParam("id") @ApiParam("log id") String id, 
    		                @QueryParam("offset") @DefaultValue("0") @ApiParam("starting offset, can be negative to be counted from the end of log file") long offset
    		                ) throws IOException{
        final  File file = logService.getFile(id);
    	final long to = file.length();
    	if (offset<0){
    		// return from the end of file
    		offset = to + offset;
    	}
		if (offset>to){
			// requested file length is greater, return the whole file
			offset = 0;
		} 
		
    	// create a response
		ResponseBuilder responseBuilder;
    	if (to == offset){
    		responseBuilder = Response.ok().status(Status.NO_CONTENT);
    	} else{
    		final long start = offset;
    		responseBuilder = Response.ok(new StreamingOutput(){
				@Override
				public void write(OutputStream output) throws IOException, WebApplicationException {
	                try(RandomAccessFile raf = new RandomAccessFile(file, "r")){
		                raf.seek(start);
		                byte[] buffer = new byte[4096];
		                int bytesRead;
		                long offset = start;
		                
		                while((offset < to) 
		                	&& ((bytesRead = raf.read(buffer,0,offset+buffer.length>to?(int)(to-offset):buffer.length)) >= 0) ){
		                	offset+=bytesRead;
		                	output.write(buffer,0,bytesRead);
		                }
	                };
				}
    		}).status(Status.OK);
    	}
    	if (offset  == 0L){
    		// add file name in case that we are reading from the beginning
    		responseBuilder.header("Content-Disposition", "attachment; filename=\""+id+"\"");
    	}
    	return responseBuilder
    		.link(
    			uriInfo.getAbsolutePathBuilder().queryParam("offset", offset).build(),
    			"current")
    	    .link(
    			uriInfo.getAbsolutePathBuilder().queryParam("offset", to).build(),
    			"next")
    	    .build();
    }
}