package stud.edu.aiopencv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class ImageTestActivity extends AppCompatActivity {

    ImageView imageView;

    Mat rgbaImage;

    BaseLoaderCallback mCallBackLoader = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                    TestImage();
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
        setContentView(R.layout.activity_image_test);

        imageView = findViewById(R.id.imageView);
    }

    private void TestImage() {
        try {
            rgbaImage = LoadMatFromAsset("track1.jpg");
        } catch(IOException e) {
            e.printStackTrace();
            return;
        }

        Mat lines = ImageProcessing.ProcessImage(rgbaImage);
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
                Imgproc.line(rgbaImage, start, end, new Scalar(0,255, 0, 255),10);


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

        Imgproc.putText(rgbaImage,
                        avg < 0? "L" : "R",
                        new Point(50, rgbaImage.height() - 50),
                        Core.FONT_HERSHEY_SIMPLEX,
                        10,
                        new Scalar(0, 0, 255),
                        20);

        Bitmap image = ImageProcessing.MatToBitmap(rgbaImage);
        imageView.setImageBitmap(image);
    }

    private Mat LoadMatFromAsset(String path) throws IOException {
        Bitmap imageBitmap;
        Mat imageMat;
        try {
            InputStream is = getAssets().open(path);
            imageBitmap = BitmapFactory.decodeStream(is);
            imageMat = new Mat(imageBitmap.getHeight(), imageBitmap.getWidth(), CvType.CV_8UC1);
            Utils.bitmapToMat(imageBitmap, imageMat);
        } catch (IOException e) {
            throw e;
        }
        return  imageMat;
    }

//    private Mat CropMat(Mat image) {
//        Rect regionOfInterest = new Rect(0, image.height() / 2, image.width(), image.height() / 2);
//        return new Mat(image, regionOfInterest);
//    }


    @Override
    protected void onResume() {
        super.onResume();

        if (OpenCVLoader.initDebug()){
            Log.d("openCV", "Connected");
            //display when the activity resumed,, callback loader
            mCallBackLoader.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }else{
            Log.d("openCV", "Not connected");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_3_0, this, mCallBackLoader);
        }
    }

}