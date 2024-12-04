public class Main {
    public static void main(String[] args) {
        //Day1 day1 = new Day1("files/day1.txt");
        //System.out.println("Day 1/1: " + day1.totalDistance());
        //System.out.println("Day 1/2: " + day1.similarityScore());

        //Day2 day2 = new Day2("files/day2.txt");
        //System.out.println("Day 2/1: " + day2.safeReportsCount());
        //System.out.println("Day 2/2: " + day2.safeReportsCount(true));

        //Day3 day3 = new Day3("files/day3.txt");
        //System.out.println("Day 3/1: " + day3.multiplications("(mul)\\((\\d+),(\\d+)\\)"));
        //System.out.println("Day 3/2: " + day3.multiplications("(mul(?=\\(\\d+,\\d+\\))|do|don't)\\((?:(\\d+),(\\d+))?\\)"));

        Day4 day4 = new Day4("files/day4.txt");
        System.out.println("Day 4/1: " + day4.wordCount("xmas"));
        System.out.println("Day 4/2: " + day4.wordCount("mas", true));
    }
}