package com.wangrui.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class MyFileReader {

 public static void main(String[] args) {
  BufferedReader br = null;
  BufferedWriter bw = null;
  try {
   AgeComparator ac = new AgeComparator();
   TreeSet<Person> ts = new TreeSet<Person>(ac);
   br = new BufferedReader(new FileReader("C:\\test.txt"));
   String temp = null;
   
   while ((temp = br.readLine()) != null) {
    StringTokenizer st = new StringTokenizer(temp);
    if(st.countTokens() == 4){
     Person person = new Person();
     person.setDate(st.nextToken());
     person.setName(st.nextToken());
     person.setAge(Integer.parseInt(st.nextToken()));
     person.setSex(st.nextToken());
     ts.add(person);
    }
    else{
     System.out.println("数据格式有问题");
     return;
    }
   }
   
   Iterator<Person> iter = ts.iterator();
   bw = new BufferedWriter(new FileWriter("C:\\b.txt"));
   for(;iter.hasNext();){
    Person person = iter.next();
    StringBuffer sb = new StringBuffer();
    sb.append(person.getDate()+" "+person.getName()+" "+person.getAge()+" "+person.getSex()+"\r\n");
    System.out.print(sb.toString());
    bw.write(sb.toString());
    bw.flush();
   }
  } catch (FileNotFoundException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (IOException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } finally{
   try {
    br.close();
    bw.close();
   } catch (IOException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
   }
  }
 }
 
}
