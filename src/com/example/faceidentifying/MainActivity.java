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
			// ���ͼƬ�Ŀ���������޷�ʶ������������Ҫ��������Ϊż��
			myBitmap = zoomImage(myBitmap, imageWidth + 1, imageHeight);
			imageWidth = myBitmap.getWidth();
			imageHeight = myBitmap.getHeight();
		}

		myFace = new FaceDetector.Face[numberOfFace];
		myFaceDetect = new FaceDetector(imageWidth, imageHeight, numberOfFace);
		numberOfFaceDetected = myFaceDetect.findFaces(myBitmap, myFace);
		Toast.makeText(this, "�ҵ�" + numberOfFaceDetected + "����", 1).show();
		for (int i = 0; i < numberOfFaceDetected; i++) {
			Face face = myFace[i];
			PointF myMidPoint = new PointF();
			face.getMidPoint(myMidPoint);// �������м��
			myEyesDistance = face.eyesDistance();// ����һֻ�۾��������ĵ�ľ��룬��Ҫ�õ�������ų��ȣ�����2
			if (i == 0) {

				Bitmap newbit = myBitmap.createBitmap(myBitmap, (int) (myMidPoint.x - myEyesDistance), (int) (myMidPoint.y - myEyesDistance / 2), (int) (myEyesDistance * 2), (int) (myEyesDistance * 2));
				img2.setImageBitmap(newbit);
			}
		}
	}

	public Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
		// ��ȡ���ͼƬ�Ŀ�͸�
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// ��������ͼƬ�õ�matrix����
		Matrix matrix = new Matrix();
		// ������������
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// ����ͼƬ����
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
		return bitmap;
	}
}