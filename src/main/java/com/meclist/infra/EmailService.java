package com.meclist.infra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String remetente;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmailDefinicaoSenha(String emailDestino, String token) {
        String link = frontendUrl + "/cliente/definir-senha?token=" + token;
        String assunto = "Meclist - Defina sua senha";
        String corpo = """
                <html>
                <body style="font-family: Arial, sans-serif; padding: 20px;">
                    <h2>Bem-vindo ao Meclist!</h2>
                    <p>Sua conta foi criada com sucesso. Para começar a usar o sistema, defina sua senha clicando no link abaixo:</p>
                    <p><a href="%s" style="background-color: #D86B06; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">Definir minha senha</a></p>
                    <p>Ou copie e cole o link no navegador:</p>
                    <p>%s</p>
                    <br>
                    <p style="color: #888;">Este link expira em 48 horas.</p>
                    <p style="color: #888;">Se você não solicitou este acesso, ignore este e-mail.</p>
                </body>
                </html>
                """.formatted(link, link);

        enviarHtml(emailDestino, assunto, corpo);
    }

    private void enviarHtml(String para, String assunto, String corpoHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(remetente);
            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(corpoHtml, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar e-mail", e);
        }
    }
}
