package com.mycompany.datacom2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class Sender {
    String inputString;
    String outputString;
    String signal_string;
    String new_string;
    FileWriter fw;
    FileReader fr;
    String [] I4b = {"0000","0001","0010","0011","0100","0101","0110", "0111", "1000","1001","1010","1011","1100","1101","1110","1111"};  
    String [] I7b = {"0000000","0001101","0010111","0011010","0100011","0101110","0110100", "0111001", "1000110","1001011","1010001","1011100","1100101","1101000","1110010","11111111"};  
    
    Sender() throws FileNotFoundException, IOException{
        fr = new FileReader("labin.txt");
        fw = new FileWriter("labtemp.txt");
        BufferedReader br = new BufferedReader(fr);
        int i = 0;
        inputString = "";
        while(true){
            int x = br.read();
            i++;
            if(x == -1){
                applicationLayer(inputString);
                break;
            }
            char ch = (char) x;
            inputString = inputString + Character.toString(ch);
            if(i == 125)
            {
                applicationLayer(inputString);
                i = 0;
                inputString = "";
            }
        }
        fw.close();
    }
    
    
    void applicationLayer(String s) throws IOException{
        String mod_s = "A-H" + s;
        presentationLayer(mod_s);
    }
    
    void presentationLayer(String s) throws IOException{
        String mod_s = "P-H" + s;
        sessionLayer(mod_s);
    }
    
    void sessionLayer(String s) throws IOException{
        String mod_s = "S-H" + s;
        transportLayer(mod_s);
    }
    
    void transportLayer(String s) throws IOException{
        String mod_s = "T-H" + s;
        networkLayer(mod_s);
    }
    
    void networkLayer(String s) throws IOException{
        String mod_s = "N-H" + s;
        dataLinkLayer(mod_s);
    }
    
    void dataLinkLayer(String s) throws IOException{
        String mod_s = "D-H" + s + "D-T";
        physicalLayer(mod_s);
    }
    
    void physicalLayer(String s) throws IOException{
        String mod_s = "PH-H" + s;
        //System.out.println(mod_s.length());
        outputString = "";
        for(int i = 0 ; i < mod_s.length();i++){
            char c = mod_s.charAt(i);
            String sr = Integer.toBinaryString(c);
            int sr_len = sr.length();
            if(sr_len != 8)
            {
                for(int j = 0 ; j< 8 - sr_len ;j++){
                sr = "0" + sr;
                    //System.out.println("here" + sr);
                }
            }
            
            outputString = outputString + sr;
        }
        //System.out.println(outputString.length());
        //fw.write(outputString);
        syn(outputString);
    }
    void syn(String s) throws IOException {
       signal_string = "";
       int i,j;
       for(i=0;i < s.length(); i+=4){
           
           new_string=s.substring(i, i+4);
           
            
           for(j=0;j <16;j++){
               if (new_string.equals(I4b[j]) ) {
                 signal_string = signal_string + I7b[j]; 
                } 
            }
           Random random = new Random(); 
           int rand =random.nextInt(10);
           if(rand>70){
               char[] myNameChars = signal_string.toCharArray();
               if (myNameChars[i+2] == '0') {
                        myNameChars[i+2] = '1';
                        signal_string = String.valueOf(myNameChars);
                    } else {
                        myNameChars[i+2] = '0';
                        signal_string = String.valueOf(myNameChars);
                    }
              
           }
               
        }
          fw.write(signal_string); 
       }
 
    }
