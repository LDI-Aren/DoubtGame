class DoubtCall{
    constructor() {
        this.isNoDoubt = false;
        this.doubtResultFunction = {
            "NODOUBT" : this.noDoubt,
            "SUCCESS" : this.successDoubt,
            "FAIL" : this.failDoubt
        }
    }

    doubtResult(id, data) {
        this.isNoDoubt = false;

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

    noDoubt(id, data){
        field.setTurn();
        buttons.choiceButton("send");
    }

    successDoubt(id, data){
        console.log("success");
        console.log(id);
        console.log(data);

        field.successDoubt(id, data);
        this.doubtProcess(data.playerId);
    }

    failDoubt(id ,data){
        console.log("fail");
        console.log(id);
        console.log(data);

        field.failDoubt(id, data);
        this.doubtProcess(data.playerId);
    }

    doubtProcess(playerId){
        /*
        * playerId 의 player에게 카드를 추가
        * 모든 플레이어에게 마지막에 나왔던 카드 보여주기
        * 다음 턴 진행
        *
        * + 카드를 받아야하는 플레이어에게 받아야하는 카드 보내주기
        * */
    }
}