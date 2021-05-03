package PrintExcelPrice;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Sides;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class PrintPrice  extends Thread{
    private ArrayList<ExcelFileForPrint> list;
    private Message message;


    public PrintPrice(String name, ArrayList<ExcelFileForPrint> list, Message message) {
        super(name);
        this.list = list;
        this.message = message;
    }
    public void run(){
        long time = System.currentTimeMillis();
        printListPrice();
        message.addMessage("Распечатал  " + list.size() + " Файлов за " + (System.currentTimeMillis() - time) + " мс");
    }
    public void print(){
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PAGEABLE;

        PrintService myService = PrintServiceLookup.lookupDefaultPrintService();


        if (myService == null) {
            throw new IllegalStateException("Printer not found");
        }


            for(int i =0;i<list.size();i++) {
                while (true) {
                    if (list.get(i).isReady) {
                        try {
                            for (int copy = 0; copy < list.get(i).getNumberPrint(); copy++) {
                                FileInputStream fis = new FileInputStream("PricePDF\\" + list.get(i).getName() + "2.pdf");
                                Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
                                DocPrintJob printJob = myService.createPrintJob();

                                printJob.print(pdfDoc, new HashPrintRequestAttributeSet());


                                fis.close();
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (PrintException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }}
            }//Печать без сторонего софта напрямую
    //не смог реализовать что хотел.


    public  void printListPrice() {
        for (int i = 0; i < list.size(); i++) {
            while (true) {
                if(list.get(i).getIsReady()) {
                        for (int copy = 0; copy < list.get(i).getNumberPrint(); copy++) {
                            try {


                                Desktop.getDesktop().print(list.get(i).getLink());
                            }
                            catch (Exception e) {
                                message.addMessage("Не смог распечатать \"" + list.get(i) + "\"");
                            }
                        }

                    message.addMessage("Распечатал прайс \"" + list.get(i).getName() + "\" " + list.get(i).getNumberPrint() + " раз");
                    list.get(i).setReadyForCopyList(true);
                    break;
                }
                else {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }//Печать с использованием офиса
}
