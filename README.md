# OpenAPI Generator for Spring AI Functions

[![build](https://github.com/alexcheng1982/llm-openapi-function-spring-ai-generator/actions/workflows/build.yaml/badge.svg)](https://github.com/alexcheng1982/llm-openapi-function-spring-ai-generator/actions/workflows/build.yaml)
![Maven Central Version](https://img.shields.io/maven-central/v/io.github.alexcheng1982/openapi-function-spring-ai-generator)

Generate Spring
AI [functions](https://docs.spring.io/spring-ai/reference/api/functions.html)
from OpenAPI spec to
be used with LLMs. Connect existing APIs with LLMs.

Each OpenAPI operation will be converted into a `Function` and register to
Spring context.

`native` library is used to minimize external dependencies.

See [example](example).

## Code Generation

See [pom.xml](example/pom.xml) of the example. Add the custom generator as a
dependency of OpenAPI Generator Maven plugin.

```xml

<dependency>
  <groupId>io.github.alexcheng1982</groupId>
  <artifactId>openapi-function-spring-ai-generator</artifactId>
  <version>1.0.0</version>
</dependency>
```

For each API defined in OpenAPI spec, a Spring `Configuration` will be
generated. For each operation, the generator will generate:

* A record for request type.
* A record for response type.
* A bean of `Function` to invoke the corresponding operation.

See the sample configuration below.

```java

@Configuration
public class UniversitiesApiSpringAiFunctionConfiguration {

  public record Request_searchUniversities(String country, Integer limit) {

  }

  public record Response_searchUniversities(List<University> value) {

  }

  @Bean
  @Description("Search universities")
  public Function<Request_searchUniversities, Response_searchUniversities> searchUniversities(
      UniversitiesApi apiInvoker) {
    return (request) -> {
      try {
        return new Response_searchUniversities(
            apiInvoker.searchUniversities(request.country(), request.limit()));
      } catch (ApiException e) {
        throw new RuntimeException(e);
      }
    };
  }
}
```

## Use Generated Client

To use the generated OpenAPI client in Spring AI:

1. Add the library into your project.

2. Import the `Configuration` or use component scanning.

```java
@Import({
    UniversitiesApiSpringAiFunctionConfiguration.class
})
```

3. Declare an API bean.

```java

@Bean
public UniversitiesApi universitiesApi() {
  return new UniversitiesApi();
}
```

4. Use the function in Spring AI.

See [here](https://docs.spring.io/spring-ai/reference/api/clients/functions/openai-chat-functions.html)
for a guide of how to use functions with OpenAI.

`searchUniversities` is the name of an operation defined in the OpenAPI spec,
which is also the name of function to call.

```java
OpenAiChatClient chatClient = ...

UserMessage userMessage = new UserMessage(
    "Find 10 universities in United Kingdom, output the name and website in CSV format.");

ChatResponse response = chatClient.call(new Prompt(List.of(userMessage),
    OpenAiChatOptions.builder().withFunction("searchUniversities")
        .build())); // (1) Enable the function
```