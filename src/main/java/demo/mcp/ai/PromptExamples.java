package demo.mcp.ai;

import demo.mcp.server.ToolDefinition;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;

/**
 * Small helper that shows how to use Spring AI prompt templates without
 * requiring a live model provider. The formatted text can be pasted into any
 * MCP-compatible client once you have a large language model wired up.
 */
@Component
public class PromptExamples {

    public String buildIntroPrompt(List<ToolDefinition> tools) {
        var prompt = new PromptTemplate("""
                You are connected to a Spring Boot MCP server that exposes the following tools:
                {toolTable}
                When a user asks for help, pick the most relevant tool and explain how you are
                going to call it before you return the result.
                """);

        String toolTable = tools.stream()
                .map(tool -> "- " + tool.name() + ": " + tool.description())
                .collect(Collectors.joining("\n"));

        return prompt.render(Map.of("toolTable", toolTable));
    }
}
