package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Bot extends TelegramLongPollingBot {


    String fileName = "Student.txt";
    Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();

   Property property = new Property();

    public Bot() throws IOException {
    }


    @Override
    public String getBotUsername() {
        return "4K_I4U_Support";
    }

    @Override
    public String getBotToken() {
        return property.getValue("token");
    }
    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();
        var id = user.getId();
        var idMes = msg.getMessageId();
        var welcomeMes = "Welcome to 4K I4U Support Bot. How can we help you. choose number. /እንኳን ወደ I4U Support መጡ። አንደት አንርዳዎት ።\n" +
                "እባኮ ቁጥር ይምረጡ ።\n\n" +
                "1.Need help/እርዳታ ፈልጋለሁ        2.Need counseling/ምክር ፈልጋለሁ"   ;
        var reach = "How do you want us to reach to you. choose number./እንደት አንዲናገኞት ይፈሊጋሉ? \n\n"+
                "3.Publicly /በግልፅ        4.Anonymous/በምስጥር ";
        var matter = "Write your matter briefly. /ጉዳዮን ባጩሩ ይፃፉ። " ;
        var newMatter = msg.getFrom().getFirstName() + ". " + matter ;
        var finalGreeting = "Thank you. You will be contacted soon./እናመሰጊናለን! በቂርቡ አናገኞታለን።" ;
        var phone = "Please send us your phone number. /ሲልኮን ይላኩሊን። " ;
        var warn = "Please send working Phone number ." ;
        var helpText = "This bot lets you connect with I4U team. Any time you lost the path you can start new conversation by typing /start. /" +
                "ይህ ከI4U ትም ጋር ያገናኞታል ። ኣዲስ ንጊጊር /start ቢለዉ መጀመር ይችላሉ። ";
        String PHONE_NUMBER_REGEX = "^\\+(?:[0-9] ?){6,14}[0-9]$";



        //Else proceed normally

        //sendText(id , String.valueOf(user));
        //copyMessage(id, idMes);
        System.out.println(msg.getText());
        if(msg.isCommand()){
            if(msg.getText().equals("/start")){
                sendText(id ,welcomeMes);
                map.put(user.getUserName(), new ArrayList<String>());
                System.out.println(map.get(user.getUserName()));

            }
            if(msg.getText().equals("/help")){
                sendText(id, helpText);
            }
            return;
        }

        if ((msg.getText().equals("1") || msg.getText().equals("2")) && map.containsKey(user.getUserName())) {
            sendText(id ,reach);
            ArrayList<String> list = (ArrayList<String>)map.get(user.getUserName());
            if(msg.getText().equals("1"))
                list.add("help");
            else
                list.add("counseling");
            System.out.println(map.get(user.getUserName()));
            return ;
        }

        if (msg.getText().equals("4") && map.containsKey(user.getUserName())) {
            sendText(id, matter);
            ArrayList<String> list = (ArrayList<String>)map.get(user.getUserName());
            list.add("anonymous");
            list.add("no_phone");
            System.out.println("At anonymous looping " + map.get(user.getUserName()));
            //list.add(msg.getText());
            return;
        }

        if (msg.getText().equals("3") && map.containsKey(user.getUserName())) {
            sendText(id, phone);
            ArrayList<String> list = (ArrayList<String>)map.get(user.getUserName());
            System.out.println("At help looping " + map.get(user.getUserName()));
            list.add("publicly");

            // if (isValidPhoneNumber(phone, msg.getText())) {
            // sendText(id, newMatter);
            //ArrayList<String> list = (ArrayList<String>)map.get(user.getUserName());
            //list.add(msg.getText());

            //  db.addPhone(user.getUserName(), msg.getText());
            //} else {
            //    sendText(id, warn);
            // }
            return;
        }

        if(map.containsKey(user.getUserName()) && mapSize(user.getUserName()) == 2){
            sendText(id, newMatter);
            ArrayList<String> list = (ArrayList<String>)map.get(user.getUserName());
            list.add(msg.getText());
            System.out.println(map.get(user.getUserName()));
        }

        if(map.containsKey(user.getUserName()) && msg.getText().length() >= 16/*mapSize(user.getUserName()) == 3*/){
            ArrayList<String> list = (ArrayList<String>)map.get(user.getUserName());
            list.add(msg.getText());
            System.out.println(map.get(user.getUserName()));
        }
        if (map.containsKey(user.getUserName()) && mapSize(user.getUserName()) == 4){

            try {
                saveToFile(user.getUserName());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            sendText(id, finalGreeting);
            System.out.println(map.get(user.getUserName()));

        }

    }

    private void saveToFile(String userName) throws IOException {
        ArrayList<String> list = (ArrayList<String>) map.get(userName);
        FileWriter writer = new FileWriter(fileName, true);
        String builder = userName +
                "    " +
                list.get(0) +
                "    " +
                list.get(2) +
                "    " +
                list.get(3) +
                "    " +
                list.get(1) +
                "\n";
        writer.append(builder);
        writer.close();
        System.out.println("appended" + builder);
    }
    public void sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }
    public int mapSize(String userName){
        if(map.containsKey(userName)){
            ArrayList<String> list = (ArrayList<String>)map.get(userName);
            return list.size();
        }
        return -1;
    }
}
