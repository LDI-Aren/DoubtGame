class Button{
    constructor(ready, cancel, send, doubt) {
        this.buttons = {
            "ready" : ready,
            "cancel" : cancel,
            "send" : send,
            "doubt" : doubt
        };
    }

    choiceButton(name){
        this.disableAll();
        this.buttons[name].removeAttribute("disabled");
        this.buttons[name].classList.remove("d-none");
    }

    disableAll(){
        for(let name in this.buttons){
            this.buttons[name].setAttribute("disabled", "disabled");
            this.buttons[name].classList.add("d-none");
        }
    }
}