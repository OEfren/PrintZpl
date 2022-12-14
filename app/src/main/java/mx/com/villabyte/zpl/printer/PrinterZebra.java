package mx.com.villabyte.zpl.printer;

import android.graphics.Bitmap;
import android.util.Log;

import com.zebra.sdk.comm.BluetoothConnection;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.graphics.ZebraImageFactory;
import com.zebra.sdk.graphics.ZebraImageI;
import com.zebra.sdk.printer.PrinterLanguage;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;

public class PrinterZebra {

    private Connection connection;
    private ZebraPrinter zebraPrinter;
    private String macAddress;

    PrinterLanguage language;

    public PrinterZebra() {
        this.language = PrinterLanguage.ZPL;
    }

    public PrinterZebra(String macAddress) {
        this();
        this.macAddress = macAddress;
    }

    public PrinterZebra(String macAddress, PrinterLanguage language) {
        this.macAddress = macAddress;
        this.language = language;
    }

    public void connectar() throws Exception {
        connectar(language);
    }

    public void connectar(PrinterLanguage language) throws Exception {
        connection = new BluetoothConnection(PrinterZebra.this.macAddress);
        try {
            connection.open();
            zebraPrinter = ZebraPrinterFactory.getInstance(language, connection);
        }
        catch (ConnectionException ex) {
            closeConnection();
        }
    }

    public void imprimirText(String command) throws Exception {
        //imprimir("! U1 setvar \"device.languages\" \"zpl\"\n");
        imprimir(command);
    }


    public void imprimir(String command) {
        try {
            if (connection == null || !connection.isConnected())
                connectar();
            zebraPrinter.sendCommand(command, "UTF-8");
        }
        catch (Exception ex) {
            Log.e(getClass().getSimpleName(), ex.toString());
        }
    }

    public void imprimir(Bitmap bitmap) {
        if (zebraPrinter != null) {
            try {
                ZebraImageI zebraImageI = ZebraImageFactory.getImage(bitmap);
                zebraPrinter.printImage(zebraImageI, 250, 0, 0, -1, false);
            }
            catch (Exception ex) {
                Log.e(getClass().getSimpleName(), ex.toString());
            }
        }
    }

    public void closeConnection() {
        if(connection != null) {
            try {
                connection.close();
            }
            catch (ConnectionException ex) {
                Log.e("closeConnection", ex.getMessage());
            }
        }
    }

}
