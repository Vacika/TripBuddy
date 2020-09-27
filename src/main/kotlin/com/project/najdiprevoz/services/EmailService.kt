package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Mail
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import org.springframework.stereotype.Service
import org.thymeleaf.spring5.SpringTemplateEngine
import java.nio.charset.StandardCharsets


@Service
class EmailService(private val emailSender: JavaMailSender,
                   private val templateEngine: SpringTemplateEngine) {

    fun sendEmail(mail: Mail) {
        try {
            val message: MimeMessage = emailSender.createMimeMessage()
            val helper = MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name())
            val context = Context()
            context.setVariables(mail.model)
            val html: String = templateEngine.process("email/email-template", context)
            helper.setTo(mail.to)
            helper.setText(html, true)
            helper.setSubject(mail.subject)
            helper.setFrom(mail.from)
            emailSender.send(message)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
