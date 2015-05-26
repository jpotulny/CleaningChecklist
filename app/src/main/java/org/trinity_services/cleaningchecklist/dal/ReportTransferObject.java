package org.trinity_services.cleaningchecklist.dal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.DefaultedHttpParams;
import org.trinity_services.cleaningchecklist.models.Checklist;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by JLP on 8/22/2014.
 *  Copyright [2014] [Trinity Services Inc.]

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Applies to Apache Commons licensed libraries.  All Trinity generated code is
 <copy>2014</copy> Trinity Services, all rights reserved, and may not be used by third parties
 without express written permission of Trinity Services, Inc. - an Illinois not for profit
 Corporation.
 */
public class ReportTransferObject {

    private static final String LOG_TAG = "[TSI]Cleaning Checklist";

    private String destinationURLString;

    private String internalStorageDirectory;
    private String externalStorageDirectory;
    public ReportTransferObject() {

    }

    public void sendReport(Checklist checklist) {
        sendToWebService(checklist);

    }
    public void sendReportToFile(Checklist checklist) {

        externalStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();

        try {
            //Set up file stuff
            File filePath = new File(externalStorageDirectory + File.separator + "reports");
            File fileContent = new File(filePath, "report" + System.currentTimeMillis() + ".json");
            filePath.mkdirs();

            //Now we stream the JSON
            BufferedWriter out = new BufferedWriter(new FileWriter(fileContent));
            out.write(checklist.getChecklistData().toString());
            out.flush();
            out.close();

            File[] list = filePath.listFiles();
            for(File f : list)
            {
                Log.d(LOG_TAG,"Files: " + f.getAbsoluteFile());
            }
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "Unable to create file: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG_TAG, "General IO Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setDestination(String destination) {
        this.destinationURLString = destination;
    }
    public String getDestination() {
        Log.w(LOG_TAG, "Returning Destination: " + destinationURLString);
        return destinationURLString;
    }

    //TODO reinitialize this code when you have hibernate sorted on the application server
    public void sendToWebService(final Checklist checklist) {

        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    StringEntity report = new StringEntity(checklist.getChecklistData().toString());
                    HttpResponse response;
                    HttpPost tomcatDestination;
                    HttpClient client;

                    client = new DefaultHttpClient();
                    Log.d(LOG_TAG, "Initializing TomCat paramaters");
                    tomcatDestination = new HttpPost(destinationURLString);
                    tomcatDestination.setHeader("Accept", "application/json");
                    tomcatDestination.setHeader("Content-type", "application/json");
                    tomcatDestination.setEntity(report);
                    Log.d(LOG_TAG,"Tomcat Destination: " +tomcatDestination.getURI() );

                    Log.d(LOG_TAG, "Tomcat paramaters initialized. Next!");


                    InputStream result = null;
                    response = client.execute(tomcatDestination);
                    Log.d(LOG_TAG, "Response executed.");
                    result = response.getEntity().getContent();
                    if(result != null) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(result));
                        String temp;
                        while((temp = br.readLine()) != null) {
                            Log.d(LOG_TAG, temp);
                        }
                    } else {
                        Log.w(LOG_TAG, "Bad response from server");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        t.start();
    }
    public void setInternalStorageDirectory(String internalStorageDirectory) {this.internalStorageDirectory=internalStorageDirectory;}
}
