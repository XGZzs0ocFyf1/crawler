package ru.gurzhiy.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gurzhiy.crawler.benchmark.DataProducer;

public class Utils {

    private final static Logger log = LoggerFactory.getLogger(DataProducer.class);

    public static int getRelevantCriteriaForRequestAndLine(String request, String line) {

        long t1 = System.currentTimeMillis();
        int relevantCriteria = 0;

        String[] desiredArr = request.split(" ");
        String[] lineArr = line.split(" ");

        for (int desiredIdx = 0; desiredIdx < desiredArr.length; desiredIdx++) {

            if (line.contains(desiredArr[desiredIdx])) {


              /*  log.debug("Слово {} содержится в строке {}; КР = {}", desiredArr[desiredIdx].toUpperCase(Locale
                        .ROOT), lineArr, relevantCriteria);
*/

                //он нужен для вычисления порядка слов в строке. хранит прошлый индекс слова в запросе и в строке
                int previousHitWordPosition = -1;

                //Здесь пробегаем еще раз оба массива, сравниваем, вычисляем порядок слов на основе lastHitIndex
                for (int i = desiredIdx; i < desiredArr.length; i++) {
                    for (int j = 0; j < lineArr.length; j++) {


                        if (desiredArr[i].equals(lineArr[j])) {
                            relevantCriteria += 2;
                        /*    log.debug("Слово {} содержится в строке {}; lastIndex = {}, КР = {}",
                                    desiredArr[i].toUpperCase(Locale.ROOT),
                                    lineArr, previousHitWordPosition, relevantCriteria);*/

//                            первое вхождение первого поискового слова, обновляем индекс
                            if (previousHitWordPosition < 0) {
                                previousHitWordPosition = j;
                            }


                            //fixme: возможно цикл ниже стоит переписать
                            if (previousHitWordPosition < j && previousHitWordPosition + 1 == j) {
                         /*       log.info("Previous position = {}", previousHitWordPosition);
                                log.info("Current position = {}", j);*/

                              /*  log.debug("Слово {} из запроса идет ПОСЛЕ слова {}; lastIndex = {}, КР = {}",
                                        desiredArr[i].toUpperCase(Locale.ROOT),
                                        desiredArr[previousHitWordPosition].toUpperCase(Locale.ROOT),
                                        previousHitWordPosition, relevantCriteria);*/
                                relevantCriteria += 1;
//                                log.debug("За правильный порядок слов начисляется 2 балла; КР = {}", relevantCriteria);

                                previousHitWordPosition = j;
                            } /*   else {
                             log.debug("Слово {} из запроса идет ДО слова {}; КР = {}",
                                        desiredArr[i].toUpperCase(Locale.ROOT),
                                        desiredArr[previousHitWordPosition].toUpperCase(Locale.ROOT), relevantCriteria);
                           }*/


                        }
                    }

                    //если перебрали все слова из поискового запрос то завершаем работу с этим запросом и этой строкой
                    if (i == desiredArr.length - 1) {
//                        log.info("Значение критерия релевантности : " + relevantCriteria);
                        return relevantCriteria;
                    }
                }

            }
//            log.debug(" desiredwordOrderIdx = {}", desiredIdx);
        }
        long t2 = System.currentTimeMillis();
        log.info("время вычисления КР {} мс", (t2-t1));
        return relevantCriteria;
    }
}
