class Player{
    /**
     * @param id : String  플레이어의 아이디
     * @param name : String 플레이어 이름
     * @param DOM : HTMLElement
     * */
    constructor(id, name, DOM) {
        this.id = id;
        this.name = name;
        this.DOM = DOM;
        this.cards = 0;
        this.ready = false;
    }

    setReady(ready){
        this.ready = ready;
        if(ready){
            console.log("r")
            this.DOM.classList.add("Ready");
        } else {
            console.log("c")
            this.DOM.classList.remove("Ready");
        }
    }
}

class MyPlayer extends Player{
    /**
     * @param id : String  플레이어의 아이디
     * @param name : String 플레이어 이름
     * */
    constructor(id, name) {
        super(id, name, document.getElementById("Status"));
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
     * @param name : String 플레이어 이름
     * @param data : String room안에 있던 player들의 정보
     * */
    joinPlayer(id, name, data) {

        if(id === this.myId){
            let playerData = JSON.parse(data);
            this.players[id] = new MyPlayer(id, name);
            for(let player in playerData){
                this.joinPlayer(playerData[player].playerId, playerData[player].playerName, "{}");
            }
            return;
        }
        let joinPlayerDom = this.playerDOM.querySelector(`.Empty`);

        this.players[id] = new Player(id, name, joinPlayerDom);

        if(joinPlayerDom === null){
            return;
        }

        joinPlayerDom.classList.remove("Empty");
        joinPlayerDom.innerHTML = name;
    }

    isAllReady(){
        if(Object.keys(this.players).length !== 4){
            return false;
        }

        let isReady = true;

        for(let k in this.players){
            isReady = isReady && this.players[k].ready;
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
}