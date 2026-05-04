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

    public void enviarEmailRecuperacaoSenha(String emailDestino, String token) {
        String link = frontendUrl + "/cliente/definir-senha?token=" + token;
        String assunto = "Meclist - Recuperação de senha";
        String corpo = """
                <html>
                <body style="font-family: Arial, sans-serif; padding: 20px;">
                    <h2>Recuperação de senha</h2>
                    <p>Recebemos uma solicitação para redefinir a senha da sua conta.</p>
                    <p>Para criar uma nova senha, clique no botão abaixo:</p>
                    <p><a href="%s" style="background-color: #D86B06; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">Redefinir minha senha</a></p>
                    <p>Ou copie e cole o link no navegador:</p>
                    <p>%s</p>
                    <br>
                    <p style="color: #888;">Este link expira em 2 horas.</p>
                    <p style="color: #888;">Se você não solicitou a redefinição, ignore este e-mail.</p>
                </body>
                </html>
                """.formatted(link, link);

        enviarHtml(emailDestino, assunto, corpo);
    }

    public void enviarEmailRecuperacaoSenhaMecanico(String emailDestino, String token) {
        String link = frontendUrl + "/mecanico/definir-senha?token=" + token;
        String assunto = "Meclist - Recuperação de senha";
        String corpo = """
                <html>
                <body style="font-family: Arial, sans-serif; padding: 20px;">
                    <h2>Recuperação de senha</h2>
                    <p>Recebemos uma solicitação para redefinir a senha da sua conta de mecânico.</p>
                    <p>Para criar uma nova senha, clique no botão abaixo:</p>
                    <p><a href="%s" style="background-color: #D86B06; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">Redefinir minha senha</a></p>
                    <p>Ou copie e cole o link no navegador:</p>
                    <p>%s</p>
                    <br>
                    <p style="color: #888;">Este link expira em 2 horas.</p>
                    <p style="color: #888;">Se você não solicitou a redefinição, ignore este e-mail.</p>
                </body>
                </html>
                """.formatted(link, link);

        enviarHtml(emailDestino, assunto, corpo);
    }

    public String montarLinkAprovacaoChecklist(Long checklistId, String token) {
        return frontendUrl + "/cliente/checklist/" + checklistId + "/aprovacao?token=" + token;
    }

    public void enviarEmailLinkAprovacaoChecklist(String emailDestino,
                                                  Long checklistId,
                                                  String token,
                                                  int horasValidade) {
        String link = montarLinkAprovacaoChecklist(checklistId, token);
        String assunto = "Meclist - Aprovação de checklist";
        String corpo = """
                <html>
                <body style="font-family: Arial, sans-serif; padding: 20px;">
                    <h2>Checklist aguardando sua aprovação</h2>
                    <p>Recebemos a proposta do seu checklist e ela já está disponível para aprovação no sistema.</p>
                    <p><a href="%s" style="background-color: #D86B06; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">Aprovar checklist</a></p>
                    <p>Ou copie e cole o link no navegador:</p>
                    <p>%s</p>
                    <br>
                    <p style="color: #888;">Este link expira em %d horas e pode ser usado apenas uma vez.</p>
                    <p style="color: #888;">Se você não reconhece esta solicitação, ignore este e-mail.</p>
                </body>
                </html>
                """.formatted(link, link, horasValidade);

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
