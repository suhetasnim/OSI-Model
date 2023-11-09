package com.mycompany.datacom2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Receiver {

    String inputString;
    String outputString;
    String signal_string;
    String new_string;
    
    FileWriter fw;
    FileReader fr;
    String[] I4b = {"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};
    String[] I7b = {"0000000", "0001101", "0010111", "0011010", "0100011", "0101110", "0110100", "0111001", "1000110", "1001011", "1010001", "1011100", "1100101", "1101000", "1110010", "11111111"};
    //int [] syn = new int[ 8 ];

    Receiver() throws FileNotFoundException, IOException {
        fr = new FileReader("labtemp.txt");
        fw = new FileWriter("labout.txt");
        BufferedReader br = new BufferedReader(fr);
        int i = 0;
        inputString = "";
        while (true) {
            int x = br.read();
            i++;
            if (x == -1) {
                syn(inputString);
                break;
            }
            char ch = (char) x;
            inputString = inputString + Character.toString(ch);

            if (i == 2100) {

                syn(inputString);
                i = 0;
                inputString = "";
            }
        }
        fw.close();

    }

    void syn(String s) throws IOException {
        signal_string = "";
        int i, j;

        for (i = 0; i < s.length(); i += 7) {
            
            new_string = s.substring(i, i + 7);
            int sa = new_string.charAt(i+1) + new_string.charAt(i+2) + new_string.charAt(i+3) + new_string.charAt(i+6);
            int s0 = sa % 2;
            int sb = new_string.charAt(i) + new_string.charAt(i+1) + new_string.charAt(i+2) + new_string.charAt(i+5);
            int s1 = sb % 2;
            int sc = new_string.charAt(i+6) + new_string.charAt(i+4) + new_string.charAt(i+3) + new_string.charAt(i+4);
            int s2 = sc % 2;

            if (s0 == 0 && s1 == 0 && s2 == 0) {
                for (j = 0; j < 16; j++) {
                    if (new_string.equals(I7b[j])) {
                        signal_string = signal_string + I4b[j];
                    }
                }

            } else {
                if (s0 == 1 && s1 == 0 && s2 == 0) {
                    char[] myNameChars = new_string.toCharArray();

                    if (myNameChars[i+6] == '0') {
                        myNameChars[i+6] = '1';
                        new_string = String.valueOf(myNameChars);
                    } else {
                        myNameChars[i+6] = '0';
                        new_string = String.valueOf(myNameChars);
                    }
                } else if (s0 == 0 && s1 == 1 && s2 == 0) {
                    char[] myNameChars = new_string.toCharArray();

                    if (myNameChars[i+5] == '0') {
                        myNameChars[i+5] = '1';
                        new_string = String.valueOf(myNameChars);
                    } else {
                        myNameChars[i+5] = '0';
                        new_string = String.valueOf(myNameChars);
                    }
                } else if (s0 == 1 && s1 == 1 && s2 == 0) {
                    char[] myNameChars = new_string.toCharArray();

                    if (myNameChars[i+1] == '0') {
                        myNameChars[i+1] = '1';
                        new_string = String.valueOf(myNameChars);
                    } else {
                        myNameChars[i+1] = '0';
                        new_string = String.valueOf(myNameChars);
                    }
                } else if (s0 == 0 && s1 == 0 && s2 == 1) {
                    char[] myNameChars = new_string.toCharArray();

                    if (myNameChars[i+4] == '0') {
                        myNameChars[i+4] = '1';
                        new_string = String.valueOf(myNameChars);
                    } else {
                        myNameChars[i+4] = '0';
                        new_string = String.valueOf(myNameChars);
                    }
                } else if (s0 == 1 && s1 == 0 && s2 == 1) {
                    char[] myNameChars = new_string.toCharArray();

                    if (myNameChars[i+3] == '0') {
                        myNameChars[i+3] = '1';
                        new_string = String.valueOf(myNameChars);
                    } else {
                        myNameChars[i+3] = '0';
                        new_string = String.valueOf(myNameChars);
                    }
                } else if (s0 == 0 && s1 == 1 && s2 == 1) {
                    char[] myNameChars = new_string.toCharArray();

                    if (myNameChars[i] == '0') {
                        myNameChars[i] = '1';
                        new_string = String.valueOf(myNameChars);
                    } else {
                        myNameChars[i] = '0';
                        new_string = String.valueOf(myNameChars);
                    }
                } else if (s0 == 1 && s1 == 1 && s2 == 1) {
                    char[] myNameChars = new_string.toCharArray();

                    if (myNameChars[i+4] == '0') {
                        myNameChars[i+4] = '1';
                        new_string = String.valueOf(myNameChars);
                    } else {
                        myNameChars[i+4] = '0';
                        new_string = String.valueOf(myNameChars);
                    }
                }
                for (j = 0; j < 16; j++) {
                    if (new_string.equals(I7b[j])) {
                        signal_string = signal_string + I4b[j];
                    }
                }

            }
            

        }
        physicalLayer(signal_string);

    }

    void physicalLayer(String s) throws IOException {
        outputString = "";
        int i;
        for (i = 0; i < s.length(); i += 8) {
            String temp = s.substring(i, i + 8);
            int x = Integer.parseInt(temp, 2);
            char ch = (char) x;
            outputString = outputString + Character.toString(ch);
        }
        outputString = outputString.substring(4);
        datalinkLayer(outputString);
    }

    void datalinkLayer(String s) throws IOException {
        outputString = s.substring(3, s.length() - 3);
        networkLayer(outputString);

    }

    void networkLayer(String s) throws IOException {
        System.out.println(s);
        outputString = s.substring(3);
        System.out.println(outputString);
        transportLayer(outputString);
    }

    void transportLayer(String s) throws IOException {
        outputString = s.substring(3);
        sessionLayer(outputString);
    }

    void sessionLayer(String s) throws IOException {
        outputString = s.substring(3);
        presentationLayer(outputString);
    }

    void presentationLayer(String s) throws IOException {
        outputString = s.substring(3);
        applicationLayer(outputString);
    }

    void applicationLayer(String s) throws IOException {
        outputString = s.substring(3);
        fw.write(outputString);
    }
}
