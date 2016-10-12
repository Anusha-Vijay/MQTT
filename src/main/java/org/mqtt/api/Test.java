/**
 * Created by Anushavijay on 10/2/16.

 */
package org.mqtt.api;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;

public class Test {
    final public static mqttAPI API =new mqttAPI();

    public static void main(String[] args) throws IOException, MqttException {
        //API.getFile("/Users/Anushavijay/Desktop/Amazon/mesosraspberry.zip");
        API.ds.setClientID("Node 1");
        API.ds.setNodeIp("192.168.0.33");
        API.ds.setBrokerIP("tcp://192.168.0.25:1883");
        API.send(1,"/Users/Anushavijay/Desktop/Amazon/mesosraspberry.zip");
        API.subscribe(1);
    }
}