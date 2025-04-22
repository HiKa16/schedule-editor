package app;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


public class Config {
	
	public LocalDate startDate;
	public LocalDate endDate;
	public LocalTime startHour;
	public LocalTime endHour;
	
	
	public static Config load(String file) throws StreamReadException, DatabindException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		return mapper.readValue(new File(file), Config.class);
	}
	
}
