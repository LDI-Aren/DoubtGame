package com.bg.doubt.doubt;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class DoubtResult {
    private boolean result;
    private List<String> lastCards;

    private String playerId;
}
