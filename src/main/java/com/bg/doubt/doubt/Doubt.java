package com.bg.doubt.doubt;

import com.bg.doubt.Player.Player;
import com.bg.doubt.Player.PlayerAndCard;
import com.bg.doubt.Player.PlayerProfile;
import com.bg.doubt.card.CardList;
import com.bg.doubt.card.CardSetter;
import com.bg.doubt.gameMessage.GameStatus;
import com.bg.doubt.gameMessage.RoomStatus;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Doubt {
    private String roomId;
    private String roomName;
    private final ArrayList<Player> players;
    private final LinkedList<CardList> field;
    private final GameState gameState;
    private int turn;

    private static final String[] ordered = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

    public Doubt(String id, String name) {
        this.players = new ArrayList<>();
        this.field = new LinkedList<>();

        this.roomId = id;
        this.roomName = name;
        this.gameState = new GameState();
        turn = 0;
    }

    public String getRoomName() {
        return roomName;
    }

    public int numberOfPlayer(){
        return players.size();
    }

    private Player findPlayerById(String playerId){
        for (Player player : players) {
            if(player.getId().equals(playerId)){
                return player;
            }
        }

        return Player.EmptyPlayer;
    }

    private RoomStatus getRoomState(String playerId){
        int topCards = 0;

        if(!field.isEmpty()){
            topCards = field.peekLast().getSize();
        }

        return RoomStatus.builder()
                .playerAndCards(players.stream()
                        .map(Player::getInfo)
                        .toArray(PlayerAndCard[]::new))
                .turnPlayer(getTurnId())
                .FieldNum(topCards)
                .myCards(findPlayerById(playerId).getCards())
                .build();
    }

    public void join(Player player) throws Exception {
        if(players.size() >= 4){
            throw new Exception("Player already full");
        }

        players.add(player);
    }

    public SendCardData sendCard(String playerId ,CardList inputCards) throws Exception {
        synchronized (gameState){
            if(!gameState.canSendCard(playerId)){
                throw new Exception("not your turn");
            }
            gameState.setNow(NowProcess.STANDBY_DOUBT);
        }

        Player player =  findPlayerById(playerId);
        if(player.equals(Player.EmptyPlayer)){
            throw new Exception("Player Not Found");
        }

        if(!player.getId().equals(playerId)){
            throw new Exception("Player MisMatch");
        }

        int result = player.sendCards(inputCards);
        field.add(inputCards);

        if(result == 0){
            //해당 turn player의 승리
        }

        turn++;

        gameState.setTurnPlayerId(getTurnId());

        return SendCardData.builder()
                .playerId(playerId)
                .cardNum(ordered[(turn-1)%13])
                .numOfCards(inputCards.getSize())
                .nextCard(ordered[turn%13])
                .nextPlayer(getTurnId())
                .build();
    }

    private String getTurnId() {
        return players.get(turn%4).getId();
    }

    private boolean isMatch(List<String> last){
        String pattern = ordered[(turn-1) % 13];

        String regex = "^.+_" + pattern;

        return last.stream().allMatch(e -> e.matches(regex));
    }

    private Player getLosePlayer(boolean result, String playerId){
        if(result){
            return players.get((turn-1)%4);
        }

        return findPlayerById(playerId);
    }

    public DoubtResult callDoubt(String playerId, String value) throws Exception {
        synchronized (gameState){
            if(!gameState.canDoubt()){
                throw new Exception("not Doubt Timing");
            }
            gameState.setNow(NowProcess.TURN);
        }

        if(!Boolean.parseBoolean(value)){
            return DoubtResult.noDoubtInit(playerId);
        }

        DoubtResult dr = new DoubtResult();

        List<String> last = field.peekLast().getCards();
        dr.setLastCards(List.copyOf(last));

        boolean result = isMatch(last);
        Player player = getLosePlayer(result, playerId);
        player.gainCard(field);

        dr.setResult(result ? DoubtResultType.FAIL : DoubtResultType.SUCCESS);
        dr.setPlayerId(player.getId());

        field.clear();
        return dr;
    }

    /* test용 게임의 전체 상태를 나타냄 */
    public GameStatus getStatus() {
        return GameStatus.builder()
                .field(this.field)
                .players(this.players)
                .build();
    }

    public void gameStart() throws Exception {
        synchronized (gameState){
            if(!gameState.canStart()){
                return;
            }
            gameState.setNow(NowProcess.TURN);
            gameState.setTurnPlayerId(getTurnId());
        }

        boolean ready = players.stream().allMatch(Player::isReady);

        if(!ready){
            throw new Exception("Not All Ready");
        }

        List<Integer> initialOrder = setInitialOrder();

        for(int i = 0 ; i < players.size() ; ++i){
            List<Integer> sub = initialOrder.subList(i*13, (i*13 + 13));

            CardList cl = new CardList( CardSetter.getCards(sub) );

            players.get(i).gainCard(List.of(cl));
        }

    }

    private List<Integer> setInitialOrder() {
        List<Integer> arr = IntStream.range(0,52).boxed().collect(Collectors.toList());
        Collections.shuffle(arr);

        return arr;
    }

    public boolean gameReady(String playerId, String value) {
        Player player = findPlayerById(playerId);
        player.doReady(Boolean.parseBoolean(value));

        return player.isReady();
    }

    public RoomStatus getRoomStatusByPlayerId(String playerId) {
        return getRoomState(playerId);
    }

    public boolean isDuplicate(String playerId) {
        return !findPlayerById(playerId).equals(Player.EmptyPlayer);
    }

    public List<PlayerProfile> getPlayersProfile() {
        return players.stream()
                .map(PlayerProfile::getProfile)
                .collect(Collectors.toList());
    }

    public List<String> getPlayerIdAll() {
        return getPlayerIdWithout(null);
    }

    public List<String> getPlayerIdWithout(String playerId){
        return players.stream()
                .map(Player::getId)
                .filter(p -> !p.equals(playerId))
                .collect(Collectors.toList());
    }
}
