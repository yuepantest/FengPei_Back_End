package com.fengpei.web.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fengpei.web.entiry.RespondMsgToUser;
import com.fengpei.web.entiry.SendMsgToUser;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ExternalService {

    private final WebClient webClient;

    public ExternalService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<RespondMsgToUser> postData(String mobile, String content) {
        return webClient.post()
                .uri("/sms.aspx")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData("userid", "0")
                        .with("account", "fpjf")
                        .with("password", "123456")
                        .with("mobile", mobile)
                        .with("content", content)
                        .with("sendTime", "")
                        .with("action", "send")
                        .with("extno", ""))
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseXmlToRespondMsgToUser);
    }

    private RespondMsgToUser parseXmlToRespondMsgToUser(String xml) {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            return xmlMapper.readValue(xml, RespondMsgToUser.class);
        } catch (Exception e) {
            throw new RuntimeException("XML 解析失败", e);
        }
    }
}
