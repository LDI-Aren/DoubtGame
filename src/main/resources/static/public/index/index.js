function hrefCreateAccount(){
    location.href="/signup";
}

async function getLoginedUserId(){
    let matches = document.cookie.match(new RegExp("(?:^|; )accessToken=([^;]*)"));
    if(matches === null){
        return null;
    }

    let userId = await fetch('/userId')
        .then(res =>{ return res.json() })

    return userId.userId;
}

function login(){
    let id = document.getElementById("userId").value;
    let pwd = document.getElementById("userPassword").value;

    let option = {
        method : "post",
        headers : {
            "Content-Type" : "Application/json;charset=utf-8"
        },
        body : JSON.stringify({
            "userId" : id,
            "userPassword" : pwd
        })
    }

    console.log(option);

    fetch('/login', option)
        .then(res=>{
            location.href="/games?playerId=" + id;
        });
}