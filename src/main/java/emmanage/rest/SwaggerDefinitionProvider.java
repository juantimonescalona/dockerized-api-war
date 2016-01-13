package emmanage.rest;

import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

@SwaggerDefinition(
        info = @Info(
                description = "Management of EM application",
                version = "1.0.0",
                title = "EM Management REST API"
        ),
        tags = {
                @Tag(name = "application", description = "Management of EM application state"),
                @Tag(name = "extensions", description = "Management of EM application extensions"),
                @Tag(name = "logs", description = "Access to EM application logs")
        }
)
public interface SwaggerDefinitionProvider {
}
