package io.github.alexcheng1982.openapifunction.springai;

import org.openapitools.codegen.CodegenConfig;
import org.openapitools.codegen.languages.JavaClientCodegen;

public class OpenapiFunctionSpringAiGenerator extends
    JavaClientCodegen implements CodegenConfig {

  /**
   * Configures a friendly name for the generator.  This will be used by the
   * generator to select the library with the -g flag.
   *
   * @return the friendly name for the generator
   */
  public String getName() {
    return "Java";
  }


  /**
   * Returns human-friendly help for the generator.  Provide the consumer with
   * help tips, parameters here
   *
   * @return A string value for the help message
   */
  public String getHelp() {
    return "Generates a openapi-function-spring-ai client library.";
  }

  public OpenapiFunctionSpringAiGenerator() {
    super();
    
    setLibrary(NATIVE);

    apiTemplateFiles.put("functionConfiguration.mustache",
        "SpringAiFunctionConfiguration.java");

  }

}
