package com.alibaba.cloud.demo;

import com.alibaba.cloud.faceengine.*;
import com.alibaba.cloud.faceengine.Error;
/**
 * 线下SDK人脸检测demo
 */
public class FaceDetectDemo {

    private static int RunMode = Mode.TERMINAL;

    public static void main(String[] args) {
        //step 1: authorize or enable debug
        FaceEngine.enableDebug(false);
        System.out.println("VENDOR_KEY : " + Utils.VENDOR_KEY);
        int error = FaceEngine.authorize(Utils.VENDOR_KEY);
        if (error != Error.OK) {
            System.out.println("authorize error : " + error);
            return;
        } else {
            System.out.println("authorize OK");
        }


        //step 2: set Cloud addr and account if you using CloudServer
        //FaceEngine.setCloudAddr("101.132.89.177", 15000);
        //FaceEngine.setCloudAddr("127.0.0.1", 8080);
        FaceEngine.setCloudLoginAccount("admin", "admin");


        //face detect
        detectPicture();
    }


    private static void detectPicture() {
        System.out.println("detectPicture begin");

        //detectPicture step1: create FaceDetect, mode type can be TERMINAL or CLOUD
        FaceDetect faceDetect = FaceDetect.createInstance(RunMode);
        if (faceDetect == null) {
            System.out.println("FaceDetect.createInstance error");
            return;
        }

//        byte[] jpegData = Utils.loadFile(Utils.PICTURE_ROOT + "many_faces.jpg");
//        byte[] jpegData = Utils.loadFile(Utils.PICTURE_ROOT + "caijian.jpg");
        byte[] jpegData = Utils.loadFile(Utils.PICTURE_ROOT + "yasuo.jpg");
        //byte[] jpegData = loadFile(PICTURE_ROOT + "fanbingbing_with_glass.jpg");
        Image image = new Image();
        image.data = jpegData;
        image.format = ImageFormat.JPEG;

        //detectPicture step2: set picture detect parameter
        DetectParameter pictureDetectParameter = faceDetect.getPictureParameter();
        pictureDetectParameter.checkQuality = 1;
        pictureDetectParameter.checkLiveness = 1;
        pictureDetectParameter.checkAge = 1;
        pictureDetectParameter.checkGender = 1;
        pictureDetectParameter.checkExpression = 1;
        pictureDetectParameter.checkGlass = 1;
        faceDetect.setPictureParameter(pictureDetectParameter);

        //detectPicture step3: detect picture
        Face[] faces = faceDetect.detectPicture(image);

        if (faces == null) {
            System.out.println("detectPicture faces number:0");
        } else {
            System.out.println("detectPicture faces number:" + faces.length);
            for (int i = 0; i < faces.length; i++) {
                System.out.println("detectPicture faces[" + i + "]:" + faces[i]);
            }
        }

        //detectPicture step4: delete FaceDetect instance
        FaceDetect.deleteInstance(faceDetect);
        faceDetect = null;
        System.out.println("detectPicture end\n\n======================");
    }
}
