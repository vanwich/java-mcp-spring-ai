package demo.mcp.server;

import java.util.Map;
import java.util.function.Function;

/**
 * Minimal representation of a Model Context Protocol tool so beginners can see
 * the inputs and outputs without being overwhelmed by protocol details.
 */
public record ToolDefinition(
        String name,
        String description,
        Function<Map<String, Object>, Map<String, Object>> handler
) {
}
