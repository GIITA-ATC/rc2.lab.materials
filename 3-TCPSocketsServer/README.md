# Lab 3 - Server Sockets with Java

This repository contains the solutions for **Lab 3** of the Computer Networks II course. The exercises involve creating servers and clients using Java's networking libraries, where each exercise focuses on handling sockets, multithreading, and string manipulation over the network.

## Exercises

### **Exercise 1: `EchoServer.java`**
This program implements a simple **Echo Server**. The server listens for incoming connections on a specified port. It reads the data sent by the client, then sends the same data back to the client, effectively echoing the client's input.


### **Exercise 2: `EchoServerThread.java`**
This program implements a **Multithreaded Echo Server**. The server listens for incoming connections and, for each client that connects, it spawns a new thread (via the `EchoClientHandler` class) to handle the communication. The client sends data, which is then echoed back by the server using the thread.


### **Exercise 3: `ChangerServer.java` & `ChangerClient.java`**
This program implements a **Changer Server** and a corresponding **Changer Client**. The server listens for incoming client connections and changes the case of the letters in the received messages—converting uppercase letters to lowercase and vice versa. The client sends messages to the server and prints the transformed text received from the server. The client will continue sending messages until the user types `"END"` to exit.


## Running the Exercises

### Compilation

To compile a specific exercise, navigate to the directory containing the `.java` file and use the following command:

```bash
javac <filename>.java
```

### Running the programs

After compiling the file, run the server with the following command:

```bash
java <server_filename> [args]
```
