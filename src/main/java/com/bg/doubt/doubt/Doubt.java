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
    private String roomName;
    private final ArrayList<Player> players;
    private final LinkedList<CardList> field;
    private int turn;
    private boolean isInit;

    private static final String[] ordered = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

    public Doubt() {
        this.players = new ArrayList<>();
        this.field = new LinkedList<>();
        turn = 0;
        isInit = false;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int numberOfPlayer(){
        return players.size();
    }

    private Player findPlayerById(String playerId){
        for (Player player : players) {
            System.out.println(playerId + " : " + player.getId());
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

    public DoubtResult callDoubt(String playerId){
        DoubtResult dr = new DoubtResult();

        List<String> last = field.peekLast().getCards();
        dr.setLastCards(List.copyOf(last));

        boolean result = isMatch(last);
        Player player = getLosePlayer(result, playerId);
        player.gainCard(field);

        dr.setResult(result);
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
        boolean ready = players.stream().allMatch(Player::isReady);

        if(!ready){
            throw new Exception("Not All Ready");
        }

        List<Integer> initialOrder = setInitialOrder();

        for(int i = 0 ; i < players.size() ; ++i){
            List<Integer> sub = initialOrder.subList(i*13, (i*13 + 13));
            System.out.println(sub);

            CardList cl = new CardList(
                    CardSetter.getCards(
                            sub
                    )
            );

            players.get(i).gainCard(List.of(cl));
        }

        isInit = true;
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

    public List<String> getDestinationPlayerId(String playerId) {
        return players.stream()
                .map(Player::getId)
                .filter(id -> !id.equals(playerId))
                .collect(Collectors.toList());
    }

    public boolean isInit() {
        return isInit;
    }
}
