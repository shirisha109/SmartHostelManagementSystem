package com.smarthostel.service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** In-memory server sessions keep private student routes bound to the logged-in student. */
public final class AuthService {
    public record Session(String role, int studentId, String name) { }
    private static final Map<String, Session> SESSIONS = new ConcurrentHashMap<>();
    private static final SecureRandom RANDOM = new SecureRandom();
    private AuthService() { }
    public static String create(String role, int studentId, String name) { byte[] bytes = new byte[32]; RANDOM.nextBytes(bytes); String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes); SESSIONS.put(token, new Session(role, studentId, name)); return token; }
    public static Session get(String token) { return token == null ? null : SESSIONS.get(token); }
    public static void remove(String token) { if (token != null) SESSIONS.remove(token); }
}
