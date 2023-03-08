package cn.sustech;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class OnlineCoursesAnalyzer {
	
	private static class OnlineCourse {
	
		private String institution;
		private String courseNumber;
		private Date launchDate;
		private String courseTitle;
		private Set<String> instructors;
		private Set<String> courseSubjects;
		private int year;
		private boolean isHonor;
		private int participants;
		private int audited;
		private int certified;
		private double auditedRate;
		private double certifiedRate;
		private double certifiedRate5;
		private double playedVideoRate;
		private double postedInForumRate;
		private double ght0Rate;
		private double totalCourseHours;
		private double medianHours;
		private double medianAge;
		private double maleRate;
		private double femaleRate;
		private double bachelorsRate;
		
		private OnlineCourse(String institution, String courseNumber, Date launchDate,
			String courseTitle, Set<String> instructors, Set<String> courseSubjects, int year,
			boolean isHonor, int participants, int audited, int certified, double auditedRate,
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
			this.isHonor = isHonor;
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
		
		public Set<String> getInstructors() {
			return instructors;
		}
		
		public Set<String> getCourseSubjects() {
			return courseSubjects;
		}
		
		public int getYear() {
			return year;
		}
		
		public boolean isHonor() {
			return isHonor;
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
				", isHonor=" + isHonor +
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
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.CHINA);
		for (String dataLine = reader.readLine(); dataLine != null; dataLine = reader.readLine()) {
			String datas[] = dataLine.replace(", ", "##").split(",");
			//Arrays.stream(datas).forEach(System.out::println);
			Set<String> instructors = new HashSet<>(Arrays.asList(datas[4].replace("\"", "").split("##")));
			Set<String> subjects = new HashSet<>(Arrays.asList(datas[5].replace("and ", "").replace("\"", "").split("##")));
			
			coursesList.add(new OnlineCourse(datas[0], datas[1], sdf.parse(datas[2]), datas[3], instructors
				, subjects, Integer.parseInt(datas[6]), Integer.parseInt(datas[7]) == 1, Integer.parseInt(datas[8]), Integer.parseInt(datas[9])
				, Integer.parseInt(datas[10]), Double.parseDouble(datas[11]), Double.parseDouble(datas[12]), Double.parseDouble(datas[13]), Double.parseDouble(datas[14])
				, Double.parseDouble(datas[15]), Double.parseDouble(datas[16]), Double.parseDouble(datas[17]), Double.parseDouble(datas[18]), Double.parseDouble(datas[19])
				, Double.parseDouble(datas[20]), Double.parseDouble(datas[21]), Double.parseDouble(datas[22])));
		}
		coursesList.forEach(System.out::println);
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		new OnlineCoursesAnalyzer("local.csv");
	}
	
}
