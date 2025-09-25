package com.example.auth_service.infrastructure.mail;

import com.example.auth_service.application.ports.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
@Profile("prod")
@RequiredArgsConstructor
public class SmtpMailSender implements MailSender {
    
    private final JavaMailSender mailSender;
    
    @Override
    public void sendMagicLink(String to, String magicLink, Instant expiresAt) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(to);
            helper.setSubject("�� Magic Link - Auth Service");
            
            String formattedExpiresAt = expiresAt
                .atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm"));
            
            String htmlBody = createHtmlEmail(magicLink, formattedExpiresAt);
            helper.setText(htmlBody, true);
            
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar e-mail", e);
        }
    }
    
    private String createHtmlEmail(String magicLink, String expiresAt) {
        return String.format("""
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Magic Link - Auth Service</title>
            </head>
            <body style="margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f4f4;">
                <div style="max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);">
                    
                    <div style="background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); padding: 40px 30px; text-align: center;">
                        <h1 style="color: #ffffff; margin: 0; font-size: 28px; font-weight: 300;">
                            🔐 Auth Service
                        </h1>
                        <p style="color: #e8e8e8; margin: 10px 0 0 0; font-size: 16px;">
                            Magic Link de Acesso
                        </p>
                    </div>
                    
                    <div style="padding: 40px 30px;">
                        <h2 style="color: #333333; margin: 0 0 20px 0; font-size: 24px; font-weight: 400;">
                            Olá! 👋
                        </h2>
                        
                        <p style="color: #666666; font-size: 16px; line-height: 1.6; margin: 0 0 25px 0;">
                            Você solicitou um magic link para fazer login no <strong>Auth Service</strong>. 
                            Clique no botão abaixo para acessar sua conta de forma segura.
                        </p>
                        
                        <div style="text-align: center; margin: 35px 0;">
                            <a href="%s" style="display: inline-block; background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); color: #ffffff; text-decoration: none; padding: 15px 40px; border-radius: 50px; font-size: 16px; font-weight: 600; box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4); transition: all 0.3s ease;">
                                🚀 Fazer Login
                            </a>
                        </div>
                        
                        <div style="background-color: #f8f9fa; border-left: 4px solid #667eea; padding: 20px; margin: 25px 0; border-radius: 4px;">
                            <p style="color: #666666; font-size: 14px; margin: 0 0 10px 0; font-weight: 600;">
                                Ou copie e cole este link no seu navegador:
                            </p>
                            <p style="color: #667eea; font-size: 12px; margin: 0; word-break: break-all; background-color: #ffffff; padding: 10px; border-radius: 4px; border: 1px solid #e9ecef;">
                                %s
                            </p>
                        </div>
                        
                        <div style="background-color: #fff3cd; border: 1px solid #ffeaa7; border-radius: 6px; padding: 20px; margin: 25px 0;">
                            <h3 style="color: #856404; margin: 0 0 15px 0; font-size: 16px; font-weight: 600;">
                                ⚠️ Informações de Segurança
                            </h3>
                            <ul style="color: #856404; font-size: 14px; line-height: 1.6; margin: 0; padding-left: 20px;">
                                <li>Este link expira em: <strong>%s</strong></li>
                                <li>Este link só pode ser usado <strong>uma única vez</strong></li>
                                <li>Se você não solicitou este link, ignore este e-mail</li>
                            </ul>
                        </div>
                        
                        <p style="color: #999999; font-size: 14px; line-height: 1.6; margin: 30px 0 0 0; text-align: center;">
                            Este é um e-mail automático, por favor não responda.
                        </p>
                    </div>
                    
                    <div style="background-color: #f8f9fa; padding: 30px; text-align: center; border-top: 1px solid #e9ecef;">
                        <p style="color: #666666; font-size: 14px; margin: 0 0 10px 0;">
                            <strong>Auth Service</strong> - Sistema de Autenticação
                        </p>
                        <p style="color: #999999; font-size: 12px; margin: 0;">
                            © 2024 Auth Service. Todos os direitos reservados.
                        </p>
                    </div>
                </div>
            </body>
            </html>
            """, magicLink, magicLink, expiresAt);
    }
}