package com.example.sens.util;

import com.example.sens.exception.MyBusinessException;
import io.github.biezhi.ome.OhMyEmail;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
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
        String content = String.format("您好，您的房源《%s》已有租客预定成功，请尽快登录满蓝系统查看订单详情。", houseName);
        try {
            OhMyEmail.subject("房源预定成功通知")
                    .from(FROM_EMAIL)
                    .to(toEmail)
                    .text(content)
                    .send();
        } catch (MessagingException e) {
            log.error("发送预定成功邮件失败, toEmail={}, houseName={}", toEmail, houseName, e);
            throw new MyBusinessException("发送预定成功邮件失败");
        }
    }
}
