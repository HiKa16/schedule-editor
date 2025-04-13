package app;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import schedule.*;

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
		
		fileWriter.println("#eventDetails {");
		fileWriter.println("min-width: 300px;");
		fileWriter.println("min-height: 150px;");
		fileWriter.println("max-width: 80%;");
		fileWriter.println("max-height: 80%;");
		fileWriter.println("overflow-y: auto;");
		fileWriter.println("overflow-x: auto;");
		fileWriter.println("padding: 20px;}");
		
		
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
						String date = slot.getDate().format(DateTimeFormatter.ofPattern("EEEE d LLLL", Locale.FRENCH));
						String desc = event.getDescription().replace("\n", "\\n");
						String time = slot.getStartTime() + " - " + slot.getEndTime();
						fileWriter.println(String.format("<td rowspan=%d> <button type=\"button\" class=\"event\" "
															+ "onclick=\"openDetails('%s', '%s', '%s', \'<pre style=\\\'font-family: sans-serif\\\'> %s </pre>\');\"> %s </button> </td>", 
															rowSpan, event.getTitle(), date, time, desc, event.getTitle()));
						for (int i = row; i < row+rowSpan; i++) { isFilled[i][col] = 1; }
					}
				}
			}
		}
		fileWriter.println("</table>");
		
		fileWriter.println("<dialog id=\"eventDetails\">");
		fileWriter.println("<h2 id=\"eventTitle\" style=\"margin-top:0px\"></h2>");
		fileWriter.println("<p>📅 <span id=\"eventDate\"></span> </p>");
		fileWriter.println("<p>🕒 <span id=\"eventTime\"></span> </p>");
		fileWriter.println("<p id=\"eventDescription\"></p>");
		fileWriter.println("<form method=\"dialog\">");
		fileWriter.println("<button>Fermer</button>");
		fileWriter.println("</form>");
		fileWriter.println("</dialog>");
		
		fileWriter.println("<script>");
		fileWriter.println("function openDetails(title, date, time, desc) {");
		fileWriter.println("document.getElementById(\"eventTitle\").textContent = title;");
		fileWriter.println("document.getElementById(\"eventDate\").textContent = date;");
		fileWriter.println("document.getElementById(\"eventTime\").textContent = time;");
		fileWriter.println("document.getElementById(\"eventDescription\").innerHTML = desc;");
		fileWriter.println("const dialog = document.getElementById(\"eventDetails\");");
		fileWriter.println("dialog.showModal();");
		fileWriter.println("dialog.scrollTop = 0;}");
		fileWriter.println("</script>");
		
		fileWriter.println("</body>");
		fileWriter.println("</html>");
		fileWriter.close();
		
	}
}
