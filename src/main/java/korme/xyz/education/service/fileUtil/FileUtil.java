package korme.xyz.education.service.fileUtil;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileUtil {
    String upload(MultipartFile file, String path, String fileName);
}
