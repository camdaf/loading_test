import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    // Em desenvolvimento, repassa /api para o back-end Spring Boot
    proxy: { "/api": "http://localhost:8080" },
  },
});
