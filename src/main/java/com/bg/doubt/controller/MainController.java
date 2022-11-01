package com.bg.doubt.controller;

import com.bg.doubt.doubt.DoubtService;
import com.bg.doubt.gameMessage.GameStatus;
import com.bg.doubt.security.TokenManager;
import com.bg.doubt.user.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@Slf4j
public class MainController {
    DoubtService doubtService;
    UserService userService;

    @Autowired
    public MainController(DoubtService doubtService, UserService userService) {
        this.doubtService = doubtService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String main() {

        return "/index.html";
    }

    @GetMapping("/games")
    public String gamepage() {
        return "/gamelist.html";
    }

    @GetMapping("/rooms/list")
    @ResponseBody
    public List<RoomElement> getGamerooms(){

        List<RoomElement> roomList = doubtService.getRoomList();

        return roomList;
    }

    @GetMapping("/games/{gameName}/rooms")
    public String getGameroomByRoomId(@PathVariable("gameName") String gameName, String roomId){
        if(doubtService.findRoomByRoomId(roomId).isEmpty()){
            return  "redirect:/games";
        }

        return  "/" + gameName + ".html";
    }

    @PostMapping("/games/{gameName}/rooms")
    @ResponseBody
    public String createGameroom(@PathVariable("gameName") String gameName, @RequestBody String roomName){
        log.info("roomName : " + roomName);

        String roomId = doubtService.createGameroom(gameName, roomName);

        log.info("Created roomId : " + roomId);

        return  roomId;
    }

    @GetMapping("/games/{gameName}/rooms/{roomId}")
    @ResponseBody
    public String isPlayerDuplicate(@PathVariable("gameName") String gameName, @PathVariable("roomId") String roomId, String playerId){

        boolean result = doubtService.isDuplicate(roomId, playerId);

        return String.format("{\"result\":\"%s\"}", result);
    }

    @GetMapping("/check/gameBoard/{id}")
    @ResponseBody
    public GameStatus aa(@PathVariable String id){
        return doubtService.getGameStatus(id);
    }

    @GetMapping("/signup")
    public String signupPage(){
        return "/signup.html";
    }

    @PostMapping("/users")
    public String joinUser(UserDto user){
        UserDto userDto = userService.JoinUser(user);

        return "redirect:/afterSignup.html?username=" + userDto.getUsername();
    }

    @GetMapping("/userId")
    @ResponseBody
    public String getUserId(@CookieValue("accessToken") String accessToken, TokenManager tokenManager){
        String userId = tokenManager.getUserId(accessToken);

        return String.format("{\"userId\":\"%s\"}", userId);
    }
}
