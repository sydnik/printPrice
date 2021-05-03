package PrintExcelPrice;


import java.io.*;
import java.util.ArrayList;
import java.util.Date;


public class Main {


    public static  void main (String[] args) {

        Message message = new Message("Начинаю работать в "+new Date()+"....");

        try {

            //Ниже узнаю курс
            long time = System.currentTimeMillis();
            long allTime = time;
            Currency.ChechKurs(message);
            message.addMessage("Узнал курс за " + (System.currentTimeMillis() - time) + " мс " + "EUR:" + Currency.EUR.getValue() + " USD:" + Currency.USD.getValue());
            //Далее список файло где нужно изменить курс
            time = System.currentTimeMillis();
            ArrayList<ExcelFileForPrint> listPrice = listPrice(message);
            message.addMessage("Узнал список прайсов за " + (System.currentTimeMillis() - time) + " мс");

            EditExcelCurrencies editExcelCurrencies = new EditExcelCurrencies("EditExcel",listPrice,message);// изменяю курс в файлах
//            ExcelToPDF excelToPDF = new ExcelToPDF(listPrice,message);//Делает ПДФ ФАЙЛЫ
            PrintPrice printPrice = new PrintPrice("PrintPrice",listPrice,message);// Печать прайсов

            editExcelCurrencies.start();
//            Thread.sleep(10000);
//            excelToPDF.start();
//            Thread.sleep(10000);

            printPrice.start();
            editExcelCurrencies.join();
//            excelToPDF.join();
            printPrice.join();



            message.addMessage("Программа работала: "  + (System.currentTimeMillis() - allTime) + " мс");

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            message.writeMessage();
        }
    }

    public static ArrayList<ExcelFileForPrint> listPrice(Message  message) {
        ArrayList<ExcelFileForPrint> list = new ArrayList<>();
        String stringLine = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("list.txt"),"windows-1251"))) {
            while ((stringLine = reader.readLine()) != null) {
                ExcelFileForPrint fileForPrint = ExcelFileForPrint.createExcelFileForPrint(stringLine,message);
                if(fileForPrint!=null) {
                    list.add(fileForPrint);
                    message.addMessage(list.get(list.size() - 1).getName() + ": Расспечатать " + list.get(list.size() - 1).getNumberPrint() + " раз, валюта в прайсе " + list.get(list.size() - 1).getCurrency());
                }
            }
        } catch (FileNotFoundException e) {
            message.addMessage(stringLine+"<там проблемка. Ошибка произошла в методе listPrice. Скорее всего ошибка в ссылке на файл" + e);
        } catch (IOException e) {
            message.addMessage(stringLine+"<там проблемка.Ошибка произошла в методе listPrice"+e);
        }

        return list;
    } // создаем список файлов которые будем печатать в какой они валюте и количество копий




}
