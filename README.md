# Spring AI MCP Starter

This repo is a tiny, beginner-friendly Model Context Protocol (MCP) starter that
leans on Spring Boot, Java 17, and Spring AI 1.0.0-M6. It keeps the code small
and well-documented so you can see how tools, prompts, and a server bootstrap
fit together before adding your own model provider.

> **Why does the server start in “dry-run” mode?**
> The public MCP Java SDK artifacts live on snapshot repositories. If Maven
> cannot download them (for example, because the build environment blocks
> outbound internet access) the starter still runs and prints clear instructions
> instead of failing early.

## Requirements

- **Java 17**. The `maven-compiler-plugin` uses `--release 17` so you need a JDK
  with that target available.
- **Maven 3.6.3**. The project does not bundle the Maven Wrapper because this
  environment cannot download the wrapper JAR. Install Maven 3.6.3 locally (for
  example via [SDKMAN!](https://sdkman.io/) or `mise`) and run commands with
  that version.

## Dependency repositories

The MCP SDK is currently published as a snapshot. Add the Sonatype snapshots
repository to your global Maven `settings.xml` or keep the `<repositories>`
section in this POM. If your network blocks Maven Central or Sonatype, run with
`-Dmaven.repo.local=/path/to/offline/cache` that already contains the required
artifacts.

## Getting started

1. Install Java 17 and Maven 3.6.3.
2. Build the project once you have network access to Maven Central and the
   Sonatype snapshots repository:

   ```bash
   mvn clean package
   ```

3. Run the starter:

   ```bash
   java -jar target/java-mcp-server-0.2.0-RC1.jar
   ```

   On startup it lists the demo tools (echo and clock) and renders a prompt you
   can feed into any MCP-capable client.

## Wiring the real MCP server

`BeginnerMcpServer` is intentionally lightweight and logs a reminder when the
MCP SDK is present. Once the dependency resolves, replace the placeholder code
with the actual SDK bootstrap, for example:

```java
// Pseudocode – adapt to the concrete API from org.modelcontextprotocol.sdk
McpServer server = McpServer.builder()
    .name("spring-ai-beginner")
    .addTool("echo", tool -> tool.description("Return whatever text the user sends")
        .handler(params -> Map.of("echo", params.get("text"))))
    .addTool("clock", tool -> tool.description("Return the current UTC time")
        .handler(params -> Map.of("now", Instant.now().toString())))
    .transport(StdioServerTransport.create())
    .build();
server.start();
```

## Exploring prompts without a model provider

`PromptExamples` uses Spring AI’s `PromptTemplate` utility to render a friendly
intro prompt based on the tools that are registered. This keeps the focus on the
flow between MCP tools and LLM prompts without requiring API keys or a specific
model provider during the first run.
