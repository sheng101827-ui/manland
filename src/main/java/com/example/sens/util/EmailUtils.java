package com.example.sens.util;

import io.github.biezhi.ome.OhMyEmail;

import java.util.Properties;

public class EmailUtils {

    static {
        // 使用阿里云的 smtp.aliyun.com
        Properties props = OhMyEmail.defaultConfig(false);
        props.put("mail.smtp.host", "smtp.aliyun.com");
        OhMyEmail.config(props, "admin@manland.com", "123456");
    }

    /**
     * 发送租客预定成功邮件给房东
     *
     * @param toEmail   收件人邮箱（房东邮箱）
     * @param houseName 房子名称
     */
    public static void sendBookSuccessEmail(String toEmail, String houseName) {
        try {
            String subject = "房屋预定成功通知";
            String content = "您好，您的房屋【" + houseName + "】已被成功预定，请及时登录系统查看详情。";
            OhMyEmail.subject(subject)
                    .from("Manland管理员")
                    .to(toEmail)
                    .text(content)
                    .send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
