class Field{
    constructor(fieldDOM) {
        this.fieldDOM = fieldDOM;
        this.playerTurn = "";
        this.nextCard = "";
        this.fieldCards = 0;
    }

    setTurn(){
        let notification = `${this.importantTag(this.playerTurn)}님의 차례입니다`
        notification += `<br>`
        notification += `${this.importantTag(this.nextCard)}를 낼 차례입니다.`

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
        this.setStandByDoubt();
        this.setNumOfCards(data.numOfCards);

        this.playerTurn = data.nextPlayer;
        this.nextCard = data.nextCard;
    }

    setStandByDoubt(){
        this.fieldDOM.querySelector("#turn").innerHTML = "의심된다면 Doubt버튼을 눌러주세요";
    }

    whenSTART(turnPlayer) {
        this.playerTurn = turnPlayer;
        this.nextCard = "A";
        this.setTurn();
    }
}