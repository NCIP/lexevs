
package org.lexevs.logging;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.helpers.CyclicBuffer;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;

/**
 * Simple SMTP Client that allows your Java App send emails. Uses Java Sockets
 * to connect directly to an SMTP server. It supports sending of plain text or
 * HTML emails. Bonus: Unlike many other Java SMTP Sockets examples, this one
 * actually works.
 */
// Special thanks to Olly Oechsle, www.intelligent-web.co.uk
// http://www.intelligent-web.co.uk/examples/emailer.java.html
// for permission to use his code as the foundataion of SimpleEmailAppender.java

public class SimpleEmailAppender extends AppenderSkeleton {

    /** The _m_smtp host. */
    private String _m_smtpHost;
    
    /** The _m_subject. */
    private String _m_subject;
    
    /** The _m_from. */
    private String _m_from;
    
    /** The _m_to. */
    private String _m_to;
    
    /** The _m_buffer size. */
    private int _m_bufferSize = 512;
    
    /** The _m_location info. */
    private boolean _m_locationInfo = false;

    /** The _m_evaluator. */
    protected TriggeringEventEvaluator _m_evaluator;
    
    /** The _m_cb. */
    protected CyclicBuffer _m_cb = new CyclicBuffer(_m_bufferSize);

    /**
     * Instantiates a new simple email appender.
     */
    public SimpleEmailAppender() {
        this(new DefaultEvaluator());
    }

    /**
     * Instantiates a new simple email appender.
     * 
     * @param evaluator the evaluator
     */
    public SimpleEmailAppender(TriggeringEventEvaluator evaluator) {
        _m_evaluator = evaluator;
    }

    /**
     * Sets the to.
     * 
     * @param to the new to
     */
    public void setTo(String to) {
        _m_to = to;
    }

    /**
     * Sets the from.
     * 
     * @param from the new from
     */
    public void setFrom(String from) {
        _m_from = from;
    }

    /**
     * Sets the subject.
     * 
     * @param subject the new subject
     */
    public void setSubject(String subject) {
        _m_subject = subject;
    }

    /**
     * Sets the sMTP host.
     * 
     * @param smtpHost the new sMTP host
     */
    public void setSMTPHost(String smtpHost) {
        _m_smtpHost = smtpHost;
    }

    /**
     * Sets the buffer size.
     * 
     * @param bufferSize the new buffer size
     */
    public void setBufferSize(int bufferSize) {
        _m_bufferSize = bufferSize;
        _m_cb.resize(bufferSize);
    }

    /**
     * Sends an email.
     * 
     * @param host the host
     * @param port the port
     * @param to the to
     * @param from the from
     * @param subject the subject
     * @param message the message
     * 
     * @return The full SMTP conversation as a string.
     * 
     * @throws Exception the exception
     */
    public String send(String host, int port, String to, String from, String subject, String message) throws Exception {

        // Save the SMTP conversation into this buffer (for debugging if
        // necessary)
        StringBuffer buffer = new StringBuffer();

        try {

            // Connect to the SMTP server running on the local machine. Usually
            // this is SendMail
            Socket smtpSocket = new Socket(host, port);

            // We send commands TO the server with this
            DataOutputStream output = new DataOutputStream(smtpSocket.getOutputStream());

            // And recieve responses FROM the server with this
            BufferedReader input = new BufferedReader(new InputStreamReader(new DataInputStream(smtpSocket
                    .getInputStream())));

            try {

                // Read the server's hello message
                read(input, buffer);

                // Say hello to the server
                // send(output, "HELO localhost.localdomain\r\n", buffer);
                send(output, "HELO mayo.edu\r\n", buffer);
                read(input, buffer);

                // Who is sending the email
                send(output, "MAIL FROM: " + from + "\r\n", buffer);
                read(input, buffer);

                // Where the mail is going
                send(output, "RCPT to: " + to + "\r\n", buffer);
                read(input, buffer);

                // Start the message
                send(output, "DATA\r\n", buffer);
                read(input, buffer);

                // Set the subject
                send(output, "Subject: " + subject + "\r\n", buffer);

                // If we detect HTML in the message, set the content type so it
                // displays
                // properly in the recipient's email client.
                if (message.indexOf("<") == -1) {
                    send(output, "Content-type: text/plain; charset=\"us-ascii\"\r\n", buffer);
                } else {
                    send(output, "Content-type: text/html; charset=\"us-ascii\"\r\n", buffer);
                }

                // Send the message
                send(output, message, buffer);

                // Finish the message
                send(output, "\r\n.\r\n", buffer);
                read(input, buffer);

                // Close the socket
                smtpSocket.close();

            } catch (IOException e) {
                LogLog.error("Cannot send email as an error occurred.", e);
            }
        } catch (Exception e) {
            LogLog.error("Host unknown", e);
        }
        return buffer.toString();
    }

    /**
     * Sends a message to the server using the DataOutputStream's writeBytes()
     * method. Saves what was sent to the buffer so we can record the
     * conversation.
     * 
     * @param output the output
     * @param data the data
     * @param buffer the buffer
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void send(DataOutputStream output, String data, StringBuffer buffer) throws IOException {
        output.writeBytes(data);
        buffer.append(data);
    }

    /**
     * Reads a line from the server and adds it onto the conversation buffer.
     * 
     * @param br the br
     * @param buffer the buffer
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void read(BufferedReader br, StringBuffer buffer) throws IOException {
        int c;
        while ((c = br.read()) != -1) {
            buffer.append((char) c);
            if (c == '\n') {
                break;
            }
        }
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.AppenderSkeleton#append(org.apache.log4j.spi.LoggingEvent)
     */
    @Override
    protected void append(LoggingEvent event) {
        if (!checkEntryConditions()) {
            return;
        }
        event.getThreadName();
        event.getNDC();
        event.getMDCCopy();
        if (_m_locationInfo) {
            event.getLocationInformation();
        }
        _m_cb.add(event);
        if (_m_evaluator.isTriggeringEvent(event)) {
            String message = (String) event.getMessage();
            try {
                send(_m_smtpHost, 25, _m_to, _m_from, _m_subject, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Check entry conditions.
     * 
     * @return true, if successful
     */
    protected boolean checkEntryConditions() {
        if (this._m_evaluator == null) {
            errorHandler.error("No TriggeringEventEvaluator is set for appender [" + name + "].");
            return false;
        }
        if (this.layout == null) {
            errorHandler.error("No layout set for appender named [" + name + "].");
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.AppenderSkeleton#close()
     */
    synchronized public void close() {
        this.closed = true;
    }

    /* (non-Javadoc)
     * @see org.apache.log4j.AppenderSkeleton#requiresLayout()
     */
    public boolean requiresLayout() {
        return true;
    }

    /**
     * The main method.
     * 
     * @param args the arguments
     * 
     * @throws Exception the exception
     */
    public static void main(String[] args) throws Exception {
        SimpleEmailAppender sea = new SimpleEmailAppender(new EmailTrigger());
        String results = sea.send("server.mayo.edu", // server
                25, // port
                "janedoe@mayo.edu", // to
                "janedoe_nospam@mayo.edu", // from
                "SimpleEmailAppender test", // subject
                "Hello from SimpleEmailAppender."); // message
        System.out.println(results);
    }
}

class DefaultEvaluator implements TriggeringEventEvaluator {
    public boolean isTriggeringEvent(LoggingEvent event) {
        return event.getLevel().isGreaterOrEqual(Level.ERROR);
    }
}