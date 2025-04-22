package app;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import schedule.*;

public class EditorApp extends Application {
    private static Schedule schedule = new Schedule();
	private static GridPane scheduleGrid = new GridPane();
    private static List<LocalDate> dates; 
    private static List<LocalTime> hours; 


    @Override
    public void start(Stage stage) {
 
    	try {
			Config config = Config.load("config.json");
			dates = getDatesRange(config.startDate, config.endDate);
			hours = getHoursRange(config.startHour, config.endHour);
		} catch (IOException e) {
			System.out.println("Erreur de configuration");
			//e.printStackTrace();
			return;
		}    	
    	
    	setScheduleGrid();
      
        Button addEventButton = new Button("Ajouter un évènement");
        styleButton(addEventButton);
        addEventButton.setOnAction(value -> addEventDialog());
        
        Button exportButton = new Button("Exporter");
        exportButton.setOnAction(value -> exportScheduleDialog());
         
        VBox layout = new VBox(10, scheduleGrid, addEventButton, exportButton);
        layout.setAlignment(Pos.CENTER);      
        VBox.setVgrow(scheduleGrid, Priority.ALWAYS);
        layout.setPadding(new Insets(5));

        Scene scene = new Scene(layout, 1200, 700);
        stage.setScene(scene);

        stage.show();
    }
    
    
    //Date range of the schedule defined by the parameters
    public static List<LocalDate> getDatesRange(LocalDate start, LocalDate end) {
    	assert(!start.isAfter(end));
        List<LocalDate> datesRange = new ArrayList<LocalDate>();
        LocalDate date = start; datesRange.add(date);
        while (!date.isEqual(end)) {
        	date = date.plusDays(1);
        	datesRange.add(date);
        }
        return datesRange;  
    }
    
    //Hour range of the schedule defined by the parameters
    public static List<LocalTime> getHoursRange(LocalTime start, LocalTime end) {
    	assert(!start.isAfter(end));
    	List<LocalTime> hoursRange = new ArrayList<LocalTime>();
    	LocalTime hour = start; hoursRange.add(hour);
    	while (!hour.equals(end)) {
    		hour = hour.plusHours(1);
    		hoursRange.add(hour);
    	}
    	return hoursRange;
    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONFIGURATION
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static void setScheduleGrid() {
    	 int nbRows = hours.size();
    	 int nbCols = dates.size() + 1;
    	 
    	 ColumnConstraints colSize = new ColumnConstraints();
         colSize.setPercentWidth(100.0/nbCols);
         for (int i = 0; i < nbCols ; i++) {
             scheduleGrid.getColumnConstraints().add(colSize); 

         }
         
         RowConstraints rowSize = new RowConstraints();
         rowSize.setPercentHeight(100.0 / nbRows);
         for (int i = 0; i < nbRows; i++) {
        	 scheduleGrid.getRowConstraints().add(rowSize);
         }
        
         for (int i = 0; i < dates.size(); i++) {
        	 Label dayLabel = new Label(dates.get(i).format(DateTimeFormatter.ofPattern("EEEE d LLLL", Locale.FRENCH)));
        	 styleDayLabel(dayLabel);
        	 scheduleGrid.add(dayLabel, i+1, 0);
         }         
 
         for (int i = 0; i < hours.size() - 1; i++) {
        	 Label hourLabel = new Label(String.format("%dh - %dh", hours.get(i).getHour(), hours.get(i+1).getHour()));
        	 styleHourLabel(hourLabel);
        	 scheduleGrid.add(hourLabel, 0, i+1);
         }
    
     	styleScheduleGrid(nbCols, nbRows);

    }
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    // STYLING
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    
    // Button
    public static void styleButton(Button button) {
    	button.setStyle(
    		    "-fx-cursor: hand;" );
    }
    
    // Schedule grid
    public static void styleScheduleGrid(int nbCols, int nbRows) {
    	scheduleGrid.setStyle(
    		    "-fx-background-color: white;" +
    			"-fx-border-color: #cccccc;" + 
    		    "-fx-border-width: 2");
    	for (int col = 0; col < nbCols-1; col++) {
    		for (int row = 0; row < nbRows-1; row++) {
    			StackPane cell = new StackPane();
    			cell.setStyle(
    					"-fx-border-color: transparent #cdcdcd #dddddd transparent ;" + 
    					"-fx-border-width: 0 2 1 0;" + 
    					"-fx-padding: 5;");
    			scheduleGrid.add(cell, col, row);
    		}    		
    	}
    	for (int col = 0; col < nbCols-1; col++) {
			StackPane cell = new StackPane();
			cell.setStyle(
					"-fx-border-color: transparent #cdcdcd transparent transparent;" + 
					"-fx-border-width: 0 2 0 0;");
			scheduleGrid.add(cell, col, nbRows-1);
    		
    	}
    	for (int row = 0; row < nbRows-1; row++) {
    		StackPane cell = new StackPane();
			cell.setStyle(
					"-fx-border-color: transparent transparent #dddddd transparent ;" + 
					"-fx-border-width: 0 0 1 0;");
			scheduleGrid.add(cell, nbCols-1, row);
    	}
    	scheduleGrid.setGridLinesVisible(false); 
    }
    

    // Labels 
    public static void styleDayLabel(Label label) {
    	label.setTextAlignment(TextAlignment.CENTER); 
    	label.setAlignment(Pos.CENTER);               
    	label.setStyle(
    			"-fx-font-size: 14px;" +
    			"-fx-font-weight: bold;" );
		GridPane.setHalignment(label, HPos.CENTER);
    }
    
    public static void styleHourLabel(Label label) {
    	label.setTextAlignment(TextAlignment.CENTER); 
    	label.setAlignment(Pos.CENTER);              
    	label.setStyle( 
    			"-fx-font-size: 14px;" +
    			"-fx-font-weight: bold;" );
		GridPane.setHalignment(label, HPos.CENTER);
		GridPane.setValignment(label, VPos.CENTER);
    }

    // Event
    public static void styleEventButton(Button button, int rowSpan) {
    	button.setStyle(
    		    "-fx-background-color: linear-gradient(to bottom, #4a90e2, #0a6bff);" +  
    		    "-fx-background-radius: 6px;" +
    		    "-fx-border-color: #0047ab;" +
    		    "-fx-border-width: 2px;" +
    		    "-fx-border-radius: 6px;" +
    		    "-fx-text-fill: white;" +
    		    "-fx-font-weight: bold;" +
    		    "-fx-cursor: hand;" +
    		    "-fx-padding: 6px;"
    		);
    	GridPane.setMargin(button, new Insets(1));
    	GridPane.setRowSpan(button, rowSpan);
		button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		GridPane.setHgrow(button, Priority.ALWAYS);
		GridPane.setVgrow(button, Priority.ALWAYS);
    }
    
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    // DIALOGS
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    
    // Add Event
    private void addEventDialog() {
    	// Dialog to create a new event
        Stage dialog = new Stage();
        dialog.setTitle("Nouvel événement");

        TextField titleField = new TextField();
        TextArea descField = new TextArea();
        
        ComboBox<LocalDate> dateChoice = new ComboBox<LocalDate>();
        for (int i = 0; i < dates.size(); i++) {
        	dateChoice.getItems().add(dates.get(i));
        }
        
        ComboBox<LocalTime> startChoice = new ComboBox<LocalTime>(); 
        ComboBox<LocalTime> endChoice = new ComboBox<LocalTime>();  
        for (int i = 0; i < hours.size(); i++) {
        	startChoice.getItems().add(hours.get(i));
        	endChoice.getItems().add(hours.get(i));
        }
        
        GridPane timeChoices = new GridPane();
        timeChoices.add(new Label("Jour : "), 0, 0); timeChoices.add(dateChoice, 1, 0);
        timeChoices.add(new Label("Début : "), 0, 1); timeChoices.add(startChoice, 1, 1);
        timeChoices.add(new Label("Fin :"), 0, 2); timeChoices.add(endChoice, 1, 2);
        
        Button saveButton = new Button("Enregistrer");
        styleButton(saveButton);

        saveButton.setOnAction(save -> {
            String title = titleField.getText();
            String description = descField.getText();
            LocalDate date = dateChoice.getValue();
            LocalTime startHour = startChoice.getValue();
            LocalTime endHour = endChoice.getValue();
            
            try { 
            	TimeSlot ts = new TimeSlot(date, startHour, endHour);
            	PlannedEvent event = new PlannedEvent(title, description, ts);
				schedule.addPlannedEvent(event);
				Button eventButton = new Button(title);
				styleEventButton(eventButton, hours.indexOf(endHour) - hours.indexOf(startHour)); 
				eventButton.setUserData(event);
				eventButton.setOnAction(click -> {
					Button source = (Button) click.getSource();
					eventDetailsDialog(source);
				});
	
				scheduleGrid.add(eventButton, dates.indexOf(date) + 1, hours.indexOf(startHour) + 1);
	            dialog.close();
	            
            } catch(IllegalArgumentException e1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Créneau non valide");
				alert.setContentText("Ce créneau n'est pas valide");
				alert.show();
            } catch (UnavailableSlotException e2) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Créneau indisponible");
				alert.setContentText("Ce créneau n'est pas disponible");
				alert.show();
			} 
            
        });

        VBox layout = new VBox(10, new Label("Titre :"), titleField, new Label("Description :"), descField, timeChoices, saveButton);
        layout.setPadding(new Insets(10));

        dialog.setScene(new Scene(layout, 300, 300));
        dialog.show();
    }
    
    
    // Show event details
    public static void eventDetailsDialog(Button buttonEvent) {
        Stage dialog = new Stage();
        dialog.setTitle("Détails de l'évènement");
        PlannedEvent event = (PlannedEvent) buttonEvent.getUserData();
        TimeSlot ts = event.getTimeSlot();
        Label titleLabel = new Label(event.getTitle()); 
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Label timeLabel = new Label("🕒 " + ts.getStartTime() + " - " + ts.getEndTime());
        timeLabel.setStyle("-fx-text-fill: #555; -fx-font-size: 13px;");
        Label descLabel = new Label(event.getDescription());
        descLabel.setStyle("-fx-font-size: 13px;");
        Button delButton = new Button("Supprimer l'évènement");
        delButton.setOnAction(value -> { 
        	if (confirmRemoveEvent(buttonEvent)) {
        		removeEvent(buttonEvent); 
        		dialog.close();
        	}});
        Button editButton = new Button("Modifier l'évènement");
        editButton.setOnAction(value -> {
        	editEventDialog(buttonEvent);
        	dialog.close();
        });
        VBox layout = new VBox(10, titleLabel, timeLabel, descLabel, new HBox(10, delButton, editButton));
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER_LEFT);
        dialog.setScene(new Scene(layout));
        dialog.show();
    }
    
 
    // Edit event
    public static void editEventDialog(Button eventButton) {
    	Stage dialog = new Stage();
        dialog.setTitle("Éditer l'évènement");
        
        PlannedEvent event = (PlannedEvent) eventButton.getUserData();
        TextField titleField = new TextField(event.getTitle());
        TextArea descField = new TextArea(event.getDescription());
        
        ComboBox<LocalDate> dateChoice = new ComboBox<LocalDate>();
        for (int i = 0; i < dates.size(); i++) {
        	dateChoice.getItems().add(dates.get(i));
        }
        dateChoice.setValue(event.getTimeSlot().getDate());
        
        ComboBox<LocalTime> startChoice = new ComboBox<LocalTime>(); 
        ComboBox<LocalTime> endChoice = new ComboBox<LocalTime>();
        for (int i = 0; i < hours.size(); i++) {
        	startChoice.getItems().add(hours.get(i));
        	endChoice.getItems().add(hours.get(i));
        }
        startChoice.setValue(event.getTimeSlot().getStartTime());
        endChoice.setValue(event.getTimeSlot().getEndTime());
        
        GridPane timeChoices = new GridPane();
        timeChoices.add(new Label("Jour : "), 0, 0); timeChoices.add(dateChoice, 1, 0);
        timeChoices.add(new Label("Début : "), 0, 1); timeChoices.add(startChoice, 1, 1);
        timeChoices.add(new Label("Fin :"), 0, 2); timeChoices.add(endChoice, 1, 2);
        
        Button saveButton = new Button("Enregistrer");
        styleButton(saveButton);
        saveButton.setOnAction(save -> {
        	String newTitle = titleField.getText();
        	String newDesc = descField.getText();
        	LocalDate newDate = dateChoice.getValue();
        	LocalTime newStart = startChoice.getValue();
        	LocalTime newEnd = endChoice.getValue();

        	try {
	    		TimeSlot newSlot = new TimeSlot(newDate, newStart, newEnd);
	        	schedule.updateEvent(event, newTitle, newDesc, newSlot);
	        	eventButton.setText(event.getTitle());
	        	GridPane.setColumnIndex(eventButton, dates.indexOf(newDate) + 1);
	        	GridPane.setRowIndex(eventButton, hours.indexOf(newStart) + 1);
	        	GridPane.setRowSpan(eventButton, hours.indexOf(newEnd)-hours.indexOf(newStart));
				dialog.close();
	        } catch(IllegalArgumentException e1) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Créneau non valide");
				alert.setContentText("Ce créneau n'est pas valide");
				alert.show();
	           } catch (UnavailableSlotException e2) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Créneau indisponible");
				alert.setContentText("Ce créneau n'est pas disponible");
				alert.show();
				}
        	});
        
        VBox layout = new VBox(10, new Label("Titre :"), titleField, new Label("Description :"), descField, timeChoices, saveButton);
        layout.setPadding(new javafx.geometry.Insets(10));

        dialog.setScene(new Scene(layout, 300, 300));
        dialog.show();

    }
    
    // Remove event
    public static boolean confirmRemoveEvent(Button buttonEvent) {
    	// Alert before removing an event
    	PlannedEvent event = (PlannedEvent) buttonEvent.getUserData();
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    	alert.setHeaderText("Voulez-vous vraiment supprimer \"" + event.getTitle() + "\" ?");
    	alert.setContentText("Cette action est irréversible");
    	ButtonType oui = new ButtonType("Oui");
    	ButtonType non = new ButtonType("Non");
    	alert.getButtonTypes().setAll(oui, non);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == oui) {
            return true;
        }
        return false;
    }
    
    
    public static void removeEvent(Button buttonEvent) {
	    schedule.removePlannedEvent((PlannedEvent) buttonEvent.getUserData());
	    scheduleGrid.getChildren().remove(buttonEvent);
    }
    
    // Export schedule
    public static void exportScheduleDialog() {
    	Stage dialog = new Stage();
        dialog.setTitle("Exporter le planning");

        TextField filePathField = new TextField();
        TextField titleField = new TextField();
        titleField.setText(String.format("Planning du %s au %s",
        				dates.get(0).format(DateTimeFormatter.ofPattern("dd/MM", Locale.FRENCH)), 
        				dates.get(dates.size()-1).format(DateTimeFormatter.ofPattern("dd/MM ", Locale.FRENCH))));
        Button exportButton = new Button("Exporter");
        exportButton.setOnAction(value -> {
        	try {
        		String filePath = filePathField.getText() + ".html";
;				ScheduleExporter.exportToHTML(schedule, dates, hours, filePath, titleField.getText());
				Desktop.getDesktop().browse(new File(filePath).toURI());
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Échec d'exportation");
				alert.setContentText("Chemin invalide");
				alert.show();
				//e.printStackTrace();
			} catch (IOException e) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Fichier inaccessible");
				alert.setContentText("Impossible d'ouvrir le fichier HTML depuis cette application");
				alert.show();
				//e.printStackTrace();
			}
        	dialog.close();
        	
        });
        VBox layout = new VBox(10, new Label("Chemin du ficher :"), new HBox(5, filePathField, new Label(".html")), new Label("Titre:"), titleField, exportButton);
        HBox.setHgrow(filePathField, Priority.ALWAYS);
        layout.setPadding(new Insets(10));
        dialog.setScene(new Scene(layout, 300, 175));
        dialog.show();
        
    }
    
    
    public static void main(String[] args) {
        launch(args);
    }

}