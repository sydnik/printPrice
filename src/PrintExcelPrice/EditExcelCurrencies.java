package PrintExcelPrice;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.helpers.XSSFFormulaUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;


public class EditExcelCurrencies extends Thread {
    private ArrayList<ExcelFileForPrint> list;
    private Message message;

    public EditExcelCurrencies(String name, ArrayList<ExcelFileForPrint> list, Message message) {
        super(name);
        this.list = list;
        this.message = message;
    }

    public void run() {
        long time = System.currentTimeMillis();
        editExcelCurrencies();
        message.addMessage("Изменил курс в " + list.size() + " Файлах за " + (System.currentTimeMillis() - time) + " мс");
    }

    public void editExcelCurrencies() {
        double value = 0;
        for (int i = 0; i < list.size(); i++) {
            value = list.get(i).getCurrency().getValue();
            try {
                FileInputStream stream = new FileInputStream(list.get(i).getLink());
                XSSFWorkbook workbook = new XSSFWorkbook(stream);
                Iterator<Sheet> sheetIterator = (Iterator<Sheet>) workbook.iterator();
                while (sheetIterator.hasNext()) {
                    Sheet sheet = sheetIterator.next();
                    if(!sheet.getSheetName().equals(workbook.getSheetAt(workbook.getActiveSheetIndex()).getSheetName())){
                        workbook.setSheetHidden(workbook.getSheetIndex(sheet),true);
                    }

                }
                workbook.setForceFormulaRecalculation(false);
                XSSFSheet sheet = workbook.getSheet("КУРС!!!");
                XSSFRow row = sheet.getRow(3);
                Cell cell = row.getCell(1);
                cell.setCellValue(value);
                XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);//обновление всех формул в книге
                list.get(i).getLink().getParentFile().mkdirs();
                stream.close();
                FileOutputStream outputStream = new FileOutputStream(list.get(i).getLink());
                workbook.write(outputStream);
                workbook.close();
                outputStream.close();
                list.get(i).isReady = true;
                message.addMessage("Обновил прайс " + list.get(i).getName());


            } catch (FileNotFoundException e) {
                message.addMessage(list.get(i).getName() + "<там проблемка.Ошибка произошла в методе editExcelCurrencies. Скорее всего проблемма в ссылке на файл" + e);
            } catch (IOException e) {
                message.addMessage(list.get(i).getName() + "<там проблемка.Ошибка произошла в методе editExcelCurrencies" + e);
            }


//            list.get(i).setReady(true);

        }


    }
    public static void activeListCLEAF(File file){

        XSSFWorkbook workbook = null;
        try {
            FileInputStream stream = new FileInputStream(file);
            workbook = new XSSFWorkbook(stream);
            workbook.setSheetHidden(workbook.getSheetIndex(workbook.getSheet("Расчет стоимости заказа")),false);
            stream.close();
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            workbook.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
