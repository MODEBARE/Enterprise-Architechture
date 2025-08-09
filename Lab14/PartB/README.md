# Product Catalog AI - Prompt Stuffing Example

A simple SpringBoot application demonstrating **prompt stuffing** technique where product data from the database is injected into the AI prompt to answer user questions.

## How It Works

1. **Database**: Products are stored in H2 database
2. **Prompt Stuffing**: When a user asks a question, all product data is retrieved from the database and "stuffed" into the system prompt
3. **AI Response**: The AI answers based on the product data provided in the prompt

## Quick Start

1. **Set OpenAI API Key**:
   ```bash
   export OPENAI_API_KEY=your-api-key-here
   ```

2. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

3. **Application runs on**: http://localhost:8081

## API Endpoints

### Ask Questions About Products
```http
POST /api/products/chat?message=your-question-here
```

**Example**:
```bash
curl -X POST "http://localhost:8081/api/products/chat?message=What phones do you have?"
```

**Response**:
```json
{
  "question": "What phones do you have?",
  "answer": "Based on our product catalog, we have two smartphones available: 1. iPhone 15 Pro by Apple - $999.99...",
  "totalProducts": 15
}
```

### View All Products
```http
GET /api/products/all
```

## Sample Questions You Can Ask

- "What phones do you have?"
- "Show me laptops under $1200"
- "What's the cheapest product you have?"
- "Do you have any Nike products?"
- "What books are available?"
- "Compare the iPhone and Samsung phone"
- "What's in the Electronics category?"

## Database

- **H2 Console**: http://localhost:8081/h2-console
- **JDBC URL**: `jdbc:h2:mem:productdb`
- **Username**: `sa`
- **Password**: `password`

## Sample Products Included

The application comes pre-loaded with 15 sample products across categories:
- Electronics (phones, laptops)
- Clothing (jeans, t-shirts, sneakers)
- Home & Garden (coffee maker, air purifier)
- Books
- Sports equipment
- Food & Beverages

## Key Files

- `ProductController.java` - Main controller with prompt stuffing logic
- `Product.java` - Product entity
- `DataInitializer.java` - Seeds database with sample products
- `ProductRepository.java` - JPA repository for products

## How Prompt Stuffing Works

```java
// 1. Get all products from database
List<Product> products = productRepository.findAll();

// 2. Build product content string
String productContent = buildProductContent(products);

// 3. Create system prompt with product data
String systemPrompt = "You are a helpful assistant. Here's our catalog: " + productContent;

// 4. Send to AI with user question
String response = chatClient.prompt()
    .system(systemPrompt)
    .user(message)
    .call()
    .content();
```

This technique ensures the AI has access to current product data and can provide accurate, up-to-date responses.