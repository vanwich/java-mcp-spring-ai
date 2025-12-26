package demo.mcp.server;

import demo.mcp.ai.PromptExamples;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * A tiny, beginner-friendly Model Context Protocol server skeleton.
 *
 * <p>This class keeps the focus on plain Java types so that new users can
 * understand the moving parts even before they have downloaded the official
 * MCP Java SDK. When the SDK is available on the classpath the server logs a
 * reminder about where to integrate the real transport and registration code.</p>
 */
@Component
public class BeginnerMcpServer {

    private static final Logger logger = LoggerFactory.getLogger(BeginnerMcpServer.class);

    private final List<ToolDefinition> tools;
    private final PromptExamples prompts;

    public BeginnerMcpServer(PromptExamples prompts) {
        this.prompts = prompts;
        this.tools = List.of(
                new ToolDefinition(
                        "echo",
                        "Return whatever text the user sends.",
                        payload -> Map.of("echo", payload.getOrDefault("text", ""))),
                new ToolDefinition(
                        "clock",
                        "Return the current UTC time in ISO-8601 format.",
                        payload -> Map.of("now", Instant.now().toString()))
        );
    }

    /**
     * This method intentionally stays side-effect free so that the project can
     * run even when the MCP SDK artifacts have not been downloaded yet (which
     * can happen in restricted CI environments). Once the SDK jars are
     * available, the message reminds you where to plug them in.
     */
    public void start() {
        if (isMcpSdkPresent()) {
            logger.info("Detected the MCP Java SDK on the classpath. Replace the placeholder" +
                    " bootstrap in BeginnerMcpServer.start() with real server wiring when you" +
                    " are ready to expose tools, prompts, and resources.");
        } else {
            logger.warn("MCP Java SDK is not yet available (network restricted builds often" +
                    " skip Maven downloads). Running in dry-run mode so you can still explore" +
                    " the code and Spring AI prompts.");
        }

        logger.info("Registered tools for this demo:");
        tools.forEach(tool -> logger.info("- {}: {}", tool.name(), tool.description()));

        logger.info("Example prompt you can feed into a model once connected:\n{}",
                prompts.buildIntroPrompt(tools));
    }

    private boolean isMcpSdkPresent() {
        try {
            Class.forName("org.modelcontextprotocol.sdk.server.McpServer");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    public List<ToolDefinition> getTools() {
        return tools;
    }
}
