# Pet Healthcare AI Assistant

A Spring Boot REST API application that provides AI-powered pet healthcare advice using Spring AI and OpenAI.

## Features

- **AI-Powered Responses**: Uses OpenAI's GPT models to provide professional veterinary advice
- **Conversation Memory**: Maintains conversation history within sessions for context-aware responses
- **Question Logging**: All questions and answers are logged and stored in an H2 database
- **REST API**: Clean JSON API endpoints for easy integration
- **Session Management**: Support for multiple conversation sessions
- **Professional Guidelines**: AI is configured with veterinary assistant guidelines

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- OpenAI API Key

## Setup

1. **Get OpenAI API Key**
   - Sign up at [OpenAI](https://platform.openai.com/)
   - Generate an API key
   - Set it as an environment variable:
     ```bash
     export OPENAI_API_KEY=your-api-key-here
     ```

2. **Build and Run**
   ```bash
   cd pet-healthcare-ai
   mvn clean install
   mvn spring-boot:run
   ```

3. **Access the Application**
   - API Base URL: http://localhost:8080/api/pet-health
   - H2 Database Console: http://localhost:8080/h2-console
     - JDBC URL: `jdbc:h2:mem:testdb`
     - Username: `sa`
     - Password: `password`

## API Endpoints

### Ask a Question
```http
POST /api/pet-health/ask
Content-Type: application/json

{
    "question": "My dog has been limping. What should I do?",
    "sessionId": "optional-session-id"
}
```

**Response:**
```json
{
    "success": true,
    "data": {
        "id": 1,
        "question": "My dog has been limping. What should I do?",
        "answer": "I understand your concern about your dog's limping...",
        "timestamp": "2024-01-15T10:30:00",
        "sessionId": "session-123",
        "conversationHistory": [...]
    },
    "message": "Question processed successfully"
}
```

### Get Conversation History
```http
GET /api/pet-health/history/{sessionId}
```

### Get All Questions
```http
GET /api/pet-health/all
```

### Health Check
```http
GET /api/pet-health/health
```

## Example Usage

1. **Ask your first question:**
   ```bash
   curl -X POST http://localhost:8080/api/pet-health/ask \
     -H "Content-Type: application/json" \
     -d '{"question": "How often should I feed my puppy?", "sessionId": "my-session"}'
   ```

2. **Continue the conversation:**
   ```bash
   curl -X POST http://localhost:8080/api/pet-health/ask \
     -H "Content-Type: application/json" \
     -d '{"question": "What about treats?", "sessionId": "my-session"}'
   ```

3. **View conversation history:**
   ```bash
   curl http://localhost:8080/api/pet-health/history/my-session
   ```

## Configuration

The application can be configured via `application.yml`:

- **OpenAI API Key**: Set via environment variable `OPENAI_API_KEY`
- **Model**: Default is `gpt-3.5-turbo`
- **Temperature**: Default is `0.7` for balanced creativity
- **Database**: Uses H2 in-memory database for development

## Important Notes

- **Not a Replacement for Veterinary Care**: The AI provides educational information only
- **Session Management**: Use sessionId to maintain conversation context
- **Rate Limits**: Be aware of OpenAI API rate limits
- **Data Storage**: Questions and answers are stored locally in H2 database

## Development

- **Logging**: Detailed logging is enabled for debugging
- **Error Handling**: Comprehensive error handling with proper HTTP status codes
- **Validation**: Input validation for API requests
- **Database**: H2 console available for database inspection

## Technologies Used

- Spring Boot 3.2.0
- Spring AI 1.0.0-M1
- Spring Data JPA
- H2 Database
- OpenAI GPT-3.5-turbo
- Maven