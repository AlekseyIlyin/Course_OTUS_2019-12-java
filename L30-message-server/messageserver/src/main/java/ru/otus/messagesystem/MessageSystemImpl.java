package ru.otus.messagesystem;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public final class MessageSystemImpl implements MessageSystem {
    private static final int MESSAGE_QUEUE_SIZE = 1_000;
    private static final int MSG_HANDLER_THREAD_LIMIT = 2;
    private static final int PORT = 53333; //8080;

    private final Gson gson = new Gson();
    private final AtomicBoolean runFlag = new AtomicBoolean(true);
    private final Map<UUID, Client> clientMapDb = new ConcurrentHashMap<>();
    private final Map<UUID, Client> clientMapFront = new ConcurrentHashMap<>();
    private final BlockingQueue<Message> messageQueue = new ArrayBlockingQueue<>(MESSAGE_QUEUE_SIZE);

    private final ExecutorService msgProcessor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setName("msg-processor-thread");
        return thread;
    });

    private final ExecutorService msgHandler = Executors.newFixedThreadPool(MSG_HANDLER_THREAD_LIMIT, new ThreadFactory() {
        private final AtomicInteger threadNameSeq = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName("msg-handler-thread-" + threadNameSeq.incrementAndGet());
            return thread;
        }
    });

    public MessageSystemImpl() {
        msgProcessor.submit(this::msgProcessor);
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Server error");
        }

        Socket s;
        while (true) {
            try {
                s = serverSocket.accept();
                log.info("connection Established");
                Client st = new Client(s);
                st.start();

            } catch (Exception e) {
                e.printStackTrace();
                log.error("Connection Error");
            }
        }
    }

    private Map<UUID, Client> getMapClient(String recipientType) {

        switch (recipientType) {

            case "frontendService" : {
                return clientMapFront;
            }

            case "databaseService" : {
                return clientMapDb;
            }

            default: {
                log.error("unknown client type {}", recipientType);
                return new ConcurrentHashMap<>();
            }
        }

    }

    private void msgProcessor() {
        log.info("msgProcessor started");
        while (runFlag.get()) {
            try {
                Message msg = messageQueue.take();
                if (msg == Message.VOID_MESSAGE) {
                    log.info("received the stop message");
                } else {
                    Map<UUID,Client> clientMap = getMapClient(msg.getToClientType());
                    Client clientTo = null;
                    if (msg.getToClientId() == null) {
                        if (clientMap.isEmpty()) {
                            log.warn("client type {} with id recipient {} not found, and map doesn't content client this type", msg.getToClientType(), msg.getToClientId());
                        } else {
                            clientTo = (Client) clientMap.values().toArray()[0];
                            msg.setToClientId((UUID) clientMap.keySet().toArray()[0]);
                        }
                    } else {
                        clientTo = clientMap.get(msg.getToClientId());
                    }
                    MessageSystemImpl.Client finalClientTo = clientTo;
                    msgHandler.submit(() -> handleMessage(finalClientTo, msg));
                }
            } catch (InterruptedException ex) {
                log.error(ex.getMessage(), ex);
                Thread.currentThread().interrupt();
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }
        }
        msgHandler.submit(this::messageHandlerShutdown);
        log.info("msgProcessor finished");
    }

    private void messageHandlerShutdown() {
        msgHandler.shutdown();
        log.info("msgHandler has been shut down");
    }

    private void handleMessage(Client msClient, Message msg) {
        msClient.sendMessage(msg);
    }

    private void insertStopMessage() throws InterruptedException {
        boolean result = messageQueue.offer(Message.VOID_MESSAGE);
        while (!result) {
            Thread.sleep(100);
            result = messageQueue.offer(Message.VOID_MESSAGE);
        }
    }

    @Override
    public void addClient(Client client, String clientType, UUID clientId) {
        log.info("new client type: {} with id: {}", clientType, clientId );

        val clientMap = getMapClient(clientType);

        if (clientMap.containsKey(clientId)) {
            throw new IllegalArgumentException("Error. client: " + clientType + " with Id " + clientId + " already exists");
        }
        clientMap.put(clientId, client);
    }

    @Override
    public void removeClient(String clientType, UUID clientId) {
        val clientMap = getMapClient(clientType);
        Client removedClient = clientMap.remove(clientId);
        if (removedClient == null) {
            log.warn("client not found: ", clientId);
        } else {
            log.info("removed client:", removedClient);
        }
    }

    @Override
    public boolean newMessage(Message msg) {
        if (runFlag.get()) {
            return messageQueue.offer(msg);
        } else {
            log.warn("MS is being shutting down... rejected:{}", msg);
            return false;
        }
    }

    @Override
    public void dispose() throws InterruptedException {
        runFlag.set(false);
        insertStopMessage();
        msgProcessor.shutdown();
        msgHandler.awaitTermination(60, TimeUnit.SECONDS);
    }

    class Client extends Thread {
        private final Socket s;
        String line = null;
        BufferedReader is = null;
        PrintWriter os = null;

        public Client(Socket s) {
            this.s = s;
        }

        public void run() {
            try {
                is = new BufferedReader(new InputStreamReader(s.getInputStream()));
                os = new PrintWriter(s.getOutputStream(), true);
            } catch (IOException e) {
                log.error("IO error in server thread ", e);
            }
            try {

                boolean cont = true;
                while (cont) {
                    //is.ready()
                    line = is.readLine();

                    if (line != null) {
                        final var message = gson.fromJson(line, Message.class);
                        final var messageType = MessageType.getTypeMessageFromValue(message.getType());

                        switch (messageType) {
                            case CONNECT: {
                                addClient(this, message.getFromClientType(), message.getFromClientId());
                                break;
                            }
                            case STOP: {
                                cont = false;
                                break;
                            }
                            default: {
                                newMessage(message);
                            }
                        }
                    }

                }

            } catch (IOException e) {
                log.error("IO Error/ Client terminated abruptly", this.getName());
            } catch (NullPointerException e) {
                log.error("Client Closed", this.getName());
            } finally {
                try {
                    log.info("Connection Closing..");
                    if (is != null) {
                        is.close();
                        log.info(" Socket Input Stream Closed");
                    }

                    if (os != null) {
                        os.close();
                        log.info("Socket Out Closed");
                    }
                    if (s != null) {
                        s.close();
                        log.info("Socket Closed");
                    }

                } catch (IOException ie) {
                    log.error("Socket Close Error");
                }
            }//end finally
        }

        public void sendMessage(Message msg) {
            os.println(gson.toJson(msg));
            os.flush();
        }
    }

}
