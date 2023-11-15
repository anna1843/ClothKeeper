
const chatbot = document.querySelector('.chatbot');

chatbot.addEventListener("click",function(){
    let options = "toolbar=no,scrollbars=no,resizable=yes, status=no,menubar=no,width=500,height=600, left=600, top=150";
    window.open("/chatbot/chat",'팝업', options);
})