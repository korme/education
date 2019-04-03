package korme.xyz.education.service.excelAndEmailUtil;

import korme.xyz.education.model.seviceModel.ExcelForAppModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExcelUtil {
    int createExcel(String email, List<ExcelForAppModel> userList);

}
