package com.peng.basic.usb

import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbEndpoint
import android.hardware.usb.UsbInterface

class UsbConnection {
    var usbInterface: UsbInterface? = null
    var deviceConnection: UsbDeviceConnection? = null
    var usbEndpointIn: UsbEndpoint? = null
    var usbEndpointOut: UsbEndpoint? = null

    override fun toString(): String {
        return "UsbConnection{" +
                "usbInterface=" + (usbInterface != null) +
                ", deviceConnection=" + (deviceConnection != null) +
                ", usbEndpointIn=" + (usbEndpointIn != null) +
                ", usbEndpointOut=" + (usbEndpointOut != null) +
                '}'.toString()
    }
}
