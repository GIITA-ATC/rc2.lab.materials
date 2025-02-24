# Lab 1 - Java.net Library

This repository contains the exercises for **Lab 1** of the Computer Networks course. The exercises involve working with Java's networking libraries to retrieve information about the local machine's network configuration.

## Exercises

### **Exercise 1: `MyAddresses.java`**
This program retrieves and displays the host's name, canonical host name, and all associated IP addresses. It uses the `InetAddress` class to obtain this information.

### **Exercise 2a: `MyInterfaces.java`**
This program lists all available network interfaces on the machine, including their associated IP addresses. It uses the `NetworkInterface` class and enumerates through each active interface.

### **Exercise 2b: `MyInterfacesIPv4.java`**
This program is a modified version of Exercise 2a but filters out only IPv4 addresses. It ensures that only IPv4 addresses are displayed instead of both IPv4 and IPv6.

## Makefile

A `Makefile` is provided to facilitate compilation and cleanup:

- **`make compile`** → Compiles all Java files.
- **`make clean`** → Removes all compiled `.class` files.
- **`make`** → Default target; runs `make compile`.

To run a specific exercise after compilation, use:
```sh
java MyAddresses
java MyInterfaces
java MyInterfacesIPv4
```

Remember you can also compile a specific exercise with `javac <filename>.java`.
