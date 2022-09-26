package com.bg.doubt.doubt;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class DoubtResult {
    private DoubtResultType result;
    private List<String> lastCards;

    private String playerId;

    DoubtResult(){
        result = DoubtResultType.NODOUBT;
    }
}

enum DoubtResultType{
    NODOUBT, SUCCESS, FAIL
}