package stud.edu.aiopencv;

import android.util.Log;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;




public class Color {

// Enumeration class for color space convertion.
    enum cvColors {
    BGR2RGB,BGR2GRAY,BGR2HLS,BGR2HSV,HLS2BGR,RGB2BGR,RGB2GRAY,RGB2HLS,RGB2HSV
}
   // Not in use at the moment
   // Converts an image to the desired color space.

    public static Mat cvtColor(Mat image, int cvt_color_enum) {
        Mat temp = image.clone();
        if ( cvColors.BGR2RGB.equals(cvt_color_enum)){
            Imgproc.cvtColor(temp,image, Imgproc.COLOR_BGR2RGB);
        }
        else if(  cvColors.BGR2GRAY.equals(cvt_color_enum)) {
            Imgproc.cvtColor(temp,image, Imgproc.COLOR_BGR2GRAY);
        }
        else if(  cvColors.BGR2HLS.equals(cvt_color_enum)) {
            Imgproc.cvtColor(temp,image, Imgproc.COLOR_BGR2HLS);
        }
        else if(  cvColors.BGR2HSV.equals(cvt_color_enum)) {
            Imgproc.cvtColor(temp,image, Imgproc.COLOR_BGR2HSV);
        }
        else if(  cvColors.RGB2BGR.equals(cvt_color_enum)) {
            Imgproc.cvtColor(temp,image, Imgproc.COLOR_RGB2BGR);
        }
        else if( cvColors.RGB2GRAY.equals(cvt_color_enum)) {
            Imgproc.cvtColor(temp,image, Imgproc.COLOR_RGB2GRAY);
        }
        else if(  cvColors.RGB2HLS.equals(cvt_color_enum)) {
            Imgproc.cvtColor(temp,image, Imgproc.COLOR_RGB2HLS);
        }
        else if(  cvColors.RGB2HSV.equals(cvt_color_enum)) {
            Imgproc.cvtColor(temp,image, Imgproc.COLOR_RGB2HSV);
        }
        else if(  cvColors.HLS2BGR.equals(cvt_color_enum)) {
            Imgproc.cvtColor(temp, image, Imgproc.COLOR_HLS2BGR);
        }
        else {
            Log.d("cvtColor","Value is not defined.");
        }
        return image;

    }

    //convert Mat hsv to scalar
    public static Scalar convertScalarHsv2Rgba(Scalar hsvColor) {
        Mat pointMatRgba = new Mat();
        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, hsvColor);
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB);

        return new Scalar(pointMatRgba.get(0, 0));
    }


}
