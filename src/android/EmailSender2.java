package com.dmonmad.emailsender2;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.Multipart;
import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.DataHandler;
import java.util.Properties;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;


import java.io.FileNotFoundException;
import java.io.File;


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
                        List<String> attachments;
                        if(args.length() > 8){                        
                            attachments = getListFromString(args.getString(8));
                        }else{
                            attachments = null;
                        }

                        sendEmail(ip, port, user, password, from, to, subject, body, attachments, callbackContext);
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

    private void sendEmail(String ip, String port, String user, String password, String from, String to, String subject, String body, List<String> attachments, CallbackContext callbackContext) {
        if (ip != null && ip.length() > 0) {
            if (port != null && port.length() > 0) {
                if (user != null && user.length() > 0) {
                    if (password != null && password.length() > 0) {
                        if (from != null && from.length() > 0) {                            
                            if (to != null && to.length() > 0) {

                                try{
                                    this.sendRealEmail(ip, port, user, password, from, to, subject, body, attachments);
                                    callbackContext.success("Enviado!");

                                }catch(FileNotFoundException e){                                    
                                    callbackContext.error(e.toString());
                                }catch(MessagingException e){                                    
                                    callbackContext.error(e.toString());
                                }catch(Exception e){                                    
                                    callbackContext.error(e.toString());
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

    public void sendRealEmail(String host, String port, String user, String password, String from, String to, String subject, String body, List<String> attachments)
    throws MessagingException, FileNotFoundException {
        try{
            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap(); 
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html"); 
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml"); 
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain"); 
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed"); 
            mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822"); 

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

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(to)
            );
            message.setSubject(subject);
            message.setText(body);

            Multipart multipart = new MimeMultipart();            
            BodyPart bodyMessage = new MimeBodyPart();
            bodyMessage.setContent(body, "text/html");
            multipart.addBodyPart(bodyMessage);

            if(attachments != null){
                
                for(int i = 0; i<attachments.size(); i++){

                    MimeBodyPart messageBodyPart = new MimeBodyPart();
                    String filename = attachments.get(i);
                    DataSource source = new FileDataSource(filename);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(getFileName(filename));
                    multipart.addBodyPart(messageBodyPart);
                }

                message.setContent(multipart);
            }          

            Transport.send(message);

        } catch (MessagingException e) {
            throw new MessagingException(e.toString());
        } catch (FileNotFoundException e){
            throw new FileNotFoundException(e.toString());
        }

    }

    private List<String> getListFromString(String s) {
        List<String> list = new ArrayList<String>();
        String parts[] = s.split(",");
        for(int i = 0; i<parts.length; i++){
            list.add(parts[i]);
        }
        return list;
    }

    private String getFileName(String s) throws FileNotFoundException {
        File f;
        f = new File(s);
        return f.getName();
    }
}
