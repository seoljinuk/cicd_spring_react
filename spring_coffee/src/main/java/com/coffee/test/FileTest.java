package com.coffee.test;

import java.io.File;

public class FileTest {
    public static void main(String[] args) {
        final String imageFolder = "c:\\shop\\images";

        File folder = new File(imageFolder);

        if(folder.exists()){
            if(folder.isDirectory()){
                System.out.println("폴더");

                 File[] imageList = folder.listFiles();

                 for(File one : imageList){
                     if(one.isFile()){
                         if(one.getName().endsWith(".jpg")){
                             System.out.println(one.getName());

                             //int end_index = one.getName().indexOf(".");
                             int end_index = one.getName().lastIndexOf(".");
                             String filename = one.getName().substring(0, end_index) ;
                             System.out.println(filename);

                             String extension = one.getName().substring(end_index + 1) ;
                             System.out.println(extension);
                         }
                     }
                 }
            }else{
                System.out.println("파일");
            }
        }else{
            System.out.println("존재하지 않는 항목입니다.");
        }
    }
}













