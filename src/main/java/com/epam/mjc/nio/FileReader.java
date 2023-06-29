package com.epam.mjc.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;



public class FileReader {
    public String readFile(File file){
        StringBuilder content = new StringBuilder();
        try (RandomAccessFile aFile = new RandomAccessFile(file, "r");
             FileChannel inChannel = aFile.getChannel();) {

            //Buffer size is 1024
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (inChannel.read(buffer) > 0) {
                buffer.flip();
                for (int i = 0; i < buffer.limit(); i++) {
                    content.append((char) buffer.get());
                }
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }


    private void populateField(Profile profile, String field, String value){
        try {
            switch (field) {
                case "Name":
                    profile.setName(value);
                    break;
                case "Age":
                    profile.setAge(Integer.valueOf(value));
                    break;
                case "Email":
                    profile.setEmail(value);
                    break;
                case "Phone":
                    profile.setPhone(Long.valueOf(value));
                    break;
                default:
                    throw new IllegalArgumentException("Field " + field + "is not allowed.");
            }
        } catch(IllegalArgumentException iae){
            iae.printStackTrace();
        }
    }
    private String[] fields = {"Name", "Age", "Email", "Phone"};
    private Profile parseContent(String content){
        String[] lines = content.split("\r?\n");

        Profile profile = new Profile();
        for(String str : lines){
            for(String field : fields){
                if(str.contains(field)){
                    populateField(profile, field, str.replace(field + ": ", ""));
                }
            }
        }
        return profile;
    }
    public Profile getDataFromFile(File file) {
        return parseContent(readFile(file));
    }


}
