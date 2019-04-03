package korme.xyz.education.service.uuidUTil;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UuidUtilMPL implements  UuidUtil{
    @Override
    public String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
