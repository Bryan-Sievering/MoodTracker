import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class MoodTracker{
    
    public static void main(String[]args){
        ArrayList<Mood> moods = new ArrayList<>();
        Scanner input = new Scanner(System.in);
        
        while(true) {
            System.out.println("Press 'a' to add mood\n" +
                                "'d' to delete mood(s)\n" +
                                "'e' to edit mood\n" +
                                "'s' to search for moods\n" +
                                "'M' to get all moods\n" +
                                "'w' to write the moods to a file\n" +
                                "Type 'Exit' to exit");
            String menuOption = input.nextLine();
            switch(menuOption) {
                case "a": 	addMood(input, moods);
                            continue;
                case "d": 	dMood(input, moods);
                            continue;
                case "e": 	eMood(input, moods);
                            continue;
                case "s": 	sMood(input, moods);
                            continue;
                case "M": 	for(Mood moodObj: moods) {
                    		System.out.println(moodObj);
                			}
                            continue;
                case "w": 	try (PrintWriter writer = new PrintWriter(new FileWriter("Moods.txt"))) {
                    			for (Mood mood : moods) {
                    				writer.println(mood+"\n\n");
                    			}
                    			System.out.println("The entries are written to a file");
                			} catch (IOException e) {
                				System.err.println("Error writing to file: " + e.getMessage());
                			}
                            continue;
                case "Exit": 	System.out.println("Thank you for using the MoodTracker. Goodbye!");
                                break;
                default: 	System.out.println("Not a valid input!");
                            continue;
            }
        }
    }

    public static boolean isMoodValid(Mood mood, ArrayList<Mood> moods) throws InvalidMoodException {
        for(Mood tempMood: moods) {
            if (tempMood.equals(mood)) {
                throw new InvalidMoodException();
            }
        }
        return true;
    }

    public static void addMood(Scanner input, ArrayList<Mood> moods){
        System.out.println("Enter the mood name");
        String moodName = input.nextLine();
        System.out.println("Are you tracking the mood for a current day? y/n");
        String isForCurrentDate = input.nextLine();
        Mood moodToAdd = null;
        if(isForCurrentDate.equalsIgnoreCase("n")) {
            try {
                System.out.println("Input the date in MM/dd/yyyy format:");
                String moodDateStr = input.nextLine();
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate moodDate = LocalDate.parse(moodDateStr, dateFormatter);
                System.out.println("Input the time in HH:mm:ss format:");
                String moodTimeStr = input.nextLine();
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                LocalTime moodTime = LocalTime.parse(moodTimeStr, timeFormatter);
                System.out.println("Add notes about this mood");
                String moodNotes = input.nextLine();
                if(moodNotes.strip().equalsIgnoreCase("")) {
                    moodToAdd = new Mood(moodName, moodDate, moodTime);
                } else {
                    moodToAdd = new Mood(moodName, moodDate, moodTime, moodNotes);
                }
            } catch (DateTimeParseException dfe) {
                System.out.println("Incorrect format of date or time. Cannot create mood.\n"+dfe);
            }
        } else {
            System.out.println("Add notes about this mood");
            String moodNotes = input.nextLine();
            if(moodNotes.strip().equalsIgnoreCase("")) {
                moodToAdd = new Mood(moodName);
            } else {
                moodToAdd = new Mood(moodName, moodNotes);
            }
        }
        try {
            boolean isValid = isMoodValid(moodToAdd, moods);
            if(isValid) {
                moods.add(moodToAdd);
                System.out.println("The mood has been added to the tracker");
            }
        } catch(InvalidMoodException ime) {
            System.out.println("The mood is not valid");
        }
    }

    public static boolean deleteMoods(LocalDate moodDate, ArrayList<Mood> moods) {
        boolean removed = false;
        var iterator = moods.iterator();
        while (iterator.hasNext()) {
            Mood tempMood = iterator.next();
            if (tempMood.getDate().equals(moodDate)) {
                iterator.remove();
                removed = true;
            }
        }
        return removed;
    }

    public static boolean deleteMood(Mood mood, ArrayList<Mood> moods) {
        for(Mood tempMood: moods) {
            if (tempMood.equals(mood)) {
                moods.remove(tempMood);
                return true;
            }
        }
        return false;
    }

    public static void dMood(Scanner input, ArrayList<Mood> moods){
        System.out.println("Enter '1' to delete all moods by date\n"+
                    "Enter '2' to delete a specific mood");
        String deleteVariant = input.nextLine();
        if(deleteVariant.equals("1")) {
        	try {
        		System.out.println("Input the date in MM/dd/yyyy format:");
        		String moodDateStr = input.nextLine();
        		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        		LocalDate moodDate = LocalDate.parse(moodDateStr, dateFormatter);
        		boolean areMoodsDeleted = deleteMoods(moodDate, moods);
        		if(areMoodsDeleted) {
        			System.out.println("The moods have been deleted");
        		} else {
        			System.out.println("No matching moods found");
        		}
        	} catch (DateTimeParseException dfe) {
        		System.out.println("Incorrect format of date. Cannot delete mood.");
        	}
        } else if (deleteVariant.equals("2")) {
        	try {
        		System.out.println("Enter the mood name");
        		String moodName = input.nextLine();
        		System.out.println("Input the date in MM/dd/yyyy format:");
        		String moodDateStr = input.nextLine();
        		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        		LocalDate moodDate = LocalDate.parse(moodDateStr, dateFormatter);
        		System.out.println("Input the time in HH:mm:ss format:");
        		String moodTimeStr = input.nextLine();
        		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        		LocalTime moodTime = LocalTime.parse(moodTimeStr, timeFormatter);
        		Mood delMood = new Mood(moodName, moodDate, moodTime);
        		boolean isMoodDeleted = deleteMood(delMood, moods);
        		if(isMoodDeleted) {
        			System.out.println("The mood has been deleted");
        		} else {
        			System.out.println("No matching mood found");
        		}
        	} catch (DateTimeParseException dfe) {
        		System.out.println("Incorrect format of date or time. Cannot delete mood.");
        	}
        }
    }
    
    public static boolean editMood(Mood moodToEdit, ArrayList<Mood> moods) {
        for(Mood tempMood: moods) {
            if (tempMood.equals(moodToEdit)) {
                tempMood.setNotes(moodToEdit.getNotes());
                return true;
            }
        }
        return false;
    }
    
    public static void eMood(Scanner input, ArrayList<Mood> moods) {
    	Mood moodToEdit = null;
    	try {
    	    System.out.println("Enter the mood name");
    	    String moodName = input.nextLine();
    	    System.out.println("Input the date in MM/dd/yyyy format:");
    	    String moodDateStr = input.nextLine();
    	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    	    LocalDate moodDate = LocalDate.parse(moodDateStr, dateFormatter);
    	    System.out.println("Input the time in HH:mm:ss format:");
    	    String moodTimeStr = input.nextLine();
    	    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    	    LocalTime moodTime = LocalTime.parse(moodTimeStr, timeFormatter);
    	    System.out.println("Add new notes about this mood");
    	    String moodNotes = input.nextLine();
    	    if(moodNotes.strip().equalsIgnoreCase("")) {
    	        System.out.println("No notes entered");
    	    } else {
    	        moodToEdit = new Mood(moodName, moodDate, moodTime, moodNotes);
    	        boolean isMoodEdited = editMood(moodToEdit, moods);
    	        if(isMoodEdited) {
    	            System.out.println("The mood has been successfully edited");
    	        } else {
    	            System.out.println("No matching mood could be found");
    	        }
    	    }
    	} catch (DateTimeParseException dfe) {
    	    System.out.println("Incorrect format of date or time. Cannot create mood.");
    	}
    }
    
    public static void searchMoods(LocalDate moodDate, ArrayList<Mood> moods) {
        boolean found = false;
        for(Mood tempMood: moods) {
            if (tempMood.getDate().equals(moodDate)) {
                found = true;
                System.out.println(tempMood);
            }
        }
        if(!found) {
            System.out.println("No matching records could be found!");
        }
    }
    
    public static void searchMood(Mood mood, ArrayList<Mood> moods) {
        boolean found = false;

        for(Mood tempMood: moods) {
            if (tempMood.equals(mood)) {
                found = true;
                System.out.println(tempMood);
            }
        }
        if(!found) {
            System.out.println("No matching records could be found!");
        }
    }
    
    public static void sMood(Scanner input, ArrayList<Mood> moods) {
    	System.out.println("Enter '1' to search for all moods by date\n"+
                "Enter '2' to search for a specific mood");
    	String searchVariant = input.nextLine();
    	if(searchVariant.equals("1")) {
    		try {
    			System.out.println("Input the date in MM/dd/yyyy format:");
    			String moodDateStr = input.nextLine();
    			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    			LocalDate moodDate = LocalDate.parse(moodDateStr, dateFormatter);
    			searchMoods(moodDate, moods);
    		} catch (DateTimeParseException dfe) {
    			System.out.println("Incorrect format of date. Cannot search mood.");
    		}
    	} else if (searchVariant.equals("2")) {
    		try {
    			System.out.println("Enter the mood name");
    			String moodName = input.nextLine();
    			System.out.println("Input the date in MM/dd/yyyy format:");
    			String moodDateStr = input.nextLine();
    			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    			LocalDate moodDate = LocalDate.parse(moodDateStr, dateFormatter);
    			System.out.println("Input the time in HH:mm:ss format:");
    			String moodTimeStr = input.nextLine();
    			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    			LocalTime moodTime = LocalTime.parse(moodTimeStr, timeFormatter);
    			Mood delMood = new Mood(moodName, moodDate, moodTime);
    			searchMood(delMood, moods);
    		} catch (DateTimeParseException dfe) {
    			System.out.println("Incorrect format of date or time. Cannot search mood.");
    		}
    	}
    }
}