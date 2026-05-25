package com.example.sens.util;

import io.github.biezhi.ome.OhMyEmail;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class EmailUtils {

    private static final String SMTP_HOST = "smtp.aliyun.com";
    private static final String FROM_EMAIL = "admin@manland.com";
    private static final String PASSWORD = "123456";

    static {
        Properties properties = OhMyEmail.defaultConfig(false);
        properties.setProperty("mail.smtp.host", SMTP_HOST);
        OhMyEmail.config(properties, FROM_EMAIL, PASSWORD);
    }

    public static void sendBookSuccessEmail(String toEmail, String houseName) {
        try {
            OhMyEmail.subject("预定成功通知")
                    .from(FROM_EMAIL)
                    .to(toEmail)
                    .text("您好，您的房源【" + houseName + "】已被租客预定成功，请及时处理。")
                    .send();
            log.info("预定成功邮件已发送至: {}", toEmail);
        } catch (Exception e) {
            log.error("发送预定成功邮件失败, 收件人: {}, 房源: {}", toEmail, houseName, e);
        }
    }
}