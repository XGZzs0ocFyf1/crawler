package ru.gurzhiy.crawler;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLineHandling {


    private static Utils utilites;

    @BeforeAll
    public static void init(){
        utilites = new Utils();
    }

    @Test
    public void thenGetOneSameWordReturn2(){
        int desiredCriteria2 = utilites.getRelevantCriteriaForRequestAndLine("aaa bbb", "aaa ccc");
        assertEquals(2, desiredCriteria2);
    }

    @Test
    public void thenGetTwoWordsInSameOrderReturn5(){
        int criteriaSix1 = utilites.getRelevantCriteriaForRequestAndLine("aaa bbb", "aaa bbb ccc");
        assertEquals(5, criteriaSix1);
    }

    @Test
    public void thenGetTwoWordsInDifferentOrderReturn4(){
        int criteriaSix2 = utilites.getRelevantCriteriaForRequestAndLine("aaa bbb", "aaa  ccc bbb");
        assertEquals(4, criteriaSix2);
    }


    @Test
    public void testGetForWordsAndDesired10(){
        String line = "съешь еще этих мягких французских булок";
        String request = "съешь еще этих булок";
        int criteria = utilites.getRelevantCriteriaForRequestAndLine(request, line);
        assertEquals(10, criteria);

    }

    @Test
    public void testGetForWordsAndDesired6(){
        String line = "съешь еще этих мягких французских булок";
        String request = "этих булок ты не съешь";
        int criteria = utilites.getRelevantCriteriaForRequestAndLine(request, line);
        assertEquals(6, criteria);

    }

    @Test
    public void testThatInputWordIsPartOfComplexWordInLineAndDesired0(){
        String line = "съешь еще этих мягких французских булок";
        String request = "француз бул";
        int criteria = utilites.getRelevantCriteriaForRequestAndLine(request, line);
        assertEquals(0, criteria);
    }


    //этот тест написан по результатам прогона сгенерированного файла
    //выявляет повторное срабатывание KP  += 1 на то же слово, которое привело к полной проверке строки
    @Test
    public void testThatThisIsNot3(){
        String line = "нависающий засыпавшийся не кто иной ";
        String request = "конь не валялся";
        int criteria = utilites.getRelevantCriteriaForRequestAndLine(request, line);
        assertEquals(2, criteria);
    }


}
