package mx.com.villabyte.zpl;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConnectPrinterActivity extends AppCompatActivity {

    public static final int SELECT_PRINTER = 1000;

    String[] permissions = new String[] {
            Manifest.permission.BLUETOOTH
    };

    ListView listPrinters;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_printer);
        this.listPrinters = (ListView)findViewById(R.id.listViewPrinter);

        this.listPrinters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ConnectPrinterActivity.Printer printer = (ConnectPrinterActivity.Printer) adapterView.getItemAtPosition(i);
                Intent intent = new Intent();
                intent.putExtra("NAME_PRINTER", printer.namne);
                intent.putExtra("SELECTED_PRINTER", printer.macAddress);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        getPrinters();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getPrinters() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        List<Printer> printers = new ArrayList<Printer>();
        for(BluetoothDevice bt : pairedDevices) {
            ConnectPrinterActivity.Printer printer = new ConnectPrinterActivity.Printer();
            printer.namne = bt.getName();
            printer.macAddress = bt.getAddress();

            printers.add(printer);
        }
        ConnectPrinterActivity.ListAdapter adapter = new ConnectPrinterActivity.ListAdapter(ConnectPrinterActivity.this, printers);
        listPrinters.setAdapter(adapter);
    }

    private class ListAdapter extends ArrayAdapter<Printer> {

        private List<ConnectPrinterActivity.Printer> listPrinters;

        public ListAdapter(Context context, List<ConnectPrinterActivity.Printer> items) {
            super(context, R.layout.item_connect_printer, items);
            this.listPrinters = items;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView= inflater.inflate(R.layout.item_connect_printer, null, true);
            TextView lblName = (TextView) rowView.findViewById(R.id.lblName);
            TextView lblDireccion = (TextView) rowView.findViewById(R.id.lblDirection);
            ConnectPrinterActivity.Printer printer =  listPrinters.get(position);
            lblName.setText(printer.namne);
            lblDireccion.setText(printer.macAddress);
            return rowView;
        }
    }

    public class Printer {
        public String namne;
        public String macAddress;
    }

}
