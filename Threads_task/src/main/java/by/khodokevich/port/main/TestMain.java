package by.khodokevich.port.main;

import java.util.*;

public class TestMain {
    public static void main(String[] args) {
        Random random = new Random();

        Date startArray = new Date();
        List<Integer> arrayList = new ArrayList<>(10_000_000);
        for (int i = 0; i < 10_000; i++) {
            arrayList.add(random.nextInt(10_000));
        }
        Date endArray = new Date();

        System.out.println("Time for fill ArrayList by 10_000 is " + (endArray.getTime() - startArray.getTime()));

        Date startLinked = new Date();
        List<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < 10_000; i++) {
            linkedList.add(random.nextInt(10_000));
        }
        Date endLinked = new Date();

        System.out.println("Time for fill LinkedList by 10_000 is " + (endLinked.getTime() - startLinked.getTime()));



        Date startArray1 = new Date();
        List<Integer> arrayList1 = new ArrayList<>(10_000_000);
        for (int i = 0; i < 10_000_000; i++) {
            arrayList1.add(random.nextInt(10_000_000));
        }
        Date endArray1 = new Date();

        System.out.println("Time for fill ArrayList by 10_000_000 is " + (endArray1.getTime() - startArray1.getTime()));

        Date startLinked1 = new Date();
        List<Integer> linkedList1 = new LinkedList<>();
        for (int i = 0; i < 10_000_000; i++) {
            linkedList1.add(random.nextInt(10_000_000));
        }
        Date endLinked1 = new Date();

        System.out.println("Time for fill LinkedList by 10_000_000 is " + (endLinked1.getTime() - startLinked1.getTime()));



        Date startArray2 = new Date();
        arrayList.sort(Comparator.naturalOrder());
        Date endArray2 = new Date();

        System.out.println("Time for sort ArrayList with 10_000 element is " + (endArray2.getTime() - startArray2.getTime()));

        Date startLinked2 = new Date();
        linkedList.sort(Comparator.naturalOrder());
        Date endLinked2 = new Date();

        System.out.println("Time for sort LinkedList with 10_000 element is  " + (endLinked2.getTime() - startLinked2.getTime()));



        Date startArray3 = new Date();
        arrayList1.sort(Comparator.naturalOrder());
        Date endArray3 = new Date();

        System.out.println("Time for sort ArrayList with 10_000_000 element is " + (endArray3.getTime() - startArray3.getTime()));

        Date startLinked3 = new Date();
        linkedList1.sort(Comparator.naturalOrder());
        Date endLinked3 = new Date();

        System.out.println("Time for sort LinkedList with by 10_000_000 element is " + (endLinked3.getTime() - startLinked3.getTime()));

//        Date startArray4 = new Date();
//        for (int i = 0; i < 5_000; i++) {
//            arrayList.remove(random.nextInt(arrayList.size()));
//        }
//        Date endArray4 = new Date();
//
//        System.out.println("Time for remove 5_000 element ArrayList from 10_000 element is " + (endArray4.getTime() - startArray4.getTime()));
//
//        Date startLinked4 = new Date();
//        for (int i = 0; i < 5_000; i++) {
//            linkedList.remove(random.nextInt(linkedList.size()));
//        }
//        Date endLinked4 = new Date();
//
//        System.out.println("Time for remove 5_000 element LinkedList from 10_000 element is " + (endLinked4.getTime() - startLinked4.getTime()));
//
//
//
//        Date startArray5 = new Date();
//        for (int i = 0; i < 5_000; i++) {
//            arrayList1.remove(random.nextInt(arrayList1.size()));
//        }
//        Date endArray5 = new Date();
//
//        System.out.println("Time for remove 5_000_000 element ArrayList from 10_000_000 element is " + (endArray5.getTime() - startArray5.getTime()));
//
//        Date startLinked5 = new Date();
//        for (int i = 0; i < 5_000; i++) {
//            linkedList1.remove(random.nextInt(linkedList1.size()));
//        }
//        Date endLinked5 = new Date();
//
//        System.out.println("Time for remove 5_000_000 element LinkedList from 10_000_000 element is " + (endLinked5.getTime() - startLinked5.getTime()));
    }
}
