package korme.xyz.education.service.excelAndEmailUtil;

import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public interface EmailUtil {
    int sendEmail(String mailAddress, InputStream file);
}
