package com.bg.doubt.doubt;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class DoubtResult {
    private DoubtResultType result;
    private List<String> lastCards;

    private String playerId;

    private boolean isFinish;

    public static DoubtResult noDoubtInit(String playerId){
        DoubtResult dr = new DoubtResult();
        dr.setPlayerId(playerId);
        dr.setResult(DoubtResultType.NODOUBT);
        return dr;
    }
}

enum DoubtResultType{
    NODOUBT, SUCCESS, FAIL;
}