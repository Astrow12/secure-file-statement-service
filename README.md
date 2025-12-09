# Account Statement Api

API meant to upload and retrieve customer account statements using REST.


## Getting Started

- Install Docker or Rancher Desktop to run the Postgres database.
- Once Docker is running on your PC, run the `docker-compose.yaml` file located in the `src/resource` folder of the project.
- Once the database is running, you can import the collection and its environment (`Statements API.postman_collection.json`, `Statement Api Local Dev.postman_environment.json`) for testing the API, or access the Swagger UI on your localhost by visiting:  
  `http://localhost:9192/api/swagger-ui/index.html`

---

## Deployment

### PROD Deployment for Enterprise

1. **Docker Setup**
    - Create a `Dockerfile` for the project using Temurin as the base image.
    - Add any necessary certificates for secure communication.

2. **Push to main branch and github pipeline to deploy to Render**
   - Render can manage horizontal scaling as well as act as a load balancer.

3. **Add application yaml as application-prod.yaml for PROD configs on render**

4. **CI/CD Pipeline**
    - Create GitHub Actions workflows to build, validate, test, and deploy the application.
    - The deploy action should build a Docker image and push it to a container registry accessible by GitHub.

5. **Kubernetes & ArgoCD**
    - Ensure a Kubernetes cluster is running with proper routing policies, load balancer/API gateway (e.g., Nginx).
    - Configure ArgoCD to monitor the container registry and automatically deploy the latest image to the appropriate namespace.
    - Verify deployment via health checks and confirm that the service is accessible through the Swagger UI URL.

6. **Database Considerations**
    - For testing, you can create a pod using the same Postgres image from `docker-compose.yaml`.

---

### MVP Deployment

1. **Docker Setup**
    - Create a `Dockerfile` for the project.

2. **Database**
    - Use Render (or similar) to create a PostgreSQL database. It’s simpler to setup for MVP purposes.

3. **Application Configuration**
    - Add `application.yaml` linked to PROD configuration for consistency.

---

After following these steps, your Statement API should be deployed and accessible via your API gateway or reverse proxy.


