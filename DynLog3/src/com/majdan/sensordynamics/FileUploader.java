package com.majdan.sensordynamics;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

public class FileUploader {
	
	public static String getFileName(String name) {
		return (new SimpleDateFormat("yyyy_MM_dd__H_m_s__S__").format(new Date())+UUID.randomUUID().toString())+("__"+name);
	}
	
	@SuppressLint("NewApi")
	//public static void save(MyActivity activity, Serializable object, String fileName) {
	public static int save(FixedActivity activity, LinkedHashMap<String, FixedKeystrokeContainer> dataToSend) {
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		
        FTPClient client = new FTPClient();
        InputStream fis = null;
        int count = 0;
        try {
        	
            client.connect(InetAddress.getByName("students.mimuw.edu.pl"));
            client.login("km277713", "kaisag2jk");
            
            client.enterLocalPassiveMode();
            client.setFileType(FTP.BINARY_FILE_TYPE);
            
            client.changeWorkingDirectory("mgr/out");
            
            

            //PipedInputStream  writeIn = new PipedInputStream();
            //PipedOutputStream readOut = new PipedOutputStream( writeIn );
            
            //KeystrokesContainer kc = new KeystrokesContainer(this.keystrokes);
            //ObjectOutputStream oos = new ObjectOutputStream(readOut);
            
            //oos.writeObject(kc);
            
            for(Entry<String, FixedKeystrokeContainer> data : dataToSend.entrySet()) {
            	Log.v("TMP", data.getKey());
	            FileOutputStream fos = activity.openFileOutput(data.getKey(), Context.MODE_PRIVATE);
	            
	            ObjectOutputStream oos = new ObjectOutputStream(fos);
	            oos.writeObject(data.getValue());
	            fos.close();
	            
	
	            FileInputStream fisx = activity.openFileInput(data.getKey());
	            
	            
	            client.storeFile(data.getKey(), fisx);
	            
	            fisx.close();
	            
	            activity.deleteFile(data.getKey());
	            
	            count++;
            }
            
            client.logout();
            
        } catch (Exception e) {
        	activity.alert("Exception: "+e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) { 
                    fis.close();
                }
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return count;
        }
	}

	
	
	/*
	private void save() {
		final TextView messageText = ((TextView) findViewById(R.id.left));
		
        dialog = ProgressDialog.show(MainActivity.this, "", "Uploading file...", true);
        
        new Thread(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            messageText.setText("uploading started.....");
                        }
                    });  
                     uploadFile();
                                              
                }
              }).start();   
	}
    public int uploadFile() {
    	
    	final TextView messageText = ((TextView) findViewById(R.id.left));
    	
        HttpURLConnection conn = null;
        DataOutputStream dos = null;  
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024; 
        
             try { 
            	 
            	 String fileName = new SimpleDateFormat("yyyy_MM_dd__H_m_s__S__").format(new Date())+UUID.randomUUID().toString();
            	 
                 PipedInputStream  writeIn = new PipedInputStream();
                 PipedOutputStream readOut = new PipedOutputStream( writeIn );
                 
                 KeystrokesContainer kc = new KeystrokesContainer(this.keystrokes);
                 ObjectOutputStream oos = new ObjectOutputStream(readOut);
                 
                 oos.writeObject(kc);
                  
                 URL url = new URL(upLoadServerUri);
                  
                 // Open a HTTP  connection to  the URL
                 conn = (HttpURLConnection) url.openConnection(); 
                 conn.setDoInput(true); // Allow Inputs
                 conn.setDoOutput(true); // Allow Outputs
                 conn.setUseCaches(false); // Don't use a Cached Copy
                 conn.setRequestMethod("POST");
                 conn.setRequestProperty("Connection", "Keep-Alive");
                 conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                 conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                 conn.setRequestProperty("uploaded_file", fileName);
                  
                 dos = new DataOutputStream(conn.getOutputStream());
                 
                 dos.writeBytes(twoHyphens + boundary + lineEnd); 
                 dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                                           + fileName + "\"" + lineEnd);
                  
                 dos.writeBytes(lineEnd);
        
                 // create a buffer of  maximum size
                 bytesAvailable = writeIn.available(); 
        
                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
                 buffer = new byte[bufferSize];
        
                 // read file and write it into form...
                 bytesRead = writeIn.read(buffer, 0, bufferSize);  
                    
                 while (bytesRead > 0) {
                      
                   dos.write(buffer, 0, bufferSize);
                   bytesAvailable = writeIn.available();
                   bufferSize = Math.min(bytesAvailable, maxBufferSize);
                   bytesRead = writeIn.read(buffer, 0, bufferSize);   
                    
                  }
        
                 // send multipart form data necesssary after file data...
                 dos.writeBytes(lineEnd);
                 dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
        
                 // Responses from the server (code and message)
                 serverResponseCode = conn.getResponseCode();
                 String serverResponseMessage = conn.getResponseMessage();
                   
                 Log.i("uploadFile", "HTTP Response is : "
                         + serverResponseMessage + ": " + serverResponseCode);
                  
                 if(serverResponseCode == 200){
                      
                     runOnUiThread(new Runnable() {
                          public void run() {
                               
                              String msg = "File Upload Completed";
                               
                              messageText.setText(msg);
                              Toast.makeText(MainActivity.this, "File Upload Complete.", 
                                           Toast.LENGTH_SHORT).show();
                          }
                      });                
                 }    
                  
                 //close the streams //
                 writeIn.close();
                 dos.flush();
                 dos.close();
                   
            } catch (MalformedURLException ex) {
                 
                dialog.dismiss();  
                ex.printStackTrace();
                 
                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(MainActivity.this, "MalformedURLException", 
                                                            Toast.LENGTH_SHORT).show();
                    }
                });
                 
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
            } catch (Exception e) {
                 
                dialog.dismiss();  
                e.printStackTrace();
                 
                runOnUiThread(new Runnable() {
                    public void run() {
                        messageText.setText("Got Exception : see logcat ");
                        Toast.makeText(MainActivity.this, "Got Exception : see logcat ", 
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server Exception", "Exception : "
                                                 + e.getMessage(), e);  
            }
            dialog.dismiss();       
            return serverResponseCode; 
       } 
	*/
}
