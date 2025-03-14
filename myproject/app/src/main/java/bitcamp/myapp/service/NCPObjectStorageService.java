package bitcamp.myapp.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.io.InputStream;

@PropertySource(value = "classpath:bitcamp-study.properties")
public class NCPObjectStorageService implements StorageService {

    final AmazonS3 s3;

    @Value("ncp.end-point")
    private String endPoint;
    @Value("ncp.region-name")
    private String regionName;
    @Value("ncp.access-key")
    private String accessKey;
    @Value("ncp.secret-key")
    private String secretKey;
    @Value("ncp.bucket-name")
    private String bucketName;


    public NCPObjectStorageService(){
        s3 = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }

    public void upload (String filePath, InputStream fileIn) {

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType("application/x-directory");
        PutObjectRequest putObjectRequest = new PutObjectRequest(
                bucketName,         // 버킷 이름
                filePath,           // 업로드 파일의 이름 및 디렉토리 경로
                fileIn,             // 업로드 할 파일의 InputStram
                objectMetadata);    // 업로드에 필요한 부가 정보

        try {
            s3.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new StorageServiceException(e);
        }
    }
}
