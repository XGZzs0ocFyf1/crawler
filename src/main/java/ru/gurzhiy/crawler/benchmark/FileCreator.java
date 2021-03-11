package ru.gurzhiy.crawler.benchmark;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static ru.gurzhiy.crawler.benchmark.Informer.informer;

/**
 * Создает большой файл заполненный рандомными словами.
 */
public class FileCreator {

    private int numberOfStringInOutputFile = 10_000;
    private int numberOfWordsInLine = 5;
    private String filename = "src/main/resources/russian.txt";
    private List<String> inputList1 = new ArrayList<>();
    private List<String> inputList2 = new ArrayList<>();
    private List<String> inputList3 = new ArrayList<>();
    private List<String> inputList4 = new ArrayList<>();
    private String outputFilename = "D:\\fireField\\resultV3.txt";
    private List<String> outputData;


    public void readFile1() {

        long t1 = System.currentTimeMillis();

        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "Windows-1251"))) {
            for (String line = null; (line = br.readLine()) != null; ) {
                inputList1.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long t2 = System.currentTimeMillis();
        System.out.println("Метод r1");
        informer();
        System.out.println("Время чтения файла : " + (t2 - t1) + " ms");
        System.out.println("размер списка считанных строк: " + inputList1.size());
        System.out.println("Пример содержимого:");
        inputList1.stream().skip(20).limit(3).forEach(w -> System.out.println(w));
    }


    public void readFile2() {


        long t1 = System.currentTimeMillis();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(filename), Charset.forName("Windows-1251"))) {

            for (String line = null; (line = br.readLine()) != null; ) {
                inputList2.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long t2 = System.currentTimeMillis();
        System.out.println("Метод r2");
        informer();
        System.out.println("Время чтения файла : " + (t2 - t1) + " ms");
        System.out.println("размер списка считанных строк: " + inputList2.size());
        System.out.println("Пример содержимого:");
        inputList2.stream().skip(20).limit(3).forEach(w -> System.out.println(w));
    }


    public void readFile3() {

        long t1 = System.currentTimeMillis();
        int bufferSize = (int) Math.pow(2, 11);
        System.out.println("Buffer size : " + bufferSize);

        try (
                FileInputStream fis = new FileInputStream(filename);
                InputStreamReader isr = new InputStreamReader(fis, "Windows-1251");
                BufferedReader br = new BufferedReader(isr, bufferSize)) {
            for (String line = null; (line = br.readLine()) != null; ) {
                inputList3.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        long t2 = System.currentTimeMillis();
        System.out.println("Метод r3");
        informer();
        System.out.println("Время чтения файла : " + (t2 - t1) + " ms");
        System.out.println("размер списка считанных строк: " + inputList3.size());
        System.out.println("Пример содержимого:");
        inputList3.stream().skip(20).limit(3).forEach(w -> System.out.println(w));
    }


    public void readFile4() {

        long t1 = System.currentTimeMillis();
        int bufferSize = (int) Math.pow(2, 11);
        System.out.println("Buffer size : " + bufferSize);

        try {
            inputList4 = Files.readAllLines(Paths.get(filename), Charset.forName("Windows-1251"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        long t2 = System.currentTimeMillis();
        System.out.println("Метод r4");
        informer();
        System.out.println("Время чтения файла : " + (t2 - t1) + " ms");
        System.out.println("размер списка считанных строк: " + inputList4.size());
        System.out.println("Пример содержимого:");
        inputList4.stream().skip(20).limit(3).forEach(w -> System.out.println(w));
    }


    //читаем файл, содержащий список слов русского языка


    //формируем строку произвольной длины
    public String createOneLine(){
        int numberOfWords = ThreadLocalRandom.current().nextInt(3,6);
        StringBuilder sb = new StringBuilder(numberOfWords);
        for (int i = 0; i < numberOfWords; i++) {
            int maxRandomNumber = inputList1.size()-1;
            sb.append(inputList1.get(ThreadLocalRandom.current().nextInt(0, maxRandomNumber)));
            sb.append(" ");
        }
        sb.append("\n");
        return  sb.toString();
    }


    public void generateOutput(int numberOfStrings){
        outputData = new ArrayList<>();
        for (int i = 0; i < numberOfStrings; i++) {
            String line  = createOneLine();
            outputData.add(line);
        }
    }


    public void writeOutputToFile(){

        try(  BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFilename, true))){

//            for (int i = 0; i < 250_000; i++) {
//                bufferedWriter.write(createOneLine());
//           }
//          bufferedWriter.write("\n\r конь не валялся \n\r");
//                    "\n");
//            bufferedWriter.write("\n\r не валялся а лежал \n\r");

            for (int i = 0; i < 250_000; i++) {
             bufferedWriter.write(createOneLine());
    }
//            bufferedWriter.write("\n\r кот валялся конь бегал \n\r");
//            bufferedWriter.write("\n\r кот не валялся конь бегал \n\r");
            bufferedWriter.write("\n\r дворецкий хоть не конь целый день бегал а не валялся \n\r");
            for (int i = 0; i < 250_000; i++) {
                bufferedWriter.write(createOneLine());
            }
//            for (int i = 0; i < 20_000_000; i++) {
//                bufferedWriter.write(createOneLine());
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void doStuff(){
        long t1= System.currentTimeMillis();

        writeOutputToFile();
        long t2= System.currentTimeMillis();
        System.out.println("Время создания готового файла "+ (t2-t1) + "ms");
    }

    //сохраняем строку в список
    //сохраняем список в файл



    public static void main(String[] args) throws InterruptedException {
        FileCreator fc = new FileCreator();
        fc.readFile1(); //450 ms

        //  fc.readFile2(); //700 ms
//         fc.readFile3(); //610 ms
       // fc.readFile4(); //610 ms
        fc.doStuff();
    }

}
