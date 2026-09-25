package org.taskmanagerapi.dtos.response;

import java.util.Map;

public record ErrorResponse(Map<String, String> response) {
}
