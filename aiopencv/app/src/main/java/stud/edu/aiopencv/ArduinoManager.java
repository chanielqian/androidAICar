package stud.edu.aiopencv;

import android.content.Context;
import android.hardware.usb.UsbDevice;

import me.aflak.arduino.Arduino;
import me.aflak.arduino.ArduinoListener;

public class ArduinoManager implements ArduinoListener {
    private static final ArduinoManager ourInstance = new ArduinoManager();

    public static ArduinoManager getInstance() {
        return ourInstance;
    }

    private Arduino arduino;
    private ArduinoManagerListener listener;
    private boolean isInitialized = false;

    private ArduinoManager() {}

    public void initialize(Context context) {
        arduino = new Arduino(context);
        arduino.setArduinoListener(this);
    }

    public void setListener(ArduinoManagerListener listener) {
        this.listener = listener;
    }

    public void sendMessage(String message) {
        if(isInitialized)
            arduino.send((message + "!").getBytes());
    }

    @Override
    public void onArduinoAttached(UsbDevice device) {
        listener.onArduinoAttached(device);
        arduino.open(device);
    }

    @Override
    public void onArduinoDetached() {
        isInitialized = false;
        listener.onArduinoDetached();
    }

    @Override
    public void onArduinoMessage(byte[] bytes) {
        listener.onArduinoMessage(new String(bytes));
    }

    @Override
    public void onArduinoOpened() {
        isInitialized = true;
        listener.onArduinoOpened();
    }

    @Override
    public void onUsbPermissionDenied() {
        listener.onUsbPermissionDenied();
    }

    interface ArduinoManagerListener {
        void onArduinoAttached(UsbDevice device);
        void onArduinoDetached();
        void onArduinoMessage(String message);
        void onArduinoOpened();
        void onUsbPermissionDenied();
    }

}
