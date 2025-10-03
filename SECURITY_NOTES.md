# Spring Security 6 Highlights in Taco Cloud

This guide walks through the key Spring Security concepts demonstrated in this
project after the Spring Boot 3 / Spring 6 migration. It is meant to help students
connect the code in `tacocloud-security` and `tacocloud-domain` with the official
Spring Security reference documentation.

## 1. Declarative Security Configuration

**Reference:** [Spring Security Reference – Configuration](https://docs.spring.io/spring-security/reference/servlet/configuration/java.html)

The legacy `WebSecurityConfigurerAdapter` has been replaced with a
`SecurityFilterChain` bean (`tacocloud-security/src/main/java/tacos/security/SecurityConfig.java`).
This lambda-style configuration is the canonical approach in Spring Security 6.
It allows fine-grained control over:

- Request authorization rules (`authorizeHttpRequests`)
- Login flows (custom login page via `formLogin`)
- HTTP Basic support (`httpBasic`)
- Logout behavior (`logoutSuccessUrl`)

## 2. AuthenticationManager and UserDetailsService

**Reference:** [Authentication Architecture](https://docs.spring.io/spring-security/reference/servlet/authentication/index.html)

`SecurityConfig` defines an `AuthenticationManager` by wiring the shared
`AuthenticationManagerBuilder` with the project’s `UserDetailsService` and
`PasswordEncoder`. This shows how to integrate a custom user store while still
leveraging Spring Security’s authentication pipeline.

- `User` entity (`tacocloud-domain/src/main/java/tacos/User.java`) implements
  `UserDetails`, exposing authorities and account status flags.
- `UserRepositoryUserDetailsService` (from the original project, still present)
  adapts the reactive repository into a `UserDetailsService` expected by Spring
  Security.

## 3. Password Encoding

**Reference:** [Password Storage](https://docs.spring.io/spring-security/reference/features/authentication/password-storage.html)

A `PasswordEncoder` bean is declared (currently `NoOpPasswordEncoder` for
simplicity, but the pattern is exactly the same when swapping in BCrypt or
Argon2 encoders). Demonstrates how the encoder is shared between authentication
and registration (`RegistrationForm#toUser`).

## 4. Request Authorization Rules

**Reference:** [Authorization](https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html)

The configuration uses `authorizeHttpRequests` with request matchers:

- Permits CORS preflight requests (`HttpMethod.OPTIONS`).
- Opens the design/order endpoints (`/design`, `/orders/**`) for convenience.
- Allows PATCH requests to `/ingredients` without authentication.
- Defaults all other requests to `permitAll()` (this mirrors the original
  chapter’s “relaxed” settings, but gives students a place to tighten rules).

## 5. Login, HTTP Basic, Logout

**Reference:** [Form Login](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/form-login.html)

- Custom login page at `/login` using `formLogin().loginPage("/login")`.
- HTTP Basic enabled with a custom realm name (`httpBasic().realmName("Taco Cloud")`).
- Logout success URL resets users to the home page (`logoutSuccessUrl("/")`).

Students can follow the reference about customizing login processing URLs,
remember-me services, or other authentication mechanisms.

## 6. CSRF and Frame Options

**References:**
- [CSRF Protection](https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html)
- [Headers](https://docs.spring.io/spring-security/reference/servlet/headers.html)

For developer convenience, CSRF is disabled on selected endpoints (H2 console,
REST endpoints), and frame options are relaxed to `sameOrigin` so the H2 console
renders inside frames. This mirrors the original book setup and gives students a
starting point for understanding when/why to adjust CSRF enforcement.

## 7. Domain Model Integration

**Reference:** [UserDetailsService](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/dao-authentication-provider.html)

`User` now supplies concrete getters/setters instead of relying on Lombok. This
explicit implementation demonstrates the contract expected by Spring Security:

- `getAuthorities()` returning a collection of Spring Security roles.
- Account status methods (`isAccountNonExpired`, etc.) returning `true` to mark
  the account as active.

Students can extend this class to explore features like account locking or role
management.

## 8. Default Credentials and Registration

`RegistrationForm` creates a new `User` by encoding the raw password provided on
the registration form. This highlights the interplay between form binding,
password encoding, and persistence.

**Reference:** [Custom User Registration](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/user-details-service.html)

## Suggested Exercises

1. **Tighten authorization:** Require authentication for `/design` and `/orders`,
   then verify how the UI behaves.
2. **Swap encoders:** Replace `NoOpPasswordEncoder` with `BCryptPasswordEncoder`
   and update existing user records.
3. **Enable CSRF:** Remove the `csrf().ignoringRequestMatchers(...)` block and
   observe how POST/PUT/PATCH requests must include CSRF tokens.
4. **Add remember-me:** Follow the reference guide to add persistent login
   cookies.

These explorations reinforce the concepts demonstrated in this project while
aligning with Spring Security 6 best practices.
