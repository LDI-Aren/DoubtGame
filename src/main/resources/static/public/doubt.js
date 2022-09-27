class Hands {
    constructor(HandsDOM, cards){
        this.HandsDOM = HandsDOM;
        this.cards = cards;
        this.sendedCards = [];
    }

    pushHands(cardName){
        let target = this.HandsDOM.querySelector(`#${cardName}`);
        
        if(target !== null){
            return;
        }

        let copy = this.cards[cardName].cloneNode(true);
        copy.onclick = this.cards[cardName].onclick;

        this.HandsDOM.appendChild(copy);
    }

    getClickedHands(){
        let target = this.HandsDOM.querySelectorAll(`.ClickedHand`);
        console.log(target);

        if(target === null){
            return;
        }

        let cardNames = [];

        for(let e of target){
            cardNames.push(e.id);
            this.sendedCards.push(e);
        }

        return cardNames;
    }

    removeHands(){
        for(let e of this.sendedCards){
            e.remove();
        }
    }

    sortCards(){
        let items = this.HandsDOM.querySelectorAll(".Hand");

        [...items].sort((fir, sec) => {
            let a = fir.getAttribute("data-code").split("_");
            let b = sec.getAttribute("data-code").split("_");

            let compareNum = this.numOrder(a[1]) - this.numOrder(b[1]);
            let compareShape = this.shapeOrder(a[0]) - this.shapeOrder(b[0]);

            return (compareNum === 0) ? compareShape : compareNum;
        }).forEach(e => this.HandsDOM.appendChild(e));
    }

    shapeOrder(shape){
        switch (shape){
            case "spade" : return 1
            case "diamond" : return 2
            case "hart" : return 3
            case "clover" : return 4
        }

        return 5;
    }

    numOrder(num){
        switch (num){
            case "A" : return 1
            case "2" : return 2
            case "3" : return 3
            case "4" : return 4
            case "5" : return 5
            case "6" : return 6
            case "7" : return 7
            case "8" : return 8
            case "9" : return 9
            case "10" : return 10
            case "J" : return 11
            case "Q" : return 12
            case "K" : return 13
        }

        return 14;
    }
}

const CARD = [
    "clover_A",
    "clover_2",
    "clover_3",
    "clover_4",
    "clover_5",
    "clover_6",
    "clover_7",
    "clover_8",
    "clover_9",
    "clover_10",
    "clover_J",
    "clover_Q",
    "clover_K",
    "spade_A",
    "spade_2",
    "spade_3",
    "spade_4",
    "spade_5",
    "spade_6",
    "spade_7",
    "spade_8",
    "spade_9",
    "spade_10",
    "spade_J",
    "spade_Q",
    "spade_K",
    "hart_A",
    "hart_2",
    "hart_3",
    "hart_4",
    "hart_5",
    "hart_6",
    "hart_7",
    "hart_8",
    "hart_9",
    "hart_10",
    "hart_J",
    "hart_Q",
    "hart_K",
    "diamond_A",
    "diamond_2",
    "diamond_3",
    "diamond_4",
    "diamond_5",
    "diamond_6",
    "diamond_7",
    "diamond_8",
    "diamond_9",
    "diamond_10",
    "diamond_J",
    "diamond_Q",
    "diamond_K"
]

let hands;

function CardInit() {
    let cards = {};

    for (let i = 0; i < CARD.length; ++i) {
        let name = CARD[i];
        cards[name] = getImage(name);
    }

    hands = new Hands(document.getElementById("Hands"), cards);
}

function getImage(name) {
    let path = `/public/resource/${name}.jpg`;
    let width = 135;
    let height = 170;

    let image = `<img src="${path}" width="${width}" height="${height}" alt="${name}"/>`;
    let div = document.createElement("div");
    div.id = name;
    div.classList.add("Hand");
    div.onclick = clickCard;
    div.innerHTML = image;
    div.setAttribute("data-code", name);

    document.getElementById("imageStore").append(div);

    return div;
}

function clickCard() {
    if (this.classList.contains("ClickedHand")) {
        this.classList.remove("ClickedHand");
    } else {
        this.classList.add("ClickedHand");
    }
}

/**
 * @param list card의 이름이 담긴 배열
 */
function getHands(list){
    list.map(e => {
        hands.pushHands(e);
    })
    hands.sortCards();
}

function sendCard(){
    let cardNames = hands.getClickedHands();

    stompClient.send(`/message/send/${params.roomId}`,{}, JSON.stringify({"type" : "SEND" , "playerId" : playerId, "value" : JSON.stringify(cardNames)}));

    console.log(JSON.stringify(cardNames));
}
