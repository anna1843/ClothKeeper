package org.project.dev.chatbot.controller;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.asm.Advice;
import org.project.dev.chatbot.message.BotMessage;
import org.project.dev.chatbot.message.ClientMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chatbot")
public class ChatBotController {

    @GetMapping("/chat")
    public String chatbot(){
        return "chatbot/chat";
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public BotMessage chatSt() throws Exception{
        Thread.sleep(50);
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        String formattedDay = today.format(formatter);
        String formattedTime = today.format(DateTimeFormatter.ofPattern("a H:mm"));

        // 처음 실행되는 -> 답장문
        return new BotMessage("<div class='flex_center_date' >"+formattedDay+"</div>"+
                "<div class='msg_bot_flex'>"+
                "<div class='icon'>"+
                "<img src='/images/chatbot2.png'  th:alt=\"#{chat}\" />" +
                "</div>"+
                "<div class='message'>"+
                "<div class='part'>"+
                "<p style='text-align:center'>안녕하세요, 챗봇입니다. <br> 궁금한 점은 저에게 물어보세요!</p>"+
                "</div>" +
                "<div class='part2'>"+
                "<p>아래는 자주하는 질문 내용을 클릭이나 입력해 주세요.</p>"+
                "<div class='flex_center_menu'>"+
                "<div class='menu-item'><span onclick='menuclickFn(event)'>상품문의</span></div>"+
                "<div class='menu-item'><span onclick='menuclickFn(event)'>결제문의</span></div>"+
                "<div class='menu-item'><span onclick='menuclickFn(event)'>배송문의</span></div>"+
                "</div>"+
                "</div>"+
                "<div class='time'>"+
                formattedTime+
                "</div>"+
                "</div>"+
                "</div>");
    }
    @MessageMapping("/message")
    @SendTo("/topic/message")
    public BotMessage message(ClientMessage message) throws Exception{
        Thread.sleep(100);
        LocalDateTime today = LocalDateTime.now();
        String formattedTime = today.format(DateTimeFormatter.ofPattern("a H:mm"));
        String responseText = message.getContent()+"에 대한 안내입니다.";

        return new BotMessage("<div class='msg_bot_flex'>"+
                "<div class='icon'>"+
                "<img src='/images/chatbot2.png'  th:alt=\"#{chat}\" />" +
                "</div>"+
                "<div class='message'>"+
                "<div class='part'>"+
                "<p>"+responseText+"</p>"+
                "</div>"+
                "<div class='time'>"+
                formattedTime+
                "</div>"+
                "</div>"+
                "</div>");
    }

}
