package cn.sustech;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * class that analyse online courses.
 */
public class OnlineCoursesAnalyzer {
    
    /**
     *</p>
     * Record class for OnlineCourse which represents a course.
     *
     * @param institution online course holders
     * @param courseNumber the unique id of each course
     * @param launchDate the launch date of each course
     * @param courseTitle the title of each course
     * @param instructors the instructors of each course
     * @param courseSubjects the subject of each course
     * @param year the last time of each course
     * @param honorCode the honor code
     * @param participants the number of participants who have accessed the course
     * @param audited the number of participants who have audited more than 50% of the course
     * @param certified total number of votes
     * @param auditedRate the percent of the audited
     * @param certifiedRate the percent of the certified
     * @param certifiedRate5 the percent of the certified with accessing the course more than 50%
     * @param playedVideoRate the percent of playing video
     * @param postedInForumRate the percent of posting in forum
     * @param ght0Rate the percent of grade higher than zero
     * @param totalCourseHours total course hours(per 1000)
     * @param medianHours median hours for certification
     * @param medianAge median age of the participants
     * @param maleRate the percent of the male
     * @param femaleRate the percent of the female
     * @param bachelorsRate the percent of bachelor's degree of higher
     */
    private record OnlineCourse(String institution, String courseNumber, Date launchDate,
                                String courseTitle, String instructors, String courseSubjects,
                                int year, int honorCode, int participants, int audited,
                                int certified, double auditedRate, double certifiedRate,
                                double certifiedRate5, double playedVideoRate,
                                double postedInForumRate, double ght0Rate, double totalCourseHours,
                                double medianHours, double medianAge, double maleRate,
                                double femaleRate, double bachelorsRate) {
        
        @Override
        public String toString() {
            return "OnlineCourse{"
                + "institution='" + institution + '\''
                + ", courseNumber='" + courseNumber + '\''
                + ", launchDate=" + launchDate
                + ", courseTitle='" + courseTitle + '\''
                + ", instructors=" + instructors
                + ", courseSubjects=" + courseSubjects
                + ", year=" + year
                + ", isHonor=" + honorCode
                + ", participants=" + participants
                + ", audited=" + audited
                + ", certified=" + certified
                + ", auditedRate=" + auditedRate
                + ", certifiedRate=" + certifiedRate
                + ", certifiedRate5=" + certifiedRate5
                + ", playedVideoRate=" + playedVideoRate
                + ", postedInForumRate=" + postedInForumRate
                + ", ght0Rate=" + ght0Rate
                + ", totalCourseHours=" + totalCourseHours
                + ", medianHours=" + medianHours
                + ", medianAge=" + medianAge
                + ", maleRate=" + maleRate
                + ", femaleRate=" + femaleRate
                + ", bachelorsRate=" + bachelorsRate
                + '}';
        }
    }
    
    private final List<OnlineCourse> coursesList;
    
    /**.
     *
     * <p>the default constructor of OnlineCoursesAnalyzer</p>
     *
     * @param datasetPath the data file (.csv file) path
     * @throws IOException exception thrown when reading file
     */
    public OnlineCoursesAnalyzer(String datasetPath) throws IOException {
        coursesList = new ArrayList<>();
        File file = new File(datasetPath);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        reader.readLine();
        for (String dataLine = reader.readLine(); dataLine != null; dataLine = reader.readLine()) {
            String[] datas = dataLine.replace("\"", "").replace(", ", "##").split(",");
            coursesList.add(new OnlineCourse(datas[0], datas[1], new Date(datas[2]),
                datas[3].replace("##", ", "), datas[4].replace("##", ", "),
                datas[5].replace("##", ", "), Integer.parseInt(datas[6]),
                Integer.parseInt(datas[7]), Integer.parseInt(datas[8]), Integer.parseInt(datas[9]),
                Integer.parseInt(datas[10]), Double.parseDouble(datas[11]),
                Double.parseDouble(datas[12]), Double.parseDouble(datas[13]),
                Double.parseDouble(datas[14]),
                Double.parseDouble(datas[15]), Double.parseDouble(datas[16]),
                Double.parseDouble(datas[17]), Double.parseDouble(datas[18]),
                Double.parseDouble(datas[19]),
                Double.parseDouble(datas[20]), Double.parseDouble(datas[21]),
                Double.parseDouble(datas[22])));
        }
    }
    
    /**
     *
     * <p>This method returns a &lt;institution, count&gt; map, where the key is the institution
     * while the value is the total number of participants who have accessed
     * the courses of the institution.</p>
     *
     * @return the map of participants count by Institution
     */
    public Map<String, Integer> getPtcpCountByInst() {
        return this.coursesList.stream()
            .sorted(Comparator.comparing(OnlineCourse::institution))
            .collect(
                Collectors.toMap(OnlineCourse::institution, OnlineCourse::participants,
                    Integer::sum, LinkedHashMap::new)
            );
    }
    
    /**
     *
     * <p>This method returns a &lt;institution-course Subject, count&gt; map,
     * where the key is the string concatenating the Institution and the course
     * Subject (without quotation marks) using '-' while the value is the total
     * number of participants in a course Subject of an institution.</p>
     *
     * @return the map of participants count by Institution and course subject
     */
    public Map<String, Integer> getPtcpCountByInstAndSubject() {
        return this.coursesList.stream()
            .collect(Collectors.groupingBy(oc -> oc.institution() + "-" + oc.courseSubjects(),
                Collectors.summingInt(OnlineCourse::participants)))
            .entrySet()
            .stream()
            .sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed()
                .thenComparing(Map.Entry::getKey))
            .collect(
                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a,
                    LinkedHashMap::new)
            );
    }
    
    /**
     *
     * <p>An instructor may be responsible for multiple courses,
     * including independently responsible courses and co-developed courses.</p>
     *
     * @return the map of course list by instructor
     */
    public Map<String, List<List<String>>> getCourseListOfInstructor() {
        Set<String> instructors = new HashSet<>();
        this.coursesList.stream().map(OnlineCourse::instructors).forEach(s -> instructors.addAll(
            Arrays.asList(s.replace("and ", "").replace(", ", ",").split(","))));
        Map<String, List<List<String>>> result = new HashMap<>();
        
        instructors.stream().distinct().forEach(s -> result.put(s, new ArrayList<>()));
        instructors.stream().distinct().forEach(
            s -> result.get(s).add(
                this.coursesList.stream().filter(oc -> oc.instructors().equals(s))
                    .map(OnlineCourse::courseTitle).distinct().sorted()
                    .collect(Collectors.toList()))
        );
        instructors.stream().distinct().forEach(
            s -> result.get(s).add(this.coursesList.stream().filter(
                    oc -> (!oc.instructors().equals(s)) && (", " + oc.instructors()
                        .replace("and ", "")).contains(", " + s)).map(OnlineCourse::courseTitle)
                .distinct().sorted().collect(Collectors.toList()))
        );
        return result;
    }
    
    /**
     *
     * <p>This method returns the top K courses by the given criterion.Specifically,</p>
     * <p>by="hours": the results should be courses sorted by descending order
     * of Total Course Hours (Thousands) (from the longest course to the shortest course).</p>
     * <p>by="participants": the results should be courses sorted by descending order
     * of the number of the Participants (Course Content Accessed) (from the most to the least).</p>
     *
     * @param topK the top K courses
     * @param by the given criterion
     * @return the list of top courses
     */
    public List<String> getCourses(int topK, String by) {
        if (by.equals("hours")) {
            return this.coursesList.stream().sorted(
                    Comparator.comparingDouble(OnlineCourse::totalCourseHours).reversed()
                        .thenComparing(OnlineCourse::courseTitle)).map(OnlineCourse::courseTitle)
                .distinct().limit(topK).collect(Collectors.toList());
        } else {
            return this.coursesList.stream().sorted(
                    Comparator.comparingInt(OnlineCourse::participants).reversed()
                        .thenComparing(OnlineCourse::courseTitle)).map(OnlineCourse::courseTitle)
                .distinct().limit(topK).collect(Collectors.toList());
        }
    }
    
    /**.
     *
     * <p>This method searches courses based on three criteria:</p>
     * <p>courseSubject: Fuzzy matching is supported and case insensitive.
     * If the inputcourseSubject is "science", all courses whose course subject
     * includes "science" or "Science" or whatever (case insensitive) meet the criteria.</p>
     * <p>percentAudited: the percent of the audited should >= percentAudited</p>
     * <p>totalCourseHours: the Total Course Hours (Thousands) should <= totalCourseHours</p>
     *
     * @param courseSubject the specified course subject
     * @param percentAudited the percent of the audited
     * @param totalCourseHours the total course hours
     * @return the list of the top K courses
     */
    public List<String> searchCourses(String courseSubject, double percentAudited,
        double totalCourseHours) {
        return this.coursesList.stream()
            .filter(oc -> oc.courseSubjects().toLowerCase().contains(courseSubject.toLowerCase())
                && oc.auditedRate() >= percentAudited
                && oc.totalCourseHours() <= totalCourseHours)
            .map(OnlineCourse::courseTitle)
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }
    
    private double calculateSimilarityValue(int age, int gender, int isBachelorOrHigher,
        OnlineCourse course) {
        
        List<Double> values = this.coursesList.stream()
            .filter(oc -> oc.courseNumber().equals(course.courseNumber()))
            .collect(Collectors.collectingAndThen(Collectors.toList(), m -> {
                List<Double> list = new ArrayList<>();
                
                list.add(m.stream().collect(Collectors.summarizingDouble(OnlineCourse::medianAge))
                    .getAverage());
                list.add(m.stream().collect(Collectors.summarizingDouble(OnlineCourse::maleRate))
                    .getAverage());
                list.add(
                    m.stream().collect(Collectors.summarizingDouble(OnlineCourse::bachelorsRate))
                        .getAverage());
                return list;
            }));
        return Math.pow(age - values.get(0), 2) + Math.pow(gender * 100 - values.get(1), 2)
            + Math.pow(isBachelorOrHigher * 100 - values.get(2), 2);
    }
    
    /**.
     *
     * <p>This method recommends 10 courses based on the input parameter.</p>
     *
     * @param age age of the user
     * @param gender 0-female, 1-male
     * @param isBachelorOrHigher 0-Not get bachelor degree, 1- Bachelor degree or higher
     * @return the list of recommends 10 courses
     */
    public List<String> recommendCourses(int age, int gender, int isBachelorOrHigher) {
        Map<String, Date> launchDateMap = this.coursesList.stream().collect(
            Collectors.toMap(OnlineCourse::courseNumber, OnlineCourse::launchDate,
                (a, b) -> a.after(b) ? a : b));
        return this.coursesList.stream()
            .filter(oc -> oc.launchDate().equals(launchDateMap.get(oc.courseNumber())))
            .sorted(Comparator.comparingDouble(
                    (OnlineCourse a) -> calculateSimilarityValue(age, gender, isBachelorOrHigher, a)
                ).thenComparing(OnlineCourse::courseTitle))
            .map(OnlineCourse::courseTitle)
            .distinct()
            .limit(10)
            .collect(Collectors.toList());
    }
    
}
