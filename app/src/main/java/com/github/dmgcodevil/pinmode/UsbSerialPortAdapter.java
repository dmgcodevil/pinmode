package com.github.dmgcodevil.pinmode;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.util.HexDump;

import java.util.List;

public class UsbSerialPortAdapter extends ArrayAdapter<UsbSerialPort> {

    private int layoutResourceId;
    private List<UsbSerialPort> data;

    public UsbSerialPortAdapter(Context context, int resource, List<UsbSerialPort> data) {
        super(context, resource, data);
        this.layoutResourceId = resource;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }
        UsbSerialPort usbSerialPort = data.get(position);
        final UsbSerialDriver driver = usbSerialPort.getDriver();
        final UsbDevice device = driver.getDevice();
        final String title = String.format("Vendor %s Product %s",
                HexDump.toHexString((short) device.getVendorId()),
                HexDump.toHexString((short) device.getProductId()));

        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewItem = (TextView) convertView.findViewById(R.id.deviceTextViewItem);
        textViewItem.setText(title);
        textViewItem.setTag(device.getDeviceId());

        return convertView;
    }
}
