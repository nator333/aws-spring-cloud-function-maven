package com.serverless.services;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.services.rekognition.model.S3Object;
import java.util.List;

public class AwsRekognitionService {

  public void ddd(){

    String photo = "YOUR Photo.jpg";
    String bucket = "YOUR bucket NAME";
    AWSCredentials credentials;
    try {
      credentials = new ProfileCredentialsProvider("adminuser").getCredentials();
    } catch(Exception e) {
      throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
          + "Please make sure that your credentials file is at the correct "
          + "location (/Users/userid/.aws/credentials), and is in a valid format.", e);
    }

    AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder
        .standard()
        .withRegion(Regions.US_WEST_2)
        .withCredentials(new AWSStaticCredentialsProvider(credentials))
        .build();

    DetectLabelsRequest request = new DetectLabelsRequest()
        .withImage(new Image()
            .withS3Object(new S3Object()
                .withName(photo).withBucket(bucket)))
        .withMaxLabels(9)
        .withMinConfidence(77F);

    try {
      DetectLabelsResult result = rekognitionClient.detectLabels(request);
      List<Label> labels = result.getLabels();
      System.out.println("Detectedls for " + photo);

      for (Label label: labels) {
        System.out.println(label.getName() + ": " + label.getConfidence().toString());

      }
    } catch(AmazonRekognitionException e) {
      e.printStackTrace();
    }
  }

}
