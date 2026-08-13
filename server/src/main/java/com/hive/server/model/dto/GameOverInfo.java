package com.hive.server.model.dto;

import com.hive.server.model.enums.Colour;

public record GameOverInfo(
        Colour winner
) {
}
