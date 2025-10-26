package API;

import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.Recipient;
import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.exceptions.MailerSendException;

public class khuleDevEmailAPI {

    public void sendEmail() {

        Email email = new Email();

        email.setFrom("name", "khzondojhb024@student.wethinkcode.co.za");
        email.addRecipient("name", "khzondojhb024@student.wethinkcode.co.za ");

        // you can also add multiple recipients by calling addRecipient again
//        email.addRecipient("name 2", "your@recipient2.com");

        // there's also a recipient object you can use
//        Recipient recipient = new Recipient("name", "your@recipient3.com");
//        email.AddRecipient(recipient);

        email.setSubject("Email subject");

        email.setPlain("This is the text content");
        email.setHtml("<p>This is the HTML content</p>");

        MailerSend ms = new MailerSend();

        ms.setToken("mlsn.e900adf7a44ad1c4a816314bf8477b5be8b8930bd64298d8cd8b6ce2f861ee6e");

        try {
            MailerSendResponse response = ms.emails().send(email);
            System.out.println(response.messageId);
        } catch (MailerSendException e) {
            e.printStackTrace();
        }
    }

 static void main(String[] args) {
        khuleDevEmailAPI email = new khuleDevEmailAPI();
        email.sendEmail();
}
    }
