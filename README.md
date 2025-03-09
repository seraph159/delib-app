<div align="center">
  <img src="https://i.ibb.co/PGX6gLbv/Pi7-de-Lib.png" width="15%" />
</div>

deLib is a robust and efficient library management system designed to streamline the borrowing and returning of documents while ensuring secure user management and optimized performance. This project leverages modern technologies such as Java, Spring Boot, React.js, Azure, and PostgreSQL to deliver a scalable and reliable experience.

---

## 🌐 Frontend Repository
The frontend code for HotelGenie is located at: [https://github.com/seraph159/delib-app-frontend](https://github.com/seraph159/delib-app-frontend)

---

## 🌟 Features

### 🔐 User Management
- ✅ Secure authentication and authorization using **Spring Security with JWT**.
- 👥 **Role-based access control** for librarians and clients.

### 📖 Document Management
- 📚 **Comprehensive document catalog** with support for multiple document types.
- 🔍 **Advanced search functionality** using keyword, author, and category filters.

### 🔄 Borrowing & Returning
- ⏳ **Efficient document borrowing** and return system with automated due dates.
- ⚠️ **Late fee calculation** for overdue documents.

### ☁️ Cloud Storage Integration
- 📤 **Document file and image uploads** to **Azure Blob Storage** for enhanced reliability.
- 📉 **Reduced database storage** by 40% through external blob storage utilization.

### ⚡ Performance Optimization
- 🚀 **PostgreSQL indexing and caching** strategies for fast query response times.
- ⚡ **Optimized API response times** ensuring an average query time of **less than 500ms**.

---

## 🛠 Technologies Used

### Backend
- **Java**: Core language for backend logic.
- **Spring Boot**: Framework for creating RESTful APIs and managing the backend.
- **Spring Security**: For secure authentication and authorization.
- **Hibernate**: ORM tool for database interactions.

### Database & Cloud
- **PostgreSQL**: Robust and scalable relational database.
- **Azure Blob Storage**: Cloud storage solution for handling document files and images.

### Deployment & DevOps
- **Docker**: Containerization for seamless deployment.
- **Microsoft Azure**: Cloud hosting with continuous integration and delivery pipelines.

---

## 🏁 Getting Started

### ✅ Prerequisites
- Java 17+
- Node.js 18+
- PostgreSQL 14+
- Docker
- Microsoft Azure account (optional for deployment)

### 📥 Installation

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/delib-app.git
   cd delib
   ```

2. **Set up the backend:**
   ```bash
   cd delib-app
   ./mvnw clean install
   ```
   - Configure `src/main/resources/application.properties` for PostgreSQL, Azure Blob Storage, and security credentials.

3. **Run the backend:**
   ```bash
   cd ../delib-app
   ./mvnw spring-boot:run
   ```

4. **Access the application:**
   - Backend APIs: [http://localhost:8080](http://localhost:8080)

---

## 🔑 Mock Login Credentials

### Librarian
- **Username:** `lib@delib.com`
- **Password:** `password123`

### Client
- **Username:** `client@delib.com`
- **Password:** `password123`

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new branch:
   ```bash
   git checkout -b feature-name
   ```
3. Commit your changes:
   ```bash
   git commit -m "Add new feature"
   ```
4. Push the branch:
   ```bash
   git push origin feature-name
   ```
5. Create a pull request.

---

## 📜 License
This project is licensed under the Apache-2.0 License. See the [LICENSE](LICENSE) file for details.

---

## 🙌 Acknowledgments
- **Spring Boot team** for their incredible framework.
- **Azure** for providing scalable cloud solutions.
- **Open-source contributors** for their valuable support.
