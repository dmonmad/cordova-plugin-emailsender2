package com.dj.emailsender2;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * This class echoes a string called from JavaScript.
 */
public class EmailSender2 extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("sendEmail")) {
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    try {
                        String ip = args.getString(0);
                        String port = args.getString(1);
                        String user = args.getString(2);
                        String password = args.getString(3);
                        String from = args.getString(4);
                        String to = args.getString(5);
                        String subject = args.getString(6);
                        String body = args.getString(7);
                        sendEmail(ip, port, user, password, from, to, subject, body, callbackContext);
                    } catch (Exception e) {
                        callbackContext.error("VENGA YA TIO "+e.toString());
                    }
                }
            });            
        }else{
            return false;
        }
        return true;
    }

    private void sendEmail(String ip, String port, String user, String password, String from, String to, String subject, String body, CallbackContext callbackContext) {
        if (ip != null && ip.length() > 0) {
            if (port != null && port.length() > 0) {
                if (user != null && user.length() > 0) {
                    if (password != null && password.length() > 0) {
                        if (from != null && from.length() > 0) {                            
                            if (to != null && to.length() > 0) {

                                try{
                                    this.sendRealEmail(ip, port, user, password, from, to, subject, body);
                                    callbackContext.success("Enviado!");

                                }catch(Exception e){                                    
                                    callbackContext.error("JODER2 "+e.toString());
                                }
                            } else {
                                callbackContext.error("Destiny address is invalid.");
                            }
                        } else {
                            callbackContext.error("Sender address is invalid.");
                        }
                    } else {
                        callbackContext.error("Password is invalid.");
                    }
                } else {
                    callbackContext.error("User is invalid.");
                }
            } else {
                callbackContext.error("Port is invalid.");
            }
        } else {
            callbackContext.error("IP is invalid.");
        }
    }

    public void sendRealEmail(String host, String port, String user, String password, String from, String to, String subject, String body)
    throws MessagingException {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "false"); //TLS
                
        Session session = Session.getInstance(prop,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(to)
            );
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new MessagingException("NO ME JDOAS "+e.toString());
        }

    }
}
