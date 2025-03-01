package com.delibrary.lib_backend.client.imagestorage;

import java.io.IOException;
import java.io.InputStream;

public interface ImageStorageClient {

    String uploadImage(String containerName, String originalImageName, InputStream data, long length) throws IOException;

}
