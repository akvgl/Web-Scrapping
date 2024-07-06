import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class LotteryScrapping
{
    // global variables
    static String DATE;
    static String CODE;
    static String NAME;
    static final String URLSTR = "https://www.seminarsonly.com/tech/";

    static int RST;
    static int RED;
    static int pTagIndex = 12;
    static String thirdPrizePTagOrHTag = "p";
    static String thirdPrizePTagOrHTagContains = ":contains(For The Tickets Ending With The Following Numbers)";
    static String thirdPrizePTagOrHTagFirst = "p";
    static String thirdPrizeSiblingSelector = "h4";
    static boolean isSreeShakthi =  false;
    static boolean isFiftyFifty = false;

    // global Arrays for Prize details....
    static  ArrayList<String> consolationPrizeNumbers = new ArrayList<>();
    static  ArrayList<String> thirdPrizeNumbers = new ArrayList<>();
    static  ArrayList<String> forthPrizeNumbers = new ArrayList<>();
    static  ArrayList<String> fifthPrizeNumbers = new ArrayList<>();
    static  ArrayList<String> sixthPrizeNumbers = new ArrayList<>();
    static  ArrayList<String> seventhPrizeNumbers = new ArrayList<>();
    static  ArrayList<String> eighthPrizeNumbers = new ArrayList<>();
    static String firstPrizeNumber;
    static String secondPrizeNumber;

    public static void main(String[] args)
    {
        String url;
        getNeededData();

        if(NAME == "NOT")
        {
            return;
        }

        url = URLSTR + NAME + CODE + DATE + ".php";
        
        assignPTagIndex(NAME);
        connectToUrl(url);
        searchForPrize();
    }

    public static void connectToUrl(String url)
    {
        try
        {
            Document document = Jsoup.connect(url).get();
            scapData(document);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void scapData(Document document)
    {
        Elements numbers = document.select(".col-md-9 > h3, h4, .a");

        Element firstPrize = document.selectFirst("h3:contains(1st Prize)");
        firstPrizeNumber = getFirstPrize(firstPrize);

        Element secondPrize = document.selectFirst("h3:contains(2nd Prize)");
        secondPrizeNumber = getSecondPrize(secondPrize);

        Element consolationPrize = document.selectFirst("h3:contains(Consolation Prize)");
        consolationPrizeNumbers = getConsolationPrize(consolationPrize);

        thirdPrizeNumbers = getThirdPrize(document);

        Element forthPrize = document.selectFirst("h3:contains(4th Prize)");
        forthPrizeNumbers = getForthPrize(forthPrize);

        Element fifthPrize = document.selectFirst("h3:contains(5th Prize)");
        fifthPrizeNumbers = getFifthPrize(fifthPrize);

        Element sixthPrize = document.selectFirst("h3:contains(6th Prize)");
        sixthPrizeNumbers = getSixthPrize(sixthPrize);

        Element seventhPrize = document.selectFirst("h3:contains(7th Prize)");
        seventhPrizeNumbers = getSevethPrize(seventhPrize);

        Element eighthPrize = document.selectFirst("h3:contains(8th Prize)");
        eighthPrizeNumbers = getEighthPrize(eighthPrize);
    }
    private static void assignPTagIndex(String lotteryName)
    {
        switch(lotteryName)
        {
            case "akshaya":
                pTagIndex = 10;
                thirdPrizePTagOrHTag = "p";
                break;
            case "win-win" , "karunya-plus":
                pTagIndex = 11;
                thirdPrizePTagOrHTag = "p";
                break;
            case "karunya":
                pTagIndex = 12;
                thirdPrizePTagOrHTag = "h2";
                break;
            case "nirmal-weekly":
                pTagIndex = 12;
                thirdPrizePTagOrHTag = "p";
                thirdPrizePTagOrHTagFirst = "h4";
                thirdPrizeSiblingSelector = "p";
                break;
            case "sthree-sakthi":
                pTagIndex = 11;
                thirdPrizePTagOrHTag = "h3";
                thirdPrizePTagOrHTagContains = ":contains(4th Prize)";
                isSreeShakthi = true;
                break;
            case "fifty-fifty":
                pTagIndex = 11;
                thirdPrizePTagOrHTag = "h3";
                thirdPrizePTagOrHTagContains = ":contains(4th Prize)";
                isSreeShakthi = true;
                isFiftyFifty = true;
                break;
            default:
                break;
        }

    }

    public static void searchForPrize()
    {
        switch (NAME)
        {
            case "akshaya", "win-win", "karunya":
                searchFirstCase();
                break;
            case "karunya-plus", "nirmal-weekly":
                searchSecondCase();
                break;
            case "sthree-sakthi":
                searchThirdCase();
                break;
            case "fifty-fifty":
                searchForthCase();
                break;
        }
    }

    public static void searchFirstCase() // for search in 4th to 8th results
    {
        List<Integer> forth = forthPrizeNumbers.stream().map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> fifth = fifthPrizeNumbers.stream().map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> sixth = sixthPrizeNumbers.stream().map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> seventh = seventhPrizeNumbers.stream().map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> eighth = eighthPrizeNumbers.stream().map(Integer::valueOf).collect(Collectors.toList());
        boolean flag = false;

        System.out.println("Prizes are : ");

        for(int i = RST; i <= RED; ++i)
        {
            if(Collections.binarySearch(forth, i) >= 0)
            {
                System.out.println(i + " ---> 5k");
                flag = true;
            }
            else if(Collections.binarySearch(fifth, i) >= 0)
            {
                System.out.println(i + " ---> 2k");
                flag = true;
            }
            else if(Collections.binarySearch(sixth, i) >= 0)
            {
                System.out.println(i + " ---> 1k");
                flag = true;
            }
            else if(Collections.binarySearch(seventh, i) >= 0)
            {
                System.out.println(i + " ---> 500");
                flag = true;
            }
            else if(Collections.binarySearch(eighth, i) >= 0)
            {
                System.out.println(i + " ---> 100");
                flag = true;
            }

        }

        if(!flag)
        {
            System.out.println("NO Prizes :(");
        }

    }

    public static void searchSecondCase() // for search in 4th to 7th results
    {
        List<Integer> forth = forthPrizeNumbers.stream().map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> fifth = fifthPrizeNumbers.stream().map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> sixth = sixthPrizeNumbers.stream().map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> seventh = seventhPrizeNumbers.stream().map(Integer::valueOf).collect(Collectors.toList());
        boolean flag = false;

        System.out.println("Prizes are : ");

        for(int i = RST; i <= RED; ++i)
        {
            if(Collections.binarySearch(forth, i) >= 0)
            {
                System.out.println(i + " ---> 5k");
                flag = true;
            }
            else if(Collections.binarySearch(fifth, i) >= 0)
            {
                System.out.println(i + " ---> 1k");
                flag = true;
            }
            else if(Collections.binarySearch(sixth, i) >= 0)
            {
                System.out.println(i + " ---> 500");
                flag = true;
            }
            else if(Collections.binarySearch(seventh, i) >= 0)
            {
                System.out.println(i + " ---> 100");
                flag = true;
            }

        }

        if(!flag)
        {
            System.out.println("NO Prizes :(");
        }
    }

    public static void searchThirdCase() // for search in 3rd to 8th results (ss)...
    {
        List<Integer> third = thirdPrizeNumbers.stream().map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> forth = forthPrizeNumbers.stream().map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> fifth = fifthPrizeNumbers.stream().map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> sixth = sixthPrizeNumbers.stream().map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> seventh = seventhPrizeNumbers.stream().map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> eighth = eighthPrizeNumbers.stream().map(Integer::valueOf).collect(Collectors.toList());
        boolean flag = false;

        System.out.println("Prizes are : ");

        for(int i = RST; i <= RED; ++i)
        {
            if(Collections.binarySearch(third, i) >= 0)
            {
                System.out.println(i + " ---> 5k");
                flag = true;
            }
            else if(Collections.binarySearch(forth, i) >= 0)
            {
                System.out.println(i + " ---> 2k");
                flag = true;
            }
            else if(Collections.binarySearch(fifth, i) >= 0)
            {
                System.out.println(i + " ---> 1k");
                flag = true;
            }
            else if(Collections.binarySearch(sixth, i) >= 0)
            {
                System.out.println(i + " ---> 500");
                flag = true;
            }
            else if(Collections.binarySearch(seventh, i) >= 0)
            {
                System.out.println(i + " ---> 200");
                flag = true;
            }
            else if(Collections.binarySearch(eighth, i) >= 0)
            {
                System.out.println(i + " ---> 100");
                flag = true;
            }
        }

        if(!flag)
        {
            System.out.println("NO Prizes :( ");
        }
    }

    public static void searchForthCase() // for search in 3rd to 7th results (ff)...
    {
        List<Integer> third = thirdPrizeNumbers.stream().map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> forth = forthPrizeNumbers.stream().map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> fifth = fifthPrizeNumbers.stream().map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> sixth = sixthPrizeNumbers.stream().map(Integer::valueOf).collect(Collectors.toList());
        List<Integer> seventh = seventhPrizeNumbers.stream().map(Integer::valueOf).collect(Collectors.toList());
        boolean flag = false;

        System.out.println("Prizes are : ");

        for(int i = RST; i <= RED; ++i)
        {
            if(Collections.binarySearch(third, i) >= 0)
            {
                System.out.println(i + " ---> 5k");
                flag = true;
            }
            else if(Collections.binarySearch(forth, i) >= 0)
            {
                System.out.println(i + " ---> 2k");
                flag = true;
            }
            else if(Collections.binarySearch(fifth, i) >= 0)
            {
                System.out.println(i + " ---> 1k");
                flag = true;
            }
            else if(Collections.binarySearch(sixth, i) >= 0)
            {
                System.out.println(i + " ---> 500");
                flag = true;
            }
            else if(Collections.binarySearch(seventh, i) >= 0)
            {
                System.out.println(i + " ---> 100");
                flag = true;
            }
        }

        if(!flag)
        {
            System.out.println("NO Prizes :( ");
        }
    }
    public static void getNeededData()
    {
        Scanner Input = new Scanner(System.in);
        boolean isFF = false;

        // for lottery name
        System.out.println("Choose your lottery : \n");
        System.out.println("1) Akshaya\n2) Win Win\n3) Karunya Plus\n4) Karunya\n5) Nirmal\n6) Sthree Sakthi\n7) Fifty Fifty\nChoice : ");

        int ch = Input.nextInt();

        switch (ch)
        {
            case 1:
                NAME = "akshaya";
                break;
            case 2:
                NAME = "win-win";
                break;
            case 3:
                NAME = "karunya-plus";
                break;
            case 4:
                NAME = "karunya";
                break;
            case 5:
                NAME = "nirmal-weekly";
                break;
            case 6:
                NAME = "sthree-sakthi";
                break;
            case 7:
                NAME = "fifty-fifty";
                isFF = true;
                break;
            default:
                NAME = "NOT";
                break;
        }

        // for date

        System.out.println("Enter date (dd-mm-yy) : ");
        DATE = Input.next();

        // for lottery code

        System.out.println("Enter lottery code : ");
        CODE = Input.next();
        CODE = '-' + CODE + '-';
        CODE = CODE.toLowerCase();

        // for ff
        if(isFF)
        {
            CODE = CODE.substring(0, 3) + "-50-50" + CODE.substring(3);
        }

        // for ranges

        System.out.println("Enter Start range : ");
        RST = Input.nextInt();

        System.out.println("Enter End range : ");
        RED = Input.nextInt();
    }

    public static String getFirstPrize(Element firstPrizeTag)
    {
        String prizeNumbers = new String();

        if(firstPrizeTag != null)
        {
            prizeNumbers = firstPrizeTag.nextElementSibling().text();
            prizeNumbers = removeUnwanded(prizeNumbers);
        }

        return prizeNumbers;
    }

    public static String getSecondPrize(Element secondPrizeTag)
    {
        String prizeNumbers = new String();

        if(secondPrizeTag != null)
        {
            prizeNumbers = secondPrizeTag.nextElementSibling().text();
            prizeNumbers = removeUnwanded(prizeNumbers);
        }

        return prizeNumbers;
    }

    public static ArrayList<String> getConsolationPrize(Element consolationPrizeTag)
    {
        ArrayList<String> prizeNumbers = new ArrayList<>();
        String prizeStr = new String();
        int beginIndex = 0;
        int endIndex = beginIndex + 8;

        if(consolationPrizeTag != null)
        {
            prizeStr = consolationPrizeTag.nextElementSibling().text();
            prizeStr = removeUnwanded(prizeStr);
        }

        while(endIndex <= prizeStr.length())
        {
            prizeNumbers.add(prizeStr.substring(beginIndex, endIndex));
            beginIndex = endIndex;
            endIndex += 8;
        }

        return prizeNumbers;
    }

    public static ArrayList<String> getThirdPrize(Document document)
    {
        ArrayList<String> prizeNumbers = new ArrayList<>();
        String prizeStr = new String();

        Elements pTags = document.select("p");
        Element firstPTag = pTags.get(pTagIndex);
        Elements lastPTags = document.select(thirdPrizePTagOrHTag + thirdPrizePTagOrHTagContains);
        Element lastPTag = lastPTags.first();
        Elements h4Tags = firstPTag.nextElementSiblings().select(thirdPrizeSiblingSelector);

        for(Element h4 : h4Tags)
        {
            if(h4.elementSiblingIndex() < lastPTag.elementSiblingIndex() && !isSreeShakthi)
            {
                prizeStr = h4.text();
                prizeStr = removeUnwanded(prizeStr);
                prizeNumbers.add(prizeStr);
            }
            else if(h4.elementSiblingIndex() < lastPTag.elementSiblingIndex() && isSreeShakthi)
            {
                prizeStr = h4.text();
                for(String s : prizeStr.split("\\s"))
                {
                    if(isFiftyFifty && !isNumber(prizeStr))
                    {
                        continue;
                    }
                    prizeNumbers.add(s);
                }
            }
            else
            {
                break;
            }
        }

        return prizeNumbers;
    }

    public static ArrayList<String> getForthPrize(Element forthPrizeTag)
    {
        ArrayList<String> prizeNumbers = new ArrayList<>();
        String prizeStr = new String();
        int beginIndex = 0;
        int endIndex = beginIndex + 4;

        if(forthPrizeTag != null)
        {
            prizeStr = forthPrizeTag.nextElementSibling().text();
            prizeStr = removeUnwanded(prizeStr);
        }

        while(endIndex <= prizeStr.length())
        {
            prizeNumbers.add(prizeStr.substring(beginIndex, endIndex));
            beginIndex = endIndex;
            endIndex += 4;
        }

        return prizeNumbers;
    }

    public static ArrayList<String> getFifthPrize(Element fifthPrizeTag)
    {
        ArrayList<String> prizeNumbers = new ArrayList<>();
        String prizeStr = new String();
        int beginIndex = 0;
        int endIndex = beginIndex + 4;

        if(fifthPrizeTag != null)
        {
            prizeStr = fifthPrizeTag.nextElementSibling().text();
            prizeStr = removeUnwanded(prizeStr);
        }

        while(endIndex <= prizeStr.length())
        {
            prizeNumbers.add(prizeStr.substring(beginIndex, endIndex));
            beginIndex = endIndex;
            endIndex += 4;
        }

        return prizeNumbers;
    }

    public static ArrayList<String> getSixthPrize(Element sixthPrizeTag)
    {
        ArrayList<String> prizeNumbers = new ArrayList<>();
        String prizeStr = new String();
        int beginIndex = 0;
        int endIndex = beginIndex + 4;

        if(sixthPrizeTag != null)
        {
            prizeStr = sixthPrizeTag.nextElementSibling().text();
            prizeStr = removeUnwanded(prizeStr);
        }

        while(endIndex <= prizeStr.length())
        {
            prizeNumbers.add(prizeStr.substring(beginIndex, endIndex));
            beginIndex = endIndex;
            endIndex += 4;
        }

        return prizeNumbers;
    }
    public static ArrayList<String> getSevethPrize(Element seventhPrizeTag)
    {
    ArrayList<String> prizeNumbers = new ArrayList<>();
    String prizeStr = new String();
    int beginIndex = 0;
    int endIndex = beginIndex + 4;

    if(seventhPrizeTag != null)
    {
        prizeStr = seventhPrizeTag.nextElementSibling().text();
        prizeStr = removeUnwanded(prizeStr);
    }

    while(endIndex <= prizeStr.length())
    {
        prizeNumbers.add(prizeStr.substring(beginIndex, endIndex));
        beginIndex = endIndex;
        endIndex += 4;
    }

    return prizeNumbers;
}

    public static ArrayList<String> getEighthPrize(Element eighthPrizeTag)
    {
    ArrayList<String> prizeNumbers = new ArrayList<>();
    String prizeStr = new String();
    int beginIndex = 0;
    int endIndex = beginIndex + 4;

    if(eighthPrizeTag != null)
    {
        prizeStr = eighthPrizeTag.nextElementSibling().text();
        prizeStr = removeUnwanded(prizeStr);
    }

    while(endIndex <= prizeStr.length())
    {
        prizeNumbers.add(prizeStr.substring(beginIndex, endIndex));
        beginIndex = endIndex;
        endIndex += 4;
    }

    return prizeNumbers;
}

    public static String removeUnwanded(String str)
    {
        String result  = new String();

        String expression = "\\([^\\)]*\\)";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(str);
        result = matcher.replaceAll("");
        result = result.replaceAll("\\s", "");
        result = result.replaceAll("\\d+\\)", "");

        return result;
    }

    public static boolean isNumber(String str)
    {
        for(int i = 0; i < str.length(); i++)
        {
            if(Character.isAlphabetic(str.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

}
