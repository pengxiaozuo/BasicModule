package com.peng.basic.usb

import android.hardware.usb.UsbDevice

internal class UsbDeviceInfo(val productId: Int, val vendorId: Int) {
    var usbDevice: UsbDevice? = null
    val usbConnectionList  = ArrayList<UsbConnection>()

}