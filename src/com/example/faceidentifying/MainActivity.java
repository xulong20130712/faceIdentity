package com.example.faceidentifying;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	float eyedistance;
	private int imageWidth, imageHeight;
	private int numberOfFace = 5;
	private FaceDetector myFaceDetect;
	private FaceDetector.Face[] myFace;
	float myEyesDistance;
	int numberOfFaceDetected;
	Bitmap myBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ImageView img1 = (ImageView) findViewById(R.id.img1);
		ImageView img2 = (ImageView) findViewById(R.id.img2);
		BitmapFactory.Options BitmapFactoryOptionsbfo = new BitmapFactory.Options();
		BitmapFactoryOptionsbfo.inPreferredConfig = Bitmap.Config.RGB_565;
		myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.p1, BitmapFactoryOptionsbfo);
		imageWidth = myBitmap.getWidth();
		imageHeight = myBitmap.getHeight();
		if (imageWidth % 2 != 0) {
			// 如果图片的宽度是奇数无法识别人脸，这里要把奇数变为偶数
			myBitmap = zoomImage(myBitmap, imageWidth + 1, imageHeight);
			imageWidth = myBitmap.getWidth();
			imageHeight = myBitmap.getHeight();
		}

		myFace = new FaceDetector.Face[numberOfFace];
		myFaceDetect = new FaceDetector(imageWidth, imageHeight, numberOfFace);
		numberOfFaceDetected = myFaceDetect.findFaces(myBitmap, myFace);
		Toast.makeText(this, "找到" + numberOfFaceDetected + "张脸", 1).show();
		for (int i = 0; i < numberOfFaceDetected; i++) {
			Face face = myFace[i];
			PointF myMidPoint = new PointF();
			face.getMidPoint(myMidPoint);// 人脸的中间点
			myEyesDistance = face.eyesDistance();// 这是一只眼睛距离中心点的距离，你要得到脸部大概长度，乘以2
			if (i == 0) {

				Bitmap newbit = myBitmap.createBitmap(myBitmap, (int) (myMidPoint.x - myEyesDistance), (int) (myMidPoint.y - myEyesDistance / 2), (int) (myEyesDistance * 2), (int) (myEyesDistance * 2));
				img2.setImageBitmap(newbit);
			}
		}
	}

	public Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
		return bitmap;
	}
}