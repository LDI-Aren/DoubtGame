class Player{
    /**
     * @param id : String  플레이어의 아이디
     * @param DOM : HTMLElement
     * */
    constructor(id,  DOM) {
        this.id = id;
        this.DOM = DOM;
        this.cards = 0;
        this.ready = false;
    }

    setReady(ready){
        this.ready = ready;
        let playerStatus = this.DOM.querySelector("[data-status]");
        if(ready){
            console.log("r")
            playerStatus.innerHTML = "READY!";
        } else {
            console.log("c")
            playerStatus.innerHTML = "준비해주세요";
        }
    }

    setStatus(numOfCards){
        this.DOM.querySelector("[data-status]").innerHTML = `cards : ${numOfCards}`;
    }
}

class MyPlayer extends Player{
    /**
     * @param id : String  플레이어의 아이디
     * */
    constructor(id) {
        super(id, document.getElementById("Status"));
    }
}

class PlayerManager{
    constructor(playerDOM, myId) {
        this.playerDOM = playerDOM;
        this.myId = myId;
        this.players = {};
    }

    /**
     * @param id : String  플레이어의 아이디
     * @param data : String room안에 있던 player들의 정보
     * */
    joinPlayer(id, data) {

        if(id === this.myId){
            this.setPlayerData(id, data);
            return;
        }

        let joinPlayerDom = this.playerDOM.querySelector(`.Empty`);

        if(joinPlayerDom === null){
            return;
        }

        this.players[id] = new Player(id, joinPlayerDom);

        joinPlayerDom.classList.remove("Empty");
        joinPlayerDom.querySelector(".player-name").innerHTML = id;
        joinPlayerDom.querySelector(".player-status").innerHTML = "준비해주세요";

    }

    setPlayerData(id, data){
        let playerData = JSON.parse(data);
        this.players[id] = new MyPlayer(id);
        for(let player in playerData){
            this.joinPlayer(playerData[player].playerId, "");
            this.setPlayerReady(playerData[player].playerId, playerData[player].isReady);
        }
    }

    isAllReady(){
        if(Object.keys(this.players).length !== 4){
            return false;
        }

        let isReady = true;

        for(let n in this.players){
            isReady = isReady && this.players[n].ready;
        }

        return isReady;
    }

    /**
     * @param id : string 플레이어 아이디
     * @param ready : boolean ready여부
     */
    setPlayerReady(id, ready){
        console.log("in manager = " + id + " : " + ready);
        this.players[id].setReady(ready);
        console.log("after Setting")
    }

    /**
     * @param data : Object [{playerId , numOfCards},...]
     */
    setGameStatus(data) {
        for(let n in data){
            this.players[data[n].playerId].setStatus(data[n].numOfCards);
        }
    }
}