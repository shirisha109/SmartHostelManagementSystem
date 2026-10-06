# Smart Hostel Management System — Web Application

## Problem statement

Hostel administration is difficult to manage with paper records or a terminal-only program. This application provides a browser-based interface for handling students, rooms and complaints from one place.

## Features

- Secure database-backed admin login
- Dashboard with student, room, and complaint statistics
- Student CRUD: add, view, search, update and delete
- Room CRUD and bed allocation/vacating with capacity validation
- Complaint registration, filtering, and status updates
- AI Hostel Assistant and AI complaint analysis
- REST-style Java API between the browser and MySQL

## Technologies and architecture

```text
Browser (HTML, CSS, JavaScript)
        ↓ REST / JSON
Java Web Server / Controller → Service → JDBC DAO → MySQL
        ↓
Gemini Generative AI API (or safe demo mode)
```

The project deliberately uses only Java's built-in HTTP server plus JDBC, keeping it suitable for a BTech project without a complex framework.

## Database setup

1. Start MySQL.
2. Run [database.sql](database.sql) to create `smart_hostel_db` and its tables.
3. Configure the MySQL password in the ignored `.env` file (or in the process environment). The application defaults to the local URL below and the `root` user; the password is read from `MYSQL_PASSWORD` and has no hard-coded default:

```powershell
Add-Content .env 'MYSQL_PASSWORD=your-local-mysql-password'
```

If your MySQL username or URL differs, set `MYSQL_USERNAME` or `MYSQL_URL` in `.env` or the process environment. Never commit `.env`.

Existing tables and data are preserved. Default login after running the schema: `admin` / `admin123`.

## Run the web application

Requires JDK 11+ and the included MySQL JDBC connector.

```powershell
New-Item -ItemType Directory -Force out\web
javac -cp 'lib\mysql-connector-j-8.0.33.jar' -d out\web (Get-ChildItem -Recurse src -Filter *.java | ForEach-Object FullName)
java -cp 'out\web;lib\mysql-connector-j-8.0.33.jar' com.smarthostel.main.Main
```

Open **http://localhost:8080** in a browser. Use `$env:PORT='8081'` before startup to choose another port.

## API endpoints

- `POST /api/login`, `GET /api/dashboard`
- `GET|POST /api/students`, `GET|PUT|DELETE /api/students/{id}`
- `GET|POST /api/rooms`, `PUT|DELETE /api/rooms/{roomNumber}`, `POST /api/rooms/{roomNumber}/allocate`
- `GET|POST /api/complaints`, `PUT|DELETE /api/complaints/{id}`
- `POST /api/ai/chat`, `POST /api/ai/complaint-analysis`

## Generative AI configuration

This project uses the **Google Gemini Generative Language API** with the `gemini-2.0-flash` model. Copy the template and add a Gemini API key before starting the server:

```powershell
Copy-Item .env.example .env
notepad .env
```

Replace `replace_with_your_gemini_api_key` with your actual key, save the file, and restart the Java server. The built-in `.env` loader reads `GENAI_API_KEY` only on backend startup. You can alternatively set a session environment variable with `$env:GENAI_API_KEY='your-key'`; environment variables take priority over `.env`.

The backend, never the browser, calls Gemini's API. When this variable is missing or the AI request fails, the app returns a clearly labelled **demo mode** response so the rest of the application stays usable. Do not commit API keys; `.env` is ignored.

## Future enhancements

Role-based accounts, student self-service, fee management, notifications, and complaint attachments.
