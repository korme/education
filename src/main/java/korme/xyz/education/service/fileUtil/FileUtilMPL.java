package korme.xyz.education.service.fileUtil;

import korme.xyz.education.service.uuidUTil.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileUtilMPL implements FileUtil {
    @Autowired
    UuidUtil uuidUtil;
    @Override
    public String upload(MultipartFile file, String path, String fileName) {
        InputStream is;
        fileName=uuidUtil.getUUID()+fileName.substring(fileName.lastIndexOf("."));
        String realPath = path + "/" + fileName;

        //使用原文件名
        //String realPath = path + "/" + fileName;

        File dest = new File(realPath);
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdir();
        }
        try {

            is = file.getInputStream();
            byte[] b = new byte[4];
            is.read(b, 0, b.length);
            StringBuilder builder = new StringBuilder();
            if (b == null || b.length <= 0) {
                return null;
            }
            String hv;
            for (int i = 0; i < b.length; i++) {
                hv = Integer.toHexString(b[i] & 0xFF).toUpperCase();
                if (hv.length() < 2) {
                    builder.append(0);
                }
                builder.append(hv);
            }
            String s=builder.toString();
            if(!(s.substring(0,6).equals("FFD8FF")||s.equals("89504E47"))){
                System.out.println(s.substring(0,6));
                System.out.println(s);
                throw new IllegalStateException();
            }

            file.transferTo(dest);
            return "https://www.korme.xyz/image/"+fileName;
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "0";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }
}
