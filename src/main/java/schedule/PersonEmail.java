package schedule;

import model.database.CRUD.landlord;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
public class PersonEmail {
    private Integer landlordId;
    private ArrayList<Integer> tenants_due;


    public PersonEmail(){
        this.landlordId = landlordId;
        this.tenants_due = tenants_due;
    }

public String getUsername(){
        return new landlord().landlord_username(this.landlordId);
}


public String propertyName(){
        return new landlord().landlord_property_name(this.landlordId);
}


public String landlordEmail(){
        return new landlord().landlordEmail(this.landlordId);
}

public String send_email(){
    StringBuilder email = new StringBuilder();
    ArrayList<Integer> due_units = this.tenants_due;
    email.append("To     :").append(landlordEmail()).append(",\n\n");
    email.append("Subject: Rent Payment Reminder\n\n");
    email.append("Dear ").append(getUsername()).append(",\n\n");
    // If propertyName() returns a String, remove the String.format
    email.append("Property: ").append(propertyName()).append("\n\n");

    for (Integer unit : due_units) {
        email.append("This is a friendly reminder that tenant for Unit ")
                .append(unit)
                .append(" has rent due today.\n");
    }

    email.append("\nA second reminder will be sent this afternoon if you have not logged in yet.\n ")
            .append("MYPROMAN will automatically start counting overdue days until the tenant has paid.\n\n");
    email.append("Thank you for your prompt attention.\n\n");
    email.append("Best regards,\n");
    email.append("PROMAN Team.\n");

    return email.toString();
}
//public String sendSMS(String property_owner_name, String property_contact, ArrayList tenants_due){
//        String jsonBuilder = """
//                {
//                "body" : "proMan Reminder, Hi {F0##}, log in to mark units {F1################} who have paid",
//                 "to":  [
//                      {"address": ${property},"fields": [${property_contact},${tenants_due}] }
//                  ]
//                }
//                """;
//        return jsonBuilder;
//}

//    public String sendSMS(String propertyOwnerName,
//                          String propertyContact,
//                          ArrayList<String> tenantsDue) {
//
//        // Convert tenant list to readable text
//        String tenantsDueText = tenantsDue.stream()
//                .collect(Collectors.joining(", "));
//
//        String jsonBody = """
//        {
//          "messages": [
//            {
//              "body": "proMan Reminder, Hi {F0#}, log in to mark units {F1} who have paid",
//              "to": [
//                {
//                  "address": "%s",
//                  "fields": ["%s", "%s"]
//                }
//              ]
//            }
//          ]
//        }
//        """.formatted(
//                propertyContact,
//                propertyOwnerName,
//                tenantsDueText
//        );
//
//        return jsonBody;
//    }

    public String sendSMS(String propertyOwnerName,
                          String propertyContact,
                          ArrayList<String> tenantsDue) {

        String tenantsDueText = String.join(", ", tenantsDue);

        return """
    [
      {
        "body": "proMan Reminder, Hi {F0########}, log in to mark units {F1} who have paid",
        "to": [
          {
            "address": "%s",
            "fields": ["%s", "%s"]
          }
        ]
      }
    ]
    """.formatted(
                propertyContact,
                propertyOwnerName,
                tenantsDueText
        );
    }


    public HttpRequest makeRequest(String jsonBody) {

        String tokenId = "54B80C520D704654A5431C8DA1D9CA8D-01-B";       // username
        String tokenSecret = "vmPZOUxRA#Ymw8yLNx29OZD6v_rM6"; // apiKey
        String apiUrl = "https://api.bulksms.com/v1/messages";

        String auth = tokenId + ":" + tokenSecret;
        String encodedAuth = Base64.getEncoder()
                .encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        return HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Basic " + encodedAuth)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
    }


    public static void main(String[] args) throws Exception {

        ArrayList<String> tenants = new ArrayList<>();
        tenants.add("A1");
        tenants.add("B5");

        PersonEmail sms = new PersonEmail();

        String json = sms.sendSMS(
                "Khulekani",
                "+27826690384",
                tenants
        );

        HttpRequest request = sms.makeRequest(json);

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        System.out.println("Response: " + response.body());
    }

}
