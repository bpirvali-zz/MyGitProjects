package com.intuit.activemq.tests;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.protobuf.compiler.CommandLineSupport;
import org.apache.activemq.util.IndentPrinter;

/**
 * A simple tool for publishing messages
 * 
 * 
 */
public class ProducerTool extends Thread {

    private Destination destination;
    private int messageCount = 10;
    private long sleepTime;
    @SuppressWarnings("unused")
	private long start;
    private boolean verbose = true;
    private int messageSize = 255;
    private int startIndex = 0;
    private static int parallelThreads = 1;
    private long timeToLive;
    private String user = ActiveMQConnection.DEFAULT_USER;
    private String password = ActiveMQConnection.DEFAULT_PASSWORD;
    private String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private String subject = "TOOL.DEFAULT";
    private boolean topic;
    @SuppressWarnings("unused")
    private String inputFile = null;
    private boolean transacted;
    private boolean persistent;
    private static Object lockResults = new Object();
    @SuppressWarnings("unused")
    private FileInputStream inp = null;


    public static void main(String[] args) {
	long startTime = 0;
        ArrayList<ProducerTool> threads = new ArrayList<ProducerTool>();
        ProducerTool producerTool = new ProducerTool();
        String[] unknown = CommandLineSupport.setOptions(producerTool, args);
        if (unknown.length > 0) {
            System.out.println("Unknown options: " + Arrays.toString(unknown));
            System.exit(-1);
        }
        startTime = System.currentTimeMillis();
        producerTool.showParameters();
        for (int threadCount = 1; threadCount <= parallelThreads; threadCount++) {
            producerTool = new ProducerTool();
            producerTool.start = System.currentTimeMillis();
            CommandLineSupport.setOptions(producerTool, args);
            producerTool.start();
            threads.add(producerTool);
        }

        while (true) {
            Iterator<ProducerTool> itr = threads.iterator();
            int running = 0;
            while (itr.hasNext()) {
                ProducerTool thread = itr.next();
                if (thread.isAlive()) {
                    running++;
                }
            }
            if (running <= 0) {
                System.out.println("All threads completed their work");
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
		System.out.println("Elapsed time in ms : " + (System.currentTimeMillis() - startTime));
    }

    public void showParameters() {
        System.out.println("Connecting to URL: " + url);
        System.out.println("Publishing a Message with size " + messageSize + " to " + (topic ? "topic" : "queue") + ": " + subject);
        System.out.println("Using " + (persistent ? "persistent" : "non-persistent") + " messages");
        System.out.println("Sleeping between publish " + sleepTime + " ms");
        System.out.println("Running " + parallelThreads + " parallel threads");

        if (timeToLive != 0) {
            System.out.println("Messages time to live " + timeToLive + " ms");
        }
    }

    public void run() {
        Connection connection = null;
        try {
            // Create the connection.
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);
            connection = connectionFactory.createConnection();
            connection.start();

            // Create the session
            Session session = connection.createSession(transacted, Session.AUTO_ACKNOWLEDGE);
            if (topic) {
                destination = session.createTopic(subject);
            } else {
                destination = session.createQueue(subject);
            }

            // Create the producer.
            MessageProducer producer = session.createProducer(destination);
            if (persistent) {
                producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            } else {
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            }
            if (timeToLive != 0) {
                producer.setTimeToLive(timeToLive);
            }

            // Start sending messages
            sendLoop(session, producer);

            System.out.println("[" + this.getName() + "] Done.");

            synchronized (lockResults) {
                ActiveMQConnection c = (ActiveMQConnection) connection;
                System.out.println("[" + this.getName() + "] Results:\n");
                c.getConnectionStats().dump(new IndentPrinter());
            }

        } catch (Exception e) {
            System.out.println("[" + this.getName() + "] Caught: " + e);
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Throwable ignore) {
            }
        }
    }

    protected void sendLoop(Session session, MessageProducer producer) throws Exception {

        for (int i = 0; i < messageCount || messageCount == 0; i++) {

            TextMessage message = session.createTextMessage(createMessageText(i,true));
		message.setStringProperty("intuit_appid", "Intuit.sbg.payments.prm");
		message.setStringProperty("intuit_offeringid","Intuit.sbg.payments.prm");
		message.setStringProperty("intuit_locale", "en_US");
		message.setStringProperty("intuit_country", "US");
		message.setStringProperty("intuit_SrcDomainEnum", "SRG");
		message.setStringProperty("intuit_MessageChangeType", "INCOMINGBATCHEVENT");

            if (verbose) {
                String msg = message.getText();
                if (msg.length() > 50) {
                    msg = msg.substring(0, 50) + "...";
                }
                System.out.println("[" + this.getName() + "] Sending message: '" + msg + "'");
            }

            producer.send(message);

            if (transacted) {
                System.out.println("[" + this.getName() + "] Committing " + messageCount + " messages");
                session.commit();
            }
            Thread.sleep(sleepTime);
        }
    }

    private String createMessageText(int index, boolean flag) {
        StringBuffer buffer = new StringBuffer(messageSize);
	buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	buffer.append("<IncomingBatchEvent>");
	  buffer.append("<systemId>1</systemId>");
	  buffer.append("<merchantAccountNumber>42669622");
	  buffer.append(String.format("%08d",startIndex+index));
	  buffer.append("</merchantAccountNumber>");
	  buffer.append("<ihubMerchantId>42669622");
	  buffer.append(String.format("%08d",startIndex+index));
	  buffer.append("</ihubMerchantId>");
	  buffer.append("<eventTimeStamp>2013-12-02T00:00:00.000</eventTimeStamp>");
	  buffer.append("<eventCategoryId>0</eventCategoryId>");
	  buffer.append("<eventTypeId>0</eventTypeId>");
	  buffer.append("<eventId>0</eventId>");
	  buffer.append("<eventDetails>Automated Test for : 4266962200012546</eventDetails>");
	  buffer.append("<timeQueued>2013-12-02T00:00:00.000</timeQueued>");
	buffer.append("</IncomingBatchEvent>");
        return buffer.toString();
    }
    
//    private String createMessageText(int index) {
//        StringBuffer buffer = new StringBuffer(messageSize);
//        buffer.append("Message: " + index + " sent at: " + new Date());
//        if (buffer.length() > messageSize) {
//            return buffer.substring(0, messageSize);
//        }
//        for (int i = buffer.length(); i < messageSize; i++) {
//            buffer.append(' ');
//        }
//        return buffer.toString();
//    }

    public void setPersistent(boolean durable) {
        this.persistent = durable;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public void setMessageSize(int messageSize) {
        this.messageSize = messageSize;
    }

    public void setPassword(String pwd) {
        this.password = pwd;
    }

    public void setSleepTime(long sleepTime) {
        this.sleepTime = sleepTime;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public void setParallelThreads(int parallelThreads) {
        if (parallelThreads < 1) {
            parallelThreads = 1;
        }
        ProducerTool.parallelThreads = parallelThreads;
    }

    public void setTopic(boolean topic) {
        this.topic = topic;
    }

    public void setQueue(boolean queue) {
        this.topic = !queue;
    }

    public void setTransacted(boolean transacted) {
        this.transacted = transacted;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }
}
