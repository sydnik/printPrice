package PrintExcelPrice;

import java.io.*;


public class Message {
    private StringBuffer message;
    public Message(String message){
        this.message = new StringBuffer(message+"\r\n");
    }
    public synchronized void addMessage(String line){
        System.out.println(line);
        message.append(line+"\r\n" );
    }
    public void writeMessage() {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("message.txt")))){
            writer.write(message.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
