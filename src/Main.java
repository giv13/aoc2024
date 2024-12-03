public class Main {
    public static void main(String[] args) {
        Day1 day1 = new Day1("files/day1.txt");
        System.out.println(day1.totalDistance());
        System.out.println(day1.similarityScore());

        Day2 day2 = new Day2("files/day2.txt");
        System.out.println(day2.safeReportsCount());
        System.out.println(day2.safeReportsCount(1));

        Day3 day3 = new Day3("files/day3.txt");
        System.out.println(day3.multiplications("(mul)\\((\\d+),(\\d+)\\)"));
        System.out.println(day3.multiplications("(mul(?=\\(\\d+,\\d+\\))|do|don't)\\((?:(\\d+),(\\d+))?\\)"));
    }
}