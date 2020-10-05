package com.project.najdiprevoz.services

import com.project.najdiprevoz.domain.Mail
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;
import org.springframework.stereotype.Service
import org.thymeleaf.spring5.SpringTemplateEngine
import java.nio.charset.StandardCharsets
import java.util.HashMap


@Service
class EmailService(private val emailSender: JavaMailSender,
                   private val templateEngine: SpringTemplateEngine) {
    val logger: Logger = LoggerFactory.getLogger(EmailService::class.java)

    fun sendForgetPasswordMail(mail: Mail) {
        try {
            logger.debug("[EMAIL SERVICE] Sending [Forget Password] Mail to ${mail.to}")
            val message: MimeMessage = emailSender.createMimeMessage()
            val helper = MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name())
            val context = Context()
            context.setVariables(mail.model)
            val html: String = templateEngine.process("${mail.lang.toLowerCase()}/forget-password-template", context)
            helper.setTo(mail.to)
            helper.setText(html, true)
            helper.setSubject(mail.subject)
            helper.setFrom(mail.from)
            emailSender.send(message)
            logger.debug("[EMAIL-SERVICE] Mail sent successfully!")
        } catch (e: Exception) {
            logger.error("Mail for ${mail.to} was not sent!")
            throw RuntimeException(e)
        }
    }

    fun sendUserActivationMail(mail: Mail) {
        try {
            logger.debug("[EMAIL SERVICE] Sending [Activate User] Mail to ${mail.to}")
            val message: MimeMessage = emailSender.createMimeMessage()
            val helper = MimeMessageHelper(message,
                                           MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                                           StandardCharsets.UTF_8.name())
            val context = Context()
            context.setVariables(mail.model)
            val html: String = templateEngine.process(mail.template, context)
            helper.setTo(mail.to)
            helper.setText(html, true)
            helper.setSubject(mail.subject)
            helper.setFrom(mail.from)
            emailSender.send(message)
            logger.debug("[EMAIL-SERVICE] Mail sent successfully!")
        } catch (e: Exception) {
            logger.error("Mail for ${mail.to} was not sent!")
            throw RuntimeException(e)
        }
    }

}
