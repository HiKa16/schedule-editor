package schedule;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ScheduleExporter {
	public static void exportToHTML(Schedule schedule, List<LocalDate> dates, List<LocalTime> hours, String file, String title) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter fileWriter = new PrintWriter(file, "UTF-8");
		fileWriter.println("<!DOCTYPE html>");
		fileWriter.println("<html>");
		fileWriter.println("<head>");
		fileWriter.println("<meta charset=\"utf-8\">");
		fileWriter.println("<title>" + title + "</title>");
		
		fileWriter.println("<style>");
		
		fileWriter.println("body {");
		fileWriter.println("font-family: sans-serif;");
		fileWriter.println("font-size: 14px;}");
		
		fileWriter.println("h1 {");
		fileWriter.println("text-align: center;");
		fileWriter.println("}");
		
		fileWriter.println("table {");
		fileWriter.println("width: 100%;");
		fileWriter.println("height: 700px;");
		fileWriter.println("font-weight: bold;");
		fileWriter.println("text-align: center;");
		fileWriter.println("border: 3px solid #ccc;");
		fileWriter.println("border-collapse: collapse; }");
		
		fileWriter.println("td {");
		fileWriter.println("border-style: solid;");
		fileWriter.println("border-color: #dddddd #cdcdcd #dddddd #cdcdcd;");
		fileWriter.println("border-width: 1px 2px 1px 2px;");
		fileWriter.println("padding: 5;}");
		
		
		fileWriter.println(".event {");
		fileWriter.println("display: block;");
		fileWriter.println("background-image: linear-gradient(to bottom, #4a90e2, #0a6bff);");
		fileWriter.println("width : 100%;");
		fileWriter.println("height: 100%;");
		fileWriter.println("color: white;");
		fileWriter.println("font-weight: bold;");
		fileWriter.println("border-style: solid;");
		fileWriter.println("border-color: #0047ab;");
		fileWriter.println("border-width: 3px;");
		fileWriter.println("border-radius: 6px;");
		fileWriter.println("padding: 5px;}");
		fileWriter.println(".event:hover { cursor: pointer; }");
		
		
		fileWriter.println("</style>");
		fileWriter.println("</head>");
		
		fileWriter.println("<body>"); 
		fileWriter.println("<h1>" + title + "</h1>");
		fileWriter.println("<table>");
		
		int[][] isFilled = new int[hours.size()][dates.size()];  
		fileWriter.println("<tr>");
		fileWriter.println("<td></td>");
		for (int i = 0; i < dates.size(); i++) {
			fileWriter.println(String.format("<td> %s </td>", dates.get(i).format(DateTimeFormatter.ofPattern("EEEE d LLLL", Locale.FRENCH))));
		}
		fileWriter.println("</tr>");
		for (int row = 0; row < hours.size()-1; row++) {
			fileWriter.println("<tr>");
			fileWriter.println(String.format("<td> %dh - %dh </td>", hours.get(row).getHour(), hours.get(row+1).getHour()));
			for (int col = 0; col < dates.size(); col++) {
				System.out.println(String.format("%d %d", row, col));
				if (!(isFilled[row][col] == 1)) {
					PlannedEvent event = schedule.getEventStartingAt(dates.get(col), hours.get(row));
					if (event == null) {
						fileWriter.println("<td></td>");
					} else {
						TimeSlot slot = event.getTimeSlot();
						int rowSpan = hours.indexOf(slot.getEndTime()) - hours.indexOf(slot.getStartTime());
						fileWriter.println(String.format("<td rowspan=%d> <button type=\"button\" class=\"event\"> %s </button> </td>", rowSpan, event.getTitle()));
						for (int i = row; i < row+rowSpan; i++) { isFilled[i][col] = 1; }
					}
				}
			}
		}
		fileWriter.println("</table>");

		fileWriter.println("</body>");
		fileWriter.println("</html>");
		fileWriter.close();
		
	}
}
