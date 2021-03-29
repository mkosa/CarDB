import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import pojo.Car;
import pojo.CarsOfPeople;
import pojo.EmailTemplate;
import pojo.PersonData;

public class Main {
	public static void main(String[] args) {
		try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/cardb?ApplicationName=CarWebApp", "mkosa", "Marko75")) {
			List<Car> cars = Car.newBuilder().select(conn);
			List<CarsOfPeople> carsOfPeople = CarsOfPeople.newBuilder().select(conn);
			var emailTemplates = EmailTemplate.newBuilder().select(conn);
			var personData = PersonData.newBuilder().select(conn);

			personData.stream().forEach((person) ->
			{
				List<CarsOfPeople> result = carsOfPeople.stream().filter(car -> car.getPersonId().equals(person.getPersonId())).collect(Collectors.toList());
				List<Car> notSent = new ArrayList<>();
				for(var c: cars) {
					if (c.getIsSent() == 0 && c.getCalculatedValue() > 0) {
						notSent.add(c);
					}
				}
				if (notSent.size() > 0) {
					String template = emailTemplates.stream().filter(temp -> temp.getLanguageId().equals(person.getLanguageId())).collect(Collectors.toList()).get(0).getText();
					DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss"); 
					String message = template.replace("<country>", person.getCountry())
							.replace("<dateOfBirth>", dateFormat.format(person.getDataOfBirth()))
							.replace("<name>", person.getName());
					String[] t = message.lines().toArray(String[]::new);

					for (int i = 0; i < 3; ++i) {
						System.out.println(t[i]);
					}
					
					System.out.println(Arrays.copyOfRange(t, 5, t.length));
					for (var item: notSent) {
						for (int i = 0; i < t.length; ++i) {
							String line = t[i].replace("<brand>", item.getBrand());
							System.out.println(line);
						}
					}
				}
			}
			);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
