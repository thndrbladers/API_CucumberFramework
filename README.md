# API Cucumber Automation Framework

Production-ready API test automation framework built with **Java 17 + Cucumber 7 + RestAssured 5 + Maven**.  
Target API: [JSONPlaceholder](https://jsonplaceholder.typicode.com) (free, no-auth REST API).

---

## Project Structure

```
API_CucumberFramework/
│
├── pom.xml
├── README.md
│
├── src/main/java/com/api/automation/       ← Framework core (reusable, not test-specific)
│   ├── clients/
│   │   ├── ApiClient.java                  ← Generic RestAssured wrapper
│   │   ├── PostApiClient.java             ← Domain client for Posts
│   │   ├── CommentApiClient.java          ← Domain client for Comments
│   │   └── UserApiClient.java            ← Domain client for Users
│   ├── config/
│   │   └── ConfigManager.java             ← Singleton config loader
│   ├── payloads/
│   │   ├── request/
│   │   │   ├── PostRequest.java           ← Post request POJO
│   │   │   ├── CommentRequest.java        ← Comment request POJO
│   │   │   └── UserRequest.java           ← User request POJO
│   │   └── response/
│   │       ├── PostResponse.java          ← Post response POJO
│   │       ├── PostListResponse.java      ← List deserialization helper
│   │       ├── CommentResponse.java       ← Comment response POJO
│   │       └── UserResponse.java          ← User response POJO
│   └── utils/
│       ├── JsonUtils.java                 ← Jackson ObjectMapper wrapper
│       ├── ResponseValidator.java         ← Centralized assertions
│       ├── ExcelReader.java               ← Apache POI Excel reader
│       └── ScenarioContext.java           ← ThreadLocal state sharing
│
├── src/test/java/com/api/automation/       ← Test execution code
│   ├── hooks/
│   │   └── Hooks.java                     ← @Before/@After hooks
│   ├── runners/
│   │   ├── TestRunner.java                ← Main runner (all tests)
│   │   └── SmokeTestRunner.java           ← Smoke-only runner (@smoke)
│   ├── stepdefinitions/
│   │   ├── CommonStepDefinitions.java     ← Shared assertions (status, content-type)
│   │   ├── PostStepDefinitions.java       ← Step defs for Posts API
│   │   ├── CommentStepDefinitions.java    ← Step defs for Comments API
│   │   └── UserStepDefinitions.java       ← Step defs for Users API
│   └── testdata/
│       └── TestDataGenerator.java         ← Generates Excel test data
│
└── src/test/resources/
    ├── config/
    │   ├── dev.properties
    │   ├── qa.properties
    │   └── prod.properties
    ├── features/
    │   ├── posts/                          ← Posts resource (11 scenarios)
    │   │   ├── GetPosts.feature
    │   │   ├── CreatePost.feature
    │   │   ├── UpdatePost.feature
    │   │   ├── DeletePost.feature
    │   │   └── E2EPostFlow.feature
    │   ├── comments/                       ← Comments resource (7 scenarios)
    │   │   ├── GetComments.feature
    │   │   ├── CreateComment.feature
    │   │   └── DeleteComment.feature
    │   ├── users/                          ← Users resource (6 scenarios)
    │   │   ├── GetUsers.feature
    │   │   ├── CreateAndUpdateUsers.feature
    │   │   └── DeleteUser.feature
    │   └── e2e/                            ← Cross-resource E2E (1 scenario)
    │       └── CrossResourceFlow.feature
    ├── testdata/
    │   └── posts.xlsx
    ├── cucumber.properties
    └── log4j2.xml
```

---

## How the Layers Connect

```
Feature Files (.feature)                ← WHAT to test (plain English)
        │
Step Definitions (per resource)         ← GLUE — maps English to Java method calls
        │
Domain Clients (Post/Comment/User)      ← HOW to call the API for a specific resource
        │
Generic Client (ApiClient)             ← HOW to make any HTTP call (RestAssured)
        │
Config (ConfigManager)                  ← WHERE to send requests (base URL, timeouts)
```

Each layer only talks to the one directly below it. This is the key to keeping things maintainable.

---

## Section-by-Section Breakdown

### 1. `clients/` — API Client Layer

**What:** Classes that make HTTP calls.

- **`ApiClient.java`** — A generic wrapper around RestAssured. Provides raw `get()`, `post()`, `put()`, `patch()`, `delete()` methods. Knows nothing about specific endpoints.
- **`PostApiClient.java`** — Domain client for `/posts`. Methods: `createPost()`, `getPostById()`, `updatePost()`, etc.
- **`CommentApiClient.java`** — Domain client for `/comments`. Includes `getCommentsByPostId()` for filtered queries.
- **`UserApiClient.java`** — Domain client for `/users`. Full CRUD operations.

**Why organized this way:**
- **One domain client per API resource.** All CRUD methods for that resource live in one class.
- When your API adds a new resource (e.g., Orders), you create `OrderApiClient.java` — nothing else changes.
- Step definitions never build HTTP requests directly. They just call `postApiClient.createPost(request)`.

**Benefit:** If an endpoint URL changes from `/posts` to `/articles`, you fix it in **one place** — the domain client. Every test that creates a post automatically uses the new URL.

---

### 2. `config/` — Configuration Management

**What:** `ConfigManager.java` is a singleton that loads environment-specific properties.

**How it works:**
1. Checks for `-Denv=qa` system property
2. Falls back to `ENV` environment variable
3. Defaults to `qa`
4. Loads `config/{env}.properties` (contains `base.url`, `timeout`, etc.)

**Why:** You never hardcode a URL in test code. Same tests run against dev, QA, or prod by just changing one flag: `mvn test -Denv=prod`.

---

### 3. `payloads/` — Request & Response POJOs

**What:** Plain Java objects that map to JSON request/response bodies.

```
payloads/
├── request/
│   ├── PostRequest.java       ← { "title": "...", "body": "...", "userId": 1 }
│   ├── CommentRequest.java    ← { "postId": 1, "name": "...", "email": "...", "body": "..." }
│   └── UserRequest.java       ← { "name": "...", "username": "...", "email": "..." }
└── response/
    ├── PostResponse.java      ← Post response fields
    ├── PostListResponse.java  ← List deserialization helper
    ├── CommentResponse.java   ← Comment response fields
    └── UserResponse.java      ← User response fields
```

**Why separated into request/ and response/:**
- Request bodies and response bodies are often **different shapes** (response has `id`, timestamps, etc.).
- Keeping them separate avoids confusion about which POJO to use where.
- `@JsonIgnoreProperties(ignoreUnknown = true)` on response POJOs means the API can add new fields without breaking your tests.

---

### 4. `utils/` — Reusable Utilities

| Class | Purpose |
|-------|---------|
| `JsonUtils` | Serializes POJOs to JSON strings and vice versa. Wraps Jackson `ObjectMapper`. |
| `ResponseValidator` | Centralized assertions — `assertStatusCode()`, `assertFieldEquals()`, `assertResponseTimeBelow()`, etc. |
| `ExcelReader` | Reads `.xlsx` files and returns `List<Map<String, String>>` — each row becomes a map of column→value. |
| `ScenarioContext` | ThreadLocal storage for sharing state between steps (e.g., "step 1 saves the Response, step 3 reads it"). |

**Why `ResponseValidator` exists instead of asserting inline:**
- Without it, every step definition would repeat: `assertEquals(200, response.getStatusCode())`.
- With it, you call `ResponseValidator.assertStatusCode(response, 200)` — readable, consistent, and if assertion logic changes, you fix one class.

**Why `ScenarioContext` uses ThreadLocal:**
- Cucumber steps are separate methods. Step 1 makes a POST, step 3 checks the response. They need to share data.
- ThreadLocal ensures each test thread has its own state — safe for parallel execution.

---

### 5. `features/` — Gherkin Feature Files

**What:** Plain-English test scenarios using Given/When/Then syntax.

**How they're organized — by CRUD operation:**

**Feature files are organized by resource in subfolders:**

| Folder | Files | Scenarios |
|--------|-------|-----------|
| `posts/` | GetPosts, CreatePost, UpdatePost, DeletePost, E2EPostFlow | 11 |
| `comments/` | GetComments, CreateComment, DeleteComment | 7 |
| `users/` | GetUsers, CreateAndUpdateUsers, DeleteUser | 6 |
| `e2e/` | CrossResourceFlow (User → Post → Comment) | 1 |

**Total: 25 scenarios across 3 resources + 1 cross-resource E2E flow**

**Why subfolders per resource:**
- Easy to find tests — "where are the Comment tests?" → `features/comments/`
- You can run just one resource: `mvn test -Dcucumber.features="src/test/resources/features/comments"`
- Tags like `@smoke`, `@regression`, `@comments`, `@users` let you slice execution further

---

### 6. `stepdefinitions/` — Step Definition Classes

**What:** Java methods annotated with `@Given`, `@When`, `@Then` that map to Gherkin steps.

**How they're organized — by resource/domain:**
- `CommonStepDefinitions.java` — Shared assertions used by all resources (status code, content type, field-not-null checks).
- `PostStepDefinitions.java` — All steps for the **Posts** resource.
- `CommentStepDefinitions.java` — All steps for the **Comments** resource.
- `UserStepDefinitions.java` — All steps for the **Users** resource.

**Why by domain, not by feature file:**
- Multiple feature files reuse the same steps. "I send a GET request to /posts" appears in `GetPosts.feature`, `E2EPostFlow.feature`, etc.
- If you made one step def class per feature file, you'd have **duplicate step methods** → Cucumber throws errors.
- Organizing by domain means each step is defined **once** and reused across all feature files that need it.

**What a step definition does (and doesn't do):**
```
@When("I send a POST request to create a post")
public void createPost() {
    // ✅ Calls the domain client
    Response response = postApiClient.createPost(request);
    // ✅ Stores result for later steps
    scenarioContext.setResponse(response);
    // ❌ Does NOT build HTTP requests itself
    // ❌ Does NOT contain business logic
}
```

**Can a step definition call multiple clients?** Yes. A step like "I place an order for the latest product" might call `productClient.getLatest()` then `orderClient.placeOrder()`. Step definitions are the **orchestration layer**.

---

### 7. `runners/` — Test Runners

**What:** JUnit classes annotated with `@RunWith(Cucumber.class)` + `@CucumberOptions` that tell Cucumber:
- Where to find feature files
- Where to find step definitions (glue code)
- Which tags to include/exclude
- What report formats to generate

**Two runners exist:**
- `TestRunner.java` — runs **all** tests
- `SmokeTestRunner.java` — runs only `@smoke`-tagged scenarios

**Why separate runners:** You typically need different test suites for different pipelines (CI smoke check vs. nightly full regression).

---

### 8. `hooks/` — Setup & Teardown

**What:** `Hooks.java` contains `@Before` and `@After` methods that run around every scenario.

- `@Before` — Logs scenario name, initializes context
- `@After` — Logs pass/fail, attaches response body to report on failure, clears `ScenarioContext`

**Why hooks exist separately from step definitions:**
- Setup/teardown logic is **cross-cutting** — it applies to ALL scenarios regardless of resource.
- Keeping it in its own class prevents cluttering step definitions with infrastructure code.

---

### 9. `testdata/` — Data-Driven Testing

- **`TestDataGenerator.java`** (in `src/test`) — Generates `posts.xlsx` with test data
- **`posts.xlsx`** (in `src/test/resources/testdata/`) — Excel file with sheets of test data

**How it works:** `ExcelReader` reads the Excel file → returns rows as maps → step definitions loop through rows and call the API for each.

**Why Excel instead of hardcoding in feature files:** When you have 50+ test data combinations, putting them all in Gherkin `Examples:` tables makes feature files unreadable. Excel keeps test data external and manageable.

---

## How to Run

### Prerequisites
- Java 17+
- Maven 3.8+

### Generate Test Data (first time only)
```bash
mvn test-compile exec:java -Dexec.mainClass="com.api.automation.testdata.TestDataGenerator" -Dexec.classpathScope=test
```

### Run All Tests
```bash
mvn clean test
```

### Run by Environment
```bash
mvn clean test -Denv=dev
mvn clean test -Denv=qa           # default
mvn clean test -Denv=prod
```

### Run by Tags
```bash
mvn clean test -Dcucumber.filter.tags="@smoke"
mvn clean test -Dcucumber.filter.tags="@regression"
mvn clean test -Dcucumber.filter.tags="@smoke and not @delete"
```

### Run Specific Feature
```bash
mvn clean test -Dcucumber.features="src/test/resources/features/CreatePost.feature"
```

---

## Reports

| Report | Location |
|--------|----------|
| Cucumber HTML | `target/cucumber-reports/cucumber.html` |
| Cucumber JSON | `target/cucumber-reports/cucumber.json` |
| Execution Logs | `target/logs/test-execution.log` |

---

## Adding a New API Resource (e.g., Albums)

1. **POJOs** → Create `AlbumRequest.java` and `AlbumResponse.java` in `payloads/`
2. **Client** → Create `AlbumApiClient.java` in `clients/` with all CRUD methods for `/albums`
3. **Feature folder** → Create `features/albums/` with `GetAlbums.feature`, `CreateAlbum.feature`, etc.
4. **Step definitions** → Create `AlbumStepDefinitions.java` — one class, all Album steps
5. **Done.** Nothing else changes. Runners auto-discover new features and step defs via recursive scanning.

---

## Key Principles Summary

| Principle | How It's Applied |
|-----------|-----------------|
| **One domain client per resource** | All CRUD methods for a resource in one class |
| **One step def class per resource** | All step methods for a resource in one class, reused across features |
| **Step defs only orchestrate** | They call clients and store results — no HTTP logic, no business logic |
| **No hardcoded values** | URLs in config, test data in Excel/Gherkin, assertions via utility methods |
| **Thread-safe state** | `ScenarioContext` uses ThreadLocal for parallel execution |
| **Separation of concerns** | Feature files = what, Step defs = glue, Clients = how, Config = where |
