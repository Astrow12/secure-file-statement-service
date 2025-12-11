# Account Statement Api

API meant to upload and retrieve customer account statements using REST.
## NB
- I am using Supabase for s3 and authentication, so you will need specific roles to upload documents. Please ask for the credentials.
- I set up role based jwt tokens in supabase and added the roles as claims in the jwt token.

## Getting Started

- Install Docker or Rancher Desktop to run the Postgres database.
- To test and build project, run 'mvn build, mvn test and mvn package', mvn package will create the jar.
- Once Docker is running on your PC, run the `docker-compose.yaml` and it will run the database and the service as well
- Access the Swagger UI on your localhost by visiting:  
  `http://localhost:9192/api/swagger-ui/index.html`

---


## Deployment

### PROD Deployment for Enterprise

1. **Docker Setup**
    - Create a `Dockerfile` for the project using Temurin as the base image.
    - Add any necessary certificates for secure communication.

2. **Push to main branch and github pipeline to deploy to Render**
   - (Optional) Render can manage horizontal scaling as well as act as a load balancer.

3. **CI/CD Pipeline**
    - Create GitHub Actions workflows to build, validate, test, and deploy the application.

4. **Kubernetes & ArgoCD**
    - Ensure a Kubernetes cluster is running with proper routing policies, load balancer/API gateway (e.g., Nginx).
    - Configure ArgoCD to monitor the container registry and automatically deploy the latest image to the appropriate namespace.
    - Verify deployment via health checks and confirm that the service is accessible through the Swagger UI URL.

5. **Database Considerations**
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


