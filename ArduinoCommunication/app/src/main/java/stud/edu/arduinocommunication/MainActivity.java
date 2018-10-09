package stud.edu.arduinocommunication;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import me.aflak.arduino.Arduino;
import me.aflak.arduino.ArduinoListener;

public class MainActivity extends AppCompatActivity implements ArduinoManager.ArduinoManagerListener {

    ArduinoManager arduinoManager = ArduinoManager.getInstance();

    TextView textView;
    Button sendButton;
    EditText inputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arduinoManager.initialize(this);

        textView = findViewById(R.id.textView);
        sendButton = findViewById(R.id.send);
        inputField = findViewById(R.id.input);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputField.getText().toString().isEmpty())
                    return;

                arduinoManager.sendMessage(inputField.getText().toString());
                inputField.getText().clear();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        arduinoManager.setListener(this);
    }

    @Override
    public void onArduinoAttached(UsbDevice device) {
        display("Arduino attached!");
    }

    @Override
    public void onArduinoDetached() {
        display("Arduino detached");
    }

    @Override
    public void onArduinoMessage(String message) {
        display(message);
    }

    @Override
    public void onArduinoOpened() {
        arduinoManager.sendMessage("Hello World");
    }

    @Override
    public void onUsbPermissionDenied() {
        display("Permission denied");
    }

    public void display(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.append(message+"\n");
            }
        });
    }

}
