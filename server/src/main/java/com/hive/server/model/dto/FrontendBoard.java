package com.hive.server.model.dto;

import java.util.List;

public record FrontendBoard(
        List<FrontendCell> cells
) {
}
