package com.example.sens.util;

import io.github.biezhi.ome.OhMyEmail;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import java.util.Properties;

@Slf4j
public class EmailUtils {

    private static final String SMTP_HOST = "smtp.aliyun.com";
    private static final String USERNAME = "admin@manland.com";
    private static final String PASSWORD = "123456";

    static {
        Properties properties = OhMyEmail.defaultConfig(false);
        properties.setProperty("mail.smtp.host", SMTP_HOST);
        OhMyEmail.config(properties, USERNAME, PASSWORD);
    }

    public static void sendBookSuccessEmail(String toEmail, String houseName) {
        try {
            OhMyEmail.subject("租客预定成功通知")
                    .from(USERNAME)
                    .to(toEmail)
                    .text("您的房源「" + houseName + "」已被租客预定，请及时处理。")
                    .send();
        } catch (MessagingException e) {
            log.error("发送预定成功邮件失败，toEmail={}，houseName={}", toEmail, houseName, e);
        }
    }
}
