package korme.xyz.education.service.excelAndEmailUtil;

import korme.xyz.education.model.seviceModel.ExcelForAppModel;
import korme.xyz.education.service.uuidUTil.UuidUtil;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

@Service
public class ExcelUtilMPL implements ExcelUtil{
    @Autowired
    EmailUtil emailUtil;
    @Autowired
    UuidUtil uuidUtil;
    @Override
    public int createExcel(String email,List<ExcelForAppModel> userList) {
        // 创建工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建表
        HSSFSheet sheet = workbook.createSheet("用户信息");
        // 创建行
        HSSFRow row = sheet.createRow(0);
        // 创建单元格样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        // 表头
        String[] head = {"申请人编号", "申请人姓名","类别ID","类别名",
                "报销金额","额度余额","申请备注","申请时间"};
        HSSFCell cell;
        // 设置表头
        for(int iHead=0; iHead<head.length; iHead++) {
            cell = row.createCell(iHead);
            cell.setCellValue(head[iHead]);
            cell.setCellStyle(cellStyle);
        }
        // 设置表格内容
        /*private Integer applicationUniqueNumber;
                private String userName;
                            cateId
       private String cateName;

        private Integer mount;
        private Integer currentBalance;

        private String remark;
         private String applyTime;*/

        for(int iBody=0; iBody<userList.size(); iBody++) {
            row = sheet.createRow(iBody+1);
            ExcelForAppModel u = userList.get(iBody);
            String[] userArray = new String[8];
            userArray[0]=u.getApplicationUniqueNumber()==null?null:u.getApplicationUniqueNumber().toString();
            userArray[1]=u.getUserName();
            userArray[2]=u.getAccountId();
            userArray[3]=u.getCateName();
            userArray[4]=u.getMount()==null?null:u.getMount().toString();
            userArray[5]=u.getCurrentBalance()==null?null:u.getCurrentBalance().toString();
            userArray[6]=u.getRemark();
            userArray[7]=u.getApplyTime();
            for(int iArray=0; iArray<userArray.length; iArray++) {
                row.createCell(iArray).setCellValue(userArray[iArray]);
            }
        }
        try{
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            baos.flush();
            byte[] bt = baos.toByteArray();


            /*//test本地输出文件测试
            String fileName= uuidUtil.getUUID();
            File file =new File("d:\\"+fileName);
            //创建一个用于操作文件的字节输出流对象。一创建就必须明确数据存储目的地。
            //输出流目的是文件，会自动创建，如果文件存在，则覆盖。
            FileOutputStream fos=new FileOutputStream(file);
            fos.write(bt);
            fos.close();
            //test*/

            //生产环境
            InputStream is = new ByteArrayInputStream(bt, 0, bt.length);
            emailUtil.sendEmail(email,is);


            baos.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }


}
