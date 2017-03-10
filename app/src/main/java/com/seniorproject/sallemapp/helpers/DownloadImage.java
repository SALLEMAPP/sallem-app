package com.seniorproject.sallemapp.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

/**
 * Created by abdul on 09-Mar-2017.
 */

public class DownloadImage {


    public static Bitmap getImage(Context context, String id) throws InvalidKeyException, URISyntaxException, StorageException, IOException {
        CloudStorageAccount account = CloudStorageAccount.parse(CommonMethods.storageConnectionString);
        CloudBlobClient serviceClient = account.createCloudBlobClient();

        // Container name must be lower case.
        CloudBlobContainer container = serviceClient.getContainerReference("sallemphotos");
        //container.createIfNotExists();

        // Upload an image file.
        //String imageName = UUID.randomUUID().toString();
        CloudBlockBlob blob = container.getBlockBlobReference(id);

        File outputDir = context.getCacheDir();
        File sourceFile = File.createTempFile("101", "jpg", outputDir);
        OutputStream outputStream = new FileOutputStream(sourceFile);
        //bm.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        //outputStream.close();
        //blob.upload(new FileInputStream(sourceFile), sourceFile.length());

        // Download the image file.
        File destinationFile = new File(sourceFile.getParentFile(), "image1Download.tmp");
        blob.downloadToFile(destinationFile.getAbsolutePath());
        Bitmap image = BitmapFactory.decodeStream(new FileInputStream(destinationFile));
        return image;



    }
}
