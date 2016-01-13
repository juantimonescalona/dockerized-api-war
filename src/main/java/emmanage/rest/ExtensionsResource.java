package emmanage.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.beans.factory.annotation.Autowired;

import emmanage.model.ExtensionInfo;
import emmanage.service.ExtensionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/extensions")
@Api(value = "extensions" )
@Produces({MediaType.APPLICATION_JSON})
public class ExtensionsResource {
	@Autowired
	ExtensionsService extensionsService;
	@Context
	UriInfo uriInfo;
	
    @GET
    @ApiOperation(value="Gets list of extensions")
    public List<ExtensionInfo> getExtensions() throws IOException {
    	List<File> files = extensionsService.getFiles();
    	List<ExtensionInfo> retVal = files.stream().map((x) -> new ExtensionInfo(x,uriInfo)).collect(Collectors.toList());
		return retVal;
    }
    
    @GET
    @Path("/{id:.*\\.jar}")
    @ApiOperation(value="Gets metadata about an extension")
    @ApiResponses( value={
    		@ApiResponse(code=200, message = "success", response=ExtensionInfo.class),
    		@ApiResponse(code=404, message = "cannot read extension file")})
    public ExtensionInfo getExtensionInfo(@PathParam("id") String id) throws IOException{
    	return new ExtensionInfo(extensionsService.getFile(id), uriInfo);
    }

    @GET
    @Path("/{id:.*\\.jar}/data")
    @Produces({MediaType.APPLICATION_OCTET_STREAM})
    @ApiOperation(value="Gets extension file")
    @ApiResponses( value={
    		@ApiResponse(code=200, message = "success"),
    		@ApiResponse(code=404, message = "cannot read log file")})
    public Response getData(@PathParam("id") String id) throws IOException{
        return Response.ok(extensionsService.getFile(id))
                .header("Content-Disposition", "attachment; filename=\""+id+"\"")
                .build();
    }

    @PUT
    @Path("/{id:.*\\.jar}/data")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @ApiOperation(value="Uploads an extension file")
    public Response uploadFile(@PathParam("id") String id, InputStream stream){
    	// TODO implement extension upload, TODO restrict file size for upload
    	
    	return null;
    }
}