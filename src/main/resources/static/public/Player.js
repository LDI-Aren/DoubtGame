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
}

class MyPlayer extends Player{
    constructor(id, name) {
        super(id, name, document.querySelector(".Hands"));
    }
}

class OtherPlayer extends Player{
    constructor(id, name, DOM) {
        super(id, name, DOM);
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
     * */
    joinPlayer(id, name) {
        this.players[id] = new Player(id, name, this.playerDOM.querySelector(`#Empty`));

        if(id === this.myId){
            return;
        }

        if(joinPlayerDom === null){
            return;
        }

        joinPlayerDom.classList.remove("Empty");
        joinPlayerDom.innerHTML = name;
    }

    isAllReady(){
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
        this.players[id].ready = ready;

        if(ready) {
            joinPlayerDom.classList.add("Ready");
        } else {
            joinPlayerDom.classList.remove("Ready");
        }
    }
}