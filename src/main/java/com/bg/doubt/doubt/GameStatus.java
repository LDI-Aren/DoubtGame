package com.bg.doubt.doubt;

import com.bg.doubt.Player.Player;
import com.bg.doubt.card.CardList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameStatus {
    List<Player> players;
    List<CardList> field;
}
