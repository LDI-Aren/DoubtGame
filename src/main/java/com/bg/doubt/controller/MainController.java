package com.bg.doubt.controller;

import com.bg.doubt.doubt.DoubtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
public class MainController {
    DoubtService doubtService;

    @Autowired
    public MainController(DoubtService doubtService) {
        this.doubtService = doubtService;
    }

    @GetMapping("/")
    public ModelAndView main(String gameName) throws IOException {
        ModelAndView mav = new ModelAndView();

        String viewName = "gamelist.html";

        if(gameName != null){
            viewName = "gamelist.html";
        }

        mav.setViewName(viewName);
        return mav;
    }

    @GetMapping("/games/{gameName}/rooms/list")
    @ResponseBody
    public List<RoomElement> getGamerooms(@PathVariable("gameName") String gameName){

        List<RoomElement> roomList = doubtService.getRoomList();

        return roomList;
    }

    @GetMapping("/games/{gameName}/rooms")
    public String getGameroomByRoomId(@PathVariable("gameName") String gameName, String roomId){
        if(doubtService.findRoomByRoomId(roomId).isEmpty()){
            return  "/" + gameName + ".html";
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
}
