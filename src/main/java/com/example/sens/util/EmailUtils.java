package com.example.sens.util;

import io.github.biezhi.ome.OhMyEmail;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class EmailUtils {

    static {
        Properties properties = OhMyEmail.defaultConfig(false);
        properties.setProperty("mail.smtp.host", "smtp.aliyun.com");
        OhMyEmail.config(properties, "admin@manland.com", "123456");
    }

    public static void sendBookSuccessEmail(String toEmail, String houseName) {
        try {
            OhMyEmail.subject("【Manland】房屋预定成功通知")
                    .from("Manland")
                    .to(toEmail)
                    .text("尊敬的房东，您好！\n\n您的房屋 \"" + houseName + "\" 已被租客成功预定，请及时联系租客确认相关事宜。\n\n祝您生活愉快！\n\nManland 租房平台")
                    .send();
            log.info("邮件发送成功，收件人：{}，房屋：{}", toEmail, houseName);
        } catch (Exception e) {
            log.error("邮件发送失败，收件人：{}，房屋：{}，错误信息：{}", toEmail, houseName, e.getMessage(), e);
        }
    }
}
