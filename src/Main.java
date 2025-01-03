public class Main {
    public static void main(String[] args) {
        Day1 day1 = new Day1("files/day1.txt");
        System.out.println("Day 1/1: " + day1.totalDistance());
        System.out.println("Day 1/2: " + day1.similarityScore());

        Day2 day2 = new Day2("files/day2.txt");
        System.out.println("Day 2/1: " + day2.safeReportsCount());
        System.out.println("Day 2/2: " + day2.safeReportsCount(true));

        Day3 day3 = new Day3("files/day3.txt");
        System.out.println("Day 3/1: " + day3.multiplications("(mul)\\((\\d+),(\\d+)\\)"));
        System.out.println("Day 3/2: " + day3.multiplications("(mul(?=\\(\\d+,\\d+\\))|do|don't)\\((?:(\\d+),(\\d+))?\\)"));

        Day4 day4 = new Day4("files/day4.txt");
        System.out.println("Day 4/1: " + day4.wordCount("xmas"));
        System.out.println("Day 4/2: " + day4.wordCount("mas", true));

        Day5 day5 = new Day5("files/day5.txt");
        System.out.println("Day 5/1: " + day5.sumMiddleNumbers());
        System.out.println("Day 5/2: " + day5.sumMiddleNumbers(true));

        Day6 day6 = new Day6("files/day6.txt");
        System.out.println("Day 6/1: " + day6.countPositions());
        System.out.println("Day 6/2: " + day6.countPositions(true));

        Day7 day7 = new Day7("files/day7.txt");
        System.out.println("Day 7/1: " + day7.calibrationResult());
        System.out.println("Day 7/2: " + day7.calibrationResult(true));

        Day8 day8 = new Day8("files/day8.txt");
        System.out.println("Day 8/1: " + day8.countAntinodes());
        System.out.println("Day 8/2: " + day8.countAntinodes(true));

        Day9 day9 = new Day9("files/day9.txt");
        System.out.println("Day 9/1: " + day9.fileBlocksChecksum());
        System.out.println("Day 9/2: " + day9.filesChecksum());

        Day10 day10 = new Day10("files/day10.txt");
        System.out.println("Day 10/1: " + day10.trailheadsSum());
        System.out.println("Day 10/2: " + day10.trailheadsSum(true));

        Day11 day11 = new Day11("files/day11.txt");
        System.out.println("Day 11/1: " + day11.countNumbers(25));
        System.out.println("Day 11/2: " + day11.countNumbers(75));

        Day12 day12 = new Day12("files/day12.txt");
        System.out.println("Day 12/1: " + day12.getPrice());
        System.out.println("Day 12/2: " + day12.getPrice(true));

        Day13 day13 = new Day13("files/day13.txt");
        System.out.println("Day 13/1: " + day13.countTokens());
        System.out.println("Day 13/2: " + day13.countTokens(10000000000000L));

        Day14 day14 = new Day14("files/day14.txt");
        System.out.println("Day 14/1: " + day14.getSafetyFactor(101, 103, 100));
        System.out.println("Day 14/2: " + day14.findEasterEgg(101, 103));

        Day15 day15 = new Day15("files/day15.txt");
        System.out.println("Day 15/1: " + day15.sumBoxesCoordinates());
        System.out.println("Day 15/2: " + day15.sumBoxesCoordinates(true));

        Day16 day16 = new Day16("files/day16.txt");
        System.out.println("Day 16/1: " + day16.getScore());
        System.out.println("Day 16/2: " + day16.countTiles());

        Day17 day17 = new Day17("files/day17.txt");
        System.out.println("Day 17/1: " + day17.getProgramOutput());
        System.out.println("Day 17/2: " + day17.getLowestValue());

        Day18 day18 = new Day18("files/day18.txt");
        System.out.println("Day 18/1: " + day18.getMinSteps(1024));
        System.out.println("Day 18/2: " + day18.getFirstPreventingByte());

        Day19 day19 = new Day19("files/day19.txt");
        System.out.println("Day 19/1: " + day19.getPossibleDesigns());
        System.out.println("Day 19/2: " + day19.getAllWays());

        Day20 day20 = new Day20("files/day20.txt");
        System.out.println("Day 20/1: " + day20.countCheats(100, 2));
        System.out.println("Day 20/2: " + day20.countCheats(100, 20));

        Day21 day21 = new Day21("files/day21.txt");
        System.out.println("Day 21/1: " + day21.getSum(3));
        System.out.println("Day 21/2: " + day21.getSum(26));

        Day22 day22 = new Day22("files/day22.txt");
        System.out.println("Day 22/1: " + day22.getSum(2000));
        System.out.println("Day 22/2: " + day22.getBananas(2000));

        Day23 day23 = new Day23("files/day23.txt");
        System.out.println("Day 23/1: " + day23.getCliques());
        System.out.println("Day 23/2: " + day23.getMaxClique());

        Day24 day24 = new Day24("files/day24.txt");
        System.out.println("Day 24/1: " + day24.simulate());
        System.out.println("Day 24/2: " + day24.getSwappedWires());

        Day25 day25 = new Day25("files/day25.txt");
        System.out.println("Day 25/1: " + day25.countPairs());
    }
}