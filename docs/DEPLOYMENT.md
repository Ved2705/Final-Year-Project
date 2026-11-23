# Nexora Deployment Guide — All-in-One Docker on Render

This guide explains how to deploy **both frontend and backend together as a single Docker Web Service on Render** with **one click**.

---

## 🚀 Why All-in-One Docker?
- **Single Web Service**: Only 1 free Render service slot is consumed.
- **Zero CORS Configuration**: Both React frontend and Spring Boot backend run on the exact same domain and port (e.g. `https://nexora.onrender.com`).
- **Native SPA Routing**: Spring Boot's `WebConfig` automatically serves the React SPA and forwards non-API routes (`/investor/discovery`, etc.) to `index.html`.
- **One-Click Deploy**: Render automatically executes the multi-stage [`Dockerfile`](../Dockerfile) which builds the React bundle, copies it into Spring Boot's static folder, packages the JAR, and runs it on Alpine JRE.

---

## 🛠️ Step 1: Database Setup (Free MySQL)
Render does not offer a free MySQL service, so connect your backend to a free cloud MySQL:
1. Create a free MySQL database on [TiDB Cloud Serverless](https://tidbcloud.com/) or [Aiven](https://aiven.io/).
2. Import the schema and initial data from `migration_dump.sql`:
   ```powershell
   mysql -h <HOST> -P <PORT> -u <USER> -p <DB_NAME> --ssl-mode=REQUIRED < migration_dump.sql
   ```
3. Note your connection string:
   `jdbc:mysql://<HOST>:<PORT>/<DB_NAME>?useSSL=true&allowPublicKeyRetrieval=true`

---

## 📦 Step 2: Push Your Code to GitHub
Ensure all latest changes are committed and pushed to your GitHub repository:
```powershell
git add .
git commit -m "feat: unified single-container docker deployment for Render"
git push origin main
```

---

## 🌐 Step 3: Deploy on Render (One Click)

### Option A: Via Render Blueprint (`render.yaml`) — Recommended
1. In your [Render Dashboard](https://dashboard.render.com/), click **New +** → **Blueprint**.
2. Connect your GitHub repository.
3. Render detects `render.yaml` automatically and configures the single unified web service.
4. Fill in:
   - `SPRING_DATASOURCE_URL`: Your JDBC URL.
   - `SPRING_DATASOURCE_USERNAME`: Your DB user.
   - `SPRING_DATASOURCE_PASSWORD`: Your DB password.
5. Click **Apply**. Render will automatically build and launch the container.

---

### Option B: Manual Web Service Creation
1. Click **New +** → **Web Service**.
2. Connect your GitHub repository.
3. Configure:
   - **Name**: `nexora`
   - **Runtime**: `Docker`
   - **Docker Context**: `.`
   - **Dockerfile Path**: `Dockerfile`
   - **Instance Type**: `Free`
4. Under **Environment Variables**, add:
   - `PORT`: `8080`
   - `SPRING_DATASOURCE_URL`: `jdbc:mysql://<HOST>:<PORT>/<DB_NAME>?useSSL=true&allowPublicKeyRetrieval=true`
   - `SPRING_DATASOURCE_USERNAME`: `<DB_USERNAME>`
   - `SPRING_DATASOURCE_PASSWORD`: `<DB_PASSWORD>`
   - `JWT_SECRET`: *(64-character secret key)*
5. Click **Create Web Service**.

---

## 🎯 Verification
Once deployed, your single Render URL (e.g. `https://nexora.onrender.com`):
- Serves the **React UI** on `/` and any page refresh (`/login`, `/investor/discovery`).
- Serves the **REST APIs** directly on `/api/...`.
