package com.seniorproject.sallemapp.helpers;

import android.app.Application;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

/**
 * Created by abdul on 09-Mar-2017.
 */

public class AzureBlob {


    public static Bitmap getImage(Context context, String id) throws InvalidKeyException, URISyntaxException, StorageException, IOException {
        CloudBlobContainer container = MyHelper.getBlobContainer();

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
    public static void upload(String imagePath, Bitmap image, Context context){
        try{
            CloudBlobContainer container = MyHelper.getBlobContainer();
            // Upload an image file.
            CloudBlockBlob blob = container.getBlockBlobReference(imagePath);

            File outputDir = context.getCacheDir();
            File sourceFile = File.createTempFile("101", "jpg", outputDir);
            OutputStream outputStream = new FileOutputStream(sourceFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            blob.upload(new FileInputStream(sourceFile), sourceFile.length());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (StorageException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
