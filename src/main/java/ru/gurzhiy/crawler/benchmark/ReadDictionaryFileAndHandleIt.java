package ru.gurzhiy.crawler.benchmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static ru.gurzhiy.crawler.benchmark.Informer.informer;

public class ReadDictionaryFileAndHandleIt {

    private final static Logger log = LoggerFactory.getLogger(ReadDictionaryFileAndHandleIt.class);

    private List<String> arrayList = new ArrayList<>();
    private String filename = "D:\\fireField\\result.txt";

    public void readFile1(int numberOflines) {
        long t1 = System.currentTimeMillis();

        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"))) {
            log.debug("Ищу запрос в файле. Это может занять некоторое время.");
            for (String line = null; (line = br.readLine()) != null; ) {
                arrayList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long t2 = System.currentTimeMillis();
        System.out.println("Метод r1");
        informer();
        System.out.println("Время чтения файла : " + (t2 - t1) + " ms");
        System.out.println("размер списка считанных строк: " + arrayList.size());
        System.out.println("Пример содержимого:");
        arrayList.stream().skip(20).limit(3).forEach(w -> System.out.println(w));
    }


    public static void main(String[] args) {
        ReadDictionaryFileAndHandleIt reader = new ReadDictionaryFileAndHandleIt();
        reader.readFile1(10);
        reader.arrayList.forEach(x -> System.out.println(x));


    }
}
