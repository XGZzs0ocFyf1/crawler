package ru.gurzhiy.crawler.benchmark;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueCheck {

    public static void main(String[] args) {
        Queue<String>q = new ConcurrentLinkedQueue();
        q.offer("1");
        q.offer("2");
        q.peek();
        q.add("3");
        System.out.println("size = :" + q.size());
        q.add("4");
        System.out.println("size = :" + q.size());

        q.forEach(x-> System.out.println(x));
    }
}
