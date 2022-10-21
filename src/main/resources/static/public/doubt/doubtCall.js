class DoubtCall{
    constructor() {
        this.isNoDoubt = false;
        this.doubtResultFunction = {
            "NODOUBT" : this.doNextTurn,
            "SUCCESS" : this.successDoubt,
            "FAIL" : this.failDoubt
        }

        this.isFinish = false;
    }

    doubtResult(id, data) {
        this.isNoDoubt = false;

        this.isFinish = data.isFinish;
        this.doubtResultFunction[data.result](id, data);
    }

    doubt(){
        stompClient.send(`/message/doubt/${params.roomId}`,{}, JSON.stringify({"type" : "DOUBT" , "playerId" : playerId, "value" : true}));
    }

    countDoubt(){
        setTimeout(()=>this.doubtTimeOver(this), 5000);
        this.isNoDoubt = true;
    }

    doubtTimeOver(obj){
        if(!obj.isNoDoubt){
            return;
        }

        stompClient.send(`/message/doubt/${params.roomId}`,{}, JSON.stringify({"type" : "DOUBT" , "playerId" : playerId, "value" : false}));
    }

    doNextTurn(){
        console.log(this.isFinish);

        if(doubtCall.isFinish){
            console.log("game is finish");
            getFinishGameData();
            return;
        }

        field.setTurn();
        buttons.choiceButton("send");
    }

    successDoubt(id, data){
        console.log("success");
        console.log(id);
        console.log(data);

        field.successDoubt(id, data);

        if(data.playerId === playerId){
            hands.getGainCards();
        }

        playerManager.getDoubtCards(data.playerId);

        setTimeout(doubtCall.doNextTurn,5000);
    }

    failDoubt(id ,data){
        console.log("fail");
        console.log(id);
        console.log(data);

        field.failDoubt(id, data);

        if(data.playerId === playerId){
            hands.getGainCards();
        }

        playerManager.getDoubtCards(data.playerId);

        setTimeout(doubtCall.doNextTurn,5000);
    }


}