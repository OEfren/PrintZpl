package mx.com.villabyte.zpl;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import mx.com.villabyte.zpl.printer.PrinterZebra;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    String namePrinter;
    String printer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText txtZpl = (EditText) findViewById(R.id.txtZpl);
        if (txtZpl != null) {
            txtZpl.setText("^XA\n" +
                    "^LL240\n" +
                    "^FO80,14^BY3\n" +
                    "^A@N,30,14,B:CYRI_UB.FNT\n" +
                    "^FO010,016^A0N,35,35^FB406,3,0,C,0^FDGangster Shop\\&^FS\n" +
                    "^FO48.0,50^BY2\n" +
                    "^B3N,N,70,Y,N\n" +
                    "^FDPROD1595^FS\n" +
                    "^FO20,155^AD^FDPlayera 3/4 Antifashion ^FS\n" +
                    "^FO20,175^AD^FD240.00 MXN^FS\n" +
                    "^FO250,175^AD^FD04/10/2018^FS\n" +
                    "^XZ");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (printer == null) {
                    Snackbar.make(view, "Seleccione la impresora", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else {
                    try {
                        PrinterZebra printerZebra = new PrinterZebra(printer);
                        printerZebra.imprimirText(txtZpl.getText().toString());
                        printerZebra.closeConnection();
                    }
                    catch ( Exception ex) {
                        Snackbar.make(view, "Error al conectar a la impresora", Snackbar.LENGTH_LONG)
                                .setAction("Error", null).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == ConnectPrinterActivity.SELECT_PRINTER) {
                if (data != null && data.getExtras() != null && data.getExtras().containsKey("SELECTED_PRINTER")) {
                    MainActivity.this.printer = data.getExtras().getString("SELECTED_PRINTER");
                    MainActivity.this.namePrinter = data.getExtras().getString("NAME_PRINTER");
                    if (MainActivity.this.toolbar != null)
                        this.toolbar.setSubtitle(MainActivity.this.namePrinter);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intImpresora = new Intent(MainActivity.this, ConnectPrinterActivity.class);
            startActivityForResult(intImpresora, ConnectPrinterActivity.SELECT_PRINTER);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
