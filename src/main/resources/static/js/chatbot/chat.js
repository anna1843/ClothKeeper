let stompClient = null;
$(function(){
    $("#question").keyup(qKeyupFn);
    connect();
})
function showMessage(message){
    $("#chat-content").append(message);
    $("#chat-content").scrollTop($("#chat-content").prop("scrollHeight"));
}
function disconnect(){

    window.close();
}
function connect(){

    let soket = new SockJS('/chatEndpoint');
    stompClient = Stomp.over(soket);
    stompClient.connect({},function (frame){
        console.log('Connected:'+frame);
        stompClient.subscribe('/topic/greetings',function(botMessage){
            showMessage(JSON.parse(botMessage.body).message);
        });
        stompClient.subscribe('/topic/message',function(botMessage){
                    showMessage(JSON.parse(botMessage.body).message);
                });
        // @MessageMapping -> 처음연결시
        stompClient.send("/chat/hello",{}, JSON.stringify({'content':'guest'}));
    });
}
function inputTagString(text){
    let now = new Date();
    let ampm = (now.getHours()>11)?"오후":"오전";
    let time = ampm + now.getHours()%12+":"+now.getMinutes();
    let message = `
    <div class="msg_user_flex_end">
    <div class="message">
    <div class="part">
    <p>${text}</p>
    </div>
    <div class="time">${time}</div>
    </div>
    </div>`;
    return message;
}
function menuclickFn(event){
    let text = event.target.innerText.trim();
    let message = inputTagString(text);
    showMessage(message);
    stompClient.send("/chat/message", {}, JSON.stringify({'content':text}));
}
function qKeyupFn(event){
    if(event.keyCode!=13) return;
    msgSendClickFn()
}
function msgSendClickFn(){
    let question = $('#question').val().trim();
    if(question==""||question.length<2) return;
    let message = inputTagString(question);
    showMessage(message);
    $('#question').val("");
    stompClient.send("/chat/message",{},JSON.stringify({'content':question}));
}