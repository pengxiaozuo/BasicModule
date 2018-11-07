package com.peng.basic.usb

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.*

object UsbUtils {

    /**
     * 检查USB是否有权限
     */
    fun hasUsbPermission(usbManager: UsbManager?, usbDevice: UsbDevice?): Boolean {
        var hasPermission: Boolean? = false
        if (usbManager != null && usbDevice != null) {
            hasPermission = usbManager.hasPermission(usbDevice)
        }
        return hasPermission!!
    }

    /**
     * 根据pid，vid获取usbDevice
     */
    fun getUsbDevice(usbManager: UsbManager, vendorId: Int, productId: Int): UsbDevice? {
        if ((vendorId == 0) or (productId == 0)) {
            return null
        }
        val deviceList = usbManager.deviceList
        if (!deviceList.isEmpty()) {
            for (device in deviceList.values) {
                if (device.vendorId == vendorId && device.productId == productId) {
                    return device
                }
            }
        }
        return null
    }


    /**
     * 获取UsbInterface
     */
    fun getInterface(usbDevice: UsbDevice?, index: Int): UsbInterface? {
        if (usbDevice != null) {
            if (index < usbDevice.interfaceCount) {
                return usbDevice.getInterface(index)
            }
        }
        return null
    }


    /**
     * 获取Usb连接
     */
    fun getUsbDeviceConnection(usbManager: UsbManager?, usbDevice: UsbDevice?, usbInterface: UsbInterface?): UsbDeviceConnection? {
        if (usbInterface != null && usbDevice != null && usbManager != null) {
            var conn: UsbDeviceConnection? = null
            try {
                conn = usbManager.openDevice(usbDevice)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (conn == null) {
                return null
            }
            //需要声明占用usb接口
            return if (conn.claimInterface(usbInterface, true)) {
                conn
            } else {
                conn.close()
                null
            }
        }
        return null
    }


    /**
     * 拿到输入输入输出端点，可能有null值
     *
     * @return index : 0 - in, 1:out
     */
    fun assignEndpoint(usbInterface: UsbInterface?, endpointType: Int): Array<UsbEndpoint?> {
        val usbEndpoints = arrayOfNulls<UsbEndpoint?>(2)
        if (usbInterface != null) {
            for (i in 0 until usbInterface.endpointCount) {
                val ep = usbInterface.getEndpoint(i)
                if (ep.type == endpointType) {
                    if (ep.direction == UsbConstants.USB_DIR_OUT) {
                        usbEndpoints[1] = ep
                    } else {
                        usbEndpoints[0] = ep
                    }
                }
            }
            return usbEndpoints
        }
        return usbEndpoints
    }

    /**
     * 请求USB权限
     */
    fun requestUsbPermission(usbManager: UsbManager, usbDevice: UsbDevice,
                             context: Context, requestCode: Int,
                             receiver: BroadcastReceiver, action: String) {
        usbManager.requestPermission(usbDevice, PendingIntent
                .getBroadcast(context, requestCode, Intent(
                        action), 0))
    }

}
