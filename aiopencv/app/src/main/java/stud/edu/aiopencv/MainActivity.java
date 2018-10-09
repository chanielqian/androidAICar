package stud.edu.aiopencv;

import android.Manifest;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2, ArduinoManager.ArduinoManagerListener{
    //Permissions Android
    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    //java camera view
    JavaCameraView javaCameraView;

    // ArduinoManager for communivation
    private ArduinoManager arduinoManager = ArduinoManager.getInstance();

    private boolean isDriving = false;

    Mat mRgba, mRgbaF, mRgbaT, mIntermediateMat;
    //callback loader
    BaseLoaderCallback mCallBackLoader = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {


            switch (status){
                case BaseLoaderCallback.SUCCESS:
                    javaCameraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new  String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, PERMISSIONS_MULTIPLE_REQUEST);

        //connect the camera
        javaCameraView = (JavaCameraView)findViewById(R.id.jvc);
        //set visibility
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        //set callback function
        javaCameraView.setCvCameraViewListener(this);

        arduinoManager.initialize(this);


        findViewById(R.id.go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isDriving) {
                    arduinoManager.sendMessage("S");
                } else {
                    arduinoManager.sendMessage("F8");
                }
                isDriving = !isDriving;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(javaCameraView!=null){
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (javaCameraView!=null){
            javaCameraView.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        arduinoManager.setListener(this);

        if (OpenCVLoader.initDebug()){
            Log.d("openCV", "Connected");
            //display when the activity resumed,, callback loader
            mCallBackLoader.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }else{
            Log.d("openCV", "Not connected");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_3_0, this, mCallBackLoader);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        //4 channel
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mRgbaF = new Mat(height, width, CvType.CV_8UC4);
        mRgbaT = new Mat(width, width, CvType.CV_8UC4);

    }

    @Override
    public void onCameraViewStopped() {
        //release
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        //get each frame from camera
        mRgba = inputFrame.rgba();
        int cols = mRgba.cols();
        int rows = mRgba.rows();
        int pointCount = cols*rows;

        // Rotate mRgba 90 degrees
        Core.transpose(mRgba, mRgbaT);
        Imgproc.resize(mRgbaT, mRgbaF, mRgbaF.size(), 0,0, 0);
        Core.flip(mRgbaF, mRgba, 1 );

        Mat lines = ImageProcessing.ProcessImage(mRgba);
        List<Double> angles = new ArrayList<>();

        for (int x = 0; x < lines.rows(); x++)
        {
            double[] vec = lines.get(x, 0);
            double x1 = vec[0],y1 = vec[1],x2 = vec[2],y2 = vec[3];
            Point start = new Point(x1, y1);
            Point end = new Point(x2, y2);
            double dx = x1 - x2;
            double dy = y1 - y2;
            double dist = Math.sqrt (dx*dx + dy*dy);
            //if(dist>minLineSize)  // show those lines that have length greater than 300
            Imgproc.line(mRgba, start, end, new Scalar(0,255, 0, 255),10);


            double angle = Math.atan2(dx, dy);
            angle = Math.toDegrees(angle);
            if(angle < 0)
                angle += 180;
            if(angle > 180)
                angle -= 180;
            angle -= 90;
            angles.add(angle);
        }

        double sum = 0;
        for (Double angle : angles) {
            Log.d("Angle", angle.toString());
            sum += angle;
        }
        double avg = sum / angles.size();

        Log.d("Angle Average", avg + "");

        Imgproc.putText(mRgba,
                avg < 0? "L" : "R",
                new Point(50, mRgba.height() - 50),
                Core.FONT_HERSHEY_SIMPLEX,
                2,
                new Scalar(0, 255, 0),
                10);

        // Now we can send the information to the arduino
        arduinoManager.sendMessage("R" + (int)avg * 3);
        
        return mRgba;
    }


    // ArduinoManagerListener
    @Override
    public void onArduinoAttached(UsbDevice device) {

    }

    @Override
    public void onArduinoDetached() {

    }

    @Override
    public void onArduinoMessage(String message) {

    }

    @Override
    public void onArduinoOpened() {

    }

    @Override
    public void onUsbPermissionDenied() {

    }

}
