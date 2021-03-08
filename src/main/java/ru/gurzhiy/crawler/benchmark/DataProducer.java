package ru.gurzhiy.crawler.benchmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gurzhiy.crawler.Utils;
import ru.gurzhiy.crawler.concurrent.Pair;
import ru.gurzhiy.crawler.concurrent.Worker;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class DataProducer implements Runnable{

    private final static Logger log = LoggerFactory.getLogger(DataProducer.class);
    private AtomicBoolean eofFlag;
    //2^13 = 8192, стандартный размер буффера дает хорошие результаты для большинства операций
    private final int bufferSize = (int) Math.pow(2, 13);

   // private final String filename = "D:\\fireField\\result.txt";
    private String filename = "D:\\fireField\\resultShort.txt";



    //дерево критериев релевантности и сортировка "из коробки" по критерию
    private final TreeMap<Integer, String> criterias = new TreeMap<>();


    //список хранилище результатов для потоков из экзекьютора
    private static final CopyOnWriteArrayList<Pair> pairs = new CopyOnWriteArrayList<>();
    // private Queue<String> queue = new ConcurrentLinkedQueue();
    private Queue<Pair> queue;
//    private ExecutorService executor = Executors.newFixedThreadPool(3);




    public DataProducer() {
    }

    public DataProducer(Queue<Pair> queue, AtomicBoolean eofFlag) {
        this.queue = queue;
        this.eofFlag = eofFlag;
    }


    public DataProducer(String filename, Queue<Pair> queue, AtomicBoolean eofFlag) {
        this.eofFlag = eofFlag;
        this.filename = filename;
        this.queue = queue;
    }

    public void readFile(String filename, String request) {
        request = request.toLowerCase(Locale.ROOT);
        long t1 = System.currentTimeMillis();
        //Charset utf-8 согласно заданию
        try (
                FileInputStream fis = new FileInputStream(filename);
                InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr, bufferSize)) {


            log.debug("Ищу запрос в файле. Это может занять некоторое время.");
            //проверяем что нам подходит
            for (String line = null; (line = br.readLine()) != null; ) {

                //todo: лайны в очередь, очередь разбирать потоками
                //для достижения регистронезависимости
                line = line.toLowerCase(Locale.ROOT);

                //получаем критерий
                int relevantCriteria = getRelevantCriteriaForRequestAndLine(request, line);

                //это условие из задания
                if (relevantCriteria > 2) {

                    criterias.put(relevantCriteria, line);
                }

            }

            long t2 = System.currentTimeMillis();
            log.debug("Поиск завершен за {}", (t2 - t1));

        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }









    public void concurrentReadFile(String filename, String request) {
        request = request.toLowerCase(Locale.ROOT);
        long t1 = System.currentTimeMillis();
        //Charset utf-8 согласно заданию
        try (
                FileInputStream fis = new FileInputStream(filename);
                InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr, bufferSize)) {


            log.debug("Ищу запрос в файле. Это может занять некоторое время.");
            int a = 0;
            //проверяем что нам подходит
            for (String line = null; (line = br.readLine()) != null; ) {

                queue.offer(new Pair(0, line));
                log.info("в очереди лежит элемент {}", queue.peek());
                a++;
                if (a == 20){
                    log.info("размер очереди {}", pairs.size());
                    a=0;
                    System.out.println("++++++++++++++++++++++");
                    queue.forEach(x-> System.out.println(x));
                    System.out.println("++++++++++++++++++++++");
                    break;
                }


//                Future<Pair> future1 = executor.submit(new Worker(request, queue.poll()));
//                Future<Pair> future2 = executor.submit(new Worker(request, queue.poll()));
//                Future<Pair> future3 = executor.submit(new Worker(request, queue.poll()));

//                if (future1.isDone()) {
//                    Pair p1 = future1.get();
//                    if (p1.getRelevanceCriteria() > 2)
//                        pairs.add(p1);
//                }
//                if (future2.isDone()) {
//                    Pair p2 = future2.get();
//                    if (p2.getRelevanceCriteria() > 2)
//                        pairs.add(p2);
//                }
//                if (future3.isDone()) {
//                    Pair p3 = future3.get();
//                    if (p3.getRelevanceCriteria() > 2)
//                        pairs.add(p3);
//                }


                //todo: лайны в очередь, очередь разбирать потоками
                //для достижения регистронезависимости
                line = line.toLowerCase(Locale.ROOT);

                //получаем критерий
                int relevantCriteria = Utils.getRelevantCriteriaForRequestAndLine(request, line);

                //это условие из задания
                if (relevantCriteria > 2) {

                    criterias.put(relevantCriteria, line);
                }

            }

            long t2 = System.currentTimeMillis();
            log.debug("Поиск завершен за {}", (t2 - t1));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Данный метод предназначен для обработки одной строки. Он возвращает значение критерия релевантности. Т.е.
     * показывает насколько строка релевантна запросу.
     *
     * @param request поисковый запрос (без разделительных символов, отформатированный в нижний регистр)
     * @param line    строка без разделительных символов, отформатированная в нижний регистр
     * @return relevantCriteria - значение критерия релевантности строки к запросу в соответствии с правилами:
     * <p>
     * Критерии релевантности:
     * совпадение одного слова - 2 балла
     * совпадение N слов - 2*N баллов
     * совпадение следования друг за другом двух слов - плюс балл
     * N слов идут друг за другом как в запросе пользователя, плюс N-1 балл
     * Например если в словаре есть одно выражение "съешь еще этих мягких французских булок":
     * поисковый запрос "съешь еще этих булок" - 10 баллов (совпадение одного слова 4*2 + совпадение следования - 2*1)
     * поисковый запрос "этих булок ты не съешь" - 6 баллов (совпадение одного слова 3*2)
     */
    public static int getRelevantCriteriaForRequestAndLine(String request, String line) {
        return Utils.getRelevantCriteriaForRequestAndLine(request, line);
    }


    public TreeMap<Integer, String> getCriterias() {
        return criterias;
    }

    public CopyOnWriteArrayList<Pair> getPairs() {
        return pairs;
    }

    public static void main(String[] args) {
        DataProducer reader = new DataProducer();

        //  reader.readFile(reader.filename, "Конь не валялся");
        //  reader.getCriterias().forEach((k, v) -> log.debug("KR = {}; \"{}\"", k, v));

        System.out.println("------------------------------------------");

        reader.concurrentReadFile(reader.filename, "конь не валялся");
        System.out.println("pairs size = " + reader.getPairs().size());
        reader.getPairs().stream().sorted(Pair::compareTo).forEach(x -> System.out.println(x));

    }

    @Override
    public void run() {
        long t1 = System.currentTimeMillis();
        log.info("Запущен поток DataProducer");
        try (
                FileInputStream fis = new FileInputStream(filename);
                InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr, bufferSize)) {

//            for (int i = 0; i < 1000; i++) {
//                String line = br.readLine();
//                queue.offer(new Pair(0, line));
//                System.out.println(queue.size());
//            }


//
            for (String line= null; (line = br.readLine()) != null;){
              //  String line = br.readLine();
                queue.offer(new Pair(0, line));
//                System.out.println(queue.size());
//                queue.offer(new Pair(0, line));
            }
            long t2 = System.currentTimeMillis();
            log.info("Время чтения файла {} миллисекунд", (t2-t1));
            eofFlag.set(true);
            log.info("Set eof to {}", eofFlag.get());
            log.info("queue size {}",queue.size());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
