package korme.xyz.education.service.ALiYunOssUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.comm.Protocol;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import com.aliyun.oss.model.*;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.green.model.v20180509.TextScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import korme.xyz.education.service.uuidUTil.UuidUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class ALiYunOssUtil {
    @Value("${oss.endpoint}")
    private String endpoint;
    @Value("${oss.accessKeyId}")
    private String accessKeyId;
    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${oss.textEndpoint}")
    String textEndpoint;
    @Autowired
    UuidUtil uuidUtil;
    private static List<PartETag> partETags;
    private OSSClient ossClient = null;

    /*
    * 文本检测，返回检测完的文本
    * */
    public List<String> textScan(List<String> textList) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile(textEndpoint, accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        TextScanRequest textScanRequest = new TextScanRequest();
        textScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
        textScanRequest.setHttpContentType(FormatType.JSON);
        textScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        textScanRequest.setEncoding("UTF-8");
        textScanRequest.setRegionId(textEndpoint);
        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
        List<String> result=new ArrayList<>();
        for(String i:textList){
            Map<String, Object> task = new LinkedHashMap<String, Object>();
            task.put("dataId", UUID.randomUUID().toString());
            /**
             * 待检测的文本，长度不超过10000个字符
             */
            task.put("content",i);
            tasks.add(task);
        }
        JSONObject data = new JSONObject();
        data.put("scenes", Arrays.asList("antispam"));
        data.put("tasks", tasks);
        textScanRequest.setHttpContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);
        // 请务必设置超时时间
        textScanRequest.setConnectTimeout(3000);
        textScanRequest.setReadTimeout(6000);
            HttpResponse httpResponse = client.doAction(textScanRequest);
            if(httpResponse.isSuccess()){
                JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));
                if (200 == scrResponse.getInteger("code")) {
                    JSONArray taskResults = scrResponse.getJSONArray("data");
                    for (Object taskResult : taskResults) {
                        if(200 == ((JSONObject)taskResult).getInteger("code")){
                            JSONArray sceneResults = ((JSONObject)taskResult).getJSONArray("results");
                            if(((JSONObject)taskResult).containsKey("filteredContent")){
                                throw new Exception();
                                //result.add((String)((JSONObject)taskResult).get("filteredContent"));
                            }

                            else
                                result.add((String)((JSONObject)taskResult).get("content"));

                        }else{
                            //System.out.println("task process fail:" + ((JSONObject)taskResult).getInteger("code"));
                        }
                    }
                } else {
                    //System.out.println("detect not success. code:" + scrResponse.getInteger("code"));
                    throw new ClientException();
                }
            }else{
                //System.out.println("response not success. status:" + httpResponse.getStatus());
                throw new ClientException();
            }
            return result;
    }
    /*
    * 返回临时上传签名及链接信息
    *
    * */
    public String getTempUploadUrl(String bucketName) throws IOException {
        String host = "https://" + bucketName + "." + endpoint; // host的格式为 bucketname.endpoint
        String fileName = uuidUtil.getUUID(); // 用户上传文件时指定的前缀。
        ClientConfiguration conf = new ClientConfiguration();
        conf.setProtocol(Protocol.HTTPS);
        this.ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret,conf);
        try {
            long expireEndTime = System.currentTimeMillis() + 60 * 1000;
            Date expiration = new Date(expireEndTime);//过期时间
            PolicyConditions policyConds = new PolicyConditions();//条件
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 10485760);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, fileName);
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);
            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessKeyId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("fileName", fileName);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            respMap.put("callback", "");
            String policy = JSON.toJSON(respMap).toString();
            System.out.println(policy);
            return policy;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
    }









    /**
     * @Description: 创建bucket
     * @author liudongxin
     * @date 2019年4月5日 上午9:07:31
     */
    public int createBucket(String bucketName, int control) {
        try {
            if (!bucketIsExist(bucketName)) {
                this.ossClient = new OSSClient(this.endpoint, this.accessKeyId,this.accessKeySecret);
                this.ossClient.createBucket(bucketName);

                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                switch (control) {
                    case 0:
                        createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                        break;
                    case 1:
                        createBucketRequest.setCannedACL(CannedAccessControlList.PublicReadWrite);
                        break;
                    case 2:
                        createBucketRequest.setCannedACL(CannedAccessControlList.Private);
                        break;
                    case 3:
                        createBucketRequest.setCannedACL(CannedAccessControlList.Default);
                        break;
                    default:
                        return 1;
                }
                this.ossClient.createBucket(createBucketRequest);
                return 0;
            }
            return 1;
        } catch (Exception e) {
            return 1;
        } finally {
            if (this.ossClient != null) {
                this.ossClient.shutdown();
            }
        }
    }

    /**
     *
     * @Description: 判断bucket是否存在
     * @author liudongxin
     * @date 2019年4月5日 上午9:08:15
     */
    public boolean bucketIsExist(String bucketName) {
        try {
            this.ossClient = new OSSClient(this.endpoint, this.accessKeyId,this.accessKeySecret);

            boolean exists = this.ossClient.doesBucketExist(bucketName);

            return exists;
        } catch (Exception e) {
            return true;
        } finally {
            if (this.ossClient != null) {
                this.ossClient.shutdown();
            }
        }
    }

    /**
     *
     * @Description: 创建bucket目录
     * @author liudongxin
     * @date 2019年4月5日 上午9:08:37
     */
    public int createFolder(String bucketName, String folderName) {
        try {
            this.ossClient = new OSSClient(this.endpoint, this.accessKeyId,this.accessKeySecret);

            this.ossClient.putObject(bucketName, folderName,new ByteArrayInputStream(new byte[0]));
            return 0;
        } catch (Exception e) {
            return 1;
        } finally {
            if (this.ossClient != null) {
                this.ossClient.shutdown();
            }
        }
    }

    /**
     * @Description: 判断某个文件是否存在
     * @author liudongxin
     * @date 2019年4月5日 上午9:08:54
     */
    public boolean objectIsExist(String bucketName, String key) {
        try {
            this.ossClient = new OSSClient(this.endpoint, this.accessKeyId,this.accessKeySecret);

            boolean found = this.ossClient.doesObjectExist(bucketName, key);

            return found;
        } catch (Exception e) {
            return true;
        } finally {
            if (this.ossClient != null) {
                this.ossClient.shutdown();
            }
        }
    }

    /**
     * @Description: 10M以下单文件上传,文件转换为流的方式
     * @author liudongxin
     * @date 2019年4月5日 上午9:09:52
     */
    public int simpleUpload(String bucketName, String key, File uploadFile) {
        FileInputStream inputStream = null;
        try {
            String filePath =  uploadFile.getAbsolutePath();
            this.ossClient = new OSSClient(this.endpoint, this.accessKeyId,this.accessKeySecret);
            inputStream = new FileInputStream(uploadFile);
            this.ossClient.putObject(bucketName, key, inputStream);
            //this.ossClient.putObject(new PutObjectRequest(bucketName, key,new File(filePath)).<PutObjectRequest> withProgressListener(new PutObjectProgressListener()));
            return 0;
        } catch (Exception e) {
            return 1;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (this.ossClient != null) {
                this.ossClient.shutdown();
            }
        }
    }

    /**
     * @Description: 10M以上单文件分片上传
     * @author liudongxin
     * @date 2019年4月5日 上午9:10:06
     */
    public int multipartUploadSample(String bucketName, String key,File uploadFile) {
        this.ossClient = new OSSClient(this.endpoint, this.accessKeyId,this.accessKeySecret);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        partETags = Collections.synchronizedList(new ArrayList<PartETag>());
        try {
            String uploadId = claimUploadId(bucketName, key);

            long partSize = 5242880L;

            long fileLength = uploadFile.length();
            int partCount = (int) (fileLength / partSize);
            if (fileLength % 5242880L != 0L) {
                partCount++;
            }
            if (partCount > 10000) {
                throw new RuntimeException("Total parts count should not exceed 10000");
            }
            for (int i = 0; i < partCount; i++) {
                long startPos = i * 5242880L;
                long curPartSize = i + 1 == partCount ? fileLength - startPos: 5242880L;
                executorService.execute(new PartUploader(uploadFile, startPos,curPartSize, i + 1, uploadId, bucketName, key));
            }
            executorService.shutdown();
            while (!executorService.isTerminated()) {
                try {
                    executorService.awaitTermination(5L, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (partETags.size() != partCount) {
                throw new IllegalStateException("Upload multiparts fail due to some parts are not finished yet");
            }

            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentType("video/mp4");
            // Sets the custom metadata.
            meta.addUserMetadata("meta", "meta-value");

            listAllParts(uploadId, bucketName, key);

            completeMultipartUpload(uploadId, bucketName, key);

            this.ossClient.getObject(new GetObjectRequest(bucketName, key),uploadFile);

            // 设置文件的访问权限为私有。
            this.ossClient.setObjectAcl(bucketName, key, CannedAccessControlList.Private);

            return 0;
        } catch (OSSException oe) {
            oe.printStackTrace();
            return 1;
        } catch (ClientException ce) {
            ce.printStackTrace();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        } finally {
            if (this.ossClient != null) {
                this.ossClient.shutdown();
            }
        }
    }

    /**
     * @Description: 小文件多文件上传
     * @author liudongxin
     * @date 2019年4月5日 上午9:10:31
     */
    public int multipleFileUploadSmall(String bucketName, List<String> keys,List<File> uploadFiles) throws Exception {
        try {
            this.ossClient = new OSSClient(this.endpoint, this.accessKeyId,this.accessKeySecret);
            for (int i = 0; i < uploadFiles.size(); i++) {
                System.out.println(keys.get(i));
                this.ossClient.putObject(bucketName, (String) keys.get(i),(File) uploadFiles.get(i));
            }
            return 0;
        } catch (Exception e) {
            throw e;
        } finally {
            if (this.ossClient != null) {
                this.ossClient.shutdown();
            }
        }
    }

    /**
     * @Description: 多文件上传到Oss
     * @author liudongxin
     * @date 2019年4月5日 上午9:14:33
     */
    public int multipleFileUploadSmallLimit(String bucketName,
                                            List<String> keys, List<File> uploadFiles, int width, int height)
            throws Exception {
        FileInputStream in = null;
        try {
            int is = 1;
            this.ossClient = new OSSClient(this.endpoint, this.accessKeyId,
                    this.accessKeySecret);
            for (int i = 0; i < uploadFiles.size(); i++) {
                in = new FileInputStream((File) uploadFiles.get(i));
                BufferedImage bi = ImageIO.read(in);
                if ((bi.getWidth() != width) || (bi.getHeight() != height)) {
                    is = 3;
                    break;
                }
                is = 0;

                this.ossClient.putObject(bucketName, (String) keys.get(i),
                        (File) uploadFiles.get(i));
            }
            return is;
        } catch (Exception e) {
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (this.ossClient != null) {
                this.ossClient.shutdown();
            }
        }
    }

    /**
     * @Description: 获得OSS中文件的授权下载URL)
     * @author liudongxin
     * @date 2019年4月5日 上午9:15:02
     */
    public String getDownloadUrl(String bucketName, String key, Date expiration) {
        try {
            this.ossClient = new OSSClient(this.endpoint, this.accessKeyId,
                    this.accessKeySecret);

            URL url = this.ossClient.generatePresignedUrl(bucketName, key,
                    expiration);
            return url.toString();
        } catch (Exception e) {
            return null;
        } finally {
            if (this.ossClient != null) {
                this.ossClient.shutdown();
            }
        }
    }

    /**
     * @Description: 从OSS上下载指定文件，存储在本地指定位置
     * @author liudongxin
     * @date 2019年4月5日 上午9:15:25
     */
    public int downloadFile(String bucketName, String key, String directory) {
        try {
            this.ossClient = new OSSClient(this.endpoint, this.accessKeyId,this.accessKeySecret);
            File file = new File(directory);
            if (file.isDirectory()) {
                this.ossClient.getObject(new GetObjectRequest(bucketName, key),new File(file, key));
            } else {
                if (file.exists()) {
                    file.delete();
                }
                this.ossClient.getObject(new GetObjectRequest(bucketName, key),new File(directory));
            }
            return 0;
        } catch (Exception e) {
            return 1;
        } finally {
            if (this.ossClient != null) {
                this.ossClient.shutdown();
            }
        }
    }

    /**
     * @Description: 下载OSS上的多文件到本地指定文件夹
     * @author liudongxin
     * @date 2019年4月5日 上午9:15:58
     */
    public int downloadFiles(List<String> bucketNames, List<String> keys,
                             String directory) {
        try {
            this.ossClient = new OSSClient(this.endpoint, this.accessKeyId,this.accessKeySecret);
            for (int i = 0; i < keys.size(); i++) {
                File file = new File(directory, (String) keys.get(i));
                if (file.exists()) {
                    file.delete();
                }
                this.ossClient.getObject(new GetObjectRequest(
                                (String) bucketNames.get(i), (String) keys.get(i)),
                        new File(directory, (String) keys.get(i)));
            }
            return 0;
        } catch (Exception e) {
            return 1;
        } finally {
            if (this.ossClient != null) {
                this.ossClient.shutdown();
            }
        }
    }

    /**
     * @Description: 删除OSS上的文件
     * @author liudongxin
     * @date 2019年4月5日 上午9:16:20
     */
    public int deleteOssFile(String bucketName, String key) {
        try {
            this.ossClient = new OSSClient(this.endpoint, this.accessKeyId,this.accessKeySecret);

            this.ossClient.deleteObject(bucketName, key);
            return 0;
        } catch (Exception e) {
            return 1;
        } finally {
            if (this.ossClient != null) {
                this.ossClient.shutdown();
            }
        }
    }

    /**
     * @Description: 删除OSS上的多个文件
     * @author liudongxin
     * @date 2019年4月5日 上午9:16:50
     */
    public int deleteOssFiles(String bucketName, List<String> keys) {
        try {
            this.ossClient = new OSSClient(this.endpoint, this.accessKeyId,this.accessKeySecret);

            DeleteObjectsResult deleteObjectsResult = this.ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(keys));

            List<String> deletedObjects = deleteObjectsResult.getDeletedObjects();

            return 0;
        } catch (Exception e) {
            return 1;
        } finally {
            if (this.ossClient != null) {
                this.ossClient.shutdown();
            }
        }
    }

    /**
     * @Description: 复制源Bucket中的单个文件到目的Bucket中
     * @author liudongxin
     * @date 2019年4月5日 上午9:17:23
     */
    public int copyFile(String srcBucketName, String srcKey,String destBucketName, String destKey) {
        try {
            this.ossClient = new OSSClient(this.endpoint, this.accessKeyId,this.accessKeySecret);

            CopyObjectResult result = this.ossClient.copyObject(srcBucketName,srcKey, destBucketName, destKey);
            return 0;
        } catch (Exception e) {
            return 1;
        } finally {
            if (this.ossClient != null) {
                this.ossClient.shutdown();
            }
        }
    }

    /**
     * @Description: (复制源Bucket中的单个文件到目的Bucket中，并且删除源Bucket中的文件
     * @author liudongxin
     * @date 2019年4月5日 上午9:17:42
     */
    public int copyFileAndDeleteSrc(String srcBucketName, String srcKey,
                                    String destBucketName, String destKey) {
        try {
            this.ossClient = new OSSClient(this.endpoint, this.accessKeyId,this.accessKeySecret);

            CopyObjectResult result = this.ossClient.copyObject(srcBucketName,srcKey, destBucketName, destKey);
            this.ossClient.deleteObject(srcBucketName, srcKey);
            return 0;
        } catch (Exception e) {
            return 1;
        } finally {
            if (this.ossClient != null) {
                this.ossClient.shutdown();
            }
        }
    }

    /**
     * @Description:(复制源Bucket中的多个文件到目的Bucket中
     * @author liudongxin
     * @date 2019年4月5日 上午9:18:04
     */
    public int copyFiles(String srcBucketName, List<String> srcKeys,
                         String destBucketName, List<String> destKeys) {
        try {
            this.ossClient = new OSSClient(this.endpoint, this.accessKeyId,this.accessKeySecret);
            for (int i = 0; i < srcKeys.size(); i++) {
                CopyObjectResult localCopyObjectResult = this.ossClient
                        .copyObject(srcBucketName, (String) srcKeys.get(i),
                                destBucketName, (String) destKeys.get(i));
            }
            return 0;
        } catch (Exception e) {
            return 1;
        } finally {
            if (this.ossClient != null) {
                this.ossClient.shutdown();
            }
        }
    }

    /**
     * @Description: 复制源Bucket中的多个文件到目的Bucket中，并且删除源Bucket中的文件
     * @author liudongxin
     * @date 2019年4月5日 上午9:18:27
     */
    public int copyFilesAndDeleteSrc(String srcBucketName,List<String> srcKeys, String destBucketName, List<String> destKeys) {
        try {
            this.ossClient = new OSSClient(this.endpoint, this.accessKeyId, this.accessKeySecret);
            for (int i = 0; i < srcKeys.size(); i++) {
                CopyObjectResult localCopyObjectResult = this.ossClient
                        .copyObject(srcBucketName, (String) srcKeys.get(i),
                                destBucketName, (String) destKeys.get(i));
            }
            DeleteObjectsResult deleteObjectsResult = this.ossClient
                    .deleteObjects(new DeleteObjectsRequest(srcBucketName)
                            .withKeys(srcKeys));
            Object deletedObjects = deleteObjectsResult.getDeletedObjects();
            return 0;
        } catch (Exception e) {
            return 1;
        } finally {
            if (this.ossClient != null) {
                this.ossClient.shutdown();
            }
        }
    }

    /**
     * @Description: 设置oss文件权限
     * @author liudongxin
     * @date 2019年4月5日 上午9:04:21
     */
    public int setFilePermission(String key,String bucketName) {
        try {
            this.ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            // 设置文件的访问权限为私有。
            this.ossClient.setObjectAcl(bucketName, key, CannedAccessControlList.Private);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }finally{
            if (this.ossClient != null) {
                this.ossClient.shutdown();
            }
        }
    }

    /**
     * @Description: 获取文件权限
     * @author liudongxin
     * @date 2019年4月5日 上午9:25:13
     */
    public int getFilePermission(String key,String bucketName) {
        try {
            // 创建OSSClient实例。
            this.ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            // 获取文件的访问权限。
            ObjectAcl objectAcl = ossClient.getObjectAcl(bucketName,key);
            System.out.println(objectAcl.getPermission().toString());
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }finally{
            if (this.ossClient != null) {
                this.ossClient.shutdown();
            }
        }
    }

    private class PartUploader implements Runnable {
        private File localFile;
        private long startPos;
        private long partSize;
        private int partNumber;
        private String uploadId;
        private String bucketName;
        private String key;

        public PartUploader(File localFile, long startPos, long partSize,
                            int partNumber, String uploadId, String bucketName, String key) {
            this.localFile = localFile;
            this.startPos = startPos;
            this.partSize = partSize;
            this.partNumber = partNumber;
            this.uploadId = uploadId;
            this.bucketName = bucketName;
            this.key = key;
        }

        public void run(){
            InputStream instream = null;
            try
            {
                instream = new FileInputStream(this.localFile);
                instream.skip(this.startPos);

                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(this.bucketName);
                uploadPartRequest.setKey(this.key);
                uploadPartRequest.setUploadId(this.uploadId);
                uploadPartRequest.setInputStream(instream);
                uploadPartRequest.setPartSize(this.partSize);
                uploadPartRequest.setPartNumber(this.partNumber);

                UploadPartResult uploadPartResult = ALiYunOssUtil.this.ossClient.uploadPart(uploadPartRequest);
                synchronized (ALiYunOssUtil.partETags){
                    ALiYunOssUtil.partETags.add(uploadPartResult.getPartETag());
                }
            }
            catch (Exception e){
                e.printStackTrace();
                if (instream == null) {
                    return;
                }
                try{
                    instream.close();
                }
                catch (IOException e1){
                    e1.printStackTrace();
                }
            }
            finally{
                if (instream != null) {
                    try{
                        instream.close();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
            if (instream != null) {
                try
                {
                    instream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private File createSampleFile() throws IOException {
        File file = File.createTempFile("oss-java-sdk-", ".txt");
        file.deleteOnExit();

        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        for (int i = 0; i < 1000000; i++) {
            writer.write("abcdefghijklmnopqrstuvwxyz\n");
            writer.write("0123456789011234567890\n");
        }
        writer.close();

        return file;
    }

    private String claimUploadId(String bucketName, String key) {
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, key);
        InitiateMultipartUploadResult result = this.ossClient.initiateMultipartUpload(request);
        return result.getUploadId();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void completeMultipartUpload(String uploadId, String bucketName,String key) {
        Collections.sort(partETags, new Comparator() {
            public int compare(Object o1, Object o2) {
                PartETag p1=(PartETag)o1;
                PartETag p2=(PartETag)o2;
                return p1.getPartNumber() - p2.getPartNumber();
            }
        });
        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, key, uploadId, partETags);
        this.ossClient.completeMultipartUpload(completeMultipartUploadRequest);
    }

    private void listAllParts(String uploadId, String bucketName, String key) {
        ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName,key, uploadId);
        PartListing partListing = this.ossClient.listParts(listPartsRequest);

        int partCount = partListing.getParts().size();
        PartSummary partSummary = null;
        for (int i = 0; i < partCount; i++) {
            partSummary = (PartSummary) partListing.getParts().get(i);
        }
    }

    private class PutObjectProgressListener implements ProgressListener {
        private long bytesWritten = 0;
        private long totalBytes = -1;
        private boolean succeed = false;

        @Override
        public void progressChanged(ProgressEvent progressEvent) {
            long bytes = progressEvent.getBytes();
            ProgressEventType eventType = progressEvent.getEventType();
            switch (eventType) {
                case TRANSFER_STARTED_EVENT:
                    System.out.println("开始上传...");
                    break;
                case REQUEST_CONTENT_LENGTH_EVENT:
                    this.totalBytes = bytes;
                    System.out.println(this.totalBytes+ " 字节数将上传到OSS");
                    break;
                case REQUEST_BYTE_TRANSFER_EVENT:
                    this.bytesWritten += bytes;
                    if (this.totalBytes != -1) {
                        int percent = (int) (this.bytesWritten * 100.0 / this.totalBytes);
                        //System.out.println("进度: "+ percent + "%(" + this.bytesWritten + "/"+ this.totalBytes + ")");
                        System.out.println("进度: "+ String.format("%.1f", percent) + "%");
                    } else {
                        System.out.println("此时已写入字节，上载比率：未知"+ "(" + this.bytesWritten + "/...)");
                    }
                    break;
                case TRANSFER_COMPLETED_EVENT:
                    this.succeed = true;
                    System.out.println("上传成功, " + this.bytesWritten+ " 已传输的字节总数");
                    break;
                case TRANSFER_FAILED_EVENT:
                    System.out.println("上传失败, " + this.bytesWritten+ " 字节已传输");
                    break;
                default:
                    break;
            }
        }

        public boolean isSucceed() {
            return succeed;
        }

        /**
         * @Description: 带进度条上传视频
         * @author liudongxin
         * @date 2019年4月5日 下午7:39:34
         */
        @SuppressWarnings("unused")
        public int  saveVideoInOss(String bucketName,String key,String filePath) {
            // 创建OSSClient实例。
            ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            try {
                // 带进度条的上传。
                ossClient.putObject(new PutObjectRequest(bucketName, key,new File(filePath)).<PutObjectRequest> withProgressListener(new PutObjectProgressListener()));
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }finally{
                if(ossClient != null){
                    // 关闭OSSClient。
                    ossClient.shutdown();
                }
            }
        }
    }
}
