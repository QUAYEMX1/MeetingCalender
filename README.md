# Calendar Assistant Program

## Overview

The Calendar Assistant Program is a RESTful API built using Java Spring Boot that helps users schedule meetings between employees by finding available time slots. The application efficiently manages meetings, identifies conflicts, and suggests free slots based on employee calendars.

## Features

- **Meeting Management**: Create, update, and delete meetings for employees.
- **Free Slot Finder**: Identify available time slots for scheduling meetings of a fixed duration between two employees.
- **Conflict Detection**: Check for existing meetings that might overlap with proposed time slots.
- **REST API**: Fully compliant with REST principles, allowing easy integration with front-end applications.

## Technologies Used

- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database (for development)
- JUnit 5 (for testing)

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6.0 or higher

### Installation

1. Clone the repository:
   ```bash
   [git clone https://github.com/yourusername/calendar-assistant.git](https://github.com/QUAYEMX1/MeetingCalender.git)
   ```

## Troubleshooting

- **Ensure you have the correct Java version**: This application is compatible with Java 17 or higher(21).
- **Check your API key**: Make sure OpenWeatherMap API key is valid and has the necessary permissions.
- **Port conflicts**: If port 8080 is already in use,you can change the port in `application.properties` by adding `server.port`=? (or any other available port e.g:8081).

Developed with ❤️ by **Md Quayem Ashraf**

