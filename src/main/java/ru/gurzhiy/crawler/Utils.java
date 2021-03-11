package ru.gurzhiy.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.gurzhiy.crawler.benchmark.DataProducer;

public class Utils {

    private final static Logger log = LoggerFactory.getLogger(Utils.class);



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

        long t1 = System.currentTimeMillis();
        int relevantCriteria = 0;
//        log.info(" request: "+request);
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
     //   log.info("время вычисления КР {} мс", (t2-t1));
        return relevantCriteria;
    }
}
