package com.github.dmgcodevil.pinmode;

import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.util.ArrayList;
import java.util.List;

public class DeviceManagerActivity extends AppCompatActivity {

    private UsbManager usbManager;
    private SwipeRefreshLayout swipeContainer;
    private List<UsbSerialPort> usbSerialPorts = new ArrayList<>();
    private final String TAG = DeviceManagerActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manager);
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        ListView devicesListView = (ListView) findViewById(R.id.availableDevicesList);
        UsbSerialPortAdapter adapter = new UsbSerialPortAdapter(this, R.layout.device_list_view_row_item, usbSerialPorts);
        devicesListView.setAdapter(adapter);

        devicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "clicked: " + id);
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDevices();
            }
        });

        swipeContainer.post(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(true);
                loadDevices();
            }
        });

    }

    private void loadDevices() {
        Log.d(TAG, "Refreshing device list ...");
        usbSerialPorts.clear();
        final List<UsbSerialDriver> drivers =
                UsbSerialProber.getDefaultProber().findAllDrivers(usbManager);

        for (final UsbSerialDriver driver : drivers) {
            final List<UsbSerialPort> ports = driver.getPorts();
            Log.d(TAG, String.format("+ %s: %s port%s",
                    driver, ports.size(), ports.size() == 1 ? "" : "s"));
            usbSerialPorts.addAll(ports);
        }
        swipeContainer.setRefreshing(false);
    }
}
