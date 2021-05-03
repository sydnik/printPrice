package PrintExcelPrice;


import com.aspose.cells.*;

import java.io.File;



//попытка сделать печать без офиса ексель(не удачная:()
public class ExcelToPDFWithRedString {
    public static void excelToPDF(ExcelFileForPrint excelFileForPrint){
        try {
            File file = new File("PricePDF\\");
            if(!file.exists()){
                file.mkdir();
            }
            Workbook workbook = new Workbook(excelFileForPrint.getLink().getPath());
            workbook.save("PricePDF\\"+ excelFileForPrint.getName() + ".pdf", SaveFormat.PDF);




        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
