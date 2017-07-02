import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;


public class Main {

    public static void main(String[] args) throws IOException {
        //获得数据
        String jsonStr = readJson("./exchangeRate.json");
        JSONArray jsonArray = JSONArray.fromObject(jsonStr);
        //创建excel
        Workbook wb = new HSSFWorkbook();
        FileOutputStream fileOut = new FileOutputStream("./workbook.xls");
        //创建工作薄
        Sheet sheet1 = wb.createSheet("new sheet");
//        sheet1.autoSizeColumn(1);
        sheet1.setDefaultColumnWidth(9);
        //声明行和列
        Row row;
        Cell cell;
        HSSFCellStyle cellStyle = (HSSFCellStyle) wb.createCellStyle();
        cellStyle.setWrapText(true);
        //初始化列头内容
        String[] kindHeader = {"美元", "欧元", "港币", "日期"};
        String[] kindContent = {"dollar", "euro", "hongkong", "date"};
        //循环添加数据
        for (int i = 0; i < jsonArray.length() + 1; i++) {
            //创建行
            row = sheet1.createRow(i);
//            row.setRowStyle(cellStyle);
            //创建列
            for (int j = 0; j < kindHeader.length; j++) {
                //如果是列头部
                if (i == 0) {
                    cell = row.createCell(j);
//                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(kindHeader[j]);
                }
                //如果是数据内容
                else {
                    cell = row.createCell(j);
//                    cell.setCellStyle(cellStyle);
                    JSONObject object = (JSONObject) jsonArray.get(i - 1);
                    cell.setCellValue(object.getString(kindContent[j]));
                }
            }
        }
        wb.write(fileOut);
        fileOut.close();
    }


    public static String readJson(String path) throws IOException {
        File file = new File(path);
        BufferedReader reader;
        String jsonStr = "";
        reader = new BufferedReader(new FileReader(file));
        String tempStr;
        while ((tempStr = reader.readLine()) != null) {
            jsonStr += tempStr;
        }
        reader.close();
        return jsonStr;
    }
}
