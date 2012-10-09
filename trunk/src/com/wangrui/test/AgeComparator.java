package com.wangrui.test;

import java.util.Comparator;


public class AgeComparator implements Comparator<Person>{

 public int compare(Person object1, Person object2) {
  // TODO Auto-generated method stub
  if(object1.getAge() > object2.getAge())
   return 1;
  else if (object1.getAge() < object2.getAge()){
   return -1;
  }
  else
   return 0;
 }
}


