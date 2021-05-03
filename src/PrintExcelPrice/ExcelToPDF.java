package PrintExcelPrice;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;



//попытка сделать печать без Офиса ексель(Не удачная:()
public class ExcelToPDF extends Thread {
    private ArrayList<ExcelFileForPrint> list;
    private Message message;

    public ExcelToPDF(ArrayList<ExcelFileForPrint> list, Message message) {
        this.list = list;

        this.message = message;
    }

    public void run() {
        long time = System.currentTimeMillis();
        excelToPDF();
        message.addMessage("Сделал PDF файлы " + list.size() + " штук за " + (System.currentTimeMillis() - time) + " мс");
    }
    public void excelToPDF(){
        for (int i = 0; i < list.size(); i++) {
            while (true) {
                System.out.println(list.get(i).isReadyExcelToPDF);
                if(list.get(i).isReadyExcelToPDF) {
                    ExcelToPDFWithRedString.excelToPDF(list.get(i));

                    try {
                        PdfReader pdfReader = new PdfReader("PricePDF\\" + list.get(i).getName() + ".pdf");
                        PdfStamper stamper = new PdfStamper(pdfReader,
                                new FileOutputStream("PricePDF\\"+list.get(i).getName()+"2.pdf"));
                        for (int pages=1; pages<=pdfReader.getNumberOfPages(); pages++) {
                            PdfContentByte over = stamper.getOverContent(pages);
                            over.setRGBColorStroke(255,255,255);
                            over.setLineWidth(12f);
                            over.moveTo(0,835);
                            over.lineTo(500,835);
                            over.stroke();
                        }

                        stamper.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                    list.get(i).isReady = true;
                    if(list.get(i).getName().equals("фасады Cleaf")){
                        EditExcelCurrencies.activeListCLEAF(list.get(i).getLink());
                    }

                    break;
                }
                else try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
