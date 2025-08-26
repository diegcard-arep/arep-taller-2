package com.escuelaing.arep;

import java.io.IOException;

import static com.escuelaing.arep.WebApp.get;
import static com.escuelaing.arep.WebApp.start;
import static com.escuelaing.arep.WebApp.staticfiles;

/**
 * Example application demonstrating how to use the web framework.
 * This class shows how to define REST services using lambda functions,
 * extract query parameters, and configure static file locations.
 * 
 * The example serves:
 * - http://localhost:35000/App/hello?name=Pedro - Returns personalized greeting
 * - http://localhost:35000/App/pi - Returns the value of PI
 * - http://localhost:35000/index.html - Serves static files from /webroot
 * 
 * @author Diego Cardenas
 * @version 1.0
 */
public class ExampleApp {
    
    /**
     * Main method demonstrating the framework usage.
     * Sets up static file serving and defines REST service endpoints.
     * 
     * @param args command line arguments. First argument can be the port number.
     * @throws IOException if the server fails to start
     */
    public static void main(String[] args) throws IOException {
        // Parse port from command line arguments if provided
        int port = 35000; // default port
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
                if (port < 1 || port > 65535) {
                    System.err.println("Error: Port must be between 1 and 65535");
                    System.exit(1);
                }
            } catch (NumberFormatException e) {
                System.err.println("Error: Invalid port number '" + args[0] + "'. Using default port 35000.");
                port = 35000;
            }
        }
        
        // Set the port in ServerConfig
        com.escuelaing.arep.config.ServerConfig.setPort(port);
        
        // Configure static files location
        staticfiles("/static");
        
        // Define REST services using lambda functions
        get("/hello", (req, resp) -> {
            String name = req.getValues("name");
            if (name.isEmpty()) {
                name = "World";
            }
            return "Hello " + name + "!";
        });
        
        get("/pi", (req, resp) -> {
            return String.valueOf(Math.PI);
        });
        
        // Additional example: parameterized greeting with multiple parameters
        get("/greet", (req, resp) -> {
            String name = req.getValues("name");
            String lang = req.getValues("lang");
            
            if (name.isEmpty()) name = "Guest";
            
            String greeting = switch (lang.toLowerCase()) {
                case "es", "spanish" -> "¡Hola " + name + "!";
                case "fr", "french" -> "Bonjour " + name + "!";
                case "de", "german" -> "Hallo " + name + "!";
                default -> "Hello " + name + "!";
            };
            
            return greeting;
        });
        
        // Example with JSON response
        get("/info", (req, resp) -> {
            resp.type("application/json");
            return String.format("""
                {
                    "framework": "WebApp Framework",
                    "version": "1.0",
                    "author": "Diego Cardenas",
                    "timestamp": "%s",
                    "endpoints": [
                        "/App/hello?name=YourName",
                        "/App/pi",
                        "/App/greet?name=YourName&lang=es",
                        "/App/info"
                    ]
                }
                """, java.time.Instant.now().toString());
        });
        
        // Start the web application
        System.out.println("Starting Example Web Application...");
        System.out.println("Available endpoints:");
        System.out.println("  - http://localhost:" + port + "/App/hello?name=Pedro");
        System.out.println("  - http://localhost:" + port + "/App/pi");
        System.out.println("  - http://localhost:" + port + "/App/greet?name=Maria&lang=es");
        System.out.println("  - http://localhost:" + port + "/App/info");
        System.out.println("  - http://localhost:" + port + "/index.html (static files)");
        System.out.println("");
        
        start();
    }
}