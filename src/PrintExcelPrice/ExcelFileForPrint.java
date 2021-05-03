package PrintExcelPrice;

import java.io.File;


public class ExcelFileForPrint {

    private String name;
    private int numberPrint;//количество печати
    private File link;
    private Currency currency; // Валюта
    public boolean isReady;//Готов к печати
    private boolean isReadyForCopyList;
    public boolean  isReadyExcelToPDF;

    public ExcelFileForPrint(String name, int numberPrint, File link, Currency currency) {
        this.name = name;
        this.numberPrint = numberPrint;
        this.link = link;
        this.currency = currency;
        isReady = false;
    }

    public String getName() {
        return name;
    }
    public  int getNumberPrint() {
        return numberPrint;
    }
    public File getLink() {
        return link;
    }
    public Currency getCurrency() {
        return currency;
    }
    public boolean getIsReady() {
        return isReady;
    }
    public boolean isReadyForCopyList() {
        return isReadyForCopyList;
    }

    public boolean isReadyExcelToPDF() {
        return isReadyExcelToPDF;
    }

    public void setReadyExcelToPDF(boolean readyExcelToPDF) {
        isReadyExcelToPDF = readyExcelToPDF;
    }

    public void setReadyForCopyList(boolean readyForCopyList) {
        isReadyForCopyList = readyForCopyList;
    }
    public void setReady(boolean ready) {
        isReady = ready;
    }

    public static ExcelFileForPrint createExcelFileForPrint (String string, Message message){
        String[] listString = string.split("%");
        if(listString.length!=3){
            message.addMessage("ВНИМАНИЕ! Файл не распечатан. Проверьте данные в файле list.txt строка: " + string);
        }
        else {
            String name = listString[0].substring(listString[0].lastIndexOf("\\") + 1, listString[0].lastIndexOf("."));
            Currency currency = null;

            for (Currency cur : Currency.values()) {
                if (cur.name().equals(listString[2].toUpperCase())) {
                    currency = cur;
                }
            }
            ExcelFileForPrint excelFileForPrint = new ExcelFileForPrint(name, Integer.parseInt(listString[1]), new File(listString[0]), currency);


        return excelFileForPrint;
        }
        return null;
    }

}
