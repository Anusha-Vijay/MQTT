package org.mqtt.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anushavijay on 10/2/16.
 */
public class datastore implements java.io.Serializable{

    String fileName;
    String path;
    String ext;
    String fileData;
    String masterIp;
    String nodeIp;
    String brokerIP;
    String ClientID;
    String recievedData;
    int QOS;
    List<String> topic=new ArrayList<String>();
    List<String> nearNodes=new ArrayList<String>();

    public String getRecievedData() {
        return recievedData;
    }

    public void setRecievedData(String recievedData) {
        this.recievedData = recievedData;
    }

    public int getQOS() {
        return QOS;
    }

    public void setQOS(int QOS) {
        this.QOS = QOS;
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public String getBrokerIP() {
        return brokerIP;
    }

    public void setBrokerIP(String brokerIP) {
        this.brokerIP = brokerIP;
    }



    public String getNearNodes(int nodeID) {
        return nearNodes.get(nodeID);
    }

    public void setNearNodes(String newNodeIp) {
        this.nearNodes.add(newNodeIp);
    }



    public String getTopic(int topicID) {
        return topic.get(topicID);
    }

    public void setTopic(ArrayList<String> topic) {
        this.topic = topic;
    }



    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }

    public String getMasterIp() {
        return masterIp;
    }

    public void setMasterIp(String masterIp) {
        this.masterIp = masterIp;
    }




    public datastore(String fileName, String ext) {
        this.fileName = fileName;
        this.ext = ext;
        topic.add("MasterRegister");
        topic.add("Docker");
        topic.add("Resource Request");
        topic.add("UpdateMaster");
        topic.add("Block/Unblock");
    }



    public void addTopic(String topicName)
    {

        topic.add(topicName);

    }


    public String getFileData() {
        return fileData;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }



    public void setPath(String path) {
        this.path = path;
    }



    public void setExt(String ext) {
        this.ext = ext;
    }



    public void setFileData(String fileData) {
        this.fileData = fileData;
    }

    public String getFileName() {
        return fileName;
    }



    public String getPath() {
        return path;
    }

    protected String getExt(){
        return ext;
    }





}
