package org.mqtt.api;

/**
 * Created by Anushavijay on 10/2/16.
 */

import org.apache.commons.io.FilenameUtils;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;

import java.io.*;

/*
The API should contain the following:

getfile() (done)
decide topics ()
connect()(done)
sendfile() -> getfile() -> connect-> publish() (done)
disconnect() (done)
recieve()

functions usint this are:
Service discovery;
Registering with Master;
Requesting Resources;
Migration;
Launch docker;
Error Detection;

 */



public class mqttAPI implements MqttCallback {


    protected datastore ds;
    private Throwable t;

    public mqttAPI() {
        ds = new datastore("null", "null");
    }

    public String getFile(String fileSrc) throws IOException {

        System.out.printf(fileSrc);
        ds.setPath(fileSrc);
        int flag = 0;
        boolean exists;


        //check if the file path is a 1. folder, 2. file or 3. string and set that to it
        File file;
        file = new File(ds.getPath());
        byte[] bstream;
        // First, make sure the path exists
        exists = file.exists();
        System.out.printf("file.exists()=" + exists);

        if (exists) {

            // This will tell you if it is a directory
            System.out.printf("File.isDirectory= " + file.isDirectory());
            if (file.isDirectory() == true)
                flag = 1;
            //todo: Zip that file

            System.out.printf("File.isFile= " + file.isFile());
            if (file.isFile() == true)
                flag = 2;

            //For folders -> zip the folder and pass extension as zip
            //For Files -> File name and then extension
            //For strings -> Extensions = none

            //ALL ASSUMING ITS A FILE
            // Extracting extension from file name

            ds.setExt(FilenameUtils.getExtension(ds.getPath()));
            System.out.printf("\nextension=" + ds.getExt());

            ds.setFileName(FilenameUtils.getName(ds.getPath()));
            System.out.printf("\nfilename=" + ds.getFileName());

            //reading zipfile as byte array

            bstream = new byte[(int) file.length()];
            try {
                FileInputStream fileIn = new FileInputStream(file);//inputing the location of file
                System.out.printf("file length:"+ file.length());

                fileIn.read(bstream);
                fileIn.close();
            } catch (FileNotFoundException e) {
                System.out.printf("File not found");
                e.printStackTrace();
            } catch (IOException e1) {
                System.out.println("Error Reading The File.");
                e1.printStackTrace();
            }

            ds.setFileData(bstream.toString());

            System.out.printf("This is the byte array"+ ds.getFileData());

        }

        else{
            System.out.printf("The argument recieved is a string");
            //bstream = new byte[(int) fileSrc.length()];
            bstream=fileSrc.getBytes();
            ds.setFileData(bstream.toString());
            System.out.printf("\nInformation serialized");
        }

//serilaized datastore object.
        File serialiedfile = new File("/Users/Anushavijay/Desktop/Amazon/dockerfile.ser");
        try {
            FileOutputStream fs = new FileOutputStream(serialiedfile);
            ObjectOutputStream outStreamFile = new ObjectOutputStream(fs);
            outStreamFile.writeObject(ds);
            outStreamFile.close();

            //holds the serlialized object in string format
            ds.setFileData(outStreamFile.toString());

            System.out.printf(ds.getFileData());

            System.out.printf("\nDataobject serialized");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds.getFileData();

    }


    public MqttClient connect(String clientID) {

        MqttClient sampleClient = null;
        try {

            String broker = ds.getBrokerIP();
            //String clientId     = "JavaSample";
            String clientId = clientID;
            MemoryPersistence persistence = new MemoryPersistence();

            //blocking publish
            sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected:");
            return sampleClient;

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
            return sampleClient;
        }

    }



    public void send(int topicID, String fileSrc) throws MqttException, IOException {
        String topic = ds.getTopic(topicID);
        System.out.printf("Topic chosen:"+ds.getTopic(topicID));
        String content = getFile(fileSrc);
        System.out.printf("Content:"+ content);
        ds.setQOS(2);

        try {

            MqttClient Client;
            Client = connect(ds.getClientID()); // Sending the client ID to connect function
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(ds.getQOS());

            MqttDeliveryToken token = null;
            try {
                // publish message to broker
                Client.publish(topic, message);

                System.out.println("Message published");
                //Client.setCallback(this);
                // Wait until the message has been delivered to the broker

                Thread.sleep(100);
            } catch (Exception e) {
                e.printStackTrace();
            }


            Client.disconnect();
            System.out.println("Disconnected");
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }

    }

    public void subscribe(int topicID) throws MqttException {

        MqttClient sampleClient = connect(ds.getClientID());
        System.out.printf("\ntopic subscribed to is:"+ ds.getTopic(topicID));
        try{
            sampleClient.subscribe(ds.getTopic(topicID),ds.getQOS());

        }catch (Exception e) {
            e.printStackTrace();
        }



        try {
            System.in.read();
        } catch (IOException e) {
            //If we can't read we'll just exit
        }

        // Disconnect the client from the server
        sampleClient.disconnect();
        //System.out.printf("Disconnect!");


        //writing from bystream to zipfile

    }

    @Override
    public void connectionLost(Throwable t) {
        this.t = t;
        System.out.println("Connection lost!");
        // code to reconnect to the broker would go here if desired
    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        //store the recieved in a string
        //bstream = new byte[(int) fileSrc.length()];
        System.out.printf("Recieved something");
        byte[] recievedByteStream = new byte[mqttMessage.getPayload().length];

        //object data in this
        ds.setRecievedData(recievedByteStream.toString());
        //deserialize object

        try {
            FileOutputStream fos = new FileOutputStream("/Users/Anushavijay/Desktop/Amazon/unz.zip");

            fos.write(recievedByteStream);
            fos.close();
        }catch(FileNotFoundException ex){
            System.out.println("FileNotFoundException:" + ex);
        }catch(IOException ioe)  {
            System.out.println("IOException : " + ioe);
        }
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }


}













