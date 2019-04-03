package korme.xyz.education.service.excelAndEmailUtil;

import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Properties;

@Service
public class EmailUtilMPL implements EmailUtil {
    @Value("${spring.mail.username}")
    public String from;
    @Value("${spring.mail.password}")
    public String password;// 登录密码
    @Value("${spring.mail.protocol}")
    public String protocol;// 协议
    @Value("${spring.mail.port}")
    public String port;// 端口
    @Value("${spring.mail.host}")
    public String host;// 服务器地址

    private String theme = "注册验证码";

    private String reset = "重置验证码";
    @Autowired
    JavaMailSender mailSender;
    public Session initProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", protocol);
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);
        // 使用smtp身份验证
        properties.put("mail.smtp.auth", "true");
        // 使用SSL,企业邮箱必需 start
        // 开启安全协议
        MailSSLSocketFactory mailSSLSocketFactory = null;
        try {
            mailSSLSocketFactory = new MailSSLSocketFactory();
            mailSSLSocketFactory.setTrustAllHosts(true);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        properties.put("mail.smtp.enable", "true");
        properties.put("mail.smtp.ssl.socketFactory", mailSSLSocketFactory);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.socketFactory.fallback", "false");
        properties.put("mail.smtp.socketFactory.port", port);
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        // 使用SSL,企业邮箱必需 end
        session.setDebug(true);
        return session;
    }

    @Override
    public int sendEmail(String mailAddress, InputStream file) {
        Session session = initProperties();
        try{
            MimeMessage mimeMessage = new MimeMessage(session);
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setFrom("3320243347@qq.com");
            message.setTo(mailAddress);
            message.setSubject("handAccount报表");
            message.setText("请查收附件！");
            MimeMultipart mixed = new MimeMultipart("mixed");
            mimeMessage.setContent(mixed);//设置整封邮件的MIME消息体为混合的组合关系
            MimeBodyPart attach1 = new MimeBodyPart();


            DataHandler dh1=new DataHandler(new ByteArrayDataSource(file,"application/msexcel"));
            attach1.setDataHandler(dh1);//设置附件一的数据源
            mixed.addBodyPart(attach1);//添加带附件的邮件
            attach1.setFileName("accountTable.xls");//设置附件一的文件名

            this.mailSender.send(mimeMessage);
        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }
}
