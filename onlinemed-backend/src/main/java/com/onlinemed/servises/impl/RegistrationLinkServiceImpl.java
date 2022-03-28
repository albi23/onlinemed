package com.onlinemed.servises.impl;

import com.onlinemed.model.RegistrationLink;
import com.onlinemed.servises.api.RegistrationLinkService;
import org.springframework.stereotype.Service;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

@Service
public class RegistrationLinkServiceImpl extends BaseObjectServiceImpl<RegistrationLink> implements RegistrationLinkService {

    private String LOCAL_ADDRESS;

    public String getLocalAdres() {
        if (this.LOCAL_ADDRESS != null) return LOCAL_ADDRESS;
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            LOCAL_ADDRESS =  socket.getLocalAddress().getHostAddress();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        return LOCAL_ADDRESS;
    }

}
