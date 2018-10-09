package stud.edu.arduinocommunication;

import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

public class ControllerActivity extends AppCompatActivity implements ArduinoManager.ArduinoManagerListener {

    ArduinoManager arduinoManager = ArduinoManager.getInstance();

    SeekBar steeringSlider;
    SeekBar speedSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        arduinoManager.initialize(this);

        steeringSlider = findViewById(R.id.steeringSlider);
        steeringSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int value = i - steeringSlider.getMax() / 2;
                arduinoManager.sendMessage("R" + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                steeringSlider.setProgress(steeringSlider.getMax() / 2);
            }
        });
        steeringSlider.setProgress(steeringSlider.getMax() / 2);

        speedSlider = findViewById(R.id.speedSlider);
        speedSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int value = i - speedSlider.getMax() / 2;
                arduinoManager.sendMessage("F" + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                speedSlider.setProgress(speedSlider.getMax() / 2);
            }
        });
        speedSlider.setProgress(speedSlider.getMax() / 2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        arduinoManager.setListener(this);
    }

    @Override
    public void onArduinoAttached(UsbDevice device) {
    }

    @Override
    public void onArduinoDetached() {
    }

    @Override
    public void onArduinoMessage(String messahe) {
    }

    @Override
    public void onArduinoOpened() {
    }

    @Override
    public void onUsbPermissionDenied() {
    }

}
