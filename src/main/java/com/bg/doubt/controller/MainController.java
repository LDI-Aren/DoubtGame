package com.bg.doubt.controller;

import com.bg.doubt.doubt.DoubtService;
import com.bg.doubt.gameMessage.GameStatus;
import com.bg.doubt.user.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    public ModelAndView main(String gameName) {
        ModelAndView mav = new ModelAndView();

        String viewName = "index.html";

        if(gameName != null){
            viewName = "gamelist.html";
        }

        mav.setViewName(viewName);
        return mav;
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
            return  "/gamelist.html";
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
    public String createGameroom(@PathVariable("gameName") String gameName, @PathVariable("roomId") String roomId, String playerId){

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
    @ResponseBody
    public String joinUser(@RequestBody UserDto user){
        UserDto userDto = userService.JoinUser(user);

        return userDto.getUserId();
    }
}
