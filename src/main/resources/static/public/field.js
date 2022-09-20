class Field{
    constructor(fieldDOM) {
        this.fieldDOM = fieldDOM;
        this.playerTurn = "";
        this.fieldCards = 0;
    }

    setTurn(playerId, nextCard){
        this.playerTurn = playerId;

        let notification = `${this.importantTag(playerId)}님의 차례입니다`
        notification += `<br>`
        notification += `${this.importantTag(nextCard)}를 낼 차례입니다.`

        this.fieldDOM.querySelector("#turn").innerHTML = notification;
    }

    importantTag(str){
        return `<span class="important-text">${str}</span>`
    }

    isMyTurn(){
        return playerId === this.playerTurn;
    }

    setCardNotification(data){
        this.fieldDOM.querySelector("#card-notification").innerHTML =
            `${this.importantTag(data.playerId)}님께서 ${this.importantTag(data.cardNum)}을 ${this.importantTag(data.numOfCards)}장 냈습니다.`;
    }

    setNumOfCards(numOfCards){
        this.fieldCards += numOfCards;
        this.fieldDOM.querySelector("#num-of-cards").innerHTML = this.fieldCards;
    }

    whenSEND(data){
        this.setCardNotification(data);
        this.setTurn(data.nextPlayer, data.nextCard);
        this.setNumOfCards(data.numOfCards);
    }

    whenSTART(turnPlayer) {
        this.setTurn(turnPlayer, "A");
    }
}