package PrintExcelPrice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public enum Currency {
    EUR("Euro", 292),
    USD("Dollar", 145);

    private String name;
    private int code;
    private double value;

    private Currency(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public static void ChechKurs(Message message) {
        for (Currency currency : Currency.values()) {
            String nameSite = "http://www.nbrb.by/API/ExRates/Rates/" + currency.getCode();
            HttpURLConnection connection = null;
            String lineString = "";
            try {
                connection = (HttpURLConnection) new URL(nameSite).openConnection();

                connection.setRequestMethod("GET");
                connection.setUseCaches(false);
                connection.setConnectTimeout(500);
                ;
                connection.setReadTimeout(500);

                connection.connect();
                if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                    BufferedReader in = new BufferedReader(new InputStreamReader((connection.getInputStream())));


                    lineString = in.readLine();
                    in.close();
                }
            System.out.println(lineString);
            } catch (ProtocolException e) {
                message.addMessage("Ошибка произошла в методе ChechKurs" + e);
            } catch (MalformedURLException e) {
                message.addMessage("Ошибка произошла в методе ChechKurs" + e);
            } catch (IOException e) {
                message.addMessage("Ошибка произошла в методе ChechKurs" + e);
            }
            System.out.println(lineString);
            int i = lineString.lastIndexOf("OfficialRate\":");
            lineString.substring(i + 14, i + 20);
            currency.setValue(Double.parseDouble(lineString));
        }
    }
}
