function hrefCreateAccount(){
    location.href="/signup";
}

function isLogined(){
    return false;
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

    fetch('/users', option)
        .then(res=>res.json())
        .then(data=>{
            console.log(data);
        })
}