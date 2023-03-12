package cn.sustech;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class OnlineCoursesAnalyzer {

	private static class OnlineCourse {

		private final String institution;
		private final String courseNumber;
		private final Date launchDate;
		private final String courseTitle;
		private final String instructors;
		private final String courseSubjects;
		private final int year;
		private final int honorCode;
		private final int participants;
		private final int audited;
		private final int certified;
		private final double auditedRate;
		private final double certifiedRate;
		private final double certifiedRate5;
		private final double playedVideoRate;
		private final double postedInForumRate;
		private final double ght0Rate;
		private final double totalCourseHours;
		private final double medianHours;
		private final double medianAge;
		private final double maleRate;
		private final double femaleRate;
		private final double bachelorsRate;

		private OnlineCourse(String institution, String courseNumber, Date launchDate,
							 String courseTitle, String instructors, String courseSubjects, int year,
							 int honorCode, int participants, int audited, int certified, double auditedRate,
							 double certifiedRate, double certifiedRate5, double playedVideoRate,
							 double postedInForumRate, double ght0Rate, double totalCourseHours, double medianHours,
							 double medianAge, double maleRate, double femaleRate, double bachelorsRate) {
			this.institution = institution;
			this.courseNumber = courseNumber;
			this.launchDate = launchDate;
			this.courseTitle = courseTitle;
			this.instructors = instructors;
			this.courseSubjects = courseSubjects;
			this.year = year;
			this.honorCode = honorCode;
			this.participants = participants;
			this.audited = audited;
			this.certified = certified;
			this.auditedRate = auditedRate;
			this.certifiedRate = certifiedRate;
			this.certifiedRate5 = certifiedRate5;
			this.playedVideoRate = playedVideoRate;
			this.postedInForumRate = postedInForumRate;
			this.ght0Rate = ght0Rate;
			this.totalCourseHours = totalCourseHours;
			this.medianHours = medianHours;
			this.medianAge = medianAge;
			this.maleRate = maleRate;
			this.femaleRate = femaleRate;
			this.bachelorsRate = bachelorsRate;
		}

		public String getInstitution() {
			return institution;
		}

		public String getCourseNumber() {
			return courseNumber;
		}

		public Date getLaunchDate() {
			return launchDate;
		}

		public String getCourseTitle() {
			return courseTitle;
		}

		public String getInstructors() {
			return instructors;
		}

		public String getCourseSubjects() {
			return courseSubjects;
		}

		public int getYear() {
			return year;
		}

		public int getHonorCode() {
			return honorCode;
		}

		public int getParticipants() {
			return participants;
		}

		public int getAudited() {
			return audited;
		}

		public int getCertified() {
			return certified;
		}

		public double getAuditedRate() {
			return auditedRate;
		}

		public double getCertifiedRate() {
			return certifiedRate;
		}

		public double getCertifiedRate5() {
			return certifiedRate5;
		}

		public double getPlayedVideoRate() {
			return playedVideoRate;
		}

		public double getPostedInForumRate() {
			return postedInForumRate;
		}

		public double getGradeHigherThan0Rate() {
			return ght0Rate;
		}

		public double getTotalCourseHours() {
			return totalCourseHours;
		}

		public double getMedianHours() {
			return medianHours;
		}

		public double getMedianAge() {
			return medianAge;
		}

		public double getMaleRate() {
			return maleRate;
		}

		public double getFemaleRate() {
			return femaleRate;
		}

		public double getBachelorsRate() {
			return bachelorsRate;
		}

		@Override
		public String toString() {
			return "OnlineCourse{" +
				"institution='" + institution + '\'' +
				", courseNumber='" + courseNumber + '\'' +
				", launchDate=" + launchDate +
				", courseTitle='" + courseTitle + '\'' +
				", instructors=" + instructors +
				", courseSubjects=" + courseSubjects +
				", year=" + year +
				", isHonor=" + honorCode +
				", participants=" + participants +
				", audited=" + audited +
				", certified=" + certified +
				", auditedRate=" + auditedRate +
				", certifiedRate=" + certifiedRate +
				", certifiedRate5=" + certifiedRate5 +
				", playedVideoRate=" + playedVideoRate +
				", postedInForumRate=" + postedInForumRate +
				", ght0Rate=" + ght0Rate +
				", totalCourseHours=" + totalCourseHours +
				", medianHours=" + medianHours +
				", medianAge=" + medianAge +
				", maleRate=" + maleRate +
				", femaleRate=" + femaleRate +
				", bachelorsRate=" + bachelorsRate +
				'}';
		}
	}
	
	private List<OnlineCourse> coursesList;
	
	public OnlineCoursesAnalyzer(String datasetPath) throws IOException, ParseException {
		coursesList = new ArrayList<>();
		File file = new File(datasetPath);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		reader.readLine();
		for (String dataLine = reader.readLine(); dataLine != null; dataLine = reader.readLine()) {
			String datas[] = dataLine.replace("\"", "").replace(", ", "##").split(",");
			coursesList.add(new OnlineCourse(datas[0], datas[1], new Date(datas[2]), datas[3].replace("##", ", "), datas[4].replace("##", ", ")
				, datas[5].replace("##", ", "), Integer.parseInt(datas[6]), Integer.parseInt(datas[7]), Integer.parseInt(datas[8]), Integer.parseInt(datas[9])
				, Integer.parseInt(datas[10]), Double.parseDouble(datas[11]), Double.parseDouble(datas[12]), Double.parseDouble(datas[13]), Double.parseDouble(datas[14])
				, Double.parseDouble(datas[15]), Double.parseDouble(datas[16]), Double.parseDouble(datas[17]), Double.parseDouble(datas[18]), Double.parseDouble(datas[19])
				, Double.parseDouble(datas[20]), Double.parseDouble(datas[21]), Double.parseDouble(datas[22])));
		}
	}

	public Map<String, Integer> getPtcpCountByInst() {
		return this.coursesList.stream()
				.sorted((a, b) -> a.getInstitution().compareTo(b.getInstitution()))
				.collect(
						Collectors.toMap(OnlineCourse::getInstitution, OnlineCourse::getParticipants, (a, b) -> a + b, LinkedHashMap::new)
				);
	}

	public Map<String, Integer> getPtcpCountByInstAndSubject() {
		return this.coursesList.stream()
				.collect(Collectors.groupingBy(oc -> oc.getInstitution() + "-" + oc.getCourseSubjects(), Collectors.summingInt(OnlineCourse::getParticipants)))
				.entrySet()
				.stream()
				.sorted(Comparator.comparing(Map.Entry<String, Integer>::getValue).reversed().thenComparing(Map.Entry<String, Integer>::getKey))
				.collect(
						Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new)
				);
	}

	public Map<String, List<List<String>>> getCourseListOfInstructor() {
		Set<String> instructors = new HashSet<>();
		this.coursesList.stream().map(OnlineCourse::getInstructors).forEach(s -> instructors.addAll(Arrays.asList(s.replace("and ", "").replace(", ", ",").split(","))));
		Map<String, List<List<String>>> result = new HashMap<>();

		instructors.stream().distinct().forEach(s -> result.put(s, new ArrayList<>()));
		instructors.stream().distinct().forEach(
				s -> result.get(s).add(this.coursesList.stream().filter(oc -> oc.getInstructors().equals(s)).map(OnlineCourse::getCourseTitle).distinct().sorted().collect(Collectors.toList()))
		);
		instructors.stream().distinct().forEach(
				s -> result.get(s).add(this.coursesList.stream().filter(oc -> (!oc.getInstructors().equals(s)) && (", " + oc.getInstructors().replace("and ", "")).contains(", " + s)).map(OnlineCourse::getCourseTitle).distinct().sorted().collect(Collectors.toList()))
		);
		return result;
	}

	public List<String> getCourses(int topK, String by) {
		if (by.equals("hours")) {
			return this.coursesList.stream().sorted(Comparator.comparingDouble(OnlineCourse::getTotalCourseHours).reversed().thenComparing(OnlineCourse::getCourseTitle)).map(OnlineCourse::getCourseTitle).distinct().limit(topK).collect(Collectors.toList());
		} else {
			return this.coursesList.stream().sorted(Comparator.comparingInt(OnlineCourse::getParticipants).reversed().thenComparing(OnlineCourse::getCourseTitle)).map(OnlineCourse::getCourseTitle).distinct().limit(topK).collect(Collectors.toList());
		}
	}

	public List<String> searchCourses(String courseSubject, double percentAudited, double totalCourseHours) {
		return this.coursesList.stream()
				.filter(oc -> oc.getCourseSubjects().toLowerCase().contains(courseSubject.toLowerCase())
						&& oc.getAuditedRate() >= percentAudited
						&& oc.getTotalCourseHours() <= totalCourseHours)
				.map(OnlineCourse::getCourseTitle)
				.distinct()
				.sorted()
				.collect(Collectors.toList());
	}

	private double calculateSimilarityValue(int age, int gender, int isBachelorOrHigher, OnlineCourse course) {

		List<Double> values = this.coursesList.stream().filter(oc -> oc.getCourseNumber().equals(course.getCourseNumber())).collect(Collectors.collectingAndThen(Collectors.toList(), m -> {
			List<Double> list = new ArrayList<>();

			list.add(m.stream().collect(Collectors.summarizingDouble(OnlineCourse::getMedianAge)).getAverage());
			list.add(m.stream().collect(Collectors.summarizingDouble(OnlineCourse::getMaleRate)).getAverage());
			list.add(m.stream().collect(Collectors.summarizingDouble(OnlineCourse::getBachelorsRate)).getAverage());
			return list;
		}));
		double val = Math.pow(age - values.get(0), 2) + Math.pow(gender * 100 - values.get(1), 2) + Math.pow(isBachelorOrHigher * 100 - values.get(2), 2);
		return Math.pow(age - values.get(0), 2) + Math.pow(gender * 100 - values.get(1), 2) + Math.pow(isBachelorOrHigher * 100 - values.get(2), 2);
	}

	public List<String> recommendCourses(int age, int gender, int isBachelorOrHigher) {
		Map<String, Date> launchDateMap = this.coursesList.stream().collect(Collectors.toMap(OnlineCourse::getCourseNumber, OnlineCourse::getLaunchDate, (a, b) -> a.after(b) ? a : b));
		return this.coursesList.stream()
				.filter(oc -> oc.getLaunchDate().equals(launchDateMap.get(oc.getCourseNumber())))
				.sorted(Comparator.comparingDouble((OnlineCourse a) -> calculateSimilarityValue(age, gender, isBachelorOrHigher, a)).thenComparing(OnlineCourse::getCourseTitle))
				.map(OnlineCourse::getCourseTitle)
				.distinct()
				.limit(10)
				.collect(Collectors.toList());
	}
	
}
