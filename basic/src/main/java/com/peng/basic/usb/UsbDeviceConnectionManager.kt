package com.peng.basic.usb

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbConstants
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import com.peng.basic.util.LogUtils

/**
 * USB连接管理
 */
@SuppressLint("StaticFieldLeak")
object UsbDeviceConnectionManager {

    private const val ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION"
    private val mUsbDeviceList = ArrayList<UsbDeviceInfo>()

    private var mUsbManager: UsbManager? = null

    private val mUsbReceiver = UsbReceiver()
    private val mUsbCallbackList = ArrayList<UsbCallback>()

    private lateinit var mContext: Context

    private var mCallback: UsbCallback = object : UsbCallback {
        override fun onConnect(productId: Int, vendorId: Int, usbInterfaceCount: Int) {
            for (usbCallback in mUsbCallbackList) {
                usbCallback.onConnect(productId, vendorId, usbInterfaceCount)
            }
        }

        override fun onDisconnect(productId: Int, vendorId: Int) {
            for (usbCallback in mUsbCallbackList) {
                usbCallback.onDisconnect(productId, vendorId)
            }
        }

        override fun onFailed(productId: Int, vendorId: Int, error: String) {
            for (usbCallback in mUsbCallbackList) {
                usbCallback.onFailed(productId, vendorId, error)
            }
        }

        override fun onRelease() {
            for (usbCallback in mUsbCallbackList) {
                usbCallback.onRelease()
            }
        }

    }

    /**
     * 初始化
     */
    fun init(context: Context) {
        this.mContext = context.applicationContext
        mUsbManager = mContext.getSystemService(Context.USB_SERVICE) as UsbManager
        mUsbReceiver.register(mContext)
    }

    fun addOnUsbCallback(callback: UsbCallback) {
        mUsbCallbackList.add(callback)
    }

    fun removeUsbCallback(callback: UsbCallback) {
        mUsbCallbackList.remove(callback)
    }


    /**
     * 添加到等待连接列表，如果可以查找到usb设备，并有权限就会连接
     */
    fun addConnectUsbDevice(productId: Int, vendorId: Int) {
        if (mUsbManager == null) {
            mCallback.onFailed(productId, vendorId, "init failed")
            return
        }

        for (usbDeviceInfo in mUsbDeviceList) {
            if (usbDeviceInfo.productId == productId && usbDeviceInfo.vendorId == vendorId) {
                return
            }
        }
        val usbDeviceInfo = UsbDeviceInfo(productId, vendorId)
        val usbDevice = UsbUtils.getUsbDevice(mUsbManager!!, vendorId, productId)

        if (usbDevice == null) {
            LogUtils.d(
                "not found (productId = $productId , " +
                        "vendorId = $vendorId) usb device,wait attach usb device"
            )
        } else {
            usbDeviceInfo.usbDevice = usbDevice
            initUsbDeviceInfo(usbDeviceInfo)
        }
        mUsbDeviceList.add(usbDeviceInfo)
    }

    private fun initUsbDeviceInfo(usbDeviceInfo: UsbDeviceInfo) {
        if (UsbUtils.hasUsbPermission(mUsbManager, usbDeviceInfo.usbDevice)) {
            LogUtils.d("has permission, init usb connection")
            initUsbConnectionInfo(usbDeviceInfo)
        } else {
            LogUtils.d("request permission")
            UsbUtils.requestUsbPermission(
                mUsbManager!!,
                usbDeviceInfo.usbDevice!!,
                mContext,
                0,
                ACTION_USB_PERMISSION
            )
        }
    }

    private fun initUsbConnectionInfo(usbDeviceInfo: UsbDeviceInfo) {
        val count = usbDeviceInfo.usbDevice!!.interfaceCount
        LogUtils.d("usb device interface count : $count")

        for (i in 0 until count) {
            val usbConnection = UsbConnection()

            val usbInterface = UsbUtils.getInterface(usbDeviceInfo.usbDevice!!, i)
            usbConnection.usbInterface = usbInterface
            LogUtils.d("get usb interface index : $i, usbInterface : $usbInterface")

            val deviceConnection = UsbUtils.getUsbDeviceConnection(
                mUsbManager,
                usbDeviceInfo.usbDevice!!,
                usbInterface
            )

            usbConnection.deviceConnection = deviceConnection
            LogUtils.d("get usb device connection deviceConnection: $deviceConnection")

            if (deviceConnection != null) {
                val tempEndpoints = UsbUtils.assignEndpoint(
                    usbInterface,
                    UsbConstants.USB_ENDPOINT_XFER_INT
                )
                usbConnection.usbEndpointIn = tempEndpoints[0]
                usbConnection.usbEndpointOut = tempEndpoints[1]
                LogUtils.d(
                    "usbEndpointIn:${usbConnection.usbEndpointIn} ," +
                            " usbEndpointOut : ${usbConnection.usbEndpointOut}"
                )
            }

            usbDeviceInfo.usbConnectionList.add(usbConnection)
        }

        mCallback.onConnect(
            usbDeviceInfo.productId,
            usbDeviceInfo.vendorId,
            usbDeviceInfo.usbConnectionList.size
        )
    }

    fun bulkTransferOut(
        productId: Int,
        vendorId: Int,
        data: ByteArray,
        index: Int,
        timeout: Int
    ): Int {
        if (index < 0) {
            return 0
        }
        for (usbDeviceInfo in mUsbDeviceList) {
            if (usbDeviceInfo.productId == productId && usbDeviceInfo.vendorId == vendorId) {
                val usbConnectionList = usbDeviceInfo.usbConnectionList
                if (index > usbConnectionList.size - 1) {
                    return 0
                }
                val usbConnection = usbConnectionList[index].deviceConnection
                val usbEndpointOut = usbConnectionList[index].usbEndpointOut
                return if (usbConnection == null || usbEndpointOut == null) 0 else {
                    usbConnection.bulkTransfer(usbEndpointOut, data, data.size, timeout)
                }
            }
        }

        return 0
    }


    fun bulkTransferIn(
        productId: Int,
        vendorId: Int,
        length: Int,
        index: Int,
        timeout: Int
    ): ByteArray? {

        for (usbDeviceInfo in mUsbDeviceList) {
            if (usbDeviceInfo.productId == productId && usbDeviceInfo.vendorId == vendorId) {
                val usbConnectionList = usbDeviceInfo.usbConnectionList
                if (index > usbConnectionList.size - 1) return null
                val usbConnection = usbConnectionList[index].deviceConnection
                val usbEndpointIn = usbConnectionList[index].usbEndpointIn
                return if (usbConnection == null || usbEndpointIn == null) null else {
                    val data = ByteArray(length)
                    usbConnection.bulkTransfer(usbEndpointIn, data, data.size, timeout)
                    data
                }
            }
        }

        return null
    }

    /**
     * 从usb连接列表移除设备，如果已连接，则先断开连接
     */
    fun remove(productId: Int, vendorId: Int) {
        val usbDeviceInfo = mUsbDeviceList.find {
            it.productId == productId && it.vendorId == vendorId
        }
        if (usbDeviceInfo != null) {
            disconnect(usbDeviceInfo)
            mUsbDeviceList.remove(usbDeviceInfo)
        }
    }


    /**
     * 断开连接
     */
    private fun disconnect(productId: Int, vendorId: Int) {
        val usbDeviceInfo = mUsbDeviceList.find {
            it.productId == productId && it.vendorId == vendorId
        }
        if (usbDeviceInfo != null) {
            disconnect(usbDeviceInfo)
        }

    }

    /**
     * 断开所有连接
     */
    private fun disconnectAll() {
        for (usbDeviceInfo in mUsbDeviceList) {
            disconnect(usbDeviceInfo)
        }
    }

    /**
     * 释放资源
     */
    fun release() {
        disconnectAll()
        mUsbDeviceList.clear()
        mUsbReceiver.unregister(mContext)
        mCallback.onRelease()
    }

    private fun disconnect(usbDeviceInfo: UsbDeviceInfo) {
        for (usbConnection in usbDeviceInfo.usbConnectionList) {
            if (usbConnection.deviceConnection != null) {
                usbConnection.deviceConnection!!.releaseInterface(usbConnection.usbInterface)
                usbConnection.deviceConnection!!.close()
                usbConnection.deviceConnection = null
            }
            usbConnection.usbInterface = null
            usbConnection.usbEndpointIn = null
            usbConnection.usbEndpointOut = null
        }
        usbDeviceInfo.usbDevice = null
        usbDeviceInfo.usbConnectionList.clear()

        mCallback.onDisconnect(usbDeviceInfo.productId, usbDeviceInfo.vendorId)
    }


    class UsbReceiver : BroadcastReceiver() {

        fun register(context: Context) {
            val filter = IntentFilter(ACTION_USB_PERMISSION)
            filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
            filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
            context.registerReceiver(this, filter)
        }

        fun unregister(context: Context) {
            context.unregisterReceiver(this)
        }

        override fun onReceive(context: Context, intent: Intent) {

            if (UsbManager.ACTION_USB_DEVICE_ATTACHED == intent.action) {
                val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)

                for (usbDeviceInfo in mUsbDeviceList) {
                    if (device.vendorId == usbDeviceInfo.vendorId
                        && device.productId == usbDeviceInfo.productId
                    ) {
                        usbDeviceInfo.usbDevice = device
                        initUsbDeviceInfo(usbDeviceInfo)
                        break
                    }
                }

            } else if (ACTION_USB_PERMISSION == intent.action) {
                val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                if (UsbUtils.hasUsbPermission(mUsbManager, device)) {
                    for (usbDeviceInfo in mUsbDeviceList) {
                        if (device.vendorId == usbDeviceInfo.vendorId
                            && device.productId == usbDeviceInfo.productId
                        ) {
                            usbDeviceInfo.usbDevice = device
                            initUsbConnectionInfo(usbDeviceInfo)
                            break
                        }
                    }
                } else {
                    mCallback.onFailed(device.productId, device.vendorId, "permission denied")
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED == intent.action) {
                val device = intent.getParcelableExtra<UsbDevice>(UsbManager.EXTRA_DEVICE)
                disconnect(device.productId, device.vendorId)
            }
        }
    }

    /**
     * USB连接回调
     */
    interface UsbCallback {
        /**
         * 连接成功
         */
        fun onConnect(productId: Int, vendorId: Int, usbInterfaceCount: Int)

        /**
         * 连接丢失
         */
        fun onDisconnect(productId: Int, vendorId: Int)

        /**
         * 连接失败
         */
        fun onFailed(productId: Int, vendorId: Int, error: String)

        /**
         * 释放资源
         */
        fun onRelease()
    }

}