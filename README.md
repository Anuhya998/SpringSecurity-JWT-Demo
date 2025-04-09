# Introduction to Spring Security

## What is Security?
Security is about restricting access to resources to only trusted users.
Whenever someone tries to access an application, two important questions are asked:

1. **Who are you?** - Authentication (e.g., using a username and password)
2. **What are you allowed to do?** - Authorization (defines what resources a user can access)

---

## Introduction to Spring Security
Spring Security is a powerful and customizable authentication and access-control framework for the Java Spring framework.

By simply adding the Spring Security dependency:
```groovy
implementation 'org.springframework.boot:spring-boot-starter-security'
```
You will immediately see a default login page when accessing APIs.

- Default Username: `user`
- Default Password: Printed in the console at application startup.

## How Authentication Works
- On successful login, a **Session ID** is created and stored in the browser.
- This Session ID is sent with every subsequent request to avoid multiple logins.
- Logging out clears both the browser and server sessions.

## CSRF (Cross-Site Request Forgery)
- **CSRF** attacks steal session IDs to impersonate users.
- Spring Security requires a **CSRF token** for any modifying operations like POST, PUT, DELETE.
- A CSRF token is generated along with the session and must be included in the request headers.

Example header:
```bash
_csrf: <token_value>
```

## Custom Credentials
You can configure custom credentials in the `application.properties` file or define an in-memory user details service.

Example:
```properties
spring.security.user.name=admin
spring.security.user.password=admin123
```

Internally, this uses an `InMemoryUserDetailsManager` bean.

## Spring Security Architecture
Between the client and the API controller, the request passes through several layers:

1. **Filters**: Validate the request before it reaches the DispatcherServlet.
2. **Authentication Filter**: Authenticates user credentials.
3. **Security Context**: Stores user details post-authentication.
4. **Authentication Manager**: Delegates authentication to the appropriate Authentication Provider.
5. **Authentication Provider**:
   - Fetches user details via `UserDetailsService`
   - Validates passwords using `PasswordEncoder`

## Types of Authentication Providers
- **DaoAuthenticationProvider**: Uses a database.
- **LdapAuthenticationProvider**: Connects to an LDAP server.
- **JwtAuthenticationProvider** (Custom): Authenticates via JWT tokens.

## Managing the Security Filter Chain
You can:
- Authorize all requests.
- Exclude certain endpoints (e.g., `/register`, `/login`).
- Disable CSRF (only recommended for APIs with token-based auth).
- Configure OAuth providers.

All of these are managed inside the `SecurityFilterChain` configuration.

## Multiuser Login
To handle multiple users:
1. Override `InMemoryUserDetailsManager` or create a **Custom UserDetailsService**.
2. Create a **Custom UserDetails** class to encapsulate user information.
3. Implement a **User Controller** for login and registration.
4. Encode passwords using `BCryptPasswordEncoder`.
5. Configure **DAOAuthenticationProvider** with your custom service.

## Introduction to JWT (JSON Web Tokens)
JWT is a compact way to securely transmit information between parties.

Benefits:
- Stateless Authentication (no sessions)
- Ideal for Single Sign-On (SSO) scenarios.

**JWT Structure:**
- Header
- Payload (claims)
- Signature

**Typical Flow:**
1. User logs in.
2. Server validates credentials and generates a JWT token.
3. Client stores the token and includes it in Authorization headers for future requests.
4. Server validates the token and grants access.

**Example Authorization Header:**
```bash
Authorization: Bearer <jwt_token>
```

With JWT, a user authenticated in one application (e.g., Facebook) can seamlessly access other applications (e.g., Instagram) under the same organization without re-authenticating.

---

Happy Coding! 🚀
