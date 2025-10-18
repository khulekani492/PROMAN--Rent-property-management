package API;
import com.mailersend.sdk.Recipient;
import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.exceptions.MailerSendException;

public static class khuleDevEmailAPI {

    public void sendEmail() {

        Email email = new Email();

        email.setFrom("name", "khzondojhb024@student.wethinkcode.co.za");
        email.addRecipient("name", "khulekaniszondo6@gmail.com");

        // you can also add multiple recipients by calling addRecipient again
//        email.addRecipient("name 2", "your@recipient2.com");

        // there's also a recipient object you can use
        Recipient recipient = new Recipient("name", "your@recipient3.com");
        email.AddRecipient(recipient);

        email.setSubject("Email subject");

        email.setPlain("This is the text content");
        email.setHtml("<p>This is the HTML content</p>");

        MailerSend ms = new MailerSend();

        ms.setToken("mlsn.30bb80be32bbb66074037b8bce49b75b00b32aff5f222c20735347daba8ab58c");

        try {
            MailerSendResponse response = ms.emails().send(email);
            System.out.println(response.messageId);
        } catch (MailerSendException e) {
            e.printStackTrace();
        }
    }


    }
    public static void main(String[] args) {
        khuleDevEmailAPI test_email = new khuleDevEmailAPI();
        test_email.sendEmail();
    }
