//Request handlers
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.URLEncoder;

//Character set for proper request handling
import java.nio.charset.StandardCharsets;

//JSON handlers
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

//Exception/s (Errors)
import java.io.IOException;

//Design and layout imports/handlers
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

//GUI components
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;

class App extends JFrame implements ActionListener {
     //Initializations and declarations

     //Containers
     JPanel inputPanel = new JPanel();
     JPanel outputPanel = new JPanel();

     //Input components
     JTextField cityInput;
     JButton submitBtn;

     //Output components
     JLabel cityLbl;
     JLabel tempLbl;
     JLabel humLbl;
     JLabel weatherLbl;
     JLabel emojiLbl;

     //Images for different weather conditions
     //NOTE: You can also substitute all of the "images/" part with a variable
     ImageIcon sunnyIcon = new ImageIcon("images/sunny.png");
     ImageIcon overcastIcon = new ImageIcon("images/overcast-clouds.png");
     ImageIcon cloudsIcon = new ImageIcon("images/clouds.png");
     ImageIcon snowIcon = new ImageIcon("images/snow.png");
     ImageIcon fogIcon = new ImageIcon("images/fog.png");
     ImageIcon drizzleIcon = new ImageIcon("images/drizzle.png");
     ImageIcon rainIcon = new ImageIcon("images/rain.png");
     ImageIcon thunderIcon = new ImageIcon("images/thunder.png");
     ImageIcon unknownIcon = new ImageIcon("images/unknown.png");

     ImageIcon errorIcon = new ImageIcon("images/error.png");

     //API key | Enter your open weather api key
     String apiKey = "";

     App() {
          //Initializing the GUI window
          this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          this.setSize(new Dimension(700, 800));
          this.setTitle("Weather App");
          this.setResizable(false);
          this.setLayout(new BorderLayout(0, 20));

          //Initializes components
          initializeComp();

          this.setVisible(true);
     }

     void initializeComp() {
          //Input components
          cityInput = new JTextField();
          cityInput.setFont(new Font("", Font.BOLD, 40));
          cityInput.setPreferredSize(new Dimension(450, 70));
          inputPanel.add(cityInput);

          submitBtn = new JButton("Submit");
          submitBtn.setFocusable(false);
          submitBtn.setFont(new Font("", Font.BOLD, 30));
          submitBtn.setPreferredSize(new Dimension(150, 70));
          submitBtn.addActionListener(this);

          inputPanel.add(submitBtn);

          //Output components
          outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));

          cityLbl = new JLabel("Enter a city");
          cityLbl.setFont(new Font("", Font.BOLD, 50));
          cityLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
          outputPanel.add(cityLbl);

          //NOTE: This part adds vertical gaps
          outputPanel.add(Box.createVerticalStrut(20)); 

          tempLbl = new JLabel();
          tempLbl.setFont(new Font("", Font.ITALIC, 50));
          tempLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
          outputPanel.add(tempLbl);

          outputPanel.add(Box.createVerticalStrut(20)); 

          emojiLbl = new JLabel();
          emojiLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
          outputPanel.add(emojiLbl);

          outputPanel.add(Box.createVerticalStrut(20));

          weatherLbl = new JLabel();
          weatherLbl.setFont(new Font("", Font.BOLD | Font.ITALIC, 50));
          weatherLbl.setAlignmentX(CENTER_ALIGNMENT);
          outputPanel.add(weatherLbl);

          humLbl = new JLabel();
          humLbl.setFont(new Font("", Font.BOLD | Font.ITALIC, 40));
          humLbl.setAlignmentX(CENTER_ALIGNMENT);
          outputPanel.add(humLbl);

          this.add(inputPanel, BorderLayout.NORTH);
          this.add(outputPanel, BorderLayout.CENTER);
     }

     void displayData() {
          try {
               //Retrieves the JSONObject | JSON data
               JSONObject data = getWeatherData(cityInput.getText());

               //Get's the "main" parent object from the data
               JSONObject main = (JSONObject) data.get("main");

               //Retrieves "main"'s Child object values'
               long humidity = (long) main.get("humidity");
               double temp = (double) main.get("temp");
               temp -= 273.15; //Changes Kelvin to Celsius

               //Retrieves the "weather" array
               JSONArray weatherArr = (JSONArray) data.get("weather");
               //Retrieves the parent object in it
               JSONObject weather = (JSONObject) weatherArr.get(0);

               //Retrieves data from parent object
               String desc = (String) weather.get("description");
               //Retrieves name
               String cityName = (String) data.get("name");
               //Retrieves weatherId [Necessary for finding the proper image to represent the weather]
               long weatherId = (long) weather.get("id");

               cityLbl.setText(cityName);
               weatherLbl.setText(desc); 
               tempLbl.setText(String.format("%.2f", temp) + "°C"); // "°" = \u00B0 [Unicode]
               humLbl.setText("Hum: " + String.valueOf(humidity) + "%");

               emojiLbl.setIcon(displayIcon(weatherId));
          }
          catch(Exception e) {
               displayError("Failed to retrieve data");
          }
     }

     JSONObject getWeatherData(String cityName) throws IOException, InterruptedException {
          //Encodes the city name properly adjusting the spaces for a proper URL
          String encodedCityName = URLEncoder.encode(cityName, StandardCharsets.UTF_8.toString());
          String url = "https://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=" + apiKey;

          //Creates a new HTTP client
          HttpClient client = HttpClient.newHttpClient();
          URI uri = URI.create(url);

          //Creates a new request
          HttpRequest request = HttpRequest.newBuilder().uri(uri).build();

          //Sends the request and retrieves the response
          HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

          //Retrieves the data in a JSON format
          JSONObject json = convertToJSON(response.body());

          return json;
     }

     JSONObject convertToJSON(String response) {
          //Creates a new JSONObject
          JSONObject jsonObject = new JSONObject();

          try {
               //Parses the object with a JSONParser
               JSONParser parser = new JSONParser();
               jsonObject = (JSONObject) parser.parse(response);
               
          }
          catch(Exception e) {
               displayError("Failed to parse data");
          }

          return jsonObject;
     }

     ImageIcon displayIcon(long id) {
          if(id >= 200 && id < 300) {
               return thunderIcon;
          }
          else if(id >= 300 && id < 400) {
               return drizzleIcon;
          }
          else if(id >= 500 && id < 600) {
               return rainIcon;
          }
          else if(id >= 600 && id < 700) {
               return snowIcon;
          }
          else if(id >= 700 && id < 800) {
               return fogIcon;
          }
          else if(id == 800) {
               return sunnyIcon;
          }
          else if(id >= 801 && id < 804) {
               return cloudsIcon;
          }
          else if(id == 804) {
               return overcastIcon;
          }
          else {
               return unknownIcon;
          }
     }

     @Override
     public void actionPerformed(ActionEvent event) {
          Object source = event.getSource();

          if(source == submitBtn) {
               displayData();
          }
     }

     void displayError(String msg) {
          //Clears all text
          weatherLbl.setText("");
          tempLbl.setText("");
          humLbl.setText("");

          emojiLbl.setIcon(errorIcon);
          cityLbl.setText(msg);
     }
}