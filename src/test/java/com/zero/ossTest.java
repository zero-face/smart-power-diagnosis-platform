//package com.zero;
//
//import com.aliyun.oss.OSS;
//import com.aliyun.oss.OSSClientBuilder;
//import com.aliyun.oss.event.ProgressEvent;
//import com.aliyun.oss.event.ProgressEventType;
//import com.aliyun.oss.event.ProgressListener;
//import com.aliyun.oss.model.GetObjectRequest;
//import com.aliyun.oss.model.PutObjectRequest;
//
//import com.zero.smart_power_diagnosis_platform.common.util.OSSUtils;
//import com.zero.smart_power_diagnosis_platform.controller.TransInfoController;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.io.ByteArrayInputStream;
//import java.io.File;
//
///**
// * @Author Zero
// * @Date 2021/5/11 20:05
// * @Since 1.8
// **/
//@SpringBootTest
//public class ossTest {
//    @Autowired
//    private TransInfoController transInfoController;
//    @Autowired
//    private OSSUtils ossUtils;
//    @Test
//    void createClient() {
//        String endpoint = "oss-cn-beijing.aliyuncs.com";
//        String accessKeyId = "LTAI5t6BVrPsozJmCQRJy83a";
//        String accessKeySecret = "PzCF4JLedmcS3Kc17dKxh957fNXj7D";
//        //创建ossClient实例
//        OSS build = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//        build.shutdown();
//    }
//    @Value("${aliyun.oss.bucketName}")
//    private String bucketName;
//    @Value("${aliyun.oss.endpoint}")
//    private String endpoint;
//    @Value("${aliyun.oss.endpoint}")
//    private String ALIYUN_OSS_ENDPOINT;
//    @Value("${aliyun.oss.accessKeyId}")
//    private String ALIYUN_OSS_ACCESSKEYID;
//    @Value("${aliyun.oss.accessKeySecret}")
//    private String ALIYUN_OSS_ACCESSKEYSECRET;
//
//    @Test
//    void test() {
//        System.out.println(bucketName);
//        System.out.println(endpoint);
//        System.out.println(ALIYUN_OSS_ACCESSKEYID);
//        System.out.println(ALIYUN_OSS_ACCESSKEYSECRET);
//        System.out.println(ALIYUN_OSS_ENDPOINT);
//        ossUtils.getOssClient();
//    }
//
//
//    @Test
//    void createBucketName() {
//        String endpoint = "oss-cn-beijing-internal.aliyuncs.com";
//        String accessKeyId = "LTAI5t6BVrPsozJmCQRJy83a";
//        String accessKeySecret = "PzCF4JLedmcS3Kc17dKxh957fNXj7D";
//        String BucketName = "zero";
//        //创建ossClient实例
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//        //创建存储空间
//        //Bucket bucketName = ossClient.createBucket("zero");
//        ossClient.putObject(BucketName,"1235463.txt",new ByteArrayInputStream("123".getBytes()));
//        ossClient.shutdown();
//    }
//
//
//
//    @Test
//    void listener() {
//        class PutObjectProgressListener implements ProgressListener {
//            private long bytesWritten = 0;
//            private long totalBytes = -1;
//            private boolean succeed = false;
//
//            @Override
//            public void progressChanged(ProgressEvent progressEvent) {
//                long bytes = progressEvent.getBytes();
//                ProgressEventType eventType = progressEvent.getEventType();
//                switch (eventType) {
//                    case TRANSFER_STARTED_EVENT:
//                        System.out.println("Start to upload......");
//                        break;
//                    case REQUEST_CONTENT_LENGTH_EVENT:
//                        this.totalBytes = bytes;
//                        System.out.println(this.totalBytes + " bytes in total will be uploaded to OSS");
//                        break;
//                    case REQUEST_BYTE_TRANSFER_EVENT:
//                        this.bytesWritten += bytes;
//                        if (this.totalBytes != -1) {
//                            int percent = (int)(this.bytesWritten * 100.0 / this.totalBytes);
//                            System.out.println(bytes + " bytes have been written at this time, upload progress: " + percent + "%(" + this.bytesWritten + "/" + this.totalBytes + ")");
//                        } else {
//                            System.out.println(bytes + " bytes have been written at this time, upload ratio: unknown" + "(" + this.bytesWritten + "/...)");
//                        }
//                        break;
//                    case TRANSFER_COMPLETED_EVENT:
//                        this.succeed = true;
//                        System.out.println("Succeed to upload, " + this.bytesWritten + " bytes have been transferred in total");
//                        break;
//                    case TRANSFER_FAILED_EVENT:
//                        System.out.println("Failed to upload, " + this.bytesWritten + " bytes have been transferred");
//                        break;
//                    default:
//                        break;
//                }
//            }
//
//            public boolean isSucceed() {
//                return succeed;
//            }
//
//            public void main(String[] args) {
//                // Endpoint以杭州为例，其它Region请按实际情况填写。
//                String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
//                // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
//                String accessKeyId = "<yourAccessKeyId>";
//                String accessKeySecret = "<yourAccessKeySecret>";
//                String bucketName = "<yourBucketName>";
//                String objectName = "<yourObjectName>";
//
//                // 创建OSSClient实例。
//                OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//
//                try {
//                    // 上传文件的同时指定了进度条参数。
//                    ossClient.putObject(new PutObjectRequest(bucketName, objectName, new File("<yourLocalFile>")).
//                            <PutObjectRequest>withProgressListener(new PutObjectProgressListener()));
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                // 关闭OSSClient。
//                ossClient.shutdown();
//            }
//        }
//
//    }
//    @Test
//    void download() {
//        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
//        String endpoint = "yourEndpoint";
//        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
//        String accessKeyId = "yourAccessKeyId";
//        String accessKeySecret = "yourAccessKeySecret";
//        // 填写Bucket名称。
//        String bucketName = "examplebucket";
//        // 填写不包含Bucket名称在内的Object完整路径，例如testfolder/exampleobject.txt。
//        String objectName = "testfolder/exampleobject.txt";
//
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//
//        // 下载Object到本地文件，并保存到指定的本地路径中。如果指定的本地文件存在会覆盖，不存在则新建。
//        // 如果未指定本地路径，则下载后的文件默认保存到示例程序所属项目对应本地路径中。
//        ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File("D:\\localpath\\examplefile.txt"));
//
//        // 关闭OSSClient。
//        ossClient.shutdown();
//
//
//
//    }
//}
