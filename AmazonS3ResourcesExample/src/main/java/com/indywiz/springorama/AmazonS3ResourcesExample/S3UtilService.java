package com.indywiz.springorama.AmazonS3ResourcesExample;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

@Component
public class S3UtilService {

  @Autowired
  private ResourcePatternResolver resourcePatternResolver;

  public List<String> listS3Files(String bucketName) throws IOException {
    final Resource[] resources = resourcePatternResolver
        .getResources("s3://" + bucketName + "/*.xml");

    return Arrays.stream(resources).map(
        resource -> {
          try {
            return resource.getURL().toExternalForm();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
    ).collect(Collectors.toList());
  }

}
